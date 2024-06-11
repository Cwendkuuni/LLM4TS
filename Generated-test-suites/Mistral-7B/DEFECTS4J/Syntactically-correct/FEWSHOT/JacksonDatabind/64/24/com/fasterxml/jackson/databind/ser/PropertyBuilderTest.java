package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);

        // Test case with annotated type
        JsonSerialize annotation = Mockito.mock(JsonSerialize.class);
        Mockito.when(annotation.type()).thenReturn(Boolean.TYPE);
        Mockito.when(am.getAnnotation(JsonSerialize.class)).thenReturn(annotation);
        JavaType secondaryType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertEquals(Boolean.TYPE, secondaryType.getRawClass());

        // Test case with no annotations
        Mockito.when(am.getAnnotation(JsonSerialize.class)).thenReturn(null);
        secondaryType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertNull(secondaryType);

        // Test case with static typing enabled
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder.useStaticTyping = true;
        secondaryType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertEquals(String[].class, secondaryType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = new Object() {
            Object property = "default";
        };
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        Mockito.when(am.getValue(defaultBean)).thenReturn("default");
        Mockito.when(am.getName()).thenReturn("property");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        Object defaultValue = propertyBuilder.getPropertyDefaultValue("property", am, type);
        Assert.assertEquals("default", defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Class<?> intType = Integer.TYPE;
        Object defaultValue = propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(intType));
        Assert.assertEquals(0, defaultValue);

        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        defaultValue = propertyBuilder.getDefaultValue(stringType);
        Assert.assertEquals("", defaultValue);

        JavaType arrayType = TypeFactory.defaultInstance().constructType(int[].class);
        defaultValue = propertyBuilder.getDefaultValue(arrayType);
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);

        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        defaultValue = propertyBuilder.getDefaultValue(mapType);
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);

        JavaType referenceType = TypeFactory.defaultInstance().constructType(MyClass.class);
        defaultValue = propertyBuilder.getDefaultValue(referenceType);
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription();
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<?> serializer = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSerializer = Mockito.mock(TypeSerializer.class);

        BeanPropertyWriter writer = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class),
                Mockito.mock(BeanPropertyDefinition.class), declaredType, serializer, typeSerializer,
                contentTypeSerializer, am, false);

        Assert.assertNotNull(writer);
    }

    private static class MyClass {
    }
}
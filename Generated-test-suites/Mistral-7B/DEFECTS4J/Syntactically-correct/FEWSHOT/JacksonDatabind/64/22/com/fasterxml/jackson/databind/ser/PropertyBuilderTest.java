package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        JavaType javaType = TypeFactory.uncheckedType(String[].class);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getGenericType()).thenReturn(javaType);

        Object defaultBean = new Object() {};

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(
                Mockito.mock(SerializerProvider.class),
                Mockito.mock(BeanPropertyDefinition.class),
                javaType,
                Mockito.mock(JsonSerializer.class),
                TypeSerializers.emptyStringSerializer(),
                null,
                am,
                true
        );

        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);

        Annotations annotations = propertyBuilder.getClassAnnotations();

        assertNotNull(annotations);
        assertTrue(annotations.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
        assertTrue(annotations.isEnabled(MapperFeature.AUTO_DETECT_IS_GETTERS));
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Annotated a = Mockito.mock(Annotated.class);
        when(a.getName()).thenReturn("test");

        JavaType declaredType = TypeFactory.uncheckedType(String.class);

        JavaType serializationType = propertyBuilder.findSerializationType(a, true, declaredType);

        assertEquals(String.class, serializationType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationTypeFailure() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Annotated a = Mockito.mock(Annotated.class);
        when(a.getName()).thenReturn("test");

        JavaType declaredType = TypeFactory.uncheckedType(Integer[].class);

        propertyBuilder.findSerializationType(a, true, declaredType);

        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testGetPropertyDefaultValue() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getValue(Mockito.any())).thenReturn("defaultValue");
        when(am.getName()).thenReturn("testProperty");

        Object defaultBean = new Object() {};

        Object result = propertyBuilder.getPropertyDefaultValue("testProperty", am, TypeFactory.uncheckedType(String.class));

        assertEquals("defaultValue", result);
    }

    @Test
    public void testGetPropertyDefaultValueNull() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);

        Object defaultBean = new Object() {};

        Object result = propertyBuilder.getPropertyDefaultValue("testProperty", am, TypeFactory.uncheckedType(Object.class));

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueFailure() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);

        Object defaultBean = new Object() {};

        doThrow(new RuntimeException("Test Exception")).when(am).getValue(defaultBean);

        propertyBuilder.getPropertyDefaultValue("testProperty", am, TypeFactory.uncheckedType(Object.class));

        fail("Expected IllegalArgumentException to be thrown");
    }

    @Test
    public void testGetDefaultValueInt() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Object result = propertyBuilder.getDefaultValue(TypeFactory.uncheckedType(int.class));

        assertEquals(0, result.intValue());
    }

    @Test
    public void testGetDefaultValueString() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Object result = propertyBuilder.getDefaultValue(TypeFactory.uncheckedType(String.class));

        assertEquals("", result.toString());
    }

    @Test
    public void testGetDefaultValueNull() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Object result = propertyBuilder.getDefaultValue(TypeFactory.uncheckedType(Object.class));

        assertNull(result);
    }

    @Test
    public void testGetDefaultValueComplexType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Object result = propertyBuilder.getDefaultValue(TypeFactory.uncheckedType(Map.class));

        assertEquals(JsonInclude.Include.NON_EMPTY, result);
    }

    @Test
    public void testIsUseRealPropertyDefaults() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        propertyBuilder.setUseRealPropertyDefaults(true);

        assertTrue(propertyBuilder.isUseRealPropertyDefaults());
    }

    @Test
    public void testIsUseRealPropertyDefaultsFalse() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        propertyBuilder.setUseRealPropertyDefaults(false);

        assertFalse(propertyBuilder.isUseRealPropertyDefaults());
    }
}
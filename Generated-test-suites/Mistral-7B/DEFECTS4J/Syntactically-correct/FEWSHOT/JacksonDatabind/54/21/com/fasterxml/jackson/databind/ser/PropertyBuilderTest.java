package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        // Test with null declaredType
        assertNull(propertyBuilder.findSerializationType(a, false, null));

        // Test with no annotations
        when(a.getAnnotation(JsonSerialize.class)).thenReturn(null);
        when(a.getGenericType()).thenReturn(Mockito.mock(Type.class));
        assertEquals(declaredType, propertyBuilder.findSerializationType(a, false, declaredType));

        // Test with JsonInclude annotation on property
        JsonSerialize jsonSerialize = Mockito.mock(JsonSerialize.class);
        when(jsonSerialize.using()).thenReturn(JsonSerialize.Typing.STATIC);
        when(a.getAnnotation(JsonSerialize.class)).thenReturn(jsonSerialize);
        assertEquals(declaredType.withStaticTyping(), propertyBuilder.findSerializationType(a, true, declaredType));

        // Test with JsonInclude annotation on class
        JsonInclude jsonInclude = Mockito.mock(JsonInclude.class);
        when(jsonInclude.value()).thenReturn(JsonInclude.Include.NON_DEFAULT);
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations() {{
            put(JsonInclude.class, jsonInclude);
        }});
        when(a.getAnnotation(JsonInclude.class)).thenReturn(jsonInclude);
        when(a.getGenericType()).thenReturn(Mockito.mock(Type.class));
        assertEquals(null, propertyBuilder.findSerializationType(a, false, declaredType));

        // Test with JsonInclude annotation on class and property
        when(jsonInclude.value()).thenReturn(JsonInclude.Include.NON_DEFAULT);
        when(a.getAnnotation(JsonInclude.class)).thenReturn(jsonInclude);
        when(a.getName()).thenReturn("testProperty");
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations() {{
            put(JsonInclude.class, jsonInclude);
        }});
        when(beanDesc.findPropertyAnnotation(eq("testProperty"), eq(JsonSerialize.class))).thenReturn(jsonSerialize);
        when(jsonSerialize.using()).thenReturn(JsonSerialize.Typing.STATIC);
        assertEquals(declaredType.withStaticTyping(), propertyBuilder.findSerializationType(a, true, declaredType));
    }

    @Test
    public void testGetDefaultBean() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        beanDesc.setBeanClass(Object.class);
        when(beanDesc.instantiateBean(anyBoolean())).thenReturn(new Object());
        defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = new Object();
        String propName = "testProperty";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);

        when(member.getValue(defaultBean)).thenReturn(1);
        assertEquals(1, propertyBuilder.getPropertyDefaultValue(propName, member, type));

        when(member.getValue(defaultBean)).thenThrow(new IllegalAccessException());
        try {
            propertyBuilder.getPropertyDefaultValue(propName, member, type);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to get property 'testProperty' of default Object instance", e.getMessage());
        }

        when(member.getValue(defaultBean)).thenReturn(null);
        assertNull(propertyBuilder.getPropertyDefaultValue(propName, member, type));
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        assertNull(propertyBuilder.getDefaultValue(null));
        assertNull(propertyBuilder.getDefaultValue(String.class));
        assertNull(propertyBuilder.getDefaultValue(int[].class));
        assertNull(propertyBuilder.getDefaultValue(List.class));
        assertNull(propertyBuilder.getDefaultValue(Map.class));
        assertNull(propertyBuilder.getDefaultValue(MyClass.class));

        class MyClass {
            public static int value = 0;
        }

        assertEquals(0, propertyBuilder.getDefaultValue(MyClass.class.getType()));
    }
}
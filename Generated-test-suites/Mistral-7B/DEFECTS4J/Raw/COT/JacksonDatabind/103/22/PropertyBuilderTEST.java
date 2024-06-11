package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private ObjectMapper mapper;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() throws Exception {
        SimpleModule module = new SimpleModule();
        mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.setSerializationConfig(new SerializationConfig(mapper.getSerializationConfig()));

        Class<?> testClass = PropertyBuilder.class;
        Class<?> beanClass = testClass.getDeclaredField("_beanDesc").getType().getDeclaredComponents()[0].getType();
        Object beanInstance = beanClass.newInstance();
        Object defaultBean = PropertyBuilder.NO_DEFAULT_MARKER;

        propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(),
                new BeanDescription(mapper.getIntrospector().findBeanDescription(beanClass)));
        propertyBuilder._defaultBean = defaultBean;
    }

    @Test
    public void testGetClassAnnotations() throws Exception {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter_NullSerializer() throws Exception {
        SerializerProvider prov = mapper.getSerializerProvider();
        JsonSerializer<?> nullSerializer = null;
        try {
            propertyBuilder.buildWriter(prov, null, null, nullSerializer, null, null, null, false);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testBuildWriter_EmptyDeclaredType() throws Exception {
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, null, null, null, null, null, false);
        assertNotNull(writer);
    }

    @Test
    public void testBuildWriter_NotNullSerializer() throws Exception {
        SerializerProvider prov = mapper.getSerializerProvider();
        JsonSerializer<String> stringSerializer = mapper.getSerializerProvider().findValueSerializer(String.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, null, stringSerializer, null, null, null, false);
        assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType_NullAnnotated() throws Exception {
        JavaType declaredType = mapper.getTypeFactory().constructValueType(int[].class, String.class);
        JavaType secondaryType = propertyBuilder.findSerializationType(null, false, declaredType);
        assertNull(secondaryType);
    }

    @Test
    public void testFindSerializationType_ValidAnnotated() throws Exception {
        Class<?> testClass = PropertyBuilder.class;
        Class<?> beanClass = testClass.getDeclaredField("_beanDesc").getType().getDeclaredComponents()[0].getType();
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getGenericType()).thenReturn(mapper.getTypeFactory().constructType(int[].class));
        when(am.getDeclaringClass()).thenReturn(beanClass);
        JavaType declaredType = mapper.getTypeFactory().constructType(int[].class);
        JavaType secondaryType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(int[].class, secondaryType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_InvalidTypeAnnotation() throws Exception {
        Class<?> testClass = PropertyBuilder.class;
        Class<?> beanClass = testClass.getDeclaredField("_beanDesc").getType().getDeclaredComponents()[0].getType();
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getGenericType()).thenReturn(mapper.getTypeFactory().constructType(Long.class));
        when(am.getDeclaringClass()).thenReturn(beanClass);
        JavaType declaredType = mapper.getTypeFactory().constructType(int[].class);
        propertyBuilder.findSerializationType(am, false, declaredType);
    }

    @Test
    public void testGetDefaultBean() throws Exception {
        assertNull(propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue_ValidType() throws Exception {
        Class<?> testClass = PropertyBuilder.class;
        Class<?> beanClass = testClass.getDeclaredField("_beanDesc").getType().getDeclaredComponents()[0].getType();
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("test");
        when(member.getValue(anyObject())).thenReturn(42);
        JavaType type = mapper.getTypeFactory().constructType(Integer.class);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", member, type);
        assertEquals(42, propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_NullDefaultBean() throws Exception {
        Class<?> testClass = PropertyBuilder.class;
        Class<?> beanClass = testClass.getDeclaredField("_beanDesc").getType().getDeclaredComponents()[0].getType();
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("test");
        JavaType type = mapper.getTypeFactory().constructType(Integer.class);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", member, type);
        assertNull(propertyDefaultValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPropertyDefaultValue_InvalidType() throws Exception {
        Class<?> testClass = PropertyBuilder.class;
        Class<?> beanClass = testClass.getDeclaredField("_beanDesc").getType().getDeclaredComponents()[0].getType();
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("test");
        JavaType type = mapper.getTypeFactory().constructType(String.class);
        propertyBuilder.getPropertyDefaultValue("test", member, type);
    }

    @Test
    public void testGetDefaultValue_PrimitiveType() throws Exception {
        Object defaultValue = propertyBuilder.getDefaultValue(mapper.getTypeFactory().constructType(int.class));
        assertEquals(0, defaultValue);
    }

    @Test
    public void testGetDefaultValue_ClassType() throws Exception {
        Object defaultValue = propertyBuilder.getDefaultValue(mapper.getTypeFactory().constructType(String.class));
        assertNull(defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultValue_ArrayType() throws Exception {
        propertyBuilder.getDefaultValue(mapper.getTypeFactory().constructValueType(int[].class, String.class));
    }

    @Test
    public void test_throwWrapped() throws Exception {
        Exception e = new Exception("Test exception");
        String propName = "test";
        Object defaultBean = new Object();
        propertyBuilder._throwWrapped(e, propName, defaultBean);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void test_throwWrapped_NoDefaultBean() throws Exception {
        Exception e = new Exception("Test exception");
        String propName = "test";
        propertyBuilder._throwWrapped(e, propName, null);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void test_throwWrapped_NullDefaultBean() throws Exception {
        Exception e = new Exception("Test exception");
        String propName = "test";
        propertyBuilder._throwWrapped(e, propName, PropertyBuilder.NO_DEFAULT_MARKER);
        fail("Expected IllegalArgumentException");
    }
}
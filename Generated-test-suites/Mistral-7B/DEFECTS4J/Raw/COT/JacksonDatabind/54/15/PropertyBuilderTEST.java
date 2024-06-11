package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriterBuilder;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.test.BaseTest;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test case for PropertyBuilder class.
 */
public class PropertyBuilderTest extends BaseTest {

    // 

    @SuppressWarnings({"unchecked", "rawtypes"})
    @org.junit.Test
    public void testPropertyBuilder() throws Exception {
        Class<?> testClass = PropertyBuilder.class;

        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        BeanDescription beanDesc = new BeanDescription(testClass, config);
        Object defaultBean = new PropertyBuilder(config, beanDesc).getDefaultBean();

        // Test constructor
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);

        // Test getClassAnnotations()
        Annotations classAnnotations = propertyBuilder.getClassAnnotations();
        assertNotNull(classAnnotations);

        // Test buildWriter()
        SerializerProvider prov = createSerializerProvider(config);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", testClass, null, null);
        JavaType declaredType = config.getTypeFactory().constructType(String.class);
        JsonSerializer<?> ser = createMockSerializer(String.class);
        TypeSerializer typeSer = createMockTypeSerializer();
        TypeSerializer contentTypeSer = createMockTypeSerializer();
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(bpw);

        // Test findSerializationType()
        Annotated a = mock(Annotated.class);
        JavaType serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertEquals(declaredType, serializationType);

        // Test getDefaultBean()
        Object defaultBeanValue = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBeanValue);
        assertTrue(defaultBeanValue instanceof Object);

        // Test getPropertyDefaultValue()
        String name = "testProperty";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = config.getTypeFactory().constructType(String.class);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals("", propertyDefaultValue);

        // Test getDefaultValue()
        JavaType typeWithList = config.getTypeFactory().constructType(List.class, String.class);
        Object defaultValue = propertyBuilder.getDefaultValue(typeWithList);
        assertTrue(defaultValue instanceof Object);
        assertTrue(defaultValue instanceof Serializable);
        assertTrue(defaultValue instanceof List);
        assertTrue(defaultValue instanceof java.util.Collections);
        List list = (List) defaultValue;
        assertTrue(list.isEmpty());

        // Test _throwWrapped()
        Exception e = new Exception("Test exception");
        String propName = "testProperty";
        Object defaultBean = new Object();
        Object wrappedException = propertyBuilder._throwWrapped(e, propName, defaultBean);
        assertNotNull(wrappedException);
        assertTrue(wrappedException instanceof IllegalArgumentException);
    }

    // 
}
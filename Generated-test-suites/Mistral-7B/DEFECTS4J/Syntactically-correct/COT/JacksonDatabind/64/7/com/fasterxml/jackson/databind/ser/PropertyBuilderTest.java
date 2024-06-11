package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private SerializationConfig serializationConfig;
    private BeanDescription beanDescription;

    @Before
    public void setUp() {
        serializationConfig = new SerializationConfig();
        beanDescription = new BeanDescription(new ClassInfo(Foo.class));
        propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        // Given
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("name", null, null, null, null, null, null, null);
        JavaType declaredType = TypeFactory.defaultInstance();
        JsonSerializer<?> ser = new JsonSerializer<Object>() {};
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        // Then
        assertNotNull(writer);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithException() throws JsonMappingException {
        // Given
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("name", null, null, null, null, null, null, null);
        JavaType declaredType = null;
        JsonSerializer<?> ser = new JsonSerializer<Object>() {};
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        // Then this exception should be thrown
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        // Given
        Annotated a = new AnnotatedMember("name", null, null, null, null, null, null);
        boolean useStaticTyping = false;
        JavaType declaredType = TypeFactory.defaultInstance();

        // When
        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        // Then
        assertNull(serializationType);

        // Given different serialization type
        useStaticTyping = true;

        // When
        JavaType serializationType2 = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        // Then
        assertNotNull(serializationType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithIllegalArgumentException() throws JsonMappingException {
        // Given
        Annotated a = new AnnotatedMember("name", null, null, null, null, null, null);
        boolean useStaticTyping = true;
        JavaType declaredType = TypeFactory.defaultInstance();

        // When
        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        // Then this exception should be thrown
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();

        assertNull(defaultBean);

        // Given
        propertyBuilder.getDefaultBean(new Foo());

        // When
        Object defaultBean2 = propertyBuilder.getDefaultBean();

        // Then
        assertNotNull(defaultBean2);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // Given
        String name = "name";
        AnnotatedMember member = new AnnotatedMember(name, null, null, null, null, null, null);
        JavaType type = TypeFactory.defaultInstance();

        // When
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);

        // Then
        assertEquals("", defaultValue);

        // Given different type
        type = TypeFactory.constructType(ArrayList.class, Integer.TYPE);

        // When
        Object defaultValue2 = propertyBuilder.getPropertyDefaultValue(name, member, type);

        // Then
        assertNotNull(defaultValue2);
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue2);
    }

    @Test
    public void testGetDefaultValue() {
        // When
        Object defaultValue = propertyBuilder.getDefaultValue(TypeFactory.constructType(int.class));

        // Then
        assertEquals(0, defaultValue);

        // When
        Object defaultValue2 = propertyBuilder.getDefaultValue(TypeFactory.constructType(String.class));

        // Then
        assertEquals("", defaultValue2);

        // When
        Object defaultValue3 = propertyBuilder.getDefaultValue(TypeFactory.constructType(ArrayList.class, Integer.TYPE));

        // Then
        assertNotNull(defaultValue3);
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultValueWithIllegalArgumentException() {
        // When
        Object defaultValue = propertyBuilder.getDefaultValue(null);

        // Then this exception should be thrown
    }

    @Test
    public void test_throwWrapped() {
        // Given
        Exception e = new Exception("Test exception");

        // When
        Object result = propertyBuilder._throwWrapped(e, "propName", null);

        // Then
        fail("Expected IllegalArgumentException but got: " + result);
    }

    private static class Foo {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int i;
    }
}
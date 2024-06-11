package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setUp() {
        config = new SerializationConfig(new AnnotationIntrospector());
        ObjectMapper objectMapper = new ObjectMapper(config);
        beanDesc = objectMapper.constructType(PropertyBuilder.class).getDefaultInstance();
        annotationIntrospector = config.getAnnotationIntrospector();
    }

    // Test for public constructor
    @Test
    public void testPropertyBuilderConstructor() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
    }

    // Test for getClassAnnotations() method
    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    // Test for buildWriter() method with default arguments
    @Test
    public void testBuildWriterWithDefaultArguments() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        assertNotNull(beanPropertyWriter);
    }

    // Test for buildWriter() method with non-null arguments
    @Test
    public void testBuildWriterWithNonNullArguments() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new SimpleType(int.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(beanPropertyWriter);
    }

    // Test for findSerializationType() method with annotations
    @Test
    public void testFindSerializationTypeWithAnnotations() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = mock(Annotated.class);
        JavaType declaredType = new SimpleType(String.class);
        boolean useStaticTyping = true;

        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        assertEquals(String.class, serializationType.getRawClass());
    }

    // Test for findSerializationType() method without annotations
    @Test(expected = JsonMappingException.class)
    public void testFindSerializationTypeWithoutAnnotations() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = mock(Annotated.class);
        JavaType declaredType = new SimpleType(int.class);
        boolean useStaticTyping = true;

        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        fail("Exception expected: JsonMappingException");
    }

    // Test for getDefaultBean() method with default value
    @Test
    public void testGetDefaultBeanWithDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
    }

    // Test for getDefaultBean() method with null value
    @Test
    public void testGetDefaultBeanWithNullValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    // Test for getPropertyDefaultValue() method with default value
    @Test
    public void testGetPropertyDefaultValueWithDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String propName = "propName";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = new SimpleType(String.class);

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);
        assertNull(propertyDefaultValue);
    }

    // Test for getPropertyDefaultValue() method with null value
    @Test(expected = Deprecated.class)
    public void testGetPropertyDefaultValueWithNullValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String propName = "propName";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = new SimpleType(String.class);

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);
        fail("Deprecated exception expected");
    }

    // Test for getDefaultValue() method with String type
    @Test
    public void testGetDefaultValueWithStringType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType type = new SimpleType(String.class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals("", defaultValue);
    }

    // Test for getDefaultValue() method with Integer type
    @Test
    public void testGetDefaultValueWithIntegerType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType type = new SimpleType(int.class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals(0, defaultValue);
    }

    // Test for getDefaultValue() method with custom type
    @Test
    public void testGetDefaultValueWithCustomType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType type = new SimpleType(MyCustomClass.class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }
}

class MyCustomClass {
}
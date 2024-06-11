package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        this.config = mapper.getSerializationConfig();
        this.beanDesc = mapper.findBeanDescription(MyPojo.class);
        this.annotationIntrospector = config.getAnnotationIntrospector();
        this.propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    

    // Test for getClassAnnotations() method
    @Test
    public void testGetClassAnnotations() {
        assertEquals(getClassAnnotations(), propertyBuilder.getClassAnnotations());
    }

    // Test for buildWriter() method with normal scenario
    @Test
    public void testBuildWriterNormalScenario() throws JsonMappingException {
        BeanPropertyWriter builder = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        assertNotNull(builder);
    }

    // Test for buildWriter() method with exception handling
    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithException() throws JsonMappingException {
        propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
    }

    // Test for findSerializationType() method with normal scenario
    @Test
    public void testFindSerializationTypeNormalScenario() throws JsonMappingException {
        JavaType serializationType = propertyBuilder.findSerializationType(null, false, null);
        assertNotNull(serializationType);
    }

    // Test for findSerializationType() method with exception handling
    @Test(expected = JsonMappingException.class)
    public void testFindSerializationTypeWithException() throws JsonMappingException {
        propertyBuilder.findSerializationType(null, false, null);
    }

    // Test for getDefaultBean() method with normal scenario
    @Test
    public void testGetDefaultBeanNormalScenario() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
    }

    // Test for getDefaultBean() method with null return
    @Test
    public void testGetDefaultBeanNullReturn() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getDefaultBean());
    }

    // Test for getPropertyDefaultValue() method with normal scenario
    @Test
    public void testGetPropertyDefaultValueNormalScenario() {
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", null, Object.class);
        assertNotNull(propertyDefaultValue);
    }

    // Test for getPropertyDefaultValue() method with null return
    @Test
    public void testGetPropertyDefaultValueNullReturn() {
        assertNull(propertyBuilder.getPropertyDefaultValue("test", null, Object.class));
    }

    // Test for getDefaultValue() method with normal scenario
    @Test
    public void testGetDefaultValueNormalScenario() {
        Object defaultValue = propertyBuilder.getDefaultValue(Object.class);
        assertNotNull(defaultValue);
    }

    // Test for getDefaultValue() method with null return
    @Test
    public void testGetDefaultValueNullReturn() {
        assertNull(propertyBuilder.getDefaultValue(Object.class));
    }

    // Test for _throwWrapped() method with IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrappedIllegalArgumentException() {
        propertyBuilder._throwWrapped(new IllegalArgumentException("Test"), "test", null);
    }

    // Test for _throwWrapped() method with Error
    @Test
    public void testThrowWrappedError() {
        propertyBuilder._throwWrapped(new Error("Test"), "test", null);
        fail("_throwWrapped() should have thrown an IllegalArgumentException");
    }

    // Test for _throwWrapped() method with RuntimeException
    @Test
    public void testThrowWrappedRuntimeException() {
        propertyBuilder._throwWrapped(new RuntimeException("Test"), "test", null);
        fail("_throwWrapped() should have thrown an IllegalArgumentException");
    }

    

    private Annotations getClassAnnotations() {
        return beanDesc.getClassAnnotations();
    }

    private static class MyPojo {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int id;
    }
}
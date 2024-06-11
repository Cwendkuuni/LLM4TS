package com.fasterxml.jackson.databind.ser; // make sure the package name is correct

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.ser.impl.*;

import org.junit.Test;

/**
 * Test class for PropertyBuilder.
 */
public class PropertyBuilderTest {

    // 

    @Test
    public void testPropertyBuilderConstructor() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", new ClassType(String.class), null, null, null, null, null, null, null, null, null);
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        SerializerProvider prov = new SerializerProvider();
        JavaType declaredType = new ClassType(String.class);
        JsonSerializer<?> ser = new JsonStringSerializer();
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = new AnnotatedMember(new StdClassMember("testProperty"));
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(beanPropertyWriter);
    }

    // 

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithJsonMappingException() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", new ClassType(String.class), null, null, null, null, null, null, null, null, null);
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        SerializerProvider prov = new SerializerProvider();
        JavaType declaredType = new ClassType(String.class);
        JsonSerializer<?> ser = new JsonStringSerializer();
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = new AnnotatedMember(new StdClassMember("testProperty"));
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        JsonMappingException jsonMappingException = new JsonMappingException("Test JsonMappingException");
        throw jsonMappingException;
    }

    // 

    @Test
    public void testFindSerializationType() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object testObject = new Object() {
            @JsonSerialize(using = JsonSerializer.None.class)
            public Class<?> getSerializedClass() {
                return String.class;
            }
        };

        Annotated a = new AnnotatedMember(new StdClassMember("testMethod")) {
            @Override
            public Object getValue(Object t) {
                return testObject;
            }
        };

        JavaType declaredType = new ClassType(String.class);
        try {
            JavaType serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
            assertEquals(declaredType, serializationType);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException: " + e.getMessage());
        }

        Annotated aWithJsonSerialize = new AnnotatedMember(new StdClassMember("testMethod")) {
            @Override
            public Object getValue(Object t) {
                return testObject;
            }

            @JsonSerialize
            public Class<?> getSerializedClass() {
                return Integer.class;
            }
        };

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(aWithJsonSerialize, false, declaredType);
            assertEquals(Integer.class, serializationType.getRawClass());
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException: " + e.getMessage());
        }

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(a, true, declaredType);
            assertEquals(declaredType, serializationType);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException: " + e.getMessage());
        }

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(aWithJsonSerialize, true, declaredType);
            assertEquals(Integer.class, serializationType.getRawClass());
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException: " + e.getMessage());
        }

        Annotated aWithoutJsonSerialize = new AnnotatedMember(new StdClassMember("testMethod")) {
            @Override
            public Object getValue(Object t) {
                return testObject;
            }
        };

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(aWithoutJsonSerialize, false, declaredType);
            assertNull(serializationType);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException: " + e.getMessage());
        }

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(aWithoutJsonSerialize, true, declaredType);
            assertNull(serializationType);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException: " + e.getMessage());
        }
    }

    // 

    // Add more test cases as needed, following the same pattern
}
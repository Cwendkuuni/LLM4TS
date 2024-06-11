package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link PropertyBuilder} class.
 */
public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        this.config = mapper.getSerializationConfig();
        this.beanDesc = mapper.findBeanDescription(Object[].class);
    }

    @Test
    public void testPropertyBuilder_Constructor() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations classAnnotations = propertyBuilder.getClassAnnotations();
        assertNotNull(classAnnotations);
    }

    @Test
    public void testBuildWriter_Default() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        ObjectWriter objectWriter = config.getObjectWriter();
        ObjectWriter.DefaultTyping defaultTyping = ObjectWriter.DefaultTyping.NON_FINAL;

        Object value = new Object[] {1, 2, 3};
        JsonSerializer<Object> jsonSerializer = new StandardSerializer<Object>(Object[].class);
        BeanPropertyWriter expectedBeanPropertyWriter = new BeanPropertyWriter(
                null, null, null, Object[].class, jsonSerializer, null, null, false, null);

        BeanPropertyWriter actualBeanPropertyWriter = propertyBuilder.buildWriter(
                null, null, JavaType.constructCollectionType(ArrayType.class, Object.class), jsonSerializer,
                null, null, null, defaultTyping);

        assertEquals(expectedBeanPropertyWriter, actualBeanPropertyWriter);
    }

    @Test
    public void testBuildWriter_WithContentTypeSerializer() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        ObjectWriter objectWriter = config.getObjectWriter();
        ObjectWriter.DefaultTyping defaultTyping = ObjectWriter.DefaultTyping.NON_FINAL;

        Object value = new Object[] {1, 2, 3};
        JsonSerializer<Object> jsonSerializer = new StandardSerializer<Object>(Object[].class);
        TypeSerializer contentTypeSerializer = new StdSerializer<IntType>(IntType.class);

        JavaType javaType = JavaType.constructCollectionType(ArrayType.class, Object.class);

        ObjectWriter.DefaultTyping serializationTypeDefaultTyping = ObjectWriter.DefaultTyping.STATIC;
        JavaType serializationType = JavaType.constructCollectionType(ArrayType.class, Int.class);

        AnnotatedMember am = null;
        BeanPropertyWriter expectedBeanPropertyWriter = new BeanPropertyWriter(
                null, null, null, Object[].class, jsonSerializer, null, serializationType, false, null);

        BeanPropertyWriter actualBeanPropertyWriter = propertyBuilder.buildWriter(
                null, null, javaType, jsonSerializer, null, contentTypeSerializer, null, defaultTyping);

        assertEquals(expectedBeanPropertyWriter, actualBeanPropertyWriter);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationType_IllegalConcreteTypeAnnotation() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = null;
        JavaType declaredType = JavaType.constructType(Long.class);
        throw propertyBuilder.findSerializationType(a, false, declaredType);
    }

    @Test
    public void testGetPropertyDefaultValue_DefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertEquals(null, defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue_StringType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        if (defaultBean != null) {
            String defaultValue = propertyBuilder.getPropertyDefaultValue("test", null, String.class);
            assertEquals("", defaultValue);
        }
    }

    @Test
    public void testGetPropertyDefaultValue_ArrayType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        if (defaultBean != null) {
            Object[] defaultValue = propertyBuilder.getPropertyDefaultValue("test", null, Object[].class);
            assertArrayEquals(new Object[]{null}, defaultValue);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_Null() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String propName = "test";
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(propName, null, String.class);
        assertNull(defaultValue);

        throw propertyBuilder._throwWrapped(new IllegalArgumentException("Failed to get property 'test' of default null instance"), propName, defaultBean);
    }
}
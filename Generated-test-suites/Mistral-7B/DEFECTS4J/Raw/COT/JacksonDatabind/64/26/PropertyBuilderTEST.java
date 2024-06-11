package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter.MarkerForEmpty;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.SetupContext;
import com.fasterxml.jackson.databind.util.TypeFactory;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedMember;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;
    private SerializationConfig config;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        ObjectMapper.Init context = new ObjectMapper.Init();
        context.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper = context.build();
        config = objectMapper.getSerializationConfig();
    }

    @Test
    public void testPropertyBuilderConstruction() {
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDesc = new BeanDescription();
        beanDesc.getClassAnnotations().add(new JsonInclude(JsonInclude.Include.NON_EMPTY));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertEquals(1, annotations.size());
    }

    @Test
    public void testBuildWriterWithNullDeclaredType() {
        BeanDescription beanDesc = new BeanDescription();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", int.class, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        try {
            BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, false);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testFindSerializationTypeWithNullAnnotatedMember() {
        BeanDescription beanDesc = new BeanDescription();
        JavaType declaredType = TypeFactory.defaultInstance().constructType(int[].class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        try {
            JavaType serializationType = propertyBuilder.findSerializationType(null, false, declaredType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testFindSerializationTypeWithNullDeclaredType() {
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        try {
            JavaType serializationType = propertyBuilder.findSerializationType(null, false, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testGetPropertyDefaultValueWithNullName() {
        BeanDescription beanDesc = new BeanDescription();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", int.class, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        try {
            Object defaultValue = propertyBuilder.getPropertyDefaultValue(null, null, TypeFactory.defaultInstance().constructType(int.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetPropertyDefaultValueWithNonExistentProperty() {
        BeanDescription beanDesc = new BeanDescription();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", int.class, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = new Object() {};
        try {
            Object defaultValue = propertyBuilder.getPropertyDefaultValue("nonExistentProperty", null, TypeFactory.defaultInstance().constructType(int.class), propDef, propertyBuilder);
            assertNull(defaultValue);
        } catch (Exception e) {
            fail("Unexpected exception: "+e.getMessage());
        }
    }

    @Test
    public void testGetPropertyDefaultValueWithIntProperty() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", int.class, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = new Object() {
            int test = 1;
        };
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("test", null, TypeFactory.defaultInstance().constructType(int.class), propDef, propertyBuilder);
        assertEquals(1, defaultValue.intValue());
    }

    @Test
    public void testGetPropertyDefaultValueWithStringProperty() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", String.class, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = new Object() {
            String test = "test";
        };
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("test", null, TypeFactory.defaultInstance().constructType(String.class), propDef, propertyBuilder);
        assertEquals("test", defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueWithCollectionProperty() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", List.class, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = new Object() {
            List<Integer> test = Arrays.asList(1, 2, 3);
        };
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("test", null, TypeFactory.defaultInstance().constructType(List.class), propDef, propertyBuilder);
        assertEquals(Arrays.asList(1, 2, 3), defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueWithArrayProperty() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", int[].class, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = new Object() {
            int[] test = new int[] {1, 2, 3};
        };
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("test", null, TypeFactory.defaultInstance().constructType(int[].class), propDef, propertyBuilder);
        assertArrayEquals(new int[] {1, 2, 3}, (int[]) defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueWithNullDefaultBean() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", int.class, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = null;
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("test", null, TypeFactory.defaultInstance().constructType(int.class), propDef, propertyBuilder);
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValueWithPrimitiveType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        Object defaultValue = propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(int.class));
        assertEquals(0, defaultValue);
    }

    @Test
    public void testGetDefaultValueWithWrapperType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        Object defaultValue = propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(Integer.class));
        assertEquals(0, defaultValue);
    }

    @Test
    public void testGetDefaultValueWithCollectionType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        Object defaultValue = propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(List.class));
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testGetDefaultValueWithReferenceType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        Object defaultValue = propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(String.class));
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testGetDefaultValueWithObjectType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        Object defaultValue = propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(Object.class));
        assertNull(defaultValue);
    }

    @Test
    public void testGetMarkerForEmpty() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        Object markerForEmpty = propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(MarkerForEmpty.class));
        assertEquals(MarkerForEmpty.class, markerForEmpty.getClass());
    }
}
package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_EMPTY_JSON_ARRAYS;

public class PropertyBuilderTest {

    private static final Object NO_DEFAULT_MARKER = Boolean.FALSE;
    private static final SerializationConfig config;
    private static final BeanDescription beanDesc;

    static {
        config = new SerializationConfig();
        config.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        config.setDefaultSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        config.setSerializationFeature(WRITE_EMPTY_JSON_ARRAYS, false);
        config.setAnnotationIntrospector(new StandardAnnotationIntrospector());

        beanDesc = new BeanDescription(Object.class);
    }

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    // Test getClassAnnotations()
    @Test
    public void testGetClassAnnotations() {
        Assert.assertEquals(beanDesc.getClassAnnotations(), propertyBuilder.getClassAnnotations());
    }

    // Test buildWriter() with default parameters
    @Test
    public void testBuildWriterWithDefaultParameters() throws JsonMappingException {
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        Assert.assertNotNull(writer);
    }

    // Test buildWriter() with custom TypeSerializer
    @Test
    public void testBuildWriterWithCustomTypeSerializer() throws JsonMappingException {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("contentTypeSer", new TypeSerializerInstance(new SimpleTypeSerializer()));

        BeanDescription beanDescWithCustomTypeSerializer = new BeanDescription(Object.class);
        beanDescWithCustomTypeSerializer.setSerializerByContext(BeanSerializerFactory.serializeWithType(node));

        PropertyBuilder propertyBuilderWithCustomTypeSerializer = new PropertyBuilder(config, beanDescWithCustomTypeSerializer);
        BeanPropertyWriter writer = propertyBuilderWithCustomTypeSerializer.buildWriter(null, null, null, null, null, node, null, false);
        Assert.assertNotNull(writer);
    }

    // Test findSerializationType() with no annotations
    @Test(expected = JsonMappingException.class)
    public void testFindSerializationTypeWithNoAnnotations() throws JsonMappingException {
        propertyBuilder.findSerializationType(null, false, Object.class);
    }

    // Test findSerializationType() with a JsonSerialize annotation
    @JsonSerialize(using = CustomSerializer.class)
    static class TestClassWithJsonSerializeAnnotation {
        // Empty class with a JsonSerialize annotation
    }

    @Test
    public void testFindSerializationTypeWithJsonSerializeAnnotation() throws JsonMappingException {
        AnnotatedMember member = BeanDescription.findAnnotation(TestClassWithJsonSerializeAnnotation.class, "class");
        JavaType serializationType = propertyBuilder.findSerializationType(member, false, Object.class);
        Assert.assertEquals(CustomSerializer.class, serializationType.getRawClass());
    }

    // Test getDefaultBean()
    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);
    }

    // Test getPropertyDefaultValue() with a non-null default value
    @Test
    public void testGetPropertyDefaultValueWithNonNullDefaultValue() {
        BeanDescription beanDescWithDefaultValue = new BeanDescription(DefaultValueClass.class);
        PropertyBuilder propertyBuilderWithDefaultValue = new PropertyBuilder(config, beanDescWithDefaultValue);
        Object defaultBean = propertyBuilderWithDefaultValue.getDefaultBean();
        Assert.assertNotNull(defaultBean);
        Object propertyDefaultValue = propertyBuilderWithDefaultValue.getPropertyDefaultValue("defaultValue", null, DefaultValueClass.class);
        Assert.assertEquals(defaultBean, propertyDefaultValue);
    }

    // Test getPropertyDefaultValue() with a null default value
    @Test
    public void testGetPropertyDefaultValueWithNullDefaultValue() {
        BeanDescription beanDescWithNullDefaultValue = new BeanDescription(NullDefaultValueClass.class);
        PropertyBuilder propertyBuilderWithNullDefaultValue = new PropertyBuilder(config, beanDescWithNullDefaultValue);
        Object defaultBean = propertyBuilderWithNullDefaultValue.getDefaultBean();
        Assert.assertNull(defaultBean);
        Object propertyDefaultValue = propertyBuilderWithNullDefaultValue.getPropertyDefaultValue("nullDefaultValue", null, NullDefaultValueClass.class);
        Assert.assertNull(propertyDefaultValue);
    }

    // Test getDefaultValue() with primitive types
    @Test
    public void testGetDefaultValueWithPrimitiveTypes() {
        Assert.assertEquals(0, propertyBuilder.getDefaultValue(Integer.TYPE));
        Assert.assertEquals(0, propertyBuilder.getDefaultValue(Integer.class));
        Assert.assertEquals(0L, propertyBuilder.getDefaultValue(Long.TYPE));
        Assert.assertEquals(0L, propertyBuilder.getDefaultValue(Long.class));
        Assert.assertEquals("", propertyBuilder.getDefaultValue(String.class));
    }

    // Test getDefaultValue() with non-primitive types
    @Test
    public void testGetDefaultValueWithNonPrimitiveTypes() {
        Assert.assertNotNull(propertyBuilder.getDefaultValue(new SimpleType(Object.class)));
        Assert.assertNotNull(propertyBuilder.getDefaultValue(new SimpleModule().addSerializer(String.class, new StringSerializer()).build()));
        Assert.assertNotNull(propertyBuilder.getDefaultValue(new TypeFactory().constructCollectionType(List.class, String.class)));
    }

    // Test getDefaultValue() with container types
    @Test
    public void testGetDefaultValueWithContainerTypes() {
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(new TypeFactory().constructType(List.class)));
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(new TypeFactory().constructType(Map.class)));
    }

    // Test getDefaultValue() with reference types
    @Test
    public void testGetDefaultValueWithReferenceTypes() {
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(String[].class));
    }

    // Test _throwWrapped()
    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrapped() throws JsonMappingException {
        propertyBuilder._throwWrapped(new RuntimeException(), "propName", new Object());
    }
}

class CustomSerializer extends JsonSerializer<Object> {
    // Empty custom serializer for testing purposes
}

class DefaultValueClass {
    private String defaultValue;

    public String getDefaultValue() {
        return "defaultValue";
    }
}

class NullDefaultValueClass {
    // Empty class with no default value
}

class StringSerializer extends JsonSerializer<String> {
    // Empty serializer for testing purposes
}
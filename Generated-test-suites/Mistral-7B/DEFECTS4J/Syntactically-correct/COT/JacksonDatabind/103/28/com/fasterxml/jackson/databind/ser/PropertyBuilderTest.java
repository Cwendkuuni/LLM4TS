package com.fasterxml.jackson.databind.ser; // make sure the package is correct

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private SerializationConfig _config;
    private BeanDescription _beanDesc;
    private AnnotationIntrospector _annotationIntrospector;
    private PropertyBuilder _propertyBuilder;

    @Before
    public void setUp() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        _config = mapper.getSerializationConfig();
        _beanDesc = mapper.findBeanDescription(String[].class);
        _annotationIntrospector = _config.getAnnotationIntrospector();
    }

    // Test for constructor
    @Test
    public void testPropertyBuilderConstructor() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        assertNotNull(propertyBuilder);
    }

    // Test for getClassAnnotations() method
    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = _propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    // Test for buildWriter() method with simple value type
    @Test
    public void testBuildWriterSimpleValueType() throws JsonMappingException, JsonProcessingException {
        BeanPropertyWriter writer = _propertyBuilder.buildWriter(null, null, JsonTypeFactory.valueType(int.class), null, null, null, null, true);
        assertNotNull(writer);
    }

    // Test for buildWriter() method with array type
    @Test
    public void testBuildWriterArrayType() throws JsonMappingException, JsonProcessingException {
        BeanPropertyWriter writer = _propertyBuilder.buildWriter(null, null, JsonTypeFactory.valueType(int[].class), null, null, null, null, true);
        assertNotNull(writer);
    }

    // Test for buildWriter() method with container type
    @Test
    public void testBuildWriterContainerType() throws JsonMappingException, JsonProcessingException {
        BeanPropertyWriter writer = _propertyBuilder.buildWriter(null, null, JsonTypeFactory.valueType(List.class), null, null, null, null, true);
        assertNotNull(writer);
    }

    // Test for buildWriter() method with custom null serializer
    @JsonSerialize(nulls = SerializationFeature.Custom)
    static class CustomNullSerializer implements JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeString("NULL_VALUE");
            } else {
                gen.writeObject(value);
            }
        }
    }

    // Test for buildWriter() method with custom null serializer
    @Test
    public void testBuildWriterCustomNullSerializer() throws JsonMappingException, JsonProcessingException {
        SerializerProvider serializers = new StandardSerializerProvider();
        SerializerProvider.Base serializerBase = serializers.findSerializer(String.class);
        serializerBase.setNullSerializer(new CustomNullSerializer());
        BeanPropertyWriter writer = _propertyBuilder.buildWriter(serializers, null, JsonTypeFactory.valueType(String.class), null, null, null, null, true);
        assertNotNull(writer);
    }

    // Test for findSerializationType() method with no annotations
    @Test(expected = JsonMappingException.class)
    public void testFindSerializationTypeNoAnnotations() throws JsonMappingException {
        _propertyBuilder.findSerializationType(null, false, JsonTypeFactory.valueType(String.class));
    }

    // Test for findSerializationType() method with annotations
    @Test
    public void testFindSerializationTypeWithAnnotations() throws JsonMappingException {
        JavaType secondary = _annotationIntrospector.refineSerializationType(_config, null, JsonTypeFactory.valueType(String[].class));
        assertEquals(JsonTypeFactory.valueType(String[].class), secondary);
    }

    // Test for getDefaultBean() method
    @Test
    public void testGetDefaultBean() {
        Object defaultBean = _propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    // Test for getPropertyDefaultValue() method with simple value type
    @Test
    public void testGetPropertyDefaultValueSimpleValueType() {
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = _propertyBuilder.getDefaultBean();
        if (defaultBean != null) {
            Object defaultValue = _propertyBuilder.getPropertyDefaultValue("testProperty", null, JsonTypeFactory.valueType(int.class));
            assertEquals(0, defaultValue);
        }
    }

    // Test for getPropertyDefaultValue() method with array type
    @Test
    public void testGetPropertyDefaultValueArrayType() {
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = _propertyBuilder.getDefaultBean();
        if (defaultBean != null) {
            Object[] defaultValue = (Object[]) _propertyBuilder.getPropertyDefaultValue("testProperty", null, JsonTypeFactory.valueType(int[].class));
            assertArrayEquals(new int[0], defaultValue);
        }
    }

    // Test for getDefaultValue() method with simple value type
    @Test
    public void testGetDefaultValueSimpleValueType() {
        Object defaultValue = _propertyBuilder.getDefaultValue(JsonTypeFactory.valueType(int.class));
        assertEquals(0, defaultValue);
    }

    // Test for getDefaultValue() method with array type
    @Test
    public void testGetDefaultValueArrayType() {
        Object defaultValue = _propertyBuilder.getDefaultValue(JsonTypeFactory.valueType(int[].class));
        assertArrayEquals(new int[0], defaultValue);
    }

    // Test for _throwWrapped() method
    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrappedIllegalArgumentException() {
        _propertyBuilder. _throwWrapped(new IllegalArgumentException("Test exception"), "testProperty", null);
    }
}
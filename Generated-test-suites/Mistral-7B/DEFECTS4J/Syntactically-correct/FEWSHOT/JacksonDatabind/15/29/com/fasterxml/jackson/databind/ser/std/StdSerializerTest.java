package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {

    @Test
    public void testConstructors() {
        Class<Object> handledType = Object.class;
        StdSerializer<Object> serializer1 = new StdSerializer<>(handledType);
        StdSerializer<Object> serializer2 = new StdSerializer<>(JsonTypeFactory.instance.constructType(handledType));
        StdSerializer<Object> serializer3 = new StdSerializer<>(handledType, false);
        assertEquals(handledType, serializer1.handledType());
        assertEquals(handledType, serializer2.handledType());
        assertEquals(handledType, serializer3.handledType());
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        ObjectNode result = new ObjectMapper().writeValueAsObject(null);
        ObjectNode expected = serializer.createSchemaNode("null");
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeDefault() throws JsonProcessingException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        ObjectNode result = new ObjectMapper().writeValueAsObject("foo");
        ObjectNode expected = serializer.createSchemaNode("string");
        assertEquals(expected, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.hasHandledAnyFormat());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        ObjectNode result = serializer.getSchema(new SerializerProvider(), null);
        ObjectNode expected = serializer.createSchemaNode("string");
        assertEquals(expected, result);
    }

    @Test
    public void testGetSchemaOptional() throws JsonMappingException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        ObjectNode result = serializer.getSchema(new SerializerProvider(), Boolean.FALSE);
        ObjectNode expected = serializer.createSchemaNode("string", Boolean.FALSE);
        assertEquals(expected, result);
    }

    @JsonSerialize(using = ToBigDecimalSerializer.class)
    static class TestBean {
        private BigDecimal value;
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        BeanProperty property = BeanProperty.stdProperty("value", TestBean.class);
        StdSerializer<TestBean> serializer = new StdSerializer<>(TestBean.class);
        ObjectNode result = mapper.writeValueAsObject(new TestBean());
        JsonSerializer<BigDecimal> existingSerializer = provider.findValueSerializer(JsonTypeFactory.instance.constructType(BigDecimal.class));
        JsonSerializer<Object> serializerWithConverter = serializer.findConvertingContentSerializer(provider, property, existingSerializer);
        ObjectNode expected = result;
        expected.set("value", serializerWithConverter.createSchemaNode("number"));
        assertEquals(expected, result);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        PropertyFilter filter = new FilterProvider().findPropertyFilter("testFilter", "testValue");
        ObjectNode result = mapper.writeValueAsObject("testValue");
        ObjectNode expected = result;
        if (filter != null) {
            expected = filter.serializeAsSchema(provider, JsonTypeFactory.instance.constructType(Object.class), result);
        }
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterNullFilterProvider() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.writeValueAsObject("testValue");
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        serializer.findPropertyFilter(null, "testFilter", "testValue");
    }
}
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class StdSerializerTest {

    private StdSerializer<String> stdSerializer;

    @Before
    public void setup() {
        stdSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                // do nothing
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return createSchemaNode("string");
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                visitor.expectAnyFormat(typeHint);
            }

            @Override
            protected ObjectNode createObjectNode() {
                return new ObjectNode(JsonNodeFactory.instance);
            }
        };
    }

    @Test
    public void testHandledType() {
        Assert.assertEquals(String.class, stdSerializer.handledType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSerialize() throws IOException {
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        String value = "test value";
        stdSerializer.serialize(value, jsonGenerator, serializerProvider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);
        JsonNode schema = stdSerializer.getSchema(serializerProvider, typeHint);
        Assert.assertNotNull(schema);
        Assert.assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        stdSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor, Mockito.times(1)).expectAnyFormat(typeHint);
    }

    @Test
    public void testCreateObjectNode() {
        ObjectNode objectNode = stdSerializer.createObjectNode();
        Assert.assertNotNull(objectNode);
    }

    private ObjectNode createSchemaNode(String type) {
        ObjectNode schema = stdSerializer.createObjectNode();
        schema.put("type", type);
        return schema;
    }

}
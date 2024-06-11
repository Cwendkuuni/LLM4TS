package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.mockito.Mockito;

public class StdSerializerTest {
    @Test
    public void testHandledType() {
        StdSerializer<Object> serializer = new CustomSerializer<>(Object.class);
        assertEquals(Object.class, serializer.handledType());
    }

    @Test
    public void testSerialize() throws IOException, JsonGenerationException {
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator jgen = mock(JsonGenerator.class);
        CustomSerializer<Object> serializer = new CustomSerializer<>(Object.class);
        serializer.serialize(null, jgen, provider);

        Mockito.verify(jgen).writeNull();
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        CustomSerializer<Object> serializer = new CustomSerializer<>(Object.class);
        Type typeHint = mock(Type.class);
        JsonNode schema = serializer.getSchema(provider, typeHint);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        CustomSerializer<Object> serializer = new CustomSerializer<>(Object.class);
        JavaType typeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(visitor).expectAnyFormat(typeHint);
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        StdSerializer<Object> serializer = new CustomSerializer<>(Object.class);
        Throwable exception = new Throwable();
        try {
            serializer.wrapAndThrow(provider, exception, "test", 1);
        } catch (Exception e) {
            assertEquals(JsonMappingException.class, e.getClass());
        }
    }

    private static class CustomSerializer<T> extends StdSerializer<T> {
        public CustomSerializer(Class<T> t) {
            super(t);
        }

        @Override
        public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeNull();
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
            ObjectNode schema = createSchemaNode("string");
            return schema;
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            visitor.expectAnyFormat(typeHint);
        }
    }
}
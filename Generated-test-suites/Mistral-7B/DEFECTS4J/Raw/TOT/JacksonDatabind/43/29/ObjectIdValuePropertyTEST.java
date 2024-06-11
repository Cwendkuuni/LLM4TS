package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReaderForString;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReaderForUuid;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReaderForString;
    private ObjectIdReader objectIdReaderForUuid;
    private ObjectIdValueProperty property;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        ObjectIdReader objectIdReader = new ObjectIdReaderForString();
        objectIdReaderForString = (ObjectIdReader) objectIdReader;
        objectIdReaderForString.setIdTypeClass(String.class);

        ObjectIdReader objectIdReaderUuid = new ObjectIdReaderForUuid();
        objectIdReaderForUuid = (ObjectIdReader) objectIdReaderUuid;
        objectIdReaderForUuid.setIdTypeClass(java.util.UUID.class);

        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        property = new ObjectIdValueProperty(objectIdReaderForString,
                new PropertyMetadata(null, null, null, null, null, null));
        property.setValueDeserializer(deserializer);
    }

    @Test
    public void testDeserializeAndSetNullId() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        property.deserializeAndSet(jsonParser, context, instance);

        assertNull(property.getValue(instance));
    }

    @Test
    public void testDeserializeAndSetStringId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderForString,
                new PropertyMetadata(null, null, null, null, null, null));
        property.setValueDeserializer(new StringDeserializer());

        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        String id = "123";
        when(jsonParser.getValueAsString()).thenReturn(id);

        property.deserializeAndSet(jsonParser, context, instance);

        assertEquals("123", property.getValue(instance));
    }

    @Test
    public void testDeserializeAndSetUuidId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReaderForUuid,
                new PropertyMetadata(null, null, null, null, null, null));
        property.setValueDeserializer(new UuidDeserializer());

        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        java.util.UUID id = java.util.UUID.randomUUID();
        when(jsonParser.getValueAsBinary()).thenReturn(Mockito.mock(byte[].class));

        property.deserializeAndSet(jsonParser, context, instance);

        assertEquals(id, property.getValue(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullValue() throws IOException {
        Object instance = new Object();
        property.set(instance, null);
    }

    private static class StringDeserializer extends StdDeserializer<String> {
        StringDeserializer() {
            this(null);
        }

        StringDeserializer(Class<String> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class UuidDeserializer extends StdDeserializer<java.util.UUID> {
        UuidDeserializer() {
            this(null);
        }

        UuidDeserializer(Class<java.util.UUID> vc) {
            super(vc);
        }

        @Override
        public java.util.UUID deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            byte[] binary = p.getValueAsBinary();
            return new java.util.UUID(binary[0], binary[1], binary[2], binary[3], binary[4], binary[5], binary[6], binary[7], binary[8], binary[9], binary[10], binary[11], binary[12], binary[13], binary[14], binary[15]);
        }
    }
}
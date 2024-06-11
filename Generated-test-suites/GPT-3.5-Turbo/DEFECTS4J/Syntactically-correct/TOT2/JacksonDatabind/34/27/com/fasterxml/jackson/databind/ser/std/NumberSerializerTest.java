package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerialize() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);

        try {
            serializer.serialize(BigDecimal.valueOf(10), mockGenerator, mockProvider);
            // Add more test cases covering other number types
        } catch (IOException e) {
            fail("IOException thrown during serialization");
        }
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);

        JsonNode schema = serializer.getSchema(mockProvider, typeHint);
        // Add assertions to validate the schema node
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper mockVisitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        try {
            serializer.acceptJsonFormatVisitor(mockVisitor, typeHint);
            // Add test cases for different scenarios
        } catch (JsonMappingException e) {
            fail("JsonMappingException thrown during acceptJsonFormatVisitor");
        }
    }
}
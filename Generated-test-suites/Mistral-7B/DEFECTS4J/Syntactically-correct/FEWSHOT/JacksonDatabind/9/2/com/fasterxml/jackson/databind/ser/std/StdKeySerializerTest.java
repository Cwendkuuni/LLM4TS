package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class StdKeySerializerTest {

    private ObjectMapper objectMapper;
    private JsonGenerator jsonGeneratorMock;
    private SerializerProvider serializerProviderMock;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonGeneratorMock = mock(JsonGenerator.class);
        serializerProviderMock = mock(SerializerProvider.class);
    }

    @Test
    public void testSerializeDateKey() throws JsonProcessingException {
        Date date = new Date();
        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(date, jsonGeneratorMock, serializerProviderMock);

        verify(jsonGeneratorMock, times(1)).writeNumber(eq(date.getTime()));
    }

    @Test
    public void testSerializeStringKey() throws JsonProcessingException {
        String key = "testKey";
        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(key, jsonGeneratorMock, serializerProviderMock);

        verify(jsonGeneratorMock, times(1)).writeFieldName(eq(key));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode schema = serializer.getSchema(serializerProviderMock, JavaType.getCollectionType(Map.class, String.class));

        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        String typeHint = "string";
        StdKeySerializer serializer = new StdKeySerializer();

        serializer.acceptJsonFormatVisitor(any(JsonFormatVisitorWrapper.class), argThat(t -> typeHint.equals(t.getType().getRawClass().getName())));

        verify(serializer, times(1)).serialize(any(), any(), any());
    }

    @Test
    public void testSerializeNullKey() throws JsonProcessingException {
        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(null, jsonGeneratorMock, serializerProviderMock);

        verify(jsonGeneratorMock, never()).writeFieldName(anyString());
    }
}
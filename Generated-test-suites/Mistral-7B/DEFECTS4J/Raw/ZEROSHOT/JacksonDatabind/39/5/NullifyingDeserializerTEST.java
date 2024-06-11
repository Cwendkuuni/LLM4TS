package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerTestBase;

import org.junit.Test;

/**
 * Test class for NullifyingDeserializer
 */
public class NullifyingDeserializerTest extends TypeDeserializerTestBase {

    private ObjectMapper objectMapper;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserialize() throws Exception {
        Object input = null;
        JsonParser mockParser = mock(JsonParser.class);
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        when(mockContext.findValueSerializer(Object.class)).thenReturn(NullifyingDeserializer.instance);

        Object result = NullifyingDeserializer.instance.deserialize(mockParser, mockContext);
        assertNull(result);

        verify(mockParser, times(1)).skipChildren();
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidInput() throws Exception {
        Object input = "invalid input";
        JsonParser mockParser = mock(JsonParser.class);
        when(mockParser.getText()).thenReturn(input.toString());
        DeserializationContext mockContext = mock(DeserializationContext.class);

        NullifyingDeserializer serializer = new NullifyingDeserializer();
        serializer.deserialize(mockParser, mockContext);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        Object input = null;
        JsonParser mockParser = mock(JsonParser.class);
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(mockContext.findValueSerializer(Object.class)).thenReturn(NullifyingDeserializer.instance);
        when(mockContext.findRootValueSerializer(String.class)).thenReturn(stringSerializer);
        when(typeDeserializer.deserializeTypedFromAny(mockParser, mockContext)).thenReturn("test");

        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, typeDeserializer);
        assertNull(result);

        verify(mockParser, times(1)).skipChildren();
        verify(mockParser, times(1)).nextToken();
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(mockParser, mockContext);
    }

    @Test
    public void testDeserializeWithTypeInvalidInput() throws Exception {
        Object input = "invalid input";
        JsonParser mockParser = mock(JsonParser.class);
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(mockContext.findValueSerializer(Object.class)).thenReturn(NullifyingDeserializer.instance);
        when(mockContext.findRootValueSerializer(String.class)).thenReturn(stringSerializer);
        when(typeDeserializer.deserializeTypedFromAny(mockParser, mockContext)).thenThrow(new IOException());

        NullifyingDeserializer serializer = new NullifyingDeserializer();
        serializer.deserializeWithType(mockParser, mockContext, typeDeserializer);
    }
}
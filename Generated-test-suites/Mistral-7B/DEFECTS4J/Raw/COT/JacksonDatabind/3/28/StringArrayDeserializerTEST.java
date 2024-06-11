package com.example.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ObjectBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@DisplayName("StringArrayDeserializer Tests")
public class StringArrayDeserializerTest {

    private JsonParser mockJsonParser;
    private DeserializationContext mockContext;
    private ObjectBuffer mockObjectBuffer;
    private StringArrayDeserializer testInstance;

    @BeforeEach
    public void setup() {
        mockJsonParser = mock(JsonParser.class);
        mockContext = mock(DeserializationContext.class);
        mockObjectBuffer = mock(ObjectBuffer.class);
        testInstance = new StringArrayDeserializer();
    }

    @Test
    @DisplayName("Test deserialize method with null input")
    public void testDeserializeNullInput() throws Exception {
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        String[] result = testInstance.deserialize(mockJsonParser, mockContext);
        assertNull(result);
        verify(mockContext).returnObjectBuffer(mockObjectBuffer);
    }

    @Test
    @DisplayName("Test deserialize method with empty array input")
    public void testDeserializeEmptyArrayInput() throws Exception {
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = testInstance.deserialize(mockJsonParser, mockContext);
        assertArrayEquals(new String[0], result);
        verify(mockContext).returnObjectBuffer(mockObjectBuffer);
    }

    @Test
    @DisplayName("Test deserialize method with valid input")
    public void testDeserializeValidInput() throws Exception {
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn("element1");
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn("element2");
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn("element3");
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = testInstance.deserialize(mockJsonParser, mockContext);
        assertArrayEquals(new String[]{"element1", "element2", null, "element3"}, result);
        verify(mockContext).returnObjectBuffer(mockObjectBuffer);
    }

    @Test
    @DisplayName("Test _deserializeCustom method with custom deserializer")
    public void testDeserializeCustomDeserializer() throws Exception {
        JsonDeserializer<String> mockDeserializer = mock(JsonDeserializer.class);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        testInstance = new StringArrayDeserializer(mockDeserializer);
        String[] result = testInstance.deserialize(mockJsonParser, mockContext);
        assertArrayEquals(new String[]{null}, result);
        verify(mockDeserializer).deserialize(eq(mockJsonParser), eq(mockContext));
        verifyNoMoreInteractions(mockDeserializer);
    }

    @Test
    @DisplayName("Test deserialize method with non-array input")
    public void testDeserializeNonArrayInput() throws Exception {
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn("nonArrayInput");

        String[] result = testInstance.deserialize(mockJsonParser, mockContext);
        assertArrayEquals(new String[]{"nonArrayInput"}, result);
        verify(mockContext).mappingException(eq(_valueClass));
    }

    @Test
    @DisplayName("Test createContextual method with null context")
    public void testCreateContextualNullContext() {
        assertNull(testInstance.createContextual(null, null));
    }

    @Test
    @DisplayName("Test createContextual method with valid context")
    public void testCreateContextualValidContext() throws Exception {
        JsonDeserializer<String> mockDeserializer = mock(JsonDeserializer.class);
        when(mockContext.constructType(eq(String.class))).thenReturn(String.class);
        when(mockContext.findContextualValueDeserializer(eq(String.class), any())).thenReturn(mockDeserializer);

        StringArrayDeserializer result = (StringArrayDeserializer) testInstance.createContextual(mockContext, null);
        assertEquals(testInstance, result);
        verify(mockContext).findContextualValueDeserializer(eq(String.class), any());
    }

    @AfterEach
    public void cleanup() {
        reset(mockJsonParser, mockContext, mockObjectBuffer);
    }
}
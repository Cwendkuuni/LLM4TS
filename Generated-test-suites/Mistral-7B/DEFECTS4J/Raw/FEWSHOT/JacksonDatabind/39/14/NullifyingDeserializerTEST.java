package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class NullifyingDeserializerTest {

    private JsonParser mockJsonParser;
    private DeserializationContext mockDeserializationContext;
    private TypeDeserializer mockTypeDeserializer;

    @Before
    public void setup() {
        mockJsonParser = mock(JsonParser.class);
        mockDeserializationContext = mock(DeserializationContext.class);
        mockTypeDeserializer = mock(TypeDeserializer.class);
    }

    @Test
    public void testDeserialize_EmptyObject() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonParser.Current.START_OBJECT);

        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);

        verify(mockJsonParser).skipChildren();
        verify(mockDeserializationContext).handleUnexpectedValue(isA(Null.class), eq(NullifyingDeserializer.class), any(), any());
        assertEquals(null, result);
    }

    @Test
    public void testDeserialize_EmptyArray() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonParser.Current.START_ARRAY);

        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);

        verify(mockJsonParser).skipChildren();
        verify(mockDeserializationContext).handleUnexpectedValue(isA(Null.class), eq(NullifyingDeserializer.class), any(), any());
        assertEquals(null, result);
    }

    @Test
    public void testDeserialize_ValueOtherThanObjectOrArray() throws IOException {
        when(mockJsonParser.getCurrentTokenId())
                .thenReturn(JsonParser.Current.VALUE_STRING);

        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        Object result = deserializer.deserialize(mockJsonParser, mockDeserializationContext);

        verify(mockJsonParser).skipChildren();
        verify(mockDeserializationContext).handleUnexpectedValue(isA(Null.class), eq(NullifyingDeserializer.class), any(), any());
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithType_Array() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonParser.Current.START_ARRAY);
        when(mockTypeDeserializer.deserializeTypedFromAny(mockJsonParser, mockDeserializationContext)).thenReturn(new Object()[0]);

        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        Object result = deserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer);

        verify(mockJsonParser).nextToken();
        verify(mockJsonParser).nextToken();
        verify(mockJsonParser).skipChildren();
        verify(mockDeserializationContext).handleUnexpectedValue(isA(Null.class), eq(NullifyingDeserializer.class), any(), any());
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithType_Object() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonParser.Current.FIELD_NAME);
        String fieldName = "someFieldName";
        when(mockJsonParser.getText()).thenReturn(fieldName);

        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        Object result = deserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer);

        verify(mockJsonParser).nextToken();
        verify(mockJsonParser).nextToken();
        verify(mockJsonParser).nextToken();
        verify(mockJsonParser).nextToken();
        verify(mockJsonParser).skipChildren();
        verify(mockDeserializationContext).handleUnexpectedValue(isA(Null.class), eq(NullifyingDeserializer.class), any(), any());
        assertEquals(null, result);
    }
}
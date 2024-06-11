package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class NullifyingDeserializerTest {

    private JsonParser mockJsonParser;
    private DeserializationContext mockContext;
    private TypeDeserializer mockTypeDeserializer;

    @Before
    public void setUp() {
        mockJsonParser = mock(JsonParser.class);
        mockContext = mock(DeserializationContext.class);
        mockTypeDeserializer = mock(TypeDeserializer.class);
    }

    @Test
    public void testDeserialize_whenCurrentTokenIsNotFieldName_thenReturnNull() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.getId());

        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        Object result = deserializer.deserialize(mockJsonParser, mockContext);

        verify(mockJsonParser).skipChildren();
        assertEquals(null, result);
    }

    @Test
    public void testDeserialize_whenCurrentTokenIsFieldName_thenCallTypeDeserializer() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME.getId());

        NullifyingDeserializer deserializer = new NullifyingDeserializer();

        doReturn(mockTypeDeserializer).when(mockContext).findContextualTypeDeserializer(Object.class, null);

        Object result = deserializer.deserialize(mockJsonParser, mockContext);

        verify(mockTypeDeserializer).deserializeTypedFromAny(mockJsonParser, mockContext);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithType_whenCurrentTokenIsStartArray_thenCallTypeDeserializer() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.getId());

        NullifyingDeserializer deserializer = new NullifyingDeserializer();

        doReturn(mockTypeDeserializer).when(mockContext).findContextualTypeDeserializer(Object.class, null);

        Object result = deserializer.deserializeWithType(mockJsonParser, mockContext, mockTypeDeserializer);

        verify(mockTypeDeserializer).deserializeTypedFromAny(mockJsonParser, mockContext);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithType_whenCurrentTokenIsStartObject_thenCallTypeDeserializer() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT.getId());

        NullifyingDeserializer deserializer = new NullifyingDeserializer();

        doReturn(mockTypeDeserializer).when(mockContext).findContextualTypeDeserializer(Object.class, null);

        Object result = deserializer.deserializeWithType(mockJsonParser, mockContext, mockTypeDeserializer);

        verify(mockTypeDeserializer).deserializeTypedFromAny(mockJsonParser, mockContext);
        assertEquals(null, result);
    }

    @Test(expected = IOException.class)
    public void testDeserialize_whenCurrentTokenIsInvalid_thenThrowIOException() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(-1);

        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        deserializer.deserialize(mockJsonParser, mockContext);
    }
}
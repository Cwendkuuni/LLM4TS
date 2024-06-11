package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Mock
    private JsonParser mockJsonParser;
    @Mock
    private DeserializationContext mockDeserializationContext;
    @Mock
    private TypeDeserializer mockTypeDeserializer;

    private NullifyingDeserializer nullifyingDeserializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        nullifyingDeserializer = new NullifyingDeserializer();
    }

    @Test
    public void testDeserialize() throws IOException {
        Object result = nullifyingDeserializer.deserialize(mockJsonParser, mockDeserializationContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeForStartArray() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);

        Object result = nullifyingDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeForStartObject() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);

        Object result = nullifyingDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeForFieldName() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);

        Object result = nullifyingDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeForOtherTokenIds() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_NULL);

        Object result = nullifyingDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer);

        assertNull(result);
    }
}
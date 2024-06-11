
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NullifyingDeserializerTest {

    NullifyingDeserializer nullifyingDeserializer;

    @Mock
    JsonParser mockJsonParser;

    @Mock
    DeserializationContext mockDeserializationContext;

    @Mock
    TypeDeserializer mockTypeDeserializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        nullifyingDeserializer = new NullifyingDeserializer();
    }

    @Test
    public void testDeserialize() throws IOException {
        assertNull(nullifyingDeserializer.deserialize(mockJsonParser, mockDeserializationContext));
    }

    @Test
    public void testDeserializeWithType_StartArray() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        assertNull(nullifyingDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer));
    }

    @Test
    public void testDeserializeWithType_StartObject() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        assertNull(nullifyingDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer));
    }

    @Test
    public void testDeserializeWithType_FieldName() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        assertNull(nullifyingDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer));
    }

    @Test
    public void testDeserializeWithType_Default() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_END_OBJECT);
        assertNull(nullifyingDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer));
    }
}

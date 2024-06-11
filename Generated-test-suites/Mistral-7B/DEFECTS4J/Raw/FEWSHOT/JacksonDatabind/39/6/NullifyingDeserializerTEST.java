package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TypeDeserializer typeDeserializer;

    @Before
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        typeDeserializer = mock(TypeDeserializer.class);
    }

    @Test
    public void testDeserialize() throws IOException {
        when(objectMapper.treeAsTokens(any(CharSequence.class))).thenReturn(jsonParser);

        Object result = new NullifyingDeserializer().deserialize(jsonParser, deserializationContext);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        when(objectMapper.treeAsTokens(any(CharSequence.class))).thenReturn(jsonParser);
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(new Object());

        Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithStartArray() throws IOException {
        when(objectMapper.treeAsTokens(any(CharSequence.class))).thenReturn(jsonParser);

        given(jsonParser.getCurrentTokenId()).willReturn(JsonParser.Current.START_ARRAY);

        Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithStartObject() throws IOException {
        when(objectMapper.treeAsTokens(any(CharSequence.class))).thenReturn(jsonParser);

        given(jsonParser.getCurrentTokenId()).willReturn(JsonParser.Current.START_OBJECT);

        Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithFieldName() throws IOException {
        when(objectMapper.treeAsTokens(any(CharSequence.class))).thenReturn(jsonParser);

        given(jsonParser.getCurrentTokenId()).willReturn(JsonParser.Current.FIELD_NAME);
        given(jsonParser.nextToken()).willReturn(JsonParser.Current.VALUE_NULL);

        Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithValueString() throws IOException {
        when(objectMapper.treeAsTokens(any(CharSequence.class))).thenReturn(jsonParser);

        given(jsonParser.getCurrentTokenId()).willReturn(JsonParser.Current.VALUE_STRING);
        given(jsonParser.getValueAsString()).willReturn("Test String");

        Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithValueNumberInt() throws IOException {
        when(objectMapper.treeAsTokens(any(CharSequence.class))).thenReturn(jsonParser);

        given(jsonParser.getCurrentTokenId()).willReturn(JsonParser.Current.VALUE_NUMBER_INT);
        given(jsonParser.getValueAsInt()).willReturn(42);

        Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithValueNumberLong() throws IOException {
        when(objectMapper.treeAsTokens(any(CharSequence.class))).thenReturn(jsonParser);

        given(jsonParser.getCurrentTokenId()).willReturn(JsonParser.Current.VALUE_NUMBER_LONG);
        given(jsonParser.getValueAsLong()).willReturn(42L);

        Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithValueNull() throws IOException {
        when(objectMapper.treeAsTokens(any(CharSequence.class))).thenReturn(jsonParser);

        given(jsonParser.getCurrentTokenId()).willReturn(JsonParser.Current.VALUE_NULL);

        Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertEquals(null, result);
    }
}
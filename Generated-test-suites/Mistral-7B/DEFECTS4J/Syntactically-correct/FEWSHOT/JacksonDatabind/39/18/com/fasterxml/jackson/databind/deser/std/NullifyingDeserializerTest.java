package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Test;
import static org.junit.Assert.assertNull;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser mockParser = createMockParser();
        DeserializationContext ctxt = createMockContext();

        assertNull(NullifyingDeserializer.instance.deserialize(mockParser, ctxt));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser mockParser = createMockParser();
        DeserializationContext ctxt = createMockContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();

        mockParser.getCurrentTokenIdReturns(JsonParser.Current.START_ARRAY);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(mockParser, ctxt, mockTypeDeserializer));

        mockParser.getCurrentTokenIdReturns(JsonParser.Current.START_OBJECT);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(mockParser, ctxt, mockTypeDeserializer));

        mockParser.getCurrentTokenIdReturns(JsonParser.Current.FIELD_NAME);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(mockParser, ctxt, mockTypeDeserializer));

        mockParser.getCurrentTokenIdReturns(JsonParser.Current.VALUE_NUMBER_INT);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(mockParser, ctxt, mockTypeDeserializer));

        mockParser.getCurrentTokenIdReturns(JsonParser.Current.VALUE_STRING);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(mockParser, ctxt, mockTypeDeserializer));

        mockParser.getCurrentTokenIdReturns(JsonParser.Current.VALUE_EMBEDDED_ARRAY);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(mockParser, ctxt, mockTypeDeserializer));

        mockParser.getCurrentTokenIdReturns(JsonParser.Current.VALUE_TRUE);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(mockParser, ctxt, mockTypeDeserializer));

        mockParser.getCurrentTokenIdReturns(JsonParser.Current.VALUE_FALSE);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(mockParser, ctxt, mockTypeDeserializer));

        mockParser.getCurrentTokenIdReturns(JsonParser.Current.VALUE_NULL);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(mockParser, ctxt, mockTypeDeserializer));

        mockParser.getCurrentTokenIdReturns(JsonParser.Current.END_ARRAY);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(mockParser, ctxt, mockTypeDeserializer));

        mockParser.getCurrentTokenIdReturns(JsonParser.Current.END_OBJECT);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(mockParser, ctxt, mockTypeDeserializer));

        mockParser.getCurrentTokenIdReturns(JsonParser.Current.END_DOCUMENT);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(mockParser, ctxt, mockTypeDeserializer));
    }

    private JsonParser createMockParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext createMockContext() {
        return mock(DeserializationContext.class);
    }

    private TypeDeserializer createMockTypeDeserializer() {
        return mock(TypeDeserializer.class);
    }
}
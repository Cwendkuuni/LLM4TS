package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    private static final NullifyingDeserializer TESTEE = NullifyingDeserializer.instance;

    @Test
    public void testDeserialize() throws Exception {
        JsonParser mockParser = createMockParser(JsonToken.START_ARRAY);
        DeserializationContext mockContext = createMockContext();

        Object result = TESTEE.deserialize(mockParser, mockContext);
        assertNull(result);

        mockParser = createMockParser(JsonToken.START_OBJECT);
        result = TESTEE.deserialize(mockParser, mockContext);
        assertNull(result);

        mockParser = createMockParser(JsonToken.VALUE_STRING);
        mockParser.nextToken(); // consume "foo"
        result = TESTEE.deserialize(mockParser, mockContext);
        assertNull(result);

        mockParser = createMockParser(JsonToken.FIELD_NAME);
        mockParser.nextToken(); // consume "name"
        result = TESTEE.deserialize(mockParser, mockContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser mockParser = createMockParser(JsonToken.START_ARRAY);
        DeserializationContext mockContext = createMockContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();

        Object result = TESTEE.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);

        mockParser = createMockParser(JsonToken.START_OBJECT);
        result = TESTEE.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);

        mockParser = createMockParser(JsonToken.FIELD_NAME);
        mockParser.nextToken(); // consume "name"
        result = TESTEE.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);

        mockParser = createMockParser(JsonToken.VALUE_STRING);
        mockParser.nextToken(); // consume "foo"
        result = TESTEE.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);

        mockParser = createMockParser(JsonToken.START_ARRAY);
        result = TESTEE.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);

        mockParser = createMockParser(JsonToken.START_OBJECT);
        result = TESTEE.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);
    }

    private JsonParser createMockParser(JsonToken expectedToken) {
        JsonParser mockParser = mock(JsonParser.class);
        when(mockParser.getCurrentToken()).thenReturn(expectedToken);
        return mockParser;
    }

    private DeserializationContext createMockContext() {
        DeserializationContext mockContext = mock(DeserializationContext.class);
        return mockContext;
    }

    private TypeDeserializer createMockTypeDeserializer() {
        TypeDeserializer mockTypeDeserializer = mock(TypeDeserializer.class);
        return mockTypeDeserializer;
    }
}
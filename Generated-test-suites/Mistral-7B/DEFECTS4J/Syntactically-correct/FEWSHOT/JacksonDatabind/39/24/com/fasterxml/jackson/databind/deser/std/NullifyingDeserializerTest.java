package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserializeNull() throws IOException {
        JsonParser mockParser = createMockJsonParser();
        DeserializationContext mockContext = createMockDeserializationContext();

        Object result = NullifyingDeserializer.instance.deserialize(mockParser, mockContext);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeNull() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.ID_START_OBJECT.getValue());
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();

        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeArray() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.ID_START_ARRAY.getValue());
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();

        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeObject() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.ID_FIELD_NAME.getValue());
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();

        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);

        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithTypeUnexpectedToken() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.ID_START_ARRAY.getValue());
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();

        NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
    }

    private JsonParser createMockJsonParser() {
        return createMock(JsonParser.class);
    }

    private JsonParser createMockJsonParser(int currentTokenId) {
        JsonParser mockParser = createMock(JsonParser.class);
        expect(mockParser.getCurrentTokenId()).andReturn(currentTokenId);

        return mockParser;
    }

    private DeserializationContext createMockDeserializationContext() {
        return createMock(DeserializationContext.class);
    }

    private TypeDeserializer createMockTypeDeserializer() {
        return createMock(TypeDeserializer.class);
    }
}
package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserializeNullifyingDeserializerWithEmptyInput() throws IOException {
        JsonParser mockParser = createMockJsonParser();
        DeserializationContext mockContext = createMockDeserializationContext();
        Object result = NullifyingDeserializer.instance.deserialize(mockParser, mockContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithNullInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.NULL);
        DeserializationContext mockContext = createMockDeserializationContext();
        Object result = NullifyingDeserializer.instance.deserialize(mockParser, mockContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithNumberInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.NUMBER_INT);
        DeserializationContext mockContext = createMockDeserializationContext();
        Object result = NullifyingDeserializer.instance.deserialize(mockParser, mockContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithBooleanInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.VALUE_TRUE);
        DeserializationContext mockContext = createMockDeserializationContext();
        Object result = NullifyingDeserializer.instance.deserialize(mockParser, mockContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithStringInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.VALUE_STRING, "TestString");
        DeserializationContext mockContext = createMockDeserializationContext();
        Object result = NullifyingDeserializer.instance.deserialize(mockParser, mockContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithStartArrayInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.START_ARRAY);
        DeserializationContext mockContext = createMockDeserializationContext();
        Object result = NullifyingDeserializer.instance.deserialize(mockParser, mockContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithStartObjectInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.START_OBJECT);
        DeserializationContext mockContext = createMockDeserializationContext();
        Object result = NullifyingDeserializer.instance.deserialize(mockParser, mockContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithFieldNameInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.FIELD_NAME, "testFieldName");
        DeserializationContext mockContext = createMockDeserializationContext();
        Object result = NullifyingDeserializer.instance.deserialize(mockParser, mockContext);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNullifyingDeserializerWithOtherTokenInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.COMMENT);
        DeserializationContext mockContext = createMockDeserializationContext();
        Object result = NullifyingDeserializer.instance.deserialize(mockParser, mockContext);
        fail("Expected IOException but got: " + result);
    }

    @Test
    public void testDeserializeTypedNullifyingDeserializerWithEmptyInput() throws IOException {
        JsonParser mockParser = createMockJsonParser();
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();
        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedNullifyingDeserializerWithNullInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.NULL);
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();
        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedNullifyingDeserializerWithNumberInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.NUMBER_INT);
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();
        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedNullifyingDeserializerWithBooleanInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.VALUE_TRUE);
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();
        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedNullifyingDeserializerWithStringInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.VALUE_STRING, "TestString");
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();
        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedNullifyingDeserializerWithStartArrayInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.START_ARRAY);
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();
        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedNullifyingDeserializerWithStartObjectInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.START_OBJECT);
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();
        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedNullifyingDeserializerWithFieldNameInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.FIELD_NAME, "testFieldName");
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();
        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedNullifyingDeserializerWithOtherTokenInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.COMMENT);
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();
        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedNullifyingDeserializerWithValidInput() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonTokenId.START_ARRAY);
        DeserializationContext mockContext = createMockDeserializationContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer(TestClass.class);
        Object result = NullifyingDeserializer.instance.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertNull(result);

        mockParser.nextToken(); // consume [
        mockParser.nextToken(); // consume {
        mockParser.nextToken(); // consume "name":
        String name = mockParser.getText();
        mockParser.nextToken(); // consume:
        JsonToken expectedType = JsonTokenId.VALUE_STRING;
        JsonToken actualType = mockParser.getCurrentToken();
        assertEquals(expectedType, actualType);
        String resultName = mockParser.getText();
        mockParser.nextToken(); // consume,
        JsonToken expectedValueToken = JsonTokenId.START_ARRAY;
        JsonToken actualValueToken = mockParser.getCurrentToken();
        assertEquals(expectedValueToken, actualValueToken);
        Object resultList = mockTypeDeserializer.deserializeTypedFromAny(mockParser, mockContext);
        assertEquals(List.class, resultList.getClass());
        mockParser.nextToken(); // consume ]
        mockParser.nextToken(); // consume null
        mockParser.nextToken(); // consume }
    }

    private JsonParser createMockJsonParser() {
        return createMockJsonParser((JsonToken) null);
    }

    private JsonParser createMockJsonParser(JsonToken expectedToken) {
        JsonParser mockParser = mock(JsonParser.class);
        when(mockParser.getCurrentToken()).thenReturn(expectedToken);
        return mockParser;
    }

    private JsonParser createMockJsonParser(JsonTokenId tokenId) {
        return createMockJsonParser(tokenId.getValue());
    }

    private JsonParser createMockJsonParser(JsonTokenId tokenId, String expectedText) {
        JsonParser mockParser = createMockJsonParser(tokenId);
        when(mockParser.getText()).thenReturn(expectedText);
        return mockParser;
    }

    private DeserializationContext createMockDeserializationContext() {
        return mock(DeserializationContext.class);
    }

    private TypeDeserializer createMockTypeDeserializer() {
        return mock(TypeDeserializer.class);
    }

    private TypeDeserializer createMockTypeDeserializer(Class<?> expectedType) {
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();
        when(mockTypeDeserializer.deserializeTypedFromAny(any(), any())).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            JsonParser parser = (JsonParser) args[0];
            DeserializationContext context = (DeserializationContext) args[1];
            return new ArrayList<>();
        });
        when(mockTypeDeserializer.getType()).thenReturn(expectedType);
        return mockTypeDeserializer;
    }

    private static class TestClass {}
}
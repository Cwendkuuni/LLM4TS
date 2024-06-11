package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MappingIteratorTest {

    private MappingIterator<Object> mappingIterator;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private JsonDeserializer<Object> jsonDeserializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mappingIterator = new MappingIterator<Object>(null, jsonParser, deserializationContext,
                jsonDeserializer, false, null);
    }

    @Test
    public void testHasNextValue_ReturnsFalse_IfParserIsNull() throws IOException {
        // Given
        mappingIterator = new MappingIterator<Object>(null, null, deserializationContext,
                jsonDeserializer, false, null);

        // When
        boolean result = mappingIterator.hasNextValue();

        // Then
        assertFalse(result);
    }

    @Test
    public void testHasNextValue_ReturnsTrue_IfParserHasNextToken() throws IOException {
        // Given
        JsonToken jsonToken = JsonToken.START_OBJECT;
        givenJsonParserHasNextToken(jsonToken);

        // When
        boolean result = mappingIterator.hasNextValue();

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasNextValue_ReturnsTrue_IfParserHasMoreElements() throws IOException {
        // Given
        JsonToken jsonToken1 = JsonToken.START_ARRAY;
        JsonToken jsonToken2 = JsonToken.VALUE_STRING;
        givenJsonParserHasNextToken(jsonToken1, jsonToken2);

        // When
        boolean result = mappingIterator.hasNextValue();

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasNextValue_ReturnsFalse_IfParserHasOnlyEndArrayToken() throws IOException {
        // Given
        JsonToken jsonToken1 = JsonToken.START_ARRAY;
        JsonToken jsonToken2 = JsonToken.END_ARRAY;
        givenJsonParserHasNextToken(jsonToken1, jsonToken2);

        // When
        boolean result = mappingIterator.hasNextValue();

        // Then
        assertFalse(result);
    }

    @Test
    public void testNextValue_ReturnsDeserializedObject_IfParserHasNextToken() throws IOException {
        // Given
        Object deserializedObject = new Object();
        givenJsonParserHasNextTokenAndDeserializedObject(JsonToken.START_OBJECT, deserializedObject);

        // When
        Object result = mappingIterator.nextValue();

        // Then
        assertEquals(deserializedObject, result);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValue_ThrowsNoSuchElementException_IfParserDoesNotHaveNextToken() throws IOException {
        // Given
        givenJsonParserHasNextToken(JsonToken.END_ARRAY);

        // When
        mappingIterator.nextValue();
    }

    @Test
    public void testReadAll_ReturnsListContainingAllDeserializedObjects() throws IOException {
        // Given
        List<Object> resultList = new ArrayList<>();
        Object deserializedObject1 = new Object();
        Object deserializedObject2 = new Object();
        givenJsonParserHasNextTokenAndDeserializedObjects(JsonToken.START_ARRAY, deserializedObject1, deserializedObject2);

        // When
        List<Object> result = mappingIterator.readAll(resultList);

        // Then
        assertEquals(resultList, result);
        assertEquals(2, result.size());
        assertEquals(deserializedObject1, result.get(0));
        assertEquals(deserializedObject2, result.get(1));
    }

    private void givenJsonParserHasNextToken(JsonToken... jsonTokens) throws IOException {
        int currentTokenIndex = 0;
        JsonToken currentToken = jsonTokens[currentTokenIndex];
        givenJsonParserGetCurrentToken(currentToken);

        JsonToken nextToken = jsonTokens.length > currentTokenIndex + 1 ? jsonTokens[currentTokenIndex + 1] : null;
        givenJsonParserNextToken(nextToken);
    }

    private void givenJsonParserHasNextTokenAndDeserializedObject(JsonToken jsonToken, Object deserializedObject) throws IOException {
        givenJsonParserGetCurrentToken(jsonToken);
        givenJsonParserNextToken(jsonToken);
        givenJsonDeserializerDeserialize(deserializedObject);
    }

    private void givenJsonParserHasNextTokenAndDeserializedObjects(JsonToken jsonToken, Object... deserializedObjects) throws IOException {
        givenJsonParserGetCurrentToken(jsonToken);
        givenJsonParserNextToken(jsonToken);
        for (Object deserializedObject : deserializedObjects) {
            givenJsonDeserializerDeserialize(deserializedObject);
            givenJsonParserNextToken(jsonToken);
        }
        givenJsonParserNextToken(JsonToken.END_ARRAY);
    }

    private void givenJsonParserGetCurrentToken(JsonToken jsonToken) throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
    }

    private void givenJsonParserNextToken(JsonToken jsonToken) throws IOException {
        when(jsonParser.nextToken()).thenReturn(jsonToken);
    }

    private void givenJsonDeserializerDeserialize(Object deserializedObject) throws IOException {
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(deserializedObject);
    }
}
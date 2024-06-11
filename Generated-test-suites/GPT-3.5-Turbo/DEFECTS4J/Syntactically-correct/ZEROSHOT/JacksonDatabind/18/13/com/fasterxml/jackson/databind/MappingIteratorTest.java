package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MappingIteratorTest {

    @Test
    public void testHasNextValue_WhenParserIsNull_ReturnsFalse() throws IOException {
        // Arrange
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, null, null, null, false, null);
        
        // Act
        boolean result = iterator.hasNextValue();
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasNextValue_WhenTokenIsNull_ReturnsFalse() throws IOException {
        // Arrange
        JsonParser jsonParser = new JsonFactory().createParser("[]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, jsonParser, null, null, true, null);
        
        // Act
        boolean result = iterator.hasNextValue();
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasNextValue_WhenTokenIsEndArray_ReturnsFalse() throws IOException {
        // Arrange
        JsonParser jsonParser = new JsonFactory().createParser("[]");
        jsonParser.nextToken();
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, jsonParser, null, null, true, null);
        
        // Act
        boolean result = iterator.hasNextValue();
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasNextValue_WhenTokenIsNotNullAndNotEndArray_ReturnsTrue() throws IOException {
        // Arrange
        JsonParser jsonParser = new JsonFactory().createParser("[1]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, jsonParser, null, null, true, null);
        
        // Act
        boolean result = iterator.hasNextValue();
        
        // Assert
        assertTrue(result);
    }

    @Test (expected = RuntimeJsonMappingException.class)
    public void testNextValue_WhenHasNextValueIsFalse_ThrowsRuntimeException() throws IOException {
        // Arrange
        JsonParser jsonParser = new JsonFactory().createParser("[]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, jsonParser, null, null, true, null);
        
        // Act
        iterator.nextValue();
    }

    @Test
    public void testNextValue_WhenUpdatedValueIsNull_ReturnsDeserializedValue() throws IOException {
        // Arrange
        JsonParser jsonParser = new JsonFactory().createParser("[1]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, jsonParser, null, null, true, null);
        
        // Act
        Integer result = iterator.nextValue();
        
        // Assert
        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    public void testNextValue_WhenUpdatedValueIsNotNull_UpdatesValueAndReturnsUpdatedValue() throws IOException {
        // Arrange
        JsonParser jsonParser = new JsonFactory().createParser("[2]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, jsonParser, null, null, true, 0);
        
        // Act
        Integer result = iterator.nextValue();
        
        // Assert
        assertEquals(Integer.valueOf(2), result);
    }

    @Test
    public void testReadAll_WhenHasNextValueIsFalse_ReturnsEmptyList() throws IOException {
        // Arrange
        JsonParser jsonParser = new JsonFactory().createParser("[]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, jsonParser, null, null, true, null);
        List<Integer> resultList = new ArrayList<>();
        
        // Act
        List<Integer> result = iterator.readAll(resultList);
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReadAll_WhenHasNextValueIsTrue_ReturnsListWithAllValues() throws IOException {
        // Arrange
        JsonParser jsonParser = new JsonFactory().createParser("[1, 2, 3]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, jsonParser, null, null, true, null);
        List<Integer> resultList = new ArrayList<>();
        
        // Act
        List<Integer> result = iterator.readAll(resultList);
        
        // Assert
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(1), result.get(0));
        assertEquals(Integer.valueOf(2), result.get(1));
        assertEquals(Integer.valueOf(3), result.get(2));
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testRemove_ThrowsUnsupportedOperationException() {
        // Arrange
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, null, null, null, false, null);
        
        // Act
        iterator.remove();
    }

    @Test
    public void testClose_CallsCloseOnParser() throws IOException {
        // Arrange
        JsonParser jsonParser = new JsonFactory().createParser("[]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, jsonParser, null, null, true, null);
        
        // Act
        iterator.close();
        
        // Assert
        assertTrue(jsonParser.isClosed());
    }

    @Test
    public void testGetParser_ReturnsParser() throws IOException {
        // Arrange
        JsonParser jsonParser = new JsonFactory().createParser("[]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, jsonParser, null, null, true, null);
        
        // Act
        JsonParser result = iterator.getParser();
        
        // Assert
        assertEquals(jsonParser, result);
    }

    @Test
    public void testGetParserSchema_ReturnsParserSchema() throws IOException {
        // Arrange
        JsonParser jsonParser = new JsonFactory().createParser("[]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, jsonParser, null, null, true, null);
        
        // Act
        FormatSchema result = iterator.getParserSchema();
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testGetCurrentLocation_ReturnsCurrentLocation() throws IOException {
        // Arrange
        JsonParser jsonParser = new JsonFactory().createParser("[1]");
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, jsonParser, null, null, true, null);
        
        // Act
        JsonLocation result = iterator.getCurrentLocation();
        
        // Assert
        assertNotNull(result);
    }

}
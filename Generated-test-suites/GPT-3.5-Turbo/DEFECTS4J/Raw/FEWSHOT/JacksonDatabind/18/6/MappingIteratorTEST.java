package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.deser.RuntimeJsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class MappingIteratorTest {
    private JsonParser mockParser;
    private DeserializationContext mockContext;
    private StdDeserializer<String> mockDeserializer;
    private MappingIterator<String> iterator;
    private List<String> resultList;

    @Before
    public void setUp() {
        mockParser = mock(JsonParser.class);
        mockContext = mock(DeserializationContext.class);
        mockDeserializer = mock(StdDeserializer.class);
        iterator = new MappingIterator<String>(null, mockParser, mockContext, mockDeserializer, true, null);
        resultList = new ArrayList<String>();
    }
    
    @Test
    public void testHasNextValue_WhenHasNextValueIsTrue_ShouldReturnTrue() throws IOException {
        when(mockParser.getCurrentToken()).thenReturn(mock(JsonToken.class));
        when(mockParser.nextToken()).thenReturn(mock(JsonToken.class));
        
        boolean result = iterator.hasNextValue();
        
        assertTrue(result);
    }
    
    @Test
    public void testHasNextValue_WhenHasNextCheckedIsTrue_ShouldReturnTrue() throws IOException {
        iterator._hasNextChecked = true;
        iterator._parser = mockParser;
        
        boolean result = iterator.hasNextValue();
        
        assertTrue(result);
    }
    
    @Test
    public void testHasNextValue_WhenParserIsNull_ShouldReturnFalse() throws IOException {
        iterator._parser = null;
        
        boolean result = iterator.hasNextValue();
        
        assertFalse(result);
    }
    
    @Test
    public void testNextValue_WhenHasNextCheckedIsFalse_ShouldCallHasNextValue() throws IOException {
        iterator._parser = mockParser;
        iterator._updatedValue = null;
        
        iterator.nextValue();
        
        verify(iterator).hasNextValue();
    }
    
    @Test
    public void testNextValue_WhenParserIsNull_ShouldCallThrowNoSuchElement() throws IOException {
        iterator._parser = null;
        
        try {
            iterator.nextValue();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // Expected
        }
    }
    
    @Test
    public void testNextValue_WhenHasNextCheckedIsTrueAndParserIsNotNull_ShouldSetHasNextCheckedToFalse() throws IOException {
        iterator._hasNextChecked = true;
        iterator._parser = mockParser;
        
        iterator.nextValue();
        
        assertFalse(iterator._hasNextChecked);
    }
    
    @Test
    public void testNextValue_WhenUpdatedValueIsNull_ShouldCallDeserializeAndReturnDeserializedValue() throws IOException {
        String value = "test";
        when(mockDeserializer.deserialize(mockParser, mockContext)).thenReturn(value);
        iterator._updatedValue = null;
        iterator._parser = mockParser;
        
        String result = iterator.nextValue();
        
        assertEquals(value, result);
        verify(mockDeserializer).deserialize(mockParser, mockContext);
    }
    
    @Test
    public void testNextValue_WhenUpdatedValueIsNotNull_ShouldCallDeserializeWithUpdatedValueAndReturnUpdatedValue() throws IOException {
        String value = "test";
        when(mockParser.getCurrentToken()).thenReturn(mock(JsonToken.class));
        iterator._updatedValue = value;
        iterator._parser = mockParser;
        
        String result = iterator.nextValue();
        
        assertEquals(value, result);
        verify(mockDeserializer).deserialize(mockParser, mockContext, value);
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testReadAll_WhenIteratorDoesNotHaveNextValue_ShouldThrowNoSuchElementException() throws IOException {
        when(mockParser.getCurrentToken()).thenReturn(null);
        iterator._parser = mockParser;
        
        iterator.readAll(resultList);
    }
    
    @Test
    public void testReadAll_WhenIteratorHasNextValue_ShouldAddValuesToResultList() throws IOException {
        String value1 = "test1";
        String value2 = "test2";
        when(mockParser.getCurrentToken()).thenReturn(mock(JsonToken.class), mock(JsonToken.class), null);
        when(mockParser.nextToken()).thenReturn(mock(JsonToken.class));
        when(mockDeserializer.deserialize(mockParser, mockContext)).thenReturn(value1, value2);
        iterator._parser = mockParser;
        
        List<String> result = iterator.readAll(resultList);
        
        assertEquals(2, result.size());
        assertEquals(value1, result.get(0));
        assertEquals(value2, result.get(1));
    }
    
    @Test
    public void testGetParser_ShouldReturnParser() {
        iterator._parser = mockParser;
        
        JsonParser result = iterator.getParser();
        
        assertEquals(mockParser, result);
    }
    
    @Test
    public void testGetParserSchema_ShouldReturnParserSchema() {
        iterator._parser = mockParser;
        FormatSchema mockSchema = mock(FormatSchema.class);
        when(mockParser.getSchema()).thenReturn(mockSchema);
        
        FormatSchema result = iterator.getParserSchema();
        
        assertEquals(mockSchema, result);
    }
    
    @Test
    public void testGetCurrentLocation_ShouldReturnParserCurrentLocation() {
        JsonLocation mockLocation = mock(JsonLocation.class);
        when(mockParser.getCurrentLocation()).thenReturn(mockLocation);
        
        JsonLocation result = iterator.getCurrentLocation();
        
        assertEquals(mockLocation, result);
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testThrowNoSuchElement_ShouldThrowNoSuchElementException() {
        iterator._throwNoSuchElement();
    }
    
    @Test(expected = RuntimeJsonMappingException.class)
    public void testHandleMappingException_ShouldThrowRuntimeJsonMappingException() {
        JsonMappingException e = mock(JsonMappingException.class);
        
        iterator._handleMappingException(e);
    }
    
    @Test(expected = RuntimeException.class)
    public void testHandleIOException_ShouldThrowRuntimeException() {
        IOException e = mock(IOException.class);
        
        iterator._handleIOException(e);
    }
}
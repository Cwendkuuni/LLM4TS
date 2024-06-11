package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

@RunWith(MockitoJUnitRunner.class)
public class MappingIteratorTest {
    
    @Mock
    private MappingIterator<String> mappingIterator;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private JsonToken jsonToken;
    
    @Before
    public void setUp() throws IOException {
        Mockito.when(mappingIterator.hasNextValue()).thenCallRealMethod();
        Mockito.when(mappingIterator.nextValue()).thenCallRealMethod();
        Mockito.when(mappingIterator.hasNext()).thenCallRealMethod();
        Mockito.when(mappingIterator._handleMappingException(Mockito.any())).thenCallRealMethod();
        Mockito.when(mappingIterator._handleIOException(Mockito.any())).thenCallRealMethod();
        
        Mockito.when(mappingIterator._parser).thenReturn(jsonParser);
        
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
        
        Mockito.when(jsonParser.nextToken()).thenReturn(jsonToken);
    }
    
    @Test
    public void testHasNextValueTrue() throws IOException {
        Mockito.when(jsonToken).thenReturn(JsonToken.START_OBJECT);
        
        boolean result = mappingIterator.hasNextValue();
        
        assertTrue(result);
    }
    
    @Test
    public void testHasNextValueFalse() throws IOException {
        Mockito.when(jsonToken).thenReturn(null);
        
        boolean result = mappingIterator.hasNextValue();
        
        assertFalse(result);
    }
    
    @Test(expected = RuntimeJsonMappingException.class)
    public void testHasNextValueWithMappingException() throws IOException {
        Mockito.when(jsonToken).thenReturn(JsonToken.START_OBJECT);
        Mockito.when(mappingIterator._handleMappingException(Mockito.any())).thenThrow(new RuntimeJsonMappingException());
        
        mappingIterator.hasNextValue();
    }
    
    @Test(expected = RuntimeException.class)
    public void testHasNextValueWithIOException() throws IOException {
        Mockito.when(jsonToken).thenReturn(JsonToken.START_OBJECT);
        Mockito.when(mappingIterator._handleIOException(Mockito.any())).thenThrow(new RuntimeException());
        
        mappingIterator.hasNextValue();
    }
    
    @Test
    public void testNextValue() throws IOException {
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(mappingIterator._deserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn("value");
        
        String result = mappingIterator.nextValue();
        
        assertEquals("value", result);
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testNextValueNoElement() throws IOException {
        Mockito.when(jsonToken).thenReturn(null);
        
        mappingIterator.nextValue();
    }
    
    @Test(expected = JsonMappingException.class)
    public void testNextValueWithMappingException() throws IOException {
        Mockito.when(jsonToken).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(mappingIterator._deserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenThrow(new JsonMappingException(""));
        
        mappingIterator.nextValue();
    }
    
    @Test(expected = RuntimeException.class)
    public void testNextValueWithIOException() throws IOException {
        Mockito.when(jsonToken).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(mappingIterator._deserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenThrow(new IOException());
        
        mappingIterator.nextValue();
    }
    
    @Test
    public void testRemove() {
        try {
            mappingIterator.remove();
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testClose() throws IOException {
        mappingIterator.close();
        
        Mockito.verify(jsonParser, Mockito.times(1)).close();
    }
    
    @Test
    public void testReadAll() throws IOException {
        List<String> resultList = new ArrayList<String>();
        Mockito.when(mappingIterator.hasNextValue()).thenReturn(true, false);
        Mockito.when(mappingIterator.nextValue()).thenReturn("value");
        
        List<String> result = mappingIterator.readAll(resultList);
        
        assertEquals(1, result.size());
        assertEquals("value", result.get(0));
    }
    
    @Test
    public void testReadAllWithCollection() throws IOException {
        List<String> resultList = new ArrayList<String>();
        Mockito.when(mappingIterator.hasNextValue()).thenReturn(true, false);
        Mockito.when(mappingIterator.nextValue()).thenReturn("value");
        
        List<String> result = mappingIterator.readAll((List<String>)resultList);
        
        assertEquals(1, result.size());
        assertEquals("value", result.get(0));
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testReadAllNoElement() throws IOException {
        List<String> resultList = new ArrayList<String>();
        Mockito.when(mappingIterator.hasNextValue()).thenReturn(false);
        
        mappingIterator.readAll(resultList);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testReadAllWithMappingException() throws IOException {
        List<String> resultList = new ArrayList<String>();
        Mockito.when(mappingIterator.hasNextValue()).thenReturn(true);
        Mockito.when(mappingIterator.nextValue()).thenThrow(new JsonMappingException(""));
        
        mappingIterator.readAll(resultList);
    }
    
    @Test(expected = RuntimeException.class)
    public void testReadAllWithIOException() throws IOException {
        List<String> resultList = new ArrayList<String>();
        Mockito.when(mappingIterator.hasNextValue()).thenReturn(true);
        Mockito.when(mappingIterator.nextValue()).thenThrow(new IOException());
        
        mappingIterator.readAll(resultList);
    }
    
    @Test
    public void testGetParser() {
        JsonParser result = mappingIterator.getParser();
        
        assertEquals(jsonParser, result);
    }
    
    @Test
    public void testGetParserSchema() {
        FormatSchema formatSchema = mock(FormatSchema.class);
        Mockito.when(jsonParser.getSchema()).thenReturn(formatSchema);
        
        FormatSchema result = mappingIterator.getParserSchema();
        
        assertEquals(formatSchema, result);
    }
    
    @Test
    public void testGetCurrentLocation() {
        JsonLocation jsonLocation = mock(JsonLocation.class);
        Mockito.when(jsonParser.getCurrentLocation()).thenReturn(jsonLocation);
        
        JsonLocation result = mappingIterator.getCurrentLocation();
        
        assertEquals(jsonLocation, result);
    }

}
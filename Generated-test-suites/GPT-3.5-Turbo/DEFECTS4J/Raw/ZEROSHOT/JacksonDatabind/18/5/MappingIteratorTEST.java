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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.deser.JsonDeserializer;

public class MappingIteratorTest {
    
    private MappingIterator<Object> iterator;
    private List<Object> resultList;
    
    @Before
    public void setUp() {
        iterator = new MappingIterator<Object>(null, null, null, null, false, null);
        resultList = new ArrayList<Object>();
    }
    
    @Test
    public void testHasNext_NoElements_ReturnsFalse() throws IOException {
        // Arrange
        iterator._hasNextChecked = false;
        iterator._parser = null;
        
        // Act
        boolean hasNext = iterator.hasNext();
        
        // Assert
        assertFalse(hasNext);
    }
    
    @Test
    public void testHasNext_WithElements_ReturnsTrue() throws IOException {
        // Arrange
        iterator._hasNextChecked = false;
        iterator._parser = new JsonParser() {
            public JsonToken getCurrentToken() throws IOException { return null; }
            public JsonToken nextToken() throws IOException { return null; }
            public void clearCurrentToken() {}
            public JsonParser skipChildren() throws IOException { return null; }
            public JsonToken getCurrentTokenId() { return null; }
            public void close() throws IOException {}
        };
        
        // Act
        boolean hasNext = iterator.hasNext();
        
        // Assert
        assertTrue(hasNext);
    }
    
    @Test
    public void testNext_NoElements_ThrowsNoSuchElementException() throws IOException {
        // Arrange
        iterator._hasNextChecked = false;
        iterator._parser = null;
        
        try {
            // Act
            iterator.next();
        } catch (NoSuchElementException e) {
            // Assert
            assertEquals("No such element", e.getMessage());
        }
    }
    
    @Test
    public void testNext_WithElements_ReturnsNextElement() throws IOException {
        // Arrange
        String expected = "test";
        iterator._hasNextChecked = false;
        iterator._parser = new JsonParser() {
            public JsonToken getCurrentToken() throws IOException { return null; }
            public JsonToken nextToken() throws IOException { return null; }
            public void clearCurrentToken() {}
            public JsonParser skipChildren() throws IOException { return null; }
            public JsonToken getCurrentTokenId() { return null; }
            public void close() throws IOException {}
        };
        iterator._deserializer = new JsonDeserializer<Object>() {
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException { return expected; }
        };
        
        // Act
        Object next = iterator.next();
        
        // Assert
        assertEquals(expected, next);
    }
    
    @Test
    public void testClose_WithValidParser_ClosesParser() throws IOException {
        // Arrange
        JsonParser parser = new JsonParser() {
            public JsonToken getCurrentToken() throws IOException { return null; }
            public JsonToken nextToken() throws IOException { return null; }
            public void clearCurrentToken() {}
            public JsonParser skipChildren() throws IOException { return null; }
            public JsonToken getCurrentTokenId() { return null; }
            public void close() throws IOException {}
        };
        iterator._parser = parser;
        
        // Act
        iterator.close();
        
        // Assert
        assertTrue(parser.isClosed());
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testRemove_ThrowsUnsupportedOperationException() {
        // Act
        iterator.remove();
    }
    
    @Test
    public void testReadAll_NoElements_ReturnsEmptyList() throws IOException {
        // Arrange
        iterator._parser = null;
        
        // Act
        List<Object> result = iterator.readAll();
        
        // Assert
        assertEquals(0, result.size());
    }
    
    @Test
    public void testReadAll_WithElements_ReturnsListWithAllElements() throws IOException {
        // Arrange
        String expected1 = "test1";
        String expected2 = "test2";
        iterator._parser = new JsonParser() {
            public JsonToken getCurrentToken() throws IOException { return null; }
            public JsonToken nextToken() throws IOException { return null; }
            public void clearCurrentToken() {}
            public JsonParser skipChildren() throws IOException { return null; }
            public JsonToken getCurrentTokenId() { return null; }
            public void close() throws IOException {}
        };
        iterator._deserializer = new JsonDeserializer<Object>() {
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                if(p.getCurrentToken() == null) {
                    return expected1;
                } else {
                    return expected2;
                }
            }
        };
        
        // Act
        List<Object> result = iterator.readAll();
        
        // Assert
        assertEquals(2, result.size());
        assertEquals(expected1, result.get(0));
        assertEquals(expected2, result.get(1));
    }
    
    @Test
    public void testGetParser_ReturnsParser() {
        // Arrange
        JsonParser expected = new JsonParser() {
            public JsonToken getCurrentToken() throws IOException { return null; }
            public JsonToken nextToken() throws IOException { return null; }
            public void clearCurrentToken() {}
            public JsonParser skipChildren() throws IOException { return null; }
            public JsonToken getCurrentTokenId() { return null; }
            public void close() throws IOException {}
        };
        iterator._parser = expected;
        
        // Act
        JsonParser result = iterator.getParser();
        
        // Assert
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetParserSchema_ReturnsParserSchema() {
        // Arrange
        FormatSchema expected = new FormatSchema() {};
        JsonParser parser = new JsonParser() {
            public JsonToken getCurrentToken() throws IOException { return null; }
            public JsonToken nextToken() throws IOException { return null; }
            public void clearCurrentToken() {}
            public JsonParser skipChildren() throws IOException { return null; }
            public JsonToken getCurrentTokenId() { return null; }
            public void close() throws IOException {}
            public FormatSchema getSchema() { return expected; }
        };
        iterator._parser = parser;
        
        // Act
        FormatSchema result = iterator.getParserSchema();
        
        // Assert
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetCurrentLocation_ReturnsParserCurrentLocation() {
        // Arrange
        JsonLocation expected = new JsonLocation(null, 0);
        JsonParser parser = new JsonParser() {
            public JsonToken getCurrentToken() throws IOException { return null; }
            public JsonToken nextToken() throws IOException { return null; }
            public void clearCurrentToken() {}
            public JsonParser skipChildren() throws IOException { return null; }
            public JsonToken getCurrentTokenId() { return null; }
            public void close() throws IOException {}
            public JsonLocation getCurrentLocation() { return expected; }
        };
        iterator._parser = parser;
        
        // Act
        JsonLocation result = iterator.getCurrentLocation();
        
        // Assert
        assertEquals(expected, result);
    }
    
    @Test
    public void testHandleNoSuchElementException_ThrowsNoSuchElementException() {
        try {
            // Act
            iterator._throwNoSuchElement();
        } catch (NoSuchElementException e) {
            // Assert
            assertEquals("No such element", e.getMessage());
        }
    }
    
    @Test
    public void testHandleMappingException_ThrowsRuntimeJsonMappingException() {
        JsonMappingException exception = new JsonMappingException(null, "Test");
        try {
            // Act
            iterator._handleMappingException(exception);
        } catch (RuntimeJsonMappingException e) {
            // Assert
            assertEquals("Test", e.getMessage());
            assertEquals(exception, e.getCause());
        }
    }
    
    @Test
    public void testHandleIOException_ThrowsRuntimeException() {
        IOException exception = new IOException("IO Error");
        try {
            // Act
            iterator._handleIOException(exception);
        } catch (RuntimeException e) {
            // Assert
            assertEquals("IO Error", e.getMessage());
            assertEquals(exception, e.getCause());
        }
    }
}
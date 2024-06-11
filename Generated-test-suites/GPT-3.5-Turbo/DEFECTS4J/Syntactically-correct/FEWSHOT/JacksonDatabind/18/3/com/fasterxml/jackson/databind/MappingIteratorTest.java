package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class MappingIteratorTest {

    private MappingIterator<String> mappingIterator;
    private FakeJsonParser fakeJsonParser;
    private DeserializationContext deserializationContext;
    private JsonDeserializer<String> jsonDeserializer;
    private String updatedValue;

    @Before
    public void setup() {
        fakeJsonParser = new FakeJsonParser();
        deserializationContext = new DeserializationContext();
        jsonDeserializer = new JsonDeserializer<String>();
        updatedValue = "updatedValue";
        mappingIterator = new MappingIterator<String>(fakeJsonParser, deserializationContext, jsonDeserializer, true, updatedValue);
    }

    @Test
    public void testHasNextValue_WithNoNextValue_ShouldReturnFalse() {
        fakeJsonParser.setCurrentToken(null);
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testHasNextValue_WithNextValue_ShouldReturnTrue() {
        fakeJsonParser.setCurrentToken(new JsonToken());
        assertTrue(mappingIterator.hasNextValue());
    }

    @Test(expected = RuntimeException.class)
    public void testHasNextValue_WithIOException_ShouldThrowRuntimeException() {
        fakeJsonParser.setThrowIOException(true);
        mappingIterator.hasNextValue();
    }

    @Test(expected = RuntimeException.class)
    public void testHasNextValue_WithJsonMappingException_ShouldThrowRuntimeException() {
        fakeJsonParser.setThrowJsonMappingException(true);
        mappingIterator.hasNextValue();
    }

    @Test
    public void testNextValue_WithNoNextValue_ShouldThrowNoSuchElementException() {
        fakeJsonParser.setCurrentToken(null);
        try {
            mappingIterator.nextValue();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException ex) {
            // Pass
        }
    }

    @Test
    public void testNextValue_WithNextValue_ShouldReturnNextValue() {
        fakeJsonParser.setCurrentToken(new JsonToken());
        String result = mappingIterator.nextValue();
        assertEquals(updatedValue, result);
    }

    @Test(expected = RuntimeJsonMappingException.class)
    public void testNextValue_WithJsonMappingException_ShouldThrowRuntimeJsonMappingException() {
        fakeJsonParser.setThrowJsonMappingException(true);
        mappingIterator.nextValue();
    }

    @Test(expected = RuntimeException.class)
    public void testNextValue_WithIOException_ShouldThrowRuntimeException() {
        fakeJsonParser.setThrowIOException(true);
        mappingIterator.nextValue();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove_ShouldThrowUnsupportedOperationException() {
        mappingIterator.remove();
    }

    @Test
    public void testClose_ShouldCloseJsonParser() throws Exception {
        mappingIterator.close();
        assertTrue(fakeJsonParser.isClosed());
    }

    @Test(expected = RuntimeException.class)
    public void testClose_WithIOException_ShouldThrowRuntimeException() throws Exception {
        fakeJsonParser.setThrowIOException(true);
        mappingIterator.close();
    }

    @Test
    public void testGetParser_ShouldReturnJsonParser() {
        JsonParser result = mappingIterator.getParser();
        assertEquals(fakeJsonParser, result);
    }

    @Test
    public void testGetParserSchema_ShouldReturnNull() {
        FormatSchema result = mappingIterator.getParserSchema();
        assertNull(result);
    }

    @Test
    public void testGetCurrentLocation_ShouldReturnJsonLocation() {
        JsonLocation result = mappingIterator.getCurrentLocation();
        assertNotNull(result);
    }

    @Test
    public void testReadAll_WithEmptyIterator_ShouldReturnEmptyList() throws Exception {
        fakeJsonParser.setCurrentToken(null);
        assertEquals(0, mappingIterator.readAll().size());
    }

    @Test
    public void testReadAll_WithNonEmptyIterator_ShouldReturnListWithAllValues() throws Exception {
        fakeJsonParser.setCurrentToken(new JsonToken());
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        assertEquals(3, mappingIterator.readAll().size());
    }

    @Test
    public void testReadAll_WithEmptyIteratorAndEmptyListPassedIn_ShouldReturnEmptyList() throws Exception {
        fakeJsonParser.setCurrentToken(null);
        assertEquals(0, mappingIterator.readAll(new ArrayList<String>()).size());
    }

    @Test
    public void testReadAll_WithNonEmptyIteratorAndNonEmptyListPassedIn_ShouldReturnListWithAllValues() throws Exception {
        fakeJsonParser.setCurrentToken(new JsonToken());
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        List<String> resultList = new ArrayList<String>();
        assertEquals(3, mappingIterator.readAll(resultList).size());
        assertEquals(3, resultList.size());
    }

    @Test
    public void testReadAll_WithEmptyIteratorAndEmptyCollectionPassedIn_ShouldReturnEmptyCollection() throws Exception {
        fakeJsonParser.setCurrentToken(null);
        assertEquals(0, mappingIterator.readAll(new ArrayList<String>()).size());
    }

    @Test
    public void testReadAll_WithNonEmptyIteratorAndNonEmptyCollectionPassedIn_ShouldReturnCollectionWithAllValues() throws Exception {
        fakeJsonParser.setCurrentToken(new JsonToken());
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        Set<String> resultSet = new HashSet<String>();
        assertEquals(3, mappingIterator.readAll(resultSet).size());
        assertEquals(3, resultSet.size());
    }

    private class FakeJsonParser extends JsonParser {

        private JsonToken currentToken;
        private boolean throwIOException;
        private boolean throwJsonMappingException;
        private boolean closed;

        public void setCurrentToken(JsonToken currentToken) {
            this.currentToken = currentToken;
        }

        public void setThrowIOException(boolean throwIOException) {
            this.throwIOException = throwIOException;
        }

        public void setThrowJsonMappingException(boolean throwJsonMappingException) {
            this.throwJsonMappingException = throwJsonMappingException;
        }

        @Override
        public JsonToken getCurrentToken() {
            return currentToken;
        }

        @Override
        public JsonToken nextToken() {
            return null;
        }

        @Override
        public void close() throws IOException {
            if (throwIOException) {
                throw new IOException();
            }
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }

        @Override
        public boolean isExpectedStartArrayToken() {
            return false;
        }

        @Override
        public void clearCurrentToken() {
            // pass
        }
    }

    private class DeserializationContext {
    }

    private class JsonDeserializer<T> {
        public T deserialize(JsonParser p, DeserializationContext ctxt) {
            return null;
        }

        public void deserialize(JsonParser p, DeserializationContext ctxt, T value) {
            // pass
        }
    }
}
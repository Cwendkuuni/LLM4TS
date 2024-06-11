package com.fasterxml.jackson.databind;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MappingIteratorTest {

    private MappingIterator<Integer> iterator;
    private ObjectMapper objectMapper;
    private MockJsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private JsonDeserializer<Integer> deserializer;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        jsonParser = new MockJsonParser();
        deserializationContext = objectMapper.getDeserializationContext();
        deserializer = objectMapper.getDeserializationContext().findContextualValueDeserializer(JavaType.construct(Integer.class), null);

        iterator = new MappingIterator<Integer>(JavaType.construct(Integer.class), jsonParser, deserializationContext, deserializer, true, null);
    }

    @Test
    public void testHasNextValueWhenHasNextCheckedIsFalse() throws IOException {
        assertTrue(iterator.hasNextValue());
    }

    @Test
    public void testHasNextValueWhenHasNextCheckedIsTrue() throws IOException {
        iterator.hasNextValue();
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testHasNextValueWhenParserIsNull() throws IOException {
        iterator._parser = null;
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValueWhenHasNextCheckedIsFalse() throws IOException {
        iterator._hasNextChecked = false;
        assertTrue(iterator.nextValue() instanceof Integer);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValueWhenHasNextCheckedIsFalseAndParserIsNull() throws IOException {
        iterator._hasNextChecked = false;
        iterator._parser = null;
        iterator.nextValue();
    }

    @Test
    public void testNextValueWhenHasNextCheckedIsTrue() throws IOException {
        iterator._hasNextChecked = true;
        assertTrue(iterator.nextValue() instanceof Integer);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValueWhenHasNextCheckedIsTrueAndParserIsNull() throws IOException {
        iterator._hasNextChecked = true;
        iterator._parser = null;
        iterator.nextValue();
    }
    
    @Test
    public void testReadAll() throws IOException {
        List<Integer> resultList = iterator.readAll();
        assertEquals(0, resultList.size());
    }
    
    @Test
    public void testReadAllWithListArgument() throws IOException {
        List<Integer> resultList = new ArrayList<Integer>();
        iterator.readAll(resultList);
        assertEquals(0, resultList.size());
    }
    
    @Test
    public void testReadAllWithCollectionArgument() throws IOException {
        List<Integer> resultList = new ArrayList<Integer>();
        iterator.readAll(resultList);
        assertEquals(0, resultList.size());
    }
    
    @Test
    public void testGetParser() {
        assertNotNull(iterator.getParser());
    }
    
    @Test
    public void testGetParserSchema() {
        assertNull(iterator.getParserSchema());
    }
    
    @Test
    public void testGetCurrentLocation() {
        assertNotNull(iterator.getCurrentLocation());
    }
   
    private class MockJsonParser extends JsonParser {

        @Override
        public JsonToken nextToken() throws IOException {
            return null;
        }

        @Override
        public void clearCurrentToken() {

        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public JsonToken getCurrentToken() {
            return null;
        }

        @Override
        public String getCurrentName() throws IOException {
            return null;
        }

        @Override
        public JsonLocation getCurrentLocation() {
            return null;
        }

        @Override
        public JsonToken getLastClearedToken() {
            return null;
        }

        @Override
        public JsonParser enable(JsonParser.Feature f) {
            return null;
        }

        @Override
        public JsonParser disable(JsonParser.Feature f) {
            return null;
        }

        @Override
        public boolean isEnabled(JsonParser.Feature f) {
            return false;
        }

        @Override
        public int getFeatureMask() {
            return 0;
        }

        @Override
        public ObjectCodec getCodec() {
            return null;
        }

        @Override
        public void setCodec(ObjectCodec c) {

        }

        @Override
        public int getValueAsInt() throws IOException {
            return 0;
        }

        @Override
        public int getValueAsInt(int defaultValue) throws IOException {
            return 0;
        }

        @Override
        public long getValueAsLong() throws IOException {
            return 0;
        }

        @Override
        public long getValueAsLong(long defaultValue) throws IOException {
            return 0;
        }

        @Override
        public double getValueAsDouble() throws IOException {
            return 0;
        }

        @Override
        public double getValueAsDouble(double defaultValue) throws IOException {
            return 0;
        }

        @Override
        public boolean getValueAsBoolean() throws IOException {
            return false;
        }

        @Override
        public boolean getValueAsBoolean(boolean defaultValue) throws IOException {
            return false;
        }
    }
}
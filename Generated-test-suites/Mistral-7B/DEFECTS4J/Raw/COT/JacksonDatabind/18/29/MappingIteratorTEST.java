package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonDeserializer.BeanDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class MappingIteratorTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private MappingIterator<String> iterator;
    private ObjectReader reader;
    private JsonParser parser;
    private DeserializationContext context;

    @Before
    public void setUp() throws Exception {
        reader = mapper.readerFor(List.class);
        parser = new JsonFactory().createParser("[]");
        context = reader.getDeserializationContext();
        iterator = reader.readValues(parser);
    }

    @Test
    public void testHasNext() {
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testHasNext_whenParserIsNull_thenReturnsFalse() {
        iterator = new MappingIterator<>(null, null, null, null, false, null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNext_whenParserHasNoCurrentToken_thenReturnsFalse() {
        parser = new JsonFactory().createParser("");
        iterator = reader.readValues(parser);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNext_whenParserHasEndArrayToken_thenReturnsFalse() {
        parser.nextToken();
        parser.nextToken();
        iterator = reader.readValues(parser);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        String next = iterator.next();
        assertNull(next);
        String firstValue = iterator.next();
        assertNotNull(firstValue);
        assertEquals("", firstValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        iterator.close();
        assertNull(iterator.getParser());
    }

    @Test
    public void testHasNextValue() throws IOException {
        assertTrue(iterator.hasNextValue());
        parser.nextToken();
        assertFalse(iterator.hasNextValue());
    }

    @Test(expected = IOException.class)
    public void testNextValue_whenParserIsNull_thenThrowsIOException() throws IOException {
        iterator = reader.readValues(null);
        iterator.nextValue();
    }

    @Test(expected = JsonMappingException.class)
    public void testNextValue_whenDeserializationFails_thenThrowsJsonMappingException() throws IOException {
        BeanDeserializer<String> deserializer = new BeanDeserializer<>(mapper.getTypeFactory().constructType(String.class), context);
        deserializer.setNullValueHandler(new StdDeserializer.NullValueHandler() {
            @Override
            public Object getNullValue(SerializerProvider p, DeserializationContext ctxt) throws IOException {
                throw new JsonMappingException("Test Error");
            }
        });
        ObjectReader reader = mapper.readerFor(String.class);
        iterator = reader.readValues(parser);
        iterator.nextValue();
    }

    @Test
    public void testReadAll() throws IOException {
        List<String> actual = iterator.readAll();
        List<String> expected = Arrays.asList("");
        assertEquals(expected, actual);
    }

    @Test
    public void testReadAll_whenListIsNull_thenThrowsNullPointerException() throws IOException {
        iterator.readAll(null);
    }

    @Test
    public void testReadAll_whenIteratorIsEmpty_thenReturnsEmptyList() throws IOException {
        parser.nextToken();
        parser.nextToken();
        iterator = reader.readValues(parser);
        List<String> actual = iterator.readAll();
        List<String> expected = Collections.emptyList();
        assertEquals(expected, actual);
    }

    @Test
    public void testReadAll_whenIteratorHasValues_thenReturnsListWithAllValues() throws IOException {
        List<String> expected = Arrays.asList("", "", "");
        parser.nextToken();
        parser.nextToken();
        iterator = reader.readValues(parser);
        iterator.next();
        iterator.next();
        List<String> actual = iterator.readAll();
        assertEquals(expected, actual);
    }

    @Test
    public void testReadAll_whenCollectionIsNull_thenThrowsNullPointerException() throws IOException {
        iterator.readAll((List<String>) null);
    }

    @Test
    public void testReadAll_whenIteratorIsEmpty_thenReturnsEmptyCollection() throws IOException {
        parser.nextToken();
        parser.nextToken();
        iterator = reader.readValues(parser);
        Collection<String> actual = iterator.readAll(Collections.synchronizedList(new ArrayList<>()));
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testReadAll_whenIteratorHasValues_thenReturnsCollectionWithAllValues() throws IOException {
        parser.nextToken();
        parser.nextToken();
        iterator = reader.readValues(parser);
        iterator.next();
        iterator.next();
        Collection<String> actual = iterator.readAll(Collections.synchronizedList(new ArrayList<>()));
        List<String> expected = Arrays.asList("", "", "");
        assertEquals(expected, actual);
    }
}
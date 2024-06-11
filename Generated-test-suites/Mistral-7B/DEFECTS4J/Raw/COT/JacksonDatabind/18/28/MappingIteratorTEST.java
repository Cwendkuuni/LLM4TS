package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException.ReferenceType;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private ObjectReader objectReader;
    private InputStream inputStream;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = new JsonDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        objectReader = objectMapper.readerForListOf(String.class);
        inputStream = new ByteArrayInputStream("[\"one\",\"two\",\"three\"]".getBytes());
    }

    @After
    public void teardown() throws Exception {
        if (inputStream != null) {
            inputStream.close();
        }
    }

    // Test for constructor
    @Test
    public void testMappingIteratorConstructor() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        assertNotNull(mappingIterator);
    }

    // Test for hasNext() method
    @Test
    public void testHasNext() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        assertTrue(mappingIterator.hasNext());
        assertTrue(mappingIterator.hasNext());
        assertFalse(mappingIterator.hasNext());
    }

    // Test for next() method
    @Test
    public void testNext() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        assertEquals("one", mappingIterator.next());
        assertEquals("two", mappingIterator.next());
        assertEquals("three", mappingIterator.next());
        assertNull(mappingIterator.next());
    }

    // Test for remove() method
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        mappingIterator.remove();
    }

    // Test for close() method
    @Test
    public void testClose() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        mappingIterator.close();
        assertFalse(mappingIterator.hasNext());
    }

    // Test for hasNextValue() method
    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        assertTrue(mappingIterator.hasNextValue());
        assertFalse(mappingIterator.hasNextValue());
    }

    // Test for nextValue() method
    @Test
    public void testNextValue() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        assertEquals("one", mappingIterator.nextValue());
        assertEquals("two", mappingIterator.nextValue());
        assertEquals("three", mappingIterator.nextValue());
        assertNull(mappingIterator.nextValue());
    }

    // Test for readAll() method
    @Test
    public void testReadAll() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        List<String> list = mappingIterator.readAll();
        assertEquals(3, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two", list.get(1));
        assertEquals("three", list.get(2));
    }

    // Test for readAll(ArrayList) method
    @Test
    public void testReadAllArrayList() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        ArrayList<String> list = new ArrayList<>();
        mappingIterator.readAll(list);
        assertEquals(3, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two", list.get(1));
        assertEquals("three", list.get(2));
    }

    // Test for readAll(LinkedList) method
    @Test
    public void testReadAllLinkedList() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        LinkedList<String> list = new LinkedList<>();
        mappingIterator.readAll(list);
        assertEquals(3, list.size());
        assertEquals("one", list.getFirst());
        assertEquals("two", list.get(1));
        assertEquals("three", list.getLast());
    }

    // Test for readAll(Collections) method
    @Test
    public void testReadAllCollections() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        Collections.List<String> list = Collections.list(mappingIterator.readAll(new ArrayList<>()));
        assertEquals(3, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two", list.get(1));
        assertEquals("three", list.get(2));
    }

    // Test for readAll(Vector) method
    @Test
    public void testReadAllVector() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        Vector<String> list = new Vector<>();
        mappingIterator.readAll(list);
        assertEquals(3, list.size());
        assertEquals("one", list.get(0));
        assertEquals("two", list.get(1));
        assertEquals("three", list.get(2));
    }

    // Test for getParser() method
    @Test
    public void testGetParser() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        JsonParser jsonParser = mappingIterator.getParser();
        assertNotNull(jsonParser);
    }

    // Test for getParserSchema() method
    @Test
    public void testGetParserSchema() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        FormatSchema schema = mappingIterator.getParserSchema();
        assertNotNull(schema);
    }

    // Test for getCurrentLocation() method
    @Test
    public void testGetCurrentLocation() throws IOException {
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        JsonLocation location = mappingIterator.getCurrentLocation();
        assertNotNull(location);
        assertEquals(3, location.getColumnNumber());
        assertEquals(1, location.getLineNumber());
    }

    // Test for empty iterator
    @Test
    public void testEmptyIterator() throws IOException {
        MappingIterator<String> mappingIterator = MappingIterator.emptyIterator();
        assertFalse(mappingIterator.hasNextValue());
        assertNull(mappingIterator.nextValue());
    }

    // Test for JsonMappingException when deserialization fails
    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException() throws IOException {
        ObjectReader objectReader = objectMapper.readerForListOf(String.class);
        JsonDeserializer<String> stringDeserializer = new JsonDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new JsonMappingException("Test error", _valueClass, _context, null, null, null, null, ReferenceType.VALUE);
            }
        };
        MappingIterator<String> mappingIterator = objectReader.readValues(inputStream);
        mappingIterator.nextValue();
    }

    // Test for IOException when parser fails
    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        ObjectReader objectReader = objectMapper.readerForListOf(String.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("Invalid input", new InputStreamReader("Invalid input"));
        MappingIterator<String> mappingIterator = objectReader.readValues(jsonParser);
        mappingIterator.nextValue();
    }
}
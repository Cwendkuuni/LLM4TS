package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MappingIteratorTest {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JsonFactory jsonFactory = new JsonFactoryBuilder().build();
    private static final DeserializationContext context = mapper.getDeserializationContext();
    private static final TypeFactory typeFactory = mapper.getTypeFactory();

    private JsonParser parser;
    private ObjectReader reader;
    private MappingIterator<String> iterator;
    private List<String> resultList;

    @Before
    public void setup() throws IOException {
        parser = jsonFactory.createParser("[\"apple\", \"banana\", \"cherry\"]");
        reader = mapper.readerForListOf(String.class);
        iterator = reader.readValues(parser);
        resultList = new ArrayList<>();
    }

    @Test
    public void testHasNext() {
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        assertEquals("apple", iterator.next());
        assertEquals("banana", iterator.next());
        assertEquals("cherry", iterator.next());
        assertNull(iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        iterator.close();
        assertFalse(parser.isOpen());
    }

    @Test
    public void testHasNextValue() throws IOException {
        assertTrue(iterator.hasNextValue());
        assertTrue(iterator.hasNextValue());
        assertTrue(iterator.hasNextValue());
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        assertEquals("apple", iterator.nextValue());
        assertEquals("banana", iterator.nextValue());
        assertEquals("cherry", iterator.nextValue());
        assertNull(iterator.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        iterator.readAll(resultList);
        assertEquals(3, resultList.size());
        assertEquals("apple", resultList.get(0));
        assertEquals("banana", resultList.get(1));
        assertEquals("cherry", resultList.get(2));
    }

    @Test
    public void testReadAllList() throws IOException {
        List<String> resultList = reader.readValues(parser);
        assertEquals(3, resultList.size());
        assertEquals("apple", resultList.get(0));
        assertEquals("banana", resultList.get(1));
        assertEquals("cherry", resultList.get(2));
    }

    @Test
    public void testReadAllCollection() throws IOException {
        Collection<String> resultCollection = reader.readValues(parser);
        assertEquals(3, resultCollection.size());
        assertTrue(resultCollection.contains("apple"));
        assertTrue(resultCollection.contains("banana"));
        assertTrue(resultCollection.contains("cherry"));
    }

    @Test
    public void testHasNextWithEmptyIterator() {
        iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextWithEmptyIterator() {
        iterator = MappingIterator.emptyIterator();
        iterator.next();
    }

    @Test
    public void testHasNextAfterClose() throws IOException {
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextAfterClose() throws IOException {
        iterator.close();
        iterator.next();
    }

    @Test
    public void testReadAllAfterClose() throws IOException {
        iterator.close();
        List<String> resultList = iterator.readAll(new ArrayList<>());
        assertEquals(0, resultList.size());
    }

    @Test
    public void testMappingException() throws IOException {
        parser.nextToken(); // consume START_ARRAY
        parser.nextToken(); // consume STRING
        parser.nextToken(); // consume COLON
        parser.nextToken(); // consume INT
        parser.nextToken(); // consume COMMA
        parser.nextToken(); // consume STRING
        parser.nextToken(); // consume END_OBJECT
        parser.nextToken(); // consume END_ARRAY
        parser.nextToken(); // consume EOF

        try {
            iterator.nextValue();
            fail();
        } catch (JsonMappingException e) {
            assertEquals(Reference.FIELD_NAME, e.getPath());
            assertEquals("Expected a String but was INTEGER at [Source: (ByteArrayInputStream); line: 1, column: 1]",
                    e.getMessage());
        }
    }

    @Test
    public void testIOException() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(new byte[0]));
        iterator = reader.readValues(jsonParser);

        try {
            iterator.nextValue();
            fail();
        } catch (IOException e) {
            assertEquals("Unexpected end of JSON input", e.getMessage());
        }
    }

    @Test
    public void testDeserializerException() throws IOException {
        parser.nextToken(); // consume START_ARRAY
        parser.nextToken(); // consume STRING
        parser.nextToken(); // consume COLON
        parser.nextToken(); // consume VALUE_NULL
        parser.nextToken(); // consume COMMA
        parser.nextToken(); // consume STRING
        parser.nextToken(); // consume END_OBJECT
        parser.nextToken(); // consume END_ARRAY
        parser.nextToken(); // consume EOF

        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(Map.class, null);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        MapType mapType = typeFactory.constructMapType(String.class, Integer.class);

        try {
            iterator._updatedValue = new HashMap<>();
            iterator.nextValue();
            iterator._updatedValue.put(null, 1);
            iterator.nextValue();
            iterator._deserializer.deserialize(parser, context, iterator._updatedValue);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Null value (null) in field 'Map entry: key=null, value=class [Ljava.lang.Integer;'",
                    e.getOriginalMessage());
        }

        try {
            TypeDeserializer typeDeserializer2 = mapper.getTypeDeserializerForAnnotation(Map.class, null);
            TypeSerializer typeSerializer2 = mapper.getTypeSerializer();
            MapType mapType2 = typeFactory.constructMapType(String.class, String.class);

            iterator._updatedValue = new HashMap<>();
            iterator.nextValue();
            iterator._updatedValue.put("apple", null);
            iterator.nextValue();
            iterator._deserializer.deserialize(parser, context, iterator._updatedValue);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Null value (null) in field 'Map entry: key=apple, value=null'",
                    e.getOriginalMessage());
        }
    }
}
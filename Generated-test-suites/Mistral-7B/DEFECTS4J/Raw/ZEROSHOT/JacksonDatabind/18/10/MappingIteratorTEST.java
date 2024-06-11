package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingIterator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private JsonDeserializer<Integer> intDeserializer;
    private JsonDeserializer<String> stringDeserializer;

    @BeforeMethod
    public void setUp() {
        objectMapper = new ObjectMapper();
        intDeserializer = new JsonDeserializer<Integer>(Integer.TYPE) {
            @Override
            public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        };
        stringDeserializer = new JsonDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
    }

    @Test
    public void testEmptyIterator() throws IOException {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMappingIterator() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), intDeserializer, true, null);

        List<Integer> intList = new ArrayList<>();
        iterator.readAll(intList);

        assertEquals(intList.size(), 3);
        assertEquals(intList.get(0), 1);
        assertEquals(intList.get(1), 2);
        assertEquals(intList.get(2), 3);

        assertFalse(iterator.hasNext());

        iterator.close();
        assertNull(jsonParser);
    }

    @Test
    public void testMappingIteratorWithUpdatedValue() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), intDeserializer, true, new Integer(42));

        List<Integer> intList = new ArrayList<>();
        iterator.readAll(intList);

        assertEquals(intList.size(), 3);
        assertEquals(intList.get(0), 1);
        assertEquals(intList.get(1), 2);
        assertEquals(intList.get(2), 3);

        assertEquals(iterator.nextValue(), 42);

        assertFalse(iterator.hasNext());

        iterator.close();
        assertNull(jsonParser);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testMappingIteratorWithJsonMappingException() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[\"invalid\"");
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), stringDeserializer, true, null);

        iterator.nextValue();

        iterator.close();
        assertNull(jsonParser);
    }

    @Test(expectedExceptions = IOException.class)
    public void testMappingIteratorWithIOException() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("{ \"number\": 1.5 }");
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), intDeserializer, true, null);

        iterator.nextValue();

        jsonParser.setEncoding(null);

        iterator.nextValue();

        iterator.close();
        assertNull(jsonParser);
    }

    @Test
    public void testMappingIteratorWithNullIterator() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, null, objectMapper.getDeserializationContext(), intDeserializer, true, null);

        List<Integer> intList = new ArrayList<>();
        iterator.readAll(intList);

        assertEquals(intList.size(), 3);
        assertEquals(intList.get(0), 1);
        assertEquals(intList.get(1), 2);
        assertEquals(intList.get(2), 3);

        assertFalse(iterator.hasNext());

        iterator.close();
        assertNull(jsonParser);
    }

    @Test
    public void testMappingIteratorWithNullDeserializer() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), null, true, null);

        List<Integer> intList = new ArrayList<>();
        iterator.readAll(intList);

        assertEquals(intList.size(), 3);
        assertEquals(intList.get(0), 1);
        assertEquals(intList.get(1), 2);
        assertEquals(intList.get(2), 3);

        assertFalse(iterator.hasNext());

        iterator.close();
        assertNull(jsonParser);
    }

    @Test
    public void testMappingIteratorWithNullType() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        MappingIterator<Integer> iterator = new MappingIterator<>(null, jsonParser, objectMapper.getDeserializationContext(), intDeserializer, true, null);

        assertFalse(iterator.hasNext());

        iterator.close();
        assertNull(jsonParser);
    }

    @Test
    public void testMappingIteratorWithNullContext() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, jsonParser, null, intDeserializer, true, null);

        List<Integer> intList = new ArrayList<>();
        iterator.readAll(intList);

        assertEquals(intList.size(), 3);
        assertEquals(intList.get(0), 1);
        assertEquals(intList.get(1), 2);
        assertEquals(intList.get(2), 3);

        assertFalse(iterator.hasNext());

        iterator.close();
        assertNull(jsonParser);
    }

    @Test
    public void testMappingIteratorWithNullDeserializerAndContext() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, jsonParser, null, null, true, null);

        List<Integer> intList = new ArrayList<>();
        iterator.readAll(intList);

        assertEquals(intList.size(), 3);
        assertEquals(intList.get(0), 1);
        assertEquals(intList.get(1), 2);
        assertEquals(intList.get(2), 3);

        assertFalse(iterator.hasNext());

        iterator.close();
        assertNull(jsonParser);
    }

    @Test
    public void testMappingIteratorWithNullParser() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, null, objectMapper.getDeserializationContext(), intDeserializer, true, null);

        assertEquals(iterator.nextValue(), null);

        assertFalse(iterator.hasNext());

        iterator.close();
    }

    @Test
    public void testMappingIteratorWithNullCloseParser() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), intDeserializer, false, null);

        List<Integer> intList = new ArrayList<>();
        iterator.readAll(intList);

        assertEquals(intList.size(), 3);
        assertEquals(intList.get(0), 1);
        assertEquals(intList.get(1), 2);
        assertEquals(intList.get(2), 3);

        assertFalse(iterator.hasNext());

        iterator.close();
        assertNotNull(jsonParser);
    }

    @Test
    public void testMappingIteratorWithNullUpdatedValue() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> iterator = new MappingIterator<>(javaType, jsonParser, objectMapper.getDeserializationContext(), intDeserializer, true, null);

        List<Integer> intList = new ArrayList<>();
        iterator.readAll(intList);

        assertEquals(intList.size(), 3);
        assertEquals(intList.get(0), 1);
        assertEquals(intList.get(1), 2);
        assertEquals(intList.get(2), 3);

        assertFalse(iterator.hasNext());

        iterator.close();
        assertNull(jsonParser);
    }
}
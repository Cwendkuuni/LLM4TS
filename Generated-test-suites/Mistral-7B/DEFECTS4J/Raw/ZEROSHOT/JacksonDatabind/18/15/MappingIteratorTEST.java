package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private JsonFactory jsonFactory;
    private JsonParser jsonParser;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        jsonFactory = mapper.getFactory();
        jsonParser = jsonFactory.createParser("[]");
    }

    @After
    public void tearDown() throws IOException {
        if (jsonParser != null) {
            jsonParser.close();
        }
    }

    @Test
    public void testEmptyIterator() throws IOException {
        MappingIterator<Object> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testBasicIteratorFunctionality() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> mappingIterator = new MappingIterator<>(javaType, jsonParser, mapper.getDeserializationContext(), null, true, null);

        assertEquals(Integer.class, mappingIterator.getType());
        assertEquals(DeserializationContext.class, mappingIterator.getContext());
        assertNotNull(mappingIterator.getDeserializer());
        assertTrue(mappingIterator.hasNext());
        assertEquals(1, mappingIterator.next().intValue());
        assertTrue(mappingIterator.hasNext());
        assertEquals(2, mappingIterator.next().intValue());
        assertTrue(mappingIterator.hasNext());
        assertEquals(3, mappingIterator.next().intValue());
        assertFalse(mappingIterator.hasNext());
        mappingIterator.close();
        assertFalse(mappingIterator.hasNext());
        jsonParser.close();
    }

    @Test
    public void testHasNextValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> mappingIterator = new MappingIterator<>(javaType, jsonParser, mapper.getDeserializationContext(), null, true, null);

        assertTrue(mappingIterator.hasNextValue());
        assertTrue(mappingIterator.hasNextValue());
        assertTrue(mappingIterator.hasNextValue());
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> mappingIterator = new MappingIterator<>(javaType, jsonParser, mapper.getDeserializationContext(), null, true, null);

        assertEquals(1, mappingIterator.nextValue().intValue());
        assertEquals(2, mappingIterator.nextValue().intValue());
        assertEquals(3, mappingIterator.nextValue().intValue());
        assertNull(mappingIterator.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        MappingIterator<Integer> mappingIterator = new MappingIterator<>(javaType, jsonParser, mapper.getDeserializationContext(), null, true, null);

        List<Integer> resultList = new ArrayList<>();
        mappingIterator.readAll(resultList);

        assertEquals(3, resultList.size());
        assertEquals(1, resultList.get(0).intValue());
        assertEquals(2, resultList.get(1).intValue());
        assertEquals(3, resultList.get(2).intValue());
    }

    @Test
    public void testReadAllCollection() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Integer.class);
        MappingIterator<ArrayList<Integer>> mappingIterator = new MappingIterator<>(javaType, jsonParser, mapper.getDeserializationContext(), null, true, null);

        ArrayList<Integer> resultList = new ArrayList<>();
        mappingIterator.readAll(resultList);

        assertEquals(3, resultList.size());
        assertEquals(1, resultList.get(0).intValue());
        assertEquals(2, resultList.get(1).intValue());
        assertEquals(3, resultList.get(2).intValue());
    }
}
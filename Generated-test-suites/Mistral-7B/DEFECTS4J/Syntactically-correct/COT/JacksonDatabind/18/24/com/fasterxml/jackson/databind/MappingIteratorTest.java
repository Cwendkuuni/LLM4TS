package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Named;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private JsonParser jsonParser;

    @Before
    public void setUp() throws IOException {
        jsonFactory = JsonFactoryBuilder.builder().build();
        objectMapper = new ObjectMapper();
        jsonParser = jsonFactory.getParser();
    }

    @After
    public void tearDown() throws Exception {
        if (jsonParser != null) {
            jsonParser.close();
        }
    }

    @Test
    public void testEmptyIterator() {
        Iterator<String> emptyIterator = MappingIterator.emptyIterator();
        assertTrue(emptyIterator.isEmpty());
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testBasicFunctionality() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class);

        String jsonString = "[\"hello\", \"world\"]";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(inputStream);

        Iterator<String> mappingIterator = new MappingIterator<String>(jsonFactory, jsonParser,
                objectMapper.getDeserializationContext(), stringDeserializer, false, null);

        List<String> allEntries = new ArrayList<>();
        allEntries.addAll(mappingIterator.readAll());

        assertArrayEquals(new String[]{"hello", "world"}, allEntries.toArray(new String[0]));
    }

    @Test
    public void testHasNextValueWhenHasNextIsFalse() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class);

        String jsonString = "[\"hello\", \"world\"]";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(inputStream);

        Iterator<String> mappingIterator = new MappingIterator<String>(jsonFactory, jsonParser,
                objectMapper.getDeserializationContext(), stringDeserializer, false, null);

        assertFalse(mappingIterator.hasNextValue());
        try {
            mappingIterator.nextValue();
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleMappingException() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class);

        String jsonString = "[]";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(inputStream);

        Iterator<String> mappingIterator = new MappingIterator<String>(jsonFactory, jsonParser,
                objectMapper.getDeserializationContext(), stringDeserializer, false, null);

        mappingIterator.nextValue();

        mappingIterator._parser.nextToken();
        mappingIterator._parser.parseNull();
        mappingIterator._parser.nextToken();

        mappingIterator.nextValue();
    }

    @Test(expected = IOException.class)
    public void testHandleIOException() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class);

        String jsonString = "[\"hello\", \"world\"]";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(inputStream);

        Iterator<String> mappingIterator = new MappingIterator<String>(jsonFactory, jsonParser,
                objectMapper.getDeserializationContext(), stringDeserializer, false, null);

        mappingIterator._parser.clearCurrentToken();
        mappingIterator._parser.nextToken();
        mappingIterator._parser.nextToken();
        mappingIterator._parser.nextToken();

        mappingIterator._parser.clearCurrentToken();
        mappingIterator._parser.nextToken(); // READ_TOKEN

        mappingIterator._parser.setErrorHandler(new JsonParseException.RootJsonMapExceptionHandler() {
            @Override
            public void parseMappingException(JsonMappingException p) throws IOException {
                throw new RuntimeException(p.getMessage());
            }
        });

        mappingIterator.nextValue();
    }

    @Test
    public void testReadAllMethods() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class);

        String jsonString = "[\"hello\", \"world\", \"foo\", \"bar\"]";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(inputStream);

        Iterator<String> mappingIterator = new MappingIterator<String>(jsonFactory, jsonParser,
                objectMapper.getDeserializationContext(), stringDeserializer, false, null);

        List<String> allEntries = mappingIterator.readAll();
        List<String> expectedList = Arrays.asList("hello", "world", "foo", "bar");

        assertEquals(expectedList, allEntries);

        Collection<String> collection = new ArrayList<>();
        mappingIterator.readAll(collection);

        assertEquals(expectedList, collection);

        List<String> list = new ArrayList<>();
        mappingIterator.readAll(list);

        assertEquals(expectedList, list);
    }

    @Test
    public void testGetParser() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class);

        String jsonString = "[\"hello\", \"world\", \"foo\", \"bar\"]";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(inputStream);

        Iterator<String> mappingIterator = new MappingIterator<String>(jsonFactory, jsonParser,
                objectMapper.getDeserializationContext(), stringDeserializer, false, null);

        JsonParser parser = mappingIterator.getParser();

        assertEquals(jsonParser, parser);
    }

    @Test
    public void testGetParserSchema() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class);

        String jsonString = "[\"hello\", \"world\", \"foo\", \"bar\"]";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(inputStream);

        Iterator<String> mappingIterator = new MappingIterator<String>(jsonFactory, jsonParser,
                objectMapper.getDeserializationContext(), stringDeserializer, false, null);

        FormatSchema parserSchema = mappingIterator.getParserSchema();

        assertNull(parserSchema);
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class);

        String jsonString = "[\"hello\", \"world\", \"foo\", \"bar\"]";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(inputStream);

        Iterator<String> mappingIterator = new MappingIterator<String>(jsonFactory, jsonParser,
                objectMapper.getDeserializationContext(), stringDeserializer, false, null);

        JsonLocation currentLocation = mappingIterator.getCurrentLocation();

        assertEquals(0, currentLocation.getLineNumber());
        assertEquals(0, currentLocation.getColumnNumber());
    }

    private static class StringDeserializer extends StdDeserializer<String> {

        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class MyListDeserializer extends StdDeserializer<List<String>> {

        @Override
        public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonFactory jsonFactory = p.getCodec().getFactory();
            JsonParser jsonParser = jsonFactory.getParser();
            ObjectIdGenerator idGenerator = ctxt.getIdGenerator();

            List<String> list = new ArrayList<>();

            while (jsonParser.nextValue() != null) {
                list.add(jsonParser.getValueAsString());
            }

            return list;
        }

        @Override
        public void serializeValues(List<String> v1, JsonGenerator g, SerializerProvider p) throws IOException {
            for (String value : v1) {
                g.writeString(value);
            }
        }
    }

    @Test
    public void testCustomDeserializer() throws IOException {
        Map<String, List<String>> jsonStringMap = new HashMap<>();
        jsonStringMap.put("map", Arrays.asList("one", "two", "three"));

        String jsonString = mapper.writeValueAsString(jsonStringMap);

        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());

        Iterator<Map.Entry<String, List<String>>> mappingIterator = mapper.readValues(inputStream,
                Map.Entry.class, new MyListDeserializer());

        List<Map.Entry<String, List<String>>> expectedList = Arrays.asList(
                new AbstractMap.SimpleEntry<>("map", Arrays.asList("one", "two", "three")));

        List<Map.Entry<String, List<String>>> actualList = new ArrayList<>();
        mappingIterator.readAll(actualList);

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testMappingIteratorClosingParser() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class);

        String jsonString = "[\"hello\", \"world\", \"foo\", \"bar\"]";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(inputStream);

        Iterator<String> mappingIterator = new MappingIterator<String>(jsonFactory, jsonParser,
                objectMapper.getDeserializationContext(), stringDeserializer, true, null);

        List<String> allEntries = mappingIterator.readAll();
        List<String> expectedList = Arrays.asList("hello", "world", "foo", "bar");

        assertEquals(expectedList, allEntries);

        assertFalse(jsonParser.isOpen());
    }

    @Test
    public void testMappingIteratorDoesNotCloseParserWhenCallerPassedOne() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class);

        String jsonString = "[\"hello\", \"world\", \"foo\", \"bar\"]";
        JsonParser jsonParser = new JsonFactory().createParser(jsonString);

        Iterator<String> mappingIterator = new MappingIterator<String>(jsonFactory, jsonParser,
                objectMapper.getDeserializationContext(), stringDeserializer, false, null);

        List<String> allEntries = mappingIterator.readAll();
        List<String> expectedList = Arrays.asList("hello", "world", "foo", "bar");

        assertEquals(expectedList, allEntries);

        assertTrue(jsonParser.isOpen());
    }

    @Test
    public void testMappingIteratorSkipsStartArrayWhenManagedParser() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class);

        String jsonString = "[[\"hello\", \"world\"]]";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(inputStream);

        Iterator<String> mappingIterator = new MappingIterator<String>(jsonFactory, jsonParser,
                objectMapper.getDeserializationContext(), stringDeserializer, true, null);

        List<String> allEntries = mappingIterator.readAll();
        List<String> expectedList = Arrays.asList("hello", "world");

        assertEquals(expectedList, allEntries);

        JsonLocation currentLocation = mappingIterator.getCurrentLocation();
        assertEquals(1, currentLocation.getLineNumber());
        assertEquals(1, currentLocation.getColumnNumber());
    }
}
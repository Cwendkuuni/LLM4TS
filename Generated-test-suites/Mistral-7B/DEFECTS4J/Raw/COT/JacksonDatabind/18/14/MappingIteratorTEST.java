package com.fasterxml.jackson.databind.test; // Assuming you want to test in a separate package

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StaticMap;
import com.fasterxml.jackson.databind.util.TestBase;

public class MappingIteratorTest extends TestBase {

    private JsonFactory jsonFactory;
    private ObjectMapper mapper;
    private ObjectReader reader;
    private JsonParser parser;
    private DeserializationContext context;
    private JsonDeserializer<String> stringDeserializer;
    private JsonDeserializer<Integer> integerDeserializer;
    private TypeDeserializer<Map<String, String>> mapDeserializer;
    private Converter<String, Integer> integerConverter;
    private StaticMap<String, String> map;

    @Before
    public void setup() throws Exception {
        jsonFactory = new JsonFactoryBuilder().build();
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        reader = mapper.readerForListOf(Object.class);
        parser = jsonFactory.createParser(getResourceAsStream("/mapping-iterator-test.json"));
        context = mapper.getDeserializationContext();
        stringDeserializer = createStringDeserializer();
        integerDeserializer = createIntegerDeserializer();
        mapDeserializer = createMapDeserializer();
        integerConverter = mapper.getTypeFactory().constructValueType(Integer.class, String.class);
        map = new StaticMap<>("test", "test");
    }

    @After
    public void teardown() throws Exception {
        if (parser != null) {
            parser.close();
        }
    }

    @Test
    public void testEmptyIterator() throws Exception {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testBasicIterator() throws Exception {
        MappingIterator<String> iterator = reader.iterator();
        assertTrue(iterator.hasNext());
        String value = iterator.next();
        assertNotNull(value);
        assertTrue(iterator.hasNext());
        value = iterator.next();
        assertNotNull(value);
        assertFalse(iterator.hasNext());

        iterator.close();
    }

    @Test
    public void testIOException() throws Exception {
        JsonParser fakeParser = mock(JsonParser.class);
        when(fakeParser.getCurrentToken()).thenThrow(new IOException("Test IOException"));
        MappingIterator<String> iterator = new MappingIterator<>(context.getTypeFactory().constructType(String[].class), fakeParser, context, stringDeserializer, true, null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJsonMappingException() throws Exception {
        JsonParser fakeParser = mock(JsonParser.class);
        when(fakeParser.getCurrentToken()).thenThrow(new JsonMappingException("Test JsonMappingException"));
        MappingIterator<String> iterator = new MappingIterator<>(context.getTypeFactory().constructType(String[].class), fakeParser, context, stringDeserializer, true, null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws Exception {
        MappingIterator<String> iterator = reader.iterator();
        assertTrue(iterator.hasNextValue());
        assertTrue(iterator.hasNextValue());
        assertFalse(iterator.hasNextValue());

        iterator.close();
    }

    @Test
    public void testNextValue() throws Exception {
        MappingIterator<String> iterator = reader.iterator();
        assertEquals("test", iterator.nextValue());
        assertEquals("test2", iterator.nextValue());
        assertNull(iterator.nextValue());

        iterator.close();
    }

    @Test
    public void testReadAll() throws Exception {
        MappingIterator<String> iterator = reader.iterator();
        List<String> result = new ArrayList<>();
        assertEquals(0, result.size());
        iterator.readAll(result);
        assertEquals(2, result.size());
        assertEquals("test", result.get(0));
        assertEquals("test2", result.get(1));

        iterator.close();
    }

    @Test
    public void testReadAllCollection() throws Exception {
        MappingIterator<String> iterator = reader.iterator();
        ArrayList<String> result = new ArrayList<>();
        assertEquals(0, result.size());
        iterator.readAll(result);
        assertEquals(2, result.size());
        assertEquals("test", result.get(0));
        assertEquals("test2", result.get(1));

        iterator.close();
    }

    @Test
    public void testGetParser() throws Exception {
        MappingIterator<String> iterator = reader.iterator();
        JsonParser parser = iterator.getParser();
        assertNotNull(parser);

        iterator.close();
    }

    @Test
    public void testGetParserSchema() throws Exception {
        MappingIterator<String> iterator = reader.iterator();
        FormatSchema schema = iterator.getParserSchema();
        assertNotNull(schema);

        iterator.close();
    }

    @Test
    public void testGetCurrentLocation() throws Exception {
        MappingIterator<String> iterator = reader.iterator();
        JsonLocation location = iterator.getCurrentLocation();
        assertNotNull(location);

        iterator.close();
    }

    @Test
    public void testReadAllMap() throws Exception {
        MappingIterator<Map.Entry<String, Integer>> iterator = reader.readerForMapEntries(String.class, Integer.class).iterator();
        List<Map.Entry<String, Integer>> result = new ArrayList<>();
        assertEquals(0, result.size());
        iterator.readAll(result);
        assertEquals(2, result.size());
        assertEquals(map.entry("test", 1), result.get(0));
        assertEquals(map.entry("test2", 2), result.get(1));

        iterator.close();
    }

    private JsonDeserializer<String> createStringDeserializer() throws JsonMappingException, IOException {
        return mapper.deserializerForClass(String.class);
    }

    private JsonDeserializer<Integer> createIntegerDeserializer() throws JsonMappingException, IOException {
        return mapper.deserializerForClass(Integer.class);
    }

    private TypeDeserializer<Map<String, String>> createMapDeserializer() throws JsonMappingException, IOException {
        return mapper.mapperConfig().getTypeFactory().constructTypeDeserializer(Map.class, String.class, String.class);
    }
}
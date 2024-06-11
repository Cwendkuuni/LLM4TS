package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.ListDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullDeserializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

import org.junit.Before;
import org.junit.Test;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private JsonParser parser;
    private MappingIterator<String> iterator;

    @Before
    public void setup() throws IOException {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setDefaultInstanceSerializer(String.class, new StringSerializer());
        mapper.setDefaultUseWrapper(false);
        mapper.registerDeserializer(String.class, new StringDeserializer());
        mapper.registerDeserializer(List.class, new ListDeserializer<List<String>>(String.class, null));
        mapper.registerDeserializer(Null.class, new NullDeserializer());
        parser = mapper.getFactory().createParser("[]");
        iterator = mapper.readValues(parser, String[].class).iterator();
    }

    @Test
    public void testHasNext() {
        assertTrue(iterator.hasNext());

        JsonToken token = parser.getCurrentToken();
        parser.nextToken();
        assertFalse(iterator.hasNext());
        assertSame(JsonToken.END_ARRAY, token);
    }

    @Test
    public void testNext() {
        assertEquals("", iterator.next());

        JsonToken token = parser.getCurrentToken();
        parser.nextToken();

        try {
            iterator.next();
            fail();
        } catch (NoSuchElementException e) {
            // expected
        }
        assertSame(JsonToken.END_ARRAY, token);
    }

    @Test
    public void testClose() throws IOException {
        iterator.close();
        assertFalse(iterator.hasNext());
        assertNull(parser);
    }

    @Test
    public void testHasNextValue() throws IOException {
        assertTrue(iterator.hasNextValue());

        JsonToken token = parser.getCurrentToken();
        parser.nextToken();
        assertFalse(iterator.hasNextValue());
        assertSame(JsonToken.END_ARRAY, token);
    }

    @Test
    public void testNextValue() throws IOException {
        assertEquals("", iterator.nextValue());

        JsonToken token = parser.getCurrentToken();
        parser.nextToken();

        try {
            iterator.nextValue();
            fail();
        } catch (JsonMappingException | IOException e) {
            // expected
        }
        assertSame(JsonToken.END_ARRAY, token);
    }

    @Test
    public void testReadAll() throws IOException {
        List<String> list = new ArrayList<>();
        iterator.readAll(list);
        assertEquals(0, list.size());

        assertEquals("", iterator.nextValue());
        list.add("");
        iterator.readAll(list);
        assertEquals(1, list.size());

        assertEquals("", iterator.nextValue());
        list.add("");
        iterator.readAll(list);
        assertEquals(2, list.size());

        assertFalse(iterator.hasNextValue());

        assertEquals(2, list.size());
        assertEquals("", list.get(0));
        assertEquals("", list.get(1));
    }

    @Test
    public void testReadAllCollection() throws IOException {
        Collection<String> collection = new ArrayList<>();
        iterator.readAll(collection);
        assertEquals(0, collection.size());

        assertEquals("", iterator.nextValue());
        collection.add("");
        iterator.readAll(collection);
        assertEquals(1, collection.size());

        assertEquals("", iterator.nextValue());
        collection.add("");
        iterator.readAll(collection);
        assertEquals(2, collection.size());

        assertFalse(iterator.hasNextValue());

        assertEquals(2, collection.size());
        assertEquals("", collection.iterator().next());
        assertEquals("", collection.iterator().next());
    }

    @Test
    public void testGetParser() {
        assertEquals(parser, iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        JsonLocation location = iterator.getCurrentLocation();
        assertEquals(0, location.getLineNumber());
        assertEquals(0, location.getColumnNumber());
    }
}

class StringDeserializer extends StdDeserializer<String> {
    public StringDeserializer() {
        this(null);
    }

    public StringDeserializer(Class<String> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String result = jsonParser.getValueAsString();
        jsonParser.clearCurrentToken();
        return result;
    }
}

class StringSerializer extends StdSerializer<String> {
    public StringSerializer() {
        this(null);
    }

    public StringSerializer(Class<String> t) {
        super(t);
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(value);
    }
}

class Null {
    // empty
}
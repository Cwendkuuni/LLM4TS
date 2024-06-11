package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class MappingIteratorTest extends TestBase {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeFactory typeFactory = mapper.getTypeFactory();
    private static final JsonFactory jsonFactory = new JsonFactoryBuilder().build();
    private static final JsonParser parser;
    private static final TypeSerializer valueSerializer;
    private static final TypeSerializer typeSerializer;
    private static final ObjectReader reader;

    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        parser = jsonFactory.getParser();
        valueSerializer = mapper.getValueSerializer();
        typeSerializer = mapper.getPolymorphicTypeSerializer();
        reader = mapper.readerForListOf(String.class);
    }

    // Basic Test Cases

    @Test
    public void testHasNext() throws IOException {
        JavaType javaType = typeFactory.constructType(List.class);
        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class);

        Iterator<String> iterator = reader.iterator();
        assertTrue(iterator.hasNext());

        MappingIterator<String> mappingIterator = new MappingIterator<String>(
                javaType, parser, mapper.getContext(new StandardDeserializationContext()), deserializer,
                true, null);

        assertTrue(mappingIterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws IOException {
        JavaType javaType = typeFactory.constructType(List.class);
        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class);

        Iterator<String> iterator = reader.iterator();
        assertTrue(iterator.hasNext());

        MappingIterator<String> mappingIterator = new MappingIterator<String>(
                javaType, parser, mapper.getContext(new StandardDeserializationContext()), deserializer,
                true, null);

        assertTrue(mappingIterator.hasNextValue());
    }

    @Test
    public void testNext() throws IOException {
        JavaType javaType = typeFactory.constructType(List.class);
        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class);

        Iterator<String> iterator = reader.iterator();
        String expected = iterator.next();

        MappingIterator<String> mappingIterator = new MappingIterator<String>(
                javaType, parser, mapper.getContext(new StandardDeserializationContext()), deserializer,
                true, null);

        assertEquals(expected, mappingIterator.next());
    }

    @Test
    public void testNextValue() throws IOException {
        JavaType javaType = typeFactory.constructType(List.class);
        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class);

        Iterator<String> iterator = reader.iterator();
        String expected = iterator.next();

        MappingIterator<String> mappingIterator = new MappingIterator<String>(
                javaType, parser, mapper.getContext(new StandardDeserializationContext()), deserializer,
                true, null);

        String result = mappingIterator.nextValue();
        assertEquals(expected, result);
    }

    @Test
    public void testClose() throws IOException {
        JavaType javaType = typeFactory.constructType(List.class);
        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class);

        Iterator<String> iterator = reader.iterator();
        String expected = iterator.next();

        MappingIterator<String> mappingIterator = new MappingIterator<String>(
                javaType, parser, mapper.getContext(new StandardDeserializationContext()), deserializer,
                true, null);

        mappingIterator.close();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        JavaType javaType = typeFactory.constructType(List.class);
        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class);

        Iterator<String> iterator = reader.iterator();
        String first = iterator.next();
        String second = iterator.next();

        MappingIterator<String> mappingIterator = new MappingIterator<String>(
                javaType, parser, mapper.getContext(new StandardDeserializationContext()), deserializer,
                true, null);

        mappingIterator.nextValue(); // Consume first value
        mappingIterator.remove();

        fail("Remove operation should not be supported");
    }

    // Edge Cases and Exception Handling Scenarios

    @Test(expected = JsonMappingException.class)
    public void testMappingException() throws IOException {
        JavaType javaType = typeFactory.constructType(List.class);
        JsonDeserializer<String> deserializer =
                (JsonDeserializer<String>) mapper.getDeserializerFactory().findDeserializer(String.class);
        deserializer = new JsonDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new JsonMappingException("Mapping Exception");
            }
        };

        Iterator<String> iterator = reader.iterator();
        String expected = iterator.next();

        MappingIterator<String> mappingIterator = new MappingIterator<String>(
                javaType, parser, mapper.getContext(new StandardDeserializationContext()), deserializer,
                true, null);

        mappingIterator.nextValue();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        JavaType javaType = typeFactory.constructType(List.class);
        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class);

        parser.setCodec(mapper);
        parser.setAttribute("_mapped_value", "test");
        parser.nextToken(); // Consume START_OBJECT
        parser.nextToken(); // Consume "name": null
        parser.nextToken(); // Consume "value": null
        parser.nextToken(); // Consume END_OBJECT
        parser.nextToken(); // Consume COMMA
        parser.nextToken(); // Consume START_OBJECT
        parser.nextToken(); // Consume "name": "test2"
        parser.nextToken(); // Consume "value": null
        parser.nextToken(); // Consume END_OBJECT
        parser.nextToken(); // Consume COMMA

        parser.nextToken(); // Consume END_ARRAY

        MappingIterator<String> mappingIterator = new MappingIterator<String>(
                javaType, parser, mapper.getContext(new StandardDeserializationContext()), deserializer,
                true, null);

        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue(); // Throws IOException
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElement() throws IOException {
        JavaType javaType = typeFactory.constructType(List.class);
        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class);

        Iterator<String> iterator = reader.iterator();
        String first = iterator.next();
        iterator.remove();

        MappingIterator<String> mappingIterator = new MappingIterator<String>(
                javaType, parser, mapper.getContext(new StandardDeserializationContext()), deserializer,
                true, null);

        mappingIterator.nextValue();
        mappingIterator.nextValue();

        mappingIterator.nextValue(); // Throws NoSuchElementException
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() throws IOException {
        JavaType javaType = typeFactory.constructType(List.class);
        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class);

        parser.clearCurrentToken(); // Clear current token
        parser.nextToken(); // Consume START_ARRAY

        MappingIterator<String> mappingIterator = new MappingIterator<String>(
                javaType, parser, mapper.getContext(new StandardDeserializationContext()), deserializer,
                true, null);

        mappingIterator.hasNextValue(); // Throws IllegalStateException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws IOException {
        JavaType javaType = typeFactory.constructType(List.class);
        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class);

        parser.setCodec(mapper);
        parser.nextToken(); // Consume START_OBJECT

        MappingIterator<String> mappingIterator = new MappingIterator<String>(
                javaType, parser, mapper.getContext(new StandardDeserializationContext()), deserializer,
                true, null);

        parser.nextToken(); // Consume "name": null
        parser.nextToken(); // Consume "value": null

        mappingIterator.nextValue(); // Throws IllegalArgumentException
    }
}
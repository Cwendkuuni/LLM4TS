package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MappingIteratorTest {

    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        JsonFactory jsonFactory = new JsonFactoryBuilder()
                .withoutFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .withoutFeature(SerializationFeature.FAIL_ON_EMPTY_CLASS_ID)
                .build();
        mapper = new ObjectMapper(jsonFactory);
    }

    @After
    public void cleanup() throws Exception {
        mapper.close();
    }

    @Test
    public void testEmptyIterator() throws IOException {
        MappingIterator<String> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
        assertNull(emptyIterator.next());
    }

    @Test
    public void testBasicUseCase() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[]{}));
        MappingIterator<String> iterator = new MappingIterator<String>(
                mapper.getTypeFactory().constructType(List.class), jsonParser, mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructValueDeserializer(String.class), false, null);

        // First element
        String first = iterator.next();
        assertEquals("", first);
        assertFalse(iterator.hasNext());

        // Try to get next element, should fail
        try {
            iterator.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // Expected
        }

        // Test closing iterator
        iterator.close();
        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test reading all elements
        List<String> list = iterator.readAll();
        assertEquals(1, list.size());
        assertEquals("", list.get(0));
    }

    @Test
    public void testNextValue() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[]{'[', '1'}));
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                mapper.getTypeFactory().constructType(Integer.class), jsonParser, mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructValueDeserializer(Integer.class), false, null);

        // First element
        Integer first = iterator.nextValue();
        assertEquals(1, first.intValue());
        assertFalse(iterator.hasNext());

        // Test closing iterator
        iterator.close();
        assertFalse(iterator.hasNext());
        try {
            iterator.nextValue();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test reading all elements
        List<Integer> list = iterator.readAll();
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).intValue());
    }

    @Test
    public void testJsonMappingException() throws IOException {
        // Create test case with StringDeserializer that throws JsonMappingException
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new JsonMappingException(
                        "Test JsonMappingException during deserialization");
            }
        };
        MappingIterator<String> iterator = new MappingIterator<String>(
                mapper.getTypeFactory().constructType(List.class), null, mapper.getDeserializationContext(),
                deserializer, true, null);

        // Test handling JsonMappingException
        try {
            iterator.nextValue();
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Test JsonMappingException during deserialization", e.getMessage());
        }

        // Test closing iterator
        iterator.close();
        try {
            iterator.nextValue();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testIOException() throws IOException {
        // Create test case with IOException in deserializer
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new IOException("Test IOException during deserialization");
            }
        };
        MappingIterator<String> iterator = new MappingIterator<String>(
                mapper.getTypeFactory().constructType(List.class), null, mapper.getDeserializationContext(),
                deserializer, true, null);

        // Test handling IOException
        try {
            iterator.nextValue();
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Test IOException during deserialization", e.getMessage());
        }

        // Test closing iterator
        iterator.close();
        try {
            iterator.nextValue();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testUpdateValue() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[]{'[', '1', '2'}));
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(
                mapper.getTypeFactory().constructType(Integer.class), jsonParser, mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructValueDeserializer(Integer.class), false, new Integer(1));

        Integer updatedFirst = iterator.nextValue();
        assertEquals(1, updatedFirst.intValue());
        updatedFirst.intValue() += 1;

        Integer next = iterator.nextValue();
        assertEquals(2, next.intValue());
        assertEquals(updatedFirst, next);

        List<Integer> list = iterator.readAll();
        assertEquals(2, list.size());
        assertEquals(1, list.get(0).intValue());
        assertEquals(2, list.get(1).intValue());
    }

    @Test
    public void testUpdateValueWithTypeSerializer() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[]{'[', "1", '2'}));
        MappingIterator<String> iterator = new MappingIterator<String>(
                mapper.getTypeFactory().constructType(List.class), jsonParser, mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructValueDeserializer(String.class), false, "1");

        String updatedFirst = iterator.nextValue();
        assertEquals("1", updatedFirst);
        updatedFirst = "updated";

        String next = iterator.nextValue();
        assertEquals("2", next);

        List<String> list = iterator.readAll();
        assertEquals(2, list.size());
        assertEquals("1", list.get(0));
        assertEquals("2", list.get(1));

        TypeSerializer stringSerializer = mapper.getTypeFactory().findValueSerializer(String.class, null);
        TypeSerializer stringSerializerWithValue = mapper.getTypeFactory()
                .constructTypeSerializer(String.class, mapper.getTypeFactory().constructType(String.class), null);
        TypeDeserializer stringDeserializer = mapper.getTypeFactory().constructValueDeserializer(String.class);

        ClassKey classKey = new ClassKey(Integer.TYPE);
        JsonSerializer<List<Integer>> listSerializer = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, Integer.class);
        List<Integer> expectedList = new ArrayList<>();
        expectedList.add(1);
        expectedList.add(2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectWriter objectWriter = mapper.writerWithTypeSerializer(stringSerializerWithValue);
        objectWriter.writeValue(byteArrayOutputStream, list);
        byteArrayOutputStream.reset();
        JsonParser jsonParserForSerialization = mapper.getFactory().createParser(byteArrayOutputStream);

        ObjectReader objectReader = mapper.readerForType(ArrayList.class);
        List<Integer> actualList = objectReader.readValue(jsonParserForSerialization);
        assertEquals(expectedList, actualList);

        TypeSerializer listSerializerWithValue = mapper.getTypeFactory()
                .constructTypeSerializer(List.class, mapper.getTypeFactory().constructType(ArrayList.class), null);
        TypeSerializer stringSerializerForList = mapper.getTypeFactory()
                .constructTypeSerializer(ArrayList.class, mapper.getTypeFactory().constructType(String.class), null);
        JsonSerializer<List<String>> listSerializerForMapTest = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, String.class);

        byteArrayOutputStream = new ByteArrayOutputStream();
        objectWriter = mapper.writerWithTypeSerializer(listSerializerWithValue);
        objectWriter.writeValue(byteArrayOutputStream, iterator);
        byteArrayOutputStream.reset();
        jsonParserForSerialization = mapper.getFactory().createParser(byteArrayOutputStream);

        ObjectReader objectReaderForMapTest = mapper.readerForType(MappingIterator.class);
        MappingIterator<String> actualIterator = objectReaderForMapTest.readValue(jsonParserForSerialization);
        assertSame(iterator, actualIterator);

        assertEquals(updatedFirst, actualIterator.nextValue());
        assertEquals("2", actualIterator.nextValue().toString());

        list = actualIterator.readAll();
        assertEquals(2, list.size());
        assertEquals("1", list.get(0).toString());
        assertEquals("2", list.get(1).toString());
    }

    @Test
    public void testUpdateValueWithTypeDeserializer() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream(new byte[]{'[', "1", '2'}));
        MappingIterator<String> iterator = new MappingIterator<String>(
                mapper.getTypeFactory().constructType(List.class), jsonParser, mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructValueDeserializer(String.class), false, "1");

        String updatedFirst = iterator.nextValue();
        assertEquals("1", updatedFirst);
        updatedFirst = "updated";

        String next = iterator.nextValue();
        assertEquals("2", next);

        List<String> list = iterator.readAll();
        assertEquals(2, list.size());
        assertEquals("1", list.get(0));
        assertEquals("2", list.get(1));

        TypeSerializer stringSerializer = mapper.getTypeFactory().findValueSerializer(String.class, null);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectWriter objectWriter = mapper.writerWithTypeSerializer(stringSerializer);
        objectWriter.writeValue(byteArrayOutputStream, list);
        byteArrayOutputStream.reset();
        JsonParser jsonParserForSerialization = mapper.getFactory().createParser(byteArrayOutputStream);

        ObjectReader objectReader = mapper.readerForType(ArrayList.class);
        List<String> expectedList = new ArrayList<>();
        expectedList.add("1");
        expectedList.add("2");

        List<String> actualList = objectReader.readValue(jsonParserForSerialization);
        assertEquals(expectedList, actualList);

        TypeSerializer listSerializer = mapper.getTypeFactory().constructTypeSerializer(List.class, null);
        TypeSerializer stringSerializerForList = mapper.getTypeFactory()
                .constructTypeSerializer(ArrayList.class, String.class, null);
        JsonSerializer<List<String>> listSerializerForMapTest = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, String.class);

        byteArrayOutputStream = new ByteArrayOutputStream();
        objectWriter = mapper.writerWithTypeSerializer(listSerializer);
        objectWriter.writeValue(byteArrayOutputStream, iterator);
        byteArrayOutputStream.reset();
        jsonParserForSerialization = mapper.getFactory().createParser(byteArrayOutputStream);

        ObjectReader objectReaderForMapTest = mapper.readerForType(MappingIterator.class);
        MappingIterator<String> actualIterator = objectReaderForMapTest.readValue(jsonParserForSerialization);
        assertSame(iterator, actualIterator);

        assertEquals(updatedFirst, actualIterator.nextValue());
        assertEquals("2", actualIterator.nextValue());

        list = actualIterator.readAll();
        assertEquals(2, list.size());
        assertEquals("1", list.get(0));
        assertEquals("2", list.get(1));
    }
}
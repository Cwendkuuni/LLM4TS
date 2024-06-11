package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Test cases for {@link CollectionDeserializer} class.
 */
public class CollectionDeserializerTest {

    @Test
    public void testEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);
        Collection<Object> collection = mapper.readerForListOf(Object.class)
                .readValue(mapper.readTree("[]"));
        assertEquals(Collections.emptyList(), collection);
        assertEquals(0, collection.size());
    }

    @Test
    public void testNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);
        Collection<Object> collection = mapper.readerForListOf(Object.class)
                .readValue(mapper.readTree("null"));
        assertEquals(Collections.singletonList(null), collection);
        assertEquals(1, collection.size());
    }

    @Test
    public void testSingleValueDeserializedAsSingleElementCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(stringDeserializer, stringDeserializer, null);

        JsonParser parser = mapper.getFactory().createParser("\"Hello\"");
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Collections.singletonList("Hello"), collection);
        assertEquals(1, collection.size());
    }

    @Test
    public void testSingleValueDeserializedAsSingleElementArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);

        JsonParser parser = mapper.getFactory().createParser("[\n" +
                "  \"Hello\"\n" +
                "]");
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Collections.singletonList("Hello"), collection);
        assertEquals(1, collection.size());
    }

    @Test
    public void testNullValueDeserializedAsEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);

        JsonParser parser = mapper.getFactory().createParser("null");
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Collections.emptyList(), collection);
        assertEquals(0, collection.size());
    }

    @Test
    public void testArrayWithSingleNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);

        JsonParser parser = mapper.getFactory().createParser("[\n" +
                "  null\n" +
                "]");
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Collections.singletonList(null), collection);
        assertEquals(1, collection.size());
    }

    @Test
    public void testArrayWithEmptyElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(stringDeserializer, stringDeserializer, null);

        JsonParser parser = mapper.getFactory().createParser("[\n" +
                "  null\n" +
                "]");
        parser.nextToken(); // consume [

        JsonParser emptyElementParser = mapper.getFactory().createParser("null");
        deserializer.deserialize(emptyElementParser, null); // consume null

        JsonParser nextElementParser = mapper.getFactory().createParser("\"Hello\"");
        JsonToken nextToken = nextElementParser.nextToken();
        String nextElement = nextToken == JsonToken.VALUE_STRING ? nextElementParser.getText() : null;

        parser.nextToken(); // consume ]
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Collections.singletonList(null), collection);
        assertEquals(1, collection.size());

        assertTrue(nextElementParser.hasUneatenText());
        String nextElementString = nextElementParser.getText();
        assertEquals("Hello", nextElementString);
        assertEquals("Hello", nextElement);
        assertEquals("Hello", deserializer.deserialize(nextElementParser, null).toString());
        assertEquals("Hello", deserializer.deserializeWithType(nextElementParser, null, String.class).toString());
    }

    @Test
    public void testArrayWithSingleElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(stringDeserializer, stringDeserializer, null);

        JsonParser parser = mapper.getFactory().createParser("[\n" +
                "  \"Hello\"\n" +
                "]");
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Collections.singletonList("Hello"), collection);
        assertEquals(1, collection.size());

        JsonToken nextToken = parser.nextToken();
        assertTrue(nextToken == JsonToken.END_ARRAY || nextToken == JsonToken.END_DOCUMENT);
    }

    @Test
    public void testArrayWithMultipleElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> integerDeserializer = mapper.findValueDeserializer(Integer.class);
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(integerDeserializer, integerDeserializer, null);

        JsonParser parser = mapper.getFactory().createParser("[\n" +
                "  1\n" +
                "  2\n" +
                "  3\n" +
                "]");
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList(1, 2, 3), collection);
        assertEquals(3, collection.size());

        JsonToken nextToken = parser.nextToken();
        assertTrue(nextToken == JsonToken.END_ARRAY || nextToken == JsonToken.END_DOCUMENT);
    }

    @Test
    public void testArrayWithNullAndMultipleElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> integerDeserializer = mapper.findValueDeserializer(Integer.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(integerDeserializer, integerDeserializer, null);
        deserializer = deserializer.withResolved(stringDeserializer, stringDeserializer, null);

        JsonParser parser = mapper.getFactory().createParser("[\n" +
                "  null\n" +
                "  1\n" +
                "  \"Hello\"\n" +
                "  3\n" +
                "]");
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList(null, 1, "Hello", 3), collection);
        assertEquals(4, collection.size());

        JsonToken nextToken = parser.nextToken();
        assertTrue(nextToken == JsonToken.END_ARRAY || nextToken == JsonToken.END_DOCUMENT);
    }

    @Test
    public void testArrayWithNullAndEmptyArrayAsElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> integerDeserializer = mapper.findValueDeserializer(Integer.class);
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(integerDeserializer, integerDeserializer, null);

        JsonParser parser = mapper.getFactory().createParser("[\n" +
                "  null\n" +
                "  []\n" +
                "]");
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList(null, Collections.emptyList()), collection);
        assertEquals(2, collection.size());

        JsonToken nextToken = parser.nextToken();
        assertTrue(nextToken == JsonToken.END_ARRAY || nextToken == JsonToken.END_DOCUMENT);
    }

    @Test
    public void testArrayWithEmptyArraysAsElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);

        JsonParser parser = mapper.getFactory().createParser("[\n" +
                "  []\n" +
                "  []\n" +
                "]");
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList(Collections.emptyList(), Collections.emptyList()), collection);
        assertEquals(2, collection.size());

        JsonToken nextToken = parser.nextToken();
        assertTrue(nextToken == JsonToken.END_ARRAY || nextToken == JsonToken.END_DOCUMENT);
    }

    @Test
    public void testArrayWithMultipleEmptyArrays() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> integerDeserializer = mapper.findValueDeserializer(Integer.class);
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(integerDeserializer, integerDeserializer, null);

        JsonParser parser = mapper.getFactory().createParser("[\n" +
                "  []\n" +
                "  []\n" +
                "  []\n" +
                "]");
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList(Collections.emptyList(), Collections.emptyList(), Collections.emptyList()), collection);
        assertEquals(3, collection.size());

        JsonToken nextToken = parser.nextToken();
        assertTrue(nextToken == JsonToken.END_ARRAY || nextToken == JsonToken.END_DOCUMENT);
    }

    @Test
    public void testArrayWithSingleEmptyArrayAsElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);

        JsonParser parser = mapper.getFactory().createParser("[\n" +
                "  []\n" +
                "  1\n" +
                "]");
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList(Collections.emptyList(), 1), collection);
        assertEquals(2, collection.size());

        JsonToken nextToken = parser.nextToken();
        assertTrue(nextToken == JsonToken.END_ARRAY || nextToken == JsonToken.END_DOCUMENT);
    }

    @Test
    public void testPolymorphicArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> intDeserializer = mapper.findValueDeserializer(Integer.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(intDeserializer, intDeserializer, null);
        deserializer = deserializer.withResolved(stringDeserializer, stringDeserializer, null);

        JsonParser parser = mapper.getFactory().createParser("[\n" +
                "  1\n" +
                "  \"Hello\"\n" +
                "]");
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList(1, "Hello"), collection);
        assertEquals(2, collection.size());

        JsonToken nextToken = parser.nextToken();
        assertTrue(nextToken == JsonToken.END_ARRAY || nextToken == JsonToken.END_DOCUMENT);
    }

    @Test
    public void testArrayWithUnresolvedForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> intDeserializer = mapper.findValueDeserializer(Integer.class);
        CollectionDeserializer deserializer = mapper.findDeserializer(Collection.class);
        deserializer = deserializer.withResolved(intDeserializer, intDeserializer, null);

        // Create a forward reference
        JsonParser referenceParser = mapper.getFactory().createParser("{\"value\": 1}");
        ObjectReference reference = deserializer.handleUnknownProperty("root", referenceParser, null);

        // Deserialize an array with a forward reference as an element
        JsonParser parser = mapper.getFactory().createParser("[\n" +
                "  {\"$ref\": \"" + reference.getObjectId().getId() + "\"}\n" +
                "  1\n" +
                "]");
        Collection<Object> collection = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList(new ObjectReference(reference), 1), collection);
        assertEquals(2, collection.size());

        JsonToken nextToken = parser.nextToken();
        assertTrue(nextToken == JsonToken.END_ARRAY || nextToken == JsonToken.END_DOCUMENT);
    }
}
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefaultDeserializer() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MapperTestUtil.simpleCollectionType(String.class), null, null);

        JsonParser parser = MAPPER.getFactory().createParser("[]");
        Collection<String> collection = deserializer.deserialize(parser, null);
        assertNotNull(collection);
        assertTrue(collection.isEmpty());

        parser = MAPPER.getFactory().createParser("[\"foo\",\"bar\"]");
        collection = deserializer.deserialize(parser, null);
        List<String> expected = new ArrayList<>();
        expected.add("foo");
        expected.add("bar");
        assertEquals(expected, collection);
    }

    @Test
    public void testCustomValueDeserializer() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MapperTestUtil.simpleCollectionType(String.class), null, null,
                new CustomStringDeserializer(), null, null);

        JsonParser parser = MAPPER.getFactory().createParser("[]");
        Collection<String> collection = deserializer.deserialize(parser, null);
        assertNotNull(collection);
        assertTrue(collection.isEmpty());

        parser = MAPPER.getFactory().createParser("[\"foo\",\"bar\"]");
        collection = deserializer.deserialize(parser, null);
        List<String> expected = new ArrayList<>();
        expected.add("FOO");
        expected.add("BAR");
        assertEquals(expected, collection);
    }

    @Test
    public void testDelegateDeserializer() throws IOException {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("delegate", new ObjectNode()
                .put("class", JsonDeserializer.class.getName())
                .put("value", "$(class: java.util.ArrayList)"));

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MapperTestUtil.simpleCollectionType(String.class), null,
                new DelegatingDeserializer(MapperTestUtil.simpleCollectionType(String.class), null),
                null, null, null);

        Collection<String> collection = deserializer.deserialize(MAPPER.readTree(node), null);
        List<String> expected = new ArrayList<>();
        expected.add("foo");
        expected.add("bar");
        assertEquals(expected, collection);
    }

    @Test
    public void testNullValueProvider() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MapperTestUtil.simpleCollectionType(String.class), null, null, null,
                new NullStringProvider(), null);

        JsonParser parser = MAPPER.getFactory().createParser("[null]");
        Collection<String> collection = deserializer.deserialize(parser, null);
        List<String> expected = new ArrayList<>();
        expected.add(null);
        assertEquals(expected, collection);
    }

    @Test
    public void testContentConverters() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MapperTestUtil.simpleCollectionType(Integer.class), null, null, null, null, Boolean.TRUE);

        JsonParser parser = MAPPER.getFactory().createParser("[1,2,3]");
        Collection<Integer> collection = deserializer.deserialize(parser, null);
        List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        assertEquals(expected, collection);
    }

    @Test(expected = JsonProcessingException.class)
    public void testUnexpectedToken() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MapperTestUtil.simpleCollectionType(String.class), null, null, null, null, null);

        JsonParser parser = MAPPER.getFactory().createParser("{foo: \"bar\"}");
        deserializer.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullDeserializer() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MapperTestUtil.simpleCollectionType(String.class), null, null, null, null, null);

        JsonDeserializer<String> nullDeserializer = null;
        deserializer.withResolved(nullDeserializer, null, null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValueDeserializer() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                MapperTestUtil.simpleCollectionType(String.class), null, null, null, null, null);

        JsonDeserializer<Object> nullDeserializer = null;
        deserializer.withResolved(nullDeserializer, null, null, null);
    }

    private static class CustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String result = p.nextTextValue();
            return result.toUpperCase();
        }
    }

    private static class DelegatingDeserializer extends JsonDeserializer<Collection<String>> {
        private final CollectionType _valueType;

        public DelegatingDeserializer(CollectionType valueType, ValueInstantiator valueInstantiator) {
            super(valueType, valueInstantiator);
            _valueType = valueType;
        }

        @Override
        public Collection<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new ArrayList<>((List<?>) _valueDeserializer.deserialize(p, ctxt));
        }
    }

    private static class NullStringProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) {
            return null;
        }
    }
}
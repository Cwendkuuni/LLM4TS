package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TestBase;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for StringCollectionDeserializer.
 */
public class StringCollectionDeserializerTest extends TestBase {

    @Test
    public void testEmptyCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[]");
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, JsonToken.START_ARRAY);
        Collection<String> collection = deserializer.deserialize(jsonNode, null, null);
        assertEquals(Collections.emptyList(), collection);
    }

    @Test
    public void testSingleString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("['foo']");
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, JsonToken.START_ARRAY);
        Collection<String> collection = deserializer.deserialize(jsonNode, null, null);
        assertEquals(Collections.singletonList("foo"), collection);
    }

    @Test
    public void testMultipleStrings() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("['foo', 'bar', 'baz']");
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, JsonToken.START_ARRAY);
        Collection<String> collection = deserializer.deserialize(jsonNode, null, null);
        List<String> expected = new ArrayList<>();
        expected.add("foo");
        expected.add("bar");
        expected.add("baz");
        assertEquals(expected, collection);
    }

    @Test
    public void testNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("['null']");
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, JsonToken.START_ARRAY);
        Collection<String> collection = deserializer.deserialize(jsonNode, null, null);
        assertNull(collection);
    }

    @Test
    public void testNullValueProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setNullValueProvider(null);
        JsonNode jsonNode = mapper.readTree("['null']");
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, JsonToken.START_ARRAY);
        Collection<String> collection = deserializer.deserialize(jsonNode, null, null);
        assertNotNull(collection);
        assertEquals(Collections.singletonList("null"), collection);
    }

    @Test
    public void testStringCollectionWithCustomDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("['foo', 'bar']");

        StringDeserializer stringDeserializer = new StringDeserializer();
        ValueInstantiator valueInstantiator = mapper.getValueInstantiator();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class, String.class),
                valueInstantiator,
                null,
                stringDeserializer,
                null,
                null);

        Collection<String> collection = deserializer.deserialize(jsonNode, null, null);
        List<String> expected = new ArrayList<>();
        expected.add("foo");
        expected.add("bar");
        assertEquals(expected, collection);
    }

    @Test
    public void testStringCollectionWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("['foo', 'bar']");
        JsonDeserializer<Object> delegateDeserializer = new ObjectDeserializer();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class, String.class),
                null,
                delegateDeserializer,
                null,
                null,
                null);

        Collection<String> collection = deserializer.deserialize(jsonNode, null, null);
        List<String> expected = new ArrayList<>();
        expected.add("foo");
        expected.add("bar");
        assertEquals(expected, collection);
    }

    @Test
    public void testStringCollectionWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("['foo', 'bar']");
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class, String.class),
                null,
                null,
                valueDeserializer,
                null,
                null);

        Collection<String> collection = deserializer.deserialize(jsonNode, null, null);
        List<String> expected = new ArrayList<>();
        expected.add("foo");
        expected.add("bar");
        assertEquals(expected, collection);
    }

    @Test
    public void testStringCollectionWithNullValueProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setNullValueProvider(new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return "null";
            }
        });
        JsonNode jsonNode = mapper.readTree("['null']");
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, JsonToken.START_ARRAY);
        Collection<String> collection = deserializer.deserialize(jsonNode, null, null);
        assertEquals(Collections.singletonList("null"), collection);
    }
}

class StringDeserializer extends StdDeserializer<String> {

    public StringDeserializer() {
        this(null);
    }

    protected StringDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}

class ObjectDeserializer extends StdDeserializer<Object> {
    public ObjectDeserializer() {
        this(null);
    }

    protected ObjectDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsToken();
    }
}
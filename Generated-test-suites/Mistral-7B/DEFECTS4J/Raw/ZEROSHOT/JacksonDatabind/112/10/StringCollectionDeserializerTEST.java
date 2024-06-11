package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    @Test
    public void testEmptyStringCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        Collection<String> expected = new ArrayList<>();

        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(String[].class, null);
        Collection<String> actual = deserializer.createContextual(mapper.getDeserializationContext(), null).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test
    public void testStringCollectionDeserializationWithStandardDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"test1\", \"test2\", \"test3\"]";
        Collection<String> expected = new ArrayList<>();
        expected.add("test1");
        expected.add("test2");
        expected.add("test3");

        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, String.class);
        Collection<String> actual = deserializer.createContextual(mapper.getDeserializationContext(), null).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test
    public void testStringCollectionDeserializationWithCustomDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"value\":\"test1\"}, {\"value\":\"test2\"}, {\"value\":\"test3\"}]";

        JsonDeserializer<String> customDeserializer = mapper.getFactory().createValueDeserializer(String.class, new SimpleModule().addDeserializer(String.class, new StringDeserializer()));
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, String.class);
        deserializer = (StringCollectionDeserializer) deserializer.withResolved(null, customDeserializer, null, null);

        Collection<String> actual = deserializer.createContextual(mapper.getDeserializationContext(), null).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());

        List<String> expected = new ArrayList<>();
        expected.add("test1");
        expected.add("test2");
        expected.add("test3");

        assertEquals(expected, actual);
    }

    @Test
    public void testNullValueHandling() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        Collection<String> expected = new ArrayList<>();
        expected.add(null);

        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, String.class);
        Collection<String> actual = deserializer.createContextual(mapper.getDeserializationContext(), null).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        Collection<String> expected = new ArrayList<>();

        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, String.class);

        deserializer.createContextual(mapper.getDeserializationContext(), null).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test
    public void testEmptyValueHandling() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"\", \"\", \"\"]";
        Collection<String> expected = new ArrayList<>();

        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, String.class);
        Collection<String> actual = deserializer.createContextual(mapper.getDeserializationContext(), null).deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<String> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}
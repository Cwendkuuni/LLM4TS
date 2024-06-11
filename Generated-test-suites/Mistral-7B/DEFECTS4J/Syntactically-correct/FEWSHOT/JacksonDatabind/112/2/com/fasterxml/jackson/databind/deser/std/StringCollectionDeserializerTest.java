package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Test cases for {@link StringCollectionDeserializer}
 */
@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    private static final String JSON_STRING_ARRAY = "[\"foo\",\"bar\",\"baz\"]";
    private static final String JSON_STRING_ELEMENT = "\"foo\"";
    private static final String JSON_STRING_ARRAY_EMPTY = "[]";
    private static final String JSON_NULL = "null";

    @Test
    public void testDefault() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, String.class), null, null);

        JsonParser parser = factory.createParser(JSON_STRING_ARRAY);
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);

        parser = factory.createParser(JSON_STRING_ARRAY_EMPTY);
        result = deserializer.deserialize(parser, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testValueDeserializer() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonDeserializer<String> valueDeserializer = new StdDeserializer<String>() {};
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, String.class), null, null, null, valueDeserializer, null);

        JsonParser parser = factory.createParser(JSON_STRING_ARRAY);
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
    }

    @Test
    public void testDelegateDeserializer() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> delegateDeserializer = mapper.deserializerForClass(List.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, String.class), null, delegateDeserializer, null, null, null);

        JsonParser parser = factory.createParser(JSON_STRING_ARRAY);
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
    }

    @Test
    public void testNullValueProvider() throws IOException {
        JsonFactory factory = new JsonFactory();
        NullValueProvider nullValueProvider = new StandardNullValueProvider();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, String.class), null, null, null, null, null);
        deserializer.setNullProvider(nullValueProvider);

        JsonParser parser = factory.createParser(JSON_NULL);
        Collection<String> result = deserializer.deserialize(parser, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testValueInstantiator() throws IOException {
        JsonFactory factory = new JsonFactory();
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, String.class), valueInstantiator, null, null, null, null);

        JsonParser parser = factory.createParser(JSON_STRING_ARRAY);
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
    }

    @Test
    public void testEmptyArray() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, String.class), null, null, null, null, null);

        JsonParser parser = factory.createParser(JSON_STRING_ARRAY_EMPTY);
        Collection<String> result = deserializer.deserialize(parser, null);
        assertTrue(result.isEmpty());
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArray() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, String.class), null, null, null, null, null);

        JsonParser parser = factory.createParser(JSON_STRING_ELEMENT);
        deserializer.deserialize(parser, null);
    }

    @Test
    public void testFormatFeature() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, String.class), null, null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> valueDeserializer = mapper.deserializerForClass(String.class);
        deserializer = deserializer.withResolved(null, valueDeserializer, null, true);

        JsonParser parser = factory.createParser(JSON_STRING_ARRAY);
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);

        parser = factory.createParser(JSON_STRING_ELEMENT);
        result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo"), result);
    }

    @Test
    public void testCustomDeserializer() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonDeserializer<String> valueDeserializer = new StdDeserializer<String>() {};
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, String.class), null, null, null, valueDeserializer, null);

        JsonParser parser = factory.createParser(JSON_STRING_ARRAY);
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
    }

    @Test
    public void testCustomDeserializerWithDelegate() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonDeserializer<Object> delegateDeserializer = new StdDeserializer<Object>() {};
        JsonDeserializer<String> valueDeserializer = new StdDeserializer<String>() {};
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, String.class), null, delegateDeserializer, null, valueDeserializer, null);

        JsonParser parser = factory.createParser(JSON_STRING_ARRAY);
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
    }

    @Test
    public void testContentConverter() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, Integer.class), null, null, null, null, null);

        JsonParser parser = factory.createParser(JSON_STRING_ARRAY);
        Collection<Integer> result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    /**
     * Test case for {@link StringCollectionDeserializer#deserializeUsingCustom(JsonParser, DeserializationContext, Collection, JsonDeserializer)}
     */
    @Test
    public void testCustomDeserializerUsingCustom() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonDeserializer<String> valueDeserializer = new StdDeserializer<String>() {};
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, String.class), null, null, null, valueDeserializer, null);

        JsonParser parser = factory.createParser(JSON_STRING_ARRAY);
        Collection<String> result = deserializer.deserializeUsingCustom(parser, null, new ArrayList<>(), valueDeserializer);
        assertEquals(Arrays.asList("foo", "bar", "baz"), result);
    }

    /**
     * Test case for {@link StringCollectionDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
     */
    @Test
    public void testDeserializeWithType() throws IOException {
        JsonFactory factory = new JsonFactory();
        TypeDeserializer typeDeserializer = new StdTypeDeserializer(factory.constructType(List.class));
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, String.class), null, null, null, null, null);

        JsonParser parser = factory.createParser(JSON_STRING_ARRAY);
        Object result = deserializer.deserializeWithType(parser, null, typeDeserializer);
        assertTrue(result instanceof List);
        List<String> listResult = (List<String>) result;
        assertEquals(Arrays.asList("foo", "bar", "baz"), listResult);
    }

    /**
     * Test case for {@link StringCollectionDeserializer#handleNonArray(JsonParser, DeserializationContext, Collection)}
     */
    @Test
    public void testHandleNonArray() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                factory.constructType(Collection.class, String.class), null, null, null, null, null);

        JsonParser parser = factory.createParser(JSON_STRING_ELEMENT);
        Collection<String> result = deserializer.handleNonArray(parser, null, new ArrayList<>());
        assertEquals(Arrays.asList("foo"), result);
    }
}
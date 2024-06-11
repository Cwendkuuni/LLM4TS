package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class StringCollectionDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testEmptyCollection() throws IOException {
        String json = "[]";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(StringCollectionDeserializer.class);
        Collection<String> result = mapper.readerForListOf(String.class).readValue(node);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testStringCollectionWithStandardDeserializer() throws IOException {
        String json = "[\"foo\",\"bar\"]";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        Collection<String> result = mapper.readerForListOf(String.class).readValue(node);
        assertEquals(Collections.singletonList("foo"), result);
        assertEquals("foo", result.iterator().next());

        result = mapper.readerForListOf(String.class).readValue(node);
        assertEquals(Collections.singletonList("bar"), result);
        assertEquals("bar", result.iterator().next());
    }

    @Test
    public void testStringCollectionWithCustomDeserializer() throws IOException {
        String json = "[\"foo\",null,\"bar\"]";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        JsonDeserializer<String> valueDeserializer = new CustomStringDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class, String.class),
                null, null, null, null, null);
        deserializer._valueDeserializer = valueDeserializer;
        Collection<String> result = mapper.readerForListOf(String.class).readValue(node, deserializer);
        assertEquals(Collections.singletonList("foo"), result);
        assertEquals("foo", result.iterator().next());

        result = mapper.readerForListOf(String.class).readValue(node, deserializer);
        assertNull(result.iterator().next());

        result = mapper.readerForListOf(String.class).readValue(node, deserializer);
        assertEquals(Collections.singletonList("bar"), result);
        assertEquals("bar", result.iterator().next());
    }

    @Test
    public void testStringCollectionWithMultipleNullValues() throws IOException {
        String json = "[null, null]";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        Collection<String> result = mapper.readerForListOf(String.class).readValue(node);
        assertEquals(Collections.singletonList(null), result);
        assertNull(result.iterator().next());

        result = mapper.readerForListOf(String.class).readValue(node);
        assertEquals(Collections.singletonList(null), result);
        assertNull(result.iterator().next());
    }

    @Test
    public void testStringCollectionWithEmptyString() throws IOException {
        String json = "[\"\",\"foo\"]";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        Collection<String> result = mapper.readerForListOf(String.class).readValue(node);
        assertEquals(Collections.singletonList(""), result);
        assertEquals("", result.iterator().next());

        result = mapper.readerForListOf(String.class).readValue(node);
        assertEquals(Collections.singletonList("foo"), result);
        assertEquals("foo", result.iterator().next());
    }

    @Test
    public void testStringCollectionWithNullValueProvider() throws IOException {
        ObjectMapper mapperWithNullProvider = new ObjectMapper();
        mapperWithNullProvider.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapperWithNullProvider.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapperWithNullProvider.setSerializerProvider(mapper.getSerializerProvider());
        mapperWithNullProvider.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);

        String json = "[null, null]";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        CustomNullValueProvider nullValueProvider = new CustomNullValueProvider();
        Collection<String> result = mapperWithNullProvider.readerForListOf(String.class).readValue(node, nullValueProvider);
        assertNull(result.iterator().next());
        assertNull(result.iterator().next());
    }

    @Test
    public void testStringCollectionWithBooleanUnwrapSingle() throws IOException {
        String json = "[\"foo\",\"bar\"]";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        Collection<String> result = mapper.readerForListOf(String.class)
                .readValue(node, new StringCollectionDeserializer(
                        mapper.constructType(Collection.class, String.class),
                        null, null, null, null, Boolean.TRUE));
        List<String> expected = new ArrayList<>();
        expected.add("foo");
        expected.add("bar");
        assertEquals(expected, result);

        String jsonWithSingleValue = "[\"foo\"]";
        ObjectNode nodeWithSingleValue = mapper.readValue(jsonWithSingleValue, ObjectNode.class);
        Collection<String> resultWithSingleValue = mapper.readerForListOf(String.class)
                .readValue(nodeWithSingleValue, new StringCollectionDeserializer(
                        mapper.constructType(Collection.class, String.class),
                        null, null, null, null, Boolean.TRUE));
        List<String> expectedWithSingleValue = new ArrayList<>();
        expectedWithSingleValue.add("foo");
        assertEquals(expectedWithSingleValue, resultWithSingleValue);
    }

    @Test
    public void testStringCollectionWithJsonFormatFeature() throws IOException {
        String json = "[\"foo\",\"bar\"]";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        JsonFormat.Feature feature = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
        Collection<String> result = mapper.readerForListOf(String.class)
                .readValue(node, new StringCollectionDeserializer(
                        mapper.constructType(Collection.class, String.class),
                        null, null, null, null, null));
        List<String> expected = new ArrayList<>();
        expected.add("foo");
        expected.add("bar");
        assertEquals(expected, result);

        ObjectMapper mapperWithFeature = new ObjectMapper();
        mapperWithFeature.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapperWithFeature.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        Collection<String> resultWithFeature = mapperWithFeature.readerForListOf(String.class)
                .readValue(node);
        assertEquals(Collections.singletonList("foo"), resultWithFeature);
        assertEquals("foo", resultWithFeature.iterator().next());

        resultWithFeature = mapperWithFeature.readerForListOf(String.class)
                .readValue(node);
        assertEquals(Collections.singletonList("bar"), resultWithFeature);
        assertEquals("bar", resultWithFeature.iterator().next());
    }

    @Test
    public void testStringCollectionWithDelegateDeserializer() throws IOException {
        String json = "[\"foo\",null,\"bar\"]";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        JsonDeserializer<Object> delegateDeserializer = mapper.deserializerForClass(Object.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class, String.class),
                null, delegateDeserializer, null, null, null);
        Collection<String> result = mapper.readerForListOf(String.class).readValue(node, deserializer);
        assertEquals(Collections.singletonList("foo"), result);
        assertEquals("foo", result.iterator().next());

        result = mapper.readerForListOf(String.class).readValue(node, deserializer);
        assertNull(result.iterator().next());

        result = mapper.readerForListOf(String.class).readValue(node, deserializer);
        assertEquals(Collections.singletonList("bar"), result);
        assertEquals("bar", result.iterator().next());

        ObjectNode nodeWithArray = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.valueToTree(new Object[] { new Object(), new Object() });
        nodeWithArray.set("items", arrayNode);
        Collection<String> resultWithArray = mapper.readerForListOf(String.class).readValue(nodeWithArray, deserializer);
        List<String> expectedWithArray = new ArrayList<>();
        expectedWithArray.add("com.fasterxml.jackson.core.json.JsonFactory$1@5df5d9");
        expectedWithArray.add("com.fasterxml.jackson.core.json.JsonFactory$1@4154e8");
        assertEquals(expectedWithArray, resultWithArray);
    }

    private static class CustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String text = p.nextTextValue();
            if (text.equals("null")) {
                return null;
            }
            return text.toLowerCase();
        }
    }

    private static class CustomNullValueProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) {
            return null;
        }
    }

    private String _parseString(JsonParser p, DeserializationContext ctxt) throws IOException {
        return mapper.readValue(p, String.class);
    }
}
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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@SuppressWarnings("MagicNumber")
public class StringCollectionDeserializerTest {

    private ObjectMapper mapper;
    private StringCollectionDeserializer deserializer;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
    }

    @Test
    public void testEmptyStringCollectionDeserialization() throws Exception {
        JsonNode emptyJson = mapper.readTree("[]");
        List<String> result = mapper.treeToValue(deserializer.createContextual(mapper.getDeserializationContext(), null), emptyJson);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testSingleStringDeserialization() throws Exception {
        String testString = "test";
        JsonNode singleJson = mapper.valueToTree(testString);
        List<String> result = mapper.treeToValue(deserializer.createContextual(mapper.getDeserializationContext(), null), singleJson);
        assertEquals(Collections.singletonList(testString), result);
    }

    @Test
    public void testStringArrayDeserialization() throws Exception {
        String[] testStrings = {"test1", "test2", "test3"};
        JsonNode arrayJson = mapper.valueToTree(testStrings);
        List<String> result = mapper.treeToValue(deserializer.createContextual(mapper.getDeserializationContext(), null), arrayJson);
        List<String> expected = List.of(testStrings);
        assertEquals(expected, result);
    }

    @Test
    public void testNullDeserialization() throws Exception {
        JsonNode nullJson = mapper.valueToTree(JsonNull.getInstance());
        List<String> result = mapper.treeToValue(deserializer.createContextual(mapper.getDeserializationContext(), null), nullJson);
        assertTrue(result == null || result.isEmpty());
    }

    @Test
    public void testValueDeserializerCustomization() throws Exception {
        StringValueDeserializer stringValueDeserializer = new StringValueDeserializer();
        JsonDeserializer<String> customValueDeserializer = new CompositeDeserializer<>(String.class, deserializer, stringValueDeserializer);
        JsonNode customJson = mapper.valueToTree(List.of("test1", "test2", "test3"));
        List<String> result = mapper.treeToValue(deserializer.createContextual(mapper.getDeserializationContext(), null), customJson);
        List<String> expected = List.of("test1", "test2", "test3");
        assertEquals(expected, result);
    }

    @Test
    public void testDelegateDeserializerCustomization() throws Exception {
        JsonDeserializer<Object> delegateDeserializer = mapper.findValueDeserializer(String[].class, null);
        JsonNode delegateJson = mapper.valueToTree(new String[]{"test1", "test2"});
        List<String> result = mapper.treeToValue(deserializer.createContextual(mapper.getDeserializationContext(), null), delegateJson);
        List<String> expected = List.of("test1", "test2");
        assertEquals(expected, result);
    }

    @Test
    public void testNullValueProviderCustomization() throws Exception {
        NullValueProvider nullValueProvider = mapper.getConfig().getNullValueProvider();
        ObjectNode nullJson = mapper.createObjectNode();
        nullJson.put("value", nullValueProvider.getNullValue(mapper.getDeserializationContext()));
        List<String> result = mapper.treeToValue(deserializer.createContextual(mapper.getDeserializationContext(), null), nullJson);
        assertTrue(result == null || result.isEmpty());
    }

    @Test
    public void testCustomFormatFeature() throws Exception {
        ObjectNode customFormatJson = mapper.createObjectNode();
        customFormatJson.put("format", JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY.name());
        List<String> result = mapper.treeToValue(deserializer.createContextual(mapper.getDeserializationContext(), null), customFormatJson);
        assertTrue(result instanceof List);
        assertEquals(1, result.size());
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArrayInput() throws Exception {
        JsonNode nonArrayJson = mapper.valueToTree("test");
        mapper.treeToValue(deserializer.createContextual(mapper.getDeserializationContext(), null), nonArrayJson);
    }

    @Test(expected = JsonMappingException.class)
    public void testValueDeserializerMismatch() throws Exception {
        JsonDeserializer<Integer> integerDeserializer = mapper.findValueDeserializer(Integer.class, null);
        JsonNode mismatchJson = mapper.valueToTree(List.of("test1", "test2"));
        mapper.treeToValue(deserializer.createContextual(mapper.getDeserializationContext(), null), mismatchJson);
    }
}
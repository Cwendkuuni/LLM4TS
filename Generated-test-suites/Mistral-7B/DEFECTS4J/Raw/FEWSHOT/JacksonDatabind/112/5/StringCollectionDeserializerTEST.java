package com.fasterxml.jackson.databind.deser.std;

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
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@JacksonStdImpl
public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyStringCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        JsonNode emptyArrayNode = mapper.valueToTree(new String[0]);
        rootNode.set("emptyCollection", emptyArrayNode);
        Collection<String> emptyCollection = mapper.readerForUpdating(rootNode).readValueAs(String[].class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(String.class, String[].class), null, null, null, null, null);
        Collection<String> deserializedCollection = deserializer.deserialize(mapper.getFactory().createParser(rootNode), new TestContext());
        assertEquals(Arrays.asList(""), deserializedCollection);
    }

    @Test
    public void testStringCollectionDeserializationWithCustomValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        rootNode.put("strings", new TextNode("foo,bar,baz"));
        JsonDeserializer<String> stringDeserializer = mapper.deserializationConfig().findValueDeserializer(String.class, null);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(String.class, String[].class), null, null, stringDeserializer, null, null);
        Collection<String> deserializedCollection = deserializer.deserialize(mapper.getFactory().createParser(rootNode), new TestContext());
        assertEquals(Arrays.asList("foo", "bar", "baz"), deserializedCollection);
    }

    @Test
    public void testStringCollectionDeserializationWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        rootNode.put("strings", new TextNode("{\"value\":[\"foo\",\"bar\",\"baz\"]}"));
        JsonDeserializer<Object> delegateDeserializer = mapper.deserializationConfig().findValueDeserializer(Object.class, null);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(String.class, String[].class), null, delegateDeserializer, null, null, null);
        Collection<String> deserializedCollection = deserializer.deserialize(mapper.getFactory().createParser(rootNode), new TestContext());
        assertEquals(Arrays.asList("foo", "bar", "baz"), deserializedCollection);
    }

    @Test
    public void testStringCollectionDeserializationWithNullValueProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        rootNode.put("strings", new TextNode("null"));
        NullValueProvider nullValueProvider = context -> new String[0];
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(String.class, String[].class), null, null, null, nullValueProvider, null);
        Collection<String> deserializedCollection = deserializer.deserialize(mapper.getFactory().createParser(rootNode), new TestContext());
        assertNotNull(deserializedCollection);
        assertTrue(deserializedCollection.isEmpty());
    }

    @Test
    public void testStringCollectionDeserializationWithUnwrappedSingleValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        rootNode.put("string", new TextNode("foo"));
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(String.class, String[].class), null, null, null, null, Boolean.TRUE);
        Collection<String> deserializedCollection = deserializer.deserialize(mapper.getFactory().createParser(rootNode), new TestContext());
        assertEquals(Arrays.asList("foo"), deserializedCollection);
    }

    @Test
    public void testStringCollectionDeserializationWithSkippedNullValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        rootNode.put("strings", new TextNode("[null, \"foo\", null, \"bar\"]"));
        NullValueProvider nullValueProvider = context -> new String[0];
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(String.class, String[].class), null, null, null, nullValueProvider, Boolean.TRUE);
        Collection<String> deserializedCollection = deserializer.deserialize(mapper.getFactory().createParser(rootNode), new TestContext());
        assertEquals(Arrays.asList("foo", "bar"), deserializedCollection);
    }

    @Test(expected = JsonMappingException.class)
    public void testStringCollectionDeserializationWithInvalidInput() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        rootNode.put("strings", new TextNode("invalidInput"));
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(String.class, String[].class), null, null, null, null, null);
        deserializer.deserialize(mapper.getFactory().createParser(rootNode), new TestContext());
    }

    @JsonRootName("test")
    static class TestContext extends DeserializationContext {
        @Override
        public JsonDeserializer<String> findValueDeserializer(Class<?> valueType, BeanProperty property) {
            return super.findValueDeserializer(String.class, null);
        }

        @Override
        public JsonDeserializer<?> findContextualValueDeserializer(Class<?> valueType, BeanProperty property) {
            return super.findContextualValueDeserializer(String.class, null);
        }

        @Override
        public JsonDeserializer<Object> findValueDeserializer(Class<?> valueType) {
            return super.findValueDeserializer(Object.class);
        }

        @Override
        public JsonDeserializer<?> findContextualValueDeserializer(Class<?> valueType) {
            return super.findContextualValueDeserializer(Object.class);
        }

        @Override
        public ObjectReader getReaderForUpdating(JsonNode node) {
            return null;
        }

        @Override
        public ObjectReader getRootValueReader(JsonNode node) {
            return null;
        }

        @Override
        public ValueInstantiator findValueInstantiator(Class<?> beanType) {
            return null;
        }

        @Override
        public TypeDeserializer findTypeDeserializer(Class<?> valueType) {
            return null;
        }

        @Override
        public JsonNodeFactory getNodeFactory() {
            return null;
        }

        @Override
        public JsonParser getParser() {
            return null;
        }

        @Override
        public Object getActiveContextualHandler() {
            return null;
        }

        @Override
        public DeserializationConfig getConfig() {
            return null;
        }

        @Override
        public JsonFactory getFactory() {
            return null;
        }

        @Override
        public JsonParser.Feature findFeature(String name) {
            return null;
        }

        @Override
        public JsonParser.Feature findDefaultFeature(String name) {
            return null;
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }

        @Override
        public boolean isEnabled(JsonParser.Feature feature) {
            return true;
        }
    }
}
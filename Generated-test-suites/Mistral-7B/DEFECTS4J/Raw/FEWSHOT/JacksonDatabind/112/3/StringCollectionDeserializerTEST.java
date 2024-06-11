package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

public class StringCollectionDeserializerTest {

    @Test
    public void testDefaultDeserializer() throws Exception {
        JavaType collectionType = MapperFactory.getSimpleCollectionType(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree("['foo', 'bar']");
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals(Arrays.asList("foo", "bar"), result);
    }

    @Test
    public void testCustomValueDeserializer() throws Exception {
        JavaType collectionType = MapperFactory.getSimpleCollectionType(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, new CustomJsonDeserializer(), null, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("['foo', 'bar']");
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals(Arrays.asList("FOO", "BAR"), result);
    }

    @Test
    public void testDelegateDeserializer() throws Exception {
        JavaType collectionType = MapperFactory.getSimpleCollectionType(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, new DelegateJsonDeserializer(), null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("['foo', 'bar']");
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals(Arrays.asList("FOO", "BAR"), result);
    }

    @Test
    public void testNullValueProvider() throws Exception {
        JavaType collectionType = MapperFactory.getSimpleCollectionType(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext context) throws IOException {
                return null;
            }
        }, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("null");
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertTrue(result instanceof Collection<?> && result.isEmpty());
    }

    @Test
    public void testEmptyCollection() throws Exception {
        JavaType collectionType = MapperFactory.getSimpleCollectionType(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.createObjectNode();
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertTrue(result instanceof Collection<?> && result.isEmpty());
    }

    @Test
    public void testEmptyArray() throws Exception {
        JavaType collectionType = MapperFactory.getSimpleCollectionType(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.createArrayNode();
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertTrue(result instanceof Collection<?> && result.isEmpty());
    }

    @Test
    public void testSingleElement() throws Exception {
        JavaType collectionType = MapperFactory.getSimpleCollectionType(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.createObjectNode();
        node.set("foo", new TextNode("bar"));
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(node), null);
        assertEquals(Collections.singletonList("bar"), result);
    }

    @Test
    public void testCustomDeserializationContext() throws Exception {
        JavaType collectionType = MapperFactory.getSimpleCollectionType(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        context.overrideCurrentValue(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonNode node = mapper.readTree("{}");
        Collection<String> result = deserializer.createContextual(context, null);
        assertTrue(result instanceof Collection<?> && result.isEmpty());
    }

    private static class CustomJsonDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString().toUpperCase();
        }
    }

    private static class DelegateJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString().toUpperCase();
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            return new CustomJsonDeserializer();
        }
    }
}
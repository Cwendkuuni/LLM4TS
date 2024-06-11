package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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

/**
 * Test cases for {@link StringCollectionDeserializer}.
 */
public class StringCollectionDeserializerTest {

    @Test
    public void testStringCollectionDeserializer() {
        // Test instantiating with no arguments
        StringCollectionDeserializer deserializer1 = new StringCollectionDeserializer(null, null, null, null, null, null);
        assertNull(deserializer1._valueDeserializer);
        assertNull(deserializer1._valueInstantiator);
        assertNull(deserializer1._delegateDeserializer);

        // Test instantiating with all arguments
        JavaType collectionType = new TypeFactory().constructCollectionType(Collection.class, String.class);
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        ValueInstantiator valueInstantiator = new StringValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = new ObjectDeserializer();
        StringCollectionDeserializer deserializer2 = new StringCollectionDeserializer(collectionType, valueInstantiator, delegateDeserializer, valueDeserializer, null, null);
        assertSame(collectionType, deserializer2._containerType);
        assertSame(valueInstantiator, deserializer2._valueInstantiator);
        assertSame(delegateDeserializer, deserializer2._delegateDeserializer);
        assertNull(deserializer2._valueDeserializer);
    }

    @Test
    public void testCreateContextual() throws IOException {
        // Test creating with no custom handling
        StringCollectionDeserializer deserializer1 = new StringCollectionDeserializer(null, null, null, null, null, null);
        DeserializationContext ctxt = createTestContext();
        JsonDeserializer<?> deser = deserializer1.createContextual(ctxt, null);
        assertNull(deser);

        // Test creating with custom value deserializer
        JsonDeserializer<String> customValueDeserializer = new CustomStringDeserializer();
        StringCollectionDeserializer deserializer2 = new StringCollectionDeserializer(null, null, null, null, null, null);
        deserializer2._valueDeserializer = customValueDeserializer;
        JsonDeserializer<?> deser2 = deserializer2.createContextual(ctxt, null);
        assertSame(customValueDeserializer, deser2);

        // Test creating with custom value instantiation and value deserializer
        ValueInstantiator customValueInstantiator = new CustomStringValueInstantiator();
        JsonDeserializer<String> customValueDeserializer2 = new CustomStringDeserializer();
        StringCollectionDeserializer deserializer3 = new StringCollectionDeserializer(null, customValueInstantiator, null, null, null, null);
        deserializer3._valueDeserializer = customValueDeserializer2;
        JsonDeserializer<?> deser3 = deserializer3.createContextual(ctxt, null);
        assertSame(customValueDeserializer2, deser3.getValueDeserializer());
        assertSame(customValueInstantiator, deserializer3.getValueInstantiator());

        // Test creating with delegate deserializer
        JsonDeserializer<Object> delegateDeserializer = new ObjectDeserializer();
        StringCollectionDeserializer deserializer4 = new StringCollectionDeserializer(null, null, delegateDeserializer, null, null, null);
        JsonDeserializer<?> deser4 = deserializer4.createContextual(ctxt, null);
        assertSame(delegateDeserializer, deser4);
    }

    @Test
    public void testDeserialize() throws IOException {
        // Test deserializing empty array
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        DeserializationContext ctxt = createTestContext();
        JsonParser p = createTestParser(ctx -> ctx.setText("[]"));
        Collection<String> result = deserializer.deserialize(p, ctxt);
        assertTrue(result.isEmpty());

        // Test deserializing single value
        ctxt.setEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        p = createTestParser(ctx -> ctx.setText("\"foo\""));
        result = deserializer.deserialize(p, ctxt);
        assertEquals(1, result.size());
        assertEquals("foo", result.iterator().next());

        // Test deserializing single value with custom deserializer
        JsonDeserializer<String> customValueDeserializer = new CustomStringDeserializer();
        deserializer._valueDeserializer = customValueDeserializer;
        p = createTestParser(ctx -> ctx.setText("\"foo\""));
        result = deserializer.deserialize(p, ctxt);
        assertEquals(1, result.size());
        assertEquals("BAR", result.iterator().next());

        // Test deserializing multiple values
        p = createTestParser(ctx -> ctx.setText("[\"foo\", \"bar\"]"));
        result = deserializer.deserialize(p, ctxt);
        assertEquals(2, result.size());
        assertEquals("foo", result.iterator().next());
        assertEquals("bar", result.iterator().next());

        // Test deserializing multiple values with custom deserializer
        deserializer._valueDeserializer = customValueDeserializer;
        p = createTestParser(ctx -> ctx.setText("[\"foo\", \"bar\"]"));
        result = deserializer.deserialize(p, ctxt);
        assertEquals(2, result.size());
        assertEquals("FOO", result.iterator().next());
        assertEquals("BAZ", result.iterator().next());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        DeserializationContext ctxt = createTestContext();
        TypeDeserializer typeDeserializer = new BaseTypeDeserializer();
        Object result = deserializer.deserializeWithType(createTestParser(null), ctxt, typeDeserializer);
        assertEquals(Collections.emptyList(), result);

        ctxt.setEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JsonParser p = createTestParser(ctx -> ctx.setText("\"foo\""));
        result = deserializer.deserializeWithType(p, ctxt, typeDeserializer);
        assertEquals(Collections.singletonList("foo"), result);

        p = createTestParser(ctx -> ctx.setText("[\"foo\", \"bar\"]"));
        result = deserializer.deserializeWithType(p, ctxt, typeDeserializer);
        assertEquals(Arrays.asList("foo", "bar"), result);
    }

    @Test
    public void testIsCachable() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        assertTrue(deserializer.isCachable());

        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        StringCollectionDeserializer deserializer2 = new StringCollectionDeserializer(null, null, null, null, null, null);
        deserializer2._valueDeserializer = valueDeserializer;
        assertFalse(deserializer2.isCachable());

        JsonDeserializer<Object> delegateDeserializer = new ObjectDeserializer();
        StringCollectionDeserializer deserializer3 = new StringCollectionDeserializer(null, null, delegateDeserializer, null, null, null);
        assertFalse(deserializer3.isCachable());
    }

    private static DeserializationContext createTestContext() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getDeserializationContext();
    }

    private static JsonParser createTestParser(DeserializationConfig config) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (config != null) {
            mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.registerModule(new SimpleModule() {
                @Override
                public StanfordCoreNLPModule getTextStanfordCoreNLPModule() {
                    return null;
                }
            });
            mapper.setDeserializationConfig(config);
        }
        return mapper.getFactory().createParser("{}");
    }

    private static class CustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "BAR";
        }
    }

    private static class CustomStringValueInstantiator implements ValueInstantiator {
        @Override
        public Object createValueInstance(DeserializationContext ctxt) throws IOException {
            return "BAZ";
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return "BAZ";
        }
    }

    private static class ObjectDeserializer extends StdDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    private static class BaseTypeDeserializer extends StdDeserializer<List<?>> {
        @Override
        public List<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return Collections.emptyList();
        }
    }

    private static class Foo {
        @JsonProperty("foo")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
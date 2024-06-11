package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
import com.fasterxml.jackson.databind.node.ObjectNode;

@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @JsonRootName("strings")
    static class Root {
        private Collection<String> value;

        public Collection<String> getValue() {
            return value;
        }

        public void setValue(Collection<String> value) {
            this.value = value;
        }
    }

    @Test
    public void testDeserializeWithDefaultValueDeserializer() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);

        ObjectReader reader = mapper.readerFor(Root.class);
        Root root = reader.readValue(JsonTestData.STRING_COLLECTION_JSON);
        assertEquals(Arrays.asList("foo", "bar", "baz"), root.getValue());
    }

    @Test
    public void testDeserializeWithCustomValueDeserializer() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, stringDeserializer, null, null);

        ObjectReader reader = mapper.readerFor(Root.class);
        Root root = reader.readValue(JsonTestData.STRING_COLLECTION_JSON);
        assertEquals(Arrays.asList("FOO", "BAR", "BAZ"), root.getValue());
    }

    @Test
    public void testDeserializeWithValueInstantiator() throws IOException {
        ValueInstantiator valueInstantiator = mapper.getFactory().createValueInstantiator(ArrayList.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, valueInstantiator, null);

        ObjectReader reader = mapper.readerFor(Root.class);
        Root root = reader.readValue(JsonTestData.STRING_COLLECTION_JSON);
        assertEquals(Arrays.asList("foo", "bar", "baz"), root.getValue());
    }

    @Test
    public void testDeserializeWithDelegateDeserializer() throws IOException {
        JsonDeserializer<Object> delegateDeserializer = new StandardSerializer<>(Object.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, delegateDeserializer, null, null, null);

        ObjectReader reader = mapper.readerFor(Root.class);
        Root root = reader.readValue(JsonTestData.STRING_COLLECTION_JSON);
        assertNull(root.getValue());
    }

    @Test
    public void testDeserializeWithNullValueProvider() throws IOException {
        NullValueProvider nullValueProvider = new StandardSerializer<>() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return "NULL";
            }
        };
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, nullValueProvider);

        ObjectReader reader = mapper.readerFor(Root.class);
        Root root = reader.readValue(JsonTestData.EMPTY_STRING_COLLECTION_JSON);
        assertEquals(Arrays.asList("NULL"), root.getValue());
    }

    @Test
    public void testDeserializeWithFormatFeature() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, stringDeserializer, null, null);

        ObjectMapper mapperWithFeature = new ObjectMapper();
        mapperWithFeature.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        ObjectReader reader = mapperWithFeature.readerFor(Root.class);
        Root root = reader.readValue(JsonTestData.SINGLE_STRING_JSON);
        assertEquals(Arrays.asList("singleString"), root.getValue());
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeWithInvalidJson() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);

        ObjectReader reader = mapper.readerFor(Root.class);
        reader.readValue(JsonTestData.INVALID_JSON);
    }

    @Test
    public void testCreateContextual() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        JsonDeserializer<Object> delegateDeserializer = new StandardSerializer<>(Object.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        ValueInstantiator valueInstantiator = mapper.getFactory().createValueInstantiator(ArrayList.class);
        NullValueProvider nullValueProvider = new StandardSerializer<>() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return "NULL";
            }
        };

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);

        DeserializationContext context = mapper.getDeserializationConfig().contextInstance();
        BeanProperty property = context.findBeanProperty("value", Root.class);

        JsonDeserializer<?> result = deserializer.createContextual(context, property);
        assertEquals(deserializer, result);

        // Value deserializer is null, so no custom handling is required.
        result = deserializer.createContextual(context, property);
        assertNull(result._valueDeserializer);

        // Value deserializer is provided, so custom handling is required.
        result = deserializer.createContextual(context, property);
        assertEquals(stringDeserializer, result._valueDeserializer);

        // Delegate deserializer is provided, so delegate creation is required.
        result = deserializer.createContextual(context, property);
        assertEquals(delegateDeserializer, result._delegateDeserializer);

        // Value instantiation is provided, so custom handling is required.
        result = deserializer.createContextual(context, property);
        assertEquals(valueInstantiator, result._valueInstantiator);

        // Null value provider is provided, so custom handling is required.
        result = deserializer.createContextual(context, property);
        assertEquals(nullValueProvider, result._nullProvider);
    }
}

static class StringDeserializer extends StdDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.nextTextValue();
        ctxt.handleUnexpectedStringValue("String", value.getClass(), value);
        return value.toUpperCase();
    }
}

static class JsonTestData {
    static final String STRING_COLLECTION_JSON = "[\"foo\", \"bar\", \"baz\"]";
    static final String EMPTY_STRING_COLLECTION_JSON = "[]";
    static final String SINGLE_STRING_JSON = "\"singleString\"";
    static final String INVALID_JSON = "{ \"invalid\": \"json\" }";
}
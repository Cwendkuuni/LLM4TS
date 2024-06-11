package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyStringCollectionDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        String json = "[]";
        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);

        Collection<String> result = deserializer.createContextual(new DeserializationContext(mapper), null);
        assertTrue(result.isEmpty());

        jsonParser.close();
    }

    @Test
    public void testSingleStringDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        String json = "[\"abc\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);

        Collection<String> result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper));
        assertEquals(1, result.size());
        assertEquals("abc", result.iterator().next());

        jsonParser.close();
    }

    @Test
    public void testMultipleStringsDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        String json = "[\"abc\",\"def\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);

        Collection<String> result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper));
        assertEquals(2, result.size());
        assertTrue(Arrays.asList(result.toArray()).containsAll(Arrays.asList("abc", "def")));

        jsonParser.close();
    }

    @Test
    public void testNullValuesDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.setNullValueProvider(new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) {
                return null;
            }
        });

        String json = "[null]";
        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);

        Collection<String> result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper));
        assertEquals(1, result.size());
        assertNull(result.iterator().next());

        jsonParser.close();
    }

    @Test
    public void testEmptyStringCollectionWithCustomValueDeserializer()
            throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        JsonDeserializer<String> customValueDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return p.nextTextValue();
            }
        };

        String json = "[\"\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, customValueDeserializer, null, null);

        Collection<String> result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper));
        assertEquals(1, result.size());
        assertEquals("", result.iterator().next());

        jsonParser.close();
    }

    @Test
    public void testStringCollectionDeserializationWithCustomDelegateDeserializer()
            throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        JsonDeserializer<Object> customDelegateDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return p.nextValue();
            }
        };

        String json = "[1,2,3]";
        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, customDelegateDeserializer, null, null, null);

        Collection<String> result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper));
        assertEquals(3, result.size());
        assertTrue(Arrays.asList(result.toArray()).containsAll(Arrays.asList("1", "2", "3")));

        jsonParser.close();
    }

    @Test
    public void testStringCollectionDeserializationWithCustomValueDeserializerAndDelegateDeserializer()
            throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        JsonDeserializer<String> customValueDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                TextNode textNode = (TextNode) p.readValueAs(TextNode.class);
                return Integer.toString(textNode.asIntValue());
            }
        };

        JsonDeserializer<Object> customDelegateDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return p.nextValue();
            }
        };

        String json = "[42,5]";
        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, customDelegateDeserializer, customValueDeserializer, null, null);

        Collection<String> result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper));
        assertEquals(2, result.size());
        assertEquals("42", result.iterator().next());
        assertEquals("5", result.iterator().next());

        jsonParser.close();
    }

    @Test
    public void testStringCollectionDeserializationWithCustomValueDeserializerAndDelegateDeserializerWithTypeDeserializer()
            throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        JsonDeserializer<String> customValueDeserializer = new StringDeserializer();
        JsonDeserializer<Object> customDelegateDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return p.nextValue();
            }
        };
        TypeDeserializer typeDeserializer = new StandardTypeDeserializer();

        String json = "[42,5]";
        JsonParser jsonParser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, customDelegateDeserializer, customValueDeserializer, typeDeserializer, null);

        Collection<String> result = deserializer.deserialize(jsonParser, new DeserializationContext(mapper));
        assertEquals(2, result.size());
        assertEquals("42", result.iterator().next());
        assertEquals("5", result.iterator().next());

        jsonParser.close();
    }
}

class StringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        TextNode textNode = (TextNode) p.readValueAs(TextNode.class);
        return Integer.toString(textNode.asIntValue());
    }
}

class StandardTypeDeserializer extends TypeDeserializer {

    @SuppressWarnings("unchecked")
    @Override
    public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        return p.readValueAs(ArrayList.class);
    }
}
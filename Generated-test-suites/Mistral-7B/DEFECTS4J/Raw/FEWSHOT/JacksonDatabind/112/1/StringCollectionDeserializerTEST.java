package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Test class for StringCollectionDeserializer.
 */
public class StringCollectionDeserializerTest {

    @Test
    public void testCreateContextual() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(Collection.class, String.class);

        JsonDeserializer<String> valueDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, null);

        StringCollectionDeserializer deserializer1 = new StringCollectionDeserializer(collectionType, null, null,
                valueDeserializer, null, null);
        JsonDeserializer<Object> delegateDeserializer = mapper.getFactory()
                .createValueDeserializer(Object.class, null);
        StringCollectionDeserializer deserializer2 = new StringCollectionDeserializer(collectionType, null,
                delegateDeserializer, null, null, null);

        assertEquals(deserializer1, deserializer1.createContextual(null, null));
        assertEquals(deserializer2, deserializer2.createContextual(null, null));

        ValueInstantiator valueInstantiator = mapper.getFactory().getConstructors()
                .findConstructors(ArrayList.class)[0];
        JsonDeserializer<Object> delegateCreator = mapper.getFactory()
                .createValueDeserializer(ArrayList.class, valueInstantiator);
        StringCollectionDeserializer deserializer3 = new StringCollectionDeserializer(collectionType,
                valueInstantiator, delegateCreator, null, null, null);

        JsonDeserializer<?> deserializer = deserializer3.createContextual(null, null);
        assertNotNull(deserializer);
        assertNotSame(deserializer3, deserializer);
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(Collection.class, String.class);

        JsonDeserializer<String> valueDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, null);

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null,
                valueDeserializer, null, null);

        String json = "[\"foo\", \"bar\", null, \"baz\"]";
        JsonParser parser = mapper.getFactory().createParser(json);

        Collection<String> result = deserializer.deserialize(parser, null, null);
        assertEquals(3, result.size());
        assertEquals("foo", result.iterator().next());
        assertEquals("bar", result.iterator().next());
        assertNull(result.iterator().next());
        assertEquals("baz", result.iterator().next());
    }

    @Test
    public void testDeserialize_WithDelegateDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(Collection.class, String.class);

        JsonDeserializer<String> valueDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, null);
        JsonDeserializer<Object> delegateDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, null);

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null,
                delegateDeserializer, null, null, null);

        String json = "[\"foo\", \"bar\", null, \"baz\"]";
        JsonParser parser = mapper.getFactory().createParser(json);

        Collection<String> result = deserializer.deserialize(parser, null, null);
        assertEquals(3, result.size());
        assertEquals("foo", result.iterator().next());
        assertEquals("bar", result.iterator().next());
        assertNull(result.iterator().next());
        assertEquals("baz", result.iterator().next());

        JsonToken currentToken = parser.getCurrentToken();
        parser.nextToken();

        result = deserializer.deserialize(parser, null, null);
        assertEquals(0, result.size());

        parser.nextToken();
        JsonDeserializer<String> customDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, null);
        deserializer = new StringCollectionDeserializer(collectionType, null,
                customDeserializer, null, null, null);

        result = deserializer.deserialize(parser, null, null);
        assertEquals(1, result.size());
        assertNotNull(result.iterator().next());

        parser.nextToken();
        result = deserializer.deserialize(parser, null, null);
        assertEquals(1, result.size());
        assertNotNull(result.iterator().next());

        parser.nextToken();
        result = deserializer.deserialize(parser, null, null);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserialize_WithCustomDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(Collection.class, String.class);

        JsonDeserializer<String> valueDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, null);
        JsonDeserializer<String> customDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, null);

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null,
                valueDeserializer, null, null);

        String json = "[{\"foo\":\"bar\"},{\"foo\":\"baz\"}]";
        JsonParser parser = mapper.getFactory().createParser(json);

        Collection<String> result = deserializer.deserialize(parser, null, null);
        assertEquals(2, result.size());
        assertEquals("bar", result.iterator().next());
        assertEquals("baz", result.iterator().next());

        deserializer = new StringCollectionDeserializer(collectionType, null, null,
                customDeserializer, null, null);

        result = deserializer.deserialize(parser, null, null);
        assertEquals(2, result.size());
        assertEquals("bar", result.iterator().next());
        assertEquals("baz", result.iterator().next());
    }

    @Test
    public void testDeserialize_WithNullValueProvider() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(Collection.class, String.class);

        JsonDeserializer<String> valueDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, null);
        NullValueProvider nullValueProvider = mapper.getFactory().getValueInstantiator()
                .getCurrentValueInstantiator().getNullProvider();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null,
                valueDeserializer, nullValueProvider, null);

        String json = "[null, null, null]";
        JsonParser parser = mapper.getFactory().createParser(json);

        Collection<String> result = deserializer.deserialize(parser, null, null);
        assertEquals(3, result.size());
        for (String s : result) {
            assertNull(s);
        }
    }

    @Test
    public void testDeserialize_WithWrapperType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(Collection.class, String.class);
        JavaType wrapperType = mapper.getTypeFactory().constructType(List.class.getName() + "<String>");

        JsonDeserializer<String> valueDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, null);

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null,
                valueDeserializer, null, null);

        String json = "[\"foo\", \"bar\", null, \"baz\"]";
        JsonParser parser = mapper.getFactory().createParser(json);

        Collection<String> result = deserializer.deserialize(parser, null, null);
        assertEquals(3, result.size());
        assertEquals("foo", result.iterator().next());
        assertEquals("bar", result.iterator().next());
        assertNull(result.iterator().next());
        assertEquals("baz", result.iterator().next());

        result = deserializer.deserialize(parser, null, wrapperType);
        assertEquals(3, result.size());
        assertTrue(result instanceof List);
        List<String> listResult = (List<String>) result;
        assertEquals("foo", listResult.get(0));
        assertEquals("bar", listResult.get(1));
        assertNull(listResult.get(2));
        assertEquals("baz", listResult.get(3));
    }
}
package com.example.test; // replace with your test package name

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for StringCollectionDeserializer.
 */
public class StringCollectionDeserializerTest {

    @Test
    public void testDefaultConstructor() {
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        assertNotNull(serializer);
    }

    @Test
    public void testConstructorWithValueDeserializer() {
        JsonDeserializer<String> valueDeserializer = new StdSerializer<String>(String.class) {};
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(null, null, null, valueDeserializer, null, null);
        assertNotNull(serializer);
    }

    @Test
    public void testCreateContextual() {
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonDeserializer<Object> delegateDeserializer = new StdSerializer<Object>(Object.class) {};
        JsonDeserializer<String> valueDeserializer = new StdSerializer<String>(String.class) {};
        NullValueProvider nullValueProvider = new JsonStringNullValueProvider();

        JsonDeserializer<Collection<String>> contextualDeserializer = serializer.createContextual(null, null);
        assertEquals(StringCollectionDeserializer.class, contextualDeserializer.getClass());
        assertEquals(delegateDeserializer, contextualDeserializer._delegateDeserializer);
        assertEquals(valueDeserializer, contextualDeserializer._valueDeserializer);
        assertEquals(nullValueProvider, contextualDeserializer._nullProvider);
    }

    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = createParser("['hello', 'world']");

        Collection<String> result = serializer.deserialize(parser, null);
        assertEquals(2, result.size());
        assertEquals("hello", result.iterator().next());
        assertEquals("world", result.iterator().next());
    }

    @Test
    public void testDeserializeWithDelegateDeserializer() throws IOException {
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonDeserializer<Object> delegateDeserializer = new StdSerializer<Object>(Object.class) {};
        ValueInstantiator valueInstantiator = new StdValueInstantiator();

        JsonDeserializer<Collection<String>> deserializer = new StringCollectionDeserializer(
                JavaType.constructParamType(Collection.class, String.class),
                valueInstantiator, delegateDeserializer, null, null, null);

        Collection<String> result = deserializer.deserialize(createParser("['hello', 'world']"), null);
        assertEquals(2, result.size());
        assertEquals("hello", result.iterator().next());
        assertEquals("world", result.iterator().next());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidInput() throws IOException {
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = createParser("'invalid input'");
        serializer.deserialize(parser, null);
    }

    @Test
    public void testDeserializeWithNullValueProvider() throws IOException {
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = createParser("[]");
        NullValueProvider nullValueProvider = new JsonStringNullValueProvider();

        Collection<String> result = serializer.deserialize(parser, null, nullValueProvider);
        assertEquals(0, result.size());
    }

    @Test
    public void testIsCachable() {
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        assertTrue(serializer.isCachable());
    }

    @Test
    public void testWithResolved() {
        StringCollectionDeserializer serializer1 = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonDeserializer<String> valueDeserializer = new StdSerializer<String>(String.class) {};
        JsonDeserializer<Object> delegateDeserializer = new StdSerializer<Object>(Object.class) {};
        NullValueProvider nullValueProvider = new JsonStringNullValueProvider();

        StringCollectionDeserializer serializer2 = serializer1.withResolved(delegateDeserializer, valueDeserializer, nullValueProvider, null);
        assertNotSame(serializer1, serializer2);
        assertEquals(delegateDeserializer, serializer2._delegateDeserializer);
        assertEquals(valueDeserializer, serializer2._valueDeserializer);
        assertEquals(nullValueProvider, serializer2._nullProvider);
    }

    private JsonParser createParser(String content) throws IOException {
        ObjectReader reader = new ObjectMapper().readerForListOf(String.class);
        return reader.readValueAsTree(new StringReader(content)).getParser();
    }

    static class StdValueInstantiator extends StdSerializer<Object> implements ValueInstantiator {
        @Override
        public Object createValueInstance(Type type) throws InstantiationException, IllegalAccessException {
            return new Object();
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws Exception {
            return new Object();
        }
    }

    static class JsonStringNullValueProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) throws IOException {
            return "";
        }
    }
}
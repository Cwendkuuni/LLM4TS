package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StringCollectionSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Test cases for {@link StringCollectionDeserializer}
 */
@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEmptyCollectionDeserialization() throws Exception {
        JsonNode jsonEmpty = mapper.readTree("[]");
        JsonDeserializer<Collection<String>> deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<String> collection = deserializer.deserialize(mapper.getFactory().createParser(jsonEmpty), null);
        assertTrue(collection.isEmpty());
    }

    @Test
    public void testSingleElementCollectionDeserialization() throws Exception {
        JsonNode jsonSingle = mapper.readTree("['foo']");
        JsonDeserializer<Collection<String>> deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<String> collection = deserializer.deserialize(mapper.getFactory().createParser(jsonSingle), null);
        assertEquals(1, collection.size());
        assertEquals("foo", collection.iterator().next());
    }

    @Test
    public void testCustomValueDeserializer() throws Exception {
        JsonNode jsonCustom = mapper.readTree("['foo']");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);
        JsonDeserializer<String> valueDeserializer = mapper.findValueDeserializer(String.class, null);
        deserializer._valueDeserializer = valueDeserializer;
        Collection<String> collection = deserializer.deserialize(mapper.getFactory().createParser(jsonCustom), null);
        assertEquals(1, collection.size());
        assertEquals("foo", collection.iterator().next());
    }

    @Test
    public void testDelegateDeserializer() throws Exception {
        JsonNode jsonDelegate = mapper.readTree("{'values': ['foo']}");
        JsonDeserializer<Object> delegateDeserializer = mapper.findValueDeserializer(Object.class, null);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, delegateDeserializer, null, null, null);
        ValueInstantiator valueInstantiator = mapper.getValueInstantiator();
        Collection<String> collection = deserializer.deserialize(mapper.getFactory().createParser(jsonDelegate), null);
        assertEquals(1, collection.size());
        assertEquals("foo", collection.iterator().next());
    }

    @Test
    public void testNullValueProvider() throws Exception {
        JsonNode jsonNull = mapper.readTree("null");
        JsonDeserializer<Collection<String>> deserializer = new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, mapper.getNullValueProvider(), Boolean.TRUE);
        Collection<String> collection = deserializer.deserialize(mapper.getFactory().createParser(jsonNull), null);
        assertTrue(collection.isEmpty());
    }

    @Test
    public void testParseString() throws Exception {
        JsonParser parser = mapper.getFactory().createParser("\"foo\"");
        assertEquals("foo", StringCollectionDeserializer._parseString(parser, null));
    }

    @Test(expected = IOException.class)
    public void testInvalidJson() throws Exception {
        JsonParser parser = mapper.getFactory().createParser("{}");
        new StringCollectionDeserializer(mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null).deserialize(parser, null);
    }
}
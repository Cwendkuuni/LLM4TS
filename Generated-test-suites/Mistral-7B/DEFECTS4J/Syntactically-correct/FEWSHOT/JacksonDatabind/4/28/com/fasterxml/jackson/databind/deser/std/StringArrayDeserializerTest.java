package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StandardDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;

/**
 * Test class for StringArrayDeserializer.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final ObjectBuffer buffer = new ObjectBuffer(10);

    @Test
    public void testDeserialize() throws IOException {
        String[] result = StringArrayDeserializer.instance.deserialize(createParser("[]"), new DeserializationContext(null));
        assertArrayEquals(new String[0], result);

        result = StringArrayDeserializer.instance.deserialize(createParser("[]"), new DeserializationContext(null), String[].class);
        assertArrayEquals(new String[0], result);

        result = StringArrayDeserializer.instance.deserialize(createParser("[]"), new DeserializationContext(null), String[].class, String.class);
        assertArrayEquals(new String[0], result);

        result = StringArrayDeserializer.instance.deserialize(createParser("['']"), new DeserializationContext(null));
        assertArrayEquals(new String[0], result);

        result = StringArrayDeserializer.instance.deserialize(createParser("['']"), new DeserializationContext(null), String[].class);
        assertArrayEquals(new String[0], result);

        result = StringArrayDeserializer.instance.deserialize(createParser("['']"), new DeserializationContext(null), String[].class, String.class);
        assertArrayEquals(new String[0], result);

        result = StringArrayDeserializer.instance.deserialize(createParser("['foo']"), new DeserializationContext(null));
        assertEquals("foo", result[0]);

        result = StringArrayDeserializer.instance.deserialize(createParser("['foo']"), new DeserializationContext(null), String[].class);
        assertEquals("foo", result[0]);

        result = StringArrayDeserializer.instance.deserialize(createParser("['foo']"), new DeserializationContext(null), String[].class, String.class);
        assertEquals("foo", result[0]);

        result = StringArrayDeserializer.instance.deserialize(createParser("[\"foo\"]"), new DeserializationContext(null));
        assertEquals("foo", result[0]);

        result = StringArrayDeserializer.instance.deserialize(createParser("[\"foo\"]"), new DeserializationContext(null), String[].class);
        assertEquals("foo", result[0]);

        result = StringArrayDeserializer.instance.deserialize(createParser("[\"foo\"]"), new DeserializationContext(null), String[].class, String.class);
        assertEquals("foo", result[0]);

        result = StringArrayDeserializer.instance.deserialize(createParser("[null]"), new DeserializationContext(null));
        assertArrayEquals(new String[] { null }, result);

        result = StringArrayDeserializer.instance.deserialize(createParser("[null]"), new DeserializationContext(null), String[].class);
        assertArrayEquals(new String[] { null }, result);

        result = StringArrayDeserializer.instance.deserialize(createParser("[null]"), new DeserializationContext(null), String[].class, String.class);
        assertArrayEquals(new String[] { null }, result);

        result = StringArrayDeserializer.instance.deserialize(createParser("['foo', 'bar']"), new DeserializationContext(null));
        assertArrayEquals(new String[] { "foo", "bar" }, result);

        result = StringArrayDeserializer.instance.deserialize(createParser("['foo', 'bar']"), new DeserializationContext(null), String[].class);
        assertArrayEquals(new String[] { "foo", "bar" }, result);

        result = StringArrayDeserializer.instance.deserialize(createParser("['foo', 'bar']"), new DeserializationContext(null), String[].class, String.class);
        assertArrayEquals(new String[] { "foo", "bar" }, result);

        result = StringArrayDeserializer.instance.deserialize(createParser("[\"foo\", \"bar\"]"), new DeserializationContext(null));
        assertArrayEquals(new String[] { "foo", "bar" }, result);

        result = StringArrayDeserializer.instance.deserialize(createParser("[\"foo\", \"bar\"]"), new DeserializationContext(null), String[].class);
        assertArrayEquals(new String[] { "foo", "bar" }, result);

        result = StringArrayDeserializer.instance.deserialize(createParser("[\"foo\", \"bar\"]"), new DeserializationContext(null), String[].class, String.class);
        assertArrayEquals(new String[] { "foo", "bar" }, result);

        result = StringArrayDeserializer.instance.deserialize(createParser("['foo', null, 'bar']"), new DeserializationContext(null));
        assertArrayEquals(new String[] { "foo", null, "bar" }, result);

        result = StringArrayDeserializer.instance.deserialize(createParser("['foo', null, 'bar']"), new DeserializationContext(null), String[].class);
        assertArrayEquals(new String[] { "foo", null, "bar" }, result);

        result = StringArrayDeserializer.instance.deserialize(createParser("['foo', null, 'bar']"), new DeserializationContext(null), String[].class, String.class);
        assertArrayEquals(new String[] { "foo", null, "bar" }, result);

        result = StringArrayDeserializer.instance.deserialize(createParser("['foo', null, 'bar']"), new DeserializationContext(null), String[].class, String[].class);
        assertArrayEquals(new String[] { "foo", null, "bar" }, result);

        result = StringArrayDeserializer.instance.deserialize(createParser("[1, 2, 3]"), new DeserializationContext(null));
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonDeserializer<String[]> deser = StringArrayDeserializer.instance;
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        String[] result = (String[]) deser.deserializeWithType(createParser("['foo', 'bar']"), new DeserializationContext(null), typeDeserializer);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        String[] result = new String[0];
        DeserializationContext ctxt = new DeserializationContext(null);
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<>(String.class);

        JsonDeserializer<String[]> deser = StringArrayDeserializer.instance;
        JsonDeserializer<String> deserWithStringDeserializer = new StringArrayDeserializer(stringDeserializer);

        // null contextual deserializer
        assertNull(deser.createContextual(ctxt, null));

        // null deserializer
        assertNull(deser.createContextual(ctxt, new BeanPropertyModel("test", String[].class)));

        // default deserializer
        JsonDeserializer<String> defaultDeserializer = deser.createContextual(ctxt, new BeanPropertyModel("test", String.class));
        assertEquals(deser.getClass(), defaultDeserializer.getClass());

        // custom deserializer
        JsonDeserializer<String> customDeserializer = deserWithStringDeserializer.createContextual(ctxt, new BeanPropertyModel("test", String[].class));
        assertEquals(deserWithStringDeserializer.getClass(), customDeserializer.getClass());
        assertEquals(stringDeserializer.getClass(), customDeserializer._elementDeserializer.getClass());
    }

    @Test
    public void testHandleNonArray() throws IOException {
        String[] result = StringArrayDeserializer.instance.handleNonArray(createParser("1"), new DeserializationContext(null));
        assertEquals("1", result[0]);

        result = StringArrayDeserializer.instance.handleNonArray(createParser("true"), new DeserializationContext(null));
        assertEquals("true", result[0]);

        result = StringArrayDeserializer.instance.handleNonArray(createParser("null"), new DeserializationContext(null));
        assertArrayEquals(new String[0], result);

        result = StringArrayDeserializer.instance.handleNonArray(createParser("[]"), new DeserializationContext(null));
        assertArrayEquals(new String[0], result);

        result = StringArrayDeserializer.instance.handleNonArray(createParser("[1]"), new DeserializationContext(null));
        assertArrayEquals(new String[] { "1" }, result);

        result = StringArrayDeserializer.instance.handleNonArray(createParser("[1, 2]"), new DeserializationContext(null));
        assertArrayEquals(new String[] { "1", "2" }, result);
    }

    private JsonParser createParser(String json) throws IOException {
        return new ObjectMapper().getFactory().createParser(json);
    }
}
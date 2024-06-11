package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test cases for StringArrayDeserializer class.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, new DeserializationContext(mapper, null, null));
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSingleElementArrayDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("['foo']");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, new DeserializationContext(mapper, null, null));
        assertArrayEquals(new String[] {"foo"}, result);
    }

    @Test
    public void testCustomElementDeserializer() throws IOException {
        JsonDeserializer<String> customDeserializer = mapper.findValueDeserializer(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        JsonParser parser = mapper.getFactory().createParser("['foo']");
        String[] result = deserializer.deserialize(parser, new DeserializationContext(mapper, null, null));
        assertArrayEquals(new String[] {"foo"}, result);
    }

    @Test
    public void testNullElementInArray() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("['foo', null, 'bar']");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, new DeserializationContext(mapper, null, null));
        assertArrayEquals(new String[] {"foo", null, "bar"}, result);
    }

    @Test(expected = IOException.class)
    public void testNonArrayInput() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("'foo'");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(parser, new DeserializationContext(mapper, null, null));
    }

    @Test(expected = IOException.class)
    public void testNullInput() throws IOException {
        JsonParser parser = mapper.getFactory().createParser(null);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(parser, new DeserializationContext(mapper, null, null));
    }

    @Test
    public void testContextualDeserialization() throws IOException {
        JsonDeserializer<String> customDeserializer = mapper.findValueDeserializer(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        ObjectBuffer buffer = new ObjectBuffer(10);
        String[] result = deserializer.deserializeWithType(mapper.getFactory().createParser("['foo']"), new DeserializationContext(mapper, null, new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                String[] array = deserializer.deserialize(p, ctxt);
                buffer.writeValue(array);
                return array;
            }
        }), String[].class);
        assertArrayEquals(new String[] {"foo"}, result);
        String[] deserialized = (String[]) buffer.takeValue();
        assertEquals(deserialized.length, 1);
        assertEquals(deserialized[0], "foo");
    }
}
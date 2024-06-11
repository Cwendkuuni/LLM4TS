package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {

    private static class TestDeserializer extends FromStringDeserializer<Object> {
        private static final long serialVersionUID = 1L;

        protected TestDeserializer(Class<?> valueType) {
            super(valueType);
        }

        @Override
        protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
            // Return a mock object for testing purpose
            return new Object();
        }
    }

    private JsonParser mockParser;

    private DeserializationContext mockContext;

    @Before
    public void setup() {
        // Setup mock objects
        mockParser = new JsonParser(null);
        mockContext = new DeserializationContext(null, null);
    }

    @Test
    public void testDeserialize() throws IOException {
        // Create the deserializer object
        TestDeserializer deserializer = new TestDeserializer(Object.class);

        // Test deserialize method when value is not null
        Object result = deserializer.deserialize(mockParser, mockContext);
        assertNotNull(result);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeWithInvalidFormat() throws IOException {
        // Create the deserializer object
        TestDeserializer deserializer = new TestDeserializer(Object.class);

        // Test deserialize method when value is null
        deserializer.deserialize(null, mockContext);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeFromEmptyString() throws IOException {
        // Create the deserializer object
        TestDeserializer deserializer = new TestDeserializer(Object.class);

        // Test _deserializeFromEmptyString method
        deserializer._deserializeFromEmptyString();
    }

    @Test
    public void testFromStringDeserializerTypes() {
        // Test types method
        Class<?>[] types = FromStringDeserializer.types();
        assertNotNull(types);
        assertEquals(13, types.length);
        assertArrayEquals(new Class<?>[] { File.class, URL.class, URI.class, Class.class, JavaType.class,
                Currency.class, Pattern.class, Locale.class, Charset.class, TimeZone.class, InetAddress.class,
                InetSocketAddress.class, StringBuilder.class }, types);
    }

    @Test
    public void testFindDeserializer() {
        // Test findDeserializer method
        Std deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(deserializer);
        assertEquals(1, deserializer._kind);
        assertEquals(File.class, deserializer._valueClass);

        deserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertNotNull(deserializer);
        assertEquals(2, deserializer._kind);
        assertEquals(URL.class, deserializer._valueClass);

        deserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertNotNull(deserializer);
        assertEquals(3, deserializer._kind);
        assertEquals(URI.class, deserializer._valueClass);

        deserializer = FromStringDeserializer.findDeserializer(Class.class);
        assertNotNull(deserializer);
        assertEquals(4, deserializer._kind);
        assertEquals(Class.class, deserializer._valueClass);

        deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        assertNotNull(deserializer);
        assertEquals(5, deserializer._kind);
        assertEquals(JavaType.class, deserializer._valueClass);

        deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        assertNotNull(deserializer);
        assertEquals(6, deserializer._kind);
        assertEquals(Currency.class, deserializer._valueClass);

        deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        assertNotNull(deserializer);
        assertEquals(7, deserializer._kind);
        assertEquals(Pattern.class, deserializer._valueClass);

        deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertNotNull(deserializer);
        assertEquals(8, deserializer._kind);
        assertEquals(Locale.class, deserializer._valueClass);

        deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        assertNotNull(deserializer);
        assertEquals(9, deserializer._kind);
        assertEquals(Charset.class, deserializer._valueClass);

        deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        assertNotNull(deserializer);
        assertEquals(10, deserializer._kind);
        assertEquals(TimeZone.class, deserializer._valueClass);

        deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        assertNotNull(deserializer);
        assertEquals(11, deserializer._kind);
        assertEquals(InetAddress.class, deserializer._valueClass);

        deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertNotNull(deserializer);
        assertEquals(12, deserializer._kind);
        assertEquals(InetSocketAddress.class, deserializer._valueClass);

        deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        assertNotNull(deserializer);
        assertEquals(13, deserializer._kind);
        assertEquals(StringBuilder.class, deserializer._valueClass);

        deserializer = FromStringDeserializer.findDeserializer(Object.class);
        assertNull(deserializer);
    }
}
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 1);

        String result = deserializer.deserialize("test", new DeserializationContext());

        assertEquals("test", result);
    }

    @Test
    public void testDeserializeFromEmptyString() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 1);

        String result = deserializer.deserialize("", new DeserializationContext());

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalid() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Integer.class, 1);

        deserializer.deserialize("test", new DeserializationContext());
    }

    @Test
    public void testDeserializeFromFile() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);

        File result = deserializer.deserialize("/path/to/file.txt", new DeserializationContext());

        assertEquals(new File("/path/to/file.txt"), result);
    }

    @Test
    public void testDeserializeFromURL() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);

        URL result = deserializer.deserialize("http://www.example.com", new DeserializationContext());

        assertEquals(new URL("http://www.example.com"), result);
    }

    @Test
    public void testDeserializeFromURI() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);

        URI result = deserializer.deserialize("http://www.example.com", new DeserializationContext());

        assertEquals(new URI("http://www.example.com"), result);
    }

    @Test
    public void testDeserializeFromClass() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);

        Class<?> result = deserializer.deserialize("java.lang.String", new DeserializationContext());

        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeFromJavaType() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, 5);

        JavaType result = deserializer.deserialize("java.util.List", new DeserializationContext());

        assertEquals(JavaType.class, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeCurrencyInvalid() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);

        deserializer.deserialize("ABC", new DeserializationContext());
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);

        Currency result = deserializer.deserialize("USD", new DeserializationContext());

        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializePatternInvalid() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);

        deserializer.deserialize("[a-z", new DeserializationContext());
    }

    @Test
    public void testDeserializePattern() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);

        Pattern result = deserializer.deserialize("[a-zA-Z]", new DeserializationContext());

        assertEquals(Pattern.compile("[a-zA-Z]"), result);
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);

        Locale result = deserializer.deserialize("en_US", new DeserializationContext());

        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);

        Charset result = deserializer.deserialize("UTF-8", new DeserializationContext());

        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);

        TimeZone result = deserializer.deserialize("GMT+2", new DeserializationContext());

        assertEquals(TimeZone.getTimeZone("GMT+2"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);

        InetAddress result = deserializer.deserialize("127.0.0.1", new DeserializationContext());

        assertEquals(InetAddress.getByName("127.0.0.1"), result);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInetSocketAddressInvalid() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);

        deserializer.deserialize("[::1", new DeserializationContext());
    }

    @Test
    public void testDeserializeInetSocketAddressIPv6() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);

        InetSocketAddress result = deserializer.deserialize("[::1]:8080", new DeserializationContext());

        assertEquals(new InetSocketAddress("::1", 8080), result);
    }

    @Test
    public void testDeserializeInetSocketAddressIPv4() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);

        InetSocketAddress result = deserializer.deserialize("127.0.0.1:8080", new DeserializationContext());

        assertEquals(new InetSocketAddress("127.0.0.1", 8080), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);

        InetSocketAddress result = deserializer.deserialize("localhost:8080", new DeserializationContext());

        assertEquals(new InetSocketAddress("localhost", 8080), result);
    }

    @Test
    public void testDeserializeStringBuilder() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, 13);

        StringBuilder result = deserializer.deserialize("test", new DeserializationContext());

        assertEquals(new StringBuilder("test"), result);
    }
}
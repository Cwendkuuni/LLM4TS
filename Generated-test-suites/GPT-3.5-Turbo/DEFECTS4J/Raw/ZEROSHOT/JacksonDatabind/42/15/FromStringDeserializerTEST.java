package com.fasterxml.jackson.databind.deser.std;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize() {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(File.class, 1);

        try {
            deserializer.deserialize(null, null);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("Input cannot be null", e.getMessage());
        }
    }

    @Test
    public void testDeserialize_EmptyString() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);

        Assert.assertEquals(URI.create(""), deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testDeserialize_File() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);

        File expected = new File("/path/to/file");
        JsonParser jp = new JsonFactory().createParser("\"/path/to/file\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_Exception() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);

        JsonParser jp = new JsonFactory().createParser("\"\"");
        deserializer.deserialize(jp, null);
    }

    @Test
    public void testDeserialize_URL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);

        URL expected = new URL("http://example.com");
        JsonParser jp = new JsonFactory().createParser("\"http://example.com\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test
    public void testDeserialize_URI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);

        URI expected = URI.create("http://example.com");
        JsonParser jp = new JsonFactory().createParser("\"http://example.com\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test
    public void testDeserialize_Class() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);

        Class<?> expected = String.class;
        JsonParser jp = new JsonFactory().createParser("\"java.lang.String\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test
    public void testDeserialize_JavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);

        JavaType expected = JavaType.class;
        JsonParser jp = new JsonFactory().createParser("\"com.fasterxml.jackson.databind.JavaType\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test
    public void testDeserialize_Currency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);

        Currency expected = Currency.getInstance("USD");
        JsonParser jp = new JsonFactory().createParser("\"USD\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test
    public void testDeserialize_Pattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);

        Pattern expected = Pattern.compile("[0-9]+");
        JsonParser jp = new JsonFactory().createParser("\"[0-9]+\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test
    public void testDeserialize_Locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);

        Locale expected = new Locale("en", "US");
        JsonParser jp = new JsonFactory().createParser("\"en_US\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test
    public void testDeserialize_Charset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);

        Charset expected = Charset.forName("UTF-8");
        JsonParser jp = new JsonFactory().createParser("\"UTF-8\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);

        TimeZone expected = TimeZone.getTimeZone("GMT");
        JsonParser jp = new JsonFactory().createParser("\"GMT\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);

        InetAddress expected = InetAddress.getByName("localhost");
        JsonParser jp = new JsonFactory().createParser("\"localhost\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test
    public void testDeserialize_InetSocketAddress_IPv6() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        InetSocketAddress expected = new InetSocketAddress("::1", 80);
        JsonParser jp = new JsonFactory().createParser("\"[::1]:80\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test
    public void testDeserialize_InetSocketAddress_IPv4() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        InetSocketAddress expected = new InetSocketAddress("127.0.0.1", 80);
        JsonParser jp = new JsonFactory().createParser("\"127.0.0.1:80\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test
    public void testDeserialize_InetSocketAddress_HostOnly() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        InetSocketAddress expected = new InetSocketAddress("localhost", 0);
        JsonParser jp = new JsonFactory().createParser("\"localhost\"");

        Assert.assertEquals(expected, deserializer.deserialize(jp, null));
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_InetSocketAddress_MalformedIPv6() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        JsonParser jp = new JsonFactory().createParser("\"[::1\"");
        deserializer.deserialize(jp, null);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_InetSocketAddress_InvalidBracketedIPv6() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        JsonParser jp = new JsonFactory().createParser("\"[::1\"");
        deserializer.deserialize(jp, null);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_InetSocketAddress_InvalidHostPort() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        JsonParser jp = new JsonFactory().createParser("\"localhost:invalid\"");
        deserializer.deserialize(jp, null);
    }
}
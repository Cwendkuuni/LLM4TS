package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;

public class FromStringDeserializerTest {

    @Test
    public void testStdFile() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);

        assertEquals(new File("test.txt"), deserializer._deserialize("test.txt", null));
        assertEquals(new File(""), deserializer._deserialize("", null));
    }

    @Test
    public void testStdURL() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);

        assertEquals(new URL("http://example.com"), deserializer._deserialize("http://example.com", null));
        assertEquals(new URL(""), deserializer._deserialize("", null));
    }

    @Test
    public void testStdURI() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);

        assertEquals(URI.create("http://example.com"), deserializer._deserialize("http://example.com", null));
        assertEquals(URI.create(""), deserializer._deserialize("", null));
    }

    @Test
    public void testStdClass() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);

        assertEquals(Integer.class, deserializer._deserialize("java.lang.Integer", null));
        assertEquals(String.class, deserializer._deserialize("java.lang.String", null));
        try {
            deserializer._deserialize("invalid.class.name", null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof JsonMappingException);
        }
    }

    @Test
    public void testStdJavaType() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);

        assertEquals(JavaType.class, deserializer._deserialize("com.fasterxml.jackson.databind.JavaType", null).getClass());
        try {
            deserializer._deserialize("invalid.java.type", null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof JsonMappingException);
        }
    }

    @Test
    public void testStdCurrency() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);

        assertEquals(Currency.getInstance("USD"), deserializer._deserialize("USD", null));
        try {
            deserializer._deserialize("INVALID_CURRENCY_CODE", null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testStdPattern() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);

        assertEquals(Pattern.compile("\\w+"), deserializer._deserialize("\\w+", null));
        try {
            deserializer._deserialize("(", null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testStdLocale() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);

        assertEquals(Locale.US, deserializer._deserialize("en-US", null));
        try {
            deserializer._deserialize("INVALID_LOCALE", null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testStdCharset() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);

        assertEquals(Charset.forName("UTF-8"), deserializer._deserialize("UTF-8", null));
        try {
            deserializer._deserialize("INVALID_CHARSET", null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testStdTimeZone() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);

        assertEquals(TimeZone.getTimeZone("GMT-8:00"), deserializer._deserialize("GMT-8:00", null));
        try {
            deserializer._deserialize("INVALID_TIME_ZONE", null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
    
    @Test
    public void testStdInetAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);

        assertEquals(InetAddress.getLocalHost(), deserializer._deserialize("localhost", null));
        try {
            deserializer._deserialize("INVALID_HOST", null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof java.net.UnknownHostException);
        }
    }

    @Test
    public void testStdInetSocketAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        assertEquals(new InetSocketAddress("localhost", 80), deserializer._deserialize("localhost:80", null));
        try {
            deserializer._deserialize("INVALID_IP:PORT", null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof InvalidFormatException);
        }
    }

    @Test
    public void testStdStringBuilder() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);

        assertEquals(new StringBuilder("test"), deserializer._deserialize("test", null));
        assertEquals(new StringBuilder(""), deserializer._deserialize("", null));
    }
}
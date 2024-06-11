package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {

    @Test
    public void testStdFile() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, Std.STD_FILE);
        
        assertEquals(new File("path/to/file"), deserializer._deserialize("path/to/file", null));
    }

    @Test
    public void testStdURL() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, Std.STD_URL);
        
        assertEquals(new URL("http://www.example.com"), deserializer._deserialize("http://www.example.com", null));
    }

    @Test
    public void testStdURI() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, Std.STD_URI);
        
        assertEquals(new URI("http://www.example.com"), deserializer._deserialize("http://www.example.com", null));
    }

    @Test
    public void testStdClass() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, Std.STD_CLASS);
        
        assertEquals(String.class, deserializer._deserialize("java.lang.String", null));
    }

    @Test
    public void testStdCurrency() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, Std.STD_CURRENCY);
        
        assertEquals(Currency.getInstance("USD"), deserializer._deserialize("USD", null));
    }

    @Test
    public void testStdPattern() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, Std.STD_PATTERN);
        
        assertEquals(Pattern.compile("[a-zA-Z]+"), deserializer._deserialize("[a-zA-Z]+", null));
    }

    @Test
    public void testStdLocale() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, Std.STD_LOCALE);
        
        assertEquals(new Locale("en"), deserializer._deserialize("en", null));
        assertEquals(new Locale("en", "US"), deserializer._deserialize("en_US", null));
        assertEquals(new Locale("en", "US", "NY"), deserializer._deserialize("en_US_NY", null));
    }

    @Test
    public void testStdCharset() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, Std.STD_CHARSET);
        
        assertEquals(Charset.forName("UTF-8"), deserializer._deserialize("UTF-8", null));
    }

    @Test
    public void testStdTimeZone() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, Std.STD_TIME_ZONE);
        
        assertEquals(TimeZone.getTimeZone("PST"), deserializer._deserialize("PST", null));
    }

    @Test
    public void testStdInetAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, Std.STD_INET_ADDRESS);
        
        assertEquals(InetAddress.getByName("127.0.0.1"), deserializer._deserialize("127.0.0.1", null));
    }

    @Test
    public void testStdInetSocketAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, Std.STD_INET_SOCKET_ADDRESS);
        
        assertEquals(new InetSocketAddress("localhost", 80), deserializer._deserialize("localhost:80", null));
        assertEquals(new InetSocketAddress("[2001:db8::1]", 8080), deserializer._deserialize("[2001:db8::1]:8080", null));
    }
}
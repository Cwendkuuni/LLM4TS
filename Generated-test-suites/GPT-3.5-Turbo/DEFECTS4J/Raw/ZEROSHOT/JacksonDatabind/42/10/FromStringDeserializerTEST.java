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
    public void testDeserialize_file() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        String filename = "/path/to/file.txt";
        JsonParser jp = new JsonFactory().createParser("\"" + filename + "\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        File file = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(file.getPath(), filename);
    }

    @Test
    public void testDeserialize_url() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        String urlString = "https://www.example.com";
        JsonParser jp = new JsonFactory().createParser("\"" + urlString + "\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        URL url = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(url.toString(), urlString);
    }

    @Test
    public void testDeserialize_uri() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        String uriString = "https://www.example.com";
        JsonParser jp = new JsonFactory().createParser("\"" + uriString + "\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        URI uri = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(uri.toString(), uriString);
    }

    @Test
    public void testDeserialize_class() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        String className = "java.lang.String";
        JsonParser jp = new JsonFactory().createParser("\"" + className + "\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        Class<?> clazz = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(clazz.getName(), className);
    }

    @Test
    public void testDeserialize_javaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        String javaTypeName = "java.util.List<java.lang.String>";
        JsonParser jp = new JsonFactory().createParser("\"" + javaTypeName + "\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        JavaType javaType = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(javaType.toCanonical(), javaTypeName);
    }

    @Test
    public void testDeserialize_currency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        String currencyCode = "USD";
        JsonParser jp = new JsonFactory().createParser("\"" + currencyCode + "\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        Currency currency = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(currency.getCurrencyCode(), currencyCode);
    }

    @Test
    public void testDeserialize_pattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        String patternString = "[a-z]+";
        JsonParser jp = new JsonFactory().createParser("\"" + patternString + "\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        Pattern pattern = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(pattern.pattern(), patternString);
    }

    @Test
    public void testDeserialize_locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        String localeString = "en_US";
        JsonParser jp = new JsonFactory().createParser("\"" + localeString + "\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        Locale locale = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(locale.toString(), localeString);
    }

    @Test
    public void testDeserialize_charset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        String charsetString = "UTF-8";
        JsonParser jp = new JsonFactory().createParser("\"" + charsetString + "\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        Charset charset = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(charset.toString(), charsetString);
    }

    @Test
    public void testDeserialize_timeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        String timeZoneString = "America/Los_Angeles";
        JsonParser jp = new JsonFactory().createParser("\"" + timeZoneString + "\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        TimeZone timeZone = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(timeZone.getID(), timeZoneString);
    }

    @Test
    public void testDeserialize_inetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        String addressString = "10.0.0.1";
        JsonParser jp = new JsonFactory().createParser("\"" + addressString + "\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        InetAddress address = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(address.getHostAddress(), addressString);
    }

    @Test
    public void testDeserialize_inetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        String addressString = "127.0.0.1";
        int port = 8080;
        JsonParser jp = new JsonFactory().createParser("\"" + addressString + ":" + port + "\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        InetSocketAddress address = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(address.getHostString(), addressString);
        Assert.assertEquals(address.getPort(), port);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_invalidInput() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        String invalidInput = "invalidinput";
        JsonParser jp = new JsonFactory().createParser("\"" + invalidInput + "\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        deserializer.deserialize(jp, ctxt);
    }

    @Test
    public void testDeserialize_emptyString_uri() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        JsonParser jp = new JsonFactory().createParser("\"\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        URI uri = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(uri.toString(), "");
    }

    @Test
    public void testDeserialize_emptyString_locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        JsonParser jp = new JsonFactory().createParser("\"\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);
        Locale locale = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(locale.toString(), "");
    }

}
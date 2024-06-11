package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws Exception {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        DeserializationContext context = new DeserializationContext(null, null, null);
        File file = (File) fileDeserializer.deserialize(context.getParser(), context);
        Assert.assertNotNull(file);
        Assert.assertTrue(file instanceof File);
    }

    @Test
    public void testDeserializeURL() throws Exception {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        DeserializationContext context = new DeserializationContext(null, null, null);
        URL url = (URL) urlDeserializer.deserialize(context.getParser(), context);
        Assert.assertNotNull(url);
        Assert.assertTrue(url instanceof URL);
    }

    @Test
    public void testDeserializeURI() throws Exception {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        DeserializationContext context = new DeserializationContext(null, null, null);
        URI uri = (URI) uriDeserializer.deserialize(context.getParser(), context);
        Assert.assertNotNull(uri);
        Assert.assertTrue(uri instanceof URI);
    }

    @Test
    public void testDeserializeClass() throws Exception {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        DeserializationContext context = new DeserializationContext(null, null, null);
        Class<?> classObj = (Class<?>) classDeserializer.deserialize(context.getParser(), context);
        Assert.assertNotNull(classObj);
        Assert.assertTrue(classObj instanceof Class);
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        DeserializationContext context = new DeserializationContext(null, null, null);
        Currency currency = (Currency) currencyDeserializer.deserialize(context.getParser(), context);
        Assert.assertNotNull(currency);
        Assert.assertTrue(currency instanceof Currency);
    }

    @Test
    public void testDeserializePattern() throws Exception {
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        DeserializationContext context = new DeserializationContext(null, null, null);
        Pattern pattern = (Pattern) patternDeserializer.deserialize(context.getParser(), context);
        Assert.assertNotNull(pattern);
        Assert.assertTrue(pattern instanceof Pattern);
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        DeserializationContext context = new DeserializationContext(null, null, null);
        Locale locale = (Locale) localeDeserializer.deserialize(context.getParser(), context);
        Assert.assertNotNull(locale);
        Assert.assertTrue(locale instanceof Locale);
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        DeserializationContext context = new DeserializationContext(null, null, null);
        Charset charset = (Charset) charsetDeserializer.deserialize(context.getParser(), context);
        Assert.assertNotNull(charset);
        Assert.assertTrue(charset instanceof Charset);
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        DeserializationContext context = new DeserializationContext(null, null, null);
        TimeZone timeZone = (TimeZone) timeZoneDeserializer.deserialize(context.getParser(), context);
        Assert.assertNotNull(timeZone);
        Assert.assertTrue(timeZone instanceof TimeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        DeserializationContext context = new DeserializationContext(null, null, null);
        InetAddress inetAddress = (InetAddress) inetAddressDeserializer.deserialize(context.getParser(), context);
        Assert.assertNotNull(inetAddress);
        Assert.assertTrue(inetAddress instanceof InetAddress);
    }

    @Test
    public void testDeserializeInetSocketAddressIPv4() throws Exception {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        DeserializationContext context = new DeserializationContext(null, null, null);
        String address = "127.0.0.1";
        String port = "8080";
        InetSocketAddress inetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(context.getParser(), context);
        Assert.assertNotNull(inetSocketAddress);
        Assert.assertEquals(address, inetSocketAddress.getAddress().getHostAddress());
        Assert.assertEquals(Integer.parseInt(port), inetSocketAddress.getPort());
    }

    @Test
    public void testDeserializeStringBuilder() throws Exception {
        FromStringDeserializer.Std stringBuilderDeserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        DeserializationContext context = new DeserializationContext(null, null, null);
        StringBuilder stringBuilder = (StringBuilder) stringBuilderDeserializer.deserialize(context.getParser(), context);
        Assert.assertNotNull(stringBuilder);
        Assert.assertTrue(stringBuilder instanceof StringBuilder);
    }
}
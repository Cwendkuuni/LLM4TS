
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Test;

public class FromStringDeserializerTest {
    @Test
    public void testDeserializeFile() throws Exception {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = (File) std.deserialize(new JsonParser(null,null), new DeserializationContext(null,null,null));
        assertNull(file);
    }

    @Test
    public void testDeserializeURL() throws Exception {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = (URL) std.deserialize(new JsonParser(null,null), new DeserializationContext(null,null,null));
        assertNull(url);
    }

    @Test
    public void testDeserializeURI() throws Exception {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI) std.deserialize(new JsonParser(null,null), new DeserializationContext(null,null,null));
        assertNull(uri);
    }

    @Test
    public void testDeserializeClass() throws Exception {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class clazz = (Class) std.deserialize(new JsonParser(null,null), new DeserializationContext(null,null,null));
        assertNull(clazz);
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType javaType = (JavaType) std.deserialize(new JsonParser(null,null), new DeserializationContext(null,null,null));
        assertNull(javaType);
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = (Currency) std.deserialize(new JsonParser(null,null), new DeserializationContext(null,null,null));
        assertNull(currency);
    }

    @Test
    public void testDeserializePattern() throws Exception {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = (Pattern) std.deserialize(new JsonParser(null,null), new DeserializationContext(null,null,null));
        assertNull(pattern);
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale) std.deserialize(new JsonParser(null,null), new DeserializationContext(null,null,null));
        assertNull(locale);
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = (Charset) std.deserialize(new JsonParser(null,null), new DeserializationContext(null,null,null));
        assertNull(charset);
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = (TimeZone) std.deserialize(new JsonParser(null,null), new DeserializationContext(null,null,null));
        assertNull(timeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = (InetAddress) std.deserialize(new JsonParser(null,null), new DeserializationContext(null,null,null));
        assertNull(inetAddress);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) std.deserialize(new JsonParser(null,null), new DeserializationContext(null,null,null));
        assertNull(inetSocketAddress);
    }
}

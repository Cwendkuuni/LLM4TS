package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FromStringDeserializerTest {
    
    private FromStringDeserializer.Std deserializer;

    @Test
    public void testDeserialize_file() throws Exception {
        deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        
        File expected = new File("test.txt");
        File actual = (File) deserializer.deserialize(getJsonParser(expected.getPath()));

        assertEquals(expected.getPath(), actual.getPath());
    }

    @Test
    public void testDeserialize_url() throws Exception {
        deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);

        URL expected = new URL("http://www.google.com");
        URL actual = (URL) deserializer.deserialize(getJsonParser(expected.toString()));

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testDeserialize_uri() throws Exception {
        deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);

        URI expected = new URI("http://www.google.com");
        URI actual = (URI) deserializer.deserialize(getJsonParser(expected.toString()));

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testDeserialize_class() throws Exception {
        deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);

        Class<?> expected = String.class;
        Class<?> actual = (Class<?>) deserializer.deserialize(getJsonParser(expected.getName()));

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_javaType() throws Exception {
        deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);

        JavaType expected = JavaType.construct(String.class);
        JavaType actual = (JavaType) deserializer.deserialize(getJsonParser(expected.toCanonical()));

        assertEquals(expected.toCanonical(), actual.toCanonical());
    }

    @Test
    public void testDeserialize_currency() throws Exception {
        deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);

        Currency expected = Currency.getInstance("USD");
        Currency actual = (Currency) deserializer.deserialize(getJsonParser(expected.getCurrencyCode()));

        assertEquals(expected.getCurrencyCode(), actual.getCurrencyCode());
    }

    @Test
    public void testDeserialize_pattern() throws Exception {
        deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);

        Pattern expected = Pattern.compile("[a-z]+");
        Pattern actual = (Pattern) deserializer.deserialize(getJsonParser(expected.pattern()));

        assertEquals(expected.pattern(), actual.pattern());
    }

    @Test
    public void testDeserialize_locale() throws Exception {
        deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);

        Locale expected = Locale.US;
        Locale actual = (Locale) deserializer.deserialize(getJsonParser(expected.toString()));

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testDeserialize_charset() throws Exception {
        deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);

        Charset expected = Charset.forName("UTF-8");
        Charset actual = (Charset) deserializer.deserialize(getJsonParser(expected.name()));

        assertEquals(expected.name(), actual.name());
    }

    @Test
    public void testDeserialize_timeZone() throws Exception {
        deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);

        TimeZone expected = TimeZone.getTimeZone("GMT");
        TimeZone actual = (TimeZone) deserializer.deserialize(getJsonParser(expected.getID()));

        assertEquals(expected.getID(), actual.getID());
    }

    @Test
    public void testDeserialize_inetAddress() throws Exception {
        deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);

        InetAddress expected = InetAddress.getByName("localhost");
        InetAddress actual = (InetAddress) deserializer.deserialize(getJsonParser(expected.getHostAddress()));

        assertEquals(expected.getHostAddress(), actual.getHostAddress());
    }

    @Test
    public void testDeserialize_inetSocketAddress() throws Exception {
        deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        InetSocketAddress actual = (InetSocketAddress) deserializer.deserialize(getJsonParser(expected.getHostString() + ":" + expected.getPort()));

        assertEquals(expected.getHostString(), actual.getHostString());
        assertEquals(expected.getPort(), actual.getPort());
    }

    @Test
    public void testDeserialize_unknown() throws Exception {
        deserializer = new FromStringDeserializer.Std(Object.class, 0);

        try {
            deserializer.deserialize(getJsonParser("unknown"));
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // expected
        }
    }
    
    private JsonParser getJsonParser(String value) {
        return new JsonParserStub(value);
    }
    
    private static class JsonParserStub extends JsonParser {

        private String value;

        public JsonParserStub(String value) {
            this.value = value;
        }

        @Override
        public JsonToken nextToken() {
            return JsonToken.VALUE_STRING;
        }

        @Override
        public String getText() {
            return value;
        }
        
        // Other overridden methods are irrelevant for the test case
    }
}
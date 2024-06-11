package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserializeAllTypes() throws IOException {
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File file = (File) fileDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertEquals("Expected file deserialization to work", new File("test"), file);

        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL url = (URL) urlDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertEquals("Expected URL deserialization to work", new URL("http://test.com"), url);

        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = (URI) uriDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertEquals("Expected URI deserialization to work", new URI("http://test.com"), uri);

        FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> clazz = classDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertEquals("Expected Class deserialization to work", String.class, clazz);

        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType javaType = (JavaType) javaTypeDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertEquals("Expected JavaType deserialization to work", JsonType.constructUnboundedType(String.class), javaType);

        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = currencyDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertEquals("Expected Currency deserialization to work", Currency.getInstance("USD"), currency);

        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern pattern = patternDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertNotNull("Expected Pattern deserialization to work", pattern);

        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = localeDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertEquals("Expected Locale deserialization to work", Locale.getDefault(), locale);

        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset charset = charsetDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertEquals("Expected Charset deserialization to work", Charset.forName("UTF-8"), charset);

        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZone = timeZoneDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertEquals("Expected TimeZone deserialization to work", TimeZone.getDefault(), timeZone);

        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress inetAddress = inetAddressDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertNotNull("Expected InetAddress deserialization to work", inetAddress);

        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertNotNull("Expected InetSocketAddress deserialization to work", inetSocketAddress);

        FromStringDeserializer<StringBuilder> stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder stringBuilder = (StringBuilder) stringBuilderDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertEquals("Expected StringBuilder deserialization to work", "", stringBuilder.toString());
    }

    @org.junit.Test
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String result = (String) stringDeserializer.deserialize(new JsonParser(), new DeserializationContext());
        assertNull("Expected String deserialization from empty string to return null", result);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJsonString() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"invalid\":\"JSON\"}");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        deserializer.deserialize(parser, new DeserializationContext());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidType() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{\"type\":\"Int\"}");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(String.class);
        deserializer.deserialize(parser, new DeserializationContext());
    }

    @org.junit.Test(expected = MalformedURLException.class)
    public void testDeserializeMalformedURL() throws IOException {
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        urlDeserializer.deserialize("invalid_url", new DeserializationContext());
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidLocale() throws IOException {
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        localeDeserializer.deserialize("invalid_locale", new DeserializationContext());
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        timeZoneDeserializer.deserialize("invalid_timezone", new DeserializationContext());
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidPattern() throws IOException {
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        patternDeserializer.deserialize("invalid_pattern", new DeserializationContext());
    }
}
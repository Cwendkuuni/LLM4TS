package com.example.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFileDeserialization() throws IOException {
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File file = deserializer.deserialize(mapper.getFactory().createParser("path/to/file.txt"), mapper.getDeserializationContext());
        assertEquals(new File("path/to/file.txt"), file);
    }

    @Test
    public void testURLDeserialization() throws IOException {
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL url = deserializer.deserialize(mapper.getFactory().createParser("http://example.com"), mapper.getDeserializationContext());
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException {
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = deserializer.deserialize(mapper.getFactory().createParser("http://example.com/path/to/resource"), mapper.getDeserializationContext());
        assertEquals("http", uri.getScheme());
        assertEquals("/path/to/resource", uri.getPath());
    }

    @Test
    public void testClassDeserialization() throws IOException, ClassNotFoundException {
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder stringBuilder = deserializer.deserialize(mapper.getFactory().createParser("new StringBuilder()"), mapper.getDeserializationContext());
        assertSame(new StringBuilder(), stringBuilder);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType javaType = deserializer.deserialize(mapper.getFactory().createParser("com.example.MyClass"), mapper.getDeserializationContext());
        assertEquals(JavaType.construct(String.class), javaType);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = deserializer.deserialize(mapper.getFactory().createParser("USD"), mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern pattern = deserializer.deserialize(mapper.getFactory().createParser("/abc/def/.*"), mapper.getDeserializationContext());
        assertEquals(Pattern.compile("/abc/def/.*"), pattern);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = deserializer.deserialize(mapper.getFactory().createParser("en_US"), mapper.getDeserializationContext());
        assertEquals(Locale.US, locale);

        locale = deserializer.deserialize(mapper.getFactory().createParser("en_US_POSIX"), mapper.getDeserializationContext());
        assertEquals(Locale.US, locale);

        locale = deserializer.deserialize(mapper.getFactory().createParser("fr_FR"), mapper.getDeserializationContext());
        assertEquals(Locale.FRENCH, locale);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset charset = deserializer.deserialize(mapper.getFactory().createParser("UTF-8"), mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZone = deserializer.deserialize(mapper.getFactory().createParser("UTC"), mapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("UTC"), timeZone);
    }

    @Test
    public void testInvalidStringDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertNull(deserializer.deserialize(mapper.getFactory().createParser("invalid"), mapper.getDeserializationContext()));
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertNull(deserializer.deserialize(mapper.getFactory().createParser(""), mapper.getDeserializationContext()));
    }

    @Test
    public void testEmptyObjectDeserialization() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        assertNull(deserializer.deserialize(mapper.getFactory().createParser(""), mapper.getDeserializationContext()));
    }

    @Test
    public void testNullDeserialization() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        assertNull(deserializer.deserialize(mapper.getFactory().createParser(null), mapper.getDeserializationContext()));
    }

    @Test
    public void testInvalidFormatException() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(dateFormat);

        FromStringDeserializer<Date> deserializer = FromStringDeserializer.findDeserializer(Date.class);
        try {
            deserializer.deserialize(mapper.getFactory().createParser("2012-12-32"), mapper.getDeserializationContext());
            fail();
        } catch (InvalidFormatException e) {
            // expected
        }
    }
}
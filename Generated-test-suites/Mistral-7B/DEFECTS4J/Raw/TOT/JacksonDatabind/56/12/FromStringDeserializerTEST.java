package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializationOfFile() throws IOException, JsonProcessingException {
        String json = "\"C:\\test\\file.txt\"";
        File expected = new File("C:\\test\\file.txt");
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class);
        File actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializationOfURL() throws IOException, JsonProcessingException {
        String json = "\"http://test.com/\"";
        URL expected = new URL("http://test.com/");
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer.Std(URL.class);
        URL actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializationOfURI() throws IOException, JsonProcessingException {
        String json = "\"http://test.com/\"".replaceAll("\\/", "%2F");
        URI expected = new URI("http://test.com/");
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer.Std(URI.class);
        URI actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializationOfClass() throws IOException, JsonProcessingException {
        String json = "java.lang.String";
        Class<?> expected = String.class;
        FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer.Std(Class.class);
        Class<?> actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(expected, actual);

        // Test null and non-existent class
        json = "java.lang.NullPointerException";
        try {
            actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("not a valid textual representation, class java.lang.NullPointerException not found", e.getMessage());
        }

        json = "java.lang.Foo";
        try {
            actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("not a valid textual representation, class java.lang.Foo not found", e.getMessage());
        }
    }

    @Test
    public void testDeserializationOfJavaType() throws IOException, JsonProcessingException {
        JavaType expected = mapper.getTypeFactory().constructType(String[].class);
        String json = "java.lang.Class<java.lang.String[]>";
        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer.Std(JavaType.class);
        JavaType actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializationOfCurrency() throws IOException, JsonProcessingException {
        String json = "\"USD\"";
        Currency expected = Currency.getInstance("USD");
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer.Std(Currency.class);
        Currency actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializationOfPattern() throws IOException, JsonProcessingException {
        String json = "\"^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.([a-zA-Z]{2,4})+$\"";
        Pattern expected = Pattern.compile(json);
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer.Std(Pattern.class);
        Pattern actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializationOfLocale() throws IOException, JsonProcessingException {
        String json = "\"en_US\"";
        Locale expected = Locale.forLanguageTag("en_US");
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer.Std(Locale.class);
        Locale actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializationOfCharset() throws IOException, JsonProcessingException {
        String json = "\"UTF-8\"";
        Charset expected = Charset.forName(json);
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer.Std(Charset.class);
        Charset actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializationOfTimeZone() throws IOException, JsonProcessingException {
        String json = "\"America/New_York\"";
        TimeZone expected = TimeZone.getTimeZone(json);
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer.Std(TimeZone.class);
        TimeZone actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializationOfInetAddress() throws IOException, JsonProcessingException {
        String json = "\"192.168.1.1\"";
        InetAddress expected = InetAddress.getByName("192.168.1.1");
        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer.Std(InetAddress.class);
        InetAddress actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializationOfInetSocketAddress() throws IOException, JsonProcessingException {
        String json = "[\"192.168.1.1\", 80]";
        InetSocketAddress expected = new InetSocketAddress("192.168.1.1", 80);
        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer.Std(InetSocketAddress.class);
        InetSocketAddress actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializationWithEmptyString() throws IOException, JsonProcessingException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer.Std(String.class);
        try {
            deserializer.deserialize(mapper.getFactory().createParser(""), mapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.getCause() instanceof IOException);
            assertEquals("not a valid textual representation: empty string", e.getMessage());
        }
    }

    @Test
    public void testDeserializationWithUnknownType() throws IOException, JsonProcessingException {
        String json = "\"FooBar\"";
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(Object.class);
        try {
            deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.getCause() instanceof IOException);
            assertEquals("not a valid textual representation, class FooBar not found", e.getMessage());
        }
    }

    @Test
    public void testDeserializationWithEmptyArray() throws IOException, JsonProcessingException {
        String json = "[\"\", \"\", \"\"]";
        FromStringDeserializer<Object[]> deserializer = new FromStringDeserializer.Std(Object[].class);
        Object[] actual = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertArrayEquals(new Object[0], actual);
    }

    @Test
    public void testDeserializationWithInvalidJson() throws IOException, JsonProcessingException {
        String json = "{";
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(Object.class);
        try {
            deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.isUnexpectedType());
            assertEquals("Unexpected type, expected: Object but was: START_OBJECT", e.getMessage());
        }
    }
}
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ModuleClassIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> JavaType getType(Class<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getTypeFactory().constructType(type);
    }

    @Test
    public void testFileDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"path/to/file.txt\"");
        FromStringDeserializer<File> fileDeserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        File result = fileDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testURLDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"http://example.com\"");
        FromStringDeserializer<URL> urlDeserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        URL result = urlDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"http://example.com/path/to/file.txt\"");
        FromStringDeserializer<URI> uriDeserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        URI result = uriDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("http://example.com/path/to/file.txt", result.toString());
    }

    @Test
    public void testClassDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("java.lang.String");
        FromStringDeserializer<Class<?>> clazzDeserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = clazzDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(String.class, result);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("java.util.List<java.lang.String>");
        FromStringDeserializer<JavaType> javaTypeDeserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = javaTypeDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(JavaType.constructType(List.class, String.class), result);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"EUR\"");
        FromStringDeserializer<Currency> currencyDeserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = currencyDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"[a-zA-Z]+\\\\.([a-zA-Z]{2})\\\\b\"");
        FromStringDeserializer<Pattern> patternDeserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = patternDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("[a-zA-Z]+\\.([a-zA-Z]{2})\\b", result.pattern());
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"en_US\"");
        FromStringDeserializer<Locale> localeDeserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = localeDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("en_US", result.toString());
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"US-ASCII\"");
        FromStringDeserializer<Charset> charsetDeserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = charsetDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Charset.forName("US-ASCII"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"UTC\"");
        FromStringDeserializer<TimeZone> timeZoneDeserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = timeZoneDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("UTC", result.getID());
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"127.0.0.1\"");
        FromStringDeserializer<InetAddress> inetAddressDeserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = inetAddressDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("127.0.0.1", result.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"[255.255.255.255]:80\"");
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = inetSocketAddressDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("[255.255.255.255]:80", result.toString());
    }

    @Test
    public void testUnknownTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"java.util.ArrayList\"");
        FromStringDeserializer<?> unknownTypeDeserializer = FromStringDeserializer.findDeserializer(ArrayList.class);
        try {
            unknownTypeDeserializer.deserialize(jp, mapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Don't know how to convert embedded Object of type [Ljava.util.ArrayList; into class java.lang.Object", e.getMessage());
        }
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"\"");
        FromStringDeserializer<URI> uriDeserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        URI result = uriDeserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testInvalidTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"[1, 2, 3]\"");
        FromStringDeserializer<Integer> integerDeserializer = (FromStringDeserializer<Integer>) FromStringDeserializer.findDeserializer(Integer.class);
        try {
            integerDeserializer.deserialize(jp, mapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not a valid textual representation of a Java Number: \"[1, 2, 3]\"", e.getMessage());
        }
    }
}
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"/path/to/file.txt\"";
        File file = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals("/path/to/file.txt", file.getAbsolutePath());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeUrl() throws IOException {
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"http://www.example.com\"";
        URL url = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals("http://www.example.com", url.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeUri() throws IOException {
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"http://www.example.com/path/to/resource\"";
        URI uri = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals("http://www.example.com/path/to/resource", uri.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeClass() throws IOException, ClassNotFoundException {
        FromStringDeserializer<Class<?>> deserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(Class.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"java.lang.String\"";
        Class<?> clazz = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(String.class, clazz);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer<JavaType> deserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType("java.util.List<java.lang.String>");
        String jsonString = javaType.getClass().getName() + "<String>";
        JavaType deserializedJavaType = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(javaType, deserializedJavaType);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"EUR\"";
        Currency currency = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("EUR"), currency);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "/[a-z]+/";
        Pattern pattern = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Pattern.compile("/[a-z]+/"), pattern);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"en_US\"";
        Locale locale = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Locale.US, locale);

        jsonString = "\"fr_FR\"";
        locale = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Locale.FRANCE, locale);

        jsonString = "\"fr_FR_POSIX\"";
        locale = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Locale.ROOT, locale);
        assertNull(locale.getLanguage());
        assertNull(locale.getCountry());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"UTF-8\"";
        Charset charset = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"America/Los_Angeles\"";
        TimeZone timeZone = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), timeZone);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"192.168.1.1\"";
        InetAddress inetAddress = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(InetAddress.getByName("192.168.1.1"), inetAddress);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[\"192.168.1.1\", 80]";
        InetSocketAddress inetSocketAddress = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 80), inetSocketAddress);

        jsonString = "[\"::1\", 80]";
        inetSocketAddress = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("::1"), 80), inetSocketAddress);

        jsonString = "[\"192.168.1.1\", 8080]";
        inetSocketAddress = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 8080), inetSocketAddress);

        jsonString = "[\"192.168.1.1\", \"http\"]";
        try {
            deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}
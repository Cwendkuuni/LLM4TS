package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

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
        String jsonString = "\"http://example.com\"";
        URL url = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals("http://example.com", url.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeUri() throws IOException {
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"http://example.com/path/to/file.txt\"";
        URI uri = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals("http://example.com/path/to/file.txt", uri.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeClass() throws IOException, ClassNotFoundException {
        FromStringDeserializer<Class<?>> deserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(Class.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "java.lang.String";
        Class<?> clazz = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(String.class, clazz);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer<JavaType> deserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "java.lang.String[1]";
        JavaType javaType = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(JavaType.constructCollectionType(Map.class, String.class, 1), javaType);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "USD";
        Currency currency = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "/[a-z]+/gi";
        Pattern pattern = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Pattern.compile("/[a-z]+/gi"), pattern);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "en_US";
        Locale locale = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Locale.US, locale);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "UTF-8";
        Charset charset = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "America/New_York";
        TimeZone timeZone = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("America/New_York"), timeZone);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "192.168.1.1";
        InetAddress inetAddress = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(InetAddress.getByName("192.168.1.1"), inetAddress);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[::1]:80";
        InetSocketAddress inetSocketAddress = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByAddress(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }), 80), inetSocketAddress);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer<String> deserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"\"";
        String result = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext()).toString();
        assertEquals("", result);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws IOException {
        FromStringDeserializer<Integer> deserializer = (FromStringDeserializer<Integer>) FromStringDeserializer.findDeserializer(Integer.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "A123";
        deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
    }

}
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FromStringDeserializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);

        String filePath = "/test/sample.txt";
        File expected = new File(filePath);

        JsonParser parser = mapper.getFactory().createParser("\"/" + filePath + "\"");
        File result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);

        String urlString = "https://www.example.com/";
        URL expected = new URL(urlString);

        JsonParser parser = mapper.getFactory().createParser("\"https://www.example.com/\"");
        URL result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);

        String uriString = "https://www.example.com/path/to/file.txt?query=value#fragment";
        URI expected = new URI(uriString);

        JsonParser parser = mapper.getFactory().createParser("\"https://www.example.com/path/to/file.txt?query=value#fragment\"");
        URI result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer<Class<?>> deserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(Class.class);

        String className = "java.lang.String";
        Class<?> expected = Class.forName(className);

        JsonParser parser = mapper.getFactory().createParser("\"java.lang.String\"");
        Class<?> result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer<JavaType> deserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);

        String javaTypeString = "java.lang.String[]";
        JavaType expected = mapper.getTypeFactory().constructArrayType(JavaType.constructType(String.class));

        JsonParser parser = mapper.getFactory().createParser("\"java.lang.String[]\"");
        JavaType result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);

        String currencyCode = "USD";
        Currency expected = Currency.getInstance(currencyCode);

        JsonParser parser = mapper.getFactory().createParser("\"USD\"");
        Currency result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);

        String regex = "\\w+@\\w+\\.\\w+";
        Pattern expected = Pattern.compile(regex);

        JsonParser parser = mapper.getFactory().createParser("\"\\w+@\\w+\\.\\w+\"");
        Pattern result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(expected.pattern(), result.pattern());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);

        String localeString = "en_US";
        Locale expected = new Locale("en", "US");

        JsonParser parser = mapper.getFactory().createParser("\"en_US\"");
        Locale result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);

        String charsetName = "UTF-8";
        Charset expected = Charset.forName(charsetName);

        JsonParser parser = mapper.getFactory().createParser("\"UTF-8\"");
        Charset result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);

        String timeZoneID = "UTC";
        TimeZone expected = TimeZone.getTimeZone(timeZoneID);

        JsonParser parser = mapper.getFactory().createParser("\"UTC\"");
        TimeZone result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);

        String ipAddress = "192.168.1.1";
        InetAddress expected = InetAddress.getByName(ipAddress);

        JsonParser parser = mapper.getFactory().createParser("\"192.168.1.1\"");
        InetAddress result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);

        String socketAddressString = "[255.255.255.255]:8080";
        InetSocketAddress expected = new InetSocketAddress("255.255.255.255", 8080);

        JsonParser parser = mapper.getFactory().createParser("\"[255.255.255.255]:8080\"");
        InetSocketAddress result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInvalidString() throws IOException {
        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);

        String invalidString = "invalid";

        JsonParser parser = mapper.getFactory().createParser("\"invalid\"");
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(parser, mapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);

        Object embeddedObject = new Locale("en", "US");

        JsonParser parser = mapper.getFactory().createParser("{\"locale\": \"en_US\"}");
        Locale result = deserializer.deserializeEmbedded(embeddedObject, mapper.getDeserializationContext());

        assertEquals(embeddedObject, result);
    }

    @Test
    public void testDeserializeEmptyStringForClass() throws IOException {
        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);

        JsonParser parser = mapper.getFactory().createParser("\"\"");
        Object result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertNull(result);
    }

    @Test
    public void testDeserializeEmptyStringForURI() throws IOException {
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);

        JsonParser parser = mapper.getFactory().createParser("\"\"");
        URI result = deserializer.deserialize(parser, mapper.getDeserializationContext());

        assertEquals(URI.create(""), result);
    }

}
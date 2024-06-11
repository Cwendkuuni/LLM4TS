package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserializeFile() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(File.class);
        Locale locale = Locale.getDefault();
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.createParser("path/to/file.txt"), ctxt);
        assertEquals(new File("path/to/file.txt"), result);
    }

    @Test
    public void testDeserializeURL() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.createParser("http://www.example.com"), ctxt);
        assertEquals(new URL("http://www.example.com"), result);
    }

    @Test
    public void testDeserializeURI() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(URI.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.createParser("http://www.example.com"), ctxt);
        assertEquals(new URI("http://www.example.com"), result);
    }

    @Test
    public void testDeserializeClass() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(String.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.createParser("java.lang.String"), ctxt);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testDeserializeJavaType() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(String.class.getTypeName());
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.createParser("java.lang.String"), ctxt);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testDeserializeCurrency() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Currency.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.createParser("USD"), ctxt);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Pattern.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.createParser("/(.*)\\.txt$/"), ctxt);
        Pattern pattern = (Pattern) result;
        assertTrue(pattern.matcher("test.txt").matches());
    }

    @Test
    public void testDeserializeLocale() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.createParser("en_US"), ctxt);
        assertEquals(Locale.US, result);

        result = deserializer.deserialize(objectMapper.createParser("en_US_POSIX"), ctxt);
        assertEquals(Locale.US, result);

        result = deserializer.deserialize(objectMapper.createParser("en_GB_POSIX"), ctxt);
        assertEquals(Locale.UK, result);
    }

    @Test
    public void testDeserializeCharset() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(Charset.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.createParser("UTF-8"), ctxt);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(TimeZone.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.createParser("UTC"), ctxt);
        assertEquals(TimeZone.getTimeZone("UTC"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(InetAddress.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.createParser("192.168.1.1"), ctxt);
        InetAddress inetAddress = (InetAddress) result;
        assertTrue(inetAddress.isLoopbackAddress());
        result = deserializer.deserialize(objectMapper.createParser("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]"), ctxt);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) result;
        assertEquals(27015, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.createParser("localhost:8080"), ctxt);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) result;
        assertEquals(8080, inetSocketAddress.getPort());

        result = deserializer.deserialize(objectMapper.createParser("[255.255.255.255]:8080"), ctxt);
        InetSocketAddress inetSocketAddressBroadcast = (InetSocketAddress) result;
        assertEquals(8080, inetSocketAddressBroadcast.getPort());

        result = deserializer.deserialize(objectMapper.createParser("127.0.0.1:1900"), ctxt);
        InetSocketAddress inetSocketAddressMulticast = (InetSocketAddress) result;
        assertEquals(1900, inetSocketAddressMulticast.getPort());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws JsonProcessingException, IOException {
        FromStringDeserializer deserializer = (FromStringDeserializer) FromStringDeserializer.findDeserializer(int.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.createParser("42"), ctxt);
    }
}
package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;

@RunWith(JUnit4.class)
public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    public FromStringDeserializerTest() {
        SimpleModule module = new SimpleModule();
        mapper = new ObjectMapper();
        module.addDeserializers(FromStringDeserializer.types());
        mapper.registerModule(module);
    }

    @Test
    public void testDeserializeFile() throws IOException, JsonMappingException {
        Std deserializer = FromStringDeserializer.findDeserializer(File.class);
        String json = "\"/path/to/file.txt\"";
        File expected = new File("/path/to/file.txt");
        File result = (File) deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException, JsonMappingException {
        Std deserializer = FromStringDeserializer.findDeserializer(URL.class);
        String json = "\"http://example.com\"";
        URL expected = new URL("http://example.com");
        URL result = (URL) deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException, JsonMappingException {
        Std deserializer = FromStringDeserializer.findDeserializer(URI.class);
        String json = "\"http://example.com/path/to/resource?query=param#fragment\"";
        URI expected = new URI("http", "example.com", "/path/to/resource", null, "query=param", null, "#fragment");
        URI result = (URI) deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException, JsonMappingException {
        Std deserializer = FromStringDeserializer.findDeserializer(String.class);
        String json = "\"java.lang.String\"";
        String expected = "test";
        String result = (String) deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException, JsonMappingException {
        Std deserializer = FromStringDeserializer.findDeserializer(String[].class);
        String json = "\"[Ljava.lang.String;\"";
        String[] expected = {"test1", "test2"};
        String[] result = (String[]) deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException, JsonMappingException {
        Std deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        String json = "\"USD\"";
        Currency expected = Currency.getInstance("USD");
        Currency result = (Currency) deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals(expected.getCurrencyCode(), result.getCurrencyCode());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidPattern() throws IOException, JsonMappingException {
        Std deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        String json = "\"[a-z]+\""; // invalid regular expression
        deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
    }

    @Test
    public void testDeserializeLocale() throws IOException, JsonMappingException {
        Std deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String json = "\"en_US\"";
        Locale expected = Locale.forLanguageTag("en-US");
        Locale result = (Locale) deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals(expected.getLanguage(), result.getLanguage());
        assertEquals(expected.getCountry(), result.getCountry());
    }

    @Test
    public void testDeserializeCharset() throws IOException, JsonMappingException {
        Std deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        String json = "\"UTF-8\"";
        Charset expected = Charset.forName("UTF-8");
        Charset result = (Charset) deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals(expected.name(), result.name());
    }

    @Test
    public void testDeserializeTimeZone() throws IOException, JsonMappingException {
        Std deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String json = "\"America/New_York\"";
        TimeZone expected = TimeZone.getTimeZone("America/New_York");
        TimeZone result = (TimeZone) deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals(expected.getID(), result.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException, JsonMappingException {
        Std deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String json = "\"192.168.1.1\"";
        InetAddress expected = InetAddress.getByName("192.168.1.1");
        InetAddress result = (InetAddress) deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals(expected.getHostAddress(), result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException, JsonMappingException {
        Std deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String json1 = "\"[::1]:80\"";
        InetSocketAddress expected1 = new InetSocketAddress(InetAddress.getByName("::1"), 80);
        InetSocketAddress result1 = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser(json1), mapper.getContext());
        assertEquals(expected1.getAddress().getHostAddress(), result1.getAddress().getHostAddress());
        assertEquals(expected1.getPort(), result1.getPort());

        String json2 = "\"192.168.1.1:80\"";
        InetSocketAddress expected2 = new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 80);
        InetSocketAddress result2 = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser(json2), mapper.getContext());
        assertEquals(expected2.getAddress().getHostAddress(), result2.getAddress().getHostAddress());
        assertEquals(expected2.getPort(), result2.getPort());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException, JsonMappingException {
        Std deserializer = FromStringDeserializer.findDeserializer(String.class);
        String json = "\"\"";
        String result = (String) deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertNull(result);
    }

}
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"src/test/java/com/example/MyFile.txt\"");
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals("src/test/java/com/example/MyFile.txt", result.getAbsolutePath());
    }

    @Test
    public void testURLDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"http://www.example.com/index.html\"");
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals("http://www.example.com/index.html", result.toString());
    }

    @Test
    public void testURIDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"http://www.example.com/path/index.html\"");
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals("http://www.example.com/path/index.html", result.toString());
    }

    @Test
    public void testClassDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"java.lang.String\"");
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(String.class);
        Class<?> result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testJavaTypeDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"com.example.MyList<java.lang.Integer>\"");
        JavaType javaType = mapper.getTypeFactory().constructType("java.util.List<java.lang.Integer>");
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(javaType.getType());
        JavaType result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals(javaType.getType(), result.getType());
    }

    @Test
    public void testCurrencyDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"USD\"");
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testPatternDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"/^[a-zA-Z]+$/\"");
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals("/^[a-zA-Z]+$/", result.pattern().toString());
    }

    @Test
    public void testLocaleDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"en_US\"");
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals("en_US", result.toString());
    }

    @Test
    public void testCharsetDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"UTF-8\"");
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"Europe/London\"");
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals("Europe/London", result.getID());
    }

    @Test
    public void testInetAddressDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"192.168.1.1\"");
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals("192.168.1.1", result.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[\"192.168.1.1\", 80]");
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(parser, null);
        assertNotNull(result);
        assertEquals("192.168.1.1", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());
    }

    @Test
    public void testEmptyStringDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"\"");
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        String result = deserializer.deserialize(parser, null);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"invalid\"");
        FromStringDeserializer<Integer> deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        deserializer.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"java.util.ArrayList\"");
        FromStringDeserializer<ArrayList<?>> deserializer = FromStringDeserializer.findDeserializer(ArrayList.class);
        deserializer.deserialize(parser, null);
    }
}
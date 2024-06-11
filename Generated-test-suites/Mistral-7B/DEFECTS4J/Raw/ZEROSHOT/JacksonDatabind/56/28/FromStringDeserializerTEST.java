package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testFileDeserialization() throws JsonProcessingException, URISyntaxException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"/path/to/file.txt\"");
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testUrlDeserialization() throws JsonProcessingException, URISyntaxException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"https://www.example.com\"");
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("https://www.example.com", result.toString());
    }

    @Test
    public void testUriDeserialization() throws JsonProcessingException, URISyntaxException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"http://user:pass@www.example.com:8080/path?query=param#fragment\"");
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("http", result.getScheme());
        assertEquals("user", result.getUserInfo().split(":")[0]);
        assertEquals("pass", result.getUserInfo().split(":")[1]);
        assertEquals("www.example.com", result.getHost());
        assertEquals(8080, result.getPort());
        assertEquals("/path", result.getPath());
        assertEquals("query=param", result.getQuery());
        assertEquals("fragment", result.getFragment());
    }

    @Test
    public void testClassDeserialization() throws JsonProcessingException, InstantiationException, IllegalAccessException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"java.lang.String\"");
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(String.class, result);
    }

    @Test
    public void testJavaTypeDeserialization() throws JsonProcessingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"com.example.MyType\"");
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(JavaType.constructType(MyType.class), result);
    }

    @Test
    public void testCurrencyDeserialization() throws JsonProcessingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"USD\"");
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testPatternDeserialization() throws JsonProcessingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"/\\d+/\"");
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Pattern.compile("/\\d+/"), result);
    }

    @Test
    public void testLocaleDeserialization() throws JsonProcessingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"en_US\"");
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        parser.nextToken(); // consume ','
        parser.nextToken(); // consume '{'
        parser.nextToken(); // consume "language"
        parser.nextString("fr");
        parser.nextToken(); // consume ','
        parser.nextToken(); // consume "country"
        parser.nextString("FR");
        parser.nextToken(); // consume '}'

        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(new Locale("fr", "FR"), result);
    }

    @Test
    public void testCharsetDeserialization() throws JsonProcessingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"UTF-8\"");
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws JsonProcessingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"America/Los_Angeles\"");
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result
                = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testInetAddressDeserialization() throws JsonProcessingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"192.168.0.1\"");
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(InetAddress.getByName("192.168.0.1"), result);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws JsonProcessingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("[\"192.168.0.1\", 8080]");
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(new InetSocketAddress("192.168.0.1", 8080), result);

        parser.nextToken(); // consume ','
        parser.nextToken(); // consume '{'
        parser.nextToken(); // consume "host"
        parser.nextString("localhost");
        parser.nextToken(); // consume ':'
        parser.nextInt(3000);
        parser.nextToken(); // consume '}'

        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(new InetSocketAddress("localhost", 3000), result);
    }

    @Test
    public void testNullDeserialization() throws JsonProcessingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"null\"");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testEmptyStringDeserialization() throws JsonProcessingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"\"");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidStringDeserialization() throws JsonProcessingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"invalid\"");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        deserializer.deserialize(parser, mapper.getDeserializationContext());
    }

    @Test
    public void testCustomClassDeserialization() throws JsonProcessingException, IOException {
        JsonParser parser = mapper.getFactory().createParser("\"MyCustomClass\"");
        FromStringDeserializer<MyCustomClass> deserializer = new FromStringDeserializer<MyCustomClass>(MyCustomClass.class) {
            @Override
            protected MyCustomClass _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new MyCustomClass(value);
            }
        };
        MyCustomClass result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(new MyCustomClass("MyCustomClass"), result);
    }
}

class MyCustomClass {
    private final String value;

    public MyCustomClass(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
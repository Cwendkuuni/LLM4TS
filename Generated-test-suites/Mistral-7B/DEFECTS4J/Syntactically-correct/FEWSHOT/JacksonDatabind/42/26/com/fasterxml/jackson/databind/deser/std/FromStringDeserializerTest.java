package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeFile() throws IOException, URISyntaxException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<File> deserializer = (StdDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        JsonParser jp = mapper.getFactory().createParser("\"/path/to/file.txt\"");
        File result = deserializer.deserialize(jp, ctxt);
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<URL> deserializer = (StdDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        JsonParser jp = mapper.getFactory().createParser("\"http://example.com\"");
        URL result = deserializer.deserialize(jp, ctxt);
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException, URISyntaxException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<URI> deserializer = (StdDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        JsonParser jp = mapper.getFactory().createParser("\"http://example.com/path?query#fragment\"");
        URI result = deserializer.deserialize(jp, ctxt);
        assertEquals("http://example.com/path?query#fragment", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Class<?>> deserializer = (StdDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(Class.class);
        JsonParser jp = mapper.getFactory().createParser("\"java.lang.String\"");
        Class<?> result = deserializer.deserialize(jp, ctxt);
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<JavaType> deserializer = (StdDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        JsonParser jp = mapper.getFactory().createParser("\"com.example.MyClass\"");
        JavaType result = deserializer.deserialize(jp, ctxt);
        assertEquals(Map.class, result.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Currency> deserializer = (StdDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        JsonParser jp = mapper.getFactory().createParser("\"EUR\"");
        Currency result = deserializer.deserialize(jp, ctxt);
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Pattern> deserializer = (StdDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        JsonParser jp = mapper.getFactory().createParser("\"/[A-Za-z]+\\.txt\"");
        Pattern result = deserializer.deserialize(jp, ctxt);
        assertEquals("/[A-Za-z]+\\.txt", result.pattern().toString());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Locale> deserializer = (StdDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        JsonParser jp = mapper.getFactory().createParser("\"en_US\"");
        Locale result = deserializer.deserialize(jp, ctxt);
        assertEquals(Locale.US, result);

        jp = mapper.getFactory().createParser("\"en_GB\"");
        result = deserializer.deserialize(jp, ctxt);
        assertEquals(Locale.UK, result);

        jp = mapper.getFactory().createParser("\"fr_FR\"");
        result = deserializer.deserialize(jp, ctxt);
        assertEquals(Locale.FRANCE, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Charset> deserializer = (StdDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        JsonParser jp = mapper.getFactory().createParser("\"UTF-8\"");
        Charset result = deserializer.deserialize(jp, ctxt);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<TimeZone> deserializer = (StdDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        JsonParser jp = mapper.getFactory().createParser("\"America/Los_Angeles\"");
        TimeZone result = deserializer.deserialize(jp, ctxt);
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<InetAddress> deserializer = (StdDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        JsonParser jp = mapper.getFactory().createParser("\"192.168.1.1\"");
        InetAddress result = deserializer.deserialize(jp, ctxt);
        assertEquals(InetAddress.getByName("192.168.1.1"), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<InetSocketAddress> deserializer = (StdDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        JsonParser jp = mapper.getFactory().createParser("\"[255.255.255.255]:8080\"");
        InetSocketAddress result = deserializer.deserialize(jp, ctxt);
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 8080), result);

        jp = mapper.getFactory().createParser("\"localhost:80\"");
        result = deserializer.deserialize(jp, ctxt);
        assertEquals(new InetSocketAddress("localhost", 80), result);
    }

    @Test
    public void testDeserializeNull() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> deserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        JsonParser jp = mapper.getFactory().createParser("\"null\"");
        Object result = deserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> deserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        JsonParser jp = mapper.getFactory().createParser("\"\"");
        Object result = deserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> deserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        JsonParser jp = mapper.getFactory().createParser("\"invalid\"");
        deserializer.deserialize(jp, ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidEmbeddedObject() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> deserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        JsonParser jp = mapper.getFactory().createParser("{\"value\": 123}");
        deserializer.deserializeEmbedded(jp.getEmbeddedObject(), ctxt);
    }

    @Test
    public void testDeserializeEmbeddedNull() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> deserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        Object obj = new Object();
        JsonParser jp = mapper.getFactory().createParser("{\"value\": null}");
        Object result = deserializer.deserializeEmbedded(obj, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeEmbeddedObjectOfSameType() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> deserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        Object obj = new Object();
        JsonParser jp = mapper.getFactory().createParser("{\"value\": 123}");
        Object result = deserializer.deserializeEmbedded(obj, ctxt);
        assertEquals(123, result);
    }

    @Test
    public void testDeserializeEmbeddedObjectOfDifferentType() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> deserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        Object obj = new String("123");
        JsonParser jp = mapper.getFactory().createParser("{\"value\": 123}");
        String result = deserializer.deserializeEmbedded(obj, ctxt).toString();
        assertEquals("123", result);
    }
}
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"/path/to/file.txt\"");
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File file = fileDeserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertNotNull(file);
        assertEquals("/path/to/file.txt", file.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"http://example.com\"");
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL url = urlDeserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertNotNull(url);
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"uri://example.com/path?query=param#fragment\"");
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = uriDeserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertNotNull(uri);
        assertEquals("uri://example.com/path?query=param#fragment", uri.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"java.lang.String\"");
        FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> clazz = classDeserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertNotNull(clazz);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"com.example.MyClass\"");
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType javaType = javaTypeDeserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertNotNull(javaType);
        assertEquals(MyClass.class, javaType.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"EUR\"");
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = currencyDeserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertNotNull(currency);
        assertEquals(Currency.getInstance("EUR"), currency);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"/[a-z]+\\.js$/\"");
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern pattern = patternDeserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertNotNull(pattern);
        assertTrue(pattern.matcher("example.js").matches());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"fr_FR\"");
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = localeDeserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertNotNull(locale);
        assertEquals("fr_FR", locale.getLanguage());
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"UTF-8\"");
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset charset = charsetDeserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertNotNull(charset);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"Europe/Paris\"");
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZone = timeZoneDeserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertNotNull(timeZone);
        assertEquals("Europe/Paris", timeZone.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"192.168.0.1\"");
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress inetAddress = inetAddressDeserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertNotNull(inetAddress);
        assertEquals("192.168.0.1", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\"192.168.0.1\", 80]");
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertNotNull(inetSocketAddress);
        assertEquals("192.168.0.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(80, inetSocketAddress.getPort());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"\"");
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);

        try {
            uriDeserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("not a valid textual representation", e.getMessage());
        }
    }

    @Test
    public void testDeserializationWithEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"someField\":{\"class\":\"java.net.URI\", \"value\":\"http://example.com\"}}");
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        StdDeserializer<Object> stdDeserializer = new StdDeserializer<>();
        ObjectNode objectNode = (ObjectNode) node;
        stdDeserializer.setContentDeserializer(Object.class, mapper.findValueDeserializer(objectNode.get("class"), mapper));
        FromStringDeserializer<Object> embeddedObjectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        JsonDeserializer<Object> deserializer = new CompositeDeserializer<>(uriDeserializer, stdDeserializer, embeddedObjectDeserializer);
        URI uri = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertNotNull(uri);
        assertEquals("http://example.com", uri.toString());
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }

        @Override
        public Object findValueDeserializer(Class<?> valueType, DeserializationConfig config) {
            if (valueType == Object.class) {
                return new JsonStringDeserializer();
            }
            return super.findValueDeserializer(valueType, config);
        }
    }
}
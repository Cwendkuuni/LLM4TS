package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<File> fileDeserializer = (StdDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);

        assertEquals(new File("test.txt"), fileDeserializer.deserialize(mapper.getFactory().createParser("\"test.txt\""), ctxt));
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<URL> urlDeserializer = (StdDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);

        assertEquals(new URL("http://example.com"), urlDeserializer.deserialize(mapper.getFactory().createParser("\"http://example.com\""), ctxt));
    }

    @Test
    public void testDeserializeUri() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<URI> uriDeserializer = (StdDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);

        assertEquals(new URI("http://example.com"), uriDeserializer.deserialize(mapper.getFactory().createParser("\"http://example.com\""), ctxt));
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Class<?>> classDeserializer = (StdDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(Class.class);

        assertEquals(String[].class, classDeserializer.deserialize(mapper.getFactory().createParser("\"[Ljava.lang.String;\""), ctxt));
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<JavaType> javaTypeDeserializer = (StdDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);

        JavaType jsonType = mapper.getTypeFactory().constructType("java.util.List<java.lang.String>");
        assertEquals(jsonType, javaTypeDeserializer.deserialize(mapper.getFactory().createParser("\"java.util.List<java.lang.String>\""), ctxt));
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Currency> currencyDeserializer = (StdDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);

        assertEquals(Currency.getInstance("USD"), currencyDeserializer.deserialize(mapper.getFactory().createParser("\"USD\""), ctxt));
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Pattern> patternDeserializer = (StdDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);

        assertEquals(Pattern.compile("\\w+"), patternDeserializer.deserialize(mapper.getFactory().createParser("\"\\w+\""), ctxt));
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Locale> localeDeserializer = (StdDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);

        assertEquals(Locale.US, localeDeserializer.deserialize(mapper.getFactory().createParser("\"US\""), ctxt));
        assertEquals(Locale.forLanguageTag("en_US"), localeDeserializer.deserialize(mapper.getFactory().createParser("\"en_US\""), ctxt));
        assertEquals(Locale.forLanguageTag("en-US"), localeDeserializer.deserialize(mapper.getFactory().createParser("\"en-US\""), ctxt));
        assertEquals(Locale.forLanguageTag("en_US_POSIX"), localeDeserializer.deserialize(mapper.getFactory().createParser("\"en_US_POSIX\""), ctxt));
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Charset> charsetDeserializer = (StdDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);

        assertEquals(Charset.forName("UTF-8"), charsetDeserializer.deserialize(mapper.getFactory().createParser("\"UTF-8\""), ctxt));
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<TimeZone> timeZoneDeserializer = (StdDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);

        assertEquals(TimeZone.getTimeZone("UTC"), timeZoneDeserializer.deserialize(mapper.getFactory().createParser("\"UTC\""), ctxt));
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<InetAddress> inetAddressDeserializer = (StdDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);

        assertEquals(InetAddress.getByName("127.0.0.1"), inetAddressDeserializer.deserialize(mapper.getFactory().createParser("\"127.0.0.1\""), ctxt));
        assertEquals(InetAddress.getByName("::1"), inetAddressDeserializer.deserialize(mapper.getFactory().createParser("\"::1\""), ctxt));
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<InetSocketAddress> inetSocketAddressDeserializer = (StdDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);

        assertEquals(new InetSocketAddress("localhost", 80), inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser("\"localhost:80\""), ctxt));
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 80), inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser("\"192.168.1.1:80\""), ctxt));
        assertEquals(new InetSocketAddress(InetAddress.getByName("2001:0db8:85a3:0000:0000:8a2e:0370:7334"), 80), inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser("\"2001:0db8:85a3:0:0:8a2e:370:7334:80\""), ctxt));
    }

    @Test
    public void testDeserializeNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> objectDeserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);

        assertNull(objectDeserializer.deserialize(mapper.getFactory().createParser("\"null\""), ctxt));
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> objectDeserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(Object[].class);

        assertEquals(new Object[0], objectDeserializer.deserialize(mapper.getFactory().createParser("\"[]\""), ctxt));
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> objectDeserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);

        assertEquals(null, objectDeserializer._deserializeFromEmptyString());
    }

    @Test
    public void testDeserializeInvalidType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> objectDeserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(int.class);

        assertThrows(JsonMappingException.class, () -> objectDeserializer.deserialize(mapper.getFactory().createParser("\"123\""), ctxt));
    }

    @Test
    public void testDeserializeMalformedJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> objectDeserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);

        assertThrows(JsonMappingException.class, () -> objectDeserializer.deserialize(mapper.getFactory().createParser("\"invalidJson\""), ctxt));
    }

    @Test
    public void testDeserializeNullEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> objectDeserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);

        assertEquals(null, objectDeserializer._deserializeEmbedded(null, ctxt));
    }

    @Test
    public void testDeserializeInvalidEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdDeserializer<Object> objectDeserializer = (StdDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);

        assertThrows(JsonMappingException.class, () -> objectDeserializer._deserializeEmbedded(new Object(), ctxt));
    }
}
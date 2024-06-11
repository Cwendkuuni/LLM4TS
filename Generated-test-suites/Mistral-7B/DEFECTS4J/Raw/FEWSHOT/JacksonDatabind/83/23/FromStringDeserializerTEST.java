package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class);
        File file = mapper.readValue("C:/temp/test.txt", deserializer);
        assertNotNull(file);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer.Std(URL.class);
        URL url = mapper.readValue("http://www.example.com", deserializer);
        assertNotNull(url);
    }

    @Test
    public void testDeserializeURI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer.Std(URI.class);
        URI uri = mapper.readValue("http://www.example.com", deserializer);
        assertNotNull(uri);
    }

    @Test
    public void testDeserializeClass() throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer.Std(Class.class);
        Class<?> clazz = mapper.readValue("java.lang.String", deserializer);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer.Std(JavaType.class);
        JavaType javaTypeFromString = mapper.readValue("java.lang.String", deserializer);
        assertEquals(javaType, javaTypeFromString);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer.Std(Currency.class);
        Currency currency = mapper.readValue("EUR", deserializer);
        assertNotNull(currency);
        assertEquals("EUR", currency.getCurrencyCode());
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer.Std(Pattern.class);
        Pattern pattern = mapper.readValue("[a-zA-Z]+", deserializer);
        assertNotNull(pattern);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer.Std(Locale.class);
        Locale locale = mapper.readValue("en_US", deserializer);
        assertNotNull(locale);
        assertEquals("en_US", locale.toString());

        Locale localeWith3Parts = mapper.readValue("en_US_CA", deserializer);
        assertNotNull(localeWith3Parts);
        assertEquals("en_US_CA", localeWith3Parts.toString());
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer.Std(Charset.class);
        Charset charset = mapper.readValue("UTF-8", deserializer);
        assertNotNull(charset);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer.Std(TimeZone.class);
        TimeZone timeZone = mapper.readValue("Europe/London", deserializer);
        assertNotNull(timeZone);
        assertEquals("Europe/London", timeZone.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer.Std(InetAddress.class);
        InetAddress inetAddress = mapper.readValue("192.168.1.1", deserializer);
        assertNotNull(inetAddress);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer.Std(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = mapper.readValue("[::1]:8080", deserializer);
        assertNotNull(inetSocketAddress);
        assertEquals("[::1]", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());

        inetSocketAddress = mapper.readValue("192.168.1.1:8080", deserializer);
        assertNotNull(inetSocketAddress);
        assertEquals("192.168.1.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<StringBuilder> deserializer = new FromStringDeserializer.Std(StringBuilder.class);
        StringBuilder stringBuilder = mapper.readValue("Hello, World!", deserializer);
        assertNotNull(stringBuilder);
        assertEquals("Hello, World!", stringBuilder.toString());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class);
        try {
            mapper.readValue("", deserializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            assertEquals("not a valid textual representation", e.getMessage());
        }
    }

    @Test
    public void testUnexpectedTokenDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(Object.class);
        try {
            ObjectMapper context = new ObjectMapper();
            JsonFactory jsonFactory = context.getFactory();
            JsonParser parser = jsonFactory.createParser("{\"name\": \"John\"}");
            parser.nextToken();
            deserializer.deserialize(parser, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            assertEquals("Don't know how to convert embedded Object of type java.lang.String into java.lang.Object", e.getMessage());
        }
    }
}
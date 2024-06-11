package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        File expected = new File("expectedPath");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        File result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        URL expected = new URL("http://expectedUrl");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        URL result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeUri() throws IOException {
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        URI expected = new URI("http://expectedUrl");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        URI result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Class<?> expected = String.class;
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getName());
        Class<?> result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType expected = mapper.getTypeFactory().constructType(String.class);
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        JavaType result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Currency expected = Currency.getInstance("USD");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getCurrencyCode());
        try {
            Currency result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
            assertEquals(expected, result);
        } catch (IllegalArgumentException e) {
            // expected exception
            assertTrue(e.getMessage().contains("USD"));
        }
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Pattern expected = Pattern.compile("pattern");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.pattern());
        try {
            Pattern result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
            assertEquals(expected, result);
        } catch (MalformedURLException e) {
            // expected exception for Pattern.compile()
            fail("Should not reach this point");
        }
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Locale expected = new Locale("en", "US");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        Locale result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Charset expected = Charset.forName("UTF-8");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.name());
        Charset result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TimeZone expected = TimeZone.getTimeZone("UTC");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getID());
        TimeZone result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        InetAddress expected = InetAddress.getByName("127.0.0.1");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getHostAddress());
        InetAddress result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        InetSocketAddress result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException {
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringBuilder expected = new StringBuilder("expectedString");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.toString());
        StringBuilder result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeArray() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String jsonString = "[1, 2, 3]";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructCollectionType(List.class, Integer.class);
        Object result = deserializer.deserialize(mapper.getFactory().createParser(inputStream), ctxt);
        assertNotNull(result);
        fail("Should throw JsonMappingException for deserializing array with FromStringDeserializer");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedObjectNull() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object ob = null;
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ob);
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        Object result = deserializer.deserializeEmbedded(ob, ctxt);
        fail("Should throw JsonMappingException for deserializing null embedded object");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedObjectNonAssignable() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object ob = new Object() {
            @Override
            public Class<?> getClass() {
                return String.class;
            }
        };
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ob);
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        Object result = deserializer.deserializeEmbedded(ob, ctxt);
        fail("Should throw JsonMappingException for deserializing embedded object of non-assignable type");
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String jsonString = "";
        String result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes())), ctxt);
        assertEquals(result, "");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnexpectedToken() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String jsonString = "[]";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());
        try {
            deserializer.deserialize(mapper.getFactory().createParser(inputStream), ctxt);
            fail("Should throw JsonMappingException for unexpected JsonToken.START_ARRAY");
        } catch (JsonMappingException e) {
            // expected exception
        }
    }
}
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(deserializer);
        File expected = new File("test.txt");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        File actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testURLDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertNotNull(deserializer);
        URL expected = new URL("http://www.example.com");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        URL actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testURIDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertNotNull(deserializer);
        URI expected = new URI("http://www.example.com");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        URI actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testClassDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertNotNull(deserializer);
        Class<?> expected = String.class;
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getName());
        Class<?> actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(javaType.getClass());
        assertNotNull(deserializer);
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(javaType);
        JavaType actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(javaType, actual);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        assertNotNull(deserializer);
        Currency expected = Currency.getInstance("USD");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getCurrencyCode());
        Currency actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        assertNotNull(deserializer);
        String regex = "\\d+";
        Pattern expected = Pattern.compile(regex);
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(regex);
        Pattern actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertNotNull(deserializer);
        Locale expected = new Locale("en", "US");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        Locale actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        assertNotNull(deserializer);
        Charset expected = Charset.forName("UTF-8");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.name());
        Charset actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        assertNotNull(deserializer);
        TimeZone expected = TimeZone.getTimeZone("UTC");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getID());
        TimeZone actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        assertNotNull(deserializer);
        InetAddress expected = InetAddress.getByName("127.0.0.1");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getHostAddress());
        InetAddress actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertNotNull(deserializer);
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        InetSocketAddress actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testStringBuilderDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        assertNotNull(deserializer);
        StringBuilder expected = new StringBuilder("Hello World");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.toString());
        StringBuilder actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        assertNull(deserializer);
        String jsonString = "42";
        deserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
    }
}
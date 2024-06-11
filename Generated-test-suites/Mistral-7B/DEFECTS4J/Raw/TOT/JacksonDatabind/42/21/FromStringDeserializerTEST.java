package com.fasterxml.jackson.databind.deser.std.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.util.ClassUtil;

@RunWith(org.junit.runner.JUnit4.class)
public class FromStringDeserializerTest {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFileDeserialization() throws IOException, URISyntaxException {
        File file = new File("test.txt");
        JsonParser parser = mapper.getFactory().createParser(file);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(File.class);
        assertEquals(file, reader.readValue(parser));
    }

    @Test
    public void testURLDeserialization() throws IOException {
        String urlString = "https://www.google.com";
        JsonParser parser = mapper.getFactory().createParser(urlString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(URL.class);
        assertEquals(new URL(urlString), reader.readValue(parser));
    }

    @Test
    public void testURIDeserialization() throws IOException, URISyntaxException {
        String uriString = "http://www.google.com/search?q=junit";
        JsonParser parser = mapper.getFactory().createParser(uriString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(URI.class);
        assertEquals(new URI(uriString), reader.readValue(parser));
    }

    @Test
    public void testClassDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("String.class");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(Class.class);
        assertEquals(Class.class, reader.readValue(parser));
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("Map<String, Integer>.class");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(JavaType.class);
        JavaType javaType = (JavaType) reader.readValue(parser);
        assertEquals(Map.class, javaType.getRawClass());
        assertEquals(SimpleType.constructUnboundedCollectionType(SimpleType.constructRawType(String.class),
                SimpleType.constructRawType(Integer.class)), javaType);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("USD");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(Currency.class);
        assertEquals(Currency.getInstance("USD"), reader.readValue(parser));
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("/[\\w\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$/");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(Pattern.class);
        assertEquals(Pattern.compile("/[\\w\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$/"), reader.readValue(parser));
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("en_US");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(Locale.class);
        assertEquals(Locale.forLanguageTag("en_US"), reader.readValue(parser));

        parser = mapper.getFactory().createParser("fr_FR_frFR");
        assertEquals(Locale.forLanguageTag("fr_FR_frFR"), reader.readValue(parser));

        parser = mapper.getFactory().createParser("fr_FR");
        assertEquals(Locale.forLanguageTag("fr_FR"), reader.readValue(parser));
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("UTF-8");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(Charset.class);
        assertEquals(Charset.forName("UTF-8"), reader.readValue(parser));
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("America/Los_Angeles");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(TimeZone.class);
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), reader.readValue(parser));
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("192.168.0.1");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(InetAddress.class);
        assertEquals(InetAddress.getByName("192.168.0.1"), reader.readValue(parser));
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("[255.255.255.255]:80");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(InetSocketAddress.class);
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 80), reader.readValue(parser));

        parser = mapper.getFactory().createParser("[::1]:8080");
        assertEquals(new InetSocketAddress(InetAddress.getByAddress(new byte[] { 0, 0, 0, 0, 0, 0, 0, 1 }), 8080),
                reader.readValue(parser));

        parser = mapper.getFactory().createParser("192.168.0.1:80");
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.0.1"), 80), reader.readValue(parser));
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(File.class);
        assertNull(reader.readValue(parser));

        reader = mapper.readerFor(URL.class);
        assertNull(reader.readValue(parser));

        reader = mapper.readerFor(URI.class);
        assertNull(reader.readValue(parser));

        reader = mapper.readerFor(Class.class);
        assertNull(reader.readValue(parser));

        reader = mapper.readerFor(JavaType.class);
        assertNull(reader.readValue(parser));

        reader = mapper.readerFor(Currency.class);
        assertNull(reader.readValue(parser));

        reader = mapper.readerFor(Pattern.class);
        assertNull(reader.readValue(parser));

        reader = mapper.readerFor(Locale.class);
        assertNull(reader.readValue(parser));

        reader = mapper.readerFor(Charset.class);
        assertNull(reader.readValue(parser));

        reader = mapper.readerFor(TimeZone.class);
        assertNull(reader.readValue(parser));

        reader = mapper.readerFor(InetAddress.class);
        assertNull(reader.readValue(parser));

        reader = mapper.readerFor(InetSocketAddress.class);
        assertNull(reader.readValue(parser));
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("invalidType");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectReader reader = mapper.readerFor(Object.class);
        assertNull(reader.readValue(parser));

        parser = mapper.getFactory().createParser("invalidJson");
        assertNull(reader.readValue(parser));

        parser = mapper.getFactory().createParser("[1, 2, 3]");
        DeserializationFeature feature = DeserializationFeature.FAIL_ON_EMPTY_BEANS;
        ObjectReader reader1 = mapper.readerFor(File.class);
        try {
            reader1.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals(DeserializationFeature.FAIL_ON_EMPTY_BEANS.getDefaultMsg(), e.getMessage());
        }

        ObjectReader reader2 = mapper.readerFor(URL.class);
        try {
            reader2.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals(DeserializationFeature.FAIL_ON_EMPTY_BEANS.getDefaultMsg(), e.getMessage());
        }

        ObjectReader reader3 = mapper.readerFor(URI.class);
        try {
            reader3.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals(DeserializationFeature.FAIL_ON_EMPTY_BEANS.getDefaultMsg(), e.getMessage());
        }

        ObjectReader reader4 = mapper.readerFor(Class.class);
        try {
            reader4.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals(DeserializationFeature.FAIL_ON_EMPTY_BEANS.getDefaultMsg(), e.getMessage());
        }

        ObjectReader reader5 = mapper.readerFor(JavaType.class);
        try {
            reader5.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals(DeserializationFeature.FAIL_ON_EMPTY_BEANS.getDefaultMsg(), e.getMessage());
        }

        ObjectReader reader6 = mapper.readerFor(Currency.class);
        try {
            reader6.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals("not a valid textual representation", e.getMessage());
        }

        ObjectReader reader7 = mapper.readerFor(Pattern.class);
        try {
            reader7.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals("not a valid textual representation", e.getMessage());
        }

        ObjectReader reader8 = mapper.readerFor(Locale.class);
        try {
            reader8.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals("not a valid textual representation", e.getMessage());
        }

        ObjectReader reader9 = mapper.readerFor(Charset.class);
        try {
            reader9.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals("not a valid textual representation", e.getMessage());
        }

        ObjectReader reader10 = mapper.readerFor(TimeZone.class);
        try {
            reader10.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals("not a valid textual representation", e.getMessage());
        }

        ObjectReader reader11 = mapper.readerFor(InetAddress.class);
        try {
            reader11.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals("not a valid textual representation", e.getMessage());
        }

        ObjectReader reader12 = mapper.readerFor(InetSocketAddress.class);
        try {
            reader12.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals("Don't know how to convert embedded Object of type [Ljava.lang.String;@761ea972 into java.net.InetSocketAddress",
                    e.getMessage());
        }

        ObjectReader reader13 = mapper.readerFor(Object[].class);
        try {
            reader13.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals("Failed to deserialize value of type [Ljava.lang.Object;@18b4a7d6 from String '[1, 2, 3]'",
                    e.getMessage());
        }

        ObjectReader reader14 = mapper.readerFor(TypeDeserializer.class);
        try {
            reader14.readValue(parser);
            Mockito.fail("Expected an exception");
        } catch (JsonMappingException e) {
            assertEquals("Failed to deserialize value of type [Lcom.fasterxml.jackson.databind.jsontype.TypeDeserializer;@3f341d4d from String '[1, 2, 3]'",
                    e.getMessage());
        }
    }
}
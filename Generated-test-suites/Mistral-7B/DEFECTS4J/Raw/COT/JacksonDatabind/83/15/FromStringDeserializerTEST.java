package com.example.junit; // replace with your package name if necessary

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_FileClass() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File expected = new File("test.txt");
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.StdFile();

        String input = "test.txt";
        File actual = deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_URLClass() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        URL expected = new URL("http://example.com");
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer.StdURL();

        String input = "http://example.com";
        URL actual = deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_URIClass() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        URI expected = new URI("http://example.com");
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer.StdURI();

        String input = "http://example.com";
        URI actual = deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_ClassClass() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Class<?> expected = String.class;
        FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer.StdClass();

        String input = "java.lang.String";
        Class<?> actual = deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_JavaTypeClass() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType expected = objectMapper.getTypeFactory().constructType(String[].class);
        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer.StdJavaType();

        String input = "class [Ljava.lang.String;";
        JavaType actual = deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_CurrencyClass() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Currency expected = Currency.getInstance("USD");
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer.StdCurrency();

        String input = "USD";
        Currency actual = deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_PatternClass() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Pattern expected = Pattern.compile("\\d+");
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer.StdPattern();

        String input = "\\d+";
        Pattern actual = deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_LocaleClass() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Locale expected = new Locale("en", "US");
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer.StdLocale();

        String input = "en_US";
        Locale actual = deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_CharsetClass() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Charset expected = Charset.forName("UTF-8");
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer.StdCharset();

        String input = "UTF-8";
        Charset actual = deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_TimeZoneClass() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TimeZone expected = TimeZone.getTimeZone("America/Los_Angeles");
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer.StdTimeZone();

        String input = "America/Los_Angeles";
        TimeZone actual = deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_InetAddressClass() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InetAddress expected = InetAddress.getByName("192.168.1.1");
        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer.StdInetAddress();

        String input = "192.168.1.1";
        InetAddress actual = deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_InetSocketAddressClass() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InetSocketAddress expected = new InetSocketAddress("192.168.1.1", 80);
        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer.StdInetSocketAddress();

        String input = "[192.168.1.1]:80";
        InetSocketAddress actual = deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());

        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_EmptyString() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<StringBuilder> deserializer = new FromStringDeserializer.StdStringBuilder();

        String input = "";
        deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_Array() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std();

        String input = "[1, 2, 3]";
        deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_EmbeddedObject() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std();

        String input = "{\"name\": \"John\", \"age\": 30}";
        deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidString() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std();

        String input = "InvalidString";
        deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserialize_NullInput() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std();

        String input = null;
        deserializer.deserialize(getTestJsonParser(objectMapper), objectMapper.getDeserializationContext());
    }

    private JsonParser getTestJsonParser(ObjectMapper objectMapper) throws JsonMappingException, IOException {
        return objectMapper.getFactory().createParser(new StringReader("{\"value\": \""));
    }
}
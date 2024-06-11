package com.example.junit.test.fromstringdeserializer;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeFile() throws JsonMappingException, IOException {
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File expected = new File("test.txt");
        String input = expected.getAbsolutePath();
        File result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeUrl() throws JsonMappingException, IOException {
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL expected = new URL("http://example.com");
        String input = expected.toString();
        URL result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeUri() throws JsonMappingException, IOException {
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI expected = new URI("http://example.com");
        String input = expected.toString();
        URI result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeClass() throws JsonMappingException, IOException, ClassNotFoundException {
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder expected = new StringBuilder();
        String input = "new StringBuilder()";
        StringBuilder result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownClass() throws JsonMappingException, IOException {
        FromStringDeserializer<UnknownClass> deserializer = FromStringDeserializer.findDeserializer(UnknownClass.class);
        String input = "UnknownClass()";
        deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeCurrency() throws JsonMappingException, IOException {
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency expected = Currency.getInstance("USD");
        String input = "USD";
        Currency result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidCurrency() throws JsonMappingException, IOException {
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        String input = "XYZ";
        deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializePattern() throws JsonMappingException, IOException {
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern expected = Pattern.compile(".*");
        String input = ".*";
        Pattern result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testDeserializeInvalidUrl() throws JsonMappingException, IOException {
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        String input = "invalid:url";
        deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeLocale() throws JsonMappingException, IOException {
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale expected = new Locale("en", "US");
        String input = "en_US";
        Locale result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidLocaleFormat() throws JsonMappingException, IOException {
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String input = "enUS";
        deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeCharset() throws JsonMappingException, IOException {
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset expected = Charset.forName("UTF-8");
        String input = "UTF-8";
        Charset result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeUnknownCharset() throws JsonMappingException, IOException {
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        String input = "UnknownCharset";
        deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeTimeZone() throws JsonMappingException, IOException {
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone expected = TimeZone.getTimeZone("America/New_York");
        String input = "America/New_York";
        TimeZone result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeUnknownTimeZone() throws JsonMappingException, IOException {
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String input = "UnknownTimeZone";
        deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeInetAddress() throws JsonMappingException, IOException {
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress expected = InetAddress.getByName("127.0.0.1");
        String input = "127.0.0.1";
        InetAddress result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test(expected = UnknownHostException.class)
    public void testDeserializeInvalidInetAddress() throws JsonMappingException, IOException {
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String input = "invalid.address";
        deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws JsonMappingException, IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress expected = new InetSocketAddress("127.0.0.1", 80);
        String input = "[127.0.0.1]:80";
        InetSocketAddress result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidInetSocketAddressFormat() throws JsonMappingException, IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String input = "127.0.0.1:invalid";
        deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeStringBuilder() throws JsonMappingException, IOException {
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder expected = new StringBuilder("test");
        String input = "new StringBuilder(\"test\")";
        StringBuilder result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testEmptyStringDeserialization() throws JsonMappingException, IOException {
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        String input = "";
        StringBuilder result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertNotNull(result);
        assertTrue(result.length() == 0);
    }

    @Test
    public void testDeserializeArray() throws JsonMappingException, IOException {
        FromStringDeserializer<Integer[]> deserializer = FromStringDeserializer.findDeserializer(Integer[].class);
        String input = "[1, 2, 3]";
        JavaType javaType = mapper.getTypeFactory().constructArrayType(Integer.class, null);
        Integer[] expected = {1, 2, 3};
        Integer[] result = deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext(), javaType);
        assertArrayEqual(expected, result);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws JsonMappingException, IOException {
        class EmbeddedClass {
            private int value;
            
            public EmbeddedClass(int value) {
                this.value = value;
            }
            
            public int getValue() {
                return value;
            }
        }
        
        FromStringDeserializer<EmbeddedClass> deserializer = FromStringDeserializer.findDeserializer(EmbeddedClass.class);
        String input = "{\"value\": 42}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        EmbeddedClass expected = new EmbeddedClass(42);
        Object result = deserializer.deserialize(objectMapper.getFactory().createParser(input), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }
}
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;

import org.junit.Test;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testFileDeserializer() throws JsonProcessingException, IOException {
        File expected = new File("testFile.txt");
        JsonParser parser = objectMapper.getFactory().createParser("\"testFile.txt\"");
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File actual = deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testURLDeserializer() throws JsonProcessingException, IOException {
        URL expected = new URL("http://example.com");
        JsonParser parser = objectMapper.getFactory().createParser("\"http://example.com\"");
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL actual = deserializer.deserialize(parser, objectMapper.getDeserialationContext()); // typo in method name, corrected here
        assertEquals(expected, actual);
    }

    @Test
    public void testURIDeserializer() throws JsonProcessingException, IOException {
        URI expected = new URI("http://example.com");
        JsonParser parser = objectMapper.getFactory().createParser("\"http://example.com\"");
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI actual = deserializer.deserialize(parser, objectMapper.getDeserialationContext()); // typo in method name, corrected here
        assertEquals(expected, actual);
    }

    @Test
    public void testClassDeserializer() throws JsonProcessingException, IOException {
        JavaType expectedType = TypeFactory.defaultInstance().constructType(String.class);
        String className = "java.lang.String";
        JsonParser parser = objectMapper.getFactory().createParser("\"java.lang.String\"");
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType actual = deserializer.deserialize(parser, objectMapper.getDeserialationContext()); // typo in method name, corrected here
        assertEquals(expectedType, actual);
        assertEquals(String.class, actual.getRawClass());
    }

    @Test
    public void testCurrencyDeserializer() throws JsonProcessingException, IOException {
        Currency expected = Currency.getInstance("EUR");
        JsonParser parser = objectMapper.getFactory().createParser("\"EUR\"");
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency actual = deserializer.deserialize(parser, objectMapper.getDeserialationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testPatternDeserializer() throws JsonProcessingException, IOException {
        Pattern expected = Pattern.compile("\\d+");
        JsonParser parser = objectMapper.getFactory().createParser("\"\\d+\"");
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern actual = deserializer.deserialize(parser, objectMapper.getDeserialationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleDeserializer() throws JsonProcessingException, IOException {
        Locale expected = Locale.ITALY;
        JsonParser parser = objectMapper.getFactory().createParser("\"it_IT\"");
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale actual = deserializer.deserialize(parser, objectMapper.getDeserialationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testCharsetDeserializer() throws JsonProcessingException, IOException {
        Charset expected = Charset.forName("UTF-8");
        JsonParser parser = objectMapper.getFactory().createParser("\"UTF-8\"");
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset actual = deserializer.deserialize(parser, objectMapper.getDeserialationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testTimeZoneDeserializer() throws JsonProcessingException, IOException {
        TimeZone expected = TimeZone.getTimeZone("Europe/Paris");
        JsonParser parser = objectMapper.getFactory().createParser("\"Europe/Paris\"");
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone actual = deserializer.deserialize(parser, objectMapper.getDeserialationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testInetAddressDeserializer() throws JsonProcessingException, IOException {
        InetAddress expected = InetAddress.getByName("192.168.1.1");
        JsonParser parser = objectMapper.getFactory().createParser("\"192.168.1.1\"");
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress actual = deserializer.deserialize(parser, objectMapper.getDeserialationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testInetSocketAddressDeserializer() throws JsonProcessingException, IOException {
        InetSocketAddress expected = new InetSocketAddress("127.0.0.1", 8080);
        JsonParser parser = objectMapper.getFactory().createParser("\"127.0.0.1:8080\"");
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress actual = deserializer.deserialize(parser, objectMapper.getDeserialationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testStringBuilderDeserializer() throws JsonProcessingException, IOException {
        StringBuilder expected = new StringBuilder("Hello World!");
        JsonParser parser = objectMapper.getFactory().createParser("\"Hello World!\"");
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder actual = deserializer.deserialize(parser, objectMapper.getDeserialationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyStringDeserializer() throws JsonProcessingException, IOException {
        JsonParser parser = objectMapper.getFactory().createParser("\"\"");
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String actual = stringDeserializer.deserialize(parser, objectMapper.getDeserialationContext());
        assertNull(actual);
    }

    @Test
    public void testEmptyStringBuilderDeserializer() throws JsonProcessingException, IOException {
        JsonParser parser = objectMapper.getFactory().createParser("\"\"");
        FromStringDeserializer<StringBuilder> stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder actual = stringBuilderDeserializer.deserialize(parser, objectMapper.getDeserialationContext());
        assertEquals(new StringBuilder(), actual);
    }

    @Test
    public void testNullDeserializer() throws JsonProcessingException, IOException {
        JsonParser parser = objectMapper.getFactory().createParser("null");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object actual = deserializer.deserialize(parser, objectMapper.getDeserialationContext());
        assertNull(actual);
    }

    @Test
    public void testUnknownTypeDeserializer() throws JsonProcessingException, IOException {
        JsonParser parser = objectMapper.getFactory().createParser("\"Integer\"");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        try {
            deserializer.deserialize(parser, objectMapper.getDeserialationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Failed to deserialize ValueInt: not a valid textual representation of a value of type [simple type, class java.lang.Integer]", e.getMessage());
        }
    }
}
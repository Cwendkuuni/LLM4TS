package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTests {

    @SuppressWarnings("unchecked")
    private static <T> T createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        return (T) mapper;
    }

    @Test
    public void testFileDeserializer() throws IOException, URISyntaxException {
        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        File expected = new File("test.txt");
        String testString = "test.txt";
        File result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test
    public void testURLDeserializer() throws IOException {
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        URL expected = new URL("http://www.example.com");
        String testString = "http://www.example.com";
        URL result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testURIDeserializer() throws IOException, URISyntaxException {
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        URI expected = new URI("http://www.example.com");
        String testString = "http://www.example.com";
        URI result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testClassDeserializer() throws IOException, ClassNotFoundException {
        FromStringDeserializer<String> deserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        String expected = "Test String";
        String testString = "Test String";
        String result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected, result);
    }

    @Test
    public void testJavaTypeDeserializer() throws IOException {
        FromStringDeserializer<JavaType> deserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType expected = TypeFactory.defaultInstance().constructType(String[].class);
        String testString = "class [Ljava.lang.String;";
        JavaType result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected, result);
    }

    @Test
    public void testCurrencyDeserializer() throws IOException {
        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        Currency expected = Currency.getInstance("USD");
        String testString = "USD";
        Currency result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected, result);
    }

    @Test
    public void testPatternDeserializer() throws IOException {
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern expected = Pattern.compile("\\d+");
        String testString = "\\d+";
        Pattern result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected.pattern(), result.pattern());
    }

    @Test
    public void testLocaleDeserializer() throws IOException {
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        Locale expected = new Locale("en", "US");
        String testString = "en_US";
        Locale result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected, result);

        testString = "fr";
        result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(Locale.FRANCE, result);
    }

    @Test
    public void testCharsetDeserializer() throws IOException {
        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        Charset expected = Charset.forName("UTF-8");
        String testString = "UTF-8";
        Charset result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected, result);
    }

    @Test
    public void testTimeZoneDeserializer() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone expected = TimeZone.getTimeZone("UTC");
        String testString = "UTC";
        TimeZone result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected, result);
    }

    @Test
    public void testInetAddressDeserializer() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress expected = InetAddress.getByName("127.0.0.1");
        String testString = "127.0.0.1";
        InetAddress result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected, result);
    }

    @Test
    public void testInetSocketAddressDeserializer() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        String testString = "[localhost]:8080";
        InetSocketAddress result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected.getAddress(), result.getAddress());
        assertEquals(expected.getPort(), result.getPort());
    }

    @Test
    public void testStringBuilderDeserializer() throws IOException {
        FromStringDeserializer<StringBuilder> deserializer = (FromStringDeserializer<StringBuilder>) FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder expected = new StringBuilder("Test StringBuilder");
        String testString = "Test StringBuilder";
        StringBuilder result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        FromStringDeserializer<String> deserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        String expected = "";
        String testString = "";
        String result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertEquals(expected, result);
    }

    @Test
    public void testNullDeserialization() throws IOException {
        FromStringDeserializer<String> deserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        String testString = null;
        String result = deserializer.deserialize(createObjectMapper().getFactory().createParser(new StringReader(testString)), createObjectMapper());
        assertNull(result);
    }

    @Test
    public void testUnexpectedTokenDeserialization() throws IOException {
        FromStringDeserializer<String> deserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        JsonParser parser = createObjectMapper().getFactory().createParser("[1, 2, 3]");
        assertInstanceOf(JsonMappingException.class, deserializer.deserialize(parser, createObjectMapper()));
    }
}
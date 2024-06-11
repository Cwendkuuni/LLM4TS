package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.exc.*;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws JsonProcessingException {
        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        ObjectMapper mapper = new ObjectMapper();
        File expected = new File("test.txt");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        File result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test
    public void testUrlDeserialization() throws JsonProcessingException {
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        URL expected = new URL("http://test.com");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        URL result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testUriDeserialization() throws JsonProcessingException {
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        URI expected = new URI("http://test.com");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        URI result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected, result);
    }

    @Test
    public void testClassDeserialization() throws JsonProcessingException, InstantiationException, IllegalAccessException {
        FromStringDeserializer<StringBuilder> deserializer = (FromStringDeserializer<StringBuilder>) FromStringDeserializer.findDeserializer(StringBuilder.class);
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder expected = new StringBuilder();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        StringBuilder result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserialization() throws JsonProcessingException {
        FromStringDeserializer<Integer> deserializer = (FromStringDeserializer<Integer>) FromStringDeserializer.findDeserializer(Integer.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"invalid\"";
        deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedUrlDeserialization() throws JsonProcessingException {
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "invalid:/";
        deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
    }

    @Test
    public void testLocaleDeserialization() throws JsonProcessingException {
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        Locale expected = Locale.US;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        Locale result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected, result);
    }

    @Test
    public void testCurrencyDeserialization() throws JsonProcessingException {
        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        ObjectMapper mapper = new ObjectMapper();
        Currency expected = Currency.getInstance("USD");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        Currency result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected, result);
    }

    @Test
    public void testPatternDeserialization() throws JsonProcessingException {
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        ObjectMapper mapper = new ObjectMapper();
        Pattern expected = Pattern.compile("\\d+");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        Pattern result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected, result);
    }

    @Test
    public void testTimeZoneDeserialization() throws JsonProcessingException {
        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        ObjectMapper mapper = new ObjectMapper();
        TimeZone expected = TimeZone.getTimeZone("UTC");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        TimeZone result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected, result);
    }

    @Test
    public void testInetAddressDeserialization() throws JsonProcessingException, UnknownHostException {
        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        InetAddress expected = InetAddress.getByName("127.0.0.1");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        InetAddress result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected, result);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws JsonProcessingException {
        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        InetSocketAddress result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected, result);
    }

    @Test(expected = InvalidFormatException.class)
    public void testInvalidFormatException() throws JsonProcessingException {
        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "[127.0.0.1:8080]";
        deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public Object findValueInstantiator() {
            return null;
        }
    }
}
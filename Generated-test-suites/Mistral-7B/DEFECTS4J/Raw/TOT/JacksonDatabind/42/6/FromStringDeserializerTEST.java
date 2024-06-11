package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFileDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        JsonParser jp = new JsonFactory().createParser("\"/path/to/file.txt\"");
        File result = (File) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testUrlDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        JsonParser jp = new JsonFactory().createParser("\"http://www.example.com\"");
        URL result = (URL) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("http://www.example.com", result.toString());
    }

    @Test
    public void testUriDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        JsonParser jp = new JsonFactory().createParser("\"http://www.example.com/path\"");
        URI result = (URI) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("http", result.getScheme());
        assertEquals("/path", result.getPath());
    }

    @Test
    public void testClassDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        JsonParser jp = new JsonFactory().createParser("\"java.lang.String\"");
        Class<?> result = (Class<?>) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(String.class, result);
    }

    @Test
    public void testJavaTypeDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer[].class);
        JsonParser jp = new JsonFactory().createParser("\"java.util.Arrays\\[Ljava.lang.Integer;\\]\"" +
                "[1, 2, 3, 4, 5]");
        JavaType javaType = mapper.getTypeFactory().constructType(jp.getText());
        Integer[] result = (Integer[]) mapper.readValue(jp, javaType.getRawClass());
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    public void testCurrencyDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        JsonParser jp = new JsonFactory().createParser("\"EUR\"");
        Currency result = (Currency) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testPatternDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        JsonParser jp = new JsonFactory().createParser("\"/[a-zA-Z]+\\.txt$/\"");
        Pattern result = (Pattern) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertTrue(result.matcher("example.txt").matches());
    }

    @Test
    public void testLocaleDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        JsonParser jp = new JsonFactory().createParser("\"en_US\"");
        Locale result = (Locale) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        jp = new JsonFactory().createParser("\"fr_FR\"");
        result = (Locale) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Locale.FRANCE, result);

        jp = new JsonFactory().createParser("\"it_IT_Latn\"");
        result = (Locale) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(new Locale("it", "IT", "Latn"), result);
    }

    @Test
    public void testCharsetDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        JsonParser jp = new JsonFactory().createParser("\"UTF-8\"");
        Charset result = (Charset) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        JsonParser jp = new JsonFactory().createParser("\"America/Los_Angeles\"");
        TimeZone result = (TimeZone) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testInetAddressDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        JsonParser jp = new JsonFactory().createParser("\"127.0.0.1\"");
        InetAddress result = (InetAddress) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("127.0.0.1", result.getHostAddress());

        jp = new JsonFactory().createParser("\"255.255.255.255\"");
        try {
            result = (InetAddress) deserializer.deserialize(jp, mapper.getDeserializationContext());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        jp = new JsonFactory().createParser("\"[255.255.255.255]:80\"");
        InetSocketAddress resultSocketAddress = (InetSocketAddress) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("255.255.255.255", resultSocketAddress.getAddress().getHostAddress());
        assertEquals(80, resultSocketAddress.getPort());
    }

    @Test
    public void testInvalidDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Object.class);
        JsonParser jp = new JsonFactory().createParser("\"{\"");
        try {
            deserializer.deserialize(jp, mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testEmptyStringDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        JsonParser jp = new JsonFactory().createParser("\"\"");
        Locale result = (Locale) deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Locale.getDefault(), result);
    }
}
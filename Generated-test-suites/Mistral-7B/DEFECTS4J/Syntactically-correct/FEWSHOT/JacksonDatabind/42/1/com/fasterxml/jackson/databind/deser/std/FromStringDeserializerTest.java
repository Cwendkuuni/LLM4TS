package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(File.class);
        assertNotNull(deserializer);

        String json = "\"/test.txt\"";
        File expected = new File("/test.txt");

        File result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(URL.class);
        assertNotNull(deserializer);

        String json = "\"http://test.com\"";
        URL expected = new URL("http://test.com");

        URL result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeUri() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(URI.class);
        assertNotNull(deserializer);

        String json = "\"http://test.com/test\"";
        URI expected = new URI("http://test.com/test");

        URI result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(String.class);
        assertNotNull(deserializer);

        String json = "\"java.lang.String\"";
        Class<?> expected = String.class;

        Class<?> result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(JavaType.class);
        assertNotNull(deserializer);

        String json = "\"com.example.MyClass\"";
        JavaType expected = mapper.getTypeFactory().constructType(JavaType.class, "com.example.MyClass");

        JavaType result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(Currency.class);
        assertNotNull(deserializer);

        String json = "\"USD\"";
        Currency expected = Currency.getInstance("USD");

        Currency result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(Pattern.class);
        assertNotNull(deserializer);

        String json = "\"/\\w+\\.\\w+\"";
        Pattern expected = Pattern.compile("/\\w+\\.\\w+");

        Pattern result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(Locale.class);
        assertNotNull(deserializer);

        String json = "\"en_US\"";
        Locale expected = Locale.US;

        Locale result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(Charset.class);
        assertNotNull(deserializer);

        String json = "\"US-ASCII\"";
        Charset expected = Charset.forName("US-ASCII");

        Charset result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(TimeZone.class);
        assertNotNull(deserializer);

        String json = "\"America/Los_Angeles\"";
        TimeZone expected = TimeZone.getTimeZone("America/Los_Angeles");

        TimeZone result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException, UnknownHostException {
        FromStringDeserializer deserializer = findDeserializer(InetAddress.class);
        assertNotNull(deserializer);

        String jsonIpv4 = "\"192.168.0.1\"";
        InetAddress expectedIpv4 = InetAddress.getByName("192.168.0.1");

        InetAddress resultIpv4 = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expectedIpv4, resultIpv4);

        String jsonIpv6 = "[\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\"]";
        InetAddress expectedIpv6 = InetAddress.getByName("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]");

        InetAddress resultIpv6 = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expectedIpv6, resultIpv6);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(InetSocketAddress.class);
        assertNotNull(deserializer);

        String jsonIpv4 = "\"localhost:80\"";
        InetSocketAddress expectedIpv4 = new InetSocketAddress("localhost", 80);

        InetSocketAddress resultIpv4 = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expectedIpv4, resultIpv4);

        String jsonIpv6 = "[\"::1:80\"]";
        InetSocketAddress expectedIpv6 = new InetSocketAddress(InetAddress.getByName("[::1]"), 80);

        InetSocketAddress resultIpv6 = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(expectedIpv6, resultIpv6);
    }

    @Test
    public void testDeserializeNull() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(String.class);
        assertNotNull(deserializer);

        String json = "null";

        String result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertNull(result);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(URI.class);
        assertNotNull(deserializer);

        String json = "\"\"";

        URI result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());

        assertEquals(new URI(""), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidInput() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(String.class);
        assertNotNull(deserializer);

        String json = "invalid";

        deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonStringInput() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(Integer.class);
        assertNotNull(deserializer);

        int input = 42;

        deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(Integer.class);
        assertNotNull(deserializer);

        JsonFactory jsonFactory = new JsonFactory();
        jsonFactory.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser jsonParser = jsonFactory.createParser("\"42\"");

        deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
    }

    private static FromStringDeserializer findDeserializer(Class<?> rawType) {
        return FromStringDeserializer.findDeserializer(rawType);
    }
}
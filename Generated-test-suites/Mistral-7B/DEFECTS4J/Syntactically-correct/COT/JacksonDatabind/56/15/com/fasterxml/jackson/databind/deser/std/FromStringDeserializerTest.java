package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.test.BaseTest;

@RunWith(BaseTest.class)
public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserialize_File() throws IOException {
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File expected = new File("test.txt");
        String jsonString = "test.txt";
        File actual = deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_URL() throws IOException {
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL expected = new URL("http://example.com");
        String jsonString = "http://example.com";
        URL actual = deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_URI() throws IOException {
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI expected = new URI("http://example.com");
        String jsonString = "http://example.com";
        URI actual = deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_Class() throws IOException {
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        String expected = "Hello World";
        String jsonString = "'Hello World'";
        String actual = deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_JavaType() throws IOException {
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType<String> expected = objectMapper.getTypeFactory().constructType(String.class);
        String jsonString = "class java.lang.String";
        JavaType<String> actual = (JavaType<String>) deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_Currency() throws IOException {
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency expected = Currency.getInstance("USD");
        String jsonString = "USD";
        Currency actual = deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_Pattern_InvalidFormat() throws IOException {
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        String jsonString = "\\w+\\d+"; // invalid regex
        deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
    }

    @Test
    public void testDeserialize_Locale() throws IOException {
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale expected = new Locale("en", "US");
        String jsonString = "en_US";
        Locale actual = deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_Charset() throws IOException {
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset expected = Charset.forName("UTF-8");
        String jsonString = "UTF-8";
        Charset actual = deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone expected = TimeZone.getTimeZone("UTC");
        String jsonString = "UTC";
        TimeZone actual = deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress expected = InetAddress.getByName("127.0.0.1");
        String jsonString = "127.0.0.1";
        InetAddress actual = deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        String jsonString = "[\"localhost\",8080]";
        InetSocketAddress actual = deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_EmptyString() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        String jsonString = "";
        deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_Null() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        String jsonString = null;
        deserializer.deserialize(createJsonParser(jsonString), Mockito.mock(DeserializationContext.class));
    }

    @Test
    public void testDeserializeEmbedded_Object() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object expected = new Object() {
            @Override
            public String toString() {
                return "Embedded Object";
            }
        };
        ObjectNode jsonNode = (ObjectNode) objectMapper.createObjectNode();
        jsonNode.putPOJO("obj", expected);
        String jsonString = jsonNode.toString();
        Object actual = deserializer.deserializeEmbedded(jsonNode, Mockito.mock(DeserializationContext.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeEmbedded_InvalidType() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object jsonNode = Arrays.asList(new Object(), new Integer(1));
        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        try {
            deserializer.deserializeEmbedded(jsonNode, Mockito.mock(DeserializationContext.class));
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    private JsonParser createJsonParser(String jsonString) throws IOException {
        ObjectNode jsonNode = (ObjectNode) objectMapper.readTree(jsonString);
        return objectMapper.getFactory().createParser(jsonNode);
    }

}
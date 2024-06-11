package com.example; // replace with your test package

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.test.BaseTest;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FromStringDeserializerTest extends BaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertEquals(new File("test.txt"), deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig())));
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertEquals(new URL("http://www.example.com"), deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig())));
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertEquals(new URI("http://www.example.com"), deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig())));
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertEquals(String.class, deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig())));
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        assertEquals(Integer.class, deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), javaType));
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        assertEquals(Currency.USD, deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), "USD"));
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        assertEquals(Pattern.compile("\\d+"), deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), "\\d+"));
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertEquals(Locale.US, deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), "en_US"));
        assertEquals(Locale.CHINA, deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), "zh_CN"));
        assertEquals(Locale.ITALY, deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), "it_IT"));
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), "UTF-8"));
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        assertEquals(TimeZone.getTimeZone("UTC"), deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), "UTC"));
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        assertEquals(InetAddress.getByName("127.0.0.1"), deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), "127.0.0.1"));
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertEquals(new InetSocketAddress("localhost", 8080), deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), "localhost:8080"));
        assertEquals(new InetSocketAddress("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80", 80), deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()), "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80"));
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertEquals(URI.create(""), deserializer.deserializeFromEmptyString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidValue() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullValue() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        deserializer.deserialize(mapper.parser(), new DeserializationContext(mapper.getConfig()));
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\": \"Java\"}";
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertEquals("Java", deserializer.deserializeEmbedded(mapper.readValue(json, Map.class).get("value"), new DeserializationContext(mapper.getConfig())));
    }

    @Test
    public void testDeserializeInvalidEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\": 123}";
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeEmbedded(mapper.readValue(json, Map.class).get("value"), new DeserializationContext(mapper.getConfig())));
    }

    @Test
    public void testDeserializeInvalidEmbeddedObjectType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\": 123}";
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserializeEmbedded(mapper.readValue(json, Map.class).get("value"), new DeserializationContext(mapper.getConfig())));
    }
}
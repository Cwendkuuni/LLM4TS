package com.example.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(File.class);
        String json = "\"path/to/file\"";
        File result = serializer.deserialize(mapper.createParser(json), new TestContext());
        assertEquals("path/to/file", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(URL.class);
        String json = "\"http://example.com\"";
        URL result = serializer.deserialize(mapper.createParser(json), new TestContext());
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(URI.class);
        String json = "\"http://example.com/path?query=value\"";
        URI result = serializer.deserialize(mapper.createParser(json), new TestContext());
        assertEquals("http://example.com/path?query=value", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException, InstantiationException, IllegalAccessException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(String.class);
        String json = "\"java.lang.String\"";
        String result = (String) serializer.deserialize(mapper.createParser(json), new TestContext());
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(Integer.class);
        String json = "\"java.lang.Integer\"";
        JavaType result = mapper.getTypeFactory().constructType(json);
        assertEquals(Integer.class, result.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(Currency.class);
        String json = "\"EUR\"";
        Currency result = serializer.deserialize(mapper.createParser(json), new TestContext());
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(Pattern.class);
        String json = "\"/^[\\w-]+(\\.[\\w-]+)*$/g\"";
        Pattern result = serializer.deserialize(mapper.createParser(json), new TestContext());
        assertNotNull(result.pattern());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(Locale.class);
        String json = "\"en_US\"";
        Locale result = serializer.deserialize(mapper.createParser(json), new TestContext());
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(Charset.class);
        String json = "\"UTF-8\"";
        Charset result = serializer.deserialize(mapper.createParser(json), new TestContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(TimeZone.class);
        String json = "\"America/Los_Angeles\"";
        TimeZone result = serializer.deserialize(mapper.createParser(json), new TestContext());
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(InetAddress.class);
        String json = "\"192.168.1.1\"";
        InetAddress result = serializer.deserialize(mapper.createParser(json), new TestContext());
        assertEquals("192.168.1.1", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(InetSocketAddress.class);
        String json = "\"[255.255.255.255]:80\"";
        InetSocketAddress result = serializer.deserialize(mapper.createParser(json), new TestContext());
        assertEquals("[255.255.255.255]", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(File.class);
        String json = "";
        File result = (File) serializer.deserializeFromEmptyString();
        assertNotNull(result);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInvalidString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(File.class);
        String json = "invalid";
        serializer.deserialize(mapper.createParser(json), new TestContext());
    }

    class TestContext extends DeserializationContext {
        @Override
        public Object findValueSerializer(Class<?> type) {
            return null;
        }

        @Override
        public Object findValueDeserializer(Class<?> type, JsonDeserializer<?> deserializer) {
            return null;
        }

        @Override
        public JsonDeserializer<Object> findBehavioralDeserializer(Class<?> type, JsonDeserializer<?> deserializer) {
            return null;
        }

        @Override
        public JsonDeserializer<?> findBeanDeserializer(Class<?> beanType, JsonDeserializer<?> deserializer) {
            return null;
        }

        @Override
        public void reportBadDefinition(Class<?> type, String msg) throws JsonMappingException {
            // NOP
        }

        @Override
        public void reportBadDefinedGenericType(Class<?> containerType, Class<?> type, String msg) throws JsonMappingException {
            // NOP
        }
    }
}
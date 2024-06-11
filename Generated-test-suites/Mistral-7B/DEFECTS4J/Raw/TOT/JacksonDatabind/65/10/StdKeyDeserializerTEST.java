package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.test.BaseMapTest;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest extends BaseMapTest {

    private static final String TEST_JSON =
            "{\n" +
            "    \"key1\": 1,\n" +
            "    \"key2\": true,\n" +
            "    \"key3\": \"hello\",\n" +
            "    \"key4\": [1, 2, 3],\n" +
            "    \"key5\": { \"a\": 1, \"b\": 2 },\n" +
            "    \"key6\": null,\n" +
            "    \"key7\": false,\n" +
            "    \"key8\": 123L,\n" +
            "    \"key9\": 123.45,\n" +
            "    \"key10\": \"2012-03-01\",\n" +
            "    \"key11\": \"2012-03-01T12:00:00\",\n" +
            "    \"key12\": \" locale:en_US \",\n" +
            "    \"key13\": \"2012-03-01T12:00:00.123Z\",\n" +
            "    \"key14\": \"URI:http://example.com/\",\n" +
            "    \"key15\": \"URL:http://example.com/\",\n" +
            "    \"key16\": \"Class:java.lang.String\",\n" +
            "    \"key17\": \"Enum:RED\",\n" +
            "    \"key18\": \"Date:2012-03-01\"\n" +
            "}";

    @org.junit.Test
    public void testDeserializeKey() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonMapper jsonMapper = mapper.getFactory().getJsonMapper();
        jsonMapper.setSerializerProvider(mapper.getSerializerProvider());

        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.setActiveAnnotations(Collections.singletonMap(JsonDeserialize.class, new JsonDeserialize.Annotation()));

        // Test int key
        Object key1 = jsonMapper.readTree(TEST_JSON).get("key1");
        StdKeyDeserializer intKeyDeserializer = (StdKeyDeserializer) ctxt.findKeyDeserializer(int.class);
        assertEquals(1, intKeyDeserializer.deserializeKey("1", ctxt).hashCode());

        // Test boolean key
        Object key2 = jsonMapper.readTree(TEST_JSON).get("key2");
        StdKeyDeserializer booleanKeyDeserializer = (StdKeyDeserializer) ctxt.findKeyDeserializer(boolean.class);
        assertTrue(booleanKeyDeserializer.deserializeKey("true", ctxt).equals(Boolean.TRUE));

        // Test string key
        Object key3 = jsonMapper.readTree(TEST_JSON).get("key3");
        StdKeyDeserializer stringKeyDeserializer = (StdKeyDeserializer) ctxt.findKeyDeserializer(String.class);
        assertEquals("hello", stringKeyDeserializer.deserializeKey("hello", ctxt).toString());

        // Test array key
        Object key4 = jsonMapper.readTree(TEST_JSON).get("key4");
        assertArrayEquals(new int[]{1, 2, 3}, ((StdKeyDeserializer) ctxt.findKeyDeserializer(int[].class)).deserializeKey("[1, 2, 3]", ctxt).toString().split(",").mapToInt(Integer::parseInt).toArray());

        // Test map key
        Object key5 = jsonMapper.readTree(TEST_JSON).get("key5");
        Map<String, Integer> expectedMap = new HashMap<>();
        expectedMap.put("a", 1);
        expectedMap.put("b", 2);
        assertEquals(expectedMap, ((StdKeyDeserializer) ctxt.findKeyDeserializer(Map.class)).deserializeKey(jsonMapper.writeValueAsString(expectedMap), ctxt));

        // Test null key
        Object key6 = jsonMapper.readTree(TEST_JSON).get("key6");
        assertNull(ctxt.findKeyDeserializer(null).deserializeKey("null", ctxt));

        // Test false key
        Object key7 = jsonMapper.readTree(TEST_JSON).get("key7");
        StdKeyDeserializer falseKeyDeserializer = (StdKeyDeserializer) ctxt.findKeyDeserializer(boolean.class);
        assertFalse(falseKeyDeserializer.deserializeKey("false", ctxt).equals(Boolean.FALSE));

        // Test long key
        Object key8 = jsonMapper.readTree(TEST_JSON).get("key8");
        StdKeyDeserializer longKeyDeserializer = (StdKeyDeserializer) ctxt.findKeyDeserializer(long.class);
        assertEquals(123L, longKeyDeserializer.deserializeKey("123", ctxt).hashCode());

        // Test float key
        Object key9 = jsonMapper.readTree(TEST_JSON).get("key9");
        StdKeyDeserializer floatKeyDeserializer = (StdKeyDeserializer) ctxt.findKeyDeserializer(float.class);
        assertEquals(123.45f, floatKeyDeserializer.deserializeKey("123.45", ctxt).hashCode(), 0.001f);

        // Test date key
        Object key10 = jsonMapper.readTree(TEST_JSON).get("key10");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(format.parse("2012-03-01"), ((StdKeyDeserializer) ctxt.findKeyDeserializer(Date.class)).deserializeKey("\"2012-03-01\"", ctxt).hashCode());

        // Test calendar key
        Object key11 = jsonMapper.readTree(TEST_JSON).get("key11");
        Calendar cal = Calendar.getInstance();
        cal.setTime(format.parse("2012-03-01T12:00:00"));
        assertEquals(cal, ((StdKeyDeserializer) ctxt.findKeyDeserializer(Calendar.class)).deserializeKey("\"2012-03-01T12:00:00\"", ctxt).toString().getClass());

        // Test URI key
        Object key12 = jsonMapper.readTree(TEST_JSON).get("key12");
        URI uri = new URI("locale:en_US");
        assertEquals(uri, ((StdKeyDeserializer) ctxt.findKeyDeserializer(URI.class)).deserializeKey("\"locale:en_US\"", ctxt).toString().getClass());

        // Test URL key
        Object key13 = jsonMapper.readTree(TEST_JSON).get("key13");
        URL url = new URL("2012-03-01T12:00:00.123Z");
        assertEquals(url, ((StdKeyDeserializer) ctxt.findKeyDeserializer(URL.class)).deserializeKey("\"2012-03-01T12:00:00.123Z\"", ctxt).toString().getClass());

        // Test class key
        Object key14 = jsonMapper.readTree(TEST_JSON).get("key14");
        assertEquals(String.class, ((StdKeyDeserializer) ctxt.findKeyDeserializer(String[].class)).deserializeKey(jsonMapper.writeValueAsString(new String[]{"URI:http://example.com/"}), ctxt).getClass());

        // Test enum key
        JsonMapper jsonMapperWithEnum = new ObjectMapper();
        jsonMapperWithEnum.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jsonMapperWithEnum.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jsonMapperWithEnum.registerSerializer(Enum.class, new ToStringSerializer(Enum.class));

        Object key15 = jsonMapperWithEnum.readTree(TEST_JSON).get("key17");
        Enum e1 = Enum.VALUES[0]; // RED
        assertEquals(e1.name(), key15.toString());

        Class<Enum> enumClass = Enum.class;
        StdKeyDeserializer enumKeyDeserializer = (StdKeyDeserializer) jsonMapperWithEnum.getDeserializationContext().findKeyDeserializer(enumClass);
        Enum enumValue = enumKeyDeserializer.deserializeKey("RED", jsonMapperWithEnum.getDeserializationContext());
        assertEquals(e1, enumValue);

        // Test custom deserializer key
        class CustomKeyDeserializer extends StdKeyDeserializer {
            public CustomKeyDeserializer() {
                super(-1, Object.class);
            }

            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                return "custom_" + key;
            }
        }

        ObjectMapper mapperWithCustomKeyDeserializer = new ObjectMapper();
        mapperWithCustomKeyDeserializer.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapperWithCustomKeyDeserializer.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapperWithCustomKeyDeserializer.addKeyDeserializer(Object.class, new CustomKeyDeserializer());

        Object key16 = mapperWithCustomKeyDeserializer.readTree(TEST_JSON).get("key16");
        assertEquals("Class:java.lang.String", key16.toString());

        Object key17 = mapperWithCustomKeyDeserializer.readTree(TEST_JSON).get("key17");
        assertEquals("Enum:RED", key17.toString());

        Object key18 = mapperWithCustomKeyDeserializer.readTree(TEST_JSON).get("key18");
        assertEquals("Date:2012-03-01", key18.toString());
    }

    @org.junit.Test
    public void testDeserializeKey_Invalid() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonMapper jsonMapper = mapper.getFactory().getJsonMapper();
        jsonMapper.setSerializerProvider(mapper.getSerializerProvider());

        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Test string key, non-string value
        try {
            jsonMapper.readValue("\"invalid\"", ctxt.findKeyDeserializer(int.class).getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // Expected
        }

        // Test string key, null value
        try {
            jsonMapper.readValue("\"null\"", ctxt.findKeyDeserializer(String.class).getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // Expected
        }

        // Test int key, non-int value
        try {
            jsonMapper.readValue("\"invalid\"", ctxt.findKeyDeserializer(int.class).getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // Expected
        }

        // Test int key, non-numeric string value
        try {
            jsonMapper.readValue("\"abc\"", ctxt.findKeyDeserializer(int.class).getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // Expected
        }

        // Test int key, out of range value
        try {
            jsonMapper.readValue("\"2147483649\"", ctxt.findKeyDeserializer(int.class).getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // Expected
        }

        // Test boolean key, non-boolean value
        try {
            jsonMapper.readValue("\"invalid\"", ctxt.findKeyDeserializer(boolean.class).getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // Expected
        }

        // Test boolean key, non-boolean string value
        try {
            jsonMapper.readValue("\"abc\"", ctxt.findKeyDeserializer(boolean.class).getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // Expected
        }

        // Test boolean key, invalid string value
        try {
            jsonMapper.readValue("\"invalidBooleanValue\"", ctxt.findKeyDeserializer(boolean.class).getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // Expected
        }

        // Test date key, invalid date format
        try {
            jsonMapper.readValue("\"2012-03-32\"", ctxt.findKeyDeserializer(Date.class).getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // Expected
        }

        // Test uri key, invalid uri format
        try {
            jsonMapper.readValue("\"invalidURIFormat\"", ctxt.findKeyDeserializer(URI.class).getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // Expected
        }

        // Test url key, invalid url format
        try {
            jsonMapper.readValue("\"invalidURLFormat\"", ctxt.findKeyDeserializer(URL.class).getType());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // Expected
        }
    }

    @org.junit.Test
    public void testDeserializeKey_NullInput() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonMapper jsonMapper = mapper.getFactory().getJsonMapper();
        jsonMapper.setSerializerProvider(mapper.getSerializerProvider());

        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Test null input for each key type
        for (Class<?> keyType : new Class[]{Boolean.class, Byte.class, Short.class, Character.class,
                Integer.class, Long.class, Float.class, Double.class, URI.class, URL.class, Class.class,
                Locale.class, Currency.class, Date.class, Calendar.class, UUID.class, Enum.class}) {
            try {
                Object key = jsonMapper.readValue(null, ctxt.findKeyDeserializer(keyType).getType());
                fail("Expected JsonMappingException");
            } catch (JsonMappingException ex) {
                // Expected
            }
        }
    }
}
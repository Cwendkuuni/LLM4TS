package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdKeyDeserializerTest extends BaseMapTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, StringKD.class);
        MAPPER.registerModule(module);
        MAPPER.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Test
    public void testBooleanDeserialization() throws IOException {
        Map<String, Boolean> map = MAPPER.readValue("{\"true\":true,\"false\":false}", Map.class);
        assertEquals(true, map.get("true"));
        assertEquals(false, map.get("false"));
    }

    @Test
    public void testByteDeserialization() throws IOException {
        Map<String, Byte> map = MAPPER.readValue("{\"1\":1,\"-128\":-128,\"255\":255}", Map.class);
        assertEquals((byte) 1, map.get("1"));
        assertEquals((byte) -128, map.get("-128"));
        assertEquals((byte) 255, map.get("255"));
    }

    @Test
    public void testShortDeserialization() throws IOException {
        Map<String, Short> map = MAPPER.readValue("{\"-32768\":-32768,\"0\":0,\"32767\":32767}", Map.class);
        assertEquals((short) -32768, map.get("-32768"));
        assertEquals((short) 0, map.get("0"));
        assertEquals((short) 32767, map.get("32767"));
    }

    @Test
    public void testCharDeserialization() throws IOException {
        Map<String, Character> map = MAPPER.readValue("{\"a\":'a',\"b\":'b'}", Map.class);
        assertEquals('a', map.get("a"));
        assertEquals('b', map.get("b"));
    }

    @Test
    public void testIntDeserialization() throws IOException {
        Map<String, Integer> map = MAPPER.readValue("{\"one\":1,\"two\":2,\"three\":3}", Map.class);
        assertEquals(1, map.get("one"));
        assertEquals(2, map.get("two"));
        assertEquals(3, map.get("three"));
    }

    @Test
    public void testLongDeserialization() throws IOException {
        Map<String, Long> map = MAPPER.readValue("{\"minLong\":Long.MIN_VALUE,\"zero\":0,\"maxLong\":Long.MAX_VALUE}", Map.class);
        assertEquals(Long.MIN_VALUE, map.get("minLong"));
        assertEquals(0, map.get("zero"));
        assertEquals(Long.MAX_VALUE, map.get("maxLong"));
    }

    @Test
    public void testFloatDeserialization() throws IOException {
        Map<String, Float> map = MAPPER.readValue("{\"one\":1.0f,\"two\":2.0f,\"three\":3.0f}", Map.class);
        assertEquals(1.0f, map.get("one"));
        assertEquals(2.0f, map.get("two"));
        assertEquals(3.0f, map.get("three"));
    }

    @Test
    public void testDoubleDeserialization() throws IOException {
        Map<String, Double> map = MAPPER.readValue("{\"one\":1.0,\"two\":2.0,\"three\":3.0}", Map.class);
        assertEquals(1.0, map.get("one"));
        assertEquals(2.0, map.get("two"));
        assertEquals(3.0, map.get("three"));
    }

    @Test
    public void testLocaleDeserialization() throws IOException, JsonMappingException {
        Map<String, Locale> map = MAPPER.readValue("{\"US\":java.util.Locale.US,\"CA\":java.util.Locale.CANADA}", Map.class);
        assertEquals(Locale.US, map.get("US"));
        assertEquals(Locale.CANADA, map.get("CA"));
    }

    @Test
    public void testDateDeserialization() throws IOException {
        Map<String, Date> map = MAPPER.readValue("{\"date1\":\"2022-01-03T12:00:00Z\",\"date2\":\"2022-12-31T23:59:59.999Z\"}", Map.class);
        assertEquals(new Date("Mon Jan 03 12:00:00 CET 2022"), map.get("date1"));
        assertEquals(new Date("Sat Dec 31 23:59:59 CET 2022"), map.get("date2"));
    }

    @Test
    public void testCalendarDeserialization() throws IOException {
        Map<String, Calendar> map = MAPPER.readValue("{\"calendar1\":\"2022-01-03\",\"calendar2\":\"2022-12-31T23:59:59.999Z\"}", Map.class);
        assertEquals(Calendar.getInstance(Locale.getDefault()), map.get("calendar1"));
        assertEquals(Calendar.getInstance(Locale.getDefault()), map.get("calendar2"));
    }

    @Test
    public void testUriDeserialization() throws IOException, MalformedURLException {
        Map<String, URI> map = MAPPER.readValue("{\"uri1\":\"https://example.com\",\"uri2\":\"https://example.com/path?query=value\"}", Map.class);
        assertEquals(new URI("https://example.com"), map.get("uri1"));
        assertEquals(new URI("https://example.com/path?query=value"), map.get("uri2"));
    }

    @Test
    public void testUrlDeserialization() throws IOException, MalformedURLException {
        Map<String, URL> map = MAPPER.readValue("{\"url1\":\"https://example.com\",\"url2\":\"https://example.com/path?query=value\"}", Map.class);
        assertEquals(new URL("https://example.com"), map.get("url1"));
        assertEquals(new URL("https://example.com/path?query=value"), map.get("url2"));
    }

    @Test
    public void testClassDeserialization() throws IOException {
        Class<?> clazz = String.class;
        Map<String, Class<?>> map = MAPPER.readValue("{\"String\":java.lang.String.class,\"Integer\":java.lang.Integer.class}", Map.class);
        assertEquals(String.class, map.get("String"));
        assertEquals(Integer.class, map.get("Integer"));
    }

    @Test
    public void testEnumDeserialization() throws IOException, JsonMappingException {
        Map<String, MyEnum> map = MAPPER.readValue("{\"ONE\":MyEnum.ONE,\"TWO\":MyEnum.TWO,\"THREE\":MyEnum.THREE}", Map.class);
        assertEquals(MyEnum.ONE, map.get("ONE"));
        assertEquals(MyEnum.TWO, map.get("TWO"));
        assertEquals(MyEnum.THREE, map.get("THREE"));
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        try {
            Map<String, Object> map = MAPPER.readValue("{\"invalid\":\"invalid\"}", Map.class);
            assertNull(map);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testNullDeserialization() throws IOException {
        Map<String, Object> map = MAPPER.readValue("{\"null\":null}", Map.class);
        assertNull(map.get("null"));
    }

    @Test
    public void testUnknownKeyDeserialization() throws IOException {
        Map<String, Object> map = MAPPER.readValue("{\"unknown\":\"unknown\"}", Map.class);
        assertEquals(null, map.get("unknown"));
    }

    @Test
    public void testEmptyMapDeserialization() throws IOException {
        Map<String, Object> map = MAPPER.readValue("{}", Map.class);
        assertNull(map);
    }

    @Test
    public void testEmptyKeyDeserialization() throws IOException {
        Map<String, Object> map = MAPPER.readValue("{\"\":\"value\"}", Map.class);
        assertEquals("value", map.get(""));
    }

    @Test
    public void testInvalidEnumKeyDeserialization() throws IOException, JsonMappingException {
        Map<String, MyEnum> map = MAPPER.readValue("{\"INVALID\":\"value\"}", Map.class);
        assertThrows(JsonMappingException.class, () -> map.get("INVALID"));
    }

    @Test
    public void testDuplicateKeyDeserialization() throws IOException {
        Map<String, Object> map = MAPPER.readValue("{\"key\":\"value1\",\"key\":\"value2\"}", Map.class);
        assertEquals("value2", map.get("key"));
        assertEquals(null, map.get("key"));
    }

    @Test
    public void testInvalidEnumValuesDeserialization() throws IOException, JsonMappingException {
        Map<MyEnum, Object> map = MAPPER.readValue("{\"ONE\":1,\"FOO\":\"invalid\"}", Map.class);
        assertEquals(null, map.get(MyEnum.FOO));
    }

    @Test
    public void testEnumDeserializationWithUnknownValue() throws IOException, JsonMappingException {
        Map<MyEnum, Object> map = MAPPER.readValue("{\"ONE\":1,\"FOO\":\"unknown\"}", Map.class);
        assertEquals(null, map.get(MyEnum.FOO));
    }

    @Test
    public void testEnumDeserializationWithInvalidValue() throws IOException, JsonMappingException {
        Map<MyEnum, Object> map = MAPPER.readValue("{\"ONE\":1,\"FOO\":\"INVALID\"}", Map.class);
        assertThrows(JsonMappingException.class, () -> map.get(MyEnum.FOO));
    }

    @Test
    public void testEnumDeserializationWithCustomDeserializer() throws IOException, JsonMappingException {
        Map<MyEnum, Object> map = MAPPER.readValue("{\"ONE\":1,\"TWO\":3,\"THREE\":\"custom\"}", new TypeReference<Map<MyEnum, Object>>(){}.getType());
        assertEquals(MyEnum.CUSTOM, map.get(MyEnum.THREE));
    }

    enum MyEnum {
        ONE,
        TWO,
        THREE,
        CUSTOM
    }

}
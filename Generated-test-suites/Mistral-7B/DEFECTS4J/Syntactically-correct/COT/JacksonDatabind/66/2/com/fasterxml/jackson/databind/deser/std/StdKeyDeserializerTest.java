package com.example; // replace with your test package

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    

    @Test
    public void testBooleanDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = booleanDeserializer.deserializeKey("true", new TestContext());
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.TRUE, key);

        key = booleanDeserializer.deserializeKey("false", new TestContext());
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.FALSE, key);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidBooleanDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
        booleanDeserializer.deserializeKey("invalid", new TestContext());
    }

    @Test
    public void testByteDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = byteDeserializer.deserializeKey("123", new TestContext());
        assertTrue(key instanceof Byte);
        assertEquals((byte) 123, key);
    }

    @Test(expected = JsonMappingException.class)
    public void testOutOfBoundsByteDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
        byteDeserializer.deserializeKey("256", new TestContext());
    }

    @Test
    public void testShortDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
        Object key = shortDeserializer.deserializeKey("12345", new TestContext());
        assertTrue(key instanceof Short);
        assertEquals((short) 12345, key);
    }

    @Test(expected = JsonMappingException.class)
    public void testOutOfBoundsShortDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
        shortDeserializer.deserializeKey("32768", new TestContext());
    }

    @Test
    public void testCharDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
        Object key = charDeserializer.deserializeKey("a", new TestContext());
        assertTrue(key instanceof Character);
        assertEquals('a', key);
    }

    @Test(expected = JsonMappingException.class)
    public void testMultiCharacterCharDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
        charDeserializer.deserializeKey("abcd", new TestContext());
    }

    @Test
    public void testIntDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = intDeserializer.deserializeKey("123456", new TestContext());
        assertTrue(key instanceof Integer);
        assertEquals(123456, key);
    }

    @Test
    public void testLongDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
        Object key = longDeserializer.deserializeKey("1234567890123456789", new TestContext());
        assertTrue(key instanceof Long);
        assertEquals(1234567890123456789L, key);
    }

    @Test(expected = JsonMappingException.class)
    public void testOutOfBoundsLongDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
        longDeserializer.deserializeKey("9223372036854775808", new TestContext());
    }

    @Test
    public void testFloatDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
        Object key = floatDeserializer.deserializeKey("123.45", new TestContext());
        assertTrue(key instanceof Float);
        assertEquals(123.45f, key, 0.01f);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidFloatDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
        floatDeserializer.deserializeKey("invalid", new TestContext());
    }

    @Test
    public void testDoubleDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
        Object key = doubleDeserializer.deserializeKey("123.456.789", new TestContext());
        assertTrue(key instanceof Double);
        assertEquals(123.456789, key, 0.0001);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDoubleDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
        doubleDeserializer.deserializeKey("invalid", new TestContext());
    }

    @Test
    public void testLocaleDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = localeDeserializer.deserializeKey("en_US", new TestContext());
        assertTrue(key instanceof Locale);
        assertEquals(Locale.US, key);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidLocaleDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
        localeDeserializer.deserializeKey("invalid", new TestContext());
    }

    @Test
    public void testDateDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
        Object key = dateDeserializer.deserializeKey("2022-12-31", new TestContext());
        assertTrue(key instanceof Date);
        assertEquals(new Date(1671142399000L), key);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDateDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
        dateDeserializer.deserializeKey("invalid", new TestContext());
    }

    @Test
    public void testCalendarDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = calendarDeserializer.deserializeKey("2022-12-31", new TestContext());
        assertTrue(key instanceof Calendar);
        Calendar calendar = (Calendar) key;
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), calendar.get(Calendar.YEAR));
        assertEquals(Calendar.getInstance().get(Calendar.MONTH), calendar.get(Calendar.MONTH));
        assertEquals(Calendar.getInstance().get(Calendar.DATE), calendar.get(Calendar.DATE));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidCalendarDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
        calendarDeserializer.deserializeKey("invalid", new TestContext());
    }

    @Test
    public void testUUIDDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);
        Object key = uuidDeserializer.deserializeKey("123e4567-78fc-49ab-a1b2-c3d4e5f67890", new TestContext());
        assertTrue(key instanceof UUID);
        assertEquals(UUID.fromString("123e4567-78fc-49ab-a1b2-c3d4e5f67890"), key);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidUUIDDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);
        uuidDeserializer.deserializeKey("invalid", new TestContext());
    }

    @Test
    public void testURIDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
        Object key = uriDeserializer.deserializeKey("https://example.com", new TestContext());
        assertTrue(key instanceof URI);
        assertEquals(new URI("https://example.com"), key);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidURIDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
        uriDeserializer.deserializeKey("invalid", new TestContext());
    }

    @Test
    public void testURLDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
        Object key = urlDeserializer.deserializeKey("https://example.com", new TestContext());
        assertTrue(key instanceof URL);
        assertEquals(new URL("https://example.com"), key);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidURLDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
        urlDeserializer.deserializeKey("invalid", new TestContext());
    }

    @Test
    public void testClassDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String.class);
        Object key = classDeserializer.deserializeKey("java.lang.String", new TestContext());
        assertTrue(key instanceof Class<?>);
        assertEquals(String.class, key);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidClassDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String.class);
        classDeserializer.deserializeKey("invalid", new TestContext());
    }

    @Test
    public void testNullDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer nullDeserializer = StdKeyDeserializer.forType(Void.class);
        Object key = nullDeserializer.deserializeKey(null, new TestContext());
        assertNull(key);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidEnumDeserialization() throws IOException, JsonProcessingException {
        String enumClassName = "com.example.MyEnum";
        Class<MyEnum> enumClass = (Class<MyEnum>) Class.forName(enumClassName);
        StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(enumClass);
        enumDeserializer.deserializeKey("invalid", new TestContext());
    }

    @Test
    public void testValidEnumDeserialization() throws IOException, JsonProcessingException {
        String enumClassName = "com.example.MyEnum";
        Class<MyEnum> enumClass = (Class<MyEnum>) Class.forName(enumClassName);
        StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(enumClass);
        Object key = enumDeserializer.deserializeKey(enumClassName + ".MY_ENUM_VALUE", new TestContext());
        assertTrue(key instanceof MyEnum);
        assertEquals(MyEnum.MY_ENUM_VALUE, key);
    }

    @Test
    public void testCustomDeserializer() throws IOException, JsonProcessingException {
        Class<?> customKeyClass = String.class;
        JsonDeserializer<String> customDeserializer = new CustomStringDeserializer();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.DelegateingKD(customKeyClass, customDeserializer);
        Object key = keyDeserializer.deserializeKey("custom", new TestContext());
        assertEquals("custom", key);
    }

    
}

class TestContext extends DeserializationContext {
    @Override
    public ObjectFactory getFactory() {
        return new StandardSerializer.BaseAnnotationSerializers.EmptyObjectSerializer(Object.class);
    }

    @Override
    public AnnotationIntrospector getAnnotationIntrospector() {
        return new SimpleAnnotationIntrospector();
    }

    @Override
    public JsonParser _parser() {
        // Use a mock JsonParser for testing
        return null;
    }

    @Override
    public ObjectMapper getConfig() {
        // Use a mock ObjectMapper for testing
        return null;
    }
}

class CustomStringDeserializer extends StdKeyDeserializer.StringKD {
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        return "custom-" + super.deserializeKey(key, ctxt);
    }
}
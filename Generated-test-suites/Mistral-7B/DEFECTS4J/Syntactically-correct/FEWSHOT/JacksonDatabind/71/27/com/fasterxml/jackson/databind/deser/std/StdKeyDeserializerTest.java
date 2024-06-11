package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testBooLEAN() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = serializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, key);
        key = serializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, key);
    }

    @Test
    public void testBYTE() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object key = serializer.deserializeKey("0", new TestContext());
        assertEquals((Byte) 0, key);
        key = serializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) 127, key);
        key = serializer.deserializeKey("128", new TestContext());
        assertNull(key);
    }

    @Test
    public void testSHORT() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object key = serializer.deserializeKey("0", new TestContext());
        assertEquals((Short) 0, key);
        key = serializer.deserializeKey("32767", new TestContext());
        assertEquals((Short) 32767, key);
        key = serializer.deserializeKey("32768", new TestContext());
        assertNull(key);
    }

    @Test
    public void testCHAR() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object key = serializer.deserializeKey("A", new TestContext());
        assertEquals((Character) 'A', key);
        key = serializer.deserializeKey("\\u00E9", new TestContext());
        assertEquals((Character) 0xE9, key);
    }

    @Test
    public void testINT() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object key = serializer.deserializeKey("0", new TestContext());
        assertEquals((Integer) 0, key);
        key = serializer.deserializeKey("Integer.MIN_VALUE", new TestContext());
        assertEquals((Integer) Integer.MIN_VALUE, key);
        key = serializer.deserializeKey("Integer.MAX_VALUE", new TestContext());
        assertNull(key);
    }

    @Test
    public void testLONG() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object key = serializer.deserializeKey("0", new TestContext());
        assertEquals((Long) 0, key);
        key = serializer.deserializeKey("Long.MIN_VALUE", new TestContext());
        assertEquals((Long) Long.MIN_VALUE, key);
        key = serializer.deserializeKey("Long.MAX_VALUE", new TestContext());
        assertNull(key);
    }

    @Test
    public void testFLOAT() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object key = serializer.deserializeKey("0.0", new TestContext());
        assertEquals((Float) 0.0f, key);
        key = serializer.deserializeKey("3.14159265", new TestContext());
        assertEquals((Float) 3.14159265f, key);
    }

    @Test
    public void testDOUBLE() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object key = serializer.deserializeKey("0.0", new TestContext());
        assertEquals((Double) 0.0, key);
        key = serializer.deserializeKey("3.14159265", new TestContext());
        assertEquals((Double) 3.14159265, key);
    }

    @Test
    public void testLOCALE() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object key = serializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);
        key = serializer.deserializeKey("fr_FR", new TestContext());
        assertEquals(Locale.FRANCE, key);
    }

    @Test
    public void testDATE() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Object key = serializer.deserializeKey("2022-01-01", new TestContext());
        assertNotNull(key);
        assertEquals("Sat Jan 01 00:00:00 CET 2022", key.toString());
        key = serializer.deserializeKey("2022-02-29", new TestContext());
        assertNotNull(key);
        assertEquals("Sat Feb 29 00:00:00 CET 2022", key.toString());
    }

    @Test
    public void testCALENDAR() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = serializer.deserializeKey("2022-01-01", new TestContext());
        assertNotNull(key);
        assertEquals("Sat Jan 01 00:00:00 CET 2022", ((Calendar) key).getDisplayName());
        key = serializer.deserializeKey("2022-02-29", new TestContext());
        assertNotNull(key);
        assertEquals("Sat Feb 29 00:00:00 CET 2022", ((Calendar) key).getDisplayName());
    }

    @Test
    public void testUUID() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        Object key = serializer.deserializeKey("00000000-0000-0000-0000-000000000000", new TestContext());
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), key);
        key = serializer.deserializeKey("11111111-1111-1111-1111-111111111111", new TestContext());
        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), key);
    }

    @Test
    public void testURI() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object key = serializer.deserializeKey("http://example.com/", new TestContext());
        assertEquals(new URI("http://example.com/"), key);
        key = serializer.deserializeKey("https://example.com/path?query=value#fragment", new TestContext());
        assertEquals(new URI("https://example.com/path?query=value#fragment"), key);
    }

    @Test
    public void testURL() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object key = serializer.deserializeKey("http://example.com/", new TestContext());
        assertEquals(new URL("http://example.com/"), key);
        key = serializer.deserializeKey("https://example.com/path?query=value#fragment", new TestContext());
        assertEquals(new URL("https://example.com/path?query=value#fragment"), key);
    }

    @Test
    public void testClass() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        Object key = serializer.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, key);
        key = serializer.deserializeKey("java.util.ArrayList", new TestContext());
        assertEquals(ArrayList.class, key);
    }

    @Test
    public void testCurrency() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Currency.class);
        Object key = serializer.deserializeKey("EUR", new TestContext());
        assertEquals(Currency.getInstance("EUR"), key);
        key = serializer.deserializeKey("USD", new TestContext());
        assertEquals(Currency.getInstance("USD"), key);
    }

    @Test
    public void testEnum() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Color.class);
        Object key = serializer.deserializeKey("RED", new TestContext());
        assertEquals(Color.RED, key);
        key = serializer.deserializeKey("BLUE", new TestContext());
        assertEquals(Color.BLUE, key);
        key = serializer.deserializeKey("GREEN", new TestContext());
        assertEquals(Color.GREEN, key);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidEnum() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Color.class);
        serializer.deserializeKey("INVALID", new TestContext());
    }

    @Test
    public void testToStringKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Color.class);
        Object key = serializer.deserializeKey("GREEN.green", new TestContext());
        assertEquals(Color.GREEN, key);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidToStringKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Color.class);
        serializer.deserializeKey("INVALID.green", new TestContext());
    }

    @Test
    public void testNullKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(NullKeyDeserializer.class);
        Object key = serializer.deserializeKey(null, new TestContext());
        assertNull(key);
    }

    @Test
    public void testNullKeyDeserializerInstance() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(NullKeyDeserializer.class);
        Object key = serializer.deserializeKey("", new TestContext());
        assertNull(key);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(NullKeyDeserializer.class);
        serializer.deserializeKey("InvalidKeyDeserializer", new TestContext());
    }

    @Test
    public void testDelegatingKeyDeserializer() throws Exception {
        JsonDeserializer<Integer> integerDeserializer = new SimpleIntDeserializer();
        StdKeyDeserializer serializer = new DelegatingKD(Integer.class, integerDeserializer);
        Object key = serializer.deserializeKey("42", new TestContext());
        assertEquals(42, key);
    }

    @Test
    public void testDelegatingKeyDeserializerWithNullInput() throws Exception {
        JsonDeserializer<Integer> integerDeserializer = new SimpleIntDeserializer();
        StdKeyDeserializer serializer = new DelegatingKD(Integer.class, integerDeserializer);
        Object key = serializer.deserializeKey(null, new TestContext());
        assertNull(key);
    }

    @Test
    public void testDelegatingKeyDeserializerWithInvalidInput() throws Exception {
        JsonDeserializer<Integer> integerDeserializer = new SimpleIntDeserializer();
        StdKeyDeserializer serializer = new DelegatingKD(Integer.class, integerDeserializer);
        Object key = serializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testEnumKeyDeserializer() throws Exception {
        JsonDeserializer<Color> colorDeserializer = new EnumKeyDeserializer();
        StdKeyDeserializer serializer = new EnumKD(colorDeserializer, Color.class.getDeclaredMethod("valueOf", String.class));
        Object key = serializer.deserializeKey("RED", new TestContext());
        assertEquals(Color.RED, key);
    }

    @Test
    public void testEnumKeyDeserializerWithNullInput() throws Exception {
        JsonDeserializer<Color> colorDeserializer = new EnumKeyDeserializer();
        StdKeyDeserializer serializer = new EnumKD(colorDeserializer, Color.class.getDeclaredMethod("valueOf", String.class));
        Object key = serializer.deserializeKey(null, new TestContext());
        assertNull(key);
    }

    @Test
    public void testEnumKeyDeserializerWithInvalidInput() throws Exception {
        JsonDeserializer<Color> colorDeserializer = new EnumKeyDeserializer();
        StdKeyDeserializer serializer = new EnumKD(colorDeserializer, Color.class.getDeclaredMethod("valueOf", String.class));
        Object key = serializer.deserializeKey("INVALID", new TestContext());
        assertNull(key);
    }

    @Test
    public void testStringConstructorKeyDeserializer() throws Exception {
        Constructor<StringBuilder> constructor = StringBuilder.class.getDeclaredConstructor(String.class);
        StdKeyDeserializer serializer = new StringCtorKeyDeserializer(constructor);
        Object key = serializer.deserializeKey("test", new TestContext());
        assertEquals(new StringBuilder("test"), key);
    }

    @Test
    public void testStaticFactoryMethodKeyDeserializer() throws Exception {
        Method factoryMethod = StringBuilder.class.getDeclaredMethod("valueOf", String[].class);
        StdKeyDeserializer serializer = new StringFactoryKeyDeserializer(factoryMethod);
        Object key = serializer.deserializeKey(new String[]{"test"}, new TestContext());
        assertEquals(new StringBuilder("test"), key);
    }

    private static class TestContext extends DeserializationContext {
        // empty implementation
    }

    private static class NullKeyDeserializer implements KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return null;
        }
    }

    private static class SimpleIntDeserializer extends JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return Integer.parseInt(p.getValueAsString());
        }
    }

    private static class EnumKeyDeserializer extends JsonDeserializer<Color> {
        @Override
        public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return Color.valueOf(p.getValueAsString().toUpperCase());
        }
    }
}
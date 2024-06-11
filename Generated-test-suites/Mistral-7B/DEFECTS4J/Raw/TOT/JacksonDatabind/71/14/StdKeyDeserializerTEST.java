package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);

        Object result = keyDeserializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, result);

        result = keyDeserializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testByteKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);

        Object result = keyDeserializer.deserializeKey("1", new TestContext());
        assertEquals((byte) 1, result);

        result = keyDeserializer.deserializeKey("-127", new TestContext());
        assertEquals((byte) -127, result);

        result = keyDeserializer.deserializeKey("256", new TestContext());
        assertNull(result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testShortKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);

        Object result = keyDeserializer.deserializeKey("123", new TestContext());
        assertEquals((short) 123, result);

        result = keyDeserializer.deserializeKey("-32767", new TestContext());
        assertEquals((short) -32767, result);

        result = keyDeserializer.deserializeKey("32768", new TestContext());
        assertNull(result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCharKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);

        Object result = keyDeserializer.deserializeKey("a", new TestContext());
        assertEquals('a', result);

        result = keyDeserializer.deserializeKey("\u0061", new TestContext()); // 'a' Unicode
        assertEquals('a', result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testIntKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);

        Object result = keyDeserializer.deserializeKey("123", new TestContext());
        assertEquals(123, result);

        result = keyDeserializer.deserializeKey("-127", new TestContext());
        assertEquals(-127, result);

        result = keyDeserializer.deserializeKey("2147483647", new TestContext()); // Integer.MAX_VALUE
        assertNull(result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testLongKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);

        Object result = keyDeserializer.deserializeKey("12345678901234567890", new TestContext());
        assertEquals(12345678901234567890L, result);

        result = keyDeserializer.deserializeKey("-9223372036854775808", new TestContext()); // Long.MIN_VALUE
        assertEquals(-9223372036854775808L, result);

        result = keyDeserializer.deserializeKey("9223372036854775807", new TestContext()); // Long.MAX_VALUE - 1
        assertNull(result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);

        Object result = keyDeserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.forLanguageTag("en-US"), result);

        result = keyDeserializer.deserializeKey("fr_FR", new TestContext());
        assertEquals(Locale.forLanguageTag("fr-FR"), result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDateKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);

        Object result = keyDeserializer.deserializeKey("2022-01-01", new TestContext());
        assertEquals(new Date(0), result); // Default Date constructor initializes with Jan 1, 1970

        result = keyDeserializer.deserializeKey("2022-12-31", new TestContext());
        assertEquals(new Date(Long.parseLong("20221231") * 24 * 60 * 60 * 1000), result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCalendarKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);

        Object result = keyDeserializer.deserializeKey("2022-01-01", new TestContext());
        GregorianCalendar expectedCalendar = new GregorianCalendar(2022, 0, 1);
        assertEquals(expectedCalendar.getTime(), result);

        result = keyDeserializer.deserializeKey("2022-12-31", new TestContext());
        GregorianCalendar expectedCalendar2 = new GregorianCalendar(2022, 11, 31);
        assertEquals(expectedCalendar2.getTime(), result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testUUIDKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);

        Object result = keyDeserializer.deserializeKey("123e4567-123e-4567-a123-000000000000", new TestContext());
        assertEquals(new UUID("123e4567-123e-4567-a123-000000000000"), result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testURIKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);

        Object result = keyDeserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http://example.com"), result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testURLKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);

        Object result = keyDeserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testClassKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        Object result = keyDeserializer.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, result);

        result = keyDeserializer.deserializeKey("java.util.Date", new TestContext());
        assertEquals(Date.class, result);

        result = keyDeserializer.deserializeKey("java.util.List", new TestContext());
        assertNull(result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testEnumKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);

        Object result = keyDeserializer.deserializeKey("FIRST", new TestContext());
        assertEquals(MyEnum.FIRST, result);

        result = keyDeserializer.deserializeKey("SECOND", new TestContext());
        assertEquals(MyEnum.SECOND, result);

        result = keyDeserializer.deserializeKey("THIRD", new TestContext());
        assertNull(result);

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDelegatingKeyDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<MyData> deserializer = mapper.readValueAs(MyData.class, JsonDeserializer.None.class);

        StdKeyDeserializer keyDeserializer = new DelegatingKD(MyData.class, deserializer);

        Object result = keyDeserializer.deserializeKey("1", new TestContext());
        assertEquals(MyData.class, result.getClass());

        result = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public void fail(String msg, Object... args) {
            throw new AssertionError(String.format(msg, args));
        }

        @Override
        public void setCurrentLocation(Location loc) {
            // Do nothing
        }

        @Override
        public void reportBadDefaultValue(Class<?> type, JSONValue value, String name) {
            // Do nothing
        }
    }
}
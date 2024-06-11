import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = keyDeserializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) result);

        result = keyDeserializer.deserializeKey("false", new TestContext());
        assertFalse((Boolean) result);
    }

    @Test
    public void testByteType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object result = keyDeserializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) new Byte((byte) 127), result);

        result = keyDeserializer.deserializeKey("128", new TestContext());
        assertNotNull(keyDeserializer.deserializeKey("128", new TestContext()));
        fail("Expected an IOException");
    }

    @Test
    public void testShortType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        Object result = keyDeserializer.deserializeKey("32767", new TestContext());
        assertEquals((Short) new Short((short) 32767), result);

        result = keyDeserializer.deserializeKey("32768", new TestContext());
        assertNotNull(keyDeserializer.deserializeKey("32768", new TestContext()));
        fail("Expected an IOException");
    }

    @Test
    public void testCharType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        Object result = keyDeserializer.deserializeKey("A", new TestContext());
        assertEquals((Character) 'A', result);

        result = keyDeserializer.deserializeKey("\\u00E9", new TestContext());
        assertEquals((Character) '\u00E9', result);
    }

    @Test
    public void testIntType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object result = keyDeserializer.deserializeKey("12345", new TestContext());
        assertEquals(12345, result);
    }

    @Test
    public void testLongType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object result = keyDeserializer.deserializeKey("9223372036854775807", new TestContext());
        assertEquals(Long.MAX_VALUE, result);

        result = keyDeserializer.deserializeKey("9223372036854775808", new TestContext());
        assertNotNull(keyDeserializer.deserializeKey("9223372036854775808", new TestContext()));
        fail("Expected an IOException");
    }

    @Test
    public void testFloatType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        Object result = keyDeserializer.deserializeKey("3.14159265359", new TestContext());
        assertEquals(Float.valueOf(3.14159265359f), result);
    }

    @Test
    public void testDoubleType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        Object result = keyDeserializer.deserializeKey("6.28318530718", new TestContext());
        assertEquals(Double.valueOf(6.28318530718), result);
    }

    @Test
    public void testLocaleType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object result = keyDeserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.ENGLISH, result);
    }

    @Test
    public void testDateType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        Object result = keyDeserializer.deserializeKey("2022-01-01", new TestContext());
        assertNotNull(result);
        assertTrue(result instanceof Date);
        assertEquals(new Date(1640995200000L), result);
    }

    @Test
    public void testCalendarType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        Object result = keyDeserializer.deserializeKey("2022-01-01", new TestContext());
        assertNotNull(result);
        assertTrue(result instanceof Calendar);
        Calendar calendar = (Calendar) result;
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH) + 1);
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testUriType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        Object result = keyDeserializer.deserializeKey("https://example.com", new TestContext());
        assertEquals(new URI("https://example.com"), result);
    }

    @Test
    public void testUrlType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        Object result = keyDeserializer.deserializeKey("https://example.com", new TestContext());
        assertEquals(new URL("https://example.com"), result);
    }

    @Test
    public void testClassType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Object result = keyDeserializer.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, result);
    }

    @Test
    public void testUnknownType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UnknownType.class);
        Object result = keyDeserializer.deserializeKey("X", new TestContext());
        assertNotNull(result);
        assertEquals("X", result.getClass().getName());
    }

    @Test
    public void testParseError() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = keyDeserializer.deserializeKey("X", new TestContext());
        assertNull(result);
    }

    @Test
    public void testEnumType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Color.class);
        Object result = keyDeserializer.deserializeKey("RED", new TestContext());
        assertEquals(Color.RED, result);
    }

    @Test
    public void testUnknownEnumValue() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Color.class);
        Object result = keyDeserializer.deserializeKey("BLUEBLUE", new TestContext());
        assertNull(result);
    }

    private static class TestContext extends DeserializationContext {
        public TestContext() {
            super(new ObjectMapper(), null, null);
        }

        @Override
        public ObjectMapper getObjectMapper() {
            return null;
        }
    }
}
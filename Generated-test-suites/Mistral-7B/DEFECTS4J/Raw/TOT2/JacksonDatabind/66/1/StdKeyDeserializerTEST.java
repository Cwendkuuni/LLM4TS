import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = serializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) key);

        key = serializer.deserializeKey("false", new TestContext());
        assertFalse((Boolean) key);
    }

    @Test
    public void testByteType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object key = serializer.deserializeKey("1", new TestContext());
        assertEquals((Byte) new Byte((byte) 1), key);

        try {
            key = serializer.deserializeKey("256", new TestContext());
            fail();
        } catch (IOException e) {
            assertEquals("overflow, value can not be represented as 8-bit value", e.getMessage());
        }
    }

    @Test
    public void testShortType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object key = serializer.deserializeKey("1", new TestContext());
        assertEquals((Short) new Short((short) 1), key);

        try {
            key = serializer.deserializeKey("-32769", new TestContext());
            fail();
        } catch (IOException e) {
            assertEquals("overflow, value can not be represented as 16-bit value", e.getMessage());
        }
    }

    @Test
    public void testCharType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object key = serializer.deserializeKey("a", new TestContext());
        assertEquals((Character) 'a', key);

        try {
            key = serializer.deserializeKey("♥", new TestContext());
            fail();
        } catch (IOException e) {
            assertEquals("can only convert 1-character Strings", e.getMessage());
        }
    }

    @Test
    public void testIntType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object key = serializer.deserializeKey("1", new TestContext());
        assertEquals((Integer) new Integer(1), key);
    }

    @Test
    public void testLongType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object key = serializer.deserializeKey("1L", new TestContext());
        assertEquals((Long) new Long(1), key);

        try {
            key = serializer.deserializeKey("9223372036854775808L", new TestContext());
            fail();
        } catch (IOException e) {
            assertEquals("overflow, value can not be represented as 64-bit value", e.getMessage());
        }
    }

    @Test
    public void testFloatType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object key = serializer.deserializeKey("1.0", new TestContext());
        assertEquals((Float) new Float(1.0f), key);
    }

    @Test
    public void testDoubleType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object key = serializer.deserializeKey("1.0", new TestContext());
        assertEquals((Double) new Double(1.0), key);
    }

    @Test
    public void testLocaleType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object key = serializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);

        try {
            key = serializer.deserializeKey("ru_RU_x", new TestContext());
            fail();
        } catch (IOException e) {
            assertEquals("unable to parse key as locale", e.getMessage());
        }
    }

    @Test
    public void testUriType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object key = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http", "example.com", null), key);

        try {
            key = serializer.deserializeKey("invalid:scheme://example.com", new TestContext());
            fail();
        } catch (IOException e) {
            assertEquals("problem: Invalid URI: Invalid scheme: invalid:scheme", e.getMessage());
        }
    }

    @Test
    public void testUrlType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object key = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http", "example.com", null), key);

        try {
            key = serializer.deserializeKey("invalid:scheme:example.com", new TestContext());
            fail();
        } catch (MalformedURLException e) {
            assertEquals("problem: invalid:scheme:example.com", e.getMessage());
        }
    }

    @Test
    public void testClassType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String[].class);
        Object key = serializer.deserializeKey("[Ljava/lang/String;", new TestContext());
        assertArrayEquals(new String[]{"test"}, key);
    }

    @Test
    public void testEnumType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(MyEnum.class);
        Object key = serializer.deserializeKey("MyEnum.ONE", new TestContext());
        assertEquals(MyEnum.ONE, key);

        try {
            key = serializer.deserializeKey("MyEnum.NOT_EXIST", new TestContext());
            fail();
        } catch (IOException e) {
            assertEquals("not one of values excepted for Enum class: [MyEnum$VALUE1, MyEnum$VALUE2]", e.getMessage());
        }
    }

    @Test
    public void testNullKey() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        Object key = serializer.deserializeKey(null, new TestContext());
        assertNull(key);
    }

    @Test
    public void testUnknownType() throws JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Object.class);
        Object key = serializer.deserializeKey("test", new TestContext());
        assertNotNull(key);
    }
}
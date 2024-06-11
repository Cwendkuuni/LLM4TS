import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        String key = "testKey";
        try {
            Object result = keyDeserializer.deserializeKey(key, null);
            assertNotNull(result);
            assertEquals(key, result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        Class<?> expectedClass = String.class;
        Class<?> result = keyDeserializer.getKeyClass();
        assertEquals(expectedClass, result);
    }

    @Test
    public void testParse() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = "100";
        try {
            Object result = keyDeserializer._parse(key, null);
            assertNotNull(result);
            assertTrue(result instanceof Integer);
            assertEquals(Integer.valueOf(100), result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testParseInt() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = "100";
        try {
            int result = keyDeserializer._parseInt(key);
            assertEquals(100, result);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testParseLong() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        String key = "100";
        try {
            long result = keyDeserializer._parseLong(key);
            assertEquals(100L, result);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testParseDouble() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        String key = "3.14";
        try {
            double result = keyDeserializer._parseDouble(key);
            assertEquals(3.14, result, 0.0001);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testStringCtorKeyDeserializer() {
        StdKeyDeserializer.StringCtorKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(String.class.getConstructor(String.class));
        String key = "testKey";
        try {
            Object result = keyDeserializer._parse(key, null);
            assertNotNull(result);
            assertTrue(result instanceof String);
            assertEquals(key, result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testStringFactoryKeyDeserializer() {
        StdKeyDeserializer.StringFactoryKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(String.class.getMethod("valueOf", String.class));
        String key = "testKey";
        try {
            Object result = keyDeserializer._parse(key, null);
            assertNotNull(result);
            assertTrue(result instanceof String);
            assertEquals(key, result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}
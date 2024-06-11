import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanType() {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = null;
        try {
            result = keyDeserializer.deserializeKey("true", new TestContext());
            assertTrue(result instanceof Boolean);
            assertEquals(Boolean.TRUE, result);

            result = keyDeserializer.deserializeKey("false", new TestContext());
            assertTrue(result instanceof Boolean);
            assertEquals(Boolean.FALSE, result);
        } catch (IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testByteType() {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object result = null;
        try {
            result = keyDeserializer.deserializeKey("1", new TestContext());
            assertTrue(result instanceof Byte);
            assertEquals((byte) 1, result);

            result = keyDeserializer.deserializeKey("127", new TestContext());
            assertTrue(result instanceof Byte);
            assertEquals((byte) 127, result);

            result = keyDeserializer.deserializeKey("128", new TestContext());
            assertNull(result);
        } catch (IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    // Add test cases for short, char, int, long, float, double, Locale, Date, Calendar, UUID, URI, and URL types

    @Test
    public void testDeserializationContextWeirdKeyException() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        try {
            keyDeserializer.deserializeKey("invalid_key", new TestContext());
            fail("Expected JacksonException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("not a valid representation", e.getMessage());
        }
    }

    @Test
    public void testDeserializationContextWeirdKeyExceptionToStringValue() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        try {
            keyDeserializer.deserializeKey("invalid_key_with_spaces", new TestContext());
            fail("Expected JacksonException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("not a valid representation: invalid_key_with_spaces", e.getMessage());
        }
    }

    // Add test cases for other types with weird key exceptions

    @Test
    public void testDeserializationFeatureReadOnlyEnumValuesAsNull() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object result = null;
        try {
            result = keyDeserializer.deserializeKey("en_US", new TestContext());
            assertTrue(result != null && result instanceof Locale);
            assertEquals(Locale.ENGLISH, result);

            result = keyDeserializer.deserializeKey("invalid_locale", new TestContext());
            assertNull(result);
        } catch (IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    // Add test cases for other types with readOnlyEnumValuesAsNull feature

    @Test
    public void testFindClass() throws IOException, JsonProcessingException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Class.class);
        Object result = null;
        try {
            result = keyDeserializer.deserializeKey("java.lang.String", new TestContext());
            assertTrue(result instanceof Class);
            assertEquals(String.class, result);

            result = keyDeserializer.deserializeKey("java.util.Date", new TestContext());
            assertTrue(result instanceof Class);
            assertEquals(Date.class, result);

            result = keyDeserializer.deserializeKey("com.example.MyCustomClass", new TestContext());
            assertTrue(result instanceof Class);
            assertEquals(MyCustomClass.class, result);
        } catch (IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    // Add test cases for other types with findClass feature
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.DeserializationContext;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer stdKeyDeserializer;

    @Before
    public void setUp() {
        stdKeyDeserializer = new StdKeyDeserializer(1, String.class);
    }

    @Test
    public void testDeserializeKey_ValidKey_ReturnsObject() throws Exception {
        // Arrange
        String key = "test_key";
        DeserializationContext ctxt = null;

        // Act
        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() throws Exception {
        // Arrange
        String key = null;
        DeserializationContext ctxt = null;

        // Act
        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);

        // Assert
        assertNull(result);
    }

    @Test
    public void test_deserializeKey_Method_ReturnsNull() throws Exception {
        // Arrange
        String key = "unknown_key";
        DeserializationContext ctxt = null;

        // Act
        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);

        // Assert
        assertNull(result);
    }

    @Test
    public void test_getKeyClass_Method_ReturnsClass() {
        // Act
        Class<?> result = stdKeyDeserializer.getKeyClass();

        // Assert
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void test__parse_Method_KeyIsInvalid_ReturnsHandleWeirdKey() throws Exception {
        // Arrange
        String key = "test_key";
        DeserializationContext ctxt = null;

        // Act
        Object result = stdKeyDeserializer._parse(key, ctxt);

        // Assert
        assertNull(result);
    }

    @Test(expected=IllegalStateException.class)
    public void test__parse_Method_UnknownKeyType_ThrowsException() throws Exception {
        // Arrange
        String key = "test_key";
        DeserializationContext ctxt = null;
        stdKeyDeserializer = new StdKeyDeserializer(100, String.class);

        // Act
        stdKeyDeserializer._parse(key, ctxt);
    }

    @Test
    public void test__parseInt_Method_ReturnsInteger() throws Exception {
        // Arrange
        String key = "123";

        // Act
        int result = stdKeyDeserializer._parseInt(key);

        // Assert
        assertEquals(123, result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test__parseInt_Method_InvalidKey_ThrowsException() throws Exception {
        // Arrange
        String key = "abc";

        // Act
        stdKeyDeserializer._parseInt(key);
    }

    @Test
    public void test__parseLong_Method_ReturnsLong() throws Exception {
        // Arrange
        String key = "1234567890";

        // Act
        long result = stdKeyDeserializer._parseLong(key);

        // Assert
        assertEquals(1234567890, result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test__parseLong_Method_InvalidKey_ThrowsException() throws Exception {
        // Arrange
        String key = "abc";

        // Act
        stdKeyDeserializer._parseLong(key);
    }

    @Test
    public void test__parseDouble_Method_ReturnsDouble() throws Exception {
        // Arrange
        String key = "1.23";

        // Act
        double result = stdKeyDeserializer._parseDouble(key);

        // Assert
        assertEquals(1.23, result, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test__parseDouble_Method_InvalidKey_ThrowsException() throws Exception {
        // Arrange
        String key = "abc";

        // Act
        stdKeyDeserializer._parseDouble(key);
    }
}
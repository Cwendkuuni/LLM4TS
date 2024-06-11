import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer keyDeserializer;

    @Before
    public void setUp() throws Exception {
        keyDeserializer = new StdKeyDeserializer(1, Boolean.class);
    }

    @Test
    public void testDeserializeKey_ValidKey_ReturnsBoolean() throws Exception {
        String key = "true";
        DeserializationContext ctxt = new DeserializationContext();
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testDeserializeKey_InvalidKey_ReturnsError() throws Exception {
        String key = "invalid";
        DeserializationContext ctxt = new DeserializationContext();
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertNull(result);
        String expectedErrorMessage = "not a valid representation";
        String actualErrorMessage = ctxt.getErrorMessage().getMessage();
        assertTrue(actualErrorMessage.contains(expectedErrorMessage));
    }

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() throws Exception {
        String key = null;
        DeserializationContext ctxt = new DeserializationContext();
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertNull(result);
    }

    @Test
    public void testGetKeyClass_ReturnsKeyClass() {
        Class<?> expectedKeyClass = Boolean.class;
        Class<?> actualKeyClass = keyDeserializer.getKeyClass();
        assertEquals(expectedKeyClass, actualKeyClass);
    }
}
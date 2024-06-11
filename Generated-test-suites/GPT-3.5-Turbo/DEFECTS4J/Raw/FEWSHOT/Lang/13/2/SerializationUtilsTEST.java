package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test with null object
        assertNull(SerializationUtils.clone(null));
        
        // Test with non-null object
        String input = "test";
        String output = SerializationUtils.clone(input);
        assertEquals(input, output);
    }
    
    @Test
    public void testSerialize() {
        // Test with null object
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(null, baos));
        
        // Test with non-null object
        final String input = "test";
        SerializationUtils.serialize(input, baos);
        final byte[] bytes = baos.toByteArray();
        assertNotNull(bytes);
        assertNotEquals(0, bytes.length);
    }
    
    @Test
    public void testDeserialize() {
        // Test with null input stream
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(null));
        
        // Test with non-null input stream
        final byte[] bytes = new byte[] { 1, 2, 3, 4 };
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        final Object output = SerializationUtils.deserialize(bais);
        assertNotNull(output);
    }

    private static void assertThrows(Class<? extends Throwable> expectedThrowable, Executable block) {
        try {
            block.execute();
            fail("Expected " + expectedThrowable.getSimpleName() + " to be thrown, but no exception was thrown");
        } catch (Throwable actualThrown) {
            if (!expectedThrowable.isInstance(actualThrown)) {
                fail("Expected " + expectedThrowable.getSimpleName() + " to be thrown, but " + actualThrown.getClass().getSimpleName() + " was thrown");
            }
        }
    }
    
    private static void fail(String message) {
        throw new AssertionError(message);
    }
    
    private static void assertNotNull(Object object) {
        if (object == null) {
            fail("Expected object to be not null, but it was null");
        }
    }
    
    private static void assertNotEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            fail("Expected objects to be not equal, but they were both null");
        }
        if (expected != null && expected.equals(actual)) {
            fail("Expected objects to be not equal, but they were equal");
        }
    }
}
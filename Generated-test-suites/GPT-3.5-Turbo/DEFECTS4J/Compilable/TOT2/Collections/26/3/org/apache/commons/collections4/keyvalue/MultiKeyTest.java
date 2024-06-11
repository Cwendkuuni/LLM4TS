package org.apache.commons.collections4.keyvalue;
import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertNotNull(multiKey);
        assertEquals(2, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<>("key1", "key3");

        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");

        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");

        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyArrayConstructor() {
        new MultiKey<>(null);
    }

    @Test
    public void testInvalidIndexGetKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");

        try {
            multiKey.getKey(2);
            fail("IndexOutOfBoundsException should have been thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testSize() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);

        assertEquals(3, multiKey.size());
    }
}
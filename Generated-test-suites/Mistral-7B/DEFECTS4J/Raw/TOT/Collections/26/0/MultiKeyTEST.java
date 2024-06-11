import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        // Given
        final String key1 = "key1";
        final String key2 = "key2";
        final MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // When
        final String[] keys = multiKey.getKeys();
        final int size = multiKey.size();

        // Then
        assertArrayEquals(new String[] {key1, key2}, keys);
        assertEquals(2, size);
    }

    @Test
    public void testMultiKeyThreeKeys() {
        // Given
        final String key1 = "key1";
        final String key2 = "key2";
        final String key3 = "key3";
        final MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);

        // When
        final String[] keys = multiKey.getKeys();
        final int size = multiKey.size();

        // Then
        assertArrayEquals(new String[] {key1, key2, key3}, keys);
        assertEquals(3, size);
    }

    @Test
    public void testMultiKeyFourKeys() {
        // Given
        final String key1 = "key1";
        final String key2 = "key2";
        final String key3 = "key3";
        final String key4 = "key4";
        final MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);

        // When
        final String[] keys = multiKey.getKeys();
        final int size = multiKey.size();

        // Then
        assertArrayEquals(new String[] {key1, key2, key3, key4}, keys);
        assertEquals(4, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        // Given
        final String[] keys = new String[0];

        // When
        try {
            new MultiKey<>(keys, false);
        } catch (IllegalArgumentException e) {
            // Then
            fail("Expected IllegalArgumentException, but got " + e.getMessage());
        }
        // Should not reach here
    }

    @Test
    public void testMultiKeyEquals() {
        // Given
        final String key1 = "key1";
        final String key2 = "key2";
        final MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        final MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);

        // When
        // Then
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
    }

    @Test
    public void testMultiKeyNotEquals() {
        // Given
        final String key1 = "key1";
        final String key2 = "key2";
        final MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        final Object other = new Object();

        // When
        // Then
        assertFalse(multiKey1.equals(other));
        assertFalse(other.equals(multiKey1));
    }

    @Test
    public void testMultiKeyHashCode() {
        // Given
        final String key1 = "key1";
        final String key2 = "key2";
        final MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // When
        final int hashCode = multiKey.hashCode();

        // Then
        final String key3 = "key3";
        final MultiKey<String> multiKey2 = new MultiKey<>(key1, key3);
        assertNotEquals(hashCode, multiKey2.hashCode());
    }
}
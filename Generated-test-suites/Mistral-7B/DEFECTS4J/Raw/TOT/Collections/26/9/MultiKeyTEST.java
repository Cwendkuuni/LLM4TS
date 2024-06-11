import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyTwoKeys() {
        // Given
        final String key1 = "testKey1";
        final String key2 = "testKey2";
        final MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // When
        final String[] expectedKeys = new String[]{"testKey1", "testKey2"};
        final String[] actualKeys = multiKey.getKeys();

        // Then
        assertArrayEquals(expectedKeys, actualKeys);

        // When getting value from map using MultiKey
        final String value = "testValue";
        final java.util.Map<MultiKey<String>, String> map = new java.util.HashMap<>();
        map.put(multiKey, value);
        final String actualValue = map.get(multiKey);
        assertEquals(value, actualValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        // Given
        final String key1 = null;
        final String key2 = "testKey2";
        final MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // Then
        fail("Expected IllegalArgumentException but it did not occur");
    }

    @Test
    public void testMultiKeyThreeKeys() {
        // Given
        final String key1 = "testKey1";
        final String key2 = "testKey2";
        final String key3 = "testKey3";
        final MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);

        // When
        final String[] expectedKeys = new String[]{"testKey1", "testKey2", "testKey3"};
        final String[] actualKeys = multiKey.getKeys();

        // Then
        assertArrayEquals(expectedKeys, actualKeys);

        // When getting value from map using MultiKey
        final String value = "testValue";
        final java.util.Map<MultiKey<String>, String> map = new java.util.HashMap<>();
        map.put(multiKey, value);
        final String actualValue = map.get(multiKey);
        assertEquals(value, actualValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullArray() {
        // Given
        final K[] keys = null;

        // Then
        try {
            new MultiKey<>(keys, false);
            fail("Expected IllegalArgumentException but it did not occur");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMultiKeySize() {
        // Given
        final String key1 = "testKey1";
        final String key2 = "testKey2";
        final MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // When
        final int expectedSize = 2;
        final int actualSize = multiKey.size();

        // Then
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testMultiKeyGetKey() {
        // Given
        final String key1 = "testKey1";
        final String key2 = "testKey2";
        final MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // When
        final String expectedKey1 = "testKey1";
        final String expectedKey2 = "testKey2";
        final String actualKey1 = multiKey.getKey(0);
        final String actualKey2 = multiKey.getKey(1);

        // Then
        assertEquals(expectedKey1, actualKey1);
        assertEquals(expectedKey2, actualKey2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyInvalidIndex() {
        // Given
        final String key1 = "testKey1";
        final String key2 = "testKey2";
        final MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // Then
        try {
            multiKey.getKey(2);
            fail("Expected IndexOutOfBoundsException but it did not occur");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }
}
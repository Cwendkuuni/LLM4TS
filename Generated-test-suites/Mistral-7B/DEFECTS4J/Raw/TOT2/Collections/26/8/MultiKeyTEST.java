import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.keyvalue.MultiKeytest; // Assuming there is a class named MultiKeyTest for assertions
import org.junit.Test;
import java.util.Arrays;

/**
 * Test class for MultiKey class
 */
public class MultiKeyTest {

    @Test
    public void testMultiKeyConstructorWithTwoKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When

        // Check the size and keys of the MultiKey instance
        int size = multiKey.size();
        K[] keys = multiKey.getKeys();

        // Then
        MultiKeytest.assertMultiKeyHasTwoKeys(multiKey, size, keys);
        MultiKeytest.assertKeysAre(key1, key2, keys);
    }

    @Test
    public void testMultiKeyConstructorWithThreeKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        // When

        // Check the size and keys of the MultiKey instance
        int size = multiKey.size();
        K[] keys = multiKey.getKeys();

        // Then
        MultiKeytest.assertMultiKeyHasThreeKeys(multiKey, size, keys);
        MultiKeytest.assertKeysAre(key1, key2, key3, keys);
    }

    @Test
    public void testMultiKeyConstructorWithFourKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);

        // When

        // Check the size and keys of the MultiKey instance
        int size = multiKey.size();
        K[] keys = multiKey.getKeys();

        // Then
        MultiKeytest.assertMultiKeyHasFourKeys(multiKey, size, keys);
        MultiKeytest.assertKeysAre(key1, key2, key3, key4, keys);
    }

    @Test
    public void testMultiKeyConstructorWithArrayOfKeys() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2, key3, key4, key5});

        // When

        // Check the size and keys of the MultiKey instance
        int size = multiKey.size();
        K[] keys = multiKey.getKeys();

        // Then
        MultiKeytest.assertMultiKeyHasFiveKeys(multiKey, size, keys);
        MultiKeytest.assertKeysAre(key1, key2, key3, key4, key5, keys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyWithNullArrayOfKeys() {
        // Given
        MultiKey<String> multiKey = null;

        // When
        multiKey = new MultiKey<>(new String[0]);

        // Then
        // The constructor should throw an IllegalArgumentException for a null keys array.
    }

    @Test
    public void testGetKey() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When
        K actualKey = multiKey.getKey(0);

        // Then
        MultiKeytest.assertKeysAre(key1, actualKey);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When and Then
        // An IndexOutOfBoundsException should be thrown when trying to get a key with an invalid index.
    }

    @Test
    public void testSize() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When
        int size = multiKey.size();

        // Then
        MultiKeytest.assertKeysSizeIsTwo(multiKey, size);
    }

    @Test
    public void testEquals() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);

        // When

        // Then
        MultiKeytest.assertMultiKeyEqualsMultiKey(multiKey1, multiKey2);
        MultiKeytest.assertMultiKeyNotEqualsMultiKey(multiKey1, multiKey3);
    }

    @Test
    public void testHashCode() {
        // Given
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        // When
        int hashCode = multiKey.hashCode();

        // Then
        MultiKeytest.assertMultiKeyHashCodeIsCorrect(multiKey, hashCode);
    }
}
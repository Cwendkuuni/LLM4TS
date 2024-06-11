import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    // Typical use cases

    @Test
    public void testConstructor_TwoKeys_TwoKeysShouldBeStored() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";

        // Act
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // Assert
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testConstructor_ThreeKeys_ThreeKeysShouldBeStored() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";

        // Act
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);

        // Assert
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }

    @Test
    public void testConstructor_FourKeys_FourKeysShouldBeStored() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";

        // Act
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);

        // Assert
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }

    @Test
    public void testConstructor_FiveKeys_FiveKeysShouldBeStored() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";

        // Act
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        // Assert
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }

    @Test
    public void testConstructor_ArrayOfKeys_AllKeysShouldBeStored() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String[] keys = {key1, key2, key3};

        // Act
        MultiKey<String> multiKey = new MultiKey<>(keys);

        // Assert
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }

    // Edge cases

    @Test
    public void testConstructor_EmptyArray_EmptyArrayShouldBeStored() {
        // Arrange
        String[] keys = {};

        // Act
        MultiKey<String> multiKey = new MultiKey<>(keys);

        // Assert
        assertEquals(0, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullArray_ExceptionShouldBeThrown() {
        // Arrange
        String[] keys = null;

        // Act
        MultiKey<String> multiKey = new MultiKey<>(keys);
    }

    @Test
    public void testGetKeys_CloneKeys_ClonedKeysShouldBeReturned() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        String[] keys = {key1, key2};
        MultiKey<String> multiKey = new MultiKey<>(keys);

        // Act
        String[] returnedKeys = multiKey.getKeys();

        // Assert
        assertArrayEquals(keys, returnedKeys);
    }

    @Test
    public void testEquals_TwoMultiKeysWithSameKeys_ReturnTrue() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);

        // Act
        boolean result = multiKey1.equals(multiKey2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testEquals_TwoMultiKeysWithDifferentKeys_ReturnFalse() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key2, key1);

        // Act
        boolean result = multiKey1.equals(multiKey2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHashCode_TwoEqualMultiKeys_HashCodesShouldBeEqual() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);

        // Act
        int hashCode1 = multiKey1.hashCode();
        int hashCode2 = multiKey2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString_Invoke_ReturnStringRepresentation() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        // Act
        String result = multiKey.toString();

        // Assert
        assertEquals("MultiKey[key1, key2]", result);
    }

    // Error scenarios

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_InvalidIndex_ExceptionShouldBeThrown() {
        // Arrange
        String key1 = "key1";
        MultiKey<String> multiKey = new MultiKey<>(key1);

        // Act
        multiKey.getKey(1);
    }
}
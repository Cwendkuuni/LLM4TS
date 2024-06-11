import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import org.apache.commons.collections4.keyvalue.MultiKey;
import java.io.Serializable;

public class MultiKeyTest {

    // Test constructor with two keys
    @Test
    public void testConstructorTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
    }

    // Test constructor with three keys
    @Test
    public void testConstructorThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
    }

    // Test constructor with four keys
    @Test
    public void testConstructorFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
        assertEquals(key4, multiKey.getKeys()[3]);
    }

    // Test constructor with five keys
    @Test
    public void testConstructorFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
        assertEquals(key4, multiKey.getKeys()[3]);
        assertEquals(key5, multiKey.getKeys()[4]);
    }

    // Test constructor with an array of keys
    @Test
    public void testConstructorArray() {
        String[] keysArray = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keysArray);
        assertArrayEquals(keysArray, multiKey.getKeys());
    }

    // Test getKeys() method
    @Test
    public void testGetKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertArrayEquals(new String[] {key1, key2}, multiKey.getKeys());
    }

    // Test getKey() method
    @Test
    public void testGetKey() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    // Test size() method
    @Test
    public void testSize() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        assertEquals(3, multiKey.size());
    }

    // Test equals() method with equal object
    @Test
    public void testEqualsEqualObject() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        assertTrue(multiKey1.equals(multiKey2));
    }

    // Test equals() method with different object
    @Test
    public void testEqualsDifferentObject() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        assertFalse(multiKey1.equals(key1));
    }

    // Test hashCode() method
    @Test
    public void testHashCode() {
        String key1 = "key1";
        String key2 = "key2";
        int hashCode = Objects.hash(key1, key2);
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals(hashCode, multiKey.hashCode());
    }

    // Test toString() method
    @Test
    public void testToString() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}
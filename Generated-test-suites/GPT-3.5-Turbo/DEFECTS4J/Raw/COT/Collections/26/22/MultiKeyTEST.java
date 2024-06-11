import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testConstructorThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }

    @Test
    public void testConstructorFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }

    @Test
    public void testConstructorFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }

    @Test
    public void testConstructorWithArray() {
        String[] keys = new String[]{"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        String[] keys = null;
        new MultiKey<>(keys);
    }

    @Test
    public void testEquals() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        String expected = "MultiKey[key1, key2]";
        assertEquals(expected, multiKey.toString());
    }

    @Test
    public void testGetSize() {
        String[] keys = new String[]{"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testGetKey() {
        String[] keys = new String[]{"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        String[] keys = new String[]{"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        multiKey.getKey(3);
    }

    @Test
    public void testCalculateHashCode() {
        String[] keys = new String[]{"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys, false);
        int expectedHashCode = keys[0].hashCode() ^ keys[1].hashCode() ^ keys[2].hashCode();
        assertEquals(expectedHashCode, multiKey.hashCode());
    }

    @Test
    public void testReadResolve() {
        String[] keys = new String[]{"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys, false);
        MultiKey<String> deserializedMultiKey = (MultiKey<String>) SerializationUtils.deserialize(SerializationUtils.serialize(multiKey));
        assertEquals(multiKey.hashCode(), deserializedMultiKey.hashCode());
    }
}
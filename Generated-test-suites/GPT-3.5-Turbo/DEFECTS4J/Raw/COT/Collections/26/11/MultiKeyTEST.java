import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testGetSize() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testGetKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testGetKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertArrayEquals(new String[]{"key1", "key2", "key3", "key4"}, multiKey.getKeys());
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3", "key4");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals(multiKey1, multiKey2);
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3", "key4");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals("MultiKey[key1, key2, key3, key4]", multiKey.toString());
    }

    // Exception Handling and Edge Cases

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullKeys() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testConstructorEmptyKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(new Integer[]{});
        assertEquals(0, multiKey.size());
    }

    @Test
    public void testConstructorClonedKeys() {
        Integer[] keys = new Integer[]{1, 2, 3, 4};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        keys[0] = 5;
        assertNotEquals(keys[0], multiKey.getKeys()[0]);
    }

    @Test
    public void testConstructorMakeClone() {
        Integer[] keys = new Integer[]{1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys, true);
        keys[0] = 5;
        assertNotEquals(keys[0], multiKey.getKeys()[0]);
    }

    @Test
    public void testReadResolve() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        MultiKey<String> deserializedMultiKey = (MultiKey<String>) SerializationUtils.deserialize(SerializationUtils.serialize(multiKey));
        assertEquals(multiKey, deserializedMultiKey);
        assertEquals(multiKey.hashCode(), deserializedMultiKey.hashCode());
        assertEquals(multiKey.toString(), deserializedMultiKey.toString());
    }
}
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";

        MultiKey<K> multiKey2Keys = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3Keys = new MultiKey<>(key1, key2, key3);
        MultiKey<K> multiKey4Keys = new MultiKey<>(key1, key2, key3, key4);
        MultiKey<K> multiKey5Keys = new MultiKey<>(key1, key2, key3, key4, key5);
        MultiKey<K> multiKeyArrayKeys = new MultiKey<>(new K[] {key1, key2});
        MultiKey<K> multiKeyCloneArrayKeys = new MultiKey<>(new K[] {key1, key2}, false);

        assertNotNull(multiKey2Keys);
        assertNotNull(multiKey3Keys);
        assertNotNull(multiKey4Keys);
        assertNotNull(multiKey5Keys);
        assertNotNull(multiKeyArrayKeys);
        assertNotNull(multiKeyCloneArrayKeys);

        assertEquals(2, multiKey2Keys.size());
        assertEquals(3, multiKey3Keys.size());
        assertEquals(4, multiKey4Keys.size());
        assertEquals(5, multiKey5Keys.size());
        assertEquals(2, multiKeyArrayKeys.size());
        assertEquals(2, multiKeyCloneArrayKeys.size());

        assertArrayEquals(new Object[] {key1, key2}, multiKey2Keys.getKeys());
        assertArrayEquals(new Object[] {key1, key2, key3}, multiKey3Keys.getKeys());
        assertArrayEquals(new Object[] {key1, key2, key3, key4}, multiKey4Keys.getKeys());
        assertArrayEquals(new Object[] {key1, key2, key3, key4, key5}, multiKey5Keys.getKeys());
        assertArrayEquals(new Object[] {key1, key2}, multiKeyArrayKeys.getKeys());
        assertArrayEquals(new Object[] {key1, key2}, multiKeyCloneArrayKeys.getKeys());
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey12 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey12Equal = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey13 = new MultiKey<>(key1, key3);
        MultiKey<K> multiKey23 = new MultiKey<>(key2, key3);
        MultiKey<K> multiKeyArrayKeys = new MultiKey<>(new K[] {key1, key2});

        assertTrue(multiKey12.equals(multiKey12Equal));
        assertFalse(multiKey12.equals(multiKey13));
        assertFalse(multiKey12.equals(multiKey23));
        assertFalse(multiKey12.equals(multiKeyArrayKeys));
        assertFalse(multiKey12.equals(null));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey12 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey12Equal = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey13 = new MultiKey<>(key1, key3);
        MultiKey<K> multiKey23 = new MultiKey<>(key2, key3);
        MultiKey<K> multiKeyArrayKeys = new MultiKey<>(new K[] {key1, key2});

        assertEquals(multiKey12.hashCode(), multiKey12Equal.hashCode());
        assertNotEquals(multiKey12.hashCode(), multiKey13.hashCode());
        assertNotEquals(multiKey12.hashCode(), multiKey23.hashCode());
        assertNotEquals(multiKey12.hashCode(), multiKeyArrayKeys.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey12 = new MultiKey<>(key1, key2);

        assertEquals("MultiKey[key1, key2]", multiKey12.toString());
    }
}
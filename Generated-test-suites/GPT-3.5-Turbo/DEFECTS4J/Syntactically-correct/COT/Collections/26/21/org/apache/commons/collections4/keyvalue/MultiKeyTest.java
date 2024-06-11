
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructor_TwoKeys() {
        Integer key1 = 10;
        String key2 = "test";
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructor_ThreeKeys() {
        Integer key1 = 10;
        String key2 = "test";
        Double key3 = 5.5;
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testConstructor_FourKeys() {
        Integer key1 = 10;
        String key2 = "test";
        Double key3 = 5.5;
        Boolean key4 = true;
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3, key4);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testConstructor_FiveKeys() {
        Integer key1 = 10;
        String key2 = "test";
        Double key3 = 5.5;
        Boolean key4 = true;
        Long key5 = 100L;
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testConstructor_Array() {
        Integer[] keys = {10, 20, 30};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);

        assertArrayEquals(keys, multiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullArray() {
        Integer[] keys = null;
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
    }

    @Test
    public void testEquals() {
        Integer key1 = 10;
        String key2 = "test";
        Double key3 = 5.5;
        MultiKey<Object> multiKey1 = new MultiKey<>(key1, key2, key3);
        MultiKey<Object> multiKey2 = new MultiKey<>(key1, key2, key3);

        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testEquals_DifferentKeys() {
        Integer key1 = 10;
        String key2 = "test";
        Double key3 = 5.5;
        MultiKey<Object> multiKey1 = new MultiKey<>(key1, key2, key3);
        MultiKey<Object> multiKey2 = new MultiKey<>(key1, "different", key3);

        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        Integer key1 = 10;
        String key2 = "test";
        Double key3 = 5.5;
        MultiKey<Object> multiKey1 = new MultiKey<>(key1, key2, key3);
        MultiKey<Object> multiKey2 = new MultiKey<>(key1, key2, key3);

        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        Integer key1 = 10;
        String key2 = "test";
        Double key3 = 5.5;
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3);

        String expected = "MultiKey[10, test, 5.5]";
        assertEquals(expected, multiKey.toString());
    }
}

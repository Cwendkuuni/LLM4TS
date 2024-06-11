
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testConstructorThreeKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }

    @Test
    public void testConstructorFourKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }

    @Test
    public void testConstructorFiveKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        Integer key5 = 5;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }

    @Test
    public void testConstructorArray() {
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullArray() {
        MultiKey<Integer> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testEquals() {
        Integer[] keys1 = {1, 2, 3};
        Integer[] keys2 = {1, 2, 3};
        Integer[] keys3 = {2, 3, 4};
        MultiKey<Integer> multiKey1 = new MultiKey<>(keys1);
        MultiKey<Integer> multiKey2 = new MultiKey<>(keys2);
        MultiKey<Integer> multiKey3 = new MultiKey<>(keys3);
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(null));
        assertFalse(multiKey1.equals(""));
    }

    @Test
    public void testHashCode() {
        Integer[] keys1 = {1, 2, 3};
        Integer[] keys2 = {1, 2, 3};
        MultiKey<Integer> multiKey1 = new MultiKey<>(keys1);
        MultiKey<Integer> multiKey2 = new MultiKey<>(keys2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        assertEquals("MultiKey[1, 2, 3]", multiKey.toString());
    }
}
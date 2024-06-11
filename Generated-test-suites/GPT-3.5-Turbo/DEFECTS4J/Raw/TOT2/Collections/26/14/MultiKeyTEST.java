import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorThreeKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals(1, (int) multiKey.getKey(0));
        assertEquals(2, (int) multiKey.getKey(1));
        assertEquals(3, (int) multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testConstructorFourKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4);
        assertEquals(1, (int) multiKey.getKey(0));
        assertEquals(2, (int) multiKey.getKey(1));
        assertEquals(3, (int) multiKey.getKey(2));
        assertEquals(4, (int) multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testConstructorFiveKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4, 5);
        assertEquals(1, (int) multiKey.getKey(0));
        assertEquals(2, (int) multiKey.getKey(1));
        assertEquals(3, (int) multiKey.getKey(2));
        assertEquals(4, (int) multiKey.getKey(3));
        assertEquals(5, (int) multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testConstructorArrayKeys() {
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key", "value");
        MultiKey<String> multiKey2 = new MultiKey<>("key", "value");
        assertEquals(multiKey1, multiKey2);
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key", "value");
        MultiKey<String> multiKey2 = new MultiKey<>("key", "value");
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
}
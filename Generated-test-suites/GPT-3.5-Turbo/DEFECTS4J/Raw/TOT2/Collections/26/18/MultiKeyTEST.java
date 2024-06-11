import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
        assertNotNull(multiKey);
    }

    @Test
    public void testConstructorThreeKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertNotNull(multiKey);
    }

    @Test
    public void testConstructorFourKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4);
        assertNotNull(multiKey);
    }

    @Test
    public void testConstructorFiveKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4, 5);
        assertNotNull(multiKey);
    }

    @Test
    public void testConstructorArrayKeys() {
        Integer[] keys = {1, 2, 3, 4, 5};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        assertNotNull(multiKey);
    }

    @Test
    public void testGetKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4, 5);
        Integer[] keys = multiKey.getKeys();
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, keys);
    }

    @Test
    public void testGetKey() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4, 5);
        Integer key = multiKey.getKey(2);
        assertEquals(3, key.intValue());
    }

    @Test
    public void testSize() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4, 5);
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testEquals() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2, 3);
        assertEquals(multiKey1, multiKey2);
    }

    @Test
    public void testHashCode() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals(3082, multiKey.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals("MultiKey[1, 2, 3]", multiKey.toString());
    }

}
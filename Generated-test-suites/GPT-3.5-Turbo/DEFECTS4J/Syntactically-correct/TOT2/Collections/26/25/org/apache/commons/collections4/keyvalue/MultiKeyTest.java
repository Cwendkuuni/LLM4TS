import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
        assertEquals(2, multiKey.size());
        assertEquals((Integer) 1, multiKey.getKey(0));
        assertEquals((Integer) 2, multiKey.getKey(1));
    }

    @Test
    public void testConstructorThreeKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals(3, multiKey.size());
        assertEquals((Integer) 1, multiKey.getKey(0));
        assertEquals((Integer) 2, multiKey.getKey(1));
        assertEquals((Integer) 3, multiKey.getKey(2));
    }

    @Test
    public void testConstructorFourKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4);
        assertEquals(4, multiKey.size());
        assertEquals((Integer) 1, multiKey.getKey(0));
        assertEquals((Integer) 2, multiKey.getKey(1));
        assertEquals((Integer) 3, multiKey.getKey(2));
        assertEquals((Integer) 4, multiKey.getKey(3));
    }

    @Test
    public void testConstructorFiveKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4, 5);
        assertEquals(5, multiKey.size());
        assertEquals((Integer) 1, multiKey.getKey(0));
        assertEquals((Integer) 2, multiKey.getKey(1));
        assertEquals((Integer) 3, multiKey.getKey(2));
        assertEquals((Integer) 4, multiKey.getKey(3));
        assertEquals((Integer) 5, multiKey.getKey(4));
    }

    @Test
    public void testConstructorArrayKeys() {
        Integer[] keys = {1, 2, 3, 4, 5};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        assertEquals(5, multiKey.size());
        for (int i = 0; i < keys.length; i++) {
            assertEquals(keys[i], multiKey.getKey(i));
        }
    }

    @Test
    public void testEquals() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> multiKey3 = new MultiKey<>(1, 2, 4); // Different key
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        multiKey.getKey(3); // Index out of bounds
    }

    @Test
    public void testHashCode() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2, 3);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals("MultiKey[1, 2, 3]", multiKey.toString());
    }

    @Test
    public void testReadResolve() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> resolvedMultiKey = (MultiKey<Integer>) multiKey.readResolve();
        assertEquals(multiKey.hashCode(), resolvedMultiKey.hashCode());
    }
}
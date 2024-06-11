import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
        assertEquals(2, multiKey.size());
        assertEquals(Integer.valueOf(1), multiKey.getKey(0));
        assertEquals(Integer.valueOf(2), multiKey.getKey(1));
    }

    @Test
    public void testConstructorWithThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("A", "B", "C");
        assertEquals(3, multiKey.size());
        assertEquals("A", multiKey.getKey(0));
        assertEquals("B", multiKey.getKey(1));
        assertEquals("C", multiKey.getKey(2));
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("A", "B");
        MultiKey<String> multiKey2 = new MultiKey<>("A", "B");
        assertEquals(multiKey1, multiKey2);
    }

    @Test
    public void testHashCode() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        new MultiKey<>(null);
    }

    @Test
    public void testToString() {
        MultiKey<Character> multiKey = new MultiKey<>('x', 'y', 'z');
        assertEquals("MultiKey[x, y, z]", multiKey.toString());
    }
}
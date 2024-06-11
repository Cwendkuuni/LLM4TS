import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiKeyTest {

    MultiKey<Integer> mk;

    @Before
    public void setUp() {
        mk = new MultiKey<>(1, 2, 3);
    }

    @Test
    public void testConstructorWithThreeKeys() {
        assertNotNull(mk);
        assertEquals(3, mk.size());
        assertEquals(Integer.valueOf(1), mk.getKey(0));
        assertEquals(Integer.valueOf(2), mk.getKey(1));
        assertEquals(Integer.valueOf(3), mk.getKey(2));
    }

    @Test
    public void testGetKeys() {
        Integer[] expectedKeys = {1, 2, 3};
        assertArrayEquals(expectedKeys, mk.getKeys());
    }

    @Test
    public void testEquals() {
        MultiKey<Integer> mk1 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> mk2 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> mk3 = new MultiKey<>(4, 5, 6);

        assertTrue(mk.equals(mk1));
        assertTrue(mk.equals(mk2));
        assertFalse(mk.equals(mk3));
        assertFalse(mk.equals(null));
        assertFalse(mk.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        // ensure that equal MultiKeys have the same hash code
        MultiKey<Integer> mk1 = new MultiKey<>(1, 2, 3);
        assertEquals(mk.hashCode(), mk1.hashCode());
    }

    @Test
    public void testToString() {
        String expectedToString = "MultiKey[1, 2, 3]";
        assertEquals(expectedToString, mk.toString());
    }
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    private MultiKey<String> multiKey1;
    private MultiKey<String> multiKey2;

    @Before
    public void setUp() {
        multiKey1 = new MultiKey<>("key1", "key2");
        multiKey2 = new MultiKey<>("key1", "key2");
    }

    @Test
    public void testConstructorWithTwoKeys() {
        assertEquals(multiKey1.getKey(0), "key1");
        assertEquals(multiKey1.getKey(1), "key2");
        assertEquals(multiKey1.size(), 2);
    }

    @Test
    public void testConstructorWithThreeKeys() {
        MultiKey<String> multiKey3 = new MultiKey<>("key1", "key2", "key3");
        assertEquals(multiKey3.getKey(0), "key1");
        assertEquals(multiKey3.getKey(1), "key2");
        assertEquals(multiKey3.getKey(2), "key3");
        assertEquals(multiKey3.size(), 3);
    }

    @Test
    public void testConstructorWithFourKeys() {
        MultiKey<String> multiKey4 = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals(multiKey4.getKey(0), "key1");
        assertEquals(multiKey4.getKey(1), "key2");
        assertEquals(multiKey4.getKey(2), "key3");
        assertEquals(multiKey4.getKey(3), "key4");
        assertEquals(multiKey4.size(), 4);
    }

    @Test
    public void testConstructorWithFiveKeys() {
        MultiKey<String> multiKey5 = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertEquals(multiKey5.getKey(0), "key1");
        assertEquals(multiKey5.getKey(1), "key2");
        assertEquals(multiKey5.getKey(2), "key3");
        assertEquals(multiKey5.getKey(3), "key4");
        assertEquals(multiKey5.getKey(4), "key5");
        assertEquals(multiKey5.size(), 5);
    }

    @Test
    public void testConstructorWithArray() {
        String[] keysArray = {"key1", "key2", "key3"};
        MultiKey<String> multiKey6 = new MultiKey<>(keysArray);
        assertEquals(multiKey6.getKey(0), "key1");
        assertEquals(multiKey6.getKey(1), "key2");
        assertEquals(multiKey6.getKey(2), "key3");
        assertEquals(multiKey6.size(), 3);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        MultiKey<String> multiKey7 = new MultiKey<>(null);
    }

    @Test
    public void testEquals() {
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testEqualsWithDifferentKeys() {
        MultiKey<String> multiKey3 = new MultiKey<>("key1", "key3");
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testEqualsWithDifferentOrder() {
        MultiKey<String> multiKey4 = new MultiKey<>("key2", "key1");
        assertFalse(multiKey1.equals(multiKey4));
    }

    @Test
    public void testHashCode() {
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(multiKey1.toString(), "MultiKey[key1, key2]");
    }
}
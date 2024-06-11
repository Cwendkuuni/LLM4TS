import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorFiveKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key5", multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<>("key1", "key3");

        assertEquals(multiKey1, multiKey2);
        assertNotEquals(multiKey1, multiKey3);
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");

        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
}
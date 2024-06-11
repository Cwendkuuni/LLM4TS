import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals(2, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testConstructorThreeKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals(3, multiKey.size());
        assertEquals(1, multiKey.getKey(0).intValue());
        assertEquals(2, multiKey.getKey(1).intValue());
        assertEquals(3, multiKey.getKey(2).intValue());
    }

    @Test
    public void testEquals() {
        MultiKey<Boolean> multiKey1 = new MultiKey<>(true, false);
        MultiKey<Boolean> multiKey2 = new MultiKey<>(true, false);
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        MultiKey<Long> multiKey = new MultiKey<>(100L, 200L);
        assertEquals(100L ^ 200L, multiKey.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("test1", "test2");
        assertEquals("MultiKey[test1, test2]", multiKey.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyOutOfBounds() {
        MultiKey<String> multiKey = new MultiKey<>("key");
        multiKey.getKey(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeysConstructor() {
        new MultiKey<>(null);
    }

    @Test
    public void testReadResolve() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4);
        MultiKey<Integer> deserialized = (MultiKey<Integer>) SerializationUtils.deserialize(SerializationUtils.serialize(multiKey));
        assertTrue(multiKey.equals(deserialized));
        assertEquals(multiKey.hashCode(), deserialized.hashCode());
    }
}
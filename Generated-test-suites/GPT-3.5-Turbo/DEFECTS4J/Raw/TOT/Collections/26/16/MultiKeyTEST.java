import org.junit.*;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        String[] expectedKeys = {key1, key2};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructorWithThreeKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;

        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);

        Integer[] expectedKeys = {key1, key2, key3};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructorWithFourKeys() {
        Double key1 = 1.0;
        Double key2 = 2.0;
        Double key3 = 3.0;
        Double key4 = 4.0;

        MultiKey<Double> multiKey = new MultiKey<>(key1, key2, key3, key4);

        Double[] expectedKeys = {key1, key2, key3, key4};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructorWithFiveKeys() {
        Character key1 = 'a';
        Character key2 = 'b';
        Character key3 = 'c';
        Character key4 = 'd';
        Character key5 = 'e';

        MultiKey<Character> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        Character[] expectedKeys = {key1, key2, key3, key4, key5};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructorWithArray() {
        Object[] keys = {1, "two", 3.0};

        MultiKey<Object> multiKey = new MultiKey<>(keys);

        assertArrayEquals(keys, multiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        MultiKey<Object> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testGetKey() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        String key = "key";

        MultiKey<String> multiKey = new MultiKey<>(key);

        multiKey.getKey(1);
    }

    @Test
    public void testSize() {
        Object[] keys = {"one", 2, 3.0, '4'};

        MultiKey<Object> multiKey = new MultiKey<>(keys);

        assertEquals(keys.length, multiKey.size());
    }

    @Test
    public void testEquals() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey3 = new MultiKey<>(key2, key1);

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(null));
        assertFalse(multiKey1.equals("key1, key2"));
    }

    @Test
    public void testHashCode() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);

        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        Integer key1 = 1;
        Integer key2 = 2;

        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);

        String expectedString = "MultiKey[1, 2]";
        assertEquals(expectedString, multiKey.toString());
    }

}
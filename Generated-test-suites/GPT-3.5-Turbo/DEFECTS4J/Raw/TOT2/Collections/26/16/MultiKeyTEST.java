import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testConstructorWithThreeKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }

    @Test
    public void testConstructorWithFourKeys() {
        Double key1 = 1.0;
        Double key2 = 2.0;
        Double key3 = 3.0;
        Double key4 = 4.0;
        MultiKey<Double> multiKey = new MultiKey<>(key1, key2, key3, key4);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }

    @Test
    public void testConstructorWithFiveKeys() {
        Boolean key1 = true;
        Boolean key2 = false;
        Boolean key3 = true;
        Boolean key4 = false;
        Boolean key5 = true;
        MultiKey<Boolean> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }

    @Test
    public void testConstructorWithArrayKeys() {
        Character key1 = 'a';
        Character key2 = 'b';
        Character key3 = 'c';
        Character[] keysArray = {key1, key2, key3};
        MultiKey<Character> multiKey = new MultiKey<>(keysArray);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertArrayEquals(keysArray, multiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArrayKeys() {
        new MultiKey<>(null);
    }

    @Test
    public void testSizeMethod() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testEqualsMethod() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals(multiKey1, multiKey2);
    }

    @Test
    public void testHashCodeMethod() {
        Double key1 = 1.0;
        Double key2 = 2.0;
        MultiKey<Double> multiKey = new MultiKey<>(key1, key2);
        int expectedHashCode = key1.hashCode() ^ key2.hashCode();
        assertEquals(expectedHashCode, multiKey.hashCode());
    }
}
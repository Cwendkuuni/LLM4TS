import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    //Test constructor with two keys
    @Test
    public void testConstructorTwoKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        MultiKey<String> multiKey = new MultiKey<String>(key1, key2);
        assertNotNull(multiKey);
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
    }

    //Test constructor with three keys
    @Test
    public void testConstructorThreeKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        MultiKey<String> multiKey = new MultiKey<String>(key1, key2, key3);
        assertNotNull(multiKey);
        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
    }

    //Test constructor with four keys
    @Test
    public void testConstructorFourKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        String key4 = "Key4";
        MultiKey<String> multiKey = new MultiKey<String>(key1, key2, key3, key4);
        assertNotNull(multiKey);
        assertEquals(4, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
        assertEquals(key4, multiKey.getKeys()[3]);
    }

    //Test constructor with five keys
    @Test
    public void testConstructorFiveKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        String key4 = "Key4";
        String key5 = "Key5";
        MultiKey<String> multiKey = new MultiKey<String>(key1, key2, key3, key4, key5);
        assertNotNull(multiKey);
        assertEquals(5, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
        assertEquals(key4, multiKey.getKeys()[3]);
        assertEquals(key5, multiKey.getKeys()[4]);
    }

    //Test constructor with array of keys
    @Test
    public void testConstructorArrayKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        String[] keys = {key1, key2, key3};
        MultiKey<String> multiKey = new MultiKey<String>(keys);
        assertNotNull(multiKey);
        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
    }

    //Test equals method
    @Test
    public void testEquals() {
        String key1 = "Key1";
        String key2 = "Key2";
        MultiKey<String> multiKey1 = new MultiKey<String>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<String>(key1, key2);
        assertTrue(multiKey1.equals(multiKey2));
    }

    //Test hashcode method
    @Test
    public void testHashCode() {
        String key1 = "Key1";
        String key2 = "Key2";
        MultiKey<String> multiKey1 = new MultiKey<String>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<String>(key1, key2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    //Test toString method
    @Test
    public void testToString() {
        String key1 = "Key1";
        String key2 = "Key2";
        MultiKey<String> multiKey = new MultiKey<String>(key1, key2);
        String expected = "MultiKey[Key1, Key2]";
        assertEquals(expected, multiKey.toString());
    }

}
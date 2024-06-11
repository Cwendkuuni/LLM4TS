
package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class MultiKeyTest {
  
    @Test
    public void testConstructorAndGetKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
        
        assertNotEquals(key1, multiKey.getKey(1));
        assertNotEquals(key2, multiKey.getKey(2));
        assertNotEquals(key3, multiKey.getKey(3));
        assertNotEquals(key4, multiKey.getKey(4));
        assertNotEquals(key5, multiKey.getKey(0));
    }

   
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullKeys() {
        new MultiKey<String>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullKey() {
        String key1 = "key1";
        String key2 = null;
        
        new MultiKey<String>(key1, key2);
    }

  
    @Test
    public void testEqualsAndHashCode() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";

        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2, key3, key4, key5);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2, key3, key4, key5);

        assertEquals(multiKey1, multiKey2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());

        String key6 = "key6";
        MultiKey<String> multiKey3 = new MultiKey<>(key6, key2, key3, key4, key5);

        assertNotEquals(multiKey1, multiKey3);
        assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }

   
    @Test
    public void testToString() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        String expectedString = "MultiKey[key1, key2, key3, key4, key5]";
        assertEquals(expectedString, multiKey.toString());
    }
}

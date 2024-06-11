package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        K[] keyArray1 = (K[]) new Object[] {"key1", "key2"};
        MultiKey<String> multiKey1 = new MultiKey<>(keyArray1);

        K[] keyArray2 = (K[]) new Object[] {"key3", "key4"};
        MultiKey<String> multiKey2 = new MultiKey<>(keyArray2);

        K[] keyArray3 = (K[]) new Object[] {"key5", "key6", "key7"};
        MultiKey<String> multiKey3 = new MultiKey<>(keyArray3);

        K[] keyArray4 = (K[]) new Object[] {"key8", "key9", "key10", "key11"};
        MultiKey<String> multiKey4 = new MultiKey<>(keyArray4);

        MultiKey<String> multiKey5 = new MultiKey<>("key12", "key13");
        MultiKey<String> multiKey6 = new MultiKey<>("key14", "key15");
        MultiKey<String> multiKey7 = new MultiKey<>("key16", "key17", "key18");
        MultiKey<String> multiKey8 = new MultiKey<>("key19", "key20", "key21", "key22");

        MultiKey<String> multiKey9 = new MultiKey<>(new String[]{"key23", "key24"});
        MultiKey<String> multiKey10 = new MultiKey<>(new String[]{"key25", "key26"}, false);

        assertArrayEquals(keyArray1, multiKey1.getKeys());
        assertArrayEquals(keyArray2, multiKey2.getKeys());
        assertArrayEquals(keyArray3, multiKey3.getKeys());
        assertArrayEquals(keyArray4, multiKey4.getKeys());
        assertArrayEquals(new String[]{"key12", "key13"}, multiKey5.getKeys());
        assertArrayEquals(new String[]{"key14", "key15"}, multiKey6.getKeys());
        assertArrayEquals(new String[]{"key16", "key17", "key18"}, multiKey7.getKeys());
        assertArrayEquals(new String[]{"key19", "key20", "key21", "key22"}, multiKey8.getKeys());
        assertArrayEquals(new String[]{"key23", "key24"}, multiKey9.getKeys());
    }

    @Test
    public void testGetKey() {
        K[] keys = (K[]) new Object[] {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);

        assertSame("key1", multiKey.getKey(0));
        assertSame("key2", multiKey.getKey(1));
        assertSame("key3", multiKey.getKey(2));

        try {
            multiKey.getKey(-1);
            fail("Should have thrown IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            multiKey.getKey(3);
            fail("Should have thrown IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSize() {
        K[] keys = (K[]) new Object[] {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);

        assertEquals(3, multiKey.size());
    }

    @Test
    public void testEquals() {
        K[] keys1 = (K[]) new Object[] {"key1", "key2"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);

        K[] keys2 = (K[]) new Object[] {"key1", "key3"};
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);

        K[] keys3 = (K[]) new Object[] {"key2", "key1"};
        MultiKey<String> multiKey3 = new MultiKey<>(keys3);

        K[] keys4 = (K[]) new Object[] {"key1", "key2", "key3"};
        MultiKey<String> multiKey4 = new MultiKey<>(keys4);

        K[] keys5 = (K[]) new Object[] {"key1", "key4"};
        MultiKey<String> multiKey5 = new MultiKey<>(keys5);

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey1));

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey1));

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey1));

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey1));

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey1));

        assertFalse(multiKey1.equals(null));
        assertFalse(multiKey1.equals(new Object()));
        assertFalse(multiKey1.equals(""));
        assertFalse(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey4));
        assertFalse(multiKey1.equals(multiKey5));

        MultiKey<String> multiKey6 = new MultiKey<>(keys1, false);
        assertTrue(multiKey1.equals(multiKey6));
        assertFalse(multiKey6.equals(multiKey1));
    }

    @Test
    public void testHashCode() {
        K[] keys = (K[]) new Object[] {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);

        MultiKey<String> multiKeyClone = new MultiKey<>(keys.clone());

        int hashCode = multiKey.hashCode();
        assertEquals(hashCode, multiKeyClone.hashCode());

        assertFalse(multiKey.equals(multiKeyClone));
    }

    @Test
    public void testToString() {
        K[] keys = (K[]) new Object[] {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);

        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }

    @Test
    public void testClone() {
        K[] keys = (K[]) new Object[] {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys, false);

        assertArrayEquals(keys, multiKey.getKeys());

        MultiKey<String> clonedMultiKey = multiKey.clone();
        assertArrayEquals(keys.clone(), clonedMultiKey.getKeys());
        assertFalse(multiKey.equals(clonedMultiKey));
    }
}
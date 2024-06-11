package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {
    @Test
    public void testConstructorWithDefaultNoEntryValue() {
        TCharHash hash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        TCharHash hash = new TCharHash(initialCapacity);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(initialCapacity, hash._set.length);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TCharHash hash = new TCharHash(initialCapacity, loadFactor);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(initialCapacity, hash._set.length);
        assertEquals(loadFactor, hash._loadFactor, 0.0f);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        char noEntryValue = '!';
        TCharHash hash = new TCharHash(initialCapacity, loadFactor, noEntryValue);
        assertEquals(noEntryValue, hash.getNoEntryValue());
        assertEquals(initialCapacity, hash._set.length);
        assertEquals(loadFactor, hash._loadFactor, 0.0f);
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TCharHash hash = new TCharHash();
        hash.setUp(initialCapacity);
        assertEquals(initialCapacity, hash._set.length);
    }

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{1, 2, 3, 4, 5};
        assertTrue(hash.contains(1));
        assertTrue(hash.contains(5));
        assertFalse(hash.contains(6));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{1, 2, 3, 4, 5};
        final StringBuilder result = new StringBuilder();
        hash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                result.append(value);
                return true;
            }
        });
        assertEquals("12345", result.toString());
    }

    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{1, 2, 3, 4, 5};
        hash._states = new byte[]{1, 1, 1, 1, 1};
        hash.removeAt(2);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash._set[2]);
        assertEquals(0, hash._states[2]);
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{1, 2, 3, 4, 5};
        hash._states = new byte[]{1, 1, 1, 0, 1};
        assertEquals(2, hash.index(3));
        assertEquals(-1, hash.index(6));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{1, 2, 3, 4, 5};
        hash._states = new byte[]{1, 1, 1, 0, 1};
        assertEquals(2, hash.indexRehashed(3, 2, 7, (byte) 1));
        assertEquals(-1, hash.indexRehashed(6, 2, 7, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{1, 2, 3, 4, 0};
        hash._states = new byte[]{1, 1, 1, 0, 0};
        assertTrue(hash.insertKey(5) >= 0);
        assertTrue(hash.insertKey(6) < 0);
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{1, 2, 3, 4, 0};
        hash._states = new byte[]{1, 1, 1, 0, 0};
        assertTrue(hash.insertKeyRehash(5, 2, 7, (byte) 1) >= 0);
        try {
            hash.insertKeyRehash(6, 2, 7, (byte) 1);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{1, 2, 3, 4, 0};
        hash._states = new byte[]{1, 1, 1, 0, 0};
        hash.insertKeyAt(4, 5);
        assertEquals(5, hash._set[4]);
        assertEquals((byte) 1, hash._states[4]);
    }
}
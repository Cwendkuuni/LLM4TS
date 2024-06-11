package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TCharHashTest {

    private TCharHash tCharHash;

    @Before
    public void setUp() {
        tCharHash = new TCharHash() {
            @Override
            protected int indexRehashed(char key, int index, int hash, byte state) {
                return super.indexRehashed(key, index, hash, state);
            }

            @Override
            protected int insertKeyRehash(char val, int index, int hash, byte state) {
                return super.insertKeyRehash(val, index, hash, state);
            }

            @Override
            void insertKeyAt(int index, char val) {
                super.insertKeyAt(index, val);
            }
        };
    }

    @Test
    public void testContains() {
        assertFalse(tCharHash.contains('a'));
        assertFalse(tCharHash.contains('b'));
    }

    @Test
    public void testForEach() {
        assertFalse(tCharHash.forEach(value -> {
            fail("Should not be called");
            return true;
        }));
    }

    @Test
    public void testRemoveAt() {
        tCharHash._set = new char[]{ 'a', 'b'};
        tCharHash._states = new byte[]{ 1, 1};
        tCharHash.no_entry_value = '!';
        tCharHash.removeAt(0);
        assertEquals('!', tCharHash._set[0]);
    }

    @Test
    public void testIndex_rehash_key_exists() {
        tCharHash._set = new char[]{ 'a', 'b'};
        tCharHash._states = new byte[]{ 1, 1};
        tCharHash.no_entry_value = '!';
        int index = tCharHash.indexRehashed('a', 0, 45, (byte) 1);
        assertEquals(0, index);
    }

    @Test
    public void testIndex_rehash_key_not_exists() {
        tCharHash._set = new char[]{ 'a', 'b'};
        tCharHash._states = new byte[]{ 1, 1};
        tCharHash.no_entry_value = '!';
        int index = tCharHash.indexRehashed('c', 0, 45, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey_rehash_free_slot() {
        tCharHash._set = new char[]{ 'a', '!', 'b'};
        tCharHash._states = new byte[]{ 1, 0, 1};
        tCharHash.no_entry_value = '!';
        boolean consumeFreeSlot = false;
        int index = tCharHash.insertKeyRehash('c', 1, 45, (byte) 0);
        assertTrue(consumeFreeSlot);
        assertEquals(1, index);
        assertEquals('c', tCharHash._set[1]);
        assertEquals((byte) 1, tCharHash._states[1]);
    }

    @Test
    public void testInsertKey_rehash_no_free_slot() {
        tCharHash._set = new char[]{ 'a', 'b'};
        tCharHash._states = new byte[]{ 1, 1};
        tCharHash.no_entry_value = '!';
        boolean consumeFreeSlot = false;
        try {
            tCharHash.insertKeyRehash('c', 0, 45, (byte) 0);
        } catch (IllegalStateException e) {
            consumeFreeSlot = true;
        }
        assertTrue(consumeFreeSlot);
    }

    @Test
    public void testInsertKeyAt() {
        tCharHash._set = new char[]{ '!'};
        tCharHash._states = new byte[]{ 0};
        tCharHash.no_entry_value = '!';
        tCharHash.insertKeyAt(0, 'a');
        assertEquals('a', tCharHash._set[0]);
        assertEquals((byte) 1, tCharHash._states[0]);
    }
}
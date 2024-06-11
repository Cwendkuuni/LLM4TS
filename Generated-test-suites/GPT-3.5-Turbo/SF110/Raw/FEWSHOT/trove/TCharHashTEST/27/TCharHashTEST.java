package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TCharHashTest {

    private TCharHash tCharHash;

    @Before
    public void setUp() {
        tCharHash = new TCharHash();
    }

    @Test
    public void testConstructor() {
        assertNotNull(tCharHash);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
        assertEquals(TPrimitiveHash.DEFAULT_CAPACITY, tCharHash._states.length);
        assertArrayEquals(new char[tCharHash._states.length], tCharHash._set);
    }

    @Test
    public void testConstructor_InitialCapacity() {
        tCharHash = new TCharHash(10);
        assertNotNull(tCharHash);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
        assertEquals(10, tCharHash._states.length);
        assertArrayEquals(new char[tCharHash._states.length], tCharHash._set);
    }

    @Test
    public void testConstructor_InitialCapacity_LoadFactor() {
        tCharHash = new TCharHash(10, 0.5f);
        assertNotNull(tCharHash);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
        assertEquals(10, tCharHash._states.length);
        assertArrayEquals(new char[tCharHash._states.length], tCharHash._set);
    }

    @Test
    public void testConstructor_InitialCapacity_LoadFactor_NoEntryValue() {
        tCharHash = new TCharHash(10, 0.5f, 'N');
        assertNotNull(tCharHash);
        assertEquals('N', tCharHash.getNoEntryValue());
        assertEquals(10, tCharHash._states.length);
        assertArrayEquals(new char[tCharHash._states.length], tCharHash._set);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int capacity = tCharHash.setUp(10);
        assertEquals(10, capacity);
        assertEquals(10, tCharHash._set.length);
        assertArrayEquals(new char[10], tCharHash._set);
    }

    @Test
    public void testContains() {
        assertFalse(tCharHash.contains('a'));
        tCharHash.insertKey('a');
        assertTrue(tCharHash.contains('a'));
    }

    @Test
    public void testForEach() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        assertFalse(tCharHash.forEach(new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                if (value == 'b') {
                    return false;
                }
                return true;
            }
        }));
    }

    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('a');
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('a'));
    }

    @Test
    public void testIndex() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        assertEquals(0, tCharHash.index('a'));
        assertEquals(1, tCharHash.index('b'));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tCharHash.insertKey('a'));
        assertEquals(-1, tCharHash.insertKey('a'));
        assertEquals(1, tCharHash.insertKey('b'));
        assertEquals(0, tCharHash.insertKey('a'));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKey_FullKeySet() {
        char c = 'a';
        for (int i = 0; i < tCharHash._states.length; i++) {
            tCharHash.insertKey(c);
            c++;
        }
        tCharHash.insertKey('b');
    }

    @Test
    public void testInsertKeyAt() {
        tCharHash.insertKeyAt(0, 'a');
        assertEquals('a', tCharHash._set[0]);
        assertEquals(1, tCharHash._states[0]);
    }
}
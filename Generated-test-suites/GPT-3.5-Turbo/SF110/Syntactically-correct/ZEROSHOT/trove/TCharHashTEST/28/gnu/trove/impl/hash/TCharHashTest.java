package gnu.trove.impl.hash;

import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testNoEntryValue() {
        TCharHash tCharHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());

        TCharHash tCharHashWithInitialCapacity = new TCharHash(10);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHashWithInitialCapacity.getNoEntryValue());

        TCharHash tCharHashWithCapacityAndLoadFactor = new TCharHash(10, 0.75f);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHashWithCapacityAndLoadFactor.getNoEntryValue());

        char customNoEntryValue = 'A';
        TCharHash tCharHashWithAllParameters = new TCharHash(10, 0.75f, customNoEntryValue);
        assertEquals(customNoEntryValue, tCharHashWithAllParameters.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash(10);
        char val = 'A';
        assertFalse(tCharHash.contains(val));

        tCharHash.insertKey(val);
        assertTrue(tCharHash.contains(val));
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash(10);

        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');

        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                System.out.print(value);
                return true;
            }
        };

        assertTrue(tCharHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash(10);
        tCharHash.insertKey('A');
        int index = tCharHash.index('A');
        assertEquals('A', tCharHash._set[index]);
        
        tCharHash.removeAt(index);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash._set[index]);
    }

    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash(10);
        char val = 'A';
        assertEquals(-1, tCharHash.index(val));

        tCharHash.insertKey(val);
        assertEquals(0, tCharHash.index(val));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash tCharHash = new TCharHash(10);

        char key = 'A';
        int index = 5;
        int hash = 23;
        byte state = 1;

        assertEquals(-1, tCharHash.indexRehashed(key, index, hash, state));
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash(10);
        char val = 'A';
        assertEquals(0, tCharHash.insertKey(val));
        
        int index = tCharHash.index(val);
        assertEquals(val, tCharHash._set[index]);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash(5);

        for (int i = 0; i < 5; i++) {
            tCharHash.insertKey((char) ('A' + i));
        }

        char invalidKey = 'F'; // Key set full
        int index = 1;
        int hash = 31;
        byte state = 2;

        tCharHash.insertKeyRehash(invalidKey, index, hash, state);
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash(10);
        char val = 'A';
        int index = 2;

        tCharHash.insertKeyAt(index, val);
        assertEquals(val, tCharHash._set[index]);
        assertEquals(1, tCharHash._states[index]);
    }
}
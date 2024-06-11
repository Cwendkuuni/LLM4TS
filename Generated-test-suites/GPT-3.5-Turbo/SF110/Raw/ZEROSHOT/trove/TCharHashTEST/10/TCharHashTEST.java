package gnu.trove.impl.hash;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TCharHashTest {
    
    @Test
    public void testConstructor() {
        TCharHash tCharHash = new TCharHash();
        Assert.assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
        Assert.assertNotNull(tCharHash._set);
        Assert.assertArrayEquals(new char[tCharHash._set.length], tCharHash._set);
        
        TCharHash tCharHash2 = new TCharHash(10);
        Assert.assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash2.getNoEntryValue());
        Assert.assertNotNull(tCharHash2._set);
        Assert.assertArrayEquals(new char[tCharHash2._set.length], tCharHash2._set);
        
        TCharHash tCharHash3 = new TCharHash(10, 0.75f);
        Assert.assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash3.getNoEntryValue());
        Assert.assertNotNull(tCharHash3._set);
        Assert.assertArrayEquals(new char[tCharHash3._set.length], tCharHash3._set);
        
        char no_entry_value = 'N';
        TCharHash tCharHash4 = new TCharHash(10, 0.75f, no_entry_value);
        Assert.assertEquals(no_entry_value, tCharHash4.getNoEntryValue());
        Assert.assertNotNull(tCharHash4._set);
        Assert.assertArrayEquals(new char[tCharHash4._set.length], tCharHash4._set);
    }
    
    @Test
    public void testSetUp() {
        TCharHash tCharHash = new TCharHash();
        int capacity = 10;
        int expected = capacity + capacity / 2;
        Assert.assertEquals(expected, tCharHash.setUp(capacity));
        Assert.assertNotNull(tCharHash._set);
        Assert.assertArrayEquals(new char[tCharHash._set.length], tCharHash._set);
    }
    
    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        Assert.assertFalse(tCharHash.contains(val));
        
        tCharHash.insertKey(val);
        Assert.assertTrue(tCharHash.contains(val));
    }
    
    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        char[] values = {'A', 'B', 'C'};
        for (char val : values) {
            tCharHash.insertKey(val);
        }
        
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                Assert.assertTrue(Arrays.asList(values).contains(value));
                return true;
            }
        };
        
        Assert.assertTrue(tCharHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        tCharHash.insertKey(val);
        Assert.assertTrue(tCharHash.contains(val));
        
        tCharHash.removeAt(0);
        Assert.assertFalse(tCharHash.contains(val));
    }
    
    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        Assert.assertEquals(-1, tCharHash.index(val));
        
        tCharHash.insertKey(val);
        Assert.assertEquals(0, tCharHash.index(val));
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        int index = 0;
        int hash = val;
        byte state = 1;
        Assert.assertEquals(-1, tCharHash.indexRehashed(val, index, hash, state));
        
        tCharHash.insertKey(val);
        Assert.assertEquals(0, tCharHash.indexRehashed(val, index, hash, state));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        Assert.assertEquals(0, tCharHash.insertKey(val));
        Assert.assertTrue(tCharHash.contains(val));
        
        Assert.assertEquals(-1, tCharHash.insertKey(val));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        int index = 0;
        int hash = val;
        byte state = 1;
        Assert.assertEquals(-1, tCharHash.insertKeyRehash(val, index, hash, state));
        
        tCharHash.insertKey(val);
        Assert.assertEquals(-1, tCharHash.insertKeyRehash(val, index, hash, state));
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        int index = 0;
        tCharHash.insertKeyAt(index, val);
        Assert.assertEquals(val, tCharHash._set[index]);
        Assert.assertEquals(1, tCharHash._states[index]);
    }
}
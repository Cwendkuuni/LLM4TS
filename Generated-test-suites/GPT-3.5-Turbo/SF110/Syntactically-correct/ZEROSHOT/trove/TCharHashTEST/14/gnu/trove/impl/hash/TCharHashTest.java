import org.junit.Assert;
import org.junit.Test;

public class TCharHashTest {
    
    @Test
    public void testConstructorNoArgs() {
        TCharHash tCharHash = new TCharHash() {
            @Override
            boolean setUp(int initialCapacity) {
                return true;
            }
        };
        
        char expected = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        char actual = tCharHash.getNoEntryValue();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 5;
        TCharHash tCharHash = new TCharHash(initialCapacity) {
            @Override
            boolean setUp(int initialCapacity) {
                return true;
            }
        };
        
        char expected = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        char actual = tCharHash.getNoEntryValue();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 5;
        float loadFactor = 0.75f;
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor) {
            @Override
            boolean setUp(int initialCapacity) {
                return true;
            }
        };
        
        char expected = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        char actual = tCharHash.getNoEntryValue();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        int initialCapacity = 5;
        float loadFactor = 0.75f;
        char no_entry_value = ' ';
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor, no_entry_value) {
            @Override
            boolean setUp(int initialCapacity) {
                return true;
            }
        };
        
        char expected = no_entry_value;
        char actual = tCharHash.getNoEntryValue();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash() {
            @Override
            boolean setUp(int initialCapacity) {
                return true;
            }
        };
        
        tCharHash._set = new char[5];
        tCharHash._states = new byte[] {1, 1, 1, 0, 1};
        
        // Positive case
        char val1 = 'A';
        boolean actual1 = tCharHash.contains(val1);
        Assert.assertTrue(actual1);
        
        // Negative case
        char val2 = 'B';
        boolean actual2 = tCharHash.contains(val2);
        Assert.assertFalse(actual2);
    }
    
    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash() {
            @Override
            boolean setUp(int initialCapacity) {
                return true;
            }
        };
        
        tCharHash._set = new char[] {'A', 'B', 'C', 'D', 'E'};
        tCharHash._states = new byte[] {1, 1, 1, 0, 1};
        
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return true;
            }
        };
        
        boolean actual = tCharHash.forEach(procedure);
        Assert.assertTrue(actual);
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash() {
            @Override
            boolean setUp(int initialCapacity) {
                return true;
            }
        };
        
        tCharHash._set = new char[] {'A', 'B', 'C', 'D', 'E'};
        tCharHash._states = new byte[] {1, 1, 1, 0, 1};
        
        int index = 3;
        
        tCharHash.removeAt(index);
        
        char expected = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        char actual = tCharHash._set[index];
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash() {
            @Override
            boolean setUp(int initialCapacity) {
                return true;
            }
        };
        
        tCharHash._set = new char[] {'A', 'B', 'C', 'D', 'E'};
        tCharHash._states = new byte[] {1, 1, 1, 0, 1};
        
        // Positive case
        char val1 = 'B';
        int expected1 = 1;
        int actual1 = tCharHash.index(val1);
        Assert.assertEquals(expected1, actual1);
        
        // Negative case
        char val2 = 'F';
        int expected2 = -1;
        int actual2 = tCharHash.index(val2);
        Assert.assertEquals(expected2, actual2);
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash tCharHash = new TCharHash() {
            @Override
            boolean setUp(int initialCapacity) {
                return true;
            }
        };
        
        tCharHash._set = new char[] {'A', 'B', 'C', 'D', 'E'};
        tCharHash._states = new byte[] {1, 1, 1, 0, 1};
        
        // Positive case
        char key1 = 'C';
        int index1 = 0;
        int hash1 = 2;
        byte state1 = 1;
        
        int expected1 = 2;
        int actual1 = tCharHash.indexRehashed(key1, index1, hash1, state1);
        Assert.assertEquals(expected1, actual1);
        
        // Negative case
        char key2 = 'D';
        int index2 = 1;
        int hash2 = 3;
        byte state2 = 0;
        
        int expected2 = -1;
        int actual2 = tCharHash.indexRehashed(key2, index2, hash2, state2);
        Assert.assertEquals(expected2, actual2);
    }
    
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash() {
            @Override
            boolean setUp(int initialCapacity) {
                return true;
            }
        };
        
        tCharHash._set = new char[5];
        tCharHash._states = new byte[5];
        
        // Positive case
        char val1 = 'A';
        int expected1 = 0;
        int actual1 = tCharHash.insertKey(val1);
        Assert.assertEquals(expected1, actual1);
        
        // Negative case
        char val2 = 'B';
        int expected2 = 1;
        int actual2 = tCharHash.insertKey(val2);
        Assert.assertEquals(expected2, actual2);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash() {
            @Override
            boolean setUp(int initialCapacity) {
                return true;
            }
        };
        
        tCharHash._set = new char[5];
        tCharHash._states = new byte[5];
        
        // Positive case
        char val1 = 'A';
        int index1 = 0;
        int hash1 = 2;
        byte state1 = 1;
        
        int expected1 = 2;
        int actual1 = tCharHash.insertKeyRehash(val1, index1, hash1, state1);
        Assert.assertEquals(expected1, actual1);
        
        // Negative case
        char val2 = 'B';
        int index2 = 1;
        int hash2 = 3;
        byte state2 = 0;
        
        int expected2 = -1;
        int actual2 = tCharHash.insertKeyRehash(val2, index2, hash2, state2);
        Assert.assertEquals(expected2, actual2);
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash() {
            @Override
            boolean setUp(int initialCapacity) {
                return true;
            }
        };
        
        tCharHash._set = new char[5];
        tCharHash._states = new byte[5];
        
        char val = 'A';
        int index = 2;
        
        tCharHash.insertKeyAt(index, val);
        
        char expected = val;
        char actual = tCharHash._set[index];
        Assert.assertEquals(expected, actual);
    }
}
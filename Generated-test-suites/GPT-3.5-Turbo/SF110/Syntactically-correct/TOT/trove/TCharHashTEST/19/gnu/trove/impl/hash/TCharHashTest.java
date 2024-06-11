import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    @Test
    public void testConstructor1() {
        TCharHash tCharHash = new TCharHash();
        assertNotNull(tCharHash);
        assertEquals('\0', tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor2() {
        TCharHash tCharHash = new TCharHash(10);
        assertNotNull(tCharHash);
        assertEquals(10, tCharHash.size());
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor3() {
        TCharHash tCharHash = new TCharHash(10, 0.5f);
        assertNotNull(tCharHash);
        assertEquals(10, tCharHash.size());
        assertEquals(0.5f, tCharHash.loadFactor, 0.001);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor4() {
        TCharHash tCharHash = new TCharHash(10, 0.5f, 'A');
        assertNotNull(tCharHash);
        assertEquals(10, tCharHash.size());
        assertEquals(0.5f, tCharHash.loadFactor, 0.001);
        assertEquals('A', tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.setUp(5);
        assertEquals(5, tCharHash._set.length);
    }
    
    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[]{1, 2, 3, 4, 5};
        assertEquals(true, tCharHash.contains(3));
        assertEquals(false, tCharHash.contains(6));
    }
    
    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._states = new byte[]{1, 1, 0, 1};
        tCharHash._set = new char[]{1, 2, 0, 4};
        TCharProcedure tCharProcedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };
        
        assertEquals(true, tCharHash.forEach(tCharProcedure));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[]{1, 2, 3};
        tCharHash.removeAt(1);
        assertEquals('\0', tCharHash._set[1]);
        assertEquals(2, tCharHash.size());
    }
    
    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._states = new byte[]{1, 0, 1};
        tCharHash._set = new char[]{1, 0, 3};
        
        assertEquals(0, tCharHash.index(1));
        assertEquals(-1, tCharHash.index(2));
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._states = new byte[]{1, 0, 1};
        tCharHash._set = new char[]{1, 0, 3};
        
        assertEquals(0, tCharHash.indexRehashed(1, 0, 0, (byte) 1));
        assertEquals(-1, tCharHash.indexRehashed(2, 0, 0, (byte) 1));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._states = new byte[]{1, 0, 1};
        tCharHash._set = new char[]{1, 0, 3};
        
        assertEquals(1, tCharHash.insertKey(2));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._states = new byte[]{1, 1, 1};
        tCharHash._set = new char[]{1, 2, 3};
        
        tCharHash.insertKeyRehash(4, 0, 0, (byte) 1);
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[]{1, 2, 3};
        tCharHash._states = new byte[]{1, 1, 0};
        
        tCharHash.insertKeyAt(2, 4);
        assertEquals(4, tCharHash._set[2]);
        assertEquals(1, tCharHash._states[2]);
    }
}
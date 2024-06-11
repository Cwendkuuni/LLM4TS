import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {
    /**
     * Test method for {@link TCharHash#TCharHash()}.
     */
    @Test
    public void testTCharHash() {
        TCharHash tCharHash = new TCharHash();
        assertNotNull(tCharHash);
    }
    
    /**
     * Test method for {@link TCharHash#TCharHash(int)}.
     */
    @Test
    public void testTCharHashWithInitialCapacity() {
        TCharHash tCharHash = new TCharHash(10);
        assertNotNull(tCharHash);
    }
    
    /**
     * Test method for {@link TCharHash#TCharHash(int, float)}.
     */
    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash tCharHash = new TCharHash(10, 0.8f);
        assertNotNull(tCharHash);
    }
    
    /**
     * Test method for {@link TCharHash#TCharHash(int, float, char)}.
     */
    @Test
    public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        TCharHash tCharHash = new TCharHash(10, 0.8f, '\0');
        assertNotNull(tCharHash);
    }
    
    /**
     * Test method for {@link TCharHash#getNoEntryValue()}.
     */
    @Test
    public void testGetNoEntryValue() {
        TCharHash tCharHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }

    /**
     * Test method for {@link TCharHash#contains(char)}.
     */
    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        assertTrue(tCharHash.contains('A'));
        assertFalse(tCharHash.contains('B'));
    }
    
    /**
     * Test method for {@link TCharHash#forEach(TCharProcedure)}.
     */
    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        
        final StringBuilder sb = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                sb.append(value);
                return true;
            }
        };
        
        tCharHash.forEach(procedure);
        assertEquals("AB", sb.toString());
    }
    
    /**
     * Test method for {@link TCharHash#removeAt(int)}.
     */
    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('A'));
        assertTrue(tCharHash.contains('B'));
    }
    
    /**
     * Test method for {@link TCharHash#index(char)}.
     */
    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        
        assertEquals(0, tCharHash.index('A'));
        assertEquals(-1, tCharHash.index('B'));
    }
    
    /**
     * Test method for {@link TCharHash#indexRehashed(char, int, int, byte)}.
     */
    @Test
    public void testIndexRehashed() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        
        assertEquals(0, tCharHash.indexRehashed('A', 0, 123, (byte)1));
        assertEquals(-1, tCharHash.indexRehashed('B', 0, 123, (byte)1));
    }
    
    /**
     * Test method for {@link TCharHash#insertKey(char)}.
     */
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        int index = tCharHash.insertKey('A');
        
        assertEquals(0, index);
        assertTrue(tCharHash.contains('A'));
    }
    
    /**
     * Test method for {@link TCharHash#insertKeyRehash(char, int, int, byte)}.
     */
    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        int index = tCharHash.insertKeyRehash('C', 0, 123, (byte)1);
        
        assertEquals(2, index);
        assertTrue(tCharHash.contains('C'));
    }
    
    /**
     * Test method for {@link TCharHash#insertKeyAt(int, char)}.
     */
    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKeyAt(0, 'A');
        
        assertTrue(tCharHash.contains('A'));
    }
}
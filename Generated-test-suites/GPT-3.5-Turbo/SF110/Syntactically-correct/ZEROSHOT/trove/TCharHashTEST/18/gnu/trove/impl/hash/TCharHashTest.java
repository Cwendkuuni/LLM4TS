import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {
    private TCharHash tCharHash;
    
    @Before
    public void setUp() {
        tCharHash = new TCharHash();
    }
    
    @Test
    public void testConstructor_noArgs() {
        TCharHash hash = new TCharHash();
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash.size());
    }
    
    @Test
    public void testConstructor_initialCapacity() {
        TCharHash hash = new TCharHash(20);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(20, hash.size());
    }
    
    @Test
    public void testConstructor_initialCapacity_loadFactor() {
        TCharHash hash = new TCharHash(20, 0.75f);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(20, hash.size());
    }
    
    @Test
    public void testConstructor_initialCapacity_loadFactor_noEntryValue() {
        TCharHash hash = new TCharHash(20, 0.75f, 'X');
        assertEquals('X', hash.getNoEntryValue());
        assertEquals(20, hash.size());
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        assertFalse(tCharHash.contains('A'));
        tCharHash.insertKey('A');
        assertTrue(tCharHash.contains('A'));
    }
    
    @Test
    public void testForEach() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        
        final StringBuilder sb = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                sb.append(value);
                return true;
            }
        };
        
        assertTrue(tCharHash.forEach(procedure));
        assertEquals("ABC", sb.toString());
    }
    
    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        
        tCharHash.removeAt(1);
        assertFalse(tCharHash.contains('B'));
    }
    
    @Test
    public void testIndex() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        
        assertEquals(1, tCharHash.index('B'));
        assertEquals(-1, tCharHash.index('D'));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, tCharHash.insertKey('A'));
        assertEquals(-1, tCharHash.insertKey('A'));
        tCharHash.removeAt(0);
        assertEquals(0, tCharHash.insertKey('A'));
    }
}
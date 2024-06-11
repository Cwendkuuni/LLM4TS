import static org.junit.Assert.*;
import org.junit.*;

public class TCharHashTest {
    
    private TCharHash tCharHash;
    
    @Before
    public void setUp() {
        tCharHash = new TCharHash();
    }
    
    @Test
    public void testContains() {
        assertFalse(tCharHash.contains('A'));
        
        tCharHash.insertKey('A');
        
        assertTrue(tCharHash.contains('A'));
    }
    
    @Test
    public void testForEach() {
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };
        
        assertTrue(tCharHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('A');
        
        tCharHash.removeAt(0);
        
        assertFalse(tCharHash.contains('A'));
    }
    
    @Test
    public void testIndex() {
        tCharHash.insertKey('A');
        
        assertEquals(0, tCharHash.index('A'));
        assertEquals(-1, tCharHash.index('B'));
    }
    
    @Test
    public void testIndexRehashed() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        
        assertEquals(1, tCharHash.indexRehashed('B', 0, 0, (byte)1));
        assertEquals(-1, tCharHash.indexRehashed('D', 0, 0, (byte)1));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, tCharHash.insertKey('A'));
        
        assertEquals(-1, tCharHash.insertKey('A'));
    }
    
    @Test
    public void testInsertKeyRehash() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        
        assertEquals(1, tCharHash.insertKeyRehash('B', 0, 0, (byte)1));
        
        assertEquals(2, tCharHash.insertKeyRehash('C', 0, 0, (byte)2));
    }
    
    @Test
    public void testInsertKeyAt() {
        tCharHash.insertKeyAt(0, 'A');
        
        assertTrue(tCharHash.contains('A'));
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testGetNoEntryValue() {
        TCharHash hash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        assertFalse(hash.contains('A'));
        hash.insertKey('A');
        assertTrue(hash.contains('A'));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        hash.removeAt(1);
        
        assertFalse(hash.contains('B'));
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        assertEquals(0, hash.index('A'));
        assertEquals(1, hash.index('B'));
        assertEquals(2, hash.index('C'));
        assertEquals(-1, hash.index('D'));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        assertEquals(2, hash.indexRehashed('A', 0, 123456, (byte) 1));
        assertEquals(-1, hash.indexRehashed('B', 1, 654321, (byte) 0));
        assertEquals(0, hash.indexRehashed('C', 2, 987654, (byte) 2));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        assertEquals(0, hash.insertKey('A'));
        assertEquals(1, hash.insertKey('B'));
        assertEquals(2, hash.insertKey('C'));
        assertEquals(-1, hash.insertKey('A'));
        assertEquals(-2, hash.insertKey('B'));
        assertEquals(-3, hash.insertKey('C'));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        assertEquals(0, hash.insertKeyRehash('A', 0, 123456, (byte) 1));
        assertEquals(1, hash.insertKeyRehash('B', 1, 654321, (byte) 1));
        assertEquals(2, hash.insertKeyRehash('D', 2, 987654, (byte) 2));
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        hash.insertKeyAt(0, 'A');
        hash.insertKeyAt(1, 'B');
        hash.insertKeyAt(2, 'C');
        
        assertEquals('A', hash._set[0]);
        assertEquals('B', hash._set[1]);
        assertEquals('C', hash._set[2]);
    }
    
}
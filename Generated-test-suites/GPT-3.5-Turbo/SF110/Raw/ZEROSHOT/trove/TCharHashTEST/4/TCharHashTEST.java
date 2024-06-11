import static org.junit.Assert.*;
import org.junit.*;

public class TCharHashTest {
    
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertTrue(hash.contains('c'));
        assertFalse(hash.contains('d'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        StringBuilder stringBuilder = new StringBuilder();
        
        hash.forEach((value) -> {
            stringBuilder.append(value);
            return true;
        });
        
        assertEquals("abc", stringBuilder.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        hash.removeAt(1);
        
        assertFalse(hash.contains('b'));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertTrue(hash.contains('c'));
        assertFalse(hash.contains('d'));
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        assertEquals(0, hash.index('a'));
        assertEquals(1, hash.index('b'));
        assertEquals(2, hash.index('c'));
        assertEquals(-1, hash.index('d'));
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        assertEquals(0, hash.indexRehashed('a', 0, 15, (byte)1));
        assertEquals(1, hash.indexRehashed('b', 1, 25, (byte)1));
        assertEquals(2, hash.indexRehashed('c', 2, 35, (byte)1));
        assertEquals(-1, hash.indexRehashed('d', 2, 35, (byte)1));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash();
        
        hash.insertKeyRehash('a', 0, 15, (byte)1);
        hash.insertKeyRehash('b', 1, 25, (byte)1);
        hash.insertKeyRehash('c', 2, 35, (byte)1);
        
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertTrue(hash.contains('c'));
        assertFalse(hash.contains('d'));
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        
        hash.insertKeyAt(0, 'a');
        hash.insertKeyAt(1, 'b');
        hash.insertKeyAt(2, 'c');
        
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertTrue(hash.contains('c'));
        assertFalse(hash.contains('d'));
    }
}

import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testDefaultConstructor() {
        TCharHash hash = new TCharHash();
        assertEquals('0', hash.getNoEntryValue());
    }
  
    @Test
    public void testCapacityConstructor() {
        TCharHash hash = new TCharHash(10);
        assertEquals('0', hash.getNoEntryValue());
    }
  
    @Test
    public void testCapacityAndLoadFactorConstructor() {
        TCharHash hash = new TCharHash(10, 0.75f);
        assertEquals('0', hash.getNoEntryValue());
    }
  
    @Test
    public void testAllArgsConstructor() {
        TCharHash hash = new TCharHash(10, 0.75f, '#');
        assertEquals('#', hash.getNoEntryValue());
    }
  
    @Test
    public void testSetUp() {
        TCharHash hash = new TCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
    }
  
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
        assertTrue(hash.contains('C'));
        assertFalse(hash.contains('D'));
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
        assertEquals(1, hash.index('B'));
        assertEquals(-1, hash.index('D'));
    }
  
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
    }
  
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        hash.insertKeyAt(0, 'A');
        hash.insertKeyAt(1, 'B');
        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
    }
}

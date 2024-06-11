import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;

public class TCharHashTest {

    @Test
    public void testConstructor1() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(10, charHash.size());
    }

    @Test
    public void testConstructor3() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(10, charHash.size());
        assertEquals(0.75f, charHash.getLoadFactor(), 0.01);
    }

    @Test
    public void testConstructor4() {
        TCharHash charHash = new TCharHash(10, 0.75f, 'A');
        assertEquals('A', charHash.getNoEntryValue());
    }

    @Test
    public void testSetup() {
        TCharHash charHash = new TCharHash();
        assertEquals(charHash.setUp(15), 15);
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        assertTrue(charHash.contains('A'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        charHash.insertKey('B');
        assertTrue(charHash.forEach(value -> {
            System.out.println("Executing for: " + value);
            return true;
        }));
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        charHash.removeAt(0);
        assertFalse(charHash.contains('A'));
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        assertEquals(0, charHash.index('A'));
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        int index1 = charHash.insertKey('A');
        assertEquals(0, index1);
        
        int index2 = charHash.insertKey('A');
        assertEquals(-1, index2);
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        charHash.insertKey('B');
        int index = charHash.insertKeyRehash('C', 2, 123, (byte) 1);
        assertEquals(2, index);
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash charHash = new TCharHash();
        charHash.insertKeyAt(0, 'A');
        assertTrue(charHash.contains('A'));
    }
}
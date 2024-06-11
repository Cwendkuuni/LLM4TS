import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testConstructor1() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(10, charHash._set.length);
    }

    @Test
    public void testConstructor3() {
        TCharHash charHash = new TCharHash(10, 0.5f);
        assertEquals(10, charHash._set.length);
    }

    @Test
    public void testConstructor4() {
        char no_entry_value = 'A';
        TCharHash charHash = new TCharHash(10, 0.5f, no_entry_value);
        assertEquals(no_entry_value, charHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash();
        assertEquals(16, charHash.setUp(10));
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
        charHash.insertKey('C');
        
        TCharProcedure procedure = value -> {
            System.out.println(value);
            return true;
        };

        assertTrue(charHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        charHash.removeAt(0);
        assertFalse(charHash.contains('A'));
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.insertKey('A'));
        assertEquals(1, charHash.insertKey('B'));
        assertEquals(2, charHash.insertKey('C'));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        assertEquals(3, charHash.insertKey('D'));
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash charHash = new TCharHash();
        charHash.insertKeyAt(0, 'A');
        assertTrue(charHash.contains('A'));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyAtFull() {
        TCharHash charHash = new TCharHash(1, 1.0f);
        charHash.insertKeyAt(0, 'A');
        charHash.insertKeyAt(1, 'B');
    }
}
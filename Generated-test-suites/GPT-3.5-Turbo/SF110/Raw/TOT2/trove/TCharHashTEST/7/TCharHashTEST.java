import static org.junit.Assert.*;
import org.junit.Test;

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
        assertEquals('\0', tCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructor3() {
        TCharHash tCharHash = new TCharHash(10, 0.75f);
        assertNotNull(tCharHash);
        assertEquals('\0', tCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructor4() {
        TCharHash tCharHash = new TCharHash(10, 0.75f, 'x');
        assertNotNull(tCharHash);
        assertEquals('x', tCharHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        assertFalse(tCharHash.contains('a'));
        tCharHash.insertKey('a');
        assertTrue(tCharHash.contains('a'));
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        TCharHash finalTCharHash = tCharHash;
        TCharProcedure charProcedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                assertTrue(value == 'a' || value == 'b');
                return true;
            }
        };
        assertTrue(finalTCharHash.forEach(charProcedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('a'));
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        assertTrue(tCharHash.contains('a'));
    }

    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        assertEquals(0, tCharHash.index('a'));
    }
}
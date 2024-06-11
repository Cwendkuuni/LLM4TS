import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testConstructorDefaultNoEntryValue() {
        TCharHash charHash = new TCharHash();
        assertEquals('\0', charHash.getNoEntryValue());
    }

    @Test
    public void testConstructorCustomNoEntryValue() {
        TCharHash charHash = new TCharHash(10, 0.8f, 'X');
        assertEquals('X', charHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash(10, 0.8f);
        charHash.insertKey('A');
        assertTrue(charHash.contains('A'));
        assertFalse(charHash.contains('B'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash(10, 0.8f);
        charHash.insertKey('A');
        charHash.insertKey('B');
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return value != 'A';
            }
        };
        assertFalse(charHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash(10, 0.8f);
        charHash.insertKey('A');
        charHash.removeAt(0);
        assertFalse(charHash.contains('A'));
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash(10, 0.8f);
        charHash.insertKey('A');
        charHash.insertKey('B');
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));
    }

}
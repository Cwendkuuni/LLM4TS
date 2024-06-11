import static org.junit.Assert.*;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHash_emptyConstructor() {
        TCharHash tCharHash = new TCharHash();

        // Test default values
        assertEquals('\0', tCharHash.getNoEntryValue());
    }

    @Test
    public void testTCharHash_initialCapacityConstructor() {
        int initialCapacity = 10;
        TCharHash tCharHash = new TCharHash(initialCapacity);

        // Test initial capacity
        assertEquals(initialCapacity, tCharHash.size());
    }

    @Test
    public void testTCharHash_initialCapacityAndLoadFactorConstructor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor);

        // Test initial capacity and load factor
        assertEquals(initialCapacity, tCharHash.capacity());
        assertEquals(loadFactor, tCharHash.loadFactor(), 0);
    }

    @Test
    public void testTCharHash_fullConstructor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        char noEntryValue = 'X';
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor, noEntryValue);

        // Test initial capacity, load factor, and no entry value
        assertEquals(initialCapacity, tCharHash.capacity());
        assertEquals(loadFactor, tCharHash.loadFactor(), 0);
        assertEquals(noEntryValue, tCharHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash(3);
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');

        // Test contains existing value
        assertTrue(tCharHash.contains('B'));

        // Test contains non-existing value
        assertFalse(tCharHash.contains('D'));
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash(3);
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');

        // Test forEach with a simple procedure
        TCharProcedure procedure = new TCharProcedure() {
            StringBuilder stringBuilder = new StringBuilder();

            public boolean execute(char value) {
                stringBuilder.append(value);
                return true;
            }

            public String getResult() {
                return stringBuilder.toString();
            }
        };

        assertTrue(tCharHash.forEach(procedure));
        assertEquals("ABC", procedure.getResult());
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash(3);
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');

        int indexToRemove = tCharHash.index('B');

        // Test removeAt
        tCharHash.removeAt(indexToRemove);
        assertFalse(tCharHash.contains('B'));
        assertFalse(tCharHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                return value != 'A' && value != 'C';
            }
        }));
    }

    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash(3);
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');

        // Test index existing value
        assertEquals(2, tCharHash.index('C'));

        // Test index non-existing value
        assertEquals(-1, tCharHash.index('D'));
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash(3);
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');

        // Test insertKey on existing value
        assertEquals(-2, tCharHash.insertKey('B'));

        // Test insertKey on new value
        assertEquals(3, tCharHash.insertKey('D'));
        assertTrue(tCharHash.contains('D'));
    }

}
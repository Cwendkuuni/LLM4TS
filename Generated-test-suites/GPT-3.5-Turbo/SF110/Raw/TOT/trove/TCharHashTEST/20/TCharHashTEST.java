import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testConstructor() {
        // typical use case
        TCharHash charHash = new TCharHash();
        charHash = new TCharHash(10);
        charHash = new TCharHash(10, 0.5f);
        charHash = new TCharHash(10, 0.5f, 'X');

        // error scenario
        try {
            charHash = new TCharHash(-10);
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = createTCharHash();

        // typical use case
        char noEntryValue = charHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
    }

    @Test
    public void testContains() {
        TCharHash charHash = createTCharHash();

        // typical use case
        boolean contains = charHash.contains('A');
        assertTrue(contains);

        contains = charHash.contains('Z');
        assertFalse(contains);
    }

    @Test
    public void testForEach() {
        TCharHash charHash = createTCharHash();

        // typical use case
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };

        contains = charHash.forEach(procedure);
        assertTrue(contains);

        // error scenario
        TCharProcedure errorProcedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                throw new RuntimeException("Error in procedure");
            }
        };

        try {
            charHash.forEach(errorProcedure);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            // expected exception
        }
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = createTCharHash();

        // typical use case
        charHash.removeAt(0);
        assertFalse(charHash.contains('A'));

        // error scenario
        try {
            charHash.removeAt(-1);
            fail("Exception not thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected exception
        }
    }

    @Test
    public void testIndex() {
        TCharHash charHash = createTCharHash();

        // typical use case
        int index = charHash.index('A');
        assertEquals(0, index);

        index = charHash.index('Z');
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = createTCharHash();

        // typical use case
        int index = charHash.insertKey('X');
        assertEquals(2, index);
        assertTrue(charHash.contains('X'));

        // error scenario
        try {
            charHash.insertKey('A');
            fail("Exception not thrown");
        } catch (IllegalStateException e) {
            // expected exception
        }
    }

    private TCharHash createTCharHash() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        return charHash;
    }
}
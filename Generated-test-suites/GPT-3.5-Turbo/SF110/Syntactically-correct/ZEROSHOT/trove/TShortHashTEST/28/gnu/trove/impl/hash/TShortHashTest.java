import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testConstructorNoArguments() {
        TShortHash hash = new TShortHashImpl();

        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TShortHash hash = new TShortHashImpl(20);

        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(20, hash._set.length);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TShortHash hash = new TShortHashImpl(20, 0.5f);

        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(20, hash._set.length);
        assertEquals(0.5f, hash._loadFactor, 0.001);
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        short noEntryValue = -1;
        TShortHash hash = new TShortHashImpl(20, 0.5f, noEntryValue);

        assertEquals(noEntryValue, hash.getNoEntryValue());
        assertEquals(20, hash._set.length);
        assertEquals(0.5f, hash._loadFactor, 0.001);
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHashImpl();
        
        assertEquals(10, hash.setUp(10));
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHashImpl();
        hash._set[0] = 5;

        assertTrue(hash.contains((short) 5));
        assertFalse(hash.contains((short) 10));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHashImpl();
        hash._set[0] = 1;
        hash._set[1] = 2;

        TShortProcedure procedure = new TShortProcedure() {
            int sum = 0;

            public boolean execute(short value) {
                sum += value;
                return true;
            }

            public int getSum() {
                return sum;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(3, procedure.getSum());
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHashImpl();
        hash._set[0] = 5;
        hash._states[0] = 1;

        hash.removeAt(0);

        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash._set[0]);
        assertEquals(0, hash._states[0]);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHashImpl();
        hash._set[0] = 5;
        hash._states[0] = 1;

        assertEquals(0, hash.index((short) 5));
        assertEquals(-1, hash.index((short) 10));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHashImpl();
        hash._set[0] = 5;
        hash._states[0] = 1;

        assertEquals(-1, hash.indexRehashed((short) 10, 0, 0, (byte) 0));
        assertEquals(0, hash.indexRehashed((short) 5, 0, 0, (byte) 1));
        assertEquals(-1, hash.indexRehashed((short) 10, 0, 0, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHashImpl();

        assertEquals(0, hash.insertKey((short) 5));
        assertEquals(-1, hash.insertKey((short) 5));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHashImpl();
        hash._set[0] = 1;
        hash._states[0] = 2;

        assertEquals(1, hash.insertKeyRehash((short) 5, 0, 0, (byte) 2));
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHashImpl();

        hash.insertKeyAt(0, (short) 5);

        assertEquals(5, hash._set[0]);
        assertEquals(1, hash._states[0]);
    }
}

class TShortHashImpl extends TShortHash {}
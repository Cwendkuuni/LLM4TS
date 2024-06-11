import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash(10, 0.75f, (short) -1);
    }

    @Test
    public void testConstructors() {
        TShortHash emptyTShortHash = new TShortHash();
        assertEquals(0, emptyTShortHash.size());

        TShortHash initialCapacityTShortHash = new TShortHash(20);
        assertEquals(0, initialCapacityTShortHash.size());

        TShortHash initialCapacityLoadFactorTShortHash = new TShortHash(30, 0.8f);
        assertEquals(0, initialCapacityLoadFactorTShortHash.size());

        TShortHash allParamsTShortHash = new TShortHash(40, 0.9f, (short) -2);
        assertEquals(0, allParamsTShortHash.size());
        assertEquals(-2, allParamsTShortHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        assertEquals(10, tShortHash.size());
    }

    @Test
    public void testContains() {
        assertFalse(tShortHash.contains((short) 0));

        tShortHash.insertKey((short) 0);
        assertTrue(tShortHash.contains((short) 0));
    }

    @Test
    public void testForEach() {
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return value > 0;
            }
        };

        assertFalse(tShortHash.forEach(procedure));

        tShortHash.insertKey((short) 0);
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);

        assertTrue(tShortHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        tShortHash.insertKey((short) 0);
        assertEquals(1, tShortHash.size());

        tShortHash.removeAt(0);
        assertEquals(0, tShortHash.size());
    }

    @Test
    public void testIndex() {
        assertEquals(-1, tShortHash.index((short) 0));

        tShortHash.insertKey((short) 0);
        assertEquals(0, tShortHash.index((short) 0));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tShortHash.size());

        tShortHash.insertKey((short) 0);
        assertEquals(1, tShortHash.size());

        tShortHash.insertKey((short) -1);
        assertEquals(2, tShortHash.size());

        assertEquals(-1, tShortHash.insertKey((short) -1));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyOverflow() {
        for (short i = 0; i < 10; i++) {
            tShortHash.insertKey(i);
        }

        tShortHash.insertKey((short) 10);
    }

    @Test
    public void testInsertKeyRehash() {
        tShortHash.insertKey((short) 0);
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);

        assertTrue(tShortHash.contains((short) 0));
        assertTrue(tShortHash.contains((short) 10));
        assertTrue(tShortHash.contains((short) 20));
    }

    @Test
    public void testInsertKeyAt() {
        tShortHash.insertKeyAt(0, (short) 0);
        assertTrue(tShortHash.contains((short) 0));
    }

}
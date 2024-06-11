import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash();
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        tShortHash.add(1);
        tShortHash.add(2);
        tShortHash.add(3);

        assertTrue(tShortHash.contains(2));
        assertFalse(tShortHash.contains(5));
    }

    @Test
    public void testForEach() {
        tShortHash.add(1);
        tShortHash.add(2);
        tShortHash.add(3);

        TShortHashIterator iterator = new TShortHashIterator(tShortHash);
        while (iterator.hasNext()) {
            short value = iterator.next();
            assertTrue(value >= 1 && value <= 3);
        }
    }

    @Test
    public void testInsertKey() {
        int index = tShortHash.insertKey((short) 100);
        assertEquals(0, index);

        index = tShortHash.insertKey((short) 100);
        assertEquals(-1, index);

        for (int i = 1; i <= 1000; i++) {
            tShortHash.insertKey((short) i);
        }

        index = tShortHash.insertKey((short) 1001);
        assertTrue(index >= 0);

    }

    @Test
    public void testRemoveAt() {
        tShortHash.add(1);
        tShortHash.add(2);
        tShortHash.add(3);

        tShortHash.removeAt(1);

        assertFalse(tShortHash.contains(2));
        assertTrue(tShortHash.contains(1));
        assertTrue(tShortHash.contains(3));
    }

    @Test
    public void testEdgeCases() {
        tShortHash.add(0);
        tShortHash.add(1);
        tShortHash.add(2);
        tShortHash.add(Short.MAX_VALUE);
        tShortHash.add(Short.MIN_VALUE);

        assertTrue(tShortHash.contains(0));
        assertTrue(tShortHash.contains(1));
        assertTrue(tShortHash.contains(2));
        assertTrue(tShortHash.contains(Short.MAX_VALUE));
        assertTrue(tShortHash.contains(Short.MIN_VALUE));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        for (int i = 0; i < tShortHash.capacity(); i++) {
            tShortHash.insertKey((short) i);
        }

        tShortHash.insertKey((short) 100);
    }

}
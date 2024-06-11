import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testConstructor() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        assertTrue(hash.contains((short) 5));
        assertFalse(hash.contains((short) 10));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        hash.insertKey((short) 10);
        hash.insertKey((short) 15);
        TIntProcedure procedure = new TIntProcedure() {
            public boolean execute(int value) {
                System.out.println(value);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        hash.insertKey((short) 10);
        assertEquals(1, hash.index((short) 10));
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        hash.removeAt(0);
        assertFalse(hash.contains((short) 5));
    }
}
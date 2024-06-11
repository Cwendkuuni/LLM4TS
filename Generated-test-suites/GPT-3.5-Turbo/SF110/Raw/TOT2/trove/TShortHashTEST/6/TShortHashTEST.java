import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        assertTrue(hash.contains((short) 5));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);

        TShortProcedure procedure = new TShortProcedure() {
            boolean found = false;

            @Override
            public boolean execute(short value) {
                if (value == 20) {
                    found = true;
                    return false;
                }
                return true;
            }
        };

        assertFalse(hash.forEach(procedure));
        assertTrue(procedure.found);
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 15);
        assertTrue(hash.contains((short) 15));
    }

}
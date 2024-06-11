import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {

    TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash(10, 0.8f, (short) -1);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(-1, tShortHash.getNoEntryValue());
    }

    // Edge case: testing for negative no_entry_value
    @Test
    public void testNegativeNoEntryValue() {
        TShortHash tShortHashNeg = new TShortHash(10, 0.8f, (short) -100);
        assertEquals(-100, tShortHashNeg.getNoEntryValue());
    }

    // Exception handling: testing for IllegalStateException when key set is full
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TShortHash tShortHashFull = new TShortHash(1, 0.5f, (short) -1);
        tShortHashFull.insertKey((short) 1);
        tShortHashFull.insertKey((short) 2);
    }
}
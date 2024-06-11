import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    //Test case for no_entry_value getter
    @Test
    public void testGetNoEntryValue() {
        TShortHash tShortHash = new TShortHash(10);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    //Test case for contains method with value present
    @Test
    public void testContainsWithValuePresent() {
        TShortHash tShortHash = new TShortHash(10);
        tShortHash.insertKey((short) 5);
        assertTrue(tShortHash.contains((short) 5));
    }

    //Test case for contains method with value not present
    @Test
    public void testContainsWithValueNotPresent() {
        TShortHash tShortHash = new TShortHash(10);
        assertFalse(tShortHash.contains((short) 5));
    }

    //Test case for forEach method with procedure returning true
    @Test
    public void testForEachWithProcedureReturningTrue() {
        TShortHash tShortHash = new TShortHash(10);
        tShortHash.insertKey((short) 5);
        assertTrue(tShortHash.forEach((value) -> {
            // Procedure returning true
            return true;
        }));
    }

    //Test case for forEach method with procedure returning false
    @Test
    public void testForEachWithProcedureReturningFalse() {
        TShortHash tShortHash = new TShortHash(10);
        tShortHash.insertKey((short) 5);
        assertFalse(tShortHash.forEach((value) -> {
            // Procedure returning false
            return false;
        }));
    }

    //Test case for removeAt method
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash(10);
        tShortHash.insertKey((short) 5);
        tShortHash.removeAt(0);
        assertFalse(tShortHash.contains((short) 5));
    }

    //Test case for index method with value present at index
    @Test
    public void testIndexWithValuePresent() {
        TShortHash tShortHash = new TShortHash(10);
        tShortHash.insertKey((short) 5);
        assertEquals(0, tShortHash.index((short) 5));
    }

    //Test case for index method with value not present
    @Test
    public void testIndexWithValueNotPresent() {
        TShortHash tShortHash = new TShortHash(10);
        assertEquals(-1, tShortHash.index((short) 5));
    }

    //Test case for insertKey method with free slot available
    @Test
    public void testInsertKeyWithFreeSlotAvailable() {
        TShortHash tShortHash = new TShortHash(10);
        assertEquals(0, tShortHash.insertKey((short) 5));
        assertTrue(tShortHash.contains((short) 5));
    }

    //Test case for insertKey method with no free slot available
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithNoFreeSlotAvailable() {
        TShortHash tShortHash = new TShortHash(1);
        tShortHash.insertKey((short) 5);
        tShortHash.insertKey((short) 6);
    }

}
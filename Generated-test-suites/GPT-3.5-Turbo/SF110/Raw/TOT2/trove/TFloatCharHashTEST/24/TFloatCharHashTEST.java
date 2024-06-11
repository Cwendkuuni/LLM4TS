import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    TFloatCharHash testHash = new TFloatCharHash();

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, testHash.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', testHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        assertFalse(testHash.contains(0.5f));
        testHash.insertKey(0.5f);
        assertTrue(testHash.contains(0.5f));
    }

    @Test
    public void testForEach() {
        // To be implemented
    }

    @Test
    public void testRemoveAt() {
        // To be implemented
    }

    @Test
    public void testIndex() {
        // To be implemented
    }

    @Test
    public void testInsertKey() {
        // To be implemented
    }

    @Test
    public void testXinsertKey() {
        // To be implemented
    }

    @Test
    public void testWriteExternal() {
        // To be implemented
    }

    @Test
    public void testReadExternal() {
        // To be implemented
    }

}
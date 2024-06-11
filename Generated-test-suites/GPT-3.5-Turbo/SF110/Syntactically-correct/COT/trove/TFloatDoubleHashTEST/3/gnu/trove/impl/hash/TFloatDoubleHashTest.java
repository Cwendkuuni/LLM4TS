
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash tFloatDoubleHash;

    @Before
    public void setUp() {
        tFloatDoubleHash = new TFloatDoubleHash();
    }

    @Test
    public void testContains() {
        // Basic test case
        assertFalse(tFloatDoubleHash.contains(0.0f));
    }

    @Test
    public void testForEach() {
        // Basic test case with custom procedure
        assertFalse(tFloatDoubleHash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float val) {
                return false;
            }
        }));
    }

    @Test
    public void testRemoveAt() {
        // Basic test case
        tFloatDoubleHash.removeAt(0);
    }

    @Test
    public void testIndex() {
        // Basic test case
        assertEquals(-1, tFloatDoubleHash.index(0.0f));
    }

    @Test
    public void testInsertKey() {
        // Basic test case
        assertEquals(0, tFloatDoubleHash.insertKey(0.0f));
    }

    @Test
    public void testXinsertKey() {
        // Basic test case
        assertEquals(0, tFloatDoubleHash.XinsertKey(0.0f));
    }

    @Test
    public void testGetNoEntryKey() {
        // Basic test case
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        // Basic test case
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testWriteExternal() {
        // TODO: Implement this test case
    }

    @Test
    public void testReadExternal() {
        // TODO: Implement this test case
    }
}

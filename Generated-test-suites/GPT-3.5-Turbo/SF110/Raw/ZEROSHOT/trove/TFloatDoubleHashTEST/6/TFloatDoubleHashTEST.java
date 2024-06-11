import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash tFloatDoubleHash;

    @Before
    public void setUp() {
        tFloatDoubleHash = new TFloatDoubleHash(10, 0.5f, 0.0f, 0.0);
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testContains() {
        assertFalse(tFloatDoubleHash.contains(2.5f));
        tFloatDoubleHash.insertKey(2.5f);
        assertTrue(tFloatDoubleHash.contains(2.5f));
    }

    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                // do something
                return true;
            }
        };
        assertTrue(tFloatDoubleHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        tFloatDoubleHash.insertKey(2.5f);
        tFloatDoubleHash.insertKey(4.3f);
        tFloatDoubleHash.insertKey(1.1f);
        assertTrue(tFloatDoubleHash.contains(4.3f));
        tFloatDoubleHash.removeAt(1);
        assertFalse(tFloatDoubleHash.contains(4.3f));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tFloatDoubleHash.insertKey(2.5f));
        assertEquals(-1, tFloatDoubleHash.insertKey(2.5f));
        assertEquals(1, tFloatDoubleHash.insertKey(4.3f));
    }

    @Test
    public void testWriteExternal() {
        // writeExternal test logic
    }

    @Test
    public void testReadExternal() {
        // readExternal test logic
    }

}
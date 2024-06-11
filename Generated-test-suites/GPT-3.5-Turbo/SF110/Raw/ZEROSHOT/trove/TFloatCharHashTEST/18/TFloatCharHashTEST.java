import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.1);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        assertFalse(hash.contains(1.0f));
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0;

            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(6.0f, procedure.sum, 0.1);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        hash.removeAt(1);

        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(-1, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(-1, hash.XinsertKey(1.0f));
        assertEquals(1, hash.XinsertKey(2.0f));
    }
}
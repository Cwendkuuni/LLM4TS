import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        assertFalse(hash.contains(5.0f));
        hash.insertKey(5.0f);
        assertTrue(hash.contains(5.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        StringBuilder result = new StringBuilder();
        TFloatProcedure procedure = value -> {
            result.append(value).append(",");
            return true;
        };
        hash.forEach(procedure);
        assertEquals("2.0,3.0,4.0,", result.toString());
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.insertKey(2.0f));
        assertEquals(-2, hash.insertKey(2.0f)); // duplicate key
    }

    // Add more test cases for other methods as needed

}
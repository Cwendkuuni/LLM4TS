package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float expected = 0.0f;
        float result = hash.getNoEntryKey();
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        char expected = '\0';
        char result = hash.getNoEntryValue();
        assertEquals(expected, result);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.1f);
        boolean result = hash.contains(1.1f);
        assertTrue(result);
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.1f);
        hash.insertKey(2.2f);
        hash.insertKey(3.3f);
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                System.out.println(value);
                return true;
            }
        };
        boolean result = hash.forEach(procedure);
        assertTrue(result);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.1f);
        hash.insertKey(2.2f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.1f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.1f);
        int expected = 0;
        int result = hash.index(1.1f);
        assertEquals(expected, result);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int result = hash.insertKey(1.1f);
        int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int result = hash.XinsertKey(1.1f);
        int expected = 0;
        assertEquals(expected, result);
    }

}
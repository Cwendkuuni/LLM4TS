package gnu.trove.impl.hash;

import org.junit.*;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        float result = tFloatDoubleHash.getNoEntryKey();
        assertEquals(0.0f, result, 0.001);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        double result = tFloatDoubleHash.getNoEntryValue();
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        boolean result = tFloatDoubleHash.contains(5.0f);
        assertFalse(result);
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        boolean result = tFloatDoubleHash.forEach(new TFloatProcedure() {
            public boolean execute(float val) {
                return true;
            }
        });
        assertTrue(result);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.removeAt(0);
        // Check if the removeAt method throws any exception here
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        int result = tFloatDoubleHash.index(5.0f);
        assertEquals(-1, result);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        int result = tFloatDoubleHash.insertKey(5.0f);
        assertEquals(0, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKeyRehash(5.0f, 0, 12345, (byte)0);
        // Check if the insertKeyRehash method throws IllegalStateException when key set is full
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        int result = tFloatDoubleHash.XinsertKey(5.0f);
        assertEquals(0, result);
    }

}
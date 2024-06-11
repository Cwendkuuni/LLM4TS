
package gnu.trove.impl.hash;

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
    public void testConstructor() {
        assertNotNull(tFloatDoubleHash);
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.001);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.001);
    }
    
    @Test
    public void testContains() {
        assertFalse(tFloatDoubleHash.contains(0.0f));
    }
    
    @Test
    public void testForEach() {
        assertTrue(tFloatDoubleHash.forEach(val -> true));
    }
    
    @Test
    public void testRemoveAt() {
        tFloatDoubleHash.removeAt(0);
        assertEquals(0.0f, tFloatDoubleHash._set[0], 0.001);
    }
    
    @Test
    public void testIndex() {
        assertEquals(-1, tFloatDoubleHash.index(0.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        assertEquals(-1, tFloatDoubleHash.indexRehashed(0.0f, 0, 0, (byte)0));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, tFloatDoubleHash.insertKey(0.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        assertEquals(0, tFloatDoubleHash.insertKeyRehash(0.0f, 0, 0, (byte)0));
    }
    
    @Test
    public void testInsertKeyAt() {
        tFloatDoubleHash.insertKeyAt(0, 0.0f);
        assertEquals(0.0f, tFloatDoubleHash._set[0], 0.001);
        assertEquals((byte)1, tFloatDoubleHash._states[0]);
    }
    
    @Test
    public void testXinsertKey() {
        assertEquals(0, tFloatDoubleHash.XinsertKey(0.0f));
    }
    
    @Test
    public void testWriteExternal() {
        // TODO: test writeExternal() method
    }
    
    @Test
    public void testReadExternal() {
        // TODO: test readExternal() method
    }
    
}

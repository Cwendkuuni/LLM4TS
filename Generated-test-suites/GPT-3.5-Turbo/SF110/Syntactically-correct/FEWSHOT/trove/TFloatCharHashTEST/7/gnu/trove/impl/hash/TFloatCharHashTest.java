package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {
  
    private TFloatCharHash hash;
  
    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }
  
    @Test
    public void testContains() {
        assertFalse(hash.contains(0.0f));
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testForEach() {
        assertTrue(hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                return false;
            }
        }));
    }
    
    @Test
    public void testRemoveAt() {
        hash.removeAt(0);
    }
    
    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(0.0f));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(0.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        assertEquals(-1, hash.indexRehashed(0.0f, 0, 0, (byte) 0));
    }
    
    @Test
    public void testInsertKeyRehash() {
        assertEquals(0, hash.insertKeyRehash(0.0f, 0, 0, (byte) 0));
    }
    
    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 0.0f);
    }
  
    @Test
    public void testWriteExternal() {
      // Test implementation not provided
    }
  
    @Test
    public void testReadExternal() {
      // Test implementation not provided
    }
}
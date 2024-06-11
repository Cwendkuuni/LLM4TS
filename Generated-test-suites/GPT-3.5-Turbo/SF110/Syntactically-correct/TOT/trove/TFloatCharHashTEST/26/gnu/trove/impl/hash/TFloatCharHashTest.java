import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {
    
    @Test
    public void testConstructor1() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor2() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor3() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor4() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.5f, 'a');
        assertNotNull(hash);
        assertEquals(1.5f, hash.getNoEntryKey(), 0.0001);
        assertEquals('a', hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertNotNull(hash._set);
    }
    
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        float key = 1.5f;
        hash.insertKey(key);
        assertTrue(hash.contains(key));

        float key2 = 2.5f;
        assertFalse(hash.contains(key2));
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        float key = 1.5f;
        char value = 'a';
        hash.insertKey(key);
        hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float val) {
                assertEquals(key, val, 0.0001);
                return true;
            }
        });
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        float key = 1.5f;
        hash.insertKey(key);
        hash.removeAt(0);
        assertFalse(hash.contains(key));
    }
    
    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        float key = 1.5f;
        hash.insertKey(key);
        int index = hash.index(key);
        assertEquals(0, index);
        
        float key2 = 2.5f;
        int index2 = hash.index(key2);
        assertEquals(-1, index2);
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float key = 1.5f;
        int index = hash.insertKey(key);
        assertEquals(0, index);
        assertTrue(hash.contains(key));
        
        float key2 = 2.5f;
        int index2 = hash.insertKey(key2);
        assertEquals(1, index2);
        assertTrue(hash.contains(key2));
        
        int index3 = hash.insertKey(key);
        assertEquals(-1, index3);
    }
    
    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float key = 1.5f;
        int index = hash.XinsertKey(key);
        assertEquals(0, index);
        assertTrue(hash.contains(key));
        
        float key2 = 2.5f;
        int index2 = hash.XinsertKey(key2);
        assertEquals(1, index2);
        assertTrue(hash.contains(key2));
        
        int index3 = hash.XinsertKey(key);
        assertEquals(-1, index3);
    }
    
    @Test
    public void testWriteExternal() {
        try {
            TFloatCharHash hash = new TFloatCharHash();
            hash.writeExternal(null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testReadExternal() {
        try {
            TFloatCharHash hash = new TFloatCharHash();
            hash.readExternal(null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Exception is expected
        } catch (ClassNotFoundException e) {
            fail("Expected ClassNotFoundException to be thrown");
        }
    }
    
}
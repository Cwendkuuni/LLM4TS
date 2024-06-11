package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {
    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10, 0.75f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10, 0.75f, 1.0f, 'a');
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('a', hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        int capacity = hash.setUp(10);
        
        assertEquals(10, capacity);
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        assertFalse(hash.contains(2.5f));
        
        hash.insertKey(2.5f);
        assertTrue(hash.contains(2.5f));
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            float result = 0.0f;
            
            public boolean execute(float value) {
                result += value;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(6.0f, procedure.result, 0.0);
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        hash.removeAt(1);
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }
    
    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        assertEquals(0, hash.index(2.0f));
        assertEquals(1, hash.index(3.0f));
        assertEquals(-1, hash.index(4.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        assertEquals(-1, hash.indexRehashed(4.0f, 0, 0, (byte) 0));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        
        assertEquals(0, hash.insertKey(2.0f));
        assertEquals(-1, hash.insertKey(2.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        
        assertEquals(-1, hash.insertKeyRehash(2.0f, 0, 0, (byte) 0));
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 2.0f);
        
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        
        assertEquals(0, hash.XinsertKey(2.0f));
        assertEquals(-1, hash.XinsertKey(2.0f));
    }
    
    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            
            hash.writeExternal(oos);
            oos.flush();
            byte[] byteArray = baos.toByteArray();
            
            assertEquals(0, byteArray[0]);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testReadExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        try {
            byte[] byteArray = new byte[]{0};
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            ObjectInputStream ois = new ObjectInputStream(bais);
            
            hash.readExternal(ois);
            
            assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
            assertEquals('\0', hash.getNoEntryValue());
            
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
        }
    }
}
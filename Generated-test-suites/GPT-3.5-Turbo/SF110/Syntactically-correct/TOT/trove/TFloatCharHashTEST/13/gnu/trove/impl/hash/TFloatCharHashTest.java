import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructor1() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor2() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash.size());
    }
    
    @Test
    public void testConstructor3() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.8f);
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash.size());
    }
    
    @Test
    public void testConstructor4() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.8f, 1.5f, 'X');
        assertNotNull(hash);
        assertEquals(1.5f, hash.getNoEntryKey(), 0);
        assertEquals('X', hash.getNoEntryValue());
        assertEquals(10, hash.size());
    }
    
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        final StringBuilder sb = new StringBuilder();
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                sb.append(value).append(",");
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals("1.0,2.0,", sb.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        assertEquals(10, hash.size());
        hash.removeAt(0);
        assertEquals(9, hash.size());
        assertFalse(hash.contains(1.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(5, hash.size());
        
        assertEquals(1, hash.insertKey(2.0f));
        assertEquals(5, hash.size());
        
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(5, hash.size());
        
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TFloatCharHash hash = new TFloatCharHash(1);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
    }
    
    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(hash.insertKey(1.0f), hash.XinsertKey(1.0f));
    }
    
    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash(5, 0.8f, 1.5f, 'X');
        hash.insertKey(1.0f);
        
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(baos);
            hash.writeExternal(out);
            out.flush();
            byte[] byteArray = baos.toByteArray();
            
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            ObjectInput in = new ObjectInputStream(bais);
            TFloatCharHash newHash = new TFloatCharHash();
            newHash.readExternal(in);
            
            assertEquals(1, newHash.size());
            assertEquals(1.0f, newHash.getNoEntryKey(), 0);
            assertEquals('X', newHash.getNoEntryValue());
            assertTrue(newHash.contains(1.0f));
        } catch (IOException e) {
            fail("IOException occurred during test: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException occurred during test: " + e.getMessage());
        }
    }
    
}
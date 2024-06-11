
import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {
    
    @Test
    public void testConstructor1() {
        TFloatCharHash hash = new TFloatCharHash();
        
        // Test initial capacity
        assertEquals(10, hash._set.length);
        
        // Test no entry key and value
        assertEquals(0.0f, hash.no_entry_key, 0.0);
        assertEquals('\0', hash.no_entry_value);
    }
    
    @Test
    public void testConstructor2() {
        TFloatCharHash hash = new TFloatCharHash(20);
        
        // Test initial capacity
        assertEquals(20, hash._set.length);
        
        // Test no entry key and value
        assertEquals(0.0f, hash.no_entry_key, 0.0);
        assertEquals('\0', hash.no_entry_value);
    }
    
    @Test
    public void testConstructor3() {
        TFloatCharHash hash = new TFloatCharHash(20, 0.5f);
        
        // Test initial capacity
        assertEquals(20, hash._set.length);
        
        // Test no entry key and value
        assertEquals(0.0f, hash.no_entry_key, 0.0);
        assertEquals('\0', hash.no_entry_value);
    }
    
    @Test
    public void testConstructor4() {
        TFloatCharHash hash = new TFloatCharHash(20, 0.5f, 1.0f, 'X');
        
        // Test initial capacity
        assertEquals(20, hash._set.length);
        
        // Test no entry key and value
        assertEquals(1.0f, hash.no_entry_key, 0.0);
        assertEquals('X', hash.no_entry_value);
    }
    
    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
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
        
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                if (value == 1.0f) {
                    return false;
                }
                return true;
            }
        };
        
        assertFalse(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        hash.removeAt(0);
        
        assertFalse(hash.contains(1.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        
        index = hash.insertKey(2.0f);
        assertEquals(1, index);
    }
    
    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        try {
            FileOutputStream fileOut = new FileOutputStream("test.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            hash.writeExternal(out);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            FileInputStream fileIn = new FileInputStream("test.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            TFloatCharHash hash2 = new TFloatCharHash();
            hash2.readExternal(in);
            in.close();
            fileIn.close();
            
            assertTrue(hash2.contains(1.0f));
            assertTrue(hash2.contains(2.0f));
            
            // Test no entry key and value
            assertEquals(0.0f, hash2.no_entry_key, 0.0);
            assertEquals('\0', hash2.no_entry_value);
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

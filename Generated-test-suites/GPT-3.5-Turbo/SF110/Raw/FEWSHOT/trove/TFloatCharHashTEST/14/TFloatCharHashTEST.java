package gnu.trove.impl.hash;
import static org.junit.Assert.*;
import org.junit.Test;


public class TFloatCharHashTest {
    
    // Testing the constructor TFloatCharHash()
    @Test
    public void testConstructor1() {
        TFloatCharHash hash = new TFloatCharHash();
        
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    // Testing the constructor TFloatCharHash(int initialCapacity)
    @Test
    public void testConstructor2() {
        TFloatCharHash hash = new TFloatCharHash(10);
        
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash.capacity());
    }
    
    // Testing the constructor TFloatCharHash(int initialCapacity, float loadFactor)
    @Test
    public void testConstructor3() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f);
        
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash.capacity());
    }
    
    // Testing the constructor TFloatCharHash(int initialCapacity, float loadFactor, float no_entry_key, char no_entry_value)
    @Test
    public void testConstructor4() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f, 1.0f, 'a');
        
        assertEquals(1.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('a', hash.getNoEntryValue());
        assertEquals(10, hash.capacity());
    }
    
    // Testing the method contains(float val)
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(10);
        
        assertFalse(hash.contains(2.3f));
        
        hash.insertKey(2.3f);
        assertTrue(hash.contains(2.3f));
    }
    
    // Testing the method forEach(TFloatProcedure procedure)
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(10);
        
        hash.insertKey(2.3f);
        hash.insertKey(4.5f);
        hash.insertKey(6.7f);
        
        final float[] result = new float[3];
        final int[] counter = new int[1];
        
        hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                result[counter[0]] = value;
                counter[0]++;
                return true;
            }
        });
        
        assertEquals(2.3f, result[0], 0.001);
        assertEquals(4.5f, result[1], 0.001);
        assertEquals(6.7f, result[2], 0.001);
    }
    
    // Testing the method removeAt(int index)
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        
        hash.insertKey(2.3f);
        hash.insertKey(4.5f);
        hash.insertKey(6.7f);
        
        hash.removeAt(1);
        
        assertFalse(hash.contains(4.5f));
    }
    
    // Testing the method index(float key)
    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(10);
        
        hash.insertKey(2.3f);
        hash.insertKey(4.5f);
        hash.insertKey(6.7f);
        
        assertEquals(1, hash.index(4.5f));
        assertEquals(-1, hash.index(8.9f));
    }
    
    // Testing the method insertKey(float val)
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10);
        
        hash.insertKey(2.3f);
        hash.insertKey(4.5f);
        
        assertTrue(hash.contains(2.3f));
        assertTrue(hash.contains(4.5f));
    }
    
    // Testing the method XinsertKey(float key)
    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10);
        
        hash.insertKey(2.3f);
        hash.insertKey(4.5f);
        
        assertTrue(hash.contains(2.3f));
        assertTrue(hash.contains(4.5f));
    }
    
    // Testing the method writeExternal(ObjectOutput out)
    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash(10);
        
        hash.insertKey(2.3f);
        hash.insertKey(4.5f);
        
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
            hash.writeExternal(oos);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Write test case here
    }
    
    // Testing the method readExternal(ObjectInput in)
    @Test
    public void testReadExternal() {
        TFloatCharHash hash = new TFloatCharHash(10);
        
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream("test".getBytes()));
            hash.readExternal(ois);
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        // Write test case here
    }
    
}
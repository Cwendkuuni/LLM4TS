import org.junit.*;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor1() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.001);
    }
    
    @Test
    public void testConstructor2() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.001);
        // Test initial capacity
        assertEquals(10, hash._set.length);
        // Test initial size of states
        assertEquals(10, hash._states.length);
    }
    
    @Test
    public void testConstructor3() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.001);
        // Test initial capacity
        assertEquals(10, hash._set.length);
        // Test initial size of states
        assertEquals(10, hash._states.length);
    }
    
    @Test
    public void testConstructor4() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);
        assertEquals(1.0f, hash.getNoEntryKey(), 0.001);
        assertEquals(2.0, hash.getNoEntryValue(), 0.001);
        // Test initial capacity
        assertEquals(10, hash._set.length);
        // Test initial size of states
        assertEquals(10, hash._states.length);
    }
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key = 1.0f;
        double value = 2.0;
        hash.insertKey(key);
        assertTrue(hash.contains(key));
        assertFalse(hash.contains(0.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key1 = 1.0f;
        double value1 = 2.0;
        float key2 = 3.0f;
        double value2 = 4.0;
        float key3 = 5.0f;
        double value3 = 6.0;
        hash.insertKey(key1);
        hash.insertKey(key2);
        hash.insertKey(key3);
        
        // Test procedure that returns true
        TFloatProcedure procedure1 = new TFloatProcedure() {
            @Override
            public boolean execute(float val) {
                return true;
            }
        };
        assertTrue(hash.forEach(procedure1));
        
        // Test procedure that returns false
        TFloatProcedure procedure2 = new TFloatProcedure() {
            @Override
            public boolean execute(float val) {
                return false;
            }
        };
        assertFalse(hash.forEach(procedure2));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key = 1.0f;
        double value = 2.0;
        hash.insertKey(key);
        hash.removeAt(0);
        assertFalse(hash.contains(key));
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key1 = 1.0f;
        double value1 = 2.0;
        float key2 = 3.0f;
        double value2 = 4.0;
        hash.insertKey(key1);
        hash.insertKey(key2);
        
        assertEquals(0, hash.index(key1));
        assertEquals(1, hash.index(key2));
        assertEquals(-1, hash.index(0.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key = 1.0f;
        double value = 2.0;
        int index = hash.insertKey(key);
        assertTrue(hash.contains(key));
        assertEquals(0, index);
        
        float existingKey = 3.0f;
        int existingIndex = hash.insertKey(existingKey);
        assertFalse(hash.contains(existingKey));
        assertEquals(-1, existingIndex);
    }
    
    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key = 1.0f;
        double value = 2.0;
        hash.insertKey(key);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(hash);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            TFloatDoubleHash deserializedHash = (TFloatDoubleHash) objectInputStream.readObject();
            assertTrue(deserializedHash.contains(key));
            assertEquals(hash.getNoEntryKey(), deserializedHash.getNoEntryKey(), 0.001);
            assertEquals(hash.getNoEntryValue(), deserializedHash.getNoEntryValue(), 0.001);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadExternal() { 
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key = 1.0f;
        double value = 2.0;
        hash.insertKey(key);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(hash);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            TFloatDoubleHash deserializedHash = (TFloatDoubleHash) objectInputStream.readObject();
            assertTrue(deserializedHash.contains(key));
            assertEquals(hash.getNoEntryKey(), deserializedHash.getNoEntryKey(), 0.001);
            assertEquals(hash.getNoEntryValue(), deserializedHash.getNoEntryValue(), 0.001);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
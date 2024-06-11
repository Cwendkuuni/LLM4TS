import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor1() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        assertNotNull(tFloatDoubleHash);
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0);
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
    }
    
    @Test
    public void testConstructor2() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash(10);
        assertNotNull(tFloatDoubleHash);
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0);
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
        // Test capacity
        assertTrue(tFloatDoubleHash._set.length >= 10);
    }
    
    @Test
    public void testConstructor3() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash(10, 0.5f);
        assertNotNull(tFloatDoubleHash);
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0);
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
        // Test capacity
        assertTrue(tFloatDoubleHash._set.length >= 10);
    }
    
    @Test
    public void testConstructor4() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);
        assertNotNull(tFloatDoubleHash);
        assertEquals(1.0f, tFloatDoubleHash.getNoEntryKey(), 0.0);
        assertEquals(2.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
        // Test capacity
        assertTrue(tFloatDoubleHash._set.length >= 10);
    }
    
    @Test
    public void testSetUp() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        int capacity = tFloatDoubleHash.setUp(10);
        assertEquals(10, capacity);
        // Test _set length
        assertTrue(tFloatDoubleHash._set.length >= 10);
    }
    
    @Test
    public void testContains() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        
        // Test contains for existing value
        assertTrue(tFloatDoubleHash.contains(1.0f));
        
        // Test contains for non-existing value
        assertFalse(tFloatDoubleHash.contains(2.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        
        // Test forEach with procedure returning true
        assertTrue(tFloatDoubleHash.forEach(val -> System.out.println(val)));
        
        // Test forEach with procedure returning false
        assertFalse(tFloatDoubleHash.forEach(val -> {
            System.out.println(val);
            return false;
        }));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        
        // Test removeAt
        tFloatDoubleHash.removeAt(0);
        assertFalse(tFloatDoubleHash.contains(1.0f));
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        
        // Test index for existing key
        assertEquals(0, tFloatDoubleHash.index(1.0f));
        
        // Test index for non-existing key
        assertEquals(-1, tFloatDoubleHash.index(2.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        
        // Test indexRehashed for existing key
        assertEquals(0, tFloatDoubleHash.indexRehashed(1.0f, 0, 0, (byte)1));
        
        // Test indexRehashed for non-existing key
        assertEquals(-1, tFloatDoubleHash.indexRehashed(2.0f, 0, 0, (byte)1));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        
        // Test insertKey for new key
        assertEquals(0, tFloatDoubleHash.insertKey(1.0f));
        
        // Test insertKey for existing key
        assertEquals(-1, tFloatDoubleHash.insertKey(1.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        
        // Test insertKeyRehash for new key
        assertEquals(0, tFloatDoubleHash.insertKeyRehash(2.0f, 0, 0, (byte)1));
        
        // Test insertKeyRehash for existing key
        assertEquals(-1, tFloatDoubleHash.insertKeyRehash(1.0f, 0, 0, (byte)1));
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        
        // Test insertKeyAt
        tFloatDoubleHash.insertKeyAt(0, 1.0f);
        assertTrue(tFloatDoubleHash.contains(1.0f));
    }
    
    @Test
    public void testXinsertKey() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        
        // Test XinsertKey for new key
        assertEquals(0, tFloatDoubleHash.XinsertKey(1.0f));
        
        // Test XinsertKey for existing key
        assertEquals(-1, tFloatDoubleHash.XinsertKey(1.0f));
    }
    
    @Test
    public void testWriteExternal() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        // Modify values
        tFloatDoubleHash.no_entry_key = 1.0f;
        tFloatDoubleHash.no_entry_value = 2.0;
        
        // Test writeExternal
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(tFloatDoubleHash);
            objectOutputStream.close();
            
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            TFloatDoubleHash readObject = (TFloatDoubleHash) objectInputStream.readObject();
            objectInputStream.close();
            
            assertEquals(1.0f, readObject.no_entry_key, 0.0);
            assertEquals(2.0, readObject.no_entry_value, 0.0);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadExternal() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        // Modify values
        tFloatDoubleHash.no_entry_key = 1.0f;
        tFloatDoubleHash.no_entry_value = 2.0;
        
        // Test readExternal
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(tFloatDoubleHash);
            objectOutputStream.close();
            
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            TFloatDoubleHash readObject = (TFloatDoubleHash) objectInputStream.readObject();
            objectInputStream.close();
            
            assertEquals(1.0f, readObject.no_entry_key, 0.0);
            assertEquals(2.0, readObject.no_entry_value, 0.0);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
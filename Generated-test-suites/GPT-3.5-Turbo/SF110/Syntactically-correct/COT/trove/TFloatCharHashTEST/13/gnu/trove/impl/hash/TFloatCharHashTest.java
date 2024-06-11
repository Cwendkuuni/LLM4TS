
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {
    
    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        float expectedNoEntryKey = 0.0f;
        char expectedNoEntryValue = '\0';
        assertEquals(expectedNoEntryKey, hash.getNoEntryKey(), 0.0f);
        assertEquals(expectedNoEntryValue, hash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        float key = 2.5f;
        assertFalse(hash.contains(key));
        hash.insertKey(key);
        assertTrue(hash.contains(key));
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        float key = 2.5f;
        char value = 'a';
        hash.insertKey(key);
        assertFalse(hash.forEach((float k) -> {
            assertEquals(key, k, 0.0f);
            return false;
        }));
        assertTrue(hash.forEach((float k) -> {
            assertEquals(key, k, 0.0f);
            return true;
        }));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        float key = 2.5f;
        hash.insertKey(key);
        assertEquals(key, hash._set[0], 0.0f);
        hash.removeAt(0);
        assertEquals(0.0f, hash._set[0], 0.0f);
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float key = 2.5f;
        assertEquals(0, hash.insertKey(key));
        assertTrue(hash.contains(key));
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        float key = 2.5f;
        int index = 0;
        hash.insertKeyAt(index, key);
        assertEquals(key, hash._set[index], 0.0f);
        assertEquals(1, hash._states[index]);
    }
    
    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float key = 2.5f;
        int expectedIndex = -1;
        assertEquals(expectedIndex, hash.XinsertKey(key));
        assertFalse(hash.contains(key));
        hash.insertKey(key);
        assertTrue(hash.contains(key));
        expectedIndex = -1;
        assertEquals(expectedIndex, hash.XinsertKey(key));
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutputStream);
        objectOutputStream.flush();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        assertEquals(0, byteArray[0]);
        // TODO: More assertions on the written data
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash();
        float expectedNoEntryKey = 2.5f;
        char expectedNoEntryValue = 'a';
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutputStream);
        objectOutputStream.flush();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        hash.readExternal(objectInputStream);
        assertEquals(expectedNoEntryKey, hash.getNoEntryKey(), 0.0f);
        assertEquals(expectedNoEntryValue, hash.getNoEntryValue());
        // TODO: More assertions on the read data
    }
    
}

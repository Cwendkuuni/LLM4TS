import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {
    
    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }
    
    @Test
    public void testGetNoEntryKey() {
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0.0001);
    }
    
    @Test
    public void testGetNoEntryValue() {
        double noEntryValue = hash.getNoEntryValue();
        assertEquals(0.0, noEntryValue, 0.0001);
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        int capacity = hash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        float[] set = hash._set;
        assertEquals(initialCapacity, set.length);
    }
    
    @Test
    public void testContains() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        boolean containsExisting = hash.contains(1.0f);
        assertTrue(containsExisting);
        
        boolean containsNonExisting = hash.contains(3.0f);
        assertFalse(containsNonExisting);
    }
    
    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0;
            
            public boolean execute(float value) {
                sum += value;
                return true;
            }
            
            public double getSum() {
                return sum;
            }
        };
        
        hash.forEach(procedure);
        assertEquals(6.0, procedure.getSum(), 0.0001);
    }
    
    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        hash.removeAt(0);
        
        float removedValue = hash._set[0];
        assertEquals(0.0f, removedValue, 0.0001);
    }
    
    @Test
    public void testIndex() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        int indexExisting = hash.index(1.0f);
        assertEquals(0, indexExisting);
        
        int indexNonExisting = hash.index(3.0f);
        assertEquals(-1, indexNonExisting);
    }
    
    @Test
    public void testInsertKey() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        int indexExisting = hash.insertKey(1.0f);
        assertEquals(-1, indexExisting);
        
        int indexNonExisting = hash.insertKey(3.0f);
        assertTrue(indexNonExisting >= 0);
    }
    
    @Test
    public void testXInsertKey() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        int indexExisting = hash.XinsertKey(1.0f);
        assertEquals(-1, indexExisting);
        
        int indexNonExisting = hash.XinsertKey(3.0f);
        assertTrue(indexNonExisting >= 0);
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutputStream);
        
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        assertNotNull(byteArray);
        assertTrue(byteArray.length > 0);
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutputStream);
        
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        
        TFloatDoubleHash newHash = new TFloatDoubleHash();
        newHash.readExternal(objectInputStream);
        
        float noEntryKey = newHash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0.0001);
        
        double noEntryValue = newHash.getNoEntryValue();
        assertEquals(0.0, noEntryValue, 0.0001);
    }
    
}
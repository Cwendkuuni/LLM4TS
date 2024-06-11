import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {
    
    private TFloatCharHash hash;
    
    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(1.0f));
        
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;
            
            public boolean execute(float value) {
                sum += value;
                return true;
            }
            
            public float getSum() {
                return sum;
            }
        };
        
        assertFalse(hash.forEach(procedure));
        assertEquals(0.0f, procedure.getSum(), 0.001);
        
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertTrue(hash.forEach(procedure));
        assertEquals(3.0f, procedure.getSum(), 0.001);
    }
    
    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }
    
    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(1.0f));
        
        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(1.0f));
        assertTrue(hash.contains(1.0f));
        
        assertEquals(-1, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
        
        assertFalse(hash.consumeFreeSlot);
    }
    
    @Test
    public void testXinsertKey() {
        assertEquals(0, hash.XinsertKey(1.0f));
        assertTrue(hash.contains(1.0f));
        
        assertEquals(-1, hash.XinsertKey(1.0f));
        assertEquals(1, hash.XinsertKey(2.0f));
        
        assertFalse(hash.consumeFreeSlot);
    }
    
    @Test
    public void testWriteExternal() {
        hash.insertKey(1.0f);
        
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hash.ser"));
            hash.writeExternal(out);
            out.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("hash.ser"));
            TFloatCharHash readHash = new TFloatCharHash();
            readHash.readExternal(in);
            in.close();
            
            assertTrue(readHash.contains(1.0f));
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
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
    public void testConstructor() {
        assertEquals(0, hash.size());
        assertEquals(0, hash.capacity());
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        hash = new TFloatCharHash(10);
        assertEquals(0, hash.size());
        assertEquals(10, hash.capacity());
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        hash = new TFloatCharHash(10, 0.75f);
        assertEquals(0, hash.size());
        assertEquals(10, hash.capacity());
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryKeyAndNoEntryValue() {
        hash = new TFloatCharHash(10, 0.75f, -1.0f, '$');
        assertEquals(0, hash.size());
        assertEquals(10, hash.capacity());
        assertEquals(-1.0f, hash.getNoEntryKey(), 0);
        assertEquals('$', hash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        hash.insertKey(1.5f);
        assertTrue(hash.contains(1.5f));
        assertFalse(hash.contains(2.5f));
    }
    
    @Test
    public void testForEach() {
        hash.insertKey(1.5f);
        hash.insertKey(2.5f);
        hash.insertKey(3.5f);
        
        final StringBuilder result = new StringBuilder();
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                result.append(value);
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals("1.52.53.5", result.toString());
    }
    
    @Test
    public void testRemoveAt() {
        hash.insertKey(1.5f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.5f));
        assertEquals(0, hash.size());
    }
    
    @Test
    public void testIndex() {
        hash.insertKey(1.5f);
        hash.insertKey(2.5f);
        hash.insertKey(3.5f);
        
        assertEquals(0, hash.index(1.5f));
        assertEquals(1, hash.index(2.5f));
        assertEquals(2, hash.index(3.5f));
        assertEquals(-1, hash.index(4.5f));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(1.5f));
        assertEquals(1, hash.insertKey(2.5f));
        assertEquals(2, hash.insertKey(3.5f));
        assertEquals(-1, hash.insertKey(1.5f));
        
        assertTrue(hash.contains(1.5f));
        assertTrue(hash.contains(2.5f));
        assertTrue(hash.contains(3.5f));
        assertFalse(hash.contains(4.5f));
    }
    
    @Test
    public void testXinsertKey() {
        assertEquals(0, hash.XinsertKey(1.5f));
        assertEquals(1, hash.XinsertKey(2.5f));
        assertEquals(2, hash.XinsertKey(3.5f));
        assertEquals(-1, hash.XinsertKey(1.5f));
        
        assertTrue(hash.contains(1.5f));
        assertTrue(hash.contains(2.5f));
        assertTrue(hash.contains(3.5f));
        assertFalse(hash.contains(4.5f));
    }
    
    @Test
    public void testWriteExternal() {
        hash.insertKey(1.5f);
        hash.insertKey(2.5f);
        
        try {
            FileOutputStream fileOut = new FileOutputStream("test.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            hash.writeExternal(out);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        TFloatCharHash newHash = new TFloatCharHash();
        
        try {
            FileInputStream fileIn = new FileInputStream("test.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            newHash.readExternal(in);
            in.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        
        assertTrue(newHash.contains(1.5f));
        assertTrue(newHash.contains(2.5f));
    }
}
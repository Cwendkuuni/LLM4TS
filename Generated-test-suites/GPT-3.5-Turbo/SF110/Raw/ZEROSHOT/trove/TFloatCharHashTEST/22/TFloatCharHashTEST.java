package gnu.trove.impl.hash;

import gnu.trove.procedure.*;
import gnu.trove.impl.*;
import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;

public class TFloatCharHashTest {
    
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(3.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
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
        
        assertTrue(hash.forEach(procedure));
        assertEquals(3.0f, procedure.getSum(), 0.0f);
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
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(3.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(2, 0.5f);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }
    
    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.XinsertKey(1.0f);
        hash.XinsertKey(2.0f);
        
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        FileOutputStream file = new FileOutputStream("testfile");
        ObjectOutputStream out = new ObjectOutputStream(file);
        
        hash.writeExternal(out);
        out.close();
        
        FileInputStream fileIn = new FileInputStream("testfile");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        
        TFloatCharHash hash2 = new TFloatCharHash();
        hash2.readExternal(in);
        
        assertTrue(hash2.contains(1.0f));
        assertTrue(hash2.contains(2.0f));
        assertFalse(hash2.contains(3.0f));
        
        in.close();
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        FileOutputStream file = new FileOutputStream("testfile");
        ObjectOutputStream out = new ObjectOutputStream(file);
        
        hash.writeExternal(out);
        out.close();
        
        FileInputStream fileIn = new FileInputStream("testfile");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        
        TFloatCharHash hash2 = new TFloatCharHash();
        hash2.readExternal(in);
        
        assertTrue(hash2.contains(1.0f));
        assertTrue(hash2.contains(2.0f));
        assertFalse(hash2.contains(3.0f));
        
        in.close();
    }
    
    @AfterClass
    public static void cleanup() {
        File file = new File("testfile");
        file.delete();
    }
    
}
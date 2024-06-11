import gnu.trove.procedure.*;
import gnu.trove.impl.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {
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
        TFloatProcedure proc = new TFloatProcedure() {
            public boolean execute(float val) {
                System.out.println(val);
                return true;
            }
        };
        assertTrue(hash.forEach(proc));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
    }
    
    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        int index = hash.index(1.0f);
        assertEquals(0, index);
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        int index = hash.indexRehashed(1.0f, 0, 0, (byte)1);
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        int index = hash.insertKeyRehash(1.5f, 1, 1, (byte)1);
        assertEquals(1, index);
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 1.0f);
        assertTrue(hash.contains(1.0f));
    }
    
    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        hash.writeExternal(out);
        out.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bais);
        TFloatCharHash hash2 = new TFloatCharHash();
        hash2.readExternal(in);
        in.close();
        assertTrue(hash2.contains(1.0f));
    }
}
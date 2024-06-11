package gnu.trove.impl.hash;

import gnu.trove.procedure.*;
import gnu.trove.impl.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    
    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
        
        hash = new TFloatDoubleHash(100);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
        
        hash = new TFloatDoubleHash(100, 0.75f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
        
        hash = new TFloatDoubleHash(100, 0.75f, 1.0f, 2.0);
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(2.0, hash.getNoEntryValue(), 0.0001);
    }
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        final StringBuffer buffer = new StringBuffer();
        assertTrue(hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                buffer.append(value);
                return true;
            }
        }));
        
        assertEquals("1.02.0", buffer.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(3.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
    }
    
    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(1, hash.XinsertKey(2.0f));
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 1.0f, 2.0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        hash.writeExternal(oos);
        oos.flush();
        
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        TFloatDoubleHash readHash = new TFloatDoubleHash();
        readHash.readExternal(ois);
        
        assertEquals(1.0f, readHash.getNoEntryKey(), 0.0001);
        assertEquals(2.0, readHash.getNoEntryValue(), 0.0001);
    }
}
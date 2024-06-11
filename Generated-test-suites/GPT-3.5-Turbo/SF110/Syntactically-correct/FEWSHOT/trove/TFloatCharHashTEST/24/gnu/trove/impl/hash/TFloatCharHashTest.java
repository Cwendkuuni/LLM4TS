import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import gnu.trove.impl.hash.HashFunctions;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;

public class TFloatCharHashTest {
    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10, 0.5f, 1.0f, 'a');
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals('a', hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.keySet().add(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.keySet().add(1.0f);
        hash.keySet().add(2.0f);
        hash.keySet().add(3.0f);
        
        final List<Float> values = new ArrayList<>();
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                values.add(value);
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(3, values.size());
        assertTrue(values.contains(1.0f));
        assertTrue(values.contains(2.0f));
        assertTrue(values.contains(3.0f));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.keySet().add(1.0f);
        hash.keySet().add(2.0f);
        hash.keySet().add(3.0f);
        
        hash.removeAt(1);
        assertEquals(2.0f, hash._set[1], 0.0001);
        assertEquals(2, hash.size());
    }
    
    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.keySet().add(1.0f);
        hash.keySet().add(2.0f);
        
        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(3.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.keySet().add(1.0f);
        hash.keySet().add(2.0f);
        
        assertEquals(0, hash.indexRehashed(1.0f, 0, 0, (byte) 1));
        assertEquals(1, hash.indexRehashed(2.0f, 1, 1, (byte) 1));
        assertEquals(-1, hash.indexRehashed(3.0f, 2, 2, (byte) 1));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        assertEquals(1, hash.size());
        assertTrue(hash.contains(1.0f));
        
        hash.insertKey(2.0f);
        assertEquals(2, hash.size());
        assertTrue(hash.contains(2.0f));
        
        hash.insertKey(3.0f);
        assertEquals(3, hash.size());
        assertTrue(hash.contains(3.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyRehash(1.0f, 0, 0, (byte) 1);
        assertEquals(1, hash.size());
        assertTrue(hash.contains(1.0f));
        
        hash.insertKeyRehash(2.0f, 1, 1, (byte) 1);
        assertEquals(2, hash.size());
        assertTrue(hash.contains(2.0f));
        
        hash.insertKeyRehash(3.0f, 2, 2, (byte) 1);
        assertEquals(3, hash.size());
        assertTrue(hash.contains(3.0f));
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, hash._set[0], 0.0001);
        assertEquals(1, hash.size());
        
        hash.insertKeyAt(1, 2.0f);
        assertEquals(2.0f, hash._set[1], 0.0001);
        assertEquals(2, hash.size());
        
        hash.insertKeyAt(2, 3.0f);
        assertEquals(3.0f, hash._set[2], 0.0001);
        assertEquals(3, hash.size());
    }
    
    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.XinsertKey(1.0f);
        assertEquals(1, hash.size());
        assertTrue(hash.contains(1.0f));
        
        hash.XinsertKey(2.0f);
        assertEquals(2, hash.size());
        assertTrue(hash.contains(2.0f));
        
        hash.XinsertKey(3.0f);
        assertEquals(3, hash.size());
        assertTrue(hash.contains(3.0f));
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        hash.writeExternal(out);
        out.flush();
        
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bais);
        
        assertEquals(0, in.readByte());
        assertEquals(1, in.readInt());
        assertEquals(1, in.readFloat(), 0.0001);
        assertEquals(0, in.readChar());
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeByte(0);
        out.writeInt(1);
        out.writeFloat(1.0f);
        out.writeChar(0);
        out.flush();
        
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bais);
        in.readByte();
        hash.readExternal(in);
        
        assertEquals(1.0f, hash._set[0], 0.0001);
        assertEquals(0, hash.size());
    }
}
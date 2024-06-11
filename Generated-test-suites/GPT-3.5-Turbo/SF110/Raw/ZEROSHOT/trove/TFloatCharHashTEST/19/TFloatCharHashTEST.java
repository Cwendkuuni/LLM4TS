import org.junit.Test;
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
        
        hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                System.out.println(value);
                return true;
            }
        });
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        hash.removeAt(0);
        
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
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
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
        assertEquals(0, hash.insertKey(1.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertEquals(2, hash.insertKeyRehash(3.0f, 0, 1, (byte)0));
        assertEquals(0, hash.insertKey(1.0f));
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 1.0f);
        hash.insertKeyAt(1, 2.0f);
        
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(1, hash.XinsertKey(2.0f));
        assertEquals(0, hash.XinsertKey(1.0f));
    }

    @Test
    public void testWriteExternal(ObjectOutput out) throws IOException {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        out.writeByte(0);
        hash.writeExternal(out);
        out.writeFloat(3.0f);
        out.writeChar('\0');
    }

    @Test
    public void testReadExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        in.readByte();
        hash.readExternal(in);

        assertEquals(1.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
    }
}
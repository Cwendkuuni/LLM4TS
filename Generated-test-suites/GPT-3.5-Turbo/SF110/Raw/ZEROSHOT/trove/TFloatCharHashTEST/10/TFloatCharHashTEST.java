import static org.junit.Assert.*;
import org.junit.*;

public class TFloatCharHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001f);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }

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
        
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                System.out.print(value + " ");
                return true;
            }
        };
        
        hash.forEach(procedure);
        // expected output: 1.0 2.0 
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

        int index = hash.index(1.0f);
        assertEquals(0, index);

        index = hash.index(2.0f);
        assertEquals(1, index);

        index = hash.index(3.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int index = hash.indexRehashed(1.0f, 0, 0, (byte)1);
        assertEquals(0, index);

        index = hash.indexRehashed(2.0f, 1, 0, (byte)1);
        assertEquals(1, index);

        index = hash.indexRehashed(3.0f, 2, 0, (byte)0);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));

        index = hash.insertKey(2.0f);
        assertEquals(1, index);
        assertTrue(hash.contains(2.0f));

        index = hash.insertKey(2.0f);
        assertEquals(-2, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(2, 1.0f, 0.0f, '\0');
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f); // throws IllegalStateException
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testXInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));

        index = hash.XinsertKey(2.0f);
        assertEquals(1, index);
        assertTrue(hash.contains(2.0f));

        index = hash.XinsertKey(2.0f);
        assertEquals(-2, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteExternal() throws Exception {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        hash.writeExternal(out);
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bais);
        hash.readExternal(in); // throws IllegalStateException
    }
}
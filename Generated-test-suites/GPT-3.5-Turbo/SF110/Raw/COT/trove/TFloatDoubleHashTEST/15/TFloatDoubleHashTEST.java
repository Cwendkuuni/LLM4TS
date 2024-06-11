import static org.junit.Assert.*;
import org.junit.*;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor1() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructor2() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructor3() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(0.75f, hash.getLoadFactor(), 0.0);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructor4() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 1.0f, 2.0);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(0.75f, hash.getLoadFactor(), 0.0);
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(2.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float val) {
                System.out.println(val);
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();

        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        hash.removeAt(1);

        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();

        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
        assertEquals(2, hash.insertKey(3.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(2, hash.index(3.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(0, hash.indexRehashed(1.0f, 0, 0, (byte) 1));
        assertEquals(1, hash.indexRehashed(2.0f, 1, 1, (byte) 1));
        assertEquals(2, hash.indexRehashed(3.0f, 2, 2, (byte) 1));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();

        hash.insertKeyRehash(1.0f, 0, 0, (byte) 1);

        assertTrue(hash.contains(1.0f));
    }
    
    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash  = new TFloatDoubleHash();
        
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(1, hash.XinsertKey(2.0f));
        assertEquals(2, hash.XinsertKey(3.0f));
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        hash.writeExternal(objectOutputStream);

        // Test code here

        objectOutputStream.close();
        byteArrayOutputStream.close();
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

        hash.writeExternal(objectOutputStream);
        objectOutputStream.flush();

        hash.readExternal(objectInputStream);

        // Test code here

        objectInputStream.close();
        byteArrayOutputStream.close();
    }

}
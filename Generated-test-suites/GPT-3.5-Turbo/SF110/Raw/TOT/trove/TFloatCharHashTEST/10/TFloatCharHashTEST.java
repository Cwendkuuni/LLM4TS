import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructor1() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructor3() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructor4() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 2.0f, 'X');
        assertNotNull(hash);
        assertEquals(2.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('X', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertNotNull(hash._set);
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            int count = 0;
            public boolean execute(float value) {
                count++;
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(3, procedure.count);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertEquals(0, hash.index(2.0f));
        assertEquals(1, hash.index(3.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.insertKey(2.0f));
        assertEquals(1, hash.insertKey(3.0f));
        assertEquals(-2, hash.insertKey(2.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.XinsertKey(2.0f));
        assertEquals(1, hash.XinsertKey(3.0f));
        assertEquals(-2, hash.XinsertKey(2.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testWriteAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        hash.writeExternal(objectOutputStream);
        objectOutputStream.flush();

        byte[] data = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        TFloatCharHash deserializedHash = new TFloatCharHash();
        deserializedHash.readExternal(objectInputStream);

        assertTrue(deserializedHash.contains(2.0f));
        assertTrue(deserializedHash.contains(3.0f));
    }
}
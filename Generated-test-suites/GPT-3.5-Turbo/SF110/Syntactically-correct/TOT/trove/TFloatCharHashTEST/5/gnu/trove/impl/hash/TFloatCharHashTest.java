import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructor1() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructor3() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructor4() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f, 1.2f, 'a');
        assertEquals(1.2f, hash.getNoEntryKey(), 0.0001);
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
        hash.insertKey(1.23f);
        assertTrue(hash.contains(1.23f));
        assertFalse(hash.contains(4.56f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.23f);
        hash.insertKey(4.56f);
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                System.out.println(value);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.23f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.23f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.23f);
        int index = hash.index(1.23f);
        assertEquals(0, index);
        index = hash.index(4.56f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.insertKey(1.23f);
        assertEquals(0, index);
        assertEquals(1.23f, hash._set[0], 0.0001);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.23f);
        hash.insertKey(4.56f);
        int index = hash.index(4.56f);
        assertEquals(1, index);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 1.23f);
        assertEquals(1.23f, hash._set[0], 0.0001);
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.XinsertKey(1.23f);
        assertEquals(0, index);
        assertEquals(1.23f, hash._set[0], 0.0001);
    }

    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.23f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(baos);
            hash.writeExternal(out);
            out.flush();
            byte[] byteArray = baos.toByteArray();
            assertTrue(byteArray.length > 0);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testReadExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.23f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(baos);
            hash.writeExternal(out);
            out.flush();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInput in = new ObjectInputStream(bais);
            hash.readExternal(in);
            assertEquals(1.23f, hash._set[0], 0.0001);
        } catch (IOException | ClassNotFoundException e) {
            fail("Exception should not be thrown");
        }
    }

}
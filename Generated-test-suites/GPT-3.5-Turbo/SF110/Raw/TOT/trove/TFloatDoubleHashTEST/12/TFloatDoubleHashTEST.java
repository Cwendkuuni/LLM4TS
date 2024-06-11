import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash(5, 0.75f, -1.0f, -1.0);
    }

    @Test
    public void testConstructor1() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
        assertEquals(5, hash.capacity());
        assertEquals(0, hash.size());
    }

    @Test
    public void testConstructor2() {
        hash = new TFloatDoubleHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
        assertEquals(10, hash.capacity());
        assertEquals(0, hash.size());
    }

    @Test
    public void testConstructor3() {
        hash = new TFloatDoubleHash(10, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
        assertEquals(10, hash.capacity());
        assertEquals(0, hash.size());
    }

    @Test
    public void testConstructor4() {
        hash = new TFloatDoubleHash(10, 0.5f, -1.0f, -1.0);
        assertEquals(-1.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(-1.0, hash.getNoEntryValue(), 0.0001);
        assertEquals(10, hash.capacity());
        assertEquals(0, hash.size());
    }

    @Test
    public void testSetUp() {
        assertEquals(5, hash.capacity());
        assertEquals(5, hash._set.length);
    }

    @Test
    public void testSetAndGetNoEntryKey() {
        hash.setNoEntryKey(-2.5f);
        assertEquals(-2.5f, hash.getNoEntryKey(), 0.0001);
    }

    @Test
    public void testSetAndGetNoEntryValue() {
        hash.setNoEntryValue(-2.5);
        assertEquals(-2.5, hash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(-1.0f));
        assertFalse(hash.contains(0.0f));
        assertFalse(hash.contains(1.0f));

        hash.insertKey(-1.0f);
        assertTrue(hash.contains(-1.0f));
        assertFalse(hash.contains(0.0f));
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        assertFalse(hash.forEach((float val) -> {
            fail("This should not be called");
            return true;
        }));

        hash.insertKey(-1.0f);
        hash.insertKey(1.0f);

        assertTrue(hash.forEach((float val) -> {
            if (val != -1.0f && val != 1.0f) {
                fail("Unexpected value: " + val);
                return false;
            }
            return true;
        }));
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(-1.0f);

        hash.removeAt(0);
        assertEquals(hash.getNoEntryKey(), hash._set[0], 0.0001);
        assertEquals(0, hash.size());
    }

    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(-1.0f));
        assertEquals(-1, hash.index(0.0f));
        assertEquals(-1, hash.index(1.0f));

        hash.insertKey(-1.0f);
        assertEquals(0, hash.index(-1.0f));
        assertEquals(-1, hash.index(0.0f));
        assertEquals(-1, hash.index(1.0f));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(-1.0f));

        assertEquals(0, hash.insertKey(-1.0f));
        assertEquals(1, hash.insertKey(-1.0f));
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, -1.0f);
        assertEquals(-1.0f, hash._set[0], 0.0001);
        assertEquals(1, hash.size());
    }

    @Test
    public void testXinsertKey() {
        assertEquals(0, hash.XinsertKey(-1.0f));

        assertEquals(0, hash.XinsertKey(-1.0f));
        assertEquals(1, hash.XinsertKey(-1.0f));
    }

    @Test
    public void testReadWriteExternal() throws Exception {
        hash.insertKey(-1.0f);
        hash.insertKey(0.0f);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        hash.writeExternal(oos);
        oos.close();

        TFloatDoubleHash newHash = new TFloatDoubleHash();
        newHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())));

        assertEquals(hash.getNoEntryKey(), newHash.getNoEntryKey(), 0.0001);
        assertEquals(hash.getNoEntryValue(), newHash.getNoEntryValue(), 0.0001);
        assertEquals(hash.capacity(), newHash.capacity());
        assertEquals(hash.size(), newHash.size());

        assertTrue(newHash.contains(-1.0f));
        assertTrue(newHash.contains(0.0f));
    }

}
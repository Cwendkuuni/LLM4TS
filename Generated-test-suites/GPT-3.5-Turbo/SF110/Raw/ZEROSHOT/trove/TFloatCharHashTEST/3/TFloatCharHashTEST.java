import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals('\0', hash.getNoEntryValue());

        hash = new TFloatCharHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals('\0', hash.getNoEntryValue());

        hash = new TFloatCharHash(10, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals('\0', hash.getNoEntryValue());

        hash = new TFloatCharHash(10, 0.5f, 1.0f, 'a');
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0f);
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
        hash.setUp(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.setUp(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                System.out.println("Value: " + value);
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.setUp(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        hash.removeAt(1);
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.setUp(10);
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));

        index = hash.insertKey(2.0f);
        assertEquals(1, index);
        assertTrue(hash.contains(2.0f));

        index = hash.insertKey(1.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.setUp(10);
        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));

        index = hash.XinsertKey(2.0f);
        assertEquals(1, index);
        assertTrue(hash.contains(2.0f));

        index = hash.XinsertKey(1.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testWriteExternal() {
        // Not Testable
    }

    @Test
    public void testReadExternal() {
        // Not Testable
    }
}
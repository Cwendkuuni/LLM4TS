
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash hash;

    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(0.0f)); // Test when hash does not contain the key
        hash.insertKey(0.0f);
        assertTrue(hash.contains(0.0f)); // Test when hash contains the key
    }

    @Test
    public void testForEach() {
        // Test with an empty hash
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                fail("Method should not be called");
                return true;
            }
        };
        assertTrue(hash.forEach(procedure)); 

        // Test with non-empty hash
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 1.0f, 0.0f);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(0.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(0.0f)); // Test after removing a key
    }

    @Test
    public void testIndex() {
        hash.insertKey(0.0f);
        assertEquals(0, hash.index(0.0f)); // Test with an existing key
        assertEquals(-1, hash.index(1.0f)); // Test with a non-existing key
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(0.0f)); // Test with an empty hash
        assertEquals(-1, hash.insertKey(0.0f)); // Test with an existing key
    }

    @Test
    public void testXinsertKey() {
        assertEquals(0, hash.XinsertKey(0.0f)); // Test with an empty hash
        assertEquals(-1, hash.XinsertKey(0.0f)); // Test with an existing key
    }

    @Test
    public void testWriteExternal() {
        // Not tested as it writes to an external object
    }

    @Test
    public void testReadExternal() {
        // Not tested as it reads from an external object
    }
}

import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(16, 0.75f, -1.0f, -1.0);
        assertEquals(0, hash.insertKey(-1.0f));
        assertEquals(1, hash.insertKey(-2.0f));
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(1.0f));
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return value > 0.0f;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testReadExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        try {
            hash.readExternal(null);
            fail("Expected IOException or ClassNotFoundException");
        } catch (IOException | ClassNotFoundException e) {
            // Expected exception
        }
    }

}
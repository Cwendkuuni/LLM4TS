import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(5.0f);
        hash.insertKey(-2.5f);

        assertTrue(hash.contains(5.0f));
        assertTrue(hash.contains(-2.5f));
        assertFalse(hash.contains(10.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(5.0f);
        hash.insertKey(-2.5f);

        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;

            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(2.5f, procedure.sum, 0.001);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(5.0f);
        hash.insertKey(-2.5f);

        hash.removeAt(0);
        assertFalse(hash.contains(5.0f));
        assertTrue(hash.contains(-2.5f));

        hash.removeAt(1);
        assertFalse(hash.contains(-2.5f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index1 = hash.insertKey(5.0f);
        int index2 = hash.insertKey(-2.5f);

        assertEquals(0, index1);
        assertEquals(1, index2);

        assertTrue(hash.contains(5.0f));
        assertTrue(hash.contains(-2.5f));
    }

    @Test
    public void testInsertKeyWithRehash() {
        TFloatCharHash hash = new TFloatCharHash(2, 0.5f);
        int index1 = hash.insertKey(5.0f);
        int index2 = hash.insertKey(-2.5f);
        int index3 = hash.insertKey(10.0f);

        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(0, index3);

        assertTrue(hash.contains(5.0f));
        assertTrue(hash.contains(-2.5f));
        assertTrue(hash.contains(10.0f));
    }
}
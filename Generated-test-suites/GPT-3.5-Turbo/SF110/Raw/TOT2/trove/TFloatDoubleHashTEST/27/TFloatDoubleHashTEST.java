import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.01);
        assertEquals(0.0, hash.getNoEntryValue(), 0.01);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(5.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        TFloatProcedure procedure = value -> {
            System.out.println(value);
            return true;
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(5.0f);
        assertTrue(hash.contains(5.0f));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(5.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(5.0f));
    }

    // Add more test cases as needed

}
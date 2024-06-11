import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor1() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.001);
    }

    @Test
    public void testConstructor2() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.001);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(5.0f);
        assertTrue(hash.contains(5.0f));
        assertFalse(hash.contains(10.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(5.0f);
        hash.insertKey(10.0f);
        hash.insertKey(15.0f);

        final float[] sum = {0.0f};
        hash.forEach(value -> {
            sum[0] += value;
            return true;
        });
        
        assertEquals(30.0f, sum[0], 0.001);
    }
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    TFloatDoubleHash floatDoubleHash;

    @Before
    public void setUp() {
        floatDoubleHash = new TFloatDoubleHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, floatDoubleHash.getNoEntryKey(), 0.001);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, floatDoubleHash.getNoEntryValue(), 0.001);
    }

    @Test
    public void testContains() {
        assertFalse(floatDoubleHash.contains(5.0f));
        floatDoubleHash.insertKey(5.0f);
        assertTrue(floatDoubleHash.contains(5.0f));
    }

    @Test
    public void testForEach() {
        floatDoubleHash.insertKey(1.0f);
        floatDoubleHash.insertKey(2.0f);
        floatDoubleHash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return value < 4.0f;
            }
        };

        assertTrue(floatDoubleHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        floatDoubleHash.insertKey(10.0f);
        floatDoubleHash.removeAt(0);
        assertFalse(floatDoubleHash.contains(10.0f));
    }

    @Test
    public void testInsertKey() {
        floatDoubleHash.insertKey(6.0f);
        assertTrue(floatDoubleHash.contains(6.0f));
    }

}
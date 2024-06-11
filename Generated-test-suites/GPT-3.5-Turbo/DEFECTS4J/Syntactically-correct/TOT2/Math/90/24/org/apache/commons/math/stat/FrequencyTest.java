import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    private Frequency freq;

    @Before
    public void setUp() {
        freq = new Frequency();
    }

    @Test
    public void testAddValue() {
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testAddValueWithNegativeNumber() {
        freq.addValue(-3);
        assertEquals(1, freq.getCount(-3));
    }

    @Test
    public void testAddValueWithDifferentTypes() {
        freq.addValue(3);
        freq.addValue(3L);
        assertEquals(2, freq.getCount(3));
    }

    @Test
    public void testAddValueWithInvalidType() {
        try {
            freq.addValue("invalid");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testAddValueWithChar() {
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test
    public void testClear() {
        freq.addValue(2);
        freq.addValue(3);
        freq.clear();
        assertEquals(0, freq.getCount(2));
        assertEquals(0, freq.getCount(3));
    }

    @Test
    public void testGetSumFreq() {
        freq.addValue(4);
        freq.addValue(4);
        freq.addValue(4);
        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        freq.addValue(2);
        freq.addValue(2);
        assertEquals(2, freq.getCount(2));
    }

    @Test
    public void testGetPct() {
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        assertEquals(0.5, freq.getPct(1), 0.01);
    }

    @Test
    public void testGetCumFreq() {
        freq.addValue(3);
        freq.addValue(4);
        freq.addValue(5);
        assertEquals(2, freq.getCumFreq(4));
    }

    @Test
    public void testGetCumPct() {
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(0.666, freq.getCumPct(2), 0.001);
    }
}
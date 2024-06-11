import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(7);
        freq.clear();
        assertEquals(0, freq.getCount(7));
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        assertEquals(1, freq.getCount(3));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(2);
        assertEquals(0.5, freq.getPct(2), 0.01);
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(3);
        assertEquals(2, freq.getSumFreq());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithIncomparableObject() {
        Frequency freq = new Frequency();
        freq.addValue("test");
    }
}
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
        freq.addValue(5);
        freq.clear();
        assertEquals(0, freq.getCount(5));
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        assertEquals(2, freq.getSumFreq());
    }

    @Test
    public void testCumulativeFrequency() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(8);
        freq.addValue(5);
        freq.addValue(3);
        assertEquals(2, freq.getCumFreq(5));
        assertEquals(4, freq.getCumFreq(8));
        assertEquals(0, freq.getCumFreq(2));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(8);
        freq.addValue(5);
        freq.addValue(3);
        assertEquals(0.5, freq.getCumPct(5), 0.01);
        assertEquals(1, freq.getCumPct(8), 0.01);
        assertEquals(0, freq.getCumPct(2), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonComparableValue() {
        Frequency freq = new Frequency();
        freq.addValue(new Object());
    }

}
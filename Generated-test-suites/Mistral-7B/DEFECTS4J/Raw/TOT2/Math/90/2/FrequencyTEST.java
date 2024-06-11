import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
        freq.addValue(1);
        assertEquals(2, freq.getCount(1));
        freq.addValue(2);
        assertEquals(2, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
        freq.addValue(1);
        assertEquals(3, freq.getCount(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueDifferentType() {
        Frequency freq = new Frequency();
        freq.addValue('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue("two");
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(3, freq.getSumFreq());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCountInvalidType() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(3, freq.getSumFreq());
        assertEquals(3, freq.getCount(1));
        assertEquals(0, freq.getCount("two"));
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(3, freq.getSumFreq());
        assertEquals(3, freq.getCount(1));
        assertEquals(0, freq.getCount(2));
        freq.addValue(1);
        assertEquals(4, freq.getSumFreq());
        assertEquals(4, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(1.0 / 3.0, freq.getPct(1), 0.001);
        assertEquals(0.0, freq.getPct(2), 0.001);
        freq.addValue(1);
        assertEquals(1.0 / 4.0, freq.getPct(1), 0.001);
        assertEquals(1.0 / 4.0, freq.getPct(2), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(3, freq.getCumFreq(3));
        assertEquals(3, freq.getCumFreq(4));
        assertEquals(3, freq.getCumFreq(5));
        freq.addValue(1);
        assertEquals(4, freq.getCumFreq(1));
        assertEquals(5, freq.getCumFreq(5));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(1.0 / 3.0, freq.getPct(1), 0.001);
        assertEquals(1.0 / 3.0, freq.getCumPct(1), 0.001);
        assertEquals(2.0 / 3.0, freq.getCumPct(2), 0.001);
        freq.addValue(1);
        assertEquals(2.0 / 4.0, freq.getCumPct(1), 0.001);
        assertEquals(3.0 / 4.0, freq.getCumPct(2), 0.001);
        assertEquals(1.0, freq.getCumPct(4), 0.001);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.stat.Frequency;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testAddValueInteger() {
        Frequency freq = new Frequency();
        freq.addValue(Integer.valueOf(3));
        assertEquals(1, freq.getCount(3));
    }

    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(10L);
        assertEquals(1, freq.getCount(10L));
    }

    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(2, freq.getCount(1));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(0.333, freq.getPct(1), 0.01);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(2, freq.getCumFreq(2));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(0.666, freq.getCumPct(2), 0.01);
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

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
        freq.addValue(Integer.valueOf(10));
        assertEquals(1, freq.getCount(10));
    }

    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(Long.valueOf(15));
        assertEquals(1, freq.getCount(15));
    }

    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        assertEquals(1, freq.getCount('A'));
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(7);
        assertEquals(1, freq.getCount(7));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(0.6666666666666666, freq.getPct(5), 0.0001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(2, freq.getCumFreq(5));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(0.6666666666666666, freq.getCumPct(5), 0.0001);
    }
}
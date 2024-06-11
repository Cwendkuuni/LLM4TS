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
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        assertEquals(2, freq.getCount(5));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        assertEquals(1.0, freq.getPct(5), 0.001);
    }

    @Test
    public void testCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(7);
        assertEquals(1, freq.getCumFreq(5));
        assertEquals(2, freq.getCumFreq(7));
        assertEquals(0, freq.getCumFreq(3));
    }

    @Test
    public void testCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(7);
        assertEquals(0.5, freq.getCumPct(5), 0.001);
        assertEquals(1.0, freq.getCumPct(7), 0.001);
        assertEquals(0.0, freq.getCumPct(3), 0.001);
    }

}
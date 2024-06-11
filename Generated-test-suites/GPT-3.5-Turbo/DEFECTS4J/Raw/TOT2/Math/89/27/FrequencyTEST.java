import static org.junit.Assert.*;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testAddValue_Integer() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testAddValue_Long() {
        Frequency freq = new Frequency();
        freq.addValue(100L);
        assertEquals(1, freq.getCount(100L));
    }

    @Test
    public void testAddValue_Char() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(4);
        freq.addValue(6);
        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(2);
        assertEquals(2, freq.getCount(2));
    }

    @Test
    public void testPct() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(0.5, freq.getPct(2), 0.01);
    }

    @Test
    public void testCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(4);
        freq.addValue(4);
        assertEquals(3, freq.getCumFreq(4));
    }

    @Test
    public void testCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(4);
        assertEquals(0.5, freq.getCumPct(4), 0.01);
    }
}
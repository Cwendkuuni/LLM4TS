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
        freq.addValue(10);
        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(2, freq.getCount(5));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(2.0/3.0, freq.getPct(5), 0.0001);
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
        assertEquals(2.0/3.0, freq.getCumPct(5), 0.0001);
    }
}
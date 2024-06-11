import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
    }

    @Test
    public void testAddValueForChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
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
        assertEquals(0.5, freq.getPct(1), 0.001);
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.clear();
        assertEquals(0, freq.getCount(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueException() {
        Frequency freq = new Frequency();
        freq.addValue("invalid"); // Should throw an IllegalArgumentException
    }

    @Test
    public void testCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(1, freq.getCumFreq(1));
    }

    @Test
    public void testCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0.5, freq.getCumPct(1), 0.001);
    }
}
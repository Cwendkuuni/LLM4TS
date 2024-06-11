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
        assertEquals(1.0, freq.getPct(5), 0.0);
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
        assertEquals(0.666, freq.getCumPct(5), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency freq = new Frequency();
        freq.addValue(new Object()); // Should throw IllegalArgumentException
    }

    @Test(expected = ClassCastException.class)
    public void testAddValueIncompatibleType() {
        Frequency freq = new Frequency();
        freq.addValue('a'); // Should throw ClassCastException
    }

}
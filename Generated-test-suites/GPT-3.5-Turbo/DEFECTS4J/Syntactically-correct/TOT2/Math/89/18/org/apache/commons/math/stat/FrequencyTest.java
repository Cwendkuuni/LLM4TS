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
    public void testAddValueWithDifferentTypes() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        freq.addValue(10L);
        assertEquals(2, freq.getCount(10));
    }

    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueException() {
        Frequency freq = new Frequency();
        freq.addValue("test");
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
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        assertEquals(0.5, freq.getCumPct(1), 0.001);
    }
}
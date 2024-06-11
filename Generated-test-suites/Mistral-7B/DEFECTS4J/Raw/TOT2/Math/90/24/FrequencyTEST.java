import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(3, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
        assertEquals(3, freq.getSumFreq());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithUncomparableValues() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithUncomparableValues2() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2);
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
    }
}
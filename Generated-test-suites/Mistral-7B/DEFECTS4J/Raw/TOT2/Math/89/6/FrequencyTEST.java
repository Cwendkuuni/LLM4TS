import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testFrequencyAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);

        // Test basic functionality
        assertEquals(4, freq.getSumFreq());
        assertEquals(3, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
        assertEquals(1, freq.getCount(3));

        // Test percentage
        assertEquals(0.75, freq.getPct(1), 0.001);
        assertEquals(0.25, freq.getPct(2), 0.001);
        assertEquals(0.05, freq.getPct(3), 0.001);

        // Test cumulative frequency and percentage
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(3, freq.getCumFreq(2));
        assertEquals(4, freq.getCumFreq(3));
        assertEquals(0.25, freq.getCumPct(1), 0.001);
        assertEquals(0.5, freq.getCumPct(2), 0.001);
        assertEquals(1, freq.getCumPct(3), 0.001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyAddNonComparableValue() {
        Frequency freq = new Frequency();
        freq.addValue(new Object());
    }
    
    @Test
    public void testFrequencyClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
    }
}
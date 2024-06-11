import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest1 {
    @Test
    public void testFrequencyWithIntValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        // Test getCount() method with int value
        long count = frequency.getCount(1);
        assertEquals(2, count);

        // Test getPct() method with int value
        double pct = frequency.getPct(1);
        assertEquals(0.5, pct, 0.01);

        // Test getCumFreq() method with int value
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(2, cumFreq);

        // Test getCumPct() method with int value
        double cumPct = frequency.getCumPct(1);
        assertEquals(0.5, cumPct, 0.01);

        // Test clear() method
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }

    // Add more test cases for other methods and edge cases here
}
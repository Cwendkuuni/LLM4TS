import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest1 {

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        // Basic usage
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('a');
        // Edge cases
        frequency.addValue(Integer.MAX_VALUE);
        frequency.addValue(Integer.MIN_VALUE);
        frequency.addValue('~'); // Unprintable character
        // Exception handling
        try {
            frequency.addValue(new Object());
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            frequency.addValue(null);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Other test methods for getSumFreq(), getCount(), getPct(), getCumFreq(), and getCumPct()
}
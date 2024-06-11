import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testFrequencyWithNaturalOrder() {
        Frequency frequency = new Frequency();

        // Add some values and check the frequency, percentage, and cumulative percentage
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(3);
        frequency.addValue(1);

        // Test getCount, getPct, and getCumPct methods
        assertEquals(6, frequency.getSumFreq());
        assertEquals(1.0 / 6.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 6.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct(1), 0.001);
        assertEquals(2.0 / 6.0, frequency.getCumPct(1), 0.001);
        assertEquals(3.0 / 6.0, frequency.getCumPct(2), 0.001);
        assertEquals(4.0 / 6.0, frequency.getCumPct(3), 0.001);
        assertEquals(5.0 / 6.0, frequency.getCumPct(1), 0.001);
        assertEquals(6.0 / 6.0, frequency.getCumPct(3), 0.001);

        // Test getCount, getPct, and getCumPct methods with non-Comparable values
        try {
            frequency.addValue("nonComparableValue");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            long count = frequency.getCount("nonComparableValue");
            fail("ClassCastException expected");
        } catch (ClassCastException e) {
            // Expected
        }
        try {
            double pct = frequency.getPct("nonComparableValue");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            double cumPct = frequency.getCumPct("nonComparableValue");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test addValue with non-Comparable values
        try {
            frequency.addValue(1);
            frequency.addValue('a');
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test addValue with non-Comparable values (different classes)
        Frequency frequencyWithComparator = new Frequency(new NaturalComparator());
        try {
            frequencyWithComparator.addValue(1);
            frequencyWithComparator.addValue(new Object() {});
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test toString method
        assertEquals(
                "Value \t Freq. \t Pct. \t Cum Pct.\n" +
                        "1 \t 4 \t 0.6666666666666666 \t 0.6666666666666666\n" +
                        "2 \t 1 \t 0.16666666666666663 \t 0.7333333333333333\n" +
                        "3 \t 1 \t 0.16666666666666663 \t 1.0\n",
                frequencyWithComparator.toString());
    }
}
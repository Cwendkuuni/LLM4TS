import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency frequency = new Frequency();

        // Test adding values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        // Test getCount method
        Assert.assertEquals(3, frequency.getCount(1L));
        Assert.assertEquals(3, frequency.getCount(2L));
        Assert.assertEquals(2, frequency.getCount(3L));
        Assert.assertEquals(0, frequency.getCount(4L));

        // Test getPct method
        Assert.assertEquals(0.3, frequency.getPct(1L), 0.01);
        Assert.assertEquals(0.3, frequency.getPct(2L), 0.01);
        Assert.assertEquals(0.4, frequency.getPct(3L), 0.01);

        // Test getCumFreq method
        Assert.assertEquals(3, frequency.getCumFreq(1L));
        Assert.assertEquals(6, frequency.getCumFreq(2L));
        Assert.assertEquals(9, frequency.getCumFreq(3L));
        Assert.assertEquals(9, frequency.getCumFreq(Integer.MAX_VALUE));

        // Test getCumPct method
        Assert.assertEquals(0.3, frequency.getCumPct(1L), 0.01);
        Assert.assertEquals(0.6, frequency.getCumPct(2L), 0.01);
        Assert.assertEquals(1.0, frequency.getCumPct(3L), 0.01);

        // Test adding values with a custom Comparator
        Comparator<Comparable<?>> comparator = new NaturalComparator();
        Frequency frequencyWithComparator = new Frequency(comparator);

        frequencyWithComparator.addValue(3);
        frequencyWithComparator.addValue(1);
        frequencyWithComparator.addValue(2);

        // Test getCount method with custom Comparator
        Assert.assertEquals(1, frequencyWithComparator.getCount(1L));
        Assert.assertEquals(1, frequencyWithComparator.getCount(2L));
        Assert.assertEquals(2, frequencyWithComparator.getCount(3L));

        // Test getPct method with custom Comparator
        Assert.assertEquals(0.33, frequencyWithComparator.getPct(3L), 0.01);
        Assert.assertEquals(0.33, frequencyWithComparator.getPct(1L), 0.01);
        Assert.assertEquals(0.33, frequencyWithComparator.getPct(2L), 0.01);

        // Test getCumFreq method with custom Comparator
        Assert.assertEquals(1, frequencyWithComparator.getCumFreq(1L));
        Assert.assertEquals(2, frequencyWithComparator.getCumFreq(2L));
        Assert.assertEquals(3, frequencyWithComparator.getCumFreq(3L));

        // Test getCumPct method with custom Comparator
        Assert.assertEquals(0.33, frequencyWithComparator.getCumPct(1L), 0.01);
        Assert.assertEquals(0.67, frequencyWithComparator.getCumPct(2L), 0.01);
        Assert.assertEquals(1.0, frequencyWithComparator.getCumPct(3L), 0.01);

        // Test addValue with IllegalArgumentException
        try {
            frequency.addValue("not a number");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test addValue with ClassCastException
        try {
            frequency.addValue('a');
            Assert.fail();
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyFrequency() {
        Frequency emptyFrequency = new Frequency();

        // Test getCount method
        Assert.assertEquals(0, emptyFrequency.getCount(1L));
        Assert.assertEquals(0, emptyFrequency.getCount(2L));
        Assert.assertEquals(0, emptyFrequency.getCount(3L));

        // Test getPct method
        Assert.assertEquals(Double.NaN, emptyFrequency.getPct(1L));
        Assert.assertEquals(Double.NaN, emptyFrequency.getPct(2L));
        Assert.assertEquals(Double.NaN, emptyFrequency.getPct(3L));

        // Test getCumFreq method
        Assert.assertEquals(0, emptyFrequency.getCumFreq(1L));
        Assert.assertEquals(0, emptyFrequency.getCumFreq(2L));
        Assert.assertEquals(0, emptyFrequency.getCumFreq(3L));
        Assert.assertEquals(0, emptyFrequency.getCumFreq(Integer.MAX_VALUE));

        // Test getCumPct method
        Assert.assertEquals(0.0, emptyFrequency.getCumPct(1L), 0.01);
        Assert.assertEquals(0.0, emptyFrequency.getCumPct(2L), 0.01);
        Assert.assertEquals(0.0, emptyFrequency.getCumPct(3L), 0.01);
    }
}
import org.apache.commons.math.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        // Test default constructor
        Frequency frequency = new Frequency();
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertNull(frequency.toString());

        // Test constructor with Comparator
        Comparator comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        Frequency frequencyWithComparator = new Frequency(comparator);

        // Test addValue methods
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequencyWithComparator.addValue(1);
        frequencyWithComparator.addValue(1);
        frequencyWithComparator.addValue(2);

        // Test getSumFreq
        Assert.assertEquals(6, frequency.getSumFreq());
        Assert.assertEquals(6, frequencyWithComparator.getSumFreq());

        // Test getCount methods
        Assert.assertEquals(3, frequency.getCount(1L));
        Assert.assertEquals(3, frequencyWithComparator.getCount(1L));
        Assert.assertEquals(2, frequency.getCount(2L));
        Assert.assertEquals(2, frequencyWithComparator.getCount(2L));
        Assert.assertEquals(0, frequency.getCount('a'));
        Assert.assertEquals(0, frequencyWithComparator.getCount('a'));

        // Test getPct methods
        Assert.assertEquals(0.5, frequency.getPct(1L), 0.01);
        Assert.assertEquals(0.5, frequencyWithComparator.getPct(1L), 0.01);
        Assert.assertEquals(0.33, frequency.getPct(2L), 0.01);
        Assert.assertEquals(0.33, frequencyWithComparator.getPct(2L), 0.01);
        Assert.assertEquals(0.0, frequency.getPct('a'), 0.01);
        Assert.assertEquals(0.0, frequencyWithComparator.getPct('a'), 0.01);

        // Test getCumFreq methods
        Assert.assertEquals(0, frequency.getCumFreq(null));
        Assert.assertEquals(0, frequencyWithComparator.getCumFreq(null));
        Assert.assertEquals(3, frequency.getCumFreq(1L));
        Assert.assertEquals(3, frequency.getCumFreq(1));
        Assert.assertEquals(3, frequencyWithComparator.getCumFreq(1L));
        Assert.assertEquals(3, frequencyWithComparator.getCumFreq(1));
        Assert.assertEquals(5, frequency.getCumFreq(2L));
        Assert.assertEquals(5, frequency.getCumFreq(2));
        Assert.assertEquals(5, frequencyWithComparator.getCumFreq(2L));
        Assert.assertEquals(5, frequencyWithComparator.getCumFreq(2));

        // Test getCumPct methods
        Assert.assertEquals(0.0, frequency.getCumPct(null), 0.01);
        Assert.assertEquals(0.0, frequencyWithComparator.getCumPct(null), 0.01);
        Assert.assertEquals(0.5, frequency.getCumPct(1L), 0.01);
        Assert.assertEquals(0.5, frequency.getCumPct(1), 0.01);
        Assert.assertEquals(0.83, frequency.getCumPct(2L), 0.01);
        Assert.assertEquals(0.83, frequency.getCumPct(2), 0.01);
    }
}
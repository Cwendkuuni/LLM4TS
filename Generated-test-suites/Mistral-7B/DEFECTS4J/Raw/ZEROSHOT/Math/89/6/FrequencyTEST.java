import org.apache.commons.math.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;

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

        // Test getSumFreq
        long sumFreq = frequency.getSumFreq();
        Assert.assertEquals(6L, sumFreq);

        // Test getCount
        long count1 = frequency.getCount(1);
        Assert.assertEquals(2L, count1);

        long count2 = frequency.getCount(2);
        Assert.assertEquals(2L, count2);

        long count3 = frequency.getCount(3);
        Assert.assertEquals(2L, count3);

        // Test getCount with unsupported types
        Assert.assertNull(frequency.getCount("A"));
        Assert.assertNull(frequency.getCount(new Object()));

        // Test getPct
        double pct1 = frequency.getPct(1);
        Assert.assertEquals(0.3333333333333333, pct1, 0.00001);

        double pct2 = frequency.getPct(2);
        Assert.assertEquals(0.3333333333333333, pct2, 0.00001);

        double pct3 = frequency.getPct(3);
        Assert.assertEquals(0.3333333333333333, pct3, 0.00001);

        // Test getPct with unsupported types
        Assert.assertTrue(Double.isNaN(frequency.getPct("A")));
        Assert.assertTrue(Double.isNaN(frequency.getPct(new Object())));

        // Test getCumFreq
        long cumFreq1 = frequency.getCumFreq(1);
        Assert.assertEquals(2L, cumFreq1);

        long cumFreq2 = frequency.getCumFreq(2);
        Assert.assertEquals(4L, cumFreq2);

        long cumFreq3 = frequency.getCumFreq(3);
        Assert.assertEquals(6L, cumFreq3);

        // Test getCumFreq with unsupported types
        Assert.assertEquals(0L, frequency.getCumFreq("A"));
        Assert.assertEquals(0L, frequency.getCumFreq(new Object()));

        // Test getCumPct
        double cumPct1 = frequency.getCumPct(1);
        Assert.assertEquals(0.3333333333333333, cumPct1, 0.00001);

        double cumPct2 = frequency.getCumPct(2);
        Assert.assertEquals(0.5555555555555555, cumPct2, 0.00001);

        double cumPct3 = frequency.getCumPct(3);
        Assert.assertEquals(1, cumPct3, 0.00001);

        // Test getCumPct with unsupported types
        Assert.assertTrue(Double.isNaN(frequency.getCumPct("A")));
        Assert.assertTrue(Double.isNaN(frequency.getCumPct(new Object())));
    }

    @Test
    public void testFrequencyWithComparator() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable)o1).compareTo(o2) * -1;
            }
        };

        Frequency frequency = new Frequency(comparator);

        // Test adding values
        frequency.addValue(3);
        frequency.addValue(2);
        frequency.addValue(1);

        // Test getSumFreq
        long sumFreq = frequency.getSumFreq();
        Assert.assertEquals(3L, sumFreq);

        // Test getCount
        long count1 = frequency.getCount(1);
        Assert.assertEquals(0L, count1);

        long count2 = frequency.getCount(2);
        Assert.assertEquals(1L, count2);

        long count3 = frequency.getCount(3);
        Assert.assertEquals(1L, count3);

        // Test getPct
        double pct2 = frequency.getPct(2);
        Assert.assertEquals(0.3333333333333333, pct2, 0.00001);

        double pct3 = frequency.getPct(3);
        Assert.assertEquals(0.6666666666666666, pct3, 0.00001);

        // Test getCumFreq
        long cumFreq2 = frequency.getCumFreq(2);
        Assert.assertEquals(1L, cumFreq2);

        long cumFreq3 = frequency.getCumFreq(3);
        Assert.assertEquals(3L, cumFreq3);

        // Test getCumPct
        double cumPct2 = frequency.getCumPct(2);
        Assert.assertEquals(0.3333333333333333, cumPct2, 0.00001);

        double cumPct3 = frequency.getCumPct(3);
        Assert.assertEquals(1, cumPct3, 0.00001);
    }
}
import org.apache.commons.math3.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        // Basic functionality test
        frequency.addValue(1);
        Assert.assertEquals(1, frequency.getCount(1));
        // Edge case: adding the same value multiple times
        for (int i = 0; i < 10; i++) {
            frequency.addValue(1);
        }
        Assert.assertEquals(11, frequency.getCount(1));
        // Edge case: adding a negative number
        frequency.addValue(-1);
        Assert.assertEquals(11, frequency.getCount(1));
        Assert.assertEquals(0, frequency.getCount(-1));
        // Error scenario: adding a non-comparable object
        Assert.assertThrows(IllegalArgumentException.class, () -> frequency.addValue("test"));
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        // Basic functionality test
        frequency.addValue(1);
        Assert.assertEquals(1, frequency.getSumFreq());
        // Edge case: adding multiple values
        frequency.addValue(2);
        Assert.assertEquals(3, frequency.getSumFreq());
        // Edge case: clearing the frequency table
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        // Basic functionality test
        frequency.addValue(1);
        Assert.assertEquals(1, frequency.getCount(1));
        // Edge case: adding multiple values
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));
        // Edge case: getting the count of a non-existent value
        Assert.assertEquals(0, frequency.getCount(3));
        // Error scenario: getting the count of a non-comparable object
        Assert.assertThrows(IllegalArgumentException.class, () -> frequency.getCount("test"));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        // Basic functionality test
        frequency.addValue(1);
        Assert.assertEquals(1.0, frequency.getPct(1), 0.001);
        // Edge case: adding multiple values
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(0.5, frequency.getPct(1), 0.001);
        Assert.assertEquals(0.5, frequency.getPct(2), 0.001);
        // Edge case: no values added
        frequency.clear();
        Assert.assertEquals(0.0, frequency.getPct(1), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        // Basic functionality test
        frequency.addValue(1);
        Assert.assertEquals(1, frequency.getCumFreq(1));
        // Edge case: adding multiple values
        frequency.addValue(2);
        Assert.assertEquals(3, frequency.getCumFreq(2));
        // Edge case: getting the cumulative frequency of a non-existent value
        Assert.assertEquals(0, frequency.getCumFreq(3));
        // Edge case: getting the cumulative frequency of a value less than the smallest value
        Assert.assertEquals(0, frequency.getCumFreq(-1));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        // Basic functionality test
        frequency.addValue(1);
        Assert.assertEquals(1.0, frequency.getPct(1), 0.001);
        Assert.assertEquals(1.0, frequency.getCumPct(1), 0.001);
        // Edge case: adding multiple values
        frequency.addValue(2);
        Assert.assertEquals(0.5, frequency.getPct(1), 0.001);
        Assert.assertEquals(1.0, frequency.getCumPct(1), 0.001);
        Assert.assertEquals(0.5, frequency.getCumPct(2), 0.001);
        // Edge case: no values added
        frequency.clear();
        Assert.assertEquals(0.0, frequency.getCumPct(1), 0.001);
    }

    @Test
    public void testComparator() {
        Frequency frequency = new Frequency(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        });
        // Basic functionality test
        frequency.addValue(2);
        frequency.addValue(1);
        // Edge case: comparing different classes
        frequency.addValue(new Object());
        Assert.assertEquals(1, frequency.getCumFreq(1));
        Assert.assertEquals(2, frequency.getCumFreq(2));
        Assert.assertEquals(3, frequency.getCumFreq(new Object()));
    }
}
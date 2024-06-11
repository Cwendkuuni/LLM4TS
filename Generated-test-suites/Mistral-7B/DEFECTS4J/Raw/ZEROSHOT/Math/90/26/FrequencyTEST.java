import org.apache.commons.math3.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        Assert.assertEquals("Size should be zero initially", 0, frequency.getSumFreq());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };

        Frequency frequency = new Frequency(comparator);
        Assert.assertEquals("Size should be zero initially", 0, frequency.getSumFreq());
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        Assert.assertEquals("Sum frequency should be 4", 4, frequency.getSumFreq());
        Assert.assertEquals("Count of value 1 should be 2", 2, frequency.getCount(1));
        Assert.assertEquals("Count of value 2 should be 1", 1, frequency.getCount(2));

        Assert.assertEquals("Percentage of value 1 should be 0.5", 0.5, festival.getPct(1), 0.01);
        Assert.assertEquals("Percentage of value 2 should be 0.25", 0.25, festival.getPct(2), 0.01);

        Assert.assertEquals("Cumulative frequency of value 1 should be 2", 2, frequency.getCumFreq(1));
        Assert.assertEquals("Cumulative frequency of value 2 should be 3", 3, frequency.getCumFreq(2));

        Assert.assertEquals("Cumulative percentage of value 1 should be 0.5", 0.5, festival.getCumPct(1), 0.01);
        Assert.assertEquals("Cumulative percentage of value 2 should be 0.75", 0.75, festival.getCumPct(2), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("nonComparableObject");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValueTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "1 \t 2 \t 0.5 \t 0.5\n" +
                "2 \t 1 \t 0.25 \t 0.75\n";

        Assert.assertEquals("ToString method should return the expected string", expected, frequency.toString());
    }
}
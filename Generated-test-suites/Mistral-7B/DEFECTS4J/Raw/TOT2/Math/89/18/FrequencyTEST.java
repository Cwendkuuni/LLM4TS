import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testFrequencyEmpty() {
        Frequency frequency = new Frequency();
        Assert.assertEquals("Empty frequency distribution should have no values", 0, frequency.getSumFreq());
        Assert.assertEquals("Empty frequency distribution should have no values", 0, frequency.getCount(null));
        Assert.assertEquals("Empty frequency distribution should have no values", 0.0, frequency.getPct(null), 0.001);
        Assert.assertEquals("Empty frequency distribution should have no values", 0.0, frequency.getCumPct(null), 0.001);
    }

    @Test
    public void testFrequencyBasic() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        Assert.assertEquals("Frequency distribution should have 3 values", 3, frequency.getSumFreq());
        Assert.assertEquals("Frequency distribution should have 3 values with frequency 3 for value 1", 3, frequency.getCount(1));
        Assert.assertEquals("Frequency distribution should have 3 values with percentage 1.0 for value 1", 1.0, frequency.getPct(1), 0.001);
        Assert.assertEquals("Frequency distribution should have 3 values with cumulative percentage 1.0 for value 1", 1.0, frequency.getCumPct(1), 0.001);
        Assert.assertEquals("Frequency distribution should have 3 values with cumulative percentage 1.0 for value 1", 1.0, frequency.getCumPct(1.0), 0.001);
        Assert.assertEquals("Frequency distribution should have 3 values with cumulative percentage 1.0 for value 1", 1.0, frequency.getCumPct(1.0d), 0.001);

        Assert.assertEquals("Frequency distribution should have 3 values with cumulative percentage 1.0 for all values", 1.0, frequency.getCumPct(2.0), 0.001);
        Assert.assertEquals("Frequency distribution should have 3 values with cumulative percentage 3.0 for value 3", 3.0, frequency.getCumPct(3.0), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyInvalidValueType() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(null);
    }

    @Test
    public void testFrequencyComparator() {
        Comparator<Comparable> comparator = (o1, o2) -> o1.compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(2);
        frequency.addValue(1);

        Assert.assertEquals("Frequency distribution with custom comparator should have 2 values", 2, frequency.getSumFreq());
        Assert.assertEquals("Frequency distribution with custom comparator should have value 2 with frequency 1", 1, frequency.getCount(2));
        Assert.assertEquals("Frequency distribution with custom comparator should have value 1 with frequency 1", 1, frequency.getCount(1));

        Assert.assertEquals("Frequency distribution with custom comparator should have percentage 0.5 for value 1", 0.5, frequency.getPct(1), 0.001);
        Assert.assertEquals("Frequency distribution with custom comparator should have percentage 0.5 for value 1", 0.5, frequency.getPct(1.0), 0.001);
        Assert.assertEquals("Frequency distribution with custom comparator should have percentage 0.5 for value 1", 0.5, frequency.getPct(1.0d), 0.001);

        Assert.assertEquals("Frequency distribution with custom comparator should have cumulative percentage 0.5 for value 1", 0.5, frequency.getCumPct(1), 0.001);
        Assert.assertEquals("Frequency distribution with custom comparator should have cumulative percentage 0.5 for value 1", 0.5, frequency.getCumPct(1.0), 0.001);
        Assert.assertEquals("Frequency distribution with custom comparator should have cumulative percentage 0.5 for value 1", 0.5, frequency.getCumPct(1.0d), 0.001);

        Assert.assertEquals("Frequency distribution with custom comparator should have cumulative percentage 1.0 for value 2", 1.0, frequency.getCumPct(2), 0.001);
        Assert.assertEquals("Frequency distribution with custom comparator should have cumulative percentage 1.0 for value 2", 1.0, frequency.getCumPct(2.0), 0.001);
        Assert.assertEquals("Frequency distribution with custom comparator should have cumulative percentage 1.0 for value 2", 1.0, frequency.getCumPct(2.0d), 0.001);
    }

    @Test
    public void testFrequencyCustomValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));

        Assert.assertEquals("Frequency distribution should have 3 values", 3, frequency.getSumFreq());
        Assert.assertEquals("Frequency distribution should have value 1 with frequency 2", 2, frequency.getCount(new Integer(1)));
        Assert.assertEquals("Frequency distribution should have value 2 with frequency 1", 1, frequency.getCount(new Integer(2)));

        Assert.assertEquals("Frequency distribution should have percentage 0.6667 for value 1", 0.6667, frequency.getPct(new Integer(1)), 0.001);
        Assert.assertEquals("Frequency distribution should have percentage 0.3333 for value 2", 0.3333, frequency.getPct(new Integer(2)), 0.001);

        Assert.assertEquals("Frequency distribution should have cumulative percentage 0.6667 for value 1", 0.6667, frequency.getCumPct(new Integer(1)), 0.001);
        Assert.assertEquals("Frequency distribution should have cumulative percentage 1.0 for value 2", 1.0, frequency.getCumPct(new Integer(2)), 0.001);
    }

    @Test
    public void testFrequencyStringRepresentation() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        frequency.addValue(1);

        String expected = "Value  \t Freq.  \t Pct.  \t Cum Pct.\n2        \t 1      \t 0.333 \t 0.333\n1        \t 1      \t 0.667 \t 1.000";
        Assert.assertEquals("Frequency distribution toString should generate expected string representation", expected, frequency.toString());
    }
}
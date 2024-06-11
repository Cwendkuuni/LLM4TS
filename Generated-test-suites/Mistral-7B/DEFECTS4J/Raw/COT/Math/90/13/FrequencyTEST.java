import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        Assert.assertNotNull(frequency.freqTable);
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        Assert.assertNotNull(frequency.freqTable);
        Assert.assertEquals(comparator, frequency.freqTable.comparator());
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Assert.assertEquals(
                "Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.25 \t 0.5833333333333333\n",
                frequency.toString());
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals(Long.valueOf(1), frequency.freqTable.get(Long.valueOf(1)));
        frequency.addValue(1);
        Assert.assertEquals(Long.valueOf(2), frequency.freqTable.get(Long.valueOf(1)));
        frequency.addValue(2);
        Assert.assertEquals(Long.valueOf(1), frequency.freqTable.get(Long.valueOf(2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        Assert.assertNull(frequency.freqTable.get(Long.valueOf(1)));
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator iterator = frequency.valuesIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Long.valueOf(1), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Long.valueOf(2), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(Long.valueOf(3), frequency.freqTable.get(Long.valueOf(3)));
        Assert.assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(Long.valueOf(2), frequency.freqTable.get(Long.valueOf(1)));
        Assert.assertEquals(2, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(Long.valueOf(1), frequency.freqTable.get(Long.valueOf(2)));
        Assert.assertEquals(1, frequency.getCount(Long.valueOf(2)));
        Assert.assertEquals(Long.valueOf(0), frequency.freqTable.get(Long.valueOf(3)));
        Assert.assertEquals(0, frequency.getCount(Long.valueOf(3)));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(Double.NaN, frequency.getPct(Long.valueOf(3)));
        Assert.assertEquals(0.3333333333333333, frequency.getPct(Long.valueOf(1)), 0.0001);
        Assert.assertEquals(0.25, frequency.getPct(Long.valueOf(2)), 0.0001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(Long.valueOf(0), frequency.freqTable.get(Long.valueOf(0)));
        Assert.assertEquals(0, frequency.getCumFreq(Long.valueOf(0)));
        Assert.assertEquals(Long.valueOf(1), frequency.freqTable.get(Long.valueOf(1)));
        Assert.assertEquals(1, frequency.getCumFreq(Long.valueOf(1)));
        Assert.assertEquals(Long.valueOf(2), frequency.freqTable.get(Long.valueOf(2)));
        Assert.assertEquals(2, frequency.getCumFreq(Long.valueOf(2)));
        Assert.assertEquals(Long.valueOf(3), frequency.freqTable.get(Long.valueOf(3)));
        Assert.assertEquals(3, frequency.getCumFreq(Long.valueOf(3)));
        Assert.assertEquals(Long.valueOf(3), frequency.freqTable.get(Long.valueOf(4)));
        Assert.assertEquals(3, frequency.getCumFreq(Long.valueOf(4)));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(Double.NaN, frequency.getPct(Long.valueOf(0)));
        Assert.assertEquals(0.3333333333333333, frequency.getPct(Long.valueOf(1)), 0.0001);
        Assert.assertEquals(0.5833333333333333, frequency.getPct(Long.valueOf(2)), 0.0001);
        Assert.assertEquals(1.0, frequency.getPct(Long.valueOf(3)), 0.0001);
    }
}
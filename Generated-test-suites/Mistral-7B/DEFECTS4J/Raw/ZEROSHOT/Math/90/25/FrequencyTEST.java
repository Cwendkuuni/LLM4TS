import org.apache.commons.math.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        Assert.assertNotNull(frequency.freqTable);
        Assert.assertEquals(0, frequency.getSumFreq());
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
        Assert.assertNotNull(frequency.freqTable);
        Assert.assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals(Long.valueOf(1), frequency.freqTable.get(Long.valueOf(1)));
        Assert.assertEquals(1, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getSumFreq());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        Assert.assertEquals(Long.valueOf(1), frequency.freqTable.get(Long.valueOf(2)));
        Assert.assertEquals(1, frequency.getCount(2));
        Assert.assertEquals(1, frequency.getSumFreq());
    }

    @Test
    public void testAddValueString() {
        Frequency frequency = new Frequency();
        frequency.addValue("2");
        Assert.assertEquals(Long.valueOf(1), frequency.freqTable.get(Long.valueOf(2)));
        Assert.assertEquals(1, frequency.getCount(2));
        Assert.assertEquals(1, frequency.getSumFreq());
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 0.333\t 0.333\n2 \t 1 \t 0.333\t 0.666\n";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator values = frequency.valuesIterator();
        Assert.assertTrue(values.hasNext());
        Assert.assertEquals(Long.valueOf(1), values.next());
        Assert.assertTrue(values.hasNext());
        Assert.assertEquals(Long.valueOf(2), values.next());
        Assert.assertFalse(values.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals(1, frequency.getCount(1));
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        Assert.assertEquals(1, frequency.getCount(1L));
    }

    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        Assert.assertEquals(0, frequency.getCount('a'));
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals(0.333, frequency.getPct(1), 0.001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        Assert.assertEquals(0.333, frequency.getPct(1L), 0.001);
    }

    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        Assert.assertEquals(Double.NaN, frequency.getPct('a'));
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals(Long.valueOf(1), frequency.freqTable.get(Long.valueOf(1)));
        Assert.assertEquals(1, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getSumFreq());
        Assert.assertEquals(1, frequency.getCumFreq(1));
        frequency.addValue(2);
        Assert.assertEquals(Long.valueOf(2), frequency.freqTable.get(Long.valueOf(2)));
        Assert.assertEquals(1, frequency.getCount(2));
        Assert.assertEquals(2, frequency.getSumFreq());
        Assert.assertEquals(1, frequency.getCumFreq(1));
        Assert.assertEquals(2, frequency.getCumFreq(2));
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        Assert.assertEquals(Long.valueOf(1), frequency.freqTable.get(Long.valueOf(1)));
        Assert.assertEquals(1, frequency.getCount(1L));
        Assert.assertEquals(1, frequency.getSumFreq());
        Assert.assertEquals(1, frequency.getCumFreq(1L));
        frequency.addValue(2L);
        Assert.assertEquals(Long.valueOf(2), frequency.freqTable.get(Long.valueOf(2)));
        Assert.assertEquals(1, frequency.getCount(2L));
        Assert.assertEquals(2, frequency.getSumFreq());
        Assert.assertEquals(1, frequency.getCumFreq(1L));
        Assert.assertEquals(2, frequency.getCumFreq(2L));
    }

    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        Assert.assertEquals(0, frequency.getCumFreq('a'));
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals(0.333, frequency.getPct(1), 0.001);
        Assert.assertEquals(0.333, frequency.getCumPct(1), 0.001);
        frequency.addValue(2);
        Assert.assertEquals(0.666, frequency.getPct(2), 0.001);
        Assert.assertEquals(0.666, frequency.getCumPct(2), 0.001);
        Assert.assertEquals(1.0, frequency.getCumPct(1), 0.001);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        Assert.assertEquals(0.333, frequency.getPct(1L), 0.001);
        Assert.assertEquals(0.333, frequency.getCumPct(1L), 0.001);
        frequency.addValue(2L);
        Assert.assertEquals(0.666, frequency.getPct(2L), 0.001);
        Assert.assertEquals(0.666, frequency.getCumPct(2L), 0.001);
        Assert.assertEquals(1.0, frequency.getCumPct(1L), 0.001);
    }

    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        Assert.assertEquals(Double.NaN, frequency.getCumPct('a'));
    }
}
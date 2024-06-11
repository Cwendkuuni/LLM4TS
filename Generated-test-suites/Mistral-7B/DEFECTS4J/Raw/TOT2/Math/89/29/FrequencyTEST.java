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
        Assert.assertNotNull(frequency);
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertNull(frequency.toString());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        Assert.assertNotNull(frequency);
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertNull(frequency.toString());
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals(1, frequency.getSumFreq());
        Assert.assertEquals(1, frequency.getCount(1));
        Assert.assertEquals(1.0, frequency.getPct(1), 0.001);
        Assert.assertEquals(1.0, frequency.getCumPct(1), 0.001);
        Assert.assertEquals(1.0, frequency.getCumFreq(1), 0.001);
        Assert.assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 1.0 \t 1.0\n", frequency.toString());
        frequency.addValue(1);
        Assert.assertEquals(2, frequency.getSumFreq());
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(1.0, frequency.getPct(1), 0.001);
        Assert.assertEquals(1.5, frequency.getCumPct(1), 0.001);
        Assert.assertEquals(1.5, frequency.getCumFreq(1), 0.001);
        Assert.assertEquals(1.5, frequency.getCumFreq(2), 0.001);
        Assert.assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 1.0 \t 1.5\n", frequency.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueUncomparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueUncomparableObjects() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(new Object());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals(1, frequency.getSumFreq());
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertNull(frequency.toString());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator iterator = frequency.valuesIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next().longValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next().longValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        Assert.assertEquals(10, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(1, frequency.getCount(i));
        }
        Assert.assertEquals(0, frequency.getCount(11));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        for (int i = 0; i < 10; i++) {
            double expected = (double) i / 10.0;
            Assert.assertEquals(expected, frequency.getPct(i), 0.001);
        }
        Assert.assertEquals(0.0, frequency.getPct(11), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        for (int i = 0; i < 10; i++) {
            long expected = 0;
            if (i > 0) {
                expected = i;
            }
            Assert.assertEquals(expected, frequency.getCumFreq(i));
        }
        Assert.assertEquals(10, frequency.getCumFreq(10));
        Assert.assertEquals(0, frequency.getCumFreq(-1));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        for (int i = 0; i < 10; i++) {
            frequency.addValue(i);
        }
        for (int i = 0; i < 10; i++) {
            double expected = i * 0.1;
            Assert.assertEquals(expected, frequency.getCumPct(i), 0.001);
        }
        Assert.assertEquals(1.0, frequency.getCumPct(10), 0.001);
        Assert.assertEquals(0.0, frequency.getCumPct(-1), 0.001);
    }
}
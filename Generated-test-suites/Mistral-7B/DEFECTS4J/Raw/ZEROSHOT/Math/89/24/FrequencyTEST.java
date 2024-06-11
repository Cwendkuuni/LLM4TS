import org.apache.commons.math.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Test class for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Frequency frequency = new Frequency();
        Assert.assertNotNull(frequency);

        Frequency frequencyComparator = new Frequency(new Comparator<Comparable<?>>() {
            @Override
            public int compare(Comparable<?> o1, Comparable<?> o2) {
                return o1.compareTo(o2);
            }
        });
        Assert.assertNotNull(frequencyComparator);
    }

    /**
     * Test addValue method.
     */
    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        long sumFreq = frequency.getSumFreq();
        Assert.assertEquals(6, sumFreq);

        long count = frequency.getCount(1);
        Assert.assertEquals(2, count);

        count = frequency.getCount(2);
        Assert.assertEquals(2, count);

        count = frequency.getCount(3);
        Assert.assertEquals(2, count);

        double pct = frequency.getPct(1);
        Assert.assertNotNaN(pct);
        Assert.assertEquals(0.3333333333333333, pct, 0.0000001);

        pct = frequency.getPct(2);
        Assert.assertNotNaN(pct);
        Assert.assertEquals(0.3333333333333333, pct, 0.0000001);

        pct = frequency.getPct(3);
        Assert.assertNotNaN(pct);
        Assert.assertEquals(0.3333333333333333, pct, 0.0000001);

        long cumFreq = frequency.getCumFreq(1);
        Assert.assertEquals(2, cumFreq);

        cumFreq = frequency.getCumFreq(2);
        Assert.assertEquals(4, cumFreq);

        cumFreq = frequency.getCumFreq(3);
        Assert.assertEquals(6, cumFreq);

        double cumPct = frequency.getCumPct(1);
        Assert.assertNotNaN(cumPct);
        Assert.assertEquals(0.3333333333333333, cumPct, 0.0000001);

        cumPct = frequency.getCumPct(2);
        Assert.assertNotNaN(cumPct);
        Assert.assertEquals(0.6666666666666667, cumPct, 0.0000001);

        cumPct = frequency.getCumPct(3);
        Assert.assertNotNaN(cumPct);
        Assert.assertEquals(1.0, cumPct, 0.0000001);

        frequency.clear();
        long newSumFreq = frequency.getSumFreq();
        Assert.assertEquals(0, newSumFreq);
    }

    /**
     * Test addValue with different data types.
     */
    @Test
    public void testAddValueDifferentDataTypes() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue('b');
        frequency.addValue('b');

        long sumFreq = frequency.getSumFreq();
        Assert.assertEquals(8, sumFreq);

        long count = frequency.getCount(1);
        Assert.assertEquals(2, count);

        count = frequency.getCount('a');
        Assert.assertEquals(2, count);

        count = frequency.getCount(2);
        Assert.assertEquals(2, count);

        count = frequency.getCount('b');
        Assert.assertEquals(2, count);

        double pct = frequency.getPct(1);
        Assert.assertNotNaN(pct);
        Assert.assertEquals(0.25, pct, 0.0000001);

        pct = frequency.getPct('a');
        Assert.assertNotNaN(pct);
        Assert.assertEquals(0.25, pct, 0.0000001);

        pct = frequency.getPct(2);
        Assert.assertNotNaN(pct);
        Assert.assertEquals(0.25, pct, 0.0000001);

        pct = frequency.getPct('b');
        Assert.assertNotNaN(pct);
        Assert.assertEquals(0.25, pct, 0.0000001);

        long cumFreq = frequency.getCumFreq(1);
        Assert.assertEquals(2, cumFreq);

        cumFreq = frequency.getCumFreq('a');
        Assert.assertEquals(4, cumFreq);

        cumFreq = frequency.getCumFreq(2);
        Assert.assertEquals(6, cumFreq);

        cumFreq = frequency.getCumFreq('b');
        Assert.assertEquals(8, cumFreq);

        double cumPct = frequency.getCumPct(1);
        Assert.assertNotNaN(cumPct);
        Assert.assertEquals(0.25, cumPct, 0.0000001);

        cumPct = frequency.getCumPct('a');
        Assert.assertNotNaN(cumPct);
        Assert.assertEquals(0.5, cumPct, 0.0000001);

        cumPct = frequency.getCumPct(2);
        Assert.assertNotNaN(cumPct);
        Assert.assertEquals(0.75, cumPct, 0.0000001);

        cumPct = frequency.getCumPct('b');
        Assert.assertNotNaN(cumPct);
        Assert.assertEquals(1.0, cumPct, 0.0000001);

        frequency.clear();
        long newSumFreq = frequency.getSumFreq();
        Assert.assertEquals(0, newSumFreq);
    }

    /**
     * Test addValue with invalid data types.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidDataType() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue(2);
        frequency.addValue(2);

        frequency.addValue(new Object());
    }

    /**
     * Test valuesIterator method.
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue('b');
        frequency.addValue('b');

        Iterator<Object> iterator = frequency.valuesIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals('a', iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals('a', iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals('b', iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals('b', iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
}
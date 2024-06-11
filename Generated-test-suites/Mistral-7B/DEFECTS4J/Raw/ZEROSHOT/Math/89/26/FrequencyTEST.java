import org.apache.commons.math.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;

public class FrequencyTest {

    @Test
    public void testEmptyFrequency() {
        Frequency frequency = new Frequency();
        Assert.assertEquals("", frequency.toString());
    }

    @Test
    public void testFrequencyWithComparator() {
        Comparator<Integer> comparator = (o1, o2) -> o1.compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(5);
        frequency.addValue(2);
        frequency.addValue(5);

        Assert.assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "2 \t 1 \t 0.16666666666666663 \t 0.16666666666666663\n" +
                "5 \t 2 \t 0.3333333333333333 \t 0.5\n", frequency.toString());

        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(3, frequency.getCount(5));
        Assert.assertEquals(5, frequency.getSumFreq());

        Assert.assertEquals(0.16666666666666663, frequency.getPct(2), 0.0001);
        Assert.assertEquals(0.3333333333333333, frequency.getPct(5), 0.0001);

        Assert.assertEquals(0.16666666666666663, frequency.getCumPct(2), 0.0001);
        Assert.assertEquals(0.5, frequency.getCumPct(5), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithInvalidType() {
        Frequency frequency = new Frequency();
        frequency.addValue("invalidValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableType() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueWithComparableType() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        Assert.assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "1 \t 3 \t 1.0 \t 1.0\n", frequency.toString());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();

        Assert.assertEquals("", frequency.toString());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        Iterator<Object> valuesIterator = frequency.valuesIterator();
        Assert.assertEquals(1, valuesIterator.next());
        Assert.assertEquals(1, valuesIterator.next());
        Assert.assertEquals(1, valuesiterator.next());
        Assert.assertFalse(valuesIterator.hasNext());
    }
}
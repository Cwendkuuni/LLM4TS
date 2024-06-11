import org.apache.commons.math.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        Assert.assertEquals("Size of frequency table is incorrect.", 3, frequency.valuesIterator().size());
        Assert.assertEquals("Frequency of value 1 is incorrect.", 2, frequency.getCount(1));
        Assert.assertEquals("Frequency of value 2 is incorrect.", 1, frequency.getCount(2));
        Assert.assertEquals("Sum frequency is incorrect.", 3, frequency.getSumFreq());
        Assert.assertEquals("Percentage of value 1 is incorrect.", 0.66666667, frequency.getPct(1), 0.001);
        Assert.assertEquals("Percentage of value 2 is incorrect.", 0.33333333, frequency.getPct(2), 0.001);
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(2);
        frequency.addValue(1);

        Assert.assertEquals("Size of frequency table is incorrect.", 2, frequency.valuesIterator().size());
        Assert.assertEquals("Frequency of value 1 is incorrect.", 1, frequency.getCount(1));
        Assert.assertEquals("Frequency of value 2 is incorrect.", 1, frequency.getCount(2));
        Assert.assertEquals("Sum frequency is incorrect.", 2, frequency.getSumFreq());
        Assert.assertEquals("Percentage of value 1 is incorrect.", 0.5, frequency.getPct(1), 0.001);
        Assert.assertEquals("Percentage of value 2 is incorrect.", 0.5, frequency.getPct(2), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValuesDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue(1L);
    }

    @Test(expected = ClassCastException.class)
    public void testAddValueWithClassCastException() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object() {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithIllegalArgumentException() {
        Frequency frequency = new Frequency();
        frequency.addValue(new String("abc"));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();

        Assert.assertEquals("Size of frequency table is incorrect after clear.", 0, frequency.valuesIterator().size());
    }
}
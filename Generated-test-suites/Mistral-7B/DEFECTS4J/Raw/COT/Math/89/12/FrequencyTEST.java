import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.isEmpty());
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(null));
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount('a'));
        assertEquals(0, frequency.getCumFreq(null));
        assertEquals(0, frequency.getCumFreq(1));
        assertEquals(0, frequency.getCumFreq('a'));
        assertEquals(0, frequency.getCumPct(null));
        assertEquals(0, frequency.getCumPct(1), 0.0);
        assertEquals(0, frequency.getCumPct('a'), 0.0);
        assertEquals(0, frequency.getCumPct(100), 0.0);
    }

    @Test
    public void testFrequencyWithComparatorConstructor() {
        Comparator<Object> comparator = (o1, o2) -> ((Comparable<?>) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        assertTrue(frequency.isEmpty());
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testFrequencyAddValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        assertEquals(4, frequency.getSumFreq());
        assertEquals(3, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertEquals(0, frequency.getCount(2.5));
        assertEquals(1.0 / 4.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 4.0, frequency.getPct(1), 0.001);
        assertEquals(3.0 / 4.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 4.0, frequency.getPct(2), 0.001);

        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));
        assertEquals(4, frequency.getCumFreq(4));
        assertEquals(1.0 / 4.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0 / 4.0, frequency.getCumPct(1), 0.001);
        assertEquals(2.0 / 4.0, frequency.getCumPct(2), 0.001);
        assertEquals(3.0 / 4.0, frequency.getCumPct(3), 0.001);
        assertEquals(4.0 / 4.0, frequency.getCumPct(4), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyAddValueWithNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyAddValueWithNonComparableDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyAddValueWithNonComparableSameTypeButDifferentClasses() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(new Integer(1));
    }

    @Test
    public void testFrequencyClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertTrue(frequency.isEmpty());
    }
}
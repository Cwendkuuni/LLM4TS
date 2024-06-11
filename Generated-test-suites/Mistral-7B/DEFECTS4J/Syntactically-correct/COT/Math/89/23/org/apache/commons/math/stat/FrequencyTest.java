import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.stat.Frequency;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    private Frequency frequency;

    @Before
    public void setUp() {
        frequency = new Frequency();
    }

    // Test case for default constructor and addValue methods
    @Test
    public void testAddValueInt() {
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
        assertEquals(1.0, frequency.getPct(1), 0.01);
        assertEquals(1.0, frequency.getCumPct(1), 0.01);
    }

    @Test
    public void testAddValueLong() {
        frequency.addValue(2L);
        frequency.addValue(2L);
        assertEquals(2, frequency.getCount(2L));
        assertEquals(1.0, frequency.getPct(2L), 0.01);
        assertEquals(1.0, frequency.getCumPct(2L), 0.01);
    }

    @Test
    public void testAddValueShort() {
        frequency.addValue((short) 3);
        frequency.addValue((short) 3);
        assertEquals(2, frequency.getCount((short) 3));
        assertEquals(1.0, frequency.getPct((short) 3), 0.01);
        assertEquals(1.0, frequency.getCumPct((short) 3), 0.01);
    }

    @Test
    public void testAddValueChar() {
        frequency.addValue('a');
        frequency.addValue('a');
        assertEquals(2, frequency.getCount('a'));
        assertEquals(1.0, frequency.getPct('a'), 0.01);
        assertEquals(1.0, frequency.getCumPct('a'), 0.01);
    }

    @Test
    public void testAddValueString() {
        frequency.addValue("test");
        frequency.addValue("test");
        assertEquals(2, frequency.getCount("test"));
        assertEquals(1.0, frequency.getPct("test"), 0.01);
        assertEquals(1.0, frequency.getCumPct("test"), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueClassCastException() {
        frequency.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableType() {
        Frequency frequencyDouble = new Frequency();
        frequencyDouble.addValue(1.0);
        frequency.addValue(frequencyDouble);
    }

    @Test
    public void testAddValueComparator() {
        Comparator<Comparable<?>> comparator = (o1, o2) -> o1.compareTo(o2);
        Frequency frequencyComparator = new Frequency(comparator);
        frequencyComparator.addValue(new Integer(3));
        frequencyComparator.addValue(new Integer(1));
        assertEquals(1, frequencyComparator.getCount(new Integer(1)));
        assertEquals(0.5, frequencyComparator.getPct(new Integer(1)), 0.01);
        assertEquals(0.5, frequencyComparator.getCumPct(new Integer(1)), 0.01);
        assertEquals(1, frequencyComparator.getCount(new Integer(3)));
        assertEquals(0.5, frequencyComparator.getPct(new Integer(3)), 0.01);
        assertEquals(1.0, frequencyComparator.getCumPct(new Integer(3)), 0.01);
    }

    @Test
    public void testGetSumFreq() {
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
    }

    @Test
    public void testGetCountNull() {
        assertNull(frequency.getCount(null));
    }

    @Test
    public void testGetPct() {
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(1.0, frequency.getPct(1), 0.01);
        assertEquals(0.0, frequency.getPct(2));
    }

    @Test
    public void testGetCumPct() {
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(1.0, frequency.getCumPct(1), 0.01);
        assertEquals(1.0, frequency.getCumPct(2), 0.01);
    }

    @Test
    public void testGetCumPctEmpty() {
        assertEquals(0.0, frequency.getCumPct(null), 0.01);
    }

    @Test
    public void testGetCumFreq() {
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
    }

    @Test
    public void testGetCumFreqEmpty() {
        assertEquals(0, frequency.getCumFreq(null));
    }

    @Test
    public void testGetCumFreqNull() {
        frequency.addValue(1);
        frequency.addValue(1);
        assertNull(frequency.getCumFreq(null));
    }

    @Test
    public void testClear() {
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
    }

    @Test
    public void testValuesIterator() {
        frequency.addValue(1);
        frequency.addValue(1);
        Iterator<Long> iterator = frequency.valuesIterator();
        long expectedCount = 2;
        int count = 0;
        while (iterator.hasNext()) {
            Long value = iterator.next();
            assertEquals(expectedCount--, value.longValue());
            count++;
        }
        assertEquals(expectedCount, 0);
        assertEquals(count, 2);
    }
}
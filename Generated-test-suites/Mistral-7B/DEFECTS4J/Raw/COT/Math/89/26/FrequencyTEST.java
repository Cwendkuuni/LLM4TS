import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 1 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.3333333333333333 \t 0.6666666666666667\n3 \t 1 \t 0.3333333333333333 \t 1.0\n";
        assertEquals(expected, result);
    }

    @Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        Object v = new Integer(1);
        frequency.addValue(v);
        long count = frequency.getCount(v);
        assertEquals(1, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueUncomparableObject() {
        Frequency frequency = new Frequency();
        Object v1 = new Integer(1);
        Object v2 = new String("hello");
        frequency.addValue(v1);
        frequency.addValue(v2);
    }

    @Test
    public void testAddValueComparableObject() {
        Frequency frequency = new Frequency();
        Object v = new Integer(1);
        frequency.addValue(v);
        frequency.addValue(v);
        long count = frequency.getCount(v);
        assertEquals(2, count);
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(1, count);
    }

    @Test
    public void testAddValueInteger() {
        Frequency frequency = new Frequency();
        Integer v = new Integer(1);
        frequency.addValue(v);
        long count = frequency.getCount(v);
        assertEquals(1, count);
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        long v = 1;
        frequency.addValue(v);
        long count = frequency.getCount(v);
        assertEquals(1, count);
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        char v = 'a';
        frequency.addValue(v);
        long count = frequency.getCount(v);
        assertEquals(1, count);
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        long result = frequency.getSumFreq();
        assertEquals(6, result);
    }

    @Test
    public void testGetCountObject() {
        Frequency frequency = new Frequency();
        Object v = new Integer(1);
        frequency.addValue(v);
        frequency.addValue(v);

        long count = frequency.getCount(v);
        assertEquals(2, count);
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        long count = frequency.getCount(1);
        assertEquals(2, count);
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);

        long count = frequency.getCount(1L);
        assertEquals(2, count);
    }

    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');

        long count = frequency.getCount('a');
        assertEquals(2, count);
    }

    @Test
    public void testGetPctObject() {
        Frequency frequency = new Frequency();
        Object v = new Integer(1);
        frequency.addValue(v);
        frequency.addValue(v);
        frequency.addValue(new Integer(2));

        double result = frequency.getPct(v);
        assertEquals(0.5, result, 0.001);
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        double result = frequency.getPct(1);
        assertEquals(0.333, result, 0.001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);

        double result = frequency.getPct(1L);
        assertEquals(0.333, result, 0.001);
    }

    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        double result = frequency.getPct('a');
        assertEquals(0.5, result, 0.001);
    }

    @Test
    public void testGetCumFreqObject() {
        Frequency frequency = new Frequency();
        Object v = new Integer(1);
        frequency.addValue(v);
        frequency.addValue(v);
        frequency.addValue(new Integer(2));

        long result = frequency.getCumFreq(v);
        assertEquals(2, result);
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        long result = frequency.getCumFreq(1);
        assertEquals(2, result);
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);

        long result = frequency.getCumFreq(1L);
        assertEquals(2, result);
    }

    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        long result = frequency.getCumFreq('a');
        assertEquals(2, result);
    }

    @Test
    public void testGetCumPctObject() {
        Frequency frequency = new Frequency();
        Object v = new Integer(1);
        frequency.addValue(v);
        frequency.addValue(v);
        frequency.addValue(new Integer(2));

        double result = frequency.getCumPct(v);
        assertEquals(0.667, result, 0.001);
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        double result = frequency.getCumPct(1);
        assertEquals(0.667, result, 0.001);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);

        double result = frequency.getCumPct(1L);
        assertEquals(0.667, result, 0.001);
    }

    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        double result = frequency.getCumPct('a');
        assertEquals(0.667, result, 0.001);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();

        long count = frequency.getCount(1);
        assertEquals(0, count);
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        Iterator<Long> iterator = frequency.valuesIterator();
        Long value = iterator.next();
        assertEquals(1, value.longValue());
        value = iterator.next();
        assertEquals(2, value.longValue());
        assertFalse(iterator.hasNext());
    }
}
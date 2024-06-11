import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(Integer.valueOf(1)));
        assertEquals(0, frequency.getCount(Integer.valueOf(2)));
        assertNull(frequency.toString());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator<Comparable> comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(2));
        assertEquals(2, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(Integer.valueOf(1)));
        assertEquals(1, frequency.getCount(Integer.valueOf(2)));
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 0.5 \t 0.5\n2 \t 1 \t 0.5 \t 1.0\n", frequency.toString());
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        assertEquals(1, frequency.getCount(Integer.valueOf(1)));
    }

    @Test
    public void testAddValueInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        assertEquals(2, frequency.getCount(Integer.valueOf(1)));
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(Long.valueOf(1));
        assertEquals(1, frequency.getCount(Long.valueOf(1)));
    }

    @Test
    public void testAddValueLongValidKey() {
        Frequency frequency = new Frequency();
        frequency.addValue(Long.valueOf(1));
        frequency.addValue(Long.valueOf(1));
        assertEquals(2, frequency.getCount(Long.valueOf(1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidKey() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Character.valueOf('a'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidKeyDeprecated() {
        Frequency frequency = new Frequency();
        frequency.addValue(Long.valueOf(1));
        frequency.addValue(Character.valueOf('a'));
    }

    @Test(expected = ClassCastException.class)
    public void testAddValueInvalidKeyClassCastException() {
        Frequency frequency = new Frequency();
        frequency.addValue(Long.valueOf(1));
        frequency.addValue(new Object());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(Integer.valueOf(1)));
        assertNull(frequency.toString());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        Iterator<Comparable> iterator = frequency.valuesIterator();
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        assertEquals(3, frequency.getCount(Integer.valueOf(1)));
        assertEquals(0, frequency.getCount(Integer.valueOf(2)));
    }

    @Test
    public void testGetCountLongKey() {
        Frequency frequency = new Frequency();
        frequency.addValue(Long.valueOf(1));
        frequency.addValue(Long.valueOf(1));
        frequency.addValue(Long.valueOf(1));
        assertEquals(3, frequency.getCount(Long.valueOf(1)));
    }

    @Test
    public void testGetCountStringKey() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        assertEquals(3, frequency.getCount(String.valueOf(Integer.valueOf(1))));
    }

    @Test
    public void testGetCountCharacterKey() {
        Frequency frequency = new Frequency();
        frequency.addValue(Character.valueOf('a'));
        frequency.addValue(Character.valueOf('a'));
        frequency.addValue(Character.valueOf('a'));
        assertEquals(3, frequency.getCount(Character.valueOf('a')));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        assertEquals(0.3333333333333333, frequency.getPct(Integer.valueOf(1)), 0.000000001);
        assertEquals(Double.NaN, frequency.getPct(Integer.valueOf(2)));
    }

    @Test
    public void testGetPctLongKey() {
        Frequency frequency = new Frequency();
        frequency.addValue(Long.valueOf(1));
        frequency.addValue(Long.valueOf(1));
        frequency.addValue(Long.valueOf(1));
        assertEquals(0.3333333333333333, frequency.getPct(Long.valueOf(1)), 0.000000001);
        assertEquals(Double.NaN, frequency.getPct(Long.valueOf(2)));
    }

    @Test
    public void testGetPctCharacterKey() {
        Frequency frequency = new Frequency();
        frequency.addValue(Character.valueOf('a'));
        frequency.addValue(Character.valueOf('a'));
        frequency.addValue(Character.valueOf('a'));
        assertEquals(0.3333333333333333, frequency.getPct(Character.valueOf('a')), 0.000000001);
        assertEquals(Double.NaN, frequency.getPct(Character.valueOf('b')));
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        assertEquals(1, frequency.getCumFreq(Integer.valueOf(1)));
        assertEquals(2, frequency.getCumFreq(Integer.valueOf(2)));
        assertEquals(3, frequency.getCumFreq(Integer.valueOf(3)));
        assertEquals(3, frequency.getCumFreq(Integer.valueOf(Integer.MAX_VALUE)));
        assertEquals(0, frequency.getCumFreq(Integer.valueOf(Integer.MIN_VALUE)));
    }

    @Test
    public void testGetCumFreqLongKey() {
        Frequency frequency = new Frequency();
        frequency.addValue(Long.valueOf(1));
        frequency.addValue(Long.valueOf(1));
        frequency.addValue(Long.valueOf(1));
        assertEquals(1, frequency.getCumFreq(Long.valueOf(1)));
        assertEquals(2, frequency.getCumFreq(Long.valueOf(2)));
        assertEquals(3, frequency.getCumFreq(Long.valueOf(3)));
        assertEquals(3, frequency.getCumFreq(Long.valueOf(Long.MAX_VALUE)));
        assertEquals(0, frequency.getCumFreq(Long.valueOf(Long.MIN_VALUE)));
    }

    @Test
    public void testGetCumFreqCharacterKey() {
        Frequency frequency = new Frequency();
        frequency.addValue(Character.valueOf('a'));
        frequency.addValue(Character.valueOf('a'));
        frequency.addValue(Character.valueOf('a'));
        assertEquals(1, frequency.getCumFreq(Character.valueOf('a')));
        assertEquals(2, frequency.getCumFreq(Character.valueOf('b')));
        assertEquals(3, frequency.getCumFreq(Character.valueOf('c')));
        assertEquals(3, frequency.getCumFreq(Character.valueOf(Character.MAX_VALUE)));
        assertEquals(0, frequency.getCumFreq(Character.valueOf(Character.MIN_VALUE)));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        assertEquals(0.3333333333333333, frequency.getPct(Integer.valueOf(1)), 0.000000001);
        assertEquals(0.3333333333333333, frequency.getCumPct(Integer.valueOf(1)), 0.000000001);
        assertEquals(0.6666666666666667, frequency.getCumPct(Integer.valueOf(2)), 0.000000001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(Integer.MAX_VALUE)), 0.000000001);
        assertEquals(0.0, frequency.getCumPct(Integer.valueOf(Integer.MIN_VALUE)), 0.000000001);
    }

    @Test
    public void testGetCumPctLongKey() {
        Frequency frequency = new Frequency();
        frequency.addValue(Long.valueOf(1));
        frequency.addValue(Long.valueOf(1));
        frequency.addValue(Long.valueOf(1));
        assertEquals(0.3333333333333333, frequency.getPct(Long.valueOf(1)), 0.000000001);
        assertEquals(0.3333333333333333, frequency.getCumPct(Long.valueOf(1)), 0.000000001);
        assertEquals(0.6666666666666667, frequency.getCumPct(Long.valueOf(2)), 0.000000001);
        assertEquals(1.0, frequency.getCumPct(Long.valueOf(Long.MAX_VALUE)), 0.000000001);
        assertEquals(0.0, frequency.getCumPct(Long.valueOf(Long.MIN_VALUE)), 0.000000001);
    }

    @Test
    public void testGetCumPctCharacterKey() {
        Frequency frequency = new Frequency();
        frequency.addValue(Character.valueOf('a'));
        frequency.addValue(Character.valueOf('a'));
        frequency.addValue(Character.valueOf('a'));
        assertEquals(0.3333333333333333, frequency.getPct(Character.valueOf('a')), 0.000000001);
        assertEquals(0.3333333333333333, frequency.getCumPct(Character.valueOf('a')), 0.000000001);
        assertEquals(0.6666666666666667, frequency.getCumPct(Character.valueOf('b')), 0.000000001);
        assertEquals(1.0, frequency.getCumPct(Character.valueOf(Character.MAX_VALUE)), 0.000000001);
        assertEquals(0.0, frequency.getCumPct(Character.valueOf(Character.MIN_VALUE)), 0.000000001);
    }
}
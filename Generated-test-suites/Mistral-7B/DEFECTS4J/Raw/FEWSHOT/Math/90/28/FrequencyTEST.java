package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount(1.0));
        assertEquals(0, frequency.getCount('a'));
        assertEquals(0, frequency.getCount("test"));
        assertNull(frequency.valuesIterator().next());
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        Object value1 = 1;
        Object value2 = 2.0;
        Object value3 = 'a';
        Object value4 = "test";

        frequency.addValue(value1);
        frequency.addValue(value1);
        frequency.addValue(value2);
        frequency.addValue(value3);
        frequency.addValue(value3);
        frequency.addValue(value4);
        frequency.addValue(value4);

        Iterator iterator = frequency.valuesIterator();
        assertEquals(value1, iterator.next());
        assertEquals(Long.valueOf(2), frequency.getCount(value1));
        assertEquals(value2, iterator.next());
        assertEquals(Long.valueOf(1), frequency.getCount(value2));
        assertEquals(value3, iterator.next());
        assertEquals(Long.valueOf(2), frequency.getCount(value3));
        assertEquals(value4, iterator.next());
        assertEquals(Long.valueOf(2), frequency.getCount(value4));
        assertNull(iterator.hasNext());

        assertEquals(4, frequency.getSumFreq());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue("test");
        frequency.addValue(1);
    }

    @Test
    public void testAddValueWithIntegerValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(Long.valueOf(2), frequency.getCount(1));
        assertEquals(Long.valueOf(1), frequency.getCount(2));
    }

    @Test
    public void testAddValueWithLongValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(Long.valueOf(2), frequency.getCount(1L));
        assertEquals(Long.valueOf(1), frequency.getCount(2L));
    }

    @Test
    public void testAddValueWithShortValue() {
        Frequency frequency = new Frequency();
        frequency.addValue((short) 1);
        frequency.addValue((short) 1);
        frequency.addValue((short) 2);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(Long.valueOf(2), frequency.getCount((short) 1));
        assertEquals(Long.valueOf(1), frequency.getCount((short) 2));
    }

    @Test
    public void testAddValueWithByteValue() {
        Frequency frequency = new Frequency();
        frequency.addValue((byte) 1);
        frequency.addValue((byte) 1);
        frequency.addValue((byte) 2);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(Long.valueOf(2), frequency.getCount((byte) 1));
        assertEquals(Long.valueOf(1), frequency.getCount((byte) 2));
    }

    @Test
    public void testAddValueWithFloatValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1.0f);
        frequency.addValue(1.0f);
        frequency.addValue(2.0f);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(Long.valueOf(2), frequency.getCount(1.0f));
        assertEquals(Long.valueOf(1), frequency.getCount(2.0f));
    }

    @Test
    public void testAddValueWithDoubleValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1.0);
        frequency.addValue(1.0);
        frequency.addValue(2.0);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(Long.valueOf(2), frequency.getCount(1.0));
        assertEquals(Long.valueOf(1), frequency.getCount(2.0));
    }

    @Test
    public void testAddValueWithCharacterValue() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        assertEquals(3, frequency.getSumFreq());
        assertEquals(Long.valueOf(2), frequency.getCount('a'));
        assertEquals(Long.valueOf(1), frequency.getCount('b'));
    }

    @Test
    public void testAddValueWithObjectValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
        frequency.addValue(new Object());
        frequency.addValue(new Object());

        assertEquals(3, frequency.getSumFreq());
        assertEquals(Long.valueOf(2), frequency.getCount(new Object()));
        assertNull(frequency.getCount(new Integer(1)));
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        String expected = "Value    Freq.    Pct.    Cum Pct.\n"
                + "1       2   0.33333333333333336 0.33333333333333336\n"
                + "2       1   0.16666666666666668 0.5\n"
                + "3       1   0.16666666666666668 1\n";
        assertEquals(expected, frequency.toString());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(Long.valueOf(2), frequency.getCount(1));
        assertEquals(Long.valueOf(1), frequency.getCount(2));
        assertNull(frequency.getCount("test"));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(0.33333333333333336, frequency.getPct(1), 0.00001);
        assertEquals(0.16666666666666668, frequency.getPct(2), 0.00001);
        assertEquals(Double.NaN, frequency.getPct("test"));
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(Long.valueOf(1), frequency.getCumFreq(0));
        assertEquals(Long.valueOf(2), frequency.getCumFreq(1));
        assertEquals(Long.valueOf(3), frequency.getCumFreq(2));
        assertEquals(Long.valueOf(3), frequency.getCumFreq(3));
        assertEquals(0, frequency.getCumFreq(-1));
        assertEquals(0, frequency.getCumFreq(Integer.MAX_VALUE));
        assertEquals(Long.valueOf(1), frequency.getCumFreq(Character.MIN_VALUE));
        assertEquals(Long.valueOf(1), frequency.getCumFreq(Character.MAX_VALUE));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(0.33333333333333336, frequency.getPct(1), 0.00001);
        assertEquals(0.16666666666666668, frequency.getPct(2), 0.00001);
        assertEquals(0.5, frequency.getCumPct(1), 0.00001);
        assertEquals(1, frequency.getCumPct(2), 0.00001);
        assertEquals(Double.NaN, frequency.getCumPct("test"));
    }
}
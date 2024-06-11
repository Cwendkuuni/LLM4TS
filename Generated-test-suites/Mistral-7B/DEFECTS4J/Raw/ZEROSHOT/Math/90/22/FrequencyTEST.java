package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

/**
 * Tests for {@link Frequency}.
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);

        Frequency comparatorFrequency = new Frequency(new NaturalComparator());
        assertNotNull(comparatorFrequency);
    }

    /**
     * Test addValue method with int.
     */
    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(1.0 / 3.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumFreq(1), 0.001);

        Iterator values = frequency.valuesIterator();
        assertTrue(values.hasNext());
        assertEquals(1, values.next());
    }

    /**
     * Test addValue method with long.
     */
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(1.0 / 3.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumFreq(1), 0.001);

        assertEquals(2, frequency.getCount(2));
        assertEquals(1.0 / 3.0, frequency.getPct(2), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct(2), 0.001);
        assertEquals(2.0, frequency.getCumFreq(2), 0.001);

        Iterator values2 = frequency.valuesIterator();
        assertTrue(values2.hasNext());
        assertEquals(1, values2.next());
        assertTrue(values2.hasNext());
        assertEquals(2, values2.next());
        assertFalse(values2.hasNext());
    }

    /**
     * Test addValue method with char.
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        assertEquals(3, frequency.getSumFreq());
        assertEquals(2, frequency.getCount('a'));
        assertEquals(2.0 / 3.0, frequency.getPct('a'), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct('a'), 0.001);
        assertEquals(2.0, frequency.getCumFreq('a'), 0.001);

        assertEquals(1, frequency.getCount('b'));
        assertEquals(1.0 / 3.0, frequency.getPct('b'), 0.001);
        assertEquals(3.0 / 3.0, frequency.getCumPct('b'), 0.001);
        assertEquals(3.0, frequency.getCumFreq('b'), 0.001);

        Iterator values = frequency.valuesIterator();
        assertTrue(values.hasNext());
        assertEquals('a', values.next());
        assertTrue(values.hasNext());
        assertEquals('a', values.next());
        assertTrue(values.hasNext());
        assertEquals('b', values.next());
        assertFalse(values.hasNext());
    }

    /**
     * Test addValue method with Object.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueObjectInvalid() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(1);
    }

    /**
     * Test addValue method with Object (valid).
     */
    @Test
    public void testAddValueObjectValid() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(new Integer(1));

        assertEquals(2, frequency.getSumFreq());
        assertEquals(1, frequency.getCount('a'));
        assertEquals(1.0 / 2.0, frequency.getPct('a'), 0.001);
        assertEquals(1.0 / 2.0, frequency.getCumPct('a'), 0.001);
        assertEquals(1.0, frequency.getCumFreq('a'), 0.001);

        assertEquals(1, frequency.getCount(1));
        assertEquals(1.0 / 2.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 2.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumFreq(1), 0.001);

        Iterator values = frequency.valuesIterator();
        assertTrue(values.hasNext());
        assertEquals('a', values.next());
        assertTrue(values.hasNext());
        assertEquals(1, values.next());
        assertFalse(values.hasNext());
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();

        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(1));
        assertEquals(0.0, frequency.getPct(1), 0.001);
        assertEquals(0.0, frequency.getCumPct(1), 0.001);
        assertEquals(0.0, frequency.getCumFreq(1), 0.001);

        assertFalse(frequency.valuesIterator().hasNext());
    }
}
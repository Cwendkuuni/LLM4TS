package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

/**
 * Test class for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor with default comparator.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(null));
        assertEquals(0, frequency.getCumFreq(null));
        assertEquals(0, frequency.getCumPct(null));

        frequency.addValue(1);
        assertEquals(1, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumPct(1), 0.0);

        frequency.addValue(1);
        assertEquals(2, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCumFreq(1));
        assertEquals(1.0 / 2.0, frequency.getCumPct(1), 0.0);
    }

    /**
     * Test constructor with custom comparator.
     */
    @org.junit.Test
    public void testCustomComparatorConstructor() {
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                return Long.compare((Long) o1, (Long) o2);
            }
        };
        Frequency frequency = new Frequency(comparator);

        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1L));
        assertEquals(1, frequency.getCumFreq(1L));
        assertEquals(1.0 / 3.0, frequency.getCumPct(1L), 0.0);
        assertEquals(2.0, frequency.getCumPct(2L), 0.0);
    }

    /**
     * Test addValue method with valid arguments.
     */
    @org.junit.Test
    public void testAddValueValidArguments() {
        Frequency frequency = new Frequency();

        // add valid int values
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1.0 / 3.0, frequency.getCumPct(1), 0.0);
        assertEquals(1.0, frequency.getCumPct(3), 0.0);

        // add valid long values
        frequency.clear();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(3L);
        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1L));
        assertEquals(1, frequency.getCumFreq(1L));
        assertEquals(1.0 / 3.0, frequency.getCumPct(1L), 0.0);
        assertEquals(1.0, frequency.getCumPct(3L), 0.0);

        // add valid char values
        frequency.clear();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        assertEquals(3, frequency.getSumFreq());
        assertEquals(2, frequency.getCount('a'));
        assertEquals(1, frequency.getCumFreq('a'));
        assertEquals(1.0 / 3.0, frequency.getCumPct('a'), 0.0);
        assertEquals(2.0 / 3.0, frequency.getCumPct('b'), 0.0);
    }

    /**
     * Test addValue method with invalid arguments.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidArguments() {
        Frequency frequency = new Frequency();

        // add invalid non-comparable object
        frequency.addValue(new Object());

        // add invalid non-comparable int value
        frequency.clear();
        frequency.addValue(null);

        // add invalid non-comparable long value
        frequency.clear();
        frequency.addValue("string");

        // add invalid non-comparable char value
        frequency.clear();
        frequency.addValue(123);
    }

    /**
     * Test clear method.
     */
    @org.junit.Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCumFreq(1));
        assertEquals(0, frequency.getCumPct(1), 0.0);
    }

    /**
     * Test valuesIterator method.
     */
    @org.junit.Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        Iterator iterator = frequency.valuesIterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test getSumFreq method.
     */
    @org.junit.Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(3, frequency.getSumFreq());
    }

    /**
     * Test getCount method with valid arguments.
     */
    @org.junit.Test
    public void testGetCountValidArguments() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertEquals(1, frequency.getCount(3));
        assertEquals(0, frequency.getCount(null));
        assertEquals(0, frequency.getCount('x'));
        assertEquals(0, frequency.getCount(100L));
    }

    /**
     * Test getCount method with invalid arguments.
     */
    @org.junit.Test
    public void testGetCountInvalidArguments() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        assertNull(frequency.getCount(null));
        assertNull(frequency.getCount("string"));
        assertNull(frequency.getCount(new Object()));
        assertNull(frequency.getCount(100.0));
        assertNull(frequency.getCount(new Integer(-1)));
    }

    /**
     * Test getCumFreq method with valid arguments.
     */
    @org.junit.Test
    public void testGetCumFreqValidArguments() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));
        assertEquals(0, frequency.getCumFreq(null));
        assertEquals(0, frequency.getCumFreq('x'));
        assertEquals(0, frequency.getCumFreq(100L));
    }

    /**
     * Test getCumFreq method with invalid arguments.
     */
    @org.junit.Test
    public void testGetCumFreqInvalidArguments() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        assertEquals(0, frequency.getCumFreq(null));
        assertEquals(0, frequency.getCumFreq("string"));
        assertEquals(0, frequency.getCumFreq(new Object()));
        assertEquals(0, frequency.getCumFreq(100.0));
        assertEquals(0, frequency.getCumFreq(new Integer(-1)));
    }

    /**
     * Test getCumPct method with valid arguments.
     */
    @org.junit.Test
    public void testGetCumPctValidArguments() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        assertEquals(1.0 / 3.0, frequency.getCumPct(1), 0.0);
        assertEquals(2.0 / 3.0, frequency.getCumPct(2), 0.0);
        assertEquals(1.0, frequency.getCumPct(3), 0.0);
        assertEquals(0.0, frequency.getCumPct(null), 0.0);
        assertEquals(0.0, frequency.getCumPct('x'), 0.0);
        assertEquals(0.0, frequency.getCumPct(100L), 0.0);
    }

    /**
     * Test getCumPct method with invalid arguments.
     */
    @org.junit.Test
    public void testGetCumPctInvalidArguments() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        assertEquals(0.0, frequency.getCumPct(null), 0.0);
        assertEquals(0.0, frequency.getCumPct("string"), 0.0);
        assertEquals(0.0, frequency.getCumPct(new Object()), 0.0);
        assertEquals(0.0, frequency.getCumPct(100.0), 0.0);
        assertEquals(0.0, frequency.getCumPct(new Integer(-1)), 0.0);
    }
}
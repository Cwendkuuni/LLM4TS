package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test cases for Frequency class.
 *
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
    }

    /**
     * Test constructor with comparator.
     */
    @Test
    public void testConstructorWithComparator() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
    }

    /**
     * Test addValue method with Comparable values.
     */
    @Test
    public void testAddValueWithComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        long count = frequency.getCount(1);
        assertEquals(2, count);
        long sumFreq = frequency.getSumFreq();
        assertEquals(3, sumFreq);
    }

    /**
     * Test addValue method with non-Comparable value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue("notComparable");
    }

    /**
     * Test addValue method with integral values.
     */
    @Test
    public void testAddValueWithIntegralValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(1);

        long count = frequency.getCount(1);
        assertEquals(3, count);
        count = frequency.getCount(2);
        assertEquals(1, count);
    }

    /**
     * Test addValue method with char values.
     */
    @Test
    public void testAddValueWithCharValues() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');

        long count = frequency.getCount('a');
        assertEquals(2, count);
        count = frequency.getCount('b');
        assertEquals(1, count);
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();

        long sumFreq = frequency.getSumFreq();
        assertNotNull(sumFreq);
        assertEquals(0, sumFreq);
    }

    /**
     * Test getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        long sumFreq = frequency.getSumFreq();
        assertEquals(3, sumFreq);
    }

    /**
     * Test getCount method with Comparable values.
     */
    @Test
    public void testGetCountWithComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        long count = frequency.getCount(1);
        assertEquals(2, count);
        count = frequency.getCount(2);
        assertEquals(1, count);
    }

    /**
     * Test getCount method with non-Comparable value.
     */
    @Test
    public void testGetCountWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue("notComparable");

        long count = frequency.getCount("notComparable");
        assertNull(count);
    }

    /**
     * Test getCount method with integral values.
     */
    @Test
    public void testGetCountWithIntegralValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(1);

        long count = frequency.getCount(1);
        assertEquals(3, count);
        count = frequency.getCount(2);
        assertEquals(1, count);
    }

    /**
     * Test getCount method with char values.
     */
    @Test
    public void testGetCountWithCharValues() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');

        long count = frequency.getCount('a');
        assertEquals(2, count);
        count = frequency.getCount('b');
        assertEquals(1, count);
    }

    /**
     * Test getPct method with Comparable values.
     */
    @Test
    public void testGetPctWithComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        double pct = frequency.getPct(1);
        assertEquals(0.66, pct, 0.01);
        pct = frequency.getPct(2);
        assertEquals(0.33, pct, 0.01);
    }

    /**
     * Test getPct method with non-Comparable value.
     */
    @Test
    public void testGetPctWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue("notComparable");

        assertThrows(IllegalArgumentException.class, () -> frequency.getPct("notComparable"));
    }

    /**
     * Test getPct method with integral values.
     */
    @Test
    public void testGetPctWithIntegralValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(1);

        double pct = frequency.getPct(1);
        assertEquals(0.6, pct, 0.01);
        pct = frequency.getPct(2);
        assertEquals(0.33, pct, 0.01);
    }

    /**
     * Test getPct method with char values.
     */
    @Test
    public void testGetPctWithCharValues() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');

        double pct = frequency.getPct('a');
        assertEquals(0.66, pct, 0.01);
        pct = frequency.getPct('b');
        assertEquals(0.33, pct, 0.01);
    }

    /**
     * Test getCumFreq method with Comparable values.
     */
    @Test
    public void testGetCumFreqWithComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        long cumFreq = frequency.getCumFreq(1);
        assertEquals(1, cumFreq);
        cumFreq = frequency.getCumFreq(2);
        assertEquals(3, cumFreq);
    }

    /**
     * Test getCumFreq method with non-Comparable value.
     */
    @Test
    public void testGetCumFreqWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue("notComparable");

        assertThrows(IllegalArgumentException.class, () -> frequency.getCumFreq("notComparable"));
    }

    /**
     * Test getCumFreq method with integral values.
     */
    @Test
    public void testGetCumFreqWithIntegralValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        long cumFreq = frequency.getCumFreq(1);
        assertEquals(1, cumFreq);
        cumFreq = frequency.getCumFreq(2);
        assertEquals(3, cumFreq);
    }

    /**
     * Test getCumFreq method with char values.
     */
    @Test
    public void testGetCumFreqWithCharValues() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');

        long cumFreq = frequency.getCumFreq('a');
        assertEquals(1, cumFreq);
        cumFreq = frequency.getCumFreq('b');
        assertEquals(3, cumFreq);
    }

    /**
     * Test getCumPct method with Comparable values.
     */
    @Test
    public void testGetCumPctWithComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        double cumPct = frequency.getCumPct(1);
        assertEquals(0.33, cumPct, 0.01);
        cumPct = frequency.getCumPct(2);
        assertEquals(1.0, cumPct, 0.01);
    }

    /**
     * Test getCumPct method with non-Comparable value.
     */
    @Test
    public void testGetCumPctWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue("notComparable");

        assertThrows(IllegalArgumentException.class, () -> frequency.getCumPct("notComparable"));
    }

    /**
     * Test getCumPct method with integral values.
     */
    @Test
    public void testGetCumPctWithIntegralValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        double cumPct = frequency.getCumPct(1);
        assertEquals(0.33, cumPct, 0.01);
        cumPct = frequency.getCumPct(2);
        assertEquals(1.0, cumPct, 0.01);
    }

    /**
     * Test getCumPct method with char values.
     */
    @Test
    public void testGetCumPctWithCharValues() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');

        double cumPct = frequency.getCumPct('a');
        assertEquals(0.33, cumPct, 0.01);
        cumPct = frequency.getCumPct('b');
        assertEquals(1.0, cumPct, 0.01);
    }
}
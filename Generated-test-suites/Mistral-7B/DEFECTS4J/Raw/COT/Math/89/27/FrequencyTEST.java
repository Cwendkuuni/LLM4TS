package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

/**
 * FrequencyTest
 */
public class FrequencyTest {

    /**
     * Test constructor Frequency()
     */
    @Test
    public void testFrequencyConstructor() {
        Frequency freq = new Frequency();
        assertTrue(freq instanceof Frequency);
        assertEquals(0, freq.getSumFreq());
        assertNotNull(freq.freqTable);
    }

    /**
     * Test constructor Frequency(Comparator)
     */
    @Test
    public void testFrequencyConstructorComparator() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);
        assertTrue(freq instanceof Frequency);
        assertEquals(0, freq.getSumFreq());
        assertNotNull(freq.freqTable);
    }

    /**
     * Test method addValue
     */
    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(2, freq.getSumFreq());
        assertEquals(Long.valueOf(2), freq.freqTable.get(Long.valueOf(1)));
    }

    /**
     * Test method addValue with null value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullValue() {
        Frequency freq = new Frequency();
        freq.addValue(null);
    }

    /**
     * Test method addValue with different types
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueDifferentTypes() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
    }

    /**
     * Test method addValue with different Comparators
     */
    @Test
    public void testAddValueDifferentComparators() {
        Comparator comparator1 = new NaturalComparator();
        Frequency freq1 = new Frequency(comparator1);
        freq1.addValue(1);
        freq1.addValue(2);

        Comparator comparator2 = new ReversedComparator();
        Frequency freq2 = new Frequency(comparator2);
        freq2.addValue(1);
        freq2.addValue(2);

        // check that the results are the same, but ordered differently
        assertEquals(freq1.getSumFreq(), freq2.getSumFreq());
        Iterator it1 = freq1.valuesIterator();
        Iterator it2 = freq2.valuesIterator();
        Object value1 = it1.next();
        Object value2 = it2.next();
        while (it1.hasNext()) {
            Object next1 = it1.next();
            Object next2 = it2.next();
            assertEquals(next1, next2);
        }
    }

    /**
     * Test method clear
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
        assertFalse(freq.freqTable.containsKey(Long.valueOf(1)));
    }

    /**
     * Test method getCount
     */
    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(2, freq.getCount(Long.valueOf(1)));
        assertEquals(0, freq.getCount(Long.valueOf(2)));
    }

    /**
     * Test method getCount with null value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCountNullValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.getCount(null);
    }

    /**
     * Test method getCount with different types
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCountDifferentTypes() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.getCount('a');
    }

    /**
     * Test method getSumFreq
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(2, freq.getSumFreq());
    }

    /**
     * Test method valuesIterator
     */
    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        Iterator values = freq.valuesIterator();
        Object value = values.next();
        assertEquals(Long.valueOf(1), value);
        assertTrue(values.hasNext());
        value = values.next();
        assertEquals(Long.valueOf(1), value);
        assertFalse(values.hasNext());
    }

    /**
     * Test method getPct
     */
    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(Double.valueOf(0.5), freq.getPct(Long.valueOf(1)));
        assertEquals(Double.valueOf(0.5), freq.getPct(Long.valueOf(1)));
        assertEquals(Double.NaN, freq.getPct(Long.valueOf(2)));
    }

    /**
     * Test method getPct with null value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPctNullValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.getPct(null);
    }

    /**
     * Test method getPct with different types
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPctDifferentTypes() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.getPct('a');
    }

    /**
     * Test method getCumFreq
     */
    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(Long.valueOf(1), freq.getCumFreq(Long.valueOf(1)));
        assertEquals(Long.valueOf(2), freq.getCumFreq(Long.valueOf(1)));
        assertEquals(Long.valueOf(3), freq.getCumFreq(Long.valueOf(1)));
        assertEquals(Long.valueOf(3), freq.getCumFreq(Long.valueOf(2)));
        assertEquals(Long.valueOf(3), freq.getCumFreq(Long.valueOf(3)));
        assertEquals(Long.valueOf(3), freq.getCumFreq(null));
    }

    /**
     * Test method getCumFreq with null value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCumFreqNullValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.getCumFreq(null);
    }

    /**
     * Test method getCumFreq with different types
     */
    @Test
    public void testGetCumFreqDifferentTypes() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.getCumFreq('a');
    }

    /**
     * Test method getCumPct
     */
    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(Double.valueOf(0.333), freq.getCumPct(Long.valueOf(1)));
        assertEquals(Double.valueOf(0.667), freq.getCumPct(Long.valueOf(1)));
        assertEquals(Double.valueOf(1.0), freq.getCumPct(Long.valueOf(1)));
        assertEquals(Double.NaN, freq.getCumPct(Long.valueOf(2)));
    }

    /**
     * Test method getCumPct with null value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCumPctNullValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.getCumPct(null);
    }

    /**
     * Test method getCumPct with different types
     */
    @Test
    public void testGetCumPctDifferentTypes() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.getCumPct('a');
    }
}

/**
 * ReversedComparator
 */
class ReversedComparator implements Comparator, Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = 1L;

    /**
     * Compare the two Object arguments.
     * This method is equivalent to:
     * <pre>-(({@link Comparable Comparable})o1).{@link Comparable#compareTo compareTo}(o2)</pre>
     * 
     * @param  o1 the first object
     * @param  o2 the second object
     * @return result of comparison
     * @throws NullPointerException when <i>o1</i> is <code>null</code>, 
     *         or when <code>((Comparable)o1).compareTo(o2)</code> does
     * @throws ClassCastException when <i>o1</i> is not a {@link Comparable Comparable}, 
     *         or when <code>((Comparable)o1).compareTo(o2)</code> does
     */
    public int compare(Object o1, Object o2) {
        return ((Comparable)o1).compareTo(o2) * -1;
    }
}
//
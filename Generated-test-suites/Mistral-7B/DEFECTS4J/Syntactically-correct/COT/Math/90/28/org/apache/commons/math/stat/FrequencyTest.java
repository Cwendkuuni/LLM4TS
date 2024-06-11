package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math3.util.Precision;

/**
 * Test cases for Frequency class.
 */
public class FrequencyTest {

    private static final Comparator NATURAL_COMPARATOR = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            return ((Comparable) o1).compareTo(o2);
        }
    };

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Frequency freq = new Frequency();
        assertNotNull(freq.freqTable);

        Frequency comparatorFreq = new Frequency(NATURAL_COMPARATOR);
        assertNotNull(comparatorFreq.freqTable);
    }

    /**
     * Test addValue method with default comparator.
     */
    @Test
    public void testAddValueWithDefaultComparator() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(1);
        freq.addValue("one");
        freq.addValue('a');

        long sumFreq = freq.getSumFreq();
        assertEquals(4, sumFreq);

        long count1 = freq.getCount(1);
        assertEquals(2, count1);

        long count1l = freq.getCount(1L);
        assertEquals(2, count1l);

        long countOne = freq.getCount("one");
        assertEquals(1, countOne);

        long countA = freq.getCount('a');
        assertEquals(1, countA);
    }

    /**
     * Test addValue method with custom comparator.
     */
    @Test
    public void testAddValueWithCustomComparator() {
        Comparator customComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o2).compareTo(o1);
            }
        };

        Frequency freq = new Frequency(customComparator);

        freq.addValue(1);
        freq.addValue(1);
        freq.addValue("one");
        freq.addValue('a');

        long sumFreq = freq.getSumFreq();
        assertEquals(4, sumFreq);

        long count1 = freq.getCount(1);
        assertEquals(1, count1);

        long count1l = freq.getCount(1L);
        assertEquals(1, count1l);

        long countOne = freq.getCount("one");
        assertEquals(2, countOne);

        long countA = freq.getCount('a');
        assertEquals(1, countA);
    }

    /**
     * Test addValue method with invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithInvalidArgument() {
        Frequency freq = new Frequency();
        freq.addValue(new Object());
    }

    /**
     * Test valuesIterator method.
     */
    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue("one");
        freq.addValue('a');

        Iterator iterator = freq.valuesIterator();
        assertEquals(1, iterator.next().longValue());
        assertEquals("one", iterator.next());
        assertEquals('a', iterator.next().charValue());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue("one");
        freq.addValue('a');

        long sumFreq = freq.getSumFreq();
        assertEquals(4, sumFreq);
    }

    /**
     * Test getCount method with valid arguments.
     */
    @Test
    public void testGetCountWithValidArguments() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue("one");
        freq.addValue('a');

        long count1 = freq.getCount(1);
        assertEquals(2, count1);

        long countOne = freq.getCount("one");
        assertEquals(1, countOne);

        long countA = freq.getCount('a');
        assertEquals(1, countA);

        long countNull = freq.getCount(null);
        assertEquals(0, countNull);

        long countIntegerNull = freq.getCount(null);
        assertEquals(0, countIntegerNull);

        long countCharNull = freq.getCount(null);
        assertEquals(0, countCharNull);
    }

    /**
     * Test getCount method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCountWithInvalidArguments() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue("one");
        freq.addValue('a');

        freq.getCount(new Object());
    }

    /**
     * Test getPct method with valid arguments.
     */
    @Test
    public void testGetPctWithValidArguments() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue("one");
        freq.addValue('a');

        double pct1 = freq.getPct(1);
        assertEquals(0.5, pct1, Precision.EPSILON);

        double pctOne = freq.getPct("one");
        assertEquals(0.25, pctOne, Precision.EPSILON);

        double pctA = freq.getPct('a');
        assertEquals(0.25, pctA, Precision.EPSILON);

        double pctNull = freq.getPct(null);
        assertEquals(0.0, pctNull, Precision.EPSILON);

        double pctIntegerNull = freq.getPct(null);
        assertEquals(0.0, pctIntegerNull, Precision.EPSILON);

        double pctCharNull = freq.getPct(null);
        assertEquals(0.0, pctCharNull, Precision.EPSILON);
    }

    /**
     * Test getPct method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPctWithInvalidArguments() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue("one");
        freq.addValue('a');

        freq.getPct(new Object());
    }

    /**
     * Test getCumFreq method with valid arguments.
     */
    @Test
    public void testGetCumFreqWithValidArguments() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue("one");
        freq.addValue('a');

        long cumFreq1 = freq.getCumFreq(1);
        assertEquals(1, cumFreq1);

        long cumFreqOne = freq.getCumFreq("one");
        assertEquals(1, cumFreqOne);

        long cumFreqA = freq.getCumFreq('a');
        assertEquals(2, cumFreqA);

        long cumFreqNull = freq.getCumFreq(null);
        assertEquals(0, cumFreqNull);

        long cumFreqIntegerNull = freq.getCumFreq(null);
        assertEquals(0, cumFreqIntegerNull);

        long cumFreqCharNull = freq.getCumFreq(null);
        assertEquals(0, cumFreqCharNull);
    }

    /**
     * Test getCumFreq method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCumFreqWithInvalidArguments() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue("one");
        freq.addValue('a');

        freq.getCumFreq(new Object());
    }

    /**
     * Test getCumPct method with valid arguments.
     */
    @Test
    public void testGetCumPctWithValidArguments() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue("one");
        freq.addValue('a');

        double cumPct1 = freq.getCumPct(1);
        assertEquals(0.25, cumPct1, Precision.EPSILON);

        double cumPctOne = freq.getCumPct("one");
        assertEquals(0.375, cumPctOne, Precision.EPSILON);

        double cumPctA = freq.getCumPct('a');
        assertEquals(0.625, cumPctA, Precision.EPSILON);

        double cumPctNull = freq.getCumPct(null);
        assertEquals(0.0, cumPctNull, Precision.EPSILON);

        double cumPctIntegerNull = freq.getCumPct(null);
        assertEquals(0.0, cumPctIntegerNull, Precision.EPSILON);

        double cumPctCharNull = freq.getCumPct(null);
        assertEquals(0.0, cumPctCharNull, Precision.EPSILON);
    }

    /**
     * Test getCumPct method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCumPctWithInvalidArguments() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue("one");
        freq.addValue('a');

        freq.getCumPct(new Object());
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue("one");
        freq.addValue('a');

        long sumFreq = freq.getSumFreq();
        assertEquals(3, sumFreq);

        freq.clear();

        sumFreq = freq.getSumFreq();
        assertEquals(0, sumFreq);
    }
}
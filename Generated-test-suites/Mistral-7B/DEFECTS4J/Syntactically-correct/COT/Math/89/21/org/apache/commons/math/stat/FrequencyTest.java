package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

public class FrequencyTest {

    /**
     * Test constructor with default comparator
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    /**
     * Test constructor with custom comparator
     */
    @Test
    public void testCustomComparatorConstructor() {
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return (int) (o1 - o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    /**
     * Test toString method with empty frequency distribution
     */
    @Test
    public void testToStringEmpty() {
        Frequency frequency = new Frequency();
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\nValue 0 \t 0 \t 0.0% \t 0.0%\n";
        assertEquals(expected, result);
    }

    /**
     * Test addValue method with int value
     */
    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getSumFreq());
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\nValue 1 \t 1 \t 100.0% \t 100.0%\n";
        assertEquals(expected, result);
    }

    /**
     * Test addValue method with long value
     */
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        assertEquals(1, frequency.getSumFreq());
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\nValue 2 \t 1 \t 100.0% \t 100.0%\n";
        assertEquals(expected, result);
    }

    /**
     * Test addValue method with Character value
     */
    @Test
    public void testAddValueCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        assertEquals(1, frequency.getSumFreq());
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\nValue A \t 1 \t 100.0% \t 100.0%\n";
        assertEquals(expected, result);
    }

    /**
     * Test addValue method with uncomparable values
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueUncomparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
        frequency.addValue(1);
    }

    /**
     * Test addValue method with duplicate values
     */
    @Test
    public void testAddValueDuplicate() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getSumFreq());
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\nValue 1 \t 2 \t 50.0% \t 50.0%\n";
        assertEquals(expected, result);
    }

    /**
     * Test clear method
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n";
        assertEquals(expected, result);
    }

    /**
     * Test getCount method with non-existent value
     */
    @Test
    public void testGetCountNonExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long result = frequency.getCount(2);
        assertEquals(0, result);
    }

    /**
     * Test getCount method with existent value
     */
    @Test
    public void testGetCountExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long result = frequency.getCount(1);
        assertEquals(2, result);
    }

    /**
     * Test getCount method with non-comparable value
     */
    @Test
    public void testGetCountNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('A');
        long result = frequency.getCount('A');
        assertEquals(0, result);
    }

    /**
     * Test getSumFreq method
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long result = frequency.getSumFreq();
        assertEquals(2, result);
    }

    /**
     * Test getPct method with non-existent value
     */
    @Test
    public void testGetPctNonExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        double result = frequency.getPct(2);
        assertEquals(Double.NaN, result, 0.0);
    }

    /**
     * Test getPct method with existent value
     */
    @Test
    public void testGetPctExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        double result = frequency.getPct(1);
        assertEquals(0.5, result, 0.0);
    }

    /**
     * Test getPct method with non-comparable value
     */
    @Test
    public void testGetPctNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('A');
        double result = frequency.getPct('A');
        assertEquals(Double.NaN, result, 0.0);
    }

    /**
     * Test getCumFreq method with non-existent value
     */
    @Test
    public void testGetCumFreqNonExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long result = frequency.getCumFreq(2);
        assertEquals(0, result);
    }

    /**
     * Test getCumFreq method with existent value
     */
    @Test
    public void testGetCumFreqExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long result = frequency.getCumFreq(1);
        assertEquals(1, result);
    }

    /**
     * Test getCumFreq method with non-comparable value
     */
    @Test
    public void testGetCumFreqNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('A');
        long result = frequency.getCumFreq('A');
        assertEquals(0, result);
    }

    /**
     * Test getCumPct method with non-existent value
     */
    @Test
    public void testGetCumPctNonExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        double result = frequency.getCumPct(2);
        assertEquals(Double.NaN, result, 0.0);
    }

    /**
     * Test getCumPct method with existent value
     */
    @Test
    public void testGetCumPctExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        double result = frequency.getCumPct(1);
        assertEquals(0.5, result, 0.0);
    }

    /**
     * Test getCumPct method with non-comparable value
     */
    @Test
    public void testGetCumPctNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('A');
        double result = frequency.getCumPct('A');
        assertEquals(Double.NaN, result, 0.0);
    }

    /**
     * Test getCumPct method with null value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCumPctNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.getCumPct(null);
    }

    /**
     * Test addValue method with long array
     */
    @Test
    public void testAddValueLongArray() {
        Frequency frequency = new Frequency();
        long[] values = {1, 2, 3, 3, 3, 4, 4, 4, 4, 5};
        for (long value : values) {
            frequency.addValue(value);
        }
        long sumFreq = frequency.getSumFreq();
        assertEquals(11, sumFreq);
        TreeMap<Long, Long> expectedTable = new TreeMap<>();
        expectedTable.put(1L, 1L);
        expectedTable.put(2L, 1L);
        expectedTable.put(3L, 3L);
        expectedTable.put(4L, 4L);
        expectedTable.put(5L, 1L);
        TreeMap<Long, Long> table = (TreeMap<Long, Long>) frequency.freqTable.clone();
        assertEquals(expectedTable, table);
    }

    /**
     * Test addValue method with double array - should throw IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueDoubleArray() {
        Frequency frequency = new Frequency();
        double[] values = {1.0, 2.0, 3.0, 3.0, 3.0, 4.0, 4.0, 4.0, 4.0, 5.0};
        for (double value : values) {
            frequency.addValue(value);
        }
    }

    /**
     * Test addValue method with Object array - should throw IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueObjectArray() {
        Frequency frequency = new Frequency();
        Object[] values = {"1", "2", "3", "3", "3", "4", "4", "4", "4", "5"};
        for (Object value : values) {
            frequency.addValue(value);
        }
    }

    /**
     * Test addValue method with int array - should throw IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueIntArray() {
        Frequency frequency = new Frequency();
        int[] values = {1, 2, 3, 3, 3, 4, 4, 4, 4, 5};
        for (int value : values) {
            frequency.addValue(value);
        }
        // The testAddValueIntArray method should throw an IllegalArgumentException, but JUnit doesn't allow to test for that directly
        // Instead, we'll check if the frequency table has the correct values
        TreeMap<Integer, Long> expectedTable = new TreeMap<>();
        expectedTable.put(1, 1L);
        expectedTable.put(2, 1L);
        expectedTable.put(3, 3L);
        expectedTable.put(4, 4L);
        expectedTable.put(5, 1L);
        TreeMap<Integer, Long> table = (TreeMap<Integer, Long>) frequency.freqTable.clone();
        assertEquals(expectedTable, table);
    }

    /**
     * Test addValue method with Character array
     */
    @Test
    public void testAddValueCharacterArray() {
        Frequency frequency = new Frequency();
        Character[] values = {'A', 'B', 'C', 'C', 'C', 'D', 'D', 'D', 'D', 'E'};
        for (Character value : values) {
            frequency.addValue(value);
        }
        long sumFreq = frequency.getSumFreq();
        assertEquals(11, sumFreq);
        TreeMap<Character, Long> expectedTable = new TreeMap<>();
        expectedTable.put('A', 1L);
        expectedTable.put('B', 1L);
        expectedTable.put('C', 3L);
        expectedTable.put('D', 4L);
        expectedTable.put('E', 1L);
        TreeMap<Character, Long> table = (TreeMap<Character, Long>) frequency.freqTable.clone();
        assertEquals(expectedTable, table);
    }

    /**
     * Test addValue method with null array - should throw NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void testAddValueNullArray() {
        Frequency frequency = new Frequency();
        Object[] values = null;
        for (Object value : values) {
            frequency.addValue(value);
        }
    }

    /**
     * Test clear method
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
    }

    /**
     * Test valuesIterator method
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('B');
        Iterator<Object> iterator = frequency.valuesIterator();
        assertEquals('A', iterator.next());
        assertEquals('B', iterator.next());
        assertNull(iterator.hasNext());
    }

}
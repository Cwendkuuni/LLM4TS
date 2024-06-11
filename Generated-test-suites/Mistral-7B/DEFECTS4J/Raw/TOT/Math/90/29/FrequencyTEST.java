package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testFrequencyWithIntegerValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);

        assertEquals("Test sum frequency.", 8, frequency.getSumFreq());
        assertEquals("Test frequency of value 1.", 2, frequency.getCount(1));
        assertEquals("Test frequency of value 2.", 1, frequency.getCount(2));
        assertEquals("Test frequency of value 3.", 4, frequency.getCount(3));
        assertEquals("Test percentage of value 1.", 0.25, frequency.getPct(1), 0.01);
        assertEquals("Test percentage of value 2.", 0.125, frequency.getPct(2), 0.01);
        assertEquals("Test percentage of value 3.", 0.5, frequency.getPct(3), 0.01);

        Iterator iterator = frequency.valuesIterator();
        int i = 0;
        while (iterator.hasNext()) {
            Object value = iterator.next();
            long expectedCount = i + 1;
            long actualCount = frequency.getCount(value);
            long expectedPct = (double) expectedCount / 8.0;
            double actualPct = frequency.getPct(value);

            assertEquals("Test count and percentage for value " + value,
                    expectedCount, actualCount);
            assertEquals("Test percentage for value " + value,
                    expectedPct, actualPct, 0.01);
            i++;
        }
    }

    @Test
    public void testFrequencyWithLongValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(3L);
        frequency.addValue(3L);
        frequency.addValue(3L);

        assertEquals("Test sum frequency.", 8, frequency.getSumFreq());
        assertEquals("Test frequency of value 1.", 2, frequency.getCount(1L));
        assertEquals("Test frequency of value 2.", 1, frequency.getCount(2L));
        assertEquals("Test frequency of value 3.", 4, frequency.getCount(3L));
        assertEquals("Test percentage of value 1.", 0.25, frequency.getPct(1L), 0.01);
        assertEquals("Test percentage of value 2.", 0.125, frequency.getPct(2L), 0.01);
        assertEquals("Test percentage of value 3.", 0.5, frequency.getPct(3L), 0.01);

        Iterator iterator = frequency.valuesIterator();
        int i = 0;
        while (iterator.hasNext()) {
            Object value = iterator.next();
            long expectedCount = i + 1;
            long actualCount = frequency.getCount(value);
            long expectedPct = (double) expectedCount / 8.0;
            double actualPct = frequency.getPct(value);

            assertEquals("Test count and percentage for value " + value,
                    expectedCount, actualCount);
            assertEquals("Test percentage for value " + value,
                    expectedPct, actualPct, 0.01);
            i++;
        }
    }

    @Test
    public void testFrequencyWithStringValues() {
        Frequency frequency = new Frequency();
        frequency.addValue("a");
        frequency.addValue("a");
        frequency.addValue("b");
        frequency.addValue("b");
        frequency.addValue("c");
        frequency.addValue("c");

        assertEquals("Test sum frequency.", 6, frequency.getSumFreq());
        assertEquals("Test frequency of value a.", 2, frequency.getCount("a"));
        assertEquals("Test frequency of value b.", 2, frequency.getCount("b"));
        assertEquals("Test frequency of value c.", 2, frequency.getCount("c"));
        assertEquals("Test percentage of value a.", 0.33, frequency.getPct("a"), 0.01);
        assertEquals("Test percentage of value b.", 0.33, frequency.getPct("b"), 0.01);
        assertEquals("Test percentage of value c.", 0.33, frequency.getPct("c"), 0.01);

        Iterator iterator = frequency.valuesIterator();
        int i = 0;
        while (iterator.hasNext()) {
            Object value = iterator.next();
            long expectedCount = i + 1;
            long actualCount = frequency.getCount(value);
            long expectedPct = (double) expectedCount / 6.0;
            double actualPct = frequency.getPct(value);

            assertEquals("Test count and percentage for value " + value,
                    expectedCount, actualCount);
            assertEquals("Test percentage for value " + value,
                    expectedPct, actualPct, 0.01);
            i++;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyWithNonComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
        frequency.addValue(new Object());
    }

    @Test
    public void testFrequencyWithComparator() {
        Frequency frequency = new Frequency(String::compareTo);
        frequency.addValue("z");
        frequency.addValue("c");
        frequency.addValue("b");
        frequency.addValue("a");

        assertEquals("Test sum frequency.", 4, frequency.getSumFreq());
        assertEquals("Test frequency of value a.", 1, frequency.getCount("a"));
        assertEquals("Test frequency of value b.", 1, frequency.getCount("b"));
        assertEquals("Test frequency of value c.", 1, frequency.getCount("c"));
        assertEquals("Test frequency of value z.", 1, frequency.getCount("z"));
        assertEquals("Test percentage of value a.", 0.25, frequency.getPct("a"), 0.01);
        assertEquals("Test percentage of value b.", 0.25, frequency.getPct("b"), 0.01);
        assertEquals("Test percentage of value c.", 0.25, frequency.getPct("c"), 0.01);
        assertEquals("Test percentage of value z.", 0.25, frequency.getPct("z"), 0.01);

        Iterator iterator = frequency.valuesIterator();
        int i = 0;
        while (iterator.hasNext()) {
            Object value = iterator.next();
            long expectedCount = i + 1;
            long actualCount = frequency.getCount(value);
            long expectedPct = (double) expectedCount / 4.0;
            double actualPct = frequency.getPct(value);

            assertEquals("Test count and percentage for value " + value,
                    expectedCount, actualCount);
            assertEquals("Test percentage for value " + value,
                    expectedPct, actualPct, 0.01);
            i++;
        }
    }

    @Test
    public void testFrequencyWithEmptyCollection() {
        Frequency frequency = new Frequency();
        assertEquals("Test sum frequency.", 0, frequency.getSumFreq());
        assertEquals("Test frequency of any value.", 0, frequency.getCount(1));
        assertEquals("Test percentage of any value.", Double.NaN, frequency.getPct(1));
    }

    @Test
    public void testFrequencyWithNullInput() {
        Frequency frequency = new Frequency();
        try {
            frequency.addValue(null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFrequencyWithNullValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        try {
            Iterator iterator = frequency.valuesIterator();
            iterator.next();
            iterator.remove();
            iterator.next();
            iterator.remove();
            iterator.next();
            iterator.remove();
            iterator.next();
            iterator.remove();
            iterator.next();
            iterator.remove();
            iterator = null;
            iterator.hasNext();
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFrequencyToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);

        String expectedOutput = "Value      Freq.     Pct.      Cum Pct.\n" +
                "1           2        0.25        0.25       \n" +
                "2           3        0.375       0.625       \n" +
                "3           3        0.375       1.000       \n";

        String actualOutput = frequency.toString();
        assertEquals("Test toString method.", expectedOutput, actualOutput);
    }
}
package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

public class FrequencyTest {

    private Frequency frequency;
    private Frequency frequencyWithComparator;

    @Before
    public void setup() {
        frequency = new Frequency();
        frequencyWithComparator = new Frequency(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        });
    }

    @Test
    public void testDefaultConstructor() {
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        Iterator<Object> iterator = frequency.valuesIterator();
        List<Object> values = Arrays.asList(iterator.next(), iterator.next(), iterator.next(), iterator.next());

        // Testing the sum frequency
        assertThat(frequency.getSumFreq(), is(4L));

        // Testing the frequency of each value
        for (Object value : values) {
            assertThat(frequency.getCount(value), is(2L));
        }

        // Testing the percentage of each value
        for (Object value : values) {
            double expectedPct = (double) frequency.getCount(value) / (double) frequency.getSumFreq();
            double actualPct = frequency.getPct(value);

            // Check if the percentage is equal to the expected one
            assertThat(actualPct, is(expectedPct));
        }

        // Testing the cumulative frequency
        for (Object value : values) {
            long cumulativeFreq = frequency.getCumFreq(value);

            // Check if the cumulative frequency is equal to the sum of the frequencies before the current value
            long sumFreqBeforeCurrent = 0;
            for (Object valueBeforeCurrent : values) {
                if (frequency.compare(value, valueBeforeCurrent) > 0) {
                    sumFreqBeforeCurrent += frequency.getCount(valueBeforeCurrent);
                }
            }
            assertThat(cumulativeFreq, is(sumFreqBeforeCurrent));
        }

        // Testing the cumulative percentage
        for (Object value : values) {
            double cumulativePct = frequency.getCumPct(value);

            // Check if the cumulative percentage is equal to the sum of the percentages before the current value
            double sumPctBeforeCurrent = 0;
            for (Object valueBeforeCurrent : values) {
                if (frequency.compare(value, valueBeforeCurrent) > 0) {
                    sumPctBeforeCurrent += frequency.getPct(valueBeforeCurrent);
                }
            }
            assertThat(cumulativePct, is(sumPctBeforeCurrent));
        }

        // Testing the clear method
        frequency.clear();
        assertThat(frequency.getSumFreq(), is(0L));
        assertThat(frequency.valuesIterator().hasNext(), is(false));
    }

    @Test
    public void testConstructorWithComparator() {
        frequencyWithComparator.addValue('a');
        frequencyWithComparator.addValue('a');
        frequencyWithComparator.addValue('b');
        frequencyWithComparator.addValue('a');

        Iterator<Object> iterator = frequencyWithComparator.valuesIterator();
        List<Object> values = Arrays.asList(iterator.next(), iterator.next(), iterator.next(), iterator.next());

        // Testing the sum frequency
        assertThat(frequencyWithComparator.getSumFreq(), is(4L));

        // Testing the frequency of each value
        for (Object value : values) {
            assertThat(frequencyWithComparator.getCount(value), is(2L));
        }

        // Testing the percentage of each value
        for (Object value : values) {
            double expectedPct = (double) frequencyWithComparator.getCount(value) / (double) frequencyWithComparator.getSumFreq();
            double actualPct = frequencyWithComparator.getPct(value);

            // Check if the percentage is equal to the expected one
            assertThat(actualPct, is(expectedPct));
        }

        // Testing the cumulative frequency
        for (Object value : values) {
            long cumulativeFreq = frequencyWithComparator.getCumFreq(value);

            // Check if the cumulative frequency is equal to the sum of the frequencies before the current value
            long sumFreqBeforeCurrent = 0;
            for (Object valueBeforeCurrent : values) {
                if (frequencyWithComparator.compare(value, valueBeforeCurrent) > 0) {
                    sumFreqBeforeCurrent += frequencyWithComparator.getCount(valueBeforeCurrent);
                }
            }
            assertThat(cumulativeFreq, is(sumFreqBeforeCurrent));
        }

        // Testing the cumulative percentage
        for (Object value : values) {
            double cumulativePct = frequencyWithComparator.getCumPct(value);

            // Check if the cumulative percentage is equal to the sum of the percentages before the current value
            double sumPctBeforeCurrent = 0;
            for (Object valueBeforeCurrent : values) {
                if (frequencyWithComparator.compare(value, valueBeforeCurrent) > 0) {
                    sumPctBeforeCurrent += frequencyWithComparator.getPct(valueBeforeCurrent);
                }
            }
            assertThat(cumulativePct, is(sumPctBeforeCurrent));
        }

        // Testing the addValue method with a non-Comparable value
        try {
            frequencyWithComparator.addValue(new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Testing the addValue method with a non-comparable value
        try {
            frequencyWithComparator.addValue(1.2);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Testing the addValue method with a value that is not comparable with previous entries
        try {
            frequencyWithComparator.addValue('a');
            frequencyWithComparator.addValue('b');
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Testing the addValue method with an int value
        frequencyWithComparator.addValue(1);
        frequencyWithComparator.addValue(1);
        frequencyWithComparator.addValue(2);

        // Testing the addValue method with an Integer value
        frequencyWithComparator.addValue(new Integer(1));
        frequencyWithComparator.addValue(new Integer(1));
        frequencyWithComparator.addValue(new Integer(2));

        // Testing the addValue method with a long value
        frequencyWithComparator.addValue(1L);
        frequencyWithComparator.addValue(1L);
        frequencyWithComparator.addValue(2L);

        // Testing the addValue method with a Long value
        frequencyWithComparator.addValue(new Long(1L));
        frequencyWithComparator.addValue(new Long(1L));
        frequencyWithComparator.addValue(new Long(2L));

        // Testing the addValue method with a char value
        frequencyWithComparator.addValue('a');
        frequencyWithComparator.addValue('a');
        frequencyWithComparator.addValue('b');
        frequencyWithComparator.addValue('a');

        // Testing the addValue method with a Character value
        frequencyWithComparator.addValue(new Character('a'));
        frequencyWithComparator.addValue(new Character('a'));
        frequencyWithComparator.addValue(new Character('b'));
        frequencyWithComparator.addValue(new Character('a'));

        // Testing the clear method
        frequencyWithComparator.clear();
        assertThat(frequencyWithComparator.getSumFreq(), is(0L));
        assertThat(frequencyWithComparator.valuesIterator().hasNext(), is(false));
    }
}
import org.apache.commons.math3.stat.Frequency;
import org.junit.Test;
import org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        assertEquals("Size of frequency table should be 6", 6, frequency.getSumFreq());
        assertEquals("Frequency of value 1 should be 2", 2, frequency.getCount(1));
        assertEquals("Frequency of value 2 should be 4", 4, frequency.getCount(2));
        assertEquals("Frequency of value 3 should be 2", 2, frequency.getCount(3));
        assertEquals("Percentage of value 1 should be 0.33", 0.33, frequency.getPct(1), 0.01);
        assertEquals("Percentage of value 2 should be 0.67", 0.67, frequency.getPct(2), 0.01);
        assertEquals("Percentage of value 3 should be 0.33", 0.33, frequency.getPct(3), 0.01);
        assertEquals("Cumulative frequency of value 1 should be 2", 2, frequency.getCumFreq(1));
        assertEquals("Cumulative frequency of value 2 should be 6", 6, frequency.getCumFreq(2));
        assertEquals("Cumulative frequency of value 3 should be 8", 8, frequency.getCumFreq(3));
        assertEquals("Cumulative percentage of value 1 should be 0.33", 0.33, frequency.getCumPct(1), 0.01);
        assertEquals("Cumulative percentage of value 2 should be 0.67", 0.67, frequency.getCumPct(2), 0.01);
        assertEquals("Cumulative percentage of value 3 should be 1", 1, frequency.getCumPct(3), 0.01);

        Iterator iterator = frequency.valuesIterator();
        while (iterator.hasNext()) {
            Object value = iterator.next();
            Long frequencyCount = (Long) frequency.getCount(value);
            long cumulativeFrequency = frequency.getCumFreq(value);
            double cumulativePercentage = frequency.getCumPct(value);
            System.out.println("Value: " + value + "\tFrequency: " + frequencyCount + "\tCumulative Frequency: " + cumulativeFrequency + "\tCumulative Percentage: " + cumulativePercentage);
        }

        frequency.clear();
        assertTrue("Frequency table should be empty", frequency.getSumFreq() == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithUncomparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('A');

        // This line should throw an IllegalArgumentException
        frequency.addValue(2);
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return Long.compare((Long) o1, (Long) o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(3);
        frequency.addValue(2);
        frequency.addValue(1);

        assertEquals("Size of frequency table should be 3", 3, frequency.getSumFreq());
        assertEquals("Frequency of value 1 should be 0", 0, frequency.getCount(1));
        assertEquals("Frequency of value 2 should be 1", 1, frequency.getCount(2));
        assertEquals("Frequency of value 3 should be 2", 2, frequency.getCount(3));
        assertEquals("Percentage of value 1 should be 0", 0, frequency.getPct(1), 0.01);
        assertEquals("Percentage of value 2 should be 0.33", 0.33, frequency.getPct(2), 0.01);
        assertEquals("Percentage of value 3 should be 0.67", 0.67, frequency.getPct(3), 0.01);
        assertEquals("Cumulative frequency of value 1 should be 0", 0, frequency.getCumFreq(1));
        assertEquals("Cumulative frequency of value 2 should be 1", 1, frequency.getCumFreq(2));
        assertEquals("Cumulative frequency of value 3 should be 3", 3, frequency.getCumFreq(3));
        assertEquals("Cumulative percentage of value 1 should be 0", 0, frequency.getCumPct(1), 0.01);
        assertEquals("Cumulative percentage of value 2 should be 0.33", 0.33, frequency.getCumPct(2), 0.01);
        assertEquals("Cumulative percentage of value 3 should be 1", 1, frequency.getCumPct(3), 0.01);

        Iterator iterator = frequency.valuesIterator();
        while (iterator.hasNext()) {
            Object value = iterator.next();
            Long frequencyCount = (Long) frequency.getCount(value);
            long cumulativeFrequency = frequency.getCumFreq(value);
            double cumulativePercentage = frequency.getCumPct(value);
            System.out.println("Value: " + value + "\tFrequency: " + frequencyCount + "\tCumulative Frequency: " + cumulativeFrequency + "\tCumulative Percentage: " + cumulativePercentage);
        }

        frequency.clear();
        assertTrue("Frequency table should be empty", frequency.getSumFreq() == 0);
    }
}
import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.Iterator;

public class FrequencyTest {

    

    @Test
    public void testEmptyFrequency() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.getSumFreq() == 0);
        assertTrue(frequency.valuesIterator().hasNext() == false);
    }

    @Test
    public void testFrequencyWithOneValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);

        assertEquals(Long.valueOf(1), frequency.getCount(5));
        assertEquals(5.0, frequency.getPct(5), 0.001);
        assertEquals(5.0, frequency.getCumPct(5), 0.001);
        assertEquals(5.0, frequency.getCumFreq(5), 0.0);

        assertEquals(Long.valueOf(1), frequency.getCount(5L));
        assertEquals(5.0, frequency.getPct(5L), 0.001);
        assertEquals(5.0, frequency.getCumPct(5L), 0.001);
        assertEquals(5.0, frequency.getCumFreq(5L), 0.0);

        assertEquals(Long.valueOf(1), frequency.getCount((Comparable) 5));
        assertEquals(5.0, frequency.getPct((Comparable) 5), 0.001);
        assertEquals(5.0, frequency.getCumPct((Comparable) 5), 0.001);
        assertEquals(5.0, frequency.getCumFreq((Comparable) 5), 0.0);
    }

    @Test
    public void testFrequencyWithMultipleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(3);
        frequency.addValue(5);

        assertEquals(Long.valueOf(2), frequency.getCount(5));
        assertEquals(33.333, frequency.getPct(5), 0.001);
        assertEquals(33.333, frequency.getCumPct(5), 0.001);
        assertEquals(16.667, frequency.getCumPct(3), 0.001);

        assertEquals(Long.valueOf(2), frequency.getCount(5L));
        assertEquals(33.333, frequency.getPct(5L), 0.001);
        assertEquals(33.333, frequency.getCumPct(5L), 0.001);
        assertEquals(16.667, frequency.getCumPct(3L), 0.001);

        assertEquals(Long.valueOf(2), frequency.getCount((Comparable) 5));
        assertEquals(33.333, frequency.getPct((Comparable) 5), 0.001);
        assertEquals(33.333, frequency.getCumPct((Comparable) 5), 0.001);
        assertEquals(16.667, frequency.getCumPct((Comparable) 3), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueWithComparator() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        frequency.addValue("a");
        frequency.addValue("b");
        frequency.addValue("a");

        assertEquals(Long.valueOf(2), frequency.getCount("a"));
        assertEquals(33.333, frequency.getPct("a"), 0.001);
        assertEquals(33.333, frequency.getCumPct("a"), 0.001);
        assertEquals(66.667, frequency.getCumPct("b"), 0.001);
    }

    
}
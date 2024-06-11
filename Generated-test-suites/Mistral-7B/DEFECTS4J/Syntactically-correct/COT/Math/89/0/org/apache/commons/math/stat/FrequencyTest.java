import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
        assertNotNull(frequency.freqTable);
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = (o1, o2) -> o1.compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
        assertNotNull(frequency.freqTable);
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue("a");
        frequency.addValue("b");
        frequency.addValue("b");
        frequency.addValue("c");
        frequency.addValue("c");
        frequency.addValue("c");
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n" +
                "a \t 1 \t 0.2 \t 0.2 \n" +
                "b \t 2 \t 0.4 \t 0.6 \n" +
                "c \t 4 \t 0.8 \t 1.0 \n";
        assertEquals(expected, result);
    }

    @Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("a");
        frequency.addValue("a");
        long count = frequency.getCount("a");
        assertEquals(2, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(2, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(100);
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        long count = frequency.getCount(1L);
        assertEquals(2, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(100L);
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        long count = frequency.getCount('a');
        assertEquals(2, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('Z');
    }

    @Test
    public void testAddValueDeprecatedMethod() {
        Frequency frequency = new Frequency();
        frequency.addValue("a");
        frequency.addValue("a");
        long count = frequency.getCount("a");
        assertEquals(2, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueDeprecatedMethodInvalidObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueDeprecatedMethodInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(2, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueDeprecatedMethodNonComparableInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(100);
    }

    @Test
    public void testAddValueDeprecatedMethodLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        long count = frequency.getCount(1L);
        assertEquals(2, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueDeprecatedMethodNonComparableLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(100L);
    }

    @Test
    public void testAddValueDeprecatedMethodChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        long count = frequency.getCount('a');
        assertEquals(2, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueDeprecatedMethodNonComparableChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('Z');
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue("a");
        frequency.clear();
        long count = frequency.getCount("a");
        assertEquals(0, count);
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue("a");
        frequency.addValue("b");
        frequency.addValue("a");
        Iterator iterator = frequency.valuesIterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("a", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        long sumFreq = frequency.getSumFreq();
        assertEquals(3, sumFreq);
    }

    @Test
    public void testGetCountObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("a");
        frequency.addValue("a");
        long count = frequency.getCount("a");
        assertEquals(2, count);
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(2, count);
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        long count = frequency.getCount(1L);
        assertEquals(2, count);
    }

    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        long count = frequency.getCount('a');
        assertEquals(2, count);
    }

    @Test
    public void testGetPctObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("a");
        frequency.addValue("b");
        frequency.addValue("a");
        double pct = frequency.getPct("a");
        assertEquals(0.4, pct, 0.0);
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        double pct = frequency.getPct(1);
        assertEquals(0.333, pct, 0.001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(1L);
        double pct = frequency.getPct(1L);
        assertEquals(0.333, pct, 0.001);
    }

    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        double pct = frequency.getPct('a');
        assertEquals(0.4, pct, 0.0);
    }

    @Test
    public void testGetCumFreqObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("a");
        frequency.addValue("a");
        frequency.addValue("b");
        frequency.addValue("a");
        long cumFreq = frequency.getCumFreq("a");
        assertEquals(3, cumFreq);
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(3, cumFreq);
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(1L);
        long cumFreq = frequency.getCumFreq(1L);
        assertEquals(3, cumFreq);
    }

    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        long cumFreq = frequency.getCumFreq('a');
        assertEquals(3, cumFreq);
    }

    @Test
    public void testGetCumPctObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("a");
        frequency.addValue("b");
        frequency.addValue("a");
        double cumPct = frequency.getCumPct("a");
        assertEquals(0.6, cumPct, 0.0);
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        double cumPct = frequency.getCumPct(1);
        assertEquals(0.6, cumPct, 0.0);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(1L);
        double cumPct = frequency.getCumPct(1L);
        assertEquals(0.6, cumPct, 0.0);
    }

    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        double cumPct = frequency.getCumPct('a');
        assertEquals(0.6, cumPct, 0.0);
    }

    @Test
    public void testNaturalComparator() {
        Frequency frequency = new Frequency();
        Comparator comparator = frequency.new NaturalComparator();
        Object[] values = {"c", "b", "a"};
        for (Object value : values) {
            assertTrue(comparator.compare(value, value) == 0);
        }
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                int comparisonResult = comparator.compare(values[i], values[j]);
                if (comparisonResult < 0) {
                    assertTrue(values[i].toString().compareTo(values[j].toString()) < 0);
                } else {
                    assertTrue(values[i].toString().compareTo(values[j].toString()) >= 0);
                }
            }
        }
    }
}
import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator<Object> comparator = (o1, o2) -> ((Comparable<?>) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testAddValue_Integer() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValue_NonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue("non-comparable");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValue_Incomparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2.5);
    }

    @Test
    public void testAddValue_Character() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
    }

    @Test
    public void testAddValue_SameValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.1666666666666667 \t 0.5\n3 \t 1 \t 0.1666666666666667 \t 1\n";
        assertEquals(expected, frequency.toString());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(6, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertEquals(1, frequency.getCount(3));
        assertEquals(0, frequency.getCount(4));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(0.3333333333333333, frequency.getPct(1), 0.001);
        assertEquals(0.1666666666666667, frequency.getPct(2), 0.001);
        assertEquals(0.1666666666666667, frequency.getPct(3), 0.001);
        assertEquals(0, frequency.getPct(4), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));
        assertEquals(4, frequency.getCumFreq(4));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(0.3333333333333333, frequency.getCumPct(1), 0.001);
        assertEquals(0.4999999999999999, frequency.getCumPct(2), 0.001);
        assertEquals(0.6666666666666667, frequency.getCumPct(3), 0.001);
        assertEquals(1, frequency.getCumPct(4), 0.001);
    }
}
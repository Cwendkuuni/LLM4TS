import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {
    @Test
    public void testEmptyFrequency() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getSumFreq());
        assertEquals(0, freq.getCount(null));
        assertEquals(0.0, freq.getPct(null), 0.01);
        assertEquals(0.0, freq.getCumPct(null), 0.01);
    }

    @Test
    public void testAddIntValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1.0 / 1.0, freq.getPct(1), 0.01);
        assertEquals(1.0 / 1.0, freq.getCumPct(1), 0.01);
        assertEquals(1.0 / 1.0, freq.getCumPct(1), 0.01);

        freq.addValue(1);
        assertEquals(2, freq.getSumFreq());
        assertEquals(2, freq.getCount(1));
        assertEquals(1.0 / 2.0, freq.getPct(1), 0.01);
        assertEquals(1.0 / 2.0, freq.getCumPct(1), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonComparableValue() {
        Frequency freq = new Frequency();
        freq.addValue(new Object());
    }

    @Test
    public void testAddLongValue() {
        Frequency freq = new Frequency();
        freq.addValue(2L);
        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount(2));
        assertEquals(1.0 / 2.0, freq.getPct(2), 0.01);
        assertEquals(1.0 / 2.0, freq.getCumPct(2), 0.01);
        assertEquals(1.0 / 2.0, freq.getCumPct(2), 0.01);

        freq.addValue(2L);
        assertEquals(4, freq.getSumFreq());
        assertEquals(2, freq.getCount(2));
        assertEquals(1.0 / 4.0, freq.getPct(2), 0.01);
        assertEquals(1.0 / 4.0, freq.getCumPct(2), 0.01);
    }

    @Test
    public void testAddCharacterValue() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getSumFreq());
        assertEquals(1, freq.getCount('a'));
        assertEquals(1.0 / 1.0, freq.getPct('a'), 0.01);
        assertEquals(1.0 / 1.0, freq.getCumPct('a'), 0.01);
        assertEquals(1.0 / 1.0, freq.getCumPct('a'), 0.01);

        freq.addValue('a');
        assertEquals(2, freq.getSumFreq());
        assertEquals(2, freq.getCount('a'));
        assertEquals(1.0 / 2.0, freq.getPct('a'), 0.01);
        assertEquals(1.0 / 2.0, freq.getCumPct('a'), 0.01);
    }

    @Test
    public void testAddDifferentValues() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');

        assertEquals(2, freq.getSumFreq());
        assertFalse(freq.getCount(1) instanceof Character);
        assertFalse(freq.getCount('a') instanceof Long);

        assertEquals(1.0 / 2.0, freq.getPct(1), 0.01);
        assertEquals(1.0 / 2.0, freq.getPct('a'), 0.01);
        assertEquals(0.5, freq.getCumPct(1), 0.01);
        assertEquals(0.5, freq.getCumPct('a'), 0.01);
    }

    @Test
    public void testAddValuesWithComparator() {
        Comparator comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        Frequency freq = new Frequency(comparator);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount(2));
        assertEquals(1.0 / 2.0, freq.getPct(1), 0.01);
        assertEquals(1.0 / 2.0, freq.getCumPct(1), 0.01);
        assertEquals(1.0 / 2.0, freq.getCumPct(1), 0.01);

        assertEquals(1, freq.getCount(1));
        assertEquals(0.5, freq.getPct(1), 0.01);
        assertEquals(0.5, freq.getCumPct(1), 0.01);
        assertEquals(0.5, freq.getCumPct(1), 0.01);
    }
}
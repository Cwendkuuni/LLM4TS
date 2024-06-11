import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCount(1L), 0);
        assertEquals(1, frequency.getCount(1.0));
        assertEquals(2, frequency.getCount(2));
        assertEquals(1, frequency.getCount('1'), 0);

        assertEquals(1.0 / 3.0, frequency.getPct(1), 0.0001);
        assertEquals(1.0 / 3.0, frequency.getPct(1L), 0.0001);
        assertEquals(1.0 / 3.0, frequency.getPct(1.0), 0.0001);
        assertEquals(2.0 / 3.0, frequency.getPct(2), 0.0001);

        assertEquals(1.0 / 3.0, frequency.getCumPct(1), 0.0001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(1L), 0.0001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(1.0), 0.0001);
        assertEquals(1.0 / 3.0, frequency.getCumPct('1'), 0.0001);
        assertEquals(2.0 / 3.0, frequency.getCumPct(2), 0.0001);
        assertEquals(3.0 / 3.0, frequency.getCumPct(3), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('1');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueWithDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1L);
        frequency.addValue(1.0);
        frequency.addValue('1');

        assertEquals(4, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCount(1L), 0);
        assertEquals(1, frequency.getCount(1.0), 0);
        assertEquals(1, frequency.getCount('1'), 0);

        assertEquals(1.0 / 4.0, frequency.getPct(1), 0.0001);
        assertEquals(1.0 / 4.0, frequency.getPct(1L), 0.0001);
        assertEquals(1.0 / 4.0, frequency.getPct(1.0), 0.0001);
        assertEquals(1.0 / 4.0, frequency.getPct('1'), 0.0001);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();

        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount(1L), 0);
        assertEquals(0, frequency.getCount(1.0), 0);
        assertEquals(0, frequency.getCount('1'), 0);
    }
}
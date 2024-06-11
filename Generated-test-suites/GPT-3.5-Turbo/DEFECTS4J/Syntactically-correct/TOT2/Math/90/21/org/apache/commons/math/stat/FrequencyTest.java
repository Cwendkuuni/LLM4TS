import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1000000000000L);
        assertEquals(1, frequency.getCount(1000000000000L));
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.clear();
        assertEquals(0, frequency.getCount(5));
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(2, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1.0, frequency.getPct(5), 0.01);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(1, frequency.getCumFreq(5));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(0.5, frequency.getCumPct(5), 0.01);
    }
}
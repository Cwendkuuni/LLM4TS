import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Comparator;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
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
        frequency.addValue(10);
        assertEquals(0.5, frequency.getPct(5), 0.0001);
    }

    @Test
    public void testCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(1, frequency.getCumFreq(5));
    }

    @Test
    public void testCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(10);
        assertEquals(0.5, frequency.getCumPct(5), 0.0001);
    }

}
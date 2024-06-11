import static org.junit.Assert.*;
import org.junit.Test;

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
        frequency.addValue(10);
        frequency.clear();
        assertEquals(0, frequency.getCount(10));
    }
    
    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(3);
        frequency.addValue(3);
        assertEquals(2, frequency.getCount(3));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(0.33, frequency.getPct(1), 0.01);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(4);
        frequency.addValue(4);
        frequency.addValue(6);
        assertEquals(2, frequency.getCumFreq(4));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(7);
        frequency.addValue(7);
        frequency.addValue(8);
        assertEquals(0.66, frequency.getCumPct(7), 0.01);
    }
}
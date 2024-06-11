import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {
    
    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        assertEquals(1, frequency.getCount(2));
    }
    
    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        assertEquals(1, frequency.getCount(2));
    }
    
    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        assertEquals(1.0, frequency.getPct(2), 0.0001);
    }
    
    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(2));
    }
    
    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        assertEquals(1.0, frequency.getCumPct(2), 0.0001);
    }
    
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getCount(2));
    }
}
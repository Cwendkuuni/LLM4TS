import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency f = new Frequency();
        f.addValue(1);
        f.addValue(1);
        assertEquals(2, f.getCount(1));
        f.addValue("two"); // should throw IllegalArgumentException
    }

    @Test
    public void testGetCount() {
        Frequency f = new Frequency();
        f.addValue(1);
        f.addValue(1);
        assertEquals(2, f.getCount(1));
        assertEquals(0, f.getCount("two"));
    }

    @Test
    public void testGetPct() {
        Frequency f = new Frequency();
        f.addValue(1);
        f.addValue(1);
        f.addValue(2);
        assertEquals(0.5, f.getPct(1), 0.001);
        assertEquals(0.5, f.getPct(2), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency f = new Frequency();
        f.addValue(1);
        f.addValue(1);
        f.addValue(2);
        assertEquals(1, f.getCumFreq(1));
        assertEquals(3, f.getCumFreq(2));
    }

    @Test
    public void testGetCumPct() {
        Frequency f = new Frequency();
        f.addValue(1);
        f.addValue(1);
        f.addValue(2);
        assertEquals(0.333, f.getCumPct(1), 0.001);
        assertEquals(1.0, f.getCumPct(2), 0.001);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
        
        frequency.addValue(2);
        assertEquals(1, frequency.getCount(2));
        
        try {
            frequency.addValue("test"); // Adding a non-comparable value
            fail("Adding a non-comparable value should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (Exception e) {
            fail("Invalid exception thrown");
        }
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(2, frequency.getSumFreq());
        
        frequency.addValue(1);
        assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        assertTrue(frequency.valuesIterator().hasNext());
        assertEquals(1, frequency.valuesIterator().next());
        assertEquals(2, frequency.valuesIterator().next());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(1.0, frequency.getPct(1), 0.0);
        
        frequency.addValue(2);
        assertEquals(0.5, frequency.getPct(1), 0.0);
        assertEquals(0.5, frequency.getPct(2), 0.0);
    }

    @Test
    public void testCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(2, frequency.getCumFreq(1));
        assertEquals(3, frequency.getCumFreq(2));
        assertEquals(0, frequency.getCumFreq(3));
    }

    @Test
    public void testCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.6666666666666666, frequency.getCumPct(1), 0.0);
        assertEquals(1.0, frequency.getCumPct(2), 0.0);
        assertEquals(0.0, frequency.getCumPct(3), 0.0);
    }
}
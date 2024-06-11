import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class FrequencyTest {

    // Test cases for the constructor with default settings
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
    }
    
    // Test cases for the constructor with custom comparator
    @Test
    public void testCustomComparatorConstructor() {
        Comparator comparator = new Comparator<String>() {
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        };
        
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
    }

    // Test case for adding an Integer value
    @Test
    public void testAddValueInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }
    
    // Test case for adding a Long value
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(100L);
        assertEquals(1, frequency.getCount(100L));
    }
    
    // Test case for adding a Character value
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
    }
    
    // Test case for clearing the frequency table
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(10);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }

    // Test case for getting the sum of all frequencies
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(10);
        frequency.addValue(20);
        frequency.addValue(10);
        assertEquals(3, frequency.getSumFreq());
    }
    
    // Test case for getting the count of a value
    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }
    
    // Test case for getting the percentage of a value
    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1.0, frequency.getPct(5), 0.001);
    }
    
    // Test case for getting the cumulative frequency of a value
    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(5);
        assertEquals(2, frequency.getCumFreq(5));
    }
    
    // Test case for getting the cumulative percentage of a value
    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(5);
        assertEquals(1.0, frequency.getCumPct(5), 0.001);
    }
}
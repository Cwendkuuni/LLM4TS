import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class FrequencyTest {

    // Test constructor with default values
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
    }

    // Test constructor with custom Comparator
    @Test
    public void testConstructorWithComparator() {
        Comparator comparator = Comparator.naturalOrder();
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
    }

    // Test adding value of type Integer
    @Test
    public void testAddValueInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    // Test adding value of type Long
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(100L);
        assertEquals(1, frequency.getCount(100L));
    }

    // Test adding value of type Character
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
    }

    // Test adding multiple values and clearing the frequency table
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.addValue(10);
        frequency.clear();
        assertEquals(0, frequency.getCount(5));
        assertEquals(0, frequency.getCount(10));
        assertEquals(0, frequency.getSumFreq());
    }

    // Test getting sum of all frequencies
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(2, frequency.getSumFreq());
    }

    // Test getting count for a specific value
    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
    }

    // Test getting percentage of values that are equal to a specific value
    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.66, frequency.getPct(1), 0.01);
        assertEquals(0.33, frequency.getPct(2), 0.01);
    }

    // Test getting cumulative frequency of values less than or equal to a specific value
    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(2, frequency.getCumFreq(1));
        assertEquals(3, frequency.getCumFreq(2));
    }

    // Test getting cumulative percentage of values less than or equal to a specific value
    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.66, frequency.getCumPct(1), 0.01);
        assertEquals(1.0, frequency.getCumPct(2), 0.01);
    }
}
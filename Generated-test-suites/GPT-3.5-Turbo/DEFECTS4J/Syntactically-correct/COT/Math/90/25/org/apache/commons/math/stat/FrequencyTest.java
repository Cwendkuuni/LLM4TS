import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
    }
    
    @Test
    public void testAddValueInteger() {
        Frequency freq = new Frequency();
        freq.addValue(Integer.valueOf(2));
        assertEquals(1, freq.getCount(2));
    }
    
    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(3L);
        assertEquals(1, freq.getCount(3));
    }
    
    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency freq = new Frequency();
        freq.addValue(new Object());
    }
    
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(4);
        freq.addValue(4);
        freq.clear();
        assertEquals(0, freq.getCount(4));
    }
    
    // Add more test cases for other public methods
    
}
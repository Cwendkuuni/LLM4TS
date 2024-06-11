import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Comparator;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        assertEquals(1, freq.getCount(10));
        
        freq.addValue(20);
        assertEquals(1, freq.getCount(20));
    }
    
    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        freq.addValue(10);
        assertEquals(2, freq.getCount(10));
        assertEquals(0, freq.getCount(20));
    }
    
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        freq.addValue(20);
        assertEquals(2, freq.getSumFreq());
    }
    
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        freq.addValue(20);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
    }
    
    @Test
    public void testToString() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        freq.addValue(20);
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n" + "10\t1\t100%\t100%\n" + "20\t1\t100%\t100%\n";
        assertEquals(expected, freq.toString());
    }
    
    @Test
    public void testAddValueException() {
        Frequency freq = new Frequency();
        assertThrows(IllegalArgumentException.class, () -> {
            freq.addValue('a'); // 'a' is not comparable
        });
        
        Frequency freqWithComparator = new Frequency(Comparator.reverseOrder());
        freqWithComparator.addValue('a');
        assertEquals(1, freqWithComparator.getCount('a'));
    }
    
    @Test
    public void testGetCountCompatibility() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        assertEquals(1, freq.getCount(Long.valueOf(10)));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        freq.addValue(20);
        assertEquals(0.5, freq.getPct(10), 0.01);
        assertEquals(0.5, freq.getPct(20), 0.01);
    }
    
    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        freq.addValue(20);
        assertEquals(1, freq.getCumFreq(10));
        assertEquals(2, freq.getCumFreq(20));
    }
    
    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        freq.addValue(20);
        assertEquals(0.5, freq.getCumPct(10), 0.01);
        assertEquals(1.0, freq.getCumPct(20), 0.01);
    }

}
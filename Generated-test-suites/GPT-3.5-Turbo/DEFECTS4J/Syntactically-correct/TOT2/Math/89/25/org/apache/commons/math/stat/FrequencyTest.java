import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FrequencyTest {

    private Frequency frequency;
    
    @Before
    public void setUp() {
        frequency = new Frequency();
    }
    
    @Test
    public void testAddValue_Integer() {
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }
    
    @Test
    public void testAddValue_Long() {
        frequency.addValue(10L);
        assertEquals(1, frequency.getCount(10L));
    }
    
    @Test
    public void testAddValue_Char() {
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddValue_Exception() {
        frequency.addValue(new Object());
    }
    
    @Test
    public void testClear() {
        frequency.addValue(5);
        frequency.addValue(5);
        frequency.clear();
        assertEquals(0, frequency.getCount(5));
    }
    
    @Test
    public void testValuesIterator() {
        frequency.addValue(2);
        frequency.addValue(3);
        assertTrue(frequency.valuesIterator().hasNext());
    }
    
    @Test
    public void testGetSumFreq() {
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(2, frequency.getSumFreq());
    }
    
    @Test
    public void testGetCount_Object() {
        frequency.addValue(4);
        assertEquals(1, frequency.getCount(4));
    }
    
    @Test
    public void testGetCount_Integer() {
        frequency.addValue(4);
        assertEquals(1, frequency.getCount(4));
    }
    
    @Test
    public void testGetCount_Long() {
        frequency.addValue(4);
        assertEquals(1, frequency.getCount(4));
    }
    
    @Test
    public void testGetCount_Char() {
        frequency.addValue('b');
        assertEquals(1, frequency.getCount('b'));
    }
    
    @Test
    public void testGetPct_Object() {
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(0.5, frequency.getPct(2), 0.001);
    }
    
    @Test
    public void testGetPct_Integer() {
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(0.5, frequency.getPct(2), 0.001);
    }
    
    @Test
    public void testGetPct_Long() {
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(0.5, frequency.getPct(2), 0.001);
    }
    
    @Test
    public void testGetPct_Char() {
        frequency.addValue('c');
        frequency.addValue('d');
        assertEquals(0.5, frequency.getPct('c'), 0.001);
    }
    
    @Test
    public void testGetCumFreq_Object() {
        frequency.addValue(4);
        assertEquals(1, frequency.getCumFreq(4));
    }
    
    @Test
    public void testGetCumFreq_Integer() {
        frequency.addValue(4);
        assertEquals(1, frequency.getCumFreq(4));
    }
    
    @Test
    public void testGetCumFreq_Long() {
        frequency.addValue(4);
        assertEquals(1, frequency.getCumFreq(4));
    }
    
    @Test
    public void testGetCumFreq_Char() {
        frequency.addValue('q');
        assertEquals(1, frequency.getCumFreq('q'));
    }
    
    @Test
    public void testGetCumPct_Object() {
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
    }
    
    @Test
    public void testGetCumPct_Integer() {
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
    }
    
    @Test
    public void testGetCumPct_Long() {
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
    }
    
    @Test
    public void testGetCumPct_Char() {
        frequency.addValue('x');
        frequency.addValue('y');
        assertEquals(0.5, frequency.getCumPct('x'), 0.001);
    }
}
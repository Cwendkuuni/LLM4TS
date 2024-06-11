import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;
import java.util.Iterator;

public class FrequencyTest {

    private Frequency frequency;

    @Before
    public void setUp() {
        frequency = new Frequency();
    }

    @Test
    public void testAddValue() {
        frequency.addValue(5);
        assertEquals(1, frequency.getCount(5));
    }

    @Test
    public void testAddValueLong() {
        frequency.addValue(10L);
        assertEquals(1, frequency.getCount(10L));
    }

    @Test
    public void testAddValueChar() {
        frequency.addValue('A');
        assertEquals(1, frequency.getCount('A'));
    }

    @Test
    public void testAddValueComparator() {
        Comparator comparator = Comparator.reverseOrder();
        Frequency frequencyWithComparator = new Frequency(comparator);
        frequencyWithComparator.addValue(3);
        assertEquals(1, frequencyWithComparator.getCount(3));
    }

    @Test
    public void testToString() {
        frequency.addValue(1);
        frequency.addValue(2);
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1\t1\t50%\t50%\n2\t1\t50%\t100%\n";
        assertEquals(expected, frequency.toString());
    }

    @Test
    public void testGetSumFreq() {
        frequency.addValue(3);
        frequency.addValue(3);
        assertEquals(2, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        frequency.addValue(4);
        frequency.addValue(6);
        assertEquals(2, frequency.getCount(6));
    }

    @Test
    public void testGetPct() {
        frequency.addValue(10);
        frequency.addValue(20);
        assertEquals(0.5, frequency.getPct(10), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(2));
    }

    @Test
    public void testGetCumPct() {
        frequency.addValue(6);
        frequency.addValue(6);
        assertEquals(0.67, frequency.getCumPct(6), 0.01);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNonComparableValue() {
        frequency.addValue(new Object());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNonComparableAddValue() {
        frequency.add(new Object());
    }
    
    @Test(expected = ClassCastException.class)
    public void testInvalidComparator() {
        Comparator comparator = Comparator.reverseOrder();
        Frequency frequencyWithComparator = new Frequency(comparator);
        frequencyWithComparator.addValue("string"); // Using a String with a reverse order comparator
    }

}
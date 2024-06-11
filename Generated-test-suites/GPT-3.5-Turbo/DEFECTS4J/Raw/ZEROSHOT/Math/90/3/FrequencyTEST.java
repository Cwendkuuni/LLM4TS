import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Comparator;

public class FrequencyTest {

  private Frequency frequency;
  
  @Before
  public void setUp() {
    frequency = new Frequency();
  }
  
  @Test
  public void testAddValue_Integer() {
    frequency.addValue(2);
    assertEquals(1, frequency.getCount(2));
  }
  
  @Test
  public void testAddValue_Long() {
    frequency.addValue(1000L);
    assertEquals(1, frequency.getCount(1000L));
  }
  
  @Test
  public void testAddValue_Char() {
    frequency.addValue('a');
    assertEquals(1, frequency.getCount('a'));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAddValue_NotComparable() {
    frequency.addValue(new Object());
  }
  
  @Test
  public void testClear() {
    frequency.addValue(2);
    frequency.addValue(3);
    frequency.addValue(4);
    frequency.clear();
    assertEquals(0, frequency.getSumFreq());
  }
  
  @Test
  public void testGetSumFreq() {
    frequency.addValue(2);
    frequency.addValue(3);
    frequency.addValue(4);
    assertEquals(3, frequency.getSumFreq());
  }
  
  @Test
  public void testGetCount() {
    frequency.addValue(2);
    frequency.addValue(3);
    frequency.addValue(4);
    assertEquals(1, frequency.getCount(2));
  }
  
  @Test
  public void testGetPct() {
    frequency.addValue(2);
    frequency.addValue(3);
    frequency.addValue(4);
    assertEquals(0.333, frequency.getPct(2), 0.001);
  }
  
  @Test
  public void testGetCumFreq() {
    frequency.addValue(2);
    frequency.addValue(3);
    frequency.addValue(4);
    assertEquals(3, frequency.getCumFreq(4));
  }
  
  @Test
  public void testGetCumPct() {
    frequency.addValue(2);
    frequency.addValue(3);
    frequency.addValue(4);
    assertEquals(1.0, frequency.getCumPct(4), 0.001);
  }
  
}
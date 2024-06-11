package org.apache.commons.math.stat;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {
  
  private Frequency frequency;

  @Before
  public void setup() {
    frequency = new Frequency();
  }

  @Test
  public void testAddValue() {
    frequency.addValue(1);
    long count = frequency.getCount(1);
    assertEquals(1, count);
  }

  @Test
  public void testGetCount() {
    frequency.addValue(1);
    long count = frequency.getCount(1);
    assertEquals(1, count);
  }

  @Test
  public void testGetPct() {
    frequency.addValue(1);
    double pct = frequency.getPct(1);
    assertEquals(1.0, pct, 0.0001);
  }

  @Test
  public void testGetCumFreq() {
    frequency.addValue(1);
    long cumFreq = frequency.getCumFreq(1);
    assertEquals(1, cumFreq);
  }

  @Test
  public void testGetCumPct() {
    frequency.addValue(1);
    double cumPct = frequency.getCumPct(1);
    assertEquals(1.0, cumPct, 0.0001);
  }

  @Test
  public void testClear() {
    frequency.addValue(1);
    frequency.clear();
    long count = frequency.getCount(1);
    assertEquals(0, count);
  }

  @Test
  public void testToString() {
    frequency.addValue(1);
    String result = frequency.toString();
    String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1\t1\t100%\t100.0%\n";
    assertEquals(expected, result);
  }

  // Add more test methods to comprehensively test all methods in Frequency class

}
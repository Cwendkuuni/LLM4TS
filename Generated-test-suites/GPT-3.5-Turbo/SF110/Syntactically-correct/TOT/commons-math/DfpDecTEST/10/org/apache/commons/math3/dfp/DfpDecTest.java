import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

  @Test
  public void testConstructor1() {
    DfpDec dfp = new DfpDec(new DfpField(), 5);
    assertEquals(5, dfp.toInt());
  }
  
  @Test
  public void testConstructor2() {
    DfpDec dfp = new DfpDec(new DfpField(), 5L);
    assertEquals(5L, dfp.toLong());
  }
  
  @Test
  public void testConstructor3() {
    DfpDec dfp = new DfpDec(new DfpField(), 3.14);
    assertEquals(3.14, dfp.toDouble(), 0.0001);
  }
  
  @Test
  public void testConstructor4() {
    DfpDec dfp1 = new DfpDec(new DfpField(), 2.0);
    DfpDec dfp2 = new DfpDec(dfp1);
    assertEquals(2.0, dfp2.toDouble(), 0.0001);
  }
  
  @Test
  public void testConstructor5() {
    DfpDec dfp = new DfpDec(new DfpField(), "10.5");
    assertEquals("10.5", dfp.toString());
  }
  
  @Test
  public void testConstructor6() {
    DfpDec dfp = new DfpDec(new DfpField(), (byte)1, (byte)2);
    assertEquals("1.2", dfp.toString());
  }
  
  @Test
  public void testNewInstance1() {
    DfpDec dfp1 = new DfpDec(new DfpField(), 2.0);
    Dfp dfp2 = dfp1.newInstance();
    assertEquals(2.0, dfp2.toDouble(), 0.0001);
  }
  
  @Test
  public void testNewInstance2() {
    DfpDec dfp1 = new DfpDec(new DfpField(), 5);
    Dfp dfp2 = dfp1.newInstance((byte)3);
    assertEquals(3, dfp2.toInt());
  }
  
  @Test
  public void testNewInstance3() {
    DfpDec dfp1 = new DfpDec(new DfpField(), 3.14);
    Dfp dfp2 = dfp1.newInstance(7L);
    assertEquals(7L, dfp2.toLong());
  }
  
  @Test
  public void testNewInstance4() {
    DfpDec dfp1 = new DfpDec(new DfpField(), 7);
    Dfp dfp2 = dfp1.newInstance(8.5);
    assertEquals(8.5, dfp2.toDouble(), 0.0001);
  }
  
  @Test
  public void testNewInstance5() {
    DfpDec dfp1 = new DfpDec(new DfpField(), "5.3");
    Dfp dfp2 = dfp1.newInstance("10.5");
    assertEquals("10.5", dfp2.toString());
  }
  
  @Test
  public void testNewInstance6() {
    DfpDec dfp1 = new DfpDec(new DfpField(), (byte)1, (byte)2);
    Dfp dfp2 = dfp1.newInstance((byte)3, (byte)4);
    assertEquals("3.4", dfp2.toString());
  }
  
  @Test
  public void testGetDecimalDigits() {
    DfpDec dfp = new DfpDec(new DfpField(), "10.457");
    assertEquals(13, dfp.getDecimalDigits());
  }
  
  @Test
  public void testRound() {
    DfpDec dfp = new DfpDec(new DfpField(), "10.457");
    assertEquals(0, dfp.round(0));
  }
  
  @Test
  public void testNextAfter() {
    DfpDec dfp1 = new DfpDec(new DfpField(), 1.0);
    Dfp dfp2 = new DfpDec(new DfpField(), 1.1);
    Dfp dfp3 = dfp1.nextAfter(dfp2);
    assertEquals(1.05, dfp3.toDouble(), 0.0001);
  }
}
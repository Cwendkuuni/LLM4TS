import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

  @Test
  public void testDfpDec() {
    DfpDec dec = new DfpDec(factory);
    assertNotNull(dec);
  }
  
  @Test
  public void testDfpDecByte() {
    DfpDec dec = new DfpDec(factory, (byte) 1);
    assertNotNull(dec);
  }
  
  @Test
  public void testDfpDecInt() {
    DfpDec dec = new DfpDec(factory, 1);
    assertNotNull(dec);
  }
  
  @Test
  public void testDfpDecLong() {
    DfpDec dec = new DfpDec(factory, 1L);
    assertNotNull(dec);
  }
  
  @Test
  public void testDfpDecDouble() {
    DfpDec dec = new DfpDec(factory, 1.0);
    assertNotNull(dec);
  }
  
  @Test
  public void testDfpDecDfp() {
    Dfp d = new Dfp();
    DfpDec dec = new DfpDec(d);
    assertNotNull(dec);
  }
  
  @Test
  public void testDfpDecString() {
    DfpDec dec = new DfpDec(factory, "1");
    assertNotNull(dec);
  }
  
  @Test
  public void testDfpDecByteByte() {
    DfpDec dec = new DfpDec(factory, (byte) 1, (byte) 2);
    assertNotNull(dec);
  }
  
  @Test
  public void testNewInstance() {
    DfpDec dec = new DfpDec(factory);
    Dfp result = dec.newInstance();
    assertNotNull(result);
  }
  
  @Test
  public void testNewInstanceByte() {
    DfpDec dec = new DfpDec(factory);
    Dfp result = dec.newInstance((byte)1);
    assertNotNull(result);
  }
  
  @Test
  public void testNewInstanceInt() {
    DfpDec dec = new DfpDec(factory);
    Dfp result = dec.newInstance(1);
    assertNotNull(result);
  }
  
  @Test
  public void testNewInstanceLong() {
    DfpDec dec = new DfpDec(factory);
    Dfp result = dec.newInstance(1L);
    assertNotNull(result);
  }
  
  @Test
  public void testNewInstanceDouble() {
    DfpDec dec = new DfpDec(factory);
    Dfp result = dec.newInstance(1.0);
    assertNotNull(result);
  }
  
  @Test
  public void testNewInstanceDfp() {
    DfpDec dec = new DfpDec(factory);
    Dfp d = new Dfp();
    Dfp result = dec.newInstance(d);
    assertNotNull(result);
  }
  
  @Test
  public void testNewInstanceString() {
    DfpDec dec = new DfpDec(factory);
    Dfp result = dec.newInstance("1");
    assertNotNull(result);
  }
  
  @Test
  public void testNewInstanceByteByte() {
    DfpDec dec = new DfpDec(factory);
    Dfp result = dec.newInstance((byte)1, (byte)2);
    assertNotNull(result);
  }
  
  @Test
  public void testGetDecimalDigits() {
    DfpDec dec = new DfpDec(factory);
    int result = dec.getDecimalDigits();
    assertEquals(4, result);
  }
  
  @Test
  public void testRound() {
    DfpDec dec = new DfpDec(factory);
    int result = dec.round(5);
    assertEquals(0, result);
  }
  
  @Test
  public void testNextAfter() {
    DfpDec dec = new DfpDec(factory);
    Dfp x = new Dfp();
    Dfp result = dec.nextAfter(x);
    assertNotNull(result);
  }

}
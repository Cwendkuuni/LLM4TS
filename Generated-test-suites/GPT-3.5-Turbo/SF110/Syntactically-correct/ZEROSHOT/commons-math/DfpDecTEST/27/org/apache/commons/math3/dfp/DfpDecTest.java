import org.apache.commons.math3.dfp.*;
import org.junit.Test;

public class DfpDecTest {
  
  @Test
  public void testConstructor1() {
    DfpField field1 = new DfpField();
    DfpDec dfpDec1 = new DfpDec(field1);
    //add assertions here
  }
  
  @Test
  public void testConstructor2() {
    DfpField field2 = new DfpField();
    DfpDec dfpDec2 = new DfpDec(field2, (byte) 2);
    //add assertions here
  }
  
  @Test
  public void testConstructor3() {
    DfpField field3 = new DfpField();
    DfpDec dfpDec3 = new DfpDec(field3, 10);
    //add assertions here
  }
  
  @Test
  public void testConstructor4() {
    DfpField field4 = new DfpField();
    DfpDec dfpDec4 = new DfpDec(field4, 1000L);
    //add assertions here
  }
  
  @Test
  public void testConstructor5() {
    DfpField field5 = new DfpField();
    DfpDec dfpDec5 = new DfpDec(field5, 3.14);
    //add assertions here
  }
  
  @Test
  public void testConstructor6() {
    Dfp d = new Dfp();
    DfpDec dfpDec6 = new DfpDec(d);
    //add assertions here
  }
  
  @Test
  public void testConstructor7() {
    DfpField field7 = new DfpField();
    DfpDec dfpDec7 = new DfpDec(field7, "123.456");
    //add assertions here
  }
  
  @Test
  public void testConstructor8() {
    DfpField field8 = new DfpField();
    DfpDec dfpDec8 = new DfpDec(field8, (byte) 1, (byte) 0);
    //add assertions here
  }
  
  @Test
  public void testNewInstance() {
    DfpDec dfpDec = new DfpDec(new DfpField());
    Dfp newInstance = dfpDec.newInstance();
    //add assertions here
  }
  
  @Test
  public void testNewInstance2() {
    DfpDec dfpDec2 = new DfpDec(new DfpField());
    Dfp newInstance2 = dfpDec2.newInstance((byte) 5);
    //add assertions here
  }
  
  @Test
  public void testNewInstance3() {
    DfpDec dfpDec3 = new DfpDec(new DfpField());
    Dfp newInstance3 = dfpDec3.newInstance(100);
    //add assertions here
  }
  
  @Test
  public void testNewInstance4() {
    DfpDec dfpDec4 = new DfpDec(new DfpField());
    Dfp newInstance4 = dfpDec4.newInstance(1000L);
    //add assertions here
  }
  
  @Test
  public void testNewInstance5() {
    DfpDec dfpDec5 = new DfpDec(new DfpField());
    Dfp newInstance5 = dfpDec5.newInstance(3.14);
    //add assertions here
  }
  
  @Test
  public void testNewInstance6() {
    Dfp d6 = new Dfp();
    DfpDec dfpDec6 = new DfpDec(new DfpField());
    Dfp newInstance6 = dfpDec6.newInstance(d6);
    //add assertions here
  }
  
  @Test
  public void testNewInstance7() {
    DfpDec dfpDec7 = new DfpDec(new DfpField());
    Dfp newInstance7 = dfpDec7.newInstance("123.456");
    //add assertions here
  }
  
  @Test
  public void testNewInstance8() {
    DfpDec dfpDec8 = new DfpDec(new DfpField());
    Dfp newInstance8 = dfpDec8.newInstance((byte) 1, (byte) 0);
    //add assertions here
  }
  
  @Test
  public void testGetDecimalDigits() {
    DfpDec dfpDec = new DfpDec(new DfpField());
    int decimalDigits = dfpDec.getDecimalDigits();
    //add assertions here
  }
  
  @Test
  public void testRound() {
    DfpDec dfpDec = new DfpDec(new DfpField());
    int rounded = dfpDec.round(5);
    //add assertions here
  }
  
  @Test
  public void testNextAfter() {
    DfpDec dfpDec = new DfpDec(new DfpField());
    Dfp nextAfterDfp = dfpDec.nextAfter(new Dfp());
    //add assertions here
  }
  
}
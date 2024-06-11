import org.junit.Assert;
import org.junit.Test;

public class DfpDecTest {
    
    @Test
    public void testConstructor1() {
        DfpDec dfp = new DfpDec(new DfpField());
        Assert.assertNotNull(dfp);
    }
    
    @Test
    public void testConstructor2() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1);
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testConstructor3() {
        DfpDec dfp = new DfpDec(new DfpField(), 1);
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testConstructor4() {
        DfpDec dfp = new DfpDec(new DfpField(), 1L);
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testConstructor5() {
        DfpDec dfp = new DfpDec(new DfpField(), 1.0);
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testConstructor6() {
        DfpDec dfp = new DfpDec(new Dfp(2.0));
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testConstructor7() {
        DfpDec dfp = new DfpDec(new DfpField(), "123");
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testConstructor8() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1, (byte) 0);
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testNewInstance1() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance();
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testNewInstance2() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance((byte) 1);
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testNewInstance3() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(1);
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testNewInstance4() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(1L);
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testNewInstance5() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(1.0);
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testNewInstance6() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance(new Dfp(2.0));
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testNewInstance7() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance("123");
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testNewInstance8() {
        DfpDec dfp = new DfpDec(new DfpField()).newInstance((byte) 1, (byte) 0);
        Assert.assertNotNull(dfp);
    }
  
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField());
        int digits = dfp.getDecimalDigits();
        Assert.assertTrue(digits > 0);
    }
  
    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField());
        int rounded = dfp.round(0);
        Assert.assertEquals(rounded, 0);
    }
  
    @Test
    public void testNextAfter() {
        DfpDec dfp1 = new DfpDec(new DfpField());
        DfpDec dfp2 = new DfpDec(new DfpField());
        DfpDec nextAfter = dfp1.nextAfter(dfp2);
        Assert.assertNotNull(nextAfter);
    }
}
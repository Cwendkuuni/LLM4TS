import org.junit.*;
import org.apache.commons.math3.dfp.DfpDec;

public class DfpDecTest {
    
    @Test
    public void testDfpDecConstructorWithDfpField() {
        DfpDec dfp = new DfpDec(new DfpField());
        
        Assert.assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructorWithDfpFieldAndByte() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1);
        
        Assert.assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructorWithDfpFieldAndInt() {
        DfpDec dfp = new DfpDec(new DfpField(), 1);
        
        Assert.assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructorWithDfpFieldAndLong() {
        DfpDec dfp = new DfpDec(new DfpField(), 1L);
        
        Assert.assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructorWithDfpFieldAndDouble() {
        DfpDec dfp = new DfpDec(new DfpField(), 1.0);
        
        Assert.assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructorWithDfp() {
        DfpDec d = new DfpDec(new DfpField());
        DfpDec dfp = new DfpDec(d);
        
        Assert.assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructorWithDfpFieldAndString() {
        DfpDec dfp = new DfpDec(new DfpField(), "1.0");
        
        Assert.assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDecConstructorWithDfpFieldAndByteAndByte() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1, (byte) 1);
        
        Assert.assertNotNull(dfp);
    }
    
    @Test
    public void testNewInstance() {
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance();
        
        Assert.assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstanceWithByte() {
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance((byte) 1);
        
        Assert.assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstanceWithInt() {
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance(1);
        
        Assert.assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstanceWithLong() {
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance(1L);
        
        Assert.assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstanceWithDouble() {
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance(1.0);
        
        Assert.assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstanceWithDfp() {
        DfpDec d = new DfpDec(new DfpField());
        DfpDec dfp = new DfpDec(d);
        DfpDec newInstance = dfp.newInstance(d);
        
        Assert.assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstanceWithString() {
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance("1.0");
        
        Assert.assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstanceWithByteAndByte() {
        DfpDec dfp = new DfpDec(new DfpField());
        DfpDec newInstance = dfp.newInstance((byte) 1, (byte) 1);
        
        Assert.assertNotNull(newInstance);
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField());
        int decimalDigits = dfp.getDecimalDigits();
        
        Assert.assertEquals(5, decimalDigits);
    }
    
    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField());
        int rounded = dfp.round(1);
        
        Assert.assertEquals(0, rounded);
    }
    
    @Test
    public void testNextAfter() {
        DfpDec d = new DfpDec(new DfpField());
        DfpDec dfp = new DfpDec(d);
        DfpDec nextAfter = dfp.nextAfter(d);
        
        Assert.assertNotNull(nextAfter);
    }
}
import org.junit.Assert;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testProtectedConstructorWithFactory() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testProtectedConstructorWithByte() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) 5);
        
        Assert.assertNotNull(dfpDec);
        Assert.assertEquals(5, dfpDec.byteValue());
    }
    
    @Test
    public void testProtectedConstructorWithInt() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 10);
        
        Assert.assertNotNull(dfpDec);
        Assert.assertEquals(10, dfpDec.intValue());
    }
    
    @Test
    public void testProtectedConstructorWithLong() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 1000L);
        
        Assert.assertNotNull(dfpDec);
        Assert.assertEquals(1000L, dfpDec.longValue());
    }
    
    @Test
    public void testProtectedConstructorWithDouble() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 3.1415);
        
        Assert.assertNotNull(dfpDec);
        Assert.assertEquals(3.1415, dfpDec.doubleValue(), 0.0001);
    }
    
    @Test
    public void testPublicConstructorWithDfp() {
        DfpDec dfpDec = new DfpDec(new Dfp(new DfpField()));
        
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testProtectedConstructorWithString() {
        DfpDec dfpDec = new DfpDec(new DfpField(), "123.45");
        
        Assert.assertNotNull(dfpDec);
        Assert.assertEquals("123.45", dfpDec.toString());
    }
    
    @Test
    public void testProtectedConstructorWithSignAndNans() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) 1, (byte) 1);
        
        Assert.assertNotNull(dfpDec);
        Assert.assertEquals(1, dfpDec.getSign());
        Assert.assertEquals(1, dfpDec.getNans());
    }
    
    @Test
    public void testNewInstance() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        DfpDec newInstance = dfpDec.newInstance();
        
        Assert.assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstanceWithByte() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        DfpDec newInstance = dfpDec.newInstance((byte) 5);
        
        Assert.assertNotNull(newInstance);
        Assert.assertEquals(5, newInstance.byteValue());
    }
    
    @Test
    public void testNewInstanceWithInt() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        DfpDec newInstance = dfpDec.newInstance(10);
        
        Assert.assertNotNull(newInstance);
        Assert.assertEquals(10, newInstance.intValue());
    }
    
    @Test
    public void testNewInstanceWithLong() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        DfpDec newInstance = dfpDec.newInstance(1000L);
        
        Assert.assertNotNull(newInstance);
        Assert.assertEquals(1000L, newInstance.longValue());
    }
    
    @Test
    public void testNewInstanceWithDouble() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        DfpDec newInstance = dfpDec.newInstance(3.1415);
        
        Assert.assertNotNull(newInstance);
        Assert.assertEquals(3.1415, newInstance.doubleValue(), 0.0001);
    }
    
    @Test
    public void testNewInstanceWithDfp() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        DfpDec newInstance = dfpDec.newInstance(new Dfp(new DfpField()));
        
        Assert.assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstanceWithString() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        DfpDec newInstance = dfpDec.newInstance("123.45");
        
        Assert.assertNotNull(newInstance);
        Assert.assertEquals("123.45", newInstance.toString());
    }
    
    @Test
    public void testNewInstanceWithSignAndNans() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        DfpDec newInstance = dfpDec.newInstance((byte) 1, (byte) 1);
        
        Assert.assertNotNull(newInstance);
        Assert.assertEquals(1, newInstance.getSign());
        Assert.assertEquals(1, newInstance.getNans());
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        int decimalDigits = dfpDec.getDecimalDigits();
        
        Assert.assertEquals(dfpDec.getRadixDigits() * 4 - 3, decimalDigits);
    }
    
    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        int rounded = dfpDec.round(1234);
        
        Assert.assertEquals(0, rounded);
    }
    
    @Test
    public void testNextAfter() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        DfpDec nextAfter = dfpDec.nextAfter(new Dfp(new DfpField()));
        
        Assert.assertNotNull(nextAfter);
    }
}
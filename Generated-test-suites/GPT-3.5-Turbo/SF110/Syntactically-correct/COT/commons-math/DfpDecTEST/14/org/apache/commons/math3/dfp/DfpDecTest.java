
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.dfp.DfpDec;

public class DfpDecTest {
    @Test
    public void testConstructor() {
        DfpDec dfp = new DfpDec(10);
        Assert.assertEquals(10, dfp.intValue());
    }
    
    @Test
    public void testAdd() {
        DfpDec dfp1 = new DfpDec(5);
        DfpDec dfp2 = new DfpDec(10);
        
        DfpDec result = dfp1.add(dfp2);
        
        Assert.assertEquals(15, result.intValue());
    }
    
    @Test
    public void testSubtract() {
        DfpDec dfp1 = new DfpDec(10);
        DfpDec dfp2 = new DfpDec(5);
        
        DfpDec result = dfp1.subtract(dfp2);
        
        Assert.assertEquals(5, result.intValue());
    }
    
    @Test
    public void testMultiply() {
        DfpDec dfp1 = new DfpDec(5);
        DfpDec dfp2 = new DfpDec(10);
        
        DfpDec result = dfp1.multiply(dfp2);
        
        Assert.assertEquals(50, result.intValue());
    }
    
    @Test
    public void testDivide() {
        DfpDec dfp1 = new DfpDec(10);
        DfpDec dfp2 = new DfpDec(5);
        
        DfpDec result = dfp1.divide(dfp2);
        
        Assert.assertEquals(2, result.intValue());
    }
    
    @Test
    public void testNextAfter() {
        DfpDec dfp1 = new DfpDec(10);
        DfpDec dfp2 = new DfpDec(11);
        
        DfpDec result = dfp1.nextAfter(dfp2);
        
        Assert.assertEquals(11, result.intValue());
    }
    
    @Test
    public void testToString() {
        DfpDec dfp = new DfpDec(10);
        String result = dfp.toString();
        Assert.assertEquals("10", result);
    }
}

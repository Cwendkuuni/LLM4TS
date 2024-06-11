import static org.junit.Assert.*;
import org.junit.Test;

public class DfpDecTest {
    
    @Test
    public void testDfpDecConstructor() {
        DfpDec dfp = new DfpDec("12345.6789");
        assertEquals("12345.6789", dfp.toString());
    }
    
    @Test
    public void testDfpDecAdd() {
        DfpDec dfp1 = new DfpDec("5.5");
        DfpDec dfp2 = new DfpDec("3.5");
        DfpDec result = dfp1.add(dfp2);
        assertEquals("9", result.toString());
    }
    
    @Test
    public void testDfpDecSubtract() {
        DfpDec dfp1 = new DfpDec("7.8");
        DfpDec dfp2 = new DfpDec("3.5");
        DfpDec result = dfp1.subtract(dfp2);
        assertEquals("4.3", result.toString());
    }
    
    @Test
    public void testDfpDecMultiply() {
        DfpDec dfp1 = new DfpDec("2.5");
        DfpDec dfp2 = new DfpDec("3");
        DfpDec result = dfp1.multiply(dfp2);
        assertEquals("7.5", result.toString());
    }
    
    @Test
    public void testDfpDecDivide() {
        DfpDec dfp1 = new DfpDec("6.4");
        DfpDec dfp2 = new DfpDec("2.5");
        DfpDec result = dfp1.divide(dfp2);
        assertEquals("2.56", result.toString());
    }
    
    @Test
    public void testDfpDecNextAfter() {
        DfpDec dfp = new DfpDec("1.23");
        DfpDec nextAfter = dfp.nextAfter(new DfpDec("1.2"));
        assertEquals("1.200000001", nextAfter.toString());
    }
    
    @Test
    public void testDfpDecNewInstance() {
        DfpDec dfp = new DfpDec("3.5");
        DfpDec newInstance = dfp.newInstance();
        assertEquals("0", newInstance.toString());
    }
    
}
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testDfpDecConstructor() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        assertNotNull(dfpDec);
        assertEquals(0, dfpDec.getExponent());
    }
    
    @Test
    public void testNewInstance() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field, 5);
        Dfp newInstance = dfpDec.newInstance();
        assertNotNull(newInstance);
        assertEquals(5, newInstance.intValue());
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field, 1000);
        int digits = dfpDec.getDecimalDigits();
        assertEquals(1, digits);
    }
    
    // Additional test cases for other methods should be added
    
    @Test
    public void testNextAfter() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field, 5.5);
        Dfp nextAfterResult = dfpDec.nextAfter(new DfpDec(field, 6));
        assertNotNull(nextAfterResult);
        assertEquals(5.5001, nextAfterResult.toDouble(), 0.001);
    }
}
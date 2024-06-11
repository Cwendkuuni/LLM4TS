import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {
    
    @Test
    public void testDfpDecConstructorWithDfpField() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructorWithByte() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field, (byte) 5);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(2, decimalDigits); // Edge case
    }
    
    @Test
    public void testRound() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field, 3.14159);
        int rounded = dfpDec.round(0);
        assertEquals(0, rounded);
    }
    
    @Test
    public void testNextAfter() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec1 = new DfpDec(field, 5);
        DfpDec dfpDec2 = new DfpDec(field, 10);
        DfpDec nextDfpDec = dfpDec1.nextAfter(dfpDec2);
        assertNotNull(nextDfpDec); // Typical use case
    }
}
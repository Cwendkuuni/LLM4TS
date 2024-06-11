import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testDfpDecConstructor() {
        DfpDec dfpDec = new DfpDec(new DfpField(10), 1234567890);
        assertEquals(1234567890, dfpDec.intValue());
    }

    @Test
    public void testDfpDecNewInstance() {
        DfpDec dfpDec = new DfpDec(new DfpField(10));
        DfpDec newInstance = dfpDec.newInstance(5);
        assertEquals(5, newInstance.intValue());
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField(10), 12345);
        assertEquals(37, dfpDec.getDecimalDigits());
    }

    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(new DfpField(10), 1.234);
        int result = dfpDec.round(10);
        assertEquals(0, result);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec1 = new DfpDec(new DfpField(10), 5);
        DfpDec dfpDec2 = new DfpDec(new DfpField(10), 10);
        DfpDec result = dfpDec1.nextAfter(dfpDec2);
        assertEquals(6, result.intValue());
    }

}
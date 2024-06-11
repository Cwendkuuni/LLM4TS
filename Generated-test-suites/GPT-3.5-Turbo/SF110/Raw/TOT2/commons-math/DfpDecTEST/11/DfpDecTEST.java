import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field);
        
        Dfp result = dec.newInstance(5);
        assertEquals(5, result.toInt());
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field);
        
        int digits = dec.getDecimalDigits();
        assertEquals(37, digits);
    }

    @Test
    public void testRound() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field);

        int result = dec.round(1);
        assertEquals(0, result);
    }

    @Test
    public void testNextAfter() {
        DfpField field = new DfpField(10);
        DfpDec dec1 = new DfpDec(field, 5.0);
        DfpDec dec2 = new DfpDec(field, 6.0);
        
        Dfp result = dec1.nextAfter(dec2);
        assertEquals(5.0001, result.toDouble(), 0.0001);
    }
}
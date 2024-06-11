import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field);
        DfpDec newInstance = dec.newInstance();
        assertEquals(dec, newInstance);
    }

    @Test
    public void testNewInstanceWithLong() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field);
        DfpDec newInstance = dec.newInstance(10L);
        assertEquals(10L, newInstance.longValue());
    }

    @Test
    public void testRound() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field, 3.14159);
        assertEquals(0, dec.round(0));
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field, "123.456");
        assertEquals(13, dec.getDecimalDigits());
    }
    
    @Test
    public void testNextAfter() {
        DfpField field = new DfpField(10);
        DfpDec dec1 = new DfpDec(field, 5.0);
        DfpDec result1 = dec1.nextAfter(new DfpDec(field, 4.0));
        assertEquals(4.0, result1.doubleValue(), 0.001);

        DfpDec dec2 = new DfpDec(field, "-2.0");
        DfpDec result2 = dec2.nextAfter(new DfpDec(field, "-1.0"));
        assertEquals("-1.0", result2.toString());
    }

}
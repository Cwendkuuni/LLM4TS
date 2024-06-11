
import org.apache.commons.math3.dfp.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpDec dfpDec = new DfpDec(DfpField.getField());
        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstanceByte() {
        DfpDec dfpDec = new DfpDec(DfpField.getField(), (byte) 1);
        assertEquals(1, dfpDec.sign);
    }

    @Test
    public void testNewInstanceInt() {
        DfpDec dfpDec = new DfpDec(DfpField.getField(), 10);
        assertEquals(10, dfpDec.intValue());
    }

    @Test
    public void testNewInstanceLong() {
        DfpDec dfpDec = new DfpDec(DfpField.getField(), 100000L);
        assertEquals(100000L, dfpDec.longValue());
    }

    @Test
    public void testNewInstanceDouble() {
        DfpDec dfpDec = new DfpDec(DfpField.getField(), 3.14);
        assertEquals(3.14, dfpDec.doubleValue(), 0.0001);
    }

    @Test
    public void testNewInstanceDfp() {
        DfpDec dfpDec = new DfpDec(new Dfp(DfpField.getField(), 2.5));
        assertEquals(2.5, dfpDec.doubleValue(), 0.0001);
    }

    @Test
    public void testNewInstanceString() {
        DfpDec dfpDec = new DfpDec(DfpField.getField(), "123.456");
        assertEquals("123.456", dfpDec.toString());
    }

    @Test
    public void testNewInstanceByteByte() {
        DfpDec dfpDec = new DfpDec(DfpField.getField(), (byte) 1, (byte) 2);
        assertEquals((byte) 1, dfpDec.sign);
        assertEquals((byte) 2, dfpDec.nans);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(DfpField.getField());
        assertEquals(25, dfpDec.getDecimalDigits());
    }

    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(DfpField.getField(), 3.14);
        dfpDec.round(4);
        assertEquals(3.14, dfpDec.doubleValue(), 0.0001);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec1 = new DfpDec(DfpField.getField(), 3.0);
        DfpDec dfpDec2 = dfpDec1.nextAfter(new Dfp(DfpField.getField(), 4.0));
        assertEquals(3.0, dfpDec2.doubleValue(), 0.0001);
    }

}

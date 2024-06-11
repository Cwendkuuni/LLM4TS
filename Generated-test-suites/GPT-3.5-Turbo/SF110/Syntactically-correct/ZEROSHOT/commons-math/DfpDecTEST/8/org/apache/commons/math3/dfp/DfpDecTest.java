import org.junit.*;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp result = dfpDec.newInstance();
        assertEquals(dfpDec.getField(), result.getField());
    }

    @Test
    public void testNewInstanceByte() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp result = dfpDec.newInstance((byte) 1);
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals((byte) 1, result.byteValue());
    }

    @Test
    public void testNewInstanceInt() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp result = dfpDec.newInstance(10);
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals(10, result.intValue());
    }

    @Test
    public void testNewInstanceLong() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp result = dfpDec.newInstance(100000L);
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals(100000L, result.longValue());
    }

    @Test
    public void testNewInstanceDouble() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp result = dfpDec.newInstance(10.5);
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals(10.5, result.doubleValue(), 0.000001);
    }

    @Test
    public void testNewInstanceDfp() {
        Dfp d = new DfpDec(new DfpField(), 10);
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp result = dfpDec.newInstance(d);
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals(d, result);
    }

    @Test
    public void testNewInstanceString() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp result = dfpDec.newInstance("10.5");
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals(10.5, result.doubleValue(), 0.000001);
    }

    @Test
    public void testNewInstanceByteByte() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp result = dfpDec.newInstance((byte) 1, (byte) 2);
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals((byte) 1, result.byteValue());
        assertEquals((byte) 2, result.signByte());
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        int result = dfpDec.getDecimalDigits();
        assertEquals(7, result);
    }

    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        int result = dfpDec.round(1);
        assertEquals(0, result);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec1 = new DfpDec(new DfpField());
        dfpDec1.setDecimal(-5);
        DfpDec dfpDec2 = new DfpDec(new DfpField());
        dfpDec2.setDecimal(-4);
        Dfp result = dfpDec1.nextAfter(dfpDec2);
        assertTrue(result.lessThan(dfpDec2));
    }

}
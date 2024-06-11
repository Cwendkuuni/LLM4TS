import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testDfpDecConstructor() {
        DfpDec dfp = new DfpDec(new DfpField());
        assertNotNull(dfp);
    }

    @Test
    public void testDfpDecConstructorByte() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1);
        assertNotNull(dfp);
    }

    @Test
    public void testDfpDecConstructorInt() {
        DfpDec dfp = new DfpDec(new DfpField(), 1);
        assertNotNull(dfp);
    }

    @Test
    public void testDfpDecConstructorLong() {
        DfpDec dfp = new DfpDec(new DfpField(), 1L);
        assertNotNull(dfp);
    }

    @Test
    public void testDfpDecConstructorDouble() {
        DfpDec dfp = new DfpDec(new DfpField(), 1.0);
        assertNotNull(dfp);
    }

    @Test
    public void testDfpDecConstructorDfp() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 1);
        DfpDec dfp2 = new DfpDec(dfp1);
        assertNotNull(dfp2);
    }

    @Test
    public void testDfpDecConstructorString() {
        DfpDec dfp = new DfpDec(new DfpField(), "1");
        assertNotNull(dfp);
    }

    @Test
    public void testDfpDecConstructorByteByte() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte) 1, (byte) 0);
        assertNotNull(dfp);
    }

    @Test
    public void testNewInstance() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance();
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceByte() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance((byte) 1);
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceInt() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance(1);
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceLong() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance(1L);
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceDouble() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance(1.0);
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceDfp() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 1);
        Dfp result = dfp1.newInstance(dfp1);
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceString() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance("1");
        assertNotNull(result);
    }

    @Test
    public void testNewInstanceByteByte() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance((byte) 1, (byte) 0);
        assertNotNull(result);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField());
        int result = dfp.getDecimalDigits();
        assertEquals(9, result);
    }

    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField());
        int result = dfp.round(0);
        assertEquals(0, result);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 1);
        Dfp result = dfp1.nextAfter(dfp1);
        assertNotNull(result);
    }

}

import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testDfpDec() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        assertNotNull(dfpDec);
        assertTrue(dfpDec instanceof Dfp);
    }

    @Test
    public void testDfpDecByte() {
        DfpField field = new DfpField();
        byte x = 10;
        DfpDec dfpDec = new DfpDec(field, x);
        assertNotNull(dfpDec);
        assertTrue(dfpDec instanceof Dfp);
    }

    @Test
    public void testDfpDecInt() {
        DfpField field = new DfpField();
        int x = 20;
        DfpDec dfpDec = new DfpDec(field, x);
        assertNotNull(dfpDec);
        assertTrue(dfpDec instanceof Dfp);
    }

    @Test
    public void testDfpDecLong() {
        DfpField field = new DfpField();
        long x = 30;
        DfpDec dfpDec = new DfpDec(field, x);
        assertNotNull(dfpDec);
        assertTrue(dfpDec instanceof Dfp);
    }

    @Test
    public void testDfpDecDouble() {
        DfpField field = new DfpField();
        double x = 40.5;
        DfpDec dfpDec = new DfpDec(field, x);
        assertNotNull(dfpDec);
        assertTrue(dfpDec instanceof Dfp);
    }

    @Test
    public void testDfpDecDfp() {
        DfpField field = new DfpField();
        Dfp d = new Dfp(field);
        DfpDec dfpDec = new DfpDec(d);
        assertNotNull(dfpDec);
        assertTrue(dfpDec instanceof Dfp);
    }

    @Test
    public void testDfpDecString() {
        DfpField field = new DfpField();
        String s = "50";
        DfpDec dfpDec = new DfpDec(field, s);
        assertNotNull(dfpDec);
        assertTrue(dfpDec instanceof Dfp);
    }

    @Test
    public void testDfpDecByteByte() {
        DfpField field = new DfpField();
        byte sign = 1;
        byte nans = 2;
        DfpDec dfpDec = new DfpDec(field, sign, nans);
        assertNotNull(dfpDec);
        assertTrue(dfpDec instanceof Dfp);
    }

    @Test
    public void testNewInstance() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        Dfp instance = dfpDec.newInstance();
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
    }

    @Test
    public void testNewInstanceByte() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        byte x = 10;
        Dfp instance = dfpDec.newInstance(x);
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
    }

    @Test
    public void testNewInstanceInt() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        int x = 20;
        Dfp instance = dfpDec.newInstance(x);
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
    }

    @Test
    public void testNewInstanceLong() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        long x = 30;
        Dfp instance = dfpDec.newInstance(x);
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
    }

    @Test
    public void testNewInstanceDouble() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        double x = 40.5;
        Dfp instance = dfpDec.newInstance(x);
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
    }

    @Test
    public void testNewInstanceDfp() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        Dfp d = new Dfp(field);
        Dfp instance = dfpDec.newInstance(d);
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
    }

    @Test
    public void testNewInstanceString() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        String s = "50";
        Dfp instance = dfpDec.newInstance(s);
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
    }

    @Test
    public void testNewInstanceByteByte() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        byte sign = 1;
        byte nans = 2;
        Dfp instance = dfpDec.newInstance(sign, nans);
        assertNotNull(instance);
        assertTrue(instance instanceof DfpDec);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(0, decimalDigits);
    }

    @Test
    public void testRound() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        int in = 0;
        int rounded = dfpDec.round(in);
        assertEquals(0, rounded);
    }

    @Test
    public void testNextAfter() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        Dfp x = new Dfp(field);
        Dfp nextAfter = dfpDec.nextAfter(x);
        assertNotNull(nextAfter);
        assertTrue(nextAfter instanceof DfpDec);
    }
    
}
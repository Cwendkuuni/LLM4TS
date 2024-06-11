import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testDfpDecConstructor1() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);

        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructor2() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field, (byte) 5);

        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructor3() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field, 10);

        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructor4() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field, 100L);

        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructor5() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field, 10.5);

        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructor6() {
        Dfp d = new Dfp(5);
        DfpDec dfpDec = new DfpDec(d);

        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructor7() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field, "5");

        assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecConstructor8() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field, (byte) 1, (byte) 2);

        assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstance1() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance();

        assertNotNull(result);
    }

    @Test
    public void testNewInstance2() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance((byte) 5);

        assertNotNull(result);
    }

    @Test
    public void testNewInstance3() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance(10);

        assertNotNull(result);
    }

    @Test
    public void testNewInstance4() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance(100L);

        assertNotNull(result);
    }

    @Test
    public void testNewInstance5() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance(10.5);

        assertNotNull(result);
    }

    @Test
    public void testNewInstance6() {
        Dfp d = new Dfp(5);
        DfpDec dfpDec = new DfpDec(d);
        Dfp result = dfpDec.newInstance();

        assertNotNull(result);
    }

    @Test
    public void testNewInstance7() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance("5");

        assertNotNull(result);
    }

    @Test
    public void testNewInstance8() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance((byte) 1, (byte) 2);

        assertNotNull(result);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        int result = dfpDec.getDecimalDigits();

        assertEquals(-1, result);
    }

    @Test
    public void testRound() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        int result = dfpDec.round(5);

        assertEquals(0, result);
    }

    @Test
    public void testNextAfter() {
        DfpField field = new DfpField();
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.nextAfter(dfpDec);

        assertNotNull(result);
    }
}
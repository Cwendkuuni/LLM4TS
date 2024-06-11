import org.junit.Test;
import org.apache.commons.math3.dfp.DfpDec;

public class DfpDecTest {

    @Test
    public void testDfpDec() {
        //typical use case
        DfpDec dfpDec = new DfpDec(10);
        //edge case
        DfpDec dfpDec0 = new DfpDec(0);
        //error scenario
        DfpDec dfpDecNeg = new DfpDec(-10);
        //assertions
        assertEquals(dfpDec, dfpDec0);
        assertNotEquals(dfpDec, dfpDecNeg);
    }

    @Test
    public void testDfpDec_double() {
        //typical use case
        DfpDec dfpDec = new DfpDec(0.5);
        //edge case
        DfpDec dfpDec0 = new DfpDec(0);
        //error scenario
        DfpDec dfpDecNeg = new DfpDec(-0.5);
        //assertions
        assertEquals(dfpDec, dfpDec0);
        assertNotEquals(dfpDec, dfpDecNeg);
    }

    @Test
    public void testDfpDec_String() {
        //typical use case
        DfpDec dfpDec = new DfpDec("10");
        //edge case
        DfpDec dfpDec0 = new DfpDec("0");
        //error scenario
        DfpDec dfpDecNeg = new DfpDec("-10");
        //assertions
        assertEquals(dfpDec, dfpDec0);
        assertNotEquals(dfpDec, dfpDecNeg);
    }

    @Test
    public void testGetInstance_byte() {
        //typical use case
        DfpDec dfpDec = new DfpDec((byte) 10);
        //edge case
        DfpDec dfpDec0 = new DfpDec((byte) 0);
        //error scenario
        DfpDec dfpDecNeg = new DfpDec((byte) -10);
        //assertions
        assertEquals(dfpDec, dfpDec0);
        assertNotEquals(dfpDec, dfpDecNeg);
    }

    @Test
    public void testGetInstance_int() {
        //typical use case
        DfpDec dfpDec = new DfpDec(10);
        //edge case
        DfpDec dfpDec0 = new DfpDec(0);
        //error scenario
        DfpDec dfpDecNeg = new DfpDec(-10);
        //assertions
        assertEquals(dfpDec, dfpDec0);
        assertNotEquals(dfpDec, dfpDecNeg);
    }

    @Test
    public void testGetInstance_long() {
        //typical use case
        DfpDec dfpDec = new DfpDec(10L);
        //edge case
        DfpDec dfpDec0 = new DfpDec(0L);
        //error scenario
        DfpDec dfpDecNeg = new DfpDec(-10L);
        //assertions
        assertEquals(dfpDec, dfpDec0);
        assertNotEquals(dfpDec, dfpDecNeg);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec = new DfpDec(10);
        DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(11));
        assertEquals(nextAfter, new DfpDec(11));
    }

}
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DfpDecTest {

    private DfpDec dfpDec;

    @Before
    public void setUp() {
        dfpDec = new DfpDec(new DfpField());
    }

    @Test
    public void testConstructor1() {
        DfpDec newInstance = dfpDec.newInstance();
        assertNotNull(newInstance);
        assertEquals(0, newInstance.toDouble(), 0);
    }

    @Test
    public void testConstructor2() {
        DfpDec newInstance = dfpDec.newInstance((byte) 5);
        assertNotNull(newInstance);
        assertEquals(5, newInstance.toDouble(), 0);
    }

    @Test
    public void testConstructor3() {
        DfpDec newInstance = dfpDec.newInstance(3);
        assertNotNull(newInstance);
        assertEquals(3, newInstance.toDouble(), 0);
    }

    @Test
    public void testConstructor4() {
        DfpDec newInstance = dfpDec.newInstance(100000000000L);
        assertNotNull(newInstance);
        assertEquals(100000000000L, newInstance.toDouble(), 0);
    }

    @Test
    public void testConstructor5() {
        DfpDec newInstance = dfpDec.newInstance(3.14);
        assertNotNull(newInstance);
        assertEquals(3.14, newInstance.toDouble(), 0);
    }

    @Test
    public void testConstructor6() {
        Dfp d = new Dfp(2.5);
        DfpDec newInstance = dfpDec.newInstance(d);
        assertNotNull(newInstance);
        assertEquals(2.5, newInstance.toDouble(), 0);
    }

    @Test
    public void testConstructor7() {
        DfpDec newInstance = dfpDec.newInstance("3.14");
        assertNotNull(newInstance);
        assertEquals(3.14, newInstance.toDouble(), 0);
    }

    @Test
    public void testConstructor8() {
        DfpDec newInstance = dfpDec.newInstance((byte) 1, (byte) 2);
        assertNotNull(newInstance);
        assertEquals(Double.NaN, newInstance.toDouble(), 0);
    }

    @Test
    public void testGetDecimalDigits() {
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(13, decimalDigits);
    }

    @Test
    public void testRound() {
        int round = dfpDec.round(5);
        assertEquals(0, round);
    }

    @Test
    public void testNextAfter1() {
        DfpDec x = dfpDec.newInstance(2.5);
        DfpDec nextAfter = dfpDec.nextAfter(x);
        assertNotNull(nextAfter);
        assertEquals(2.5000000000000004, nextAfter.toDouble(), 0);
    }

    @Test
    public void testNextAfter2() {
        DfpDec x = dfpDec.newInstance(0);
        DfpDec nextAfter = dfpDec.nextAfter(x);
        assertNotNull(nextAfter);
        assertEquals(4.940656458412465E-324, nextAfter.toDouble(), 0);
    }
}
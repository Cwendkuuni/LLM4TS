import org.junit.*;
import static org.junit.Assert.*;

public class DfpDecTest {

    private DfpDec dfpDec;

    @Before
    public void setUp() {
        dfpDec = new DfpDec(new DfpField());
    }

    // Test constructors

    @Test
    public void testConstructor1() {
        assertEquals(new DfpDec(new DfpField()), dfpDec.newInstance());
    }

    @Test
    public void testConstructor2() {
        byte x = 1;
        assertEquals(new DfpDec(new DfpField(), x), dfpDec.newInstance(x));
    }

    @Test
    public void testConstructor3() {
        int x = 1;
        assertEquals(new DfpDec(new DfpField(), x), dfpDec.newInstance(x));
    }

    @Test
    public void testConstructor4() {
        long x = 1;
        assertEquals(new DfpDec(new DfpField(), x), dfpDec.newInstance(x));
    }

    @Test
    public void testConstructor5() {
        double x = 1.0;
        assertEquals(new DfpDec(new DfpField(), x), dfpDec.newInstance(x));
    }

    @Test
    public void testConstructor6() {
        Dfp d = new Dfp();
        assertEquals(new DfpDec(d), dfpDec.newInstance(d));
    }

    @Test
    public void testConstructor7() {
        String s = "1";
        assertEquals(new DfpDec(new DfpField(), s), dfpDec.newInstance(s));
    }

    @Test
    public void testConstructor8() {
        byte sign = 1;
        byte nans = 1;
        assertEquals(new DfpDec(new DfpField(), sign, nans), dfpDec.newInstance(sign, nans));
    }

    // Test getDecimalDigits

    @Test
    public void testGetDecimalDigits() {
        assertEquals(4 * dfpDec.mant.length - 3, dfpDec.getDecimalDigits());
    }

    // Test round

    @Test
    public void testRound() {
        int in = 0;
        int msb = dfpDec.mant[dfpDec.mant.length - 1];
        if (msb == 0) {
            assertEquals(0, dfpDec.round(in));
        }
    }

    // Test nextAfter

    @Test
    public void testNextAfter() {
        Dfp x = new Dfp();
        if (dfpDec.getField().getRadixDigits() != x.getField().getRadixDigits()) {
            dfpDec.getField().setIEEEFlagsBits(1);
            Dfp result = dfpDec.newInstance(dfpDec.getZero());
            result.nans = 3;
            assertEquals(result, dfpDec.dotrap(1, "nextAfter", x, result));
        }

        boolean up = false;
        if (dfpDec.lessThan(x)) {
            up = true;
        }
        if (dfpDec.equals(x)) {
            assertEquals(dfpDec.newInstance(x), dfpDec.nextAfter(x));
        }
        if (dfpDec.lessThan(dfpDec.getZero())) {
            up = !up;
        }

        Dfp result2;
        if (up) {
            Dfp inc = dfpDec.power10(dfpDec.intLog10() - dfpDec.getDecimalDigits() + 1);
            inc = Dfp.copysign(inc, dfpDec);
            if (dfpDec.equals(dfpDec.getZero())) {
                inc = dfpDec.power10K(-32767 - dfpDec.mant.length - 1);
            }
            if (inc.equals(dfpDec.getZero())) {
                result2 = Dfp.copysign(dfpDec.newInstance(dfpDec.getZero()), dfpDec);
            } else {
                result2 = dfpDec.add(inc);
            }
        } else {
            Dfp inc = dfpDec.power10(dfpDec.intLog10());
            inc = Dfp.copysign(inc, dfpDec);
            if (dfpDec.equals(inc)) {
                inc = inc.divide(dfpDec.power10(dfpDec.getDecimalDigits()));
            } else {
                inc = inc.divide(dfpDec.power10(dfpDec.getDecimalDigits() - 1));
            }
            if (dfpDec.equals(dfpDec.getZero())) {
                inc = dfpDec.power10K(-32767 - dfpDec.mant.length - 1);
            }
            if (inc.equals(dfpDec.getZero())) {
                result2 = Dfp.copysign(dfpDec.newInstance(dfpDec.getZero()), dfpDec);
            } else {
                result2 = dfpDec.subtract(inc);
            }
        }

        if (result2.classify() == 1 && dfpDec.classify() != 1) {
            dfpDec.getField().setIEEEFlagsBits(16);
            result2 = dfpDec.dotrap(16, "nextAfter", x, result2);
        }

        if (result2.equals(dfpDec.getZero()) && !dfpDec.equals(dfpDec.getZero())) {
            dfpDec.getField().setIEEEFlagsBits(16);
            result2 = dfpDec.dotrap(16, "nextAfter", x, result2);
        }

        assertEquals(result2, dfpDec.nextAfter(x));
    }

}
import org.junit.Assert;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpDec dfpDec = new DfpDec(10.5);
        Assert.assertEquals(dfpDec.newInstance(10.5), dfpDec);
        
        DfpDec newDfpDec = dfpDec.newInstance(5);
        Assert.assertEquals(newDfpDec, dfpDec);
        Assert.assertNotSame(newDfpDec, dfpDec);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(10);
        Assert.assertEquals(32, dfpDec.getDecimalDigits());

        DfpDec negativeDfpDec = new DfpDec(-234.567);
        Assert.assertEquals(32, negativeDfpDec.getDecimalDigits());

        DfpDec zeroDfpDec = new DfpDec(0);
        Assert.assertEquals(0, zeroDfpDec.getDecimalDigits());
    }

    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(10.5678);
        Assert.assertEquals(16, dfpDec.round(10));

        DfpDec zeroDfpDec = new DfpDec(0);
        Assert.assertEquals(0, zeroDfpDec.round(0));

        DfpDec roundingDfpDec = new DfpDec(5.1);
        Assert.assertEquals(16, roundingDfpDec.round(5));
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec = new DfpDec(10);
        DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(11));
        Assert.assertEquals(10, nextAfter.doubleValue(), 0.00001);

        DfpDec negativeDfpDec = new DfpDec(-10);
        DfpDec nextAfterNegative = negativeDfpDec.nextAfter(new DfpDec(-9));
        Assert.assertEquals(-10, nextAfterNegative.doubleValue(), 0.00001);

        DfpDec zeroDfpDec = new DfpDec(0);
        DfpDec nextAfterZero = zeroDfpDec.nextAfter(new DfpDec(1));
        Assert.assertEquals(1, nextAfterZero.doubleValue(), 0.00001);
    }

}
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.junit.Assert;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testDfpDec() {
        DfpField field = new DfpField(16);
        DfpDec dfpDec = new DfpDec(field);
        Assert.assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecByte() {
        DfpField field = new DfpField(16);
        byte x = 1;
        DfpDec dfpDec = new DfpDec(field, x);
        Assert.assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecInt() {
        DfpField field = new DfpField(16);
        int x = 1;
        DfpDec dfpDec = new DfpDec(field, x);
        Assert.assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecLong() {
        DfpField field = new DfpField(16);
        long x = 1L;
        DfpDec dfpDec = new DfpDec(field, x);
        Assert.assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecDouble() {
        DfpField field = new DfpField(16);
        double x = 1.0;
        DfpDec dfpDec = new DfpDec(field, x);
        Assert.assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecDfp() {
        DfpField field = new DfpField(16);
        Dfp d = new Dfp(field);
        DfpDec dfpDec = new DfpDec(d);
        Assert.assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecString() {
        DfpField field = new DfpField(16);
        String s = "1.0";
        DfpDec dfpDec = new DfpDec(field, s);
        Assert.assertNotNull(dfpDec);
    }

    @Test
    public void testDfpDecByteByte() {
        DfpField field = new DfpField(16);
        byte sign = 1;
        byte nans = 1;
        DfpDec dfpDec = new DfpDec(field, sign, nans);
        Assert.assertNotNull(dfpDec);
    }

    @Test
    public void testNewInstance() {
        DfpField field = new DfpField(16);
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNewInstanceByte() {
        DfpField field = new DfpField(16);
        byte x = 1;
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance(x);
        Assert.assertNotNull(result);
    }

    @Test
    public void testNewInstanceInt() {
        DfpField field = new DfpField(16);
        int x = 1;
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance(x);
        Assert.assertNotNull(result);
    }

    @Test
    public void testNewInstanceLong() {
        DfpField field = new DfpField(16);
        long x = 1L;
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance(x);
        Assert.assertNotNull(result);
    }

    @Test
    public void testNewInstanceDouble() {
        DfpField field = new DfpField(16);
        double x = 1.0;
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance(x);
        Assert.assertNotNull(result);
    }

    @Test
    public void testNewInstanceDfp() {
        DfpField field = new DfpField(16);
        Dfp d = new Dfp(field);
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance(d);
        Assert.assertNotNull(result);
    }

    @Test
    public void testNewInstanceString() {
        DfpField field = new DfpField(16);
        String s = "1.0";
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance(s);
        Assert.assertNotNull(result);
    }

    @Test
    public void testNewInstanceByteByte() {
        DfpField field = new DfpField(16);
        byte sign = 1;
        byte nans = 1;
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance(sign, nans);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField(16);
        DfpDec dfpDec = new DfpDec(field);
        int result = dfpDec.getDecimalDigits();
        Assert.assertEquals(61, result);
    }

    @Test
    public void testRound() {
        DfpField field = new DfpField(16);
        int in = 1;
        DfpDec dfpDec = new DfpDec(field);
        int result = dfpDec.round(in);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testNextAfter() {
        DfpField field = new DfpField(16);
        Dfp x = new Dfp(field);
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.nextAfter(x);
        Assert.assertNotNull(result);
    }

}
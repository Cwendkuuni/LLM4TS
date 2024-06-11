import org.junit.Test;
import org.junit.Assert;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        Dfp newInstance = dfpDec.newInstance(5);
        
        Assert.assertEquals("Incorrect value for newInstance(int)", 5, newInstance.toInt());
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        
        Assert.assertEquals("Incorrect number of decimal digits", 37, dfpDec.getDecimalDigits());
    }

    @Test
    public void testRound() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.round(0);
        
        Assert.assertEquals("Incorrect round result", 0, result.toInt());
    }

    @Test
    public void testNextAfter() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec1 = new DfpDec(field, 5.5);
        DfpDec dfpDec2 = new DfpDec(field, 6.5);
        Dfp nextAfter = dfpDec1.nextAfter(dfpDec2);
        
        Assert.assertEquals("Incorrect nextAfter result", 6, nextAfter.toInt());
    }
}
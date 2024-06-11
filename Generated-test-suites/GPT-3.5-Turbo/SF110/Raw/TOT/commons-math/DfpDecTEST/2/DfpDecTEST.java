import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {
    
    private DfpDec dfp;
    
    @Before
    public void setUp() {
        DfpField field = new DfpField(10);
        dfp = new DfpDec(field);
    }
    
    @Test
    public void testDfpDec_constructor1() {
        assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDec_constructor2() {
        byte x = 3;
        dfp = new DfpDec(dfp.getField(), x);
        assertEquals(3, dfp.toByte());
    }
    
    @Test
    public void testDfpDec_constructor3() {
        int x = 10;
        dfp = new DfpDec(dfp.getField(), x);
        assertEquals(10, dfp.toInt());
    }
    
    @Test
    public void testDfpDec_constructor4() {
        long x = 10000000000L;
        dfp = new DfpDec(dfp.getField(), x);
        assertEquals(10000000000L, dfp.toLong());
    }
    
    @Test
    public void testDfpDec_constructor5() {
        double x = 3.14159;
        dfp = new DfpDec(dfp.getField(), x);
        assertEquals(3.14159, dfp.toDouble(), 0.000001);
    }
    
    @Test
    public void testDfpDec_constructor6() {
        Dfp d = new Dfp(5);
        dfp = new DfpDec(d);
        assertEquals(d, dfp);
    }
    
    @Test
    public void testDfpDec_constructor7() {
        String s = "1234567890";
        dfp = new DfpDec(dfp.getField(), s);
        assertEquals(s, dfp.toString());
    }
    
    @Test
    public void testDfpDec_constructor8() {
        byte sign = -1;
        byte nans = 1;
        dfp = new DfpDec(dfp.getField(), sign, nans);
        assertEquals(sign, dfp.sign);
        assertEquals(nans, dfp.nans);
    }
    
    @Test
    public void testNewInstance1() {
        Dfp result = dfp.newInstance();
        assertNotNull(result);
        assertEquals(dfp.getField(), result.getField());
    }
    
    @Test
    public void testNewInstance2() {
        byte x = 3;
        Dfp result = dfp.newInstance(x);
        assertEquals(x, result.toByte());
    }
    
    @Test
    public void testNewInstance3() {
        int x = 10;
        Dfp result = dfp.newInstance(x);
        assertEquals(x, result.toInt());
    }
    
    @Test
    public void testNewInstance4() {
        long x = 10000000000L;
        Dfp result = dfp.newInstance(x);
        assertEquals(x, result.toLong());
    }
    
    @Test
    public void testNewInstance5() {
        double x = 3.14159;
        Dfp result = dfp.newInstance(x);
        assertEquals(x, result.toDouble(), 0.000001);
    }
    
    @Test
    public void testNewInstance6() {
        Dfp d = new Dfp(5);
        Dfp result = dfp.newInstance(d);
        assertEquals(d, result);
    }
    
    @Test
    public void testNewInstance7() {
        String s = "1234567890";
        Dfp result = dfp.newInstance(s);
        assertEquals(s, result.toString());
    }
    
    @Test
    public void testNewInstance8() {
        byte sign = -1;
        byte nans = 1;
        Dfp result = dfp.newInstance(sign, nans);
        assertEquals(sign, result.sign);
        assertEquals(nans, result.nans);
    }
    
    @Test
    public void testGetDecimalDigits() {
        int digits = dfp.getDecimalDigits();
        assertTrue(digits >= 0);
    }
    
    @Test
    public void testRound1() {
        int in = 10;
        int result = dfp.round(in);
        assertEquals(0, result);
    }
    
    @Test
    public void testRound2() {
        int in = 0;
        int result = dfp.round(in);
        assertEquals(0, result);
    }
    
    @Test
    public void testRound3() {
        int in = 1;
        int result = dfp.round(in);
        assertEquals(16, result);
    }
    
    @Test
    public void testRound4() {
        int in = 0;
        dfp.mant = new int[] {1, 0, 0, 0};
        int result = dfp.round(in);
        assertEquals(16, result);
    }
    
    @Test
    public void testRound5() {
        int in = 0;
        dfp.mant = new int[] {1, 0, 0, 1};
        int result = dfp.round(in);
        assertEquals(0, result);
    }
    
    @Test
    public void testNextAfter1() {
        Dfp x = new Dfp(3);
        Dfp result = dfp.nextAfter(x);
        assertTrue(result.greaterThan(x));
    }
    
    @Test
    public void testNextAfter2() {
        Dfp x = new Dfp(3);
        Dfp result = dfp.nextAfter(x);
        assertFalse(result.equals(x));
    }
    
    @Test
    public void testNextAfter3() {
        Dfp x = dfp.newInstance(0);
        Dfp result = dfp.nextAfter(x);
        assertTrue(result.equals(x));
    }
    
    @Test
    public void testNextAfter4() {
        Dfp x = new Dfp(-1);
        Dfp result = dfp.nextAfter(x);
        assertTrue(result.lessThan(x));
    }
    
    @Test
    public void testNextAfter5() {
        Dfp x = new Dfp(-1);
        Dfp result = dfp.nextAfter(x);
        assertFalse(result.equals(x));
    }
    
    @Test
    public void testNextAfter6() {
        Dfp x = dfp.newInstance(0);
        Dfp result = dfp.nextAfter(x);
        assertTrue(result.equals(x));
    }
    
    @Test
    public void testNextAfter7() {
        Dfp x = new Dfp(0);
        Dfp result = dfp.nextAfter(x);
        assertTrue(result.equals(x));
    }
    
    @Test
    public void testNextAfter8() {
        Dfp x = new Dfp(0);
        Dfp result = dfp.nextAfter(x);
        assertFalse(result.greaterThan(x));
    }
    
}
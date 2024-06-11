import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testConstructor1() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field);
        assertNotNull(dec);
    }
    
    @Test
    public void testConstructor2() {
        DfpField field = new DfpField(10);
        byte x = 1;
        DfpDec dec = new DfpDec(field, x);
        assertNotNull(dec);
    }
    
    @Test
    public void testConstructor3() {
        DfpField field = new DfpField(10);
        int x = 1;
        DfpDec dec = new DfpDec(field, x);
        assertNotNull(dec);
    }
    
    @Test
    public void testConstructor4() {
        DfpField field = new DfpField(10);
        long x = 1L;
        DfpDec dec = new DfpDec(field, x);
        assertNotNull(dec);
    }
    
    @Test
    public void testConstructor5() {
        DfpField field = new DfpField(10);
        double x = 1.0;
        DfpDec dec = new DfpDec(field, x);
        assertNotNull(dec);
    }
    
    @Test
    public void testConstructor6() {
        DfpField field = new DfpField(10);
        Dfp d = new Dfp(field);
        DfpDec dec = new DfpDec(d);
        assertNotNull(dec);
    }
    
    @Test
    public void testConstructor7() {
        DfpField field = new DfpField(10);
        String s = "1.0";
        DfpDec dec = new DfpDec(field, s);
        assertNotNull(dec);
    }
    
    @Test
    public void testConstructor8() {
        DfpField field = new DfpField(10);
        byte sign = 1;
        byte nans = 1;
        DfpDec dec = new DfpDec(field, sign, nans);
        assertNotNull(dec);
    }
    
    @Test
    public void testNewInstance1() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field);
        Dfp result = dec.newInstance();
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance2() {
        DfpField field = new DfpField(10);
        byte x = 1;
        DfpDec dec = new DfpDec(field);
        Dfp result = dec.newInstance(x);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance3() {
        DfpField field = new DfpField(10);
        int x = 1;
        DfpDec dec = new DfpDec(field);
        Dfp result = dec.newInstance(x);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance4() {
        DfpField field = new DfpField(10);
        long x = 1L;
        DfpDec dec = new DfpDec(field);
        Dfp result = dec.newInstance(x);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance5() {
        DfpField field = new DfpField(10);
        double x = 1.0;
        DfpDec dec = new DfpDec(field);
        Dfp result = dec.newInstance(x);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance6() {
        DfpField field = new DfpField(10);
        Dfp d = new Dfp(field);
        DfpDec dec = new DfpDec(field);
        Dfp result = dec.newInstance(d);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance7() {
        DfpField field = new DfpField(10);
        String s = "1.0";
        DfpDec dec = new DfpDec(field);
        Dfp result = dec.newInstance(s);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance8() {
        DfpField field = new DfpField(10);
        byte sign = 1;
        byte nans = 1;
        DfpDec dec = new DfpDec(field);
        Dfp result = dec.newInstance(sign, nans);
        assertNotNull(result);
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field);
        int digits = dec.getDecimalDigits();
        assertEquals(13, digits);
    }
    
    @Test
    public void testRound() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field);
        int result = dec.round(0);
        assertEquals(0, result);
    }
    
    @Test
    public void testNextAfter() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field);
        Dfp x = new Dfp(field);
        Dfp result = dec.nextAfter(x);
        assertNotNull(result);
    }

}
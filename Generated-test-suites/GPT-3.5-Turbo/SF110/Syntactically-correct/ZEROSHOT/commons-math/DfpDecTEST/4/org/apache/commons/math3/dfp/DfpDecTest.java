import org.junit.Test;

import static org.junit.Assert.*;

public class DfpDecTest {
    
    @Test
    public void testConstructor1() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testConstructor2() {
        DfpField factory = new DfpField();
        byte x = 1;
        DfpDec dfpDec = new DfpDec(factory, x);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testConstructor3() {
        DfpField factory = new DfpField();
        int x = 2;
        DfpDec dfpDec = new DfpDec(factory, x);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testConstructor4() {
        DfpField factory = new DfpField();
        long x = 3;
        DfpDec dfpDec = new DfpDec(factory, x);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testConstructor5() {
        DfpField factory = new DfpField();
        double x = 1.5;
        DfpDec dfpDec = new DfpDec(factory, x);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testConstructor6() {
        Dfp d = new Dfp();
        DfpDec dfpDec = new DfpDec(d);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testConstructor7() {
        DfpField factory = new DfpField();
        String s = "1.5";
        DfpDec dfpDec = new DfpDec(factory, s);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testConstructor8() {
        DfpField factory = new DfpField();
        byte sign = 1;
        byte nans = 2;
        DfpDec dfpDec = new DfpDec(factory, sign, nans);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testNewInstance1() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory);
        Dfp newInstance = dfpDec.newInstance();
        assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstance2() {
        DfpField factory = new DfpField();
        byte x = 2;
        DfpDec dfpDec = new DfpDec(factory);
        Dfp newInstance = dfpDec.newInstance(x);
        assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstance3() {
        DfpField factory = new DfpField();
        int x = 3;
        DfpDec dfpDec = new DfpDec(factory);
        Dfp newInstance = dfpDec.newInstance(x);
        assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstance4() {
        DfpField factory = new DfpField();
        long x = 4;
        DfpDec dfpDec = new DfpDec(factory);
        Dfp newInstance = dfpDec.newInstance(x);
        assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstance5() {
        DfpField factory = new DfpField();
        double x = 1.5;
        DfpDec dfpDec = new DfpDec(factory);
        Dfp newInstance = dfpDec.newInstance(x);
        assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstance6() {
        DfpField factory = new DfpField();
        Dfp d = new Dfp();
        DfpDec dfpDec = new DfpDec(factory);
        Dfp newInstance = dfpDec.newInstance(d);
        assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstance7() {
        DfpField factory = new DfpField();
        String s = "1.5";
        DfpDec dfpDec = new DfpDec(factory);
        Dfp newInstance = dfpDec.newInstance(s);
        assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstance8() {
        DfpField factory = new DfpField();
        byte sign = 1;
        byte nans = 2;
        DfpDec dfpDec = new DfpDec(factory);
        Dfp newInstance = dfpDec.newInstance(sign, nans);
        assertNotNull(newInstance);
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory);
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(33, decimalDigits);
    }
    
    @Test
    public void testRound() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory);
        int in = 1;
        int result = dfpDec.round(in);
        assertEquals(0, result);
    }
    
    @Test
    public void testNextAfter() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory);
        Dfp x = new Dfp();
        Dfp result = dfpDec.nextAfter(x);
        assertNotNull(result);
    }
    
}
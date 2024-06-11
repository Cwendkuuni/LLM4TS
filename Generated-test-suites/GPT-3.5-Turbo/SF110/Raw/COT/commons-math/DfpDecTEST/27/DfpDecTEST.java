
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testDfpDecConstructor1() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor2() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field, (byte) 1);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor3() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field, 10);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor4() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field, 100L);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor5() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field, 0.1);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor6() {
        Dfp d = new Dfp(10);
        DfpDec dfpDec = new DfpDec(d);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor7() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field, "10");
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor8() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field, (byte) 1, (byte) 2);
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testNewInstance1() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance();
        assertNotNull(result);
        assertEquals(field, result.getField());
    }
    
    @Test
    public void testNewInstance2() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance((byte) 1);
        assertNotNull(result);
        assertEquals(field, result.getField());
    }
    
    @Test
    public void testNewInstance3() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance(10);
        assertNotNull(result);
        assertEquals(field, result.getField());
    }
    
    @Test
    public void testNewInstance4() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance(100L);
        assertNotNull(result);
        assertEquals(field, result.getField());
    }
    
    @Test
    public void testNewInstance5() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance(0.1);
        assertNotNull(result);
        assertEquals(field, result.getField());
    }
    
    @Test
    public void testNewInstance6() {
        Dfp d = new Dfp(10);
        DfpDec dfpDec = new DfpDec(d);
        Dfp result = dfpDec.newInstance(d);
        assertNotNull(result);
        assertEquals(d, result);
    }
    
    @Test
    public void testNewInstance7() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance("10");
        assertNotNull(result);
        assertEquals(field, result.getField());
    }
    
    @Test
    public void testNewInstance8() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        Dfp result = dfpDec.newInstance((byte) 1, (byte) 2);
        assertNotNull(result);
        assertEquals(field, result.getField());
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        int digits = dfpDec.getDecimalDigits();
        assertEquals(field.getRadixDigits() * 4 - 3, digits);
    }
    
    @Test
    public void testRound() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        int result = dfpDec.round(1);
        assertEquals(0, result);
    }
    
    @Test
    public void testNextAfter1() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        Dfp x = new Dfp(10);
        Dfp result = dfpDec.nextAfter(x);
        assertNotNull(result);
    }
    
    @Test
    public void testNextAfter2() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        Dfp x = new Dfp(10);
        dfpDec.sign = 1;
        Dfp result = dfpDec.nextAfter(x);
        assertNotNull(result);
    }
    
    @Test
    public void testNextAfter3() {
        DfpField field = new DfpField(10);
        DfpDec dfpDec = new DfpDec(field);
        Dfp x = new Dfp(10);
        dfpDec.sign = -1;
        Dfp result = dfpDec.nextAfter(x);
        assertNotNull(result);
    }

}


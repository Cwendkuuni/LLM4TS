import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {
    
    @Test
    public void testDfpDecConstructor1() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        assertEquals(0, dfpDec.getRadixDigits());
    }
    
    @Test
    public void testDfpDecConstructor2() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) 5);
        assertEquals(5, dfpDec.getRadixDigits());
    }
    
    @Test
    public void testDfpDecConstructor3() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 10);
        assertEquals(10, dfpDec.getRadixDigits());
    }
    
    @Test
    public void testDfpDecConstructor4() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 100000000000L);
        assertEquals(100000000000L, dfpDec.getRadixDigits());
    }
    
    @Test
    public void testDfpDecConstructor5() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 3.14);
        assertEquals(3.14, dfpDec.getRadixDigits(), 0);
    }
    
    @Test
    public void testDfpDecConstructor6() {
        Dfp d = new Dfp();
        DfpDec dfpDec = new DfpDec(d);
        assertEquals(d, dfpDec);
    }
    
    @Test
    public void testDfpDecConstructor7() {
        DfpDec dfpDec = new DfpDec(new DfpField(), "12345");
        assertEquals("12345", dfpDec.toString());
    }
    
    @Test
    public void testDfpDecConstructor8() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) 1, (byte) 0);
        assertEquals(1, dfpDec.getMant());
        assertEquals(0, dfpDec.getNans());
    }
    
    @Test
    public void testDfpDecNewInstance1() {
        DfpDec dfpDec = new DfpDec(new DfpField());
        Dfp result = dfpDec.newInstance();
        assertEquals(dfpDec.getField(), result.getField());
        assertNotSame(dfpDec, result);
    }
    
    @Test
    public void testDfpDecNewInstance2() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) 5);
        Dfp result = dfpDec.newInstance((byte) 5);
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals(5, result.getRadixDigits());
    }
    
    @Test
    public void testDfpDecNewInstance3() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 10);
        Dfp result = dfpDec.newInstance(10);
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals(10, result.getRadixDigits());
    }
    
    @Test
    public void testDfpDecNewInstance4() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 100000000000L);
        Dfp result = dfpDec.newInstance(100000000000L);
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals(100000000000L, result.getRadixDigits());
    }
    
    @Test
    public void testDfpDecNewInstance5() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 3.14);
        Dfp result = dfpDec.newInstance(3.14);
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals(3.14, result.getRadixDigits(), 0);
    }
    
    @Test
    public void testDfpDecNewInstance6() {
        Dfp d = new Dfp();
        DfpDec dfpDec = new DfpDec(new DfpField(), d);
        Dfp result = dfpDec.newInstance(d);
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals(d, result);
    }
    
    @Test
    public void testDfpDecNewInstance7() {
        DfpDec dfpDec = new DfpDec(new DfpField(), "12345");
        Dfp result = dfpDec.newInstance("12345");
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals("12345", result.toString());
    }
    
    @Test
    public void testDfpDecNewInstance8() {
        DfpDec dfpDec = new DfpDec(new DfpField(), (byte) 1, (byte) 0);
        Dfp result = dfpDec.newInstance((byte) 1, (byte) 0);
        assertEquals(dfpDec.getField(), result.getField());
        assertEquals(1, result.getMant());
        assertEquals(0, result.getNans());
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField(), 10);
        assertEquals(37, dfpDec.getDecimalDigits());
    }
    
    @Test
    public void testRound() {
        // Test when msb is 0
        DfpDec dfpDec1 = new DfpDec(new DfpField());
        dfpDec1.mant[9] = 0;
        assertEquals(0, dfpDec1.round(5));
        
        // Test when digits = 4 * mant.length - 3
        DfpDec dfpDec2 = new DfpDec(new DfpField());
        dfpDec2.mant[9] = 1;
        assertEquals(0, dfpDec2.round(5));
        
        // Test when lsbthreshold <= 1
        DfpDec dfpDec3 = new DfpDec(new DfpField());
        dfpDec3.mant[9] = 1;
        dfpDec3.mant[8] = 10;
        dfpDec3.mant[7] = 100;
        assertEquals(5, dfpDec3.round(0));
        
        // Test when lsbthreshold > 1
        DfpDec dfpDec4 = new DfpDec(new DfpField());
        dfpDec4.mant[9] = 12345;
        assertEquals(5, dfpDec4.round(0));
        
        // Test when inc is true
        DfpDec dfpDec5 = new DfpDec(new DfpField());
        dfpDec5.mant[9] = 5000;
        assertEquals(16, dfpDec5.round(0));
        
        // Test when inc is false
        DfpDec dfpDec6 = new DfpDec(new DfpField());
        dfpDec6.mant[9] = 4000;
        assertEquals(0, dfpDec6.round(0));
        
        // Test when exp < -32767
        DfpDec dfpDec7 = new DfpDec(new DfpField());
        dfpDec7.exp = -40000;
        assertEquals(8, dfpDec7.round(0));
        
        // Test when exp > 32768
        DfpDec dfpDec8 = new DfpDec(new DfpField());
        dfpDec8.exp = 40000;
        assertEquals(4, dfpDec8.round(0));
        
        // Test when n != 0 or discarded != 0
        DfpDec dfpDec9 = new DfpDec(new DfpField());
        dfpDec9.mant[9] = 1000;
        assertEquals(16, dfpDec9.round(0));
    }
    
    @Test
    public void testNextAfter() {
        // Test when fields are different
        DfpDec dfpDec1 = new DfpDec(new DfpField());
        Dfp d = new Dfp();
        Dfp result1 = dfpDec1.nextAfter(d);
        assertEquals(d, result1);
        
        // Test when dfpDec is less than x
        DfpDec dfpDec2 = new DfpDec(new DfpField(), -5);
        Dfp result2 = dfpDec2.nextAfter(dfDec);
        assertTrue(result2.greaterThan(dfpDec2));
        
        // Test when dfpDec is equal to x
        DfpDec dfpDec3 = new DfpDec(new DfpField(), -5);
        Dfp result3 = dfpDec3.nextAfter(dfDec);
        assertEquals(dfpDec3, result3);
        
        // Test when dfpDec is greater than x
        DfpDec dfpDec4 = new DfpDec(new DfpField(), 5);
        Dfp result4 = dfpDec4.nextAfter(dfDec);
        assertTrue(result4.lessThan(dfpDec4));
        
        // Test when dfpDec < 0
        DfpDec dfpDec5 = new DfpDec(new DfpField(), -5);
        Dfp result5 = dfpDec5.nextAfter(dfDec);
        assertTrue(result5.greaterThan(dfpDec5));
        
    }
}
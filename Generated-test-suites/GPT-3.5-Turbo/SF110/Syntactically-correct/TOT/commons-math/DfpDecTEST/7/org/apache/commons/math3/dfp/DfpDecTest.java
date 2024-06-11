import org.junit.Test;

import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testDfpDec_constructor1() {
        DfpDec dfp = new DfpDec(10);
        
        assertEquals(10, dfp.intValue());
        assertEquals("10", dfp.toString());
        assertEquals(0, dfp.intValueExact());
    }
    
    @Test
    public void testDfpDec_constructor2() {
        DfpDec dfp = new DfpDec(-5);
        
        assertEquals(-5, dfp.intValue());
        assertEquals("-5", dfp.toString());
        assertEquals(0, dfp.intValueExact());
    }
    
    @Test
    public void testDfpDec_constructor3() {
        DfpDec dfp = new DfpDec(3.14159);
        
        assertEquals(3, dfp.intValue());
        assertEquals("3.14159", dfp.toString());
        assertEquals(0, dfp.intValueExact());
    }
    
    @Test
    public void testDfpDec_constructor4() {
        DfpDec dfp = new DfpDec("3.2");
        
        assertEquals(3, dfp.intValue());
        assertEquals("3.2", dfp.toString());
        assertEquals(0, dfp.intValueExact());
    }
    
    @Test
    public void testDfpDec_constructor5() {
        DfpDec dfp = new DfpDec(new DfpDec(5));
        
        assertEquals(5, dfp.intValue());
        assertEquals("5", dfp.toString());
        assertEquals(0, dfp.intValueExact());
    }
    
    @Test
    public void testDfpDec_constructor6() {
        DfpDec dfp = new DfpDec(new DfpDec("7.5"));
        
        assertEquals(7, dfp.intValue());
        assertEquals("7.5", dfp.toString());
        assertEquals(0, dfp.intValueExact());
    }
    
    @Test
    public void testDfpDec_constructor7() {
        DfpDec dfp = new DfpDec((byte) 3, (byte) 2);
        
        assertEquals(2, dfp.intValue());
        assertEquals(2, dfp.significance());
        assertEquals((byte) 3, dfp.sign());
    }
    
    @Test
    public void testDfpDec_newInstance1() {
        DfpDec dfp1 = new DfpDec(4);
        DfpDec dfp2 = dfp1.newInstance();
        
        assertNotNull(dfp2);
        assertNotSame(dfp1, dfp2);
        assertEquals(0, dfp2.intValue());
        assertEquals("0", dfp2.toString());
        assertEquals(0, dfp2.intValueExact());
    }
    
    @Test
    public void testDfpDec_newInstance2() {
        DfpDec dfp1 = new DfpDec(2);
        DfpDec dfp2 = dfp1.newInstance((byte) 3);
        
        assertNotNull(dfp2);
        assertNotSame(dfp1, dfp2);
        assertEquals(3, dfp2.intValue());
        assertEquals("3", dfp2.toString());
        assertEquals(0, dfp2.intValueExact());
    }
    
    @Test
    public void testDfpDec_newInstance3() {
        DfpDec dfp1 = new DfpDec(6);
        DfpDec dfp2 = dfp1.newInstance(7);
        
        assertNotNull(dfp2);
        assertNotSame(dfp1, dfp2);
        assertEquals(7, dfp2.intValue());
        assertEquals("7", dfp2.toString());
        assertEquals(0, dfp2.intValueExact());
    }
    
    @Test
    public void testDfpDec_newInstance4() {
        DfpDec dfp1 = new DfpDec(-8);
        DfpDec dfp2 = dfp1.newInstance(-9);
        
        assertNotNull(dfp2);
        assertNotSame(dfp1, dfp2);
        assertEquals(-9, dfp2.intValue());
        assertEquals("-9", dfp2.toString());
        assertEquals(0, dfp2.intValueExact());
    }
    
    @Test
    public void testDfpDec_newInstance5() {
        DfpDec dfp1 = new DfpDec(1);
        DfpDec dfp2 = dfp1.newInstance(2.5);
        
        assertNotNull(dfp2);
        assertNotSame(dfp1, dfp2);
        assertEquals(2, dfp2.intValue());
        assertEquals("2.5", dfp2.toString());
        assertEquals(0, dfp2.intValueExact());
    }
    
    @Test
    public void testDfpDec_newInstance6() {
        DfpDec dfp1 = new DfpDec(5);
        DfpDec dfp2 = dfp1.newInstance(new DfpDec(3));
        
        assertNotNull(dfp2);
        assertNotSame(dfp1, dfp2);
        assertEquals(3, dfp2.intValue());
        assertEquals("3", dfp2.toString());
        assertEquals(0, dfp2.intValueExact());
    }
    
    @Test
    public void testDfpDec_newInstance7() {
        DfpDec dfp1 = new DfpDec(2);
        DfpDec dfp2 = dfp1.newInstance("8.5");
        
        assertNotNull(dfp2);
        assertNotSame(dfp1, dfp2);
        assertEquals(8, dfp2.intValue());
        assertEquals("8.5", dfp2.toString());
        assertEquals(0, dfp2.intValueExact());
    }
    
    @Test
    public void testDfpDec_newInstance8() {
        DfpDec dfp1 = new DfpDec(3);
        DfpDec dfp2 = dfp1.newInstance((byte) -1, (byte) 3);
        
        assertNotNull(dfp2);
        assertNotSame(dfp1, dfp2);
        assertEquals(-1, dfp2.sign());
        assertEquals(3, dfp2.significance());
        assertEquals(0, dfp2.intValue());
        assertEquals("0", dfp2.toString());
        assertEquals(0, dfp2.intValueExact());
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(1);
        
        assertEquals(29, dfp.getDecimalDigits());
    }
    
    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(3.14159);
        
        assertEquals(0, dfp.round(0));
        assertEquals(0, dfp.round(1));
        assertEquals(0, dfp.round(2));
        assertEquals(0, dfp.round(3));
        assertEquals(0, dfp.round(4));
        assertEquals(0, dfp.round(5));
        assertEquals(0, dfp.round(6));
        assertEquals(0, dfp.round(7));
        assertEquals(0, dfp.round(8));
        assertEquals(0, dfp.round(9));
        assertEquals(16, dfp.round(10));
    }
    
    @Test
    public void testNextAfter() {
        DfpDec dfp1 = new DfpDec(1);
        DfpDec dfp2 = dfp1.nextAfter(new DfpDec(2));
        
        assertNotNull(dfp2);
        assertNotSame(dfp1, dfp2);
        assertEquals(1, dfp2.intValue());
        assertEquals("1", dfp2.toString());
        assertEquals(0, dfp2.intValueExact());
    }
}
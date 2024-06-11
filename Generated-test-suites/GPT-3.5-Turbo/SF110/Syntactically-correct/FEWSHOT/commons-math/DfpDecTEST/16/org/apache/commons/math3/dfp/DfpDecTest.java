import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.dfp.Dfp;
import org.junit.Before;
import org.junit.Test;

public class DfpDecTest {
    
    private DfpDec dfpDec;
    
    @Before
    public void setUp() {
        dfpDec = new DfpDec(new DfpField());
    }
    
    @Test
    public void testDfpDecConstructorField() {
        assertTrue(dfpDec instanceof DfpDec);
    }
    
    @Test
    public void testDfpDecConstructorFieldByte() {
        assertTrue(dfpDec.newInstance((byte)5) instanceof DfpDec);
    }
    
    @Test
    public void testDfpDecConstructorFieldInt() {
        assertTrue(dfpDec.newInstance(5) instanceof DfpDec);
    }
    
    @Test
    public void testDfpDecConstructorFieldLong() {
        assertTrue(dfpDec.newInstance(5L) instanceof DfpDec);
    }
    
    @Test
    public void testDfpDecConstructorFieldDouble() {
        assertTrue(dfpDec.newInstance(5.0) instanceof DfpDec);
    }
    
    @Test
    public void testDfpDecConstructorDfp() {
        assertTrue(dfpDec.newInstance(new DfpDec(new DfpField())) instanceof DfpDec);
    }
    
    @Test
    public void testDfpDecConstructorFieldString() {
        assertTrue(dfpDec.newInstance("5") instanceof DfpDec);
    }
    
    @Test
    public void testDfpDecConstructorFieldByteByte() {
        assertTrue(dfpDec.newInstance((byte)1, (byte)2) instanceof DfpDec);
    }
    
    @Test
    public void testGetDecimalDigits() {
        assertEquals(13, dfpDec.getDecimalDigits());
    }
    
    @Test
    public void testRound() {
        assertEquals(0, dfpDec.round(0));
    }
    
    @Test
    public void testNextAfter() {
        assertTrue(dfpDec.nextAfter(new DfpDec(new DfpField())) instanceof DfpDec);
    }
    
}
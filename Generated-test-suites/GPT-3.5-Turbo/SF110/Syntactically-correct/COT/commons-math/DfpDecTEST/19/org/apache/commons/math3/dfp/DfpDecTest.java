
import static org.junit.Assert.*;
import org.junit.Test;

public class DfpDecTest {
    
    @Test
    public void testNewInstance() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance();
        assertEquals(DfpDec.class, result.getClass());
    }
    
    @Test
    public void testNewInstanceWithByte() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance((byte) 10);
        assertEquals(DfpDec.class, result.getClass());
    }
    
    @Test
    public void testNewInstanceWithInt() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance(10);
        assertEquals(DfpDec.class, result.getClass());
    }
    
    @Test
    public void testNewInstanceWithLong() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance(10L);
        assertEquals(DfpDec.class, result.getClass());
    }
    
    @Test
    public void testNewInstanceWithDouble() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance(10.5);
        assertEquals(DfpDec.class, result.getClass());
    }
    
    @Test
    public void testNewInstanceWithDfp() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance(new Dfp());
        assertEquals(DfpDec.class, result.getClass());
    }
    
    @Test
    public void testNewInstanceWithString() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance("10");
        assertEquals(DfpDec.class, result.getClass());
    }
    
    @Test
    public void testNewInstanceWithByteAndByte() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance((byte) 1, (byte) 2);
        assertEquals(DfpDec.class, result.getClass());
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField());
        int digits = dfp.getDecimalDigits();
        assertEquals(40, digits);
    }
    
    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField());
        int result = dfp.round(10);
        assertEquals(0, result);
    }
    
    @Test
    public void testNextAfter() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.nextAfter(new Dfp());
        assertEquals(DfpDec.class, result.getClass());
    }
}

import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testProtectedDfpDecDfpField() {
        DfpDec decimal = new DfpDec(null);
        assertNotNull(decimal);
    }
    
    @Test
    public void testProtectedDfpDecDfpFieldByte() {
        DfpDec decimal = new DfpDec(null, (byte) 0);
        assertNotNull(decimal);
    }
    
    @Test
    public void testProtectedDfpDecDfpFieldInt() {
        DfpDec decimal = new DfpDec(null, 0);
        assertNotNull(decimal);
    }
    
    @Test
    public void testProtectedDfpDecDfpFieldLong() {
        DfpDec decimal = new DfpDec(null, 0L);
        assertNotNull(decimal);
    }
    
    @Test
    public void testProtectedDfpDecDfpFieldDouble() {
        DfpDec decimal = new DfpDec(null, 0.0);
        assertNotNull(decimal);
    }
    
    @Test
    public void testPublicDfpDecDfp() {
        DfpDec decimal = new DfpDec(null);
        DfpDec clone = new DfpDec(decimal);
        assertEquals(decimal, clone);
    }
    
    @Test
    public void testProtectedDfpDecDfpFieldString() {
        DfpDec decimal = new DfpDec(null, "0");
        assertNotNull(decimal);
    }
    
    @Test
    public void testProtectedDfpDecDfpFieldByteByte() {
        DfpDec decimal = new DfpDec(null, (byte) 0, (byte) 0);
        assertNotNull(decimal);
    }
    
    @Test
    public void testPublicDfpDecNewInstance() {
        DfpDec decimal = new DfpDec(null);
        Dfp instance = decimal.newInstance();
        assertNotNull(instance);
    }
    
    @Test
    public void testPublicDfpDecNewInstanceByte() {
        DfpDec decimal = new DfpDec(null);
        Dfp instance = decimal.newInstance((byte) 0);
        assertNotNull(instance);
    }
    
    @Test
    public void testPublicDfpDecNewInstanceInt() {
        DfpDec decimal = new DfpDec(null);
        Dfp instance = decimal.newInstance(0);
        assertNotNull(instance);
    }
    
    @Test
    public void testPublicDfpDecNewInstanceLong() {
        DfpDec decimal = new DfpDec(null);
        Dfp instance = decimal.newInstance(0L);
        assertNotNull(instance);
    }
    
    @Test
    public void testPublicDfpDecNewInstanceDouble() {
        DfpDec decimal = new DfpDec(null);
        Dfp instance = decimal.newInstance(0.0);
        assertNotNull(instance);
    }
    
    @Test
    public void testPublicDfpDecNewInstanceDfp() {
        DfpDec decimal = new DfpDec(null);
        Dfp instance = decimal.newInstance(decimal);
        assertNotNull(instance);
    }
    
    @Test
    public void testPublicDfpDecNewInstanceString() {
        DfpDec decimal = new DfpDec(null);
        Dfp instance = decimal.newInstance("0");
        assertNotNull(instance);
    }
    
    @Test
    public void testPublicDfpDecNewInstanceByteByte() {
        DfpDec decimal = new DfpDec(null);
        Dfp instance = decimal.newInstance((byte) 0, (byte) 0);
        assertNotNull(instance);
    }
    
    @Test
    public void testProtectedIntGetDecimalDigits() {
        DfpDec decimal = new DfpDec(null);
        int decimalDigits = decimal.getDecimalDigits();
        assertEquals(0, decimalDigits);
    }
    
    @Test
    public void testProtectedIntRoundInt() {
        DfpDec decimal = new DfpDec(null);
        int result = decimal.round(0);
        assertEquals(0, result);
    }
    
    @Test
    public void testPublicDfpNextAfterDfp() {
        DfpDec decimal = new DfpDec(null);
        Dfp next = decimal.nextAfter(decimal);
        assertEquals(decimal, next);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {
    
    @Test
    public void testProtectedDfpDec() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field);
        assertNotNull(dec);
        assertEquals(field, dec.getField());
    }
    
    @Test
    public void testPublicDfpDec() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field, 5);
        assertNotNull(dec);
        assertEquals(field, dec.getField());
        assertEquals(5, dec.intValue());
    }
    
    @Test
    public void testNewInstance() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field);
        Dfp newInstance = dec.newInstance(10);
        assertNotNull(newInstance);
        assertEquals(10, newInstance.intValue());
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field);
        assertEquals(37, dec.getDecimalDigits());
    }
    
    @Test
    public void testRound() {
        DfpField field = new DfpField(10);
        DfpDec dec = new DfpDec(field, 5.6789);
        int rounded = dec.round(0);
        assertEquals(16, rounded);
    }
    
    @Test
    public void testNextAfter() {
        DfpField field = new DfpField(10);
        DfpDec dec1 = new DfpDec(field, 5);
        DfpDec dec2 = dec1.nextAfter(new DfpDec(field, 6));
        assertNotNull(dec2);
        assertEquals(5.0001, dec2.doubleValue(), 0.0001);
    }
}
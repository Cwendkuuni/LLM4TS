import static org.junit.Assert.*;
import org.junit.Test;

public class DfpDecTest {
    
    @Test
    public void testConstructor1() {
        DfpDec dfp = new DfpDec(10);
        assertNotNull(dfp);
        assertEquals(10, dfp.intValue());
    }
    
    @Test
    public void testConstructor2() {
        DfpDec dfp = new DfpDec(-5);
        assertNotNull(dfp);
        assertEquals(-5, dfp.intValue());
    }
    
    @Test
    public void testConstructor3() {
        DfpDec dfp = new DfpDec(3.14);
        assertNotNull(dfp);
        assertEquals(3.14, dfp.doubleValue(), 0.001);
    }
    
    @Test
    public void testConstructor4() {
        DfpField field = new DfpField(10);
        DfpDec dfp = new DfpDec(field, "12345");
        assertNotNull(dfp);
        assertEquals("12345", dfp.toString());
    }
    
    @Test
    public void testConstructor5() {
        DfpField field = new DfpField(10);
        DfpDec dfp = new DfpDec(field, (byte) 1, (byte) 2);
        assertNotNull(dfp);
        assertEquals(1, dfp.signum());
        assertEquals(2, dfp.classify());
    }
    
    @Test
    public void testNewInstance1() {
        DfpDec dfp = new DfpDec(10);
        DfpDec newInstance = dfp.newInstance();
        assertEquals(dfp, newInstance);
    }
    
    @Test
    public void testNewInstance2() {
        DfpDec dfp = new DfpDec(5);
        DfpDec newInstance = dfp.newInstance((byte) 1);
        assertNotNull(newInstance);
        assertEquals(1, newInstance.signum());
    }
    
    @Test
    public void testNewInstance3() {
        DfpDec dfp = new DfpDec(3);
        DfpDec newInstance = dfp.newInstance(10);
        assertNotNull(newInstance);
        assertEquals(10, newInstance.intValue());
    }
    
    @Test
    public void testNewInstance4() {
        DfpDec dfp = new DfpDec(2);
        DfpDec newInstance = dfp.newInstance(3.14);
        assertNotNull(newInstance);
        assertEquals(3.14, newInstance.doubleValue(), 0.001);
    }
    
    @Test
    public void testNewInstance5() {
        DfpField field1 = new DfpField(10);
        DfpField field2 = new DfpField(5);
        DfpDec dfp1 = new DfpDec(field1, 12345);
        DfpDec dfp2 = new DfpDec(field2, 67890);
        DfpDec newInstance = dfp1.newInstance(dfp2);
        assertNotNull(newInstance);
        assertEquals(dfp2, newInstance);
    }
    
    @Test
    public void testNewInstance6() {
        DfpField field = new DfpField(10);
        DfpDec dfp = new DfpDec(field, "12345");
        DfpDec newInstance = dfp.newInstance("56789");
        assertNotNull(newInstance);
        assertEquals("56789", newInstance.toString());
    }
    
    @Test
    public void testNewInstance7() {
        DfpField field = new DfpField(10);
        DfpDec dfp = new DfpDec(field, (byte) -1, (byte) 2);
        DfpDec newInstance = dfp.newInstance((byte) -1, (byte) 1);
        assertNotNull(newInstance);
        assertEquals(-1, newInstance.signum());
        assertEquals(1, newInstance.classify());
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(3.14);
        int decimalDigits = dfp.getDecimalDigits();
        assertEquals(11, decimalDigits);
    }
    
    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(3.14159);
        int roundValue = dfp.round(5);
        assertEquals(0, roundValue);
    }
    
    @Test
    public void testNextAfter() {
        DfpDec dfp1 = new DfpDec(5);
        DfpDec dfp2 = new DfpDec(10);
        DfpDec nextAfter = dfp1.nextAfter(dfp2);
        assertNotNull(nextAfter);
        assertEquals(6, nextAfter.intValue());
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testDfpDec() {
        DfpDec dfp = new DfpDec(new DfpField(), 10);
        assertNotNull(dfp);
    }
    
    @Test
    public void testDfpDec2() {
        DfpDec dfp = new DfpDec(new DfpField(), 3.14);
        assertEquals(3.14, dfp.toDouble(), 0.001);
    }
    
    @Test
    public void testDfpDec3() {
        DfpDec dfp = new DfpDec(new DfpField(), "3.14");
        assertEquals(3.14, dfp.toDouble(), 0.001);
    }

    @Test
    public void testNewInstance() {
        DfpDec dfp = new DfpDec(new DfpField(), 5);
        DfpDec newInstance = dfp.newInstance();
        assertNotNull(newInstance);
    }
    
    @Test
    public void testNewInstance2() {
        DfpDec dfp = new DfpDec(new DfpField(), "10.5");
        DfpDec newInstance = dfp.newInstance();
        assertEquals(10.5, newInstance.toDouble(), 0.001);
    }
    
    @Test
    public void testNewInstance3() {
        DfpDec dfp = new DfpDec(new DfpField(), 5);
        DfpDec newInstance = dfp.newInstance(10);
        assertEquals(10, newInstance.toInt());
    }
    
    @Test
    public void testNewInstance4() {
        DfpDec dfp = new DfpDec(new DfpField(), "3.14");
        DfpDec newInstance = dfp.newInstance(3.14);
        assertEquals(3.14, newInstance.toDouble(), 0.001);
    }
    
    @Test
    public void testNewInstance5() {
        DfpDec dfp = new DfpDec(new DfpField(), 5);
        DfpDec newInstance = dfp.newInstance("10.5");
        assertEquals(10.5, newInstance.toDouble(), 0.001);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField(), 10);
        assertEquals(37, dfp.getDecimalDigits());
    }

    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField(), 3.1415);
        dfp.round(0);
        assertEquals(3.14, dfp.toDouble(), 0.001);
    }
    
    @Test
    public void testRound2() {
        DfpDec dfp = new DfpDec(new DfpField(), 3.1415);
        dfp.round(1);
        assertEquals(3.1, dfp.toDouble(), 0.001);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 3.14);
        DfpDec dfp2 = new DfpDec(new DfpField(), 3.15);
        DfpDec nextAfter = dfp1.nextAfter(dfp2);
        assertEquals(3.1415, nextAfter.toDouble(), 0.001);
    }
    
    @Test
    public void testNextAfter2() {
        DfpDec dfp1 = new DfpDec(new DfpField(), 10);
        DfpDec dfp2 = new DfpDec(new DfpField(), 9);
        DfpDec nextAfter = dfp1.nextAfter(dfp2);
        assertEquals(9.9999, nextAfter.toDouble(), 0.001);
    }

}
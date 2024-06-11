import org.junit.*;
import static org.junit.Assert.*;

public class DfpDecTest {

    @Test
    public void testConstructor1() {
        DfpDec dfp = new DfpDec(new DfpField());
        assertNotNull(dfp);
    }
    
    @Test
    public void testConstructor2() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte)1);
        assertNotNull(dfp);
    }
    
    @Test
    public void testConstructor3() {
        DfpDec dfp = new DfpDec(new DfpField(), 1);
        assertNotNull(dfp);
    }
    
    @Test
    public void testConstructor4() {
        DfpDec dfp = new DfpDec(new DfpField(), 1L);
        assertNotNull(dfp);
    }
    
    @Test
    public void testConstructor5() {
        DfpDec dfp = new DfpDec(new DfpField(), 1.0);
        assertNotNull(dfp);
    }
    
    @Test
    public void testConstructor6() {
        DfpDec dfp = new DfpDec(new Dfp(1.0));
        assertNotNull(dfp);
    }
    
    @Test
    public void testConstructor7() {
        DfpDec dfp = new DfpDec(new DfpField(), "1");
        assertNotNull(dfp);
    }
    
    @Test
    public void testConstructor8() {
        DfpDec dfp = new DfpDec(new DfpField(), (byte)1, (byte)1);
        assertNotNull(dfp);
    }
    
    @Test
    public void testNewInstance1() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance();
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance2() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance((byte)1);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance3() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance(1);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance4() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance(1L);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance5() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance(1.0);
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance6() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance(new Dfp(1.0));
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance7() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance("1");
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstance8() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.newInstance((byte)1, (byte)1);
        assertNotNull(result);
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(new DfpField());
        int result = dfp.getDecimalDigits();
        assertEquals(result, dfp.getRadixDigits() * 4 - 3);
    }
    
    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(new DfpField());
        int result = dfp.round(0);
        assertEquals(result, 0);
    }
    
    @Test
    public void testNextAfter() {
        DfpDec dfp = new DfpDec(new DfpField());
        Dfp result = dfp.nextAfter(new Dfp(1.0));
        assertNotNull(result);
    }

}
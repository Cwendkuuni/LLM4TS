import org.junit.*;
import static org.junit.Assert.*;

public class DfpDecTest {
    private DfpDec dfpDec;
    
    @Before
    public void setUp() {
        DfpField field = new DfpField();
        dfpDec = new DfpDec(field);
    }
    
    @Test
    public void testConstructor1() {
        assertNotNull(dfpDec);
    }
    
    @Test
    public void testConstructor2() {
        byte value = 1;
        DfpDec result = dfpDec.newInstance(value);
        assertNotNull(result);
    }
    
    @Test
    public void testConstructor3() {
        int value = 1;
        DfpDec result = dfpDec.newInstance(value);
        assertNotNull(result);
    }
    
    @Test
    public void testConstructor4() {
        long value = 1;
        DfpDec result = dfpDec.newInstance(value);
        assertNotNull(result);
    }
    
    @Test
    public void testConstructor5() {
        double value = 1.0;
        DfpDec result = dfpDec.newInstance(value);
        assertNotNull(result);
    }
    
    @Test
    public void testConstructor6() {
        Dfp dfp = new Dfp();
        DfpDec result = dfpDec.newInstance(dfp);
        assertNotNull(result);
    }
    
    @Test
    public void testConstructor7() {
        String value = "1";
        DfpDec result = dfpDec.newInstance(value);
        assertNotNull(result);
    }
    
    @Test
    public void testConstructor8() {
        byte sign = 1;
        byte nans = 1;
        DfpDec result = dfpDec.newInstance(sign, nans);
        assertNotNull(result);
    }
    
    @Test
    public void testGetDecimalDigits() {
        int result = dfpDec.getDecimalDigits();
        assertEquals(19, result);
    }
    
    @Test
    public void testRound1() {
        int in = 1;
        int result = dfpDec.round(in);
        assertEquals(0, result);
    }
    
    @Test
    public void testRound2() {
        int in = 0;
        int result = dfpDec.round(in);
        assertEquals(0, result);
    }
    
    @Test
    public void testNextAfter() {
        Dfp dfp = new Dfp();
        Dfp result = dfpDec.nextAfter(dfp);
        assertNotNull(result);
    }
}
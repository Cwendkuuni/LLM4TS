import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    private DfpDec dfp1;
    private DfpDec dfp2;

    @Before
    public void setUp() {
        // Initialize objects for testing
        dfp1 = new DfpDec(10);
        dfp2 = new DfpDec(5);
    }

    @Test
    public void testNewInstance() {
        // Test for byte value
        assertEquals(10, dfp1.newInstance((byte) 10).intValue());
        
        // Test for int value
        assertEquals(5, dfp2.newInstance(5).intValue());
        
        // Test for long value
        assertEquals(100, dfp1.newInstance(100L).longValue());
        
        // Test for double value
        assertEquals(20.5, dfp2.newInstance(20.5).doubleValue(), 0.001);
        
        // Test for Dfp value
        assertEquals(dfp1, dfp1.newInstance(dfp1));
    }
}
package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
    
    TShortHash tShortHash;
    
    @Before
    public void setup() {
        tShortHash = new TShortHashImpl();
    }
    
    @Test
    public void testContains() {
        short val = 1;
        boolean contains = tShortHash.contains(val);
        assertEquals(false, contains);
    }
    
    @Test
    public void testForEach() {
        TShortProcedureImpl procedure = new TShortProcedureImpl();
        boolean forEach = tShortHash.forEach(procedure);
        assertEquals(true, forEach);
    }
    
    // Implement the remaining test methods for all other functions
    
}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {
    
    private MultiDirectional optimizer;
    
    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
    }
    
    @Test
    public void testConstructor() {
        MultiDirectional customOptimizer = new MultiDirectional(3.0, 0.2);
        assertEquals(3.0, customOptimizer.khi, 0.0);
        assertEquals(0.2, customOptimizer.gamma, 0.0);
    }
    
    @Test
    public void testIterateSimplex() {
        try {
            // TODO: add test cases for iterateSimplex() method
            fail("Not implemented yet");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testEvaluateNewSimplex() {
        try {
            // TODO: add test cases for evaluateNewSimplex() method
            fail("Not implemented yet");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}
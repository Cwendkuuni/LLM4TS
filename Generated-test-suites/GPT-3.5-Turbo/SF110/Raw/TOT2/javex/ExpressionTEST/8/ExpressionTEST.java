import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression ex = new TestExpression("test");
        assertNotNull(ex.getExternalFunctions());
    }
    
    @Test
    public void testPrepare() {
        Expression ex = new TestExpression("2+2");
        ex.prepare();
        assertTrue(ex.isPrepared());
    }
    
    @Test
    public void testEvaluate() {
        Expression ex = new TestExpression("3*5");
        try {
            assertEquals(15, ex.evaluate());
        } catch (Exception e) {
            fail("Exception thrown while evaluating expression");
        }
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression ex = new TestExpression("test");
        assertTrue(ex.isReservedFunction("and"));
    }
    
    // Add more test cases for other methods as needed
    
}

class TestExpression extends Expression {
    
    public TestExpression(String s) {
        super(s);
    }
}
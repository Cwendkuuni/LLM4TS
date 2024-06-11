import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {
    
    @Test
    public void testIsContextDependent() {
        assertTrue(new Expression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }
            
            @Override
            public Object computeValue(EvalContext context) {
                return null; // implementation not required for this test case
            }
            
            @Override
            public Object compute(EvalContext context) {
                return null; // implementation not required for this test case
            }
        }.isContextDependent());
        
        assertFalse(new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
            
            @Override
            public Object computeValue(EvalContext context) {
                return null; // implementation not required for this test case
            }
            
            @Override
            public Object compute(EvalContext context) {
                return null; // implementation not required for this test case
            }
        }.isContextDependent());
    }
    
    @Test
    public void testComputeValue() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false; // implementation not required for this test case
            }
            
            @Override
            public Object computeValue(EvalContext context) {
                return "computed value";
            }
            
            @Override
            public Object compute(EvalContext context) {
                return null; // implementation not required for this test case
            }
        };
        
        assertEquals("computed value", expression.computeValue(null));
    }
    
    @Test
    public void testCompute() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false; // implementation not required for this test case
            }
            
            @Override
            public Object computeValue(EvalContext context) {
                return null; // implementation not required for this test case
            }
            
            @Override
            public Object compute(EvalContext context) {
                return "computed result";
            }
        };
        
        assertEquals("computed result", expression.compute(null));
    }
}
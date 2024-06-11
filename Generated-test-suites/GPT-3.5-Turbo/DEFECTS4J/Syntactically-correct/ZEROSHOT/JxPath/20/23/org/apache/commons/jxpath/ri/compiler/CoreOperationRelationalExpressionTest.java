import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.*;

public class CoreOperationRelationalExpressionTest {
    
    private CoreOperationRelationalExpression expression;
    
    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpressionMock();
    }
    
    @Test
    public void testComputeValue() {
        assertEquals(Boolean.TRUE, expression.computeValue(new EvalContext()));
    }
    
    @Test
    public void testGetPrecedence() {
        assertEquals(5, expression.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        assertFalse(expression.isSymmetric());
    }
    
    // Helper class for mocking CoreOperationRelationalExpression
    private class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {
        
        public CoreOperationRelationalExpressionMock() {
            super(new Expression[2]);
        }
        
        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
        
    }
}
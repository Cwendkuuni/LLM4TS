import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub();
        EvalContext context = new EvalContext();
        
        // Test case 1: Left value greater than right value
        context.setNodeSet(new Object[]{5, 3});
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Test case 2: Left value less than right value
        context.setNodeSet(new Object[]{3, 5});
        assertEquals(Boolean.FALSE, expression.computeValue(context));
        
        // Test case 3: Left value equal to right value
        context.setNodeSet(new Object[]{5, 5});
        assertEquals(Boolean.FALSE, expression.computeValue(context));
        
        // Test case 4: Left value and right value are strings
        context.setNodeSet(new Object[]{"abc", "def"});
        assertEquals(Boolean.FALSE, expression.computeValue(context));
        
        // Test case 5: Left value and right value are null
        context.setNodeSet(new Object[]{null, null});
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub();
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub();
        assertEquals(false, expression.isSymmetric());
    }
    
    private class CoreOperationRelationalExpressionStub extends CoreOperationRelationalExpression {
        
        public CoreOperationRelationalExpressionStub() {
            super(new Expression[]{});
        }
        
        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
        
    }
}
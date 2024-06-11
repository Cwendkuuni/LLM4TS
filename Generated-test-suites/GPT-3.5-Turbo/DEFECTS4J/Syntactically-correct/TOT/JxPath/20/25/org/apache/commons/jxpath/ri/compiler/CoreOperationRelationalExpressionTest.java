import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_greaterThan() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression("5");
        args[1] = new MockExpression("3");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertTrue((boolean) expression.computeValue(new EvalContext()));
    }
    
    @Test
    public void testComputeValue_greaterThanOrEqual() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression("5");
        args[1] = new MockExpression("5");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        assertTrue((boolean) expression.computeValue(new EvalContext()));
    }
    
    @Test
    public void testComputeValue_lessThan() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression("3");
        args[1] = new MockExpression("5");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertTrue((boolean) expression.computeValue(new EvalContext()));
    }
    
    @Test
    public void testComputeValue_lessThanOrEqual() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression("5");
        args[1] = new MockExpression("5");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };

        assertTrue((boolean) expression.computeValue(new EvalContext()));
    }
    
    @Test
    public void testComputeValue_invalidInput() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression("abc");
        args[1] = new MockExpression("5");
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };

        assertFalse((boolean) expression.computeValue(new EvalContext()));
    }

}
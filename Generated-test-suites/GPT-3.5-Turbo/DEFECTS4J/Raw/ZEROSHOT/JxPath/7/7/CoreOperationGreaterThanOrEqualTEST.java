import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual operation;

    @Before
    public void setUp() {
        operation = new CoreOperationGreaterThanOrEqual(null, null);
    }
    
    @Test
    public void testComputeValue() {
        // Test when arg1 and arg2 are both positive numbers
        EvalContext context1 = new EvalContext(null);
        context1.setNodeSet(new Object[] {1, 2});
        operation.setArgs(new Expression[] {new ExprLiteral(1), new ExprLiteral(2)});
        Object result1 = operation.computeValue(context1);
        assertEquals(false, result1);
        
        // Test when arg1 is greater than arg2
        EvalContext context2 = new EvalContext(null);
        context2.setNodeSet(new Object[] {3});
        operation.setArgs(new Expression[] {new ExprLiteral(3), new ExprLiteral(2)});
        Object result2 = operation.computeValue(context2);
        assertEquals(true, result2);
        
        // Test when arg1 and arg2 are both negative numbers
        EvalContext context3 = new EvalContext(null);
        context3.setNodeSet(new Object[] {-1, -2});
        operation.setArgs(new Expression[] {new ExprLiteral(-1), new ExprLiteral(-2)});
        Object result3 = operation.computeValue(context3);
        assertEquals(false, result3);
        
        // Test when arg1 is less than arg2
        EvalContext context4 = new EvalContext(null);
        context4.setNodeSet(new Object[] {4});
        operation.setArgs(new Expression[] {new ExprLiteral(3), new ExprLiteral(4)});
        Object result4 = operation.computeValue(context4);
        assertEquals(false, result4);
        
        // Test when arg1 and arg2 are both zero
        EvalContext context5 = new EvalContext(null);
        context5.setNodeSet(new Object[] {0, 0});
        operation.setArgs(new Expression[] {new ExprLiteral(0), new ExprLiteral(0)});
        Object result5 = operation.computeValue(context5);
        assertEquals(true, result5);
    }

    @Test
    public void testGetSymbol() {
        operation = new CoreOperationGreaterThanOrEqual(null, null);
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }
}
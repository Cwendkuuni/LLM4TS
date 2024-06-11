import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    private CoreOperationCompare operation;

    @Before
    public void setUp() {
        operation = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testEqualWithEqualValues() {
        EvalContext context = createContext();
        Expression left = createExpression();
        Expression right = createExpression();
        
        boolean result = operation.equal(context, left, right);
        
        assertTrue(result);
    }

    @Test
    public void testEqualWithDifferentValues() {
        EvalContext context = createContext();
        Expression left = createExpression();
        Expression right = createDifferentExpression();
        
        boolean result = operation.equal(context, left, right);
        
        assertFalse(result);
    }

    @Test
    public void testEqualWithNullValues() {
        EvalContext context = createContext();
        Expression left = createNullExpression();
        Expression right = createNullExpression();
        
        boolean result = operation.equal(context, left, right);
        
        assertTrue(result);
    }

    @Test
    public void testEqualWithCollectionValues() {
        EvalContext context = createContext();
        Expression left = createCollectionExpression();
        Expression right = createExpression();
        
        boolean result = operation.equal(context, left, right);
        
        assertFalse(result);
    }

    @Test
    public void testEqualWithNestedExpressions() {
        EvalContext context = createContext();
        Expression left = createNestedExpression();
        Expression right = createExpression();
        
        boolean result = operation.equal(context, left, right);
        
        assertTrue(result);
    }

    private EvalContext createContext() {
        // Create the necessary EvalContext object for testing
    }

    private Expression createExpression() {
        // Create a valid Expression for testing
    }

    private Expression createDifferentExpression() {
        // Create a different Expression for testing
    }

    private Expression createNullExpression() {
        // Create an Expression with null value for testing
    }

    private Expression createCollectionExpression() {
        // Create an Expression with Collection value for testing
    }

    private Expression createNestedExpression() {
        // Create an Expression with nested Expressions for testing
    }
}
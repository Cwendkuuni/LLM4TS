import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    // Test computeValue method
    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression();
        EvalContext context = new EvalContext();
        
        Object result = expr.computeValue(context);
        
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }
    
    // Test getPrecedence method
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression();
        
        int precedence = expr.getPrecedence();
        
        assertEquals(RELATIONAL_EXPR_PRECEDENCE, precedence);
    }
    
    // Test isSymmetric method
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression();
        
        boolean symmetric = expr.isSymmetric();
        
        assertFalse(symmetric);
    }
    
    // Test compute method when operands are NaN
    @Test
    public void testCompute_NaN() {
        CoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression();
        Object left = Double.NaN;
        Object right = Double.NaN;
        
        boolean result = expr.compute(left, right);
        
        assertFalse(result);
    }
    
    // Test compute method when operands are equal
    @Test
    public void testCompute_Equals() {
        CoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression();
        Object left = 5;
        Object right = 5;
        
        boolean result = expr.compute(left, right);
        
        assertFalse(result);
    }
    
    // Test compute method when left operand is less than the right operand
    @Test
    public void testCompute_LessThan() {
        CoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression();
        Object left = 5;
        Object right = 10;
        
        boolean result = expr.compute(left, right);
        
        assertTrue(result);
    }
    
    // Test compute method when left operand is greater than the right operand
    @Test
    public void testCompute_GreaterThan() {
        CoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression();
        Object left = 10;
        Object right = 5;
        
        boolean result = expr.compute(left, right);
        
        assertFalse(result);
    }
    
    // Test reduce method when object is SelfContext
    @Test
    public void testReduce_SelfContext() {
        CoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression();
        Object o = new SelfContext();
        
        Object result = expr.reduce(o);
        
        assertNotNull(result);
        assertTrue(result instanceof SelfContext);
    }
    
    // Test reduce method when object is Collection
    @Test
    public void testReduce_Collection() {
        CoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression();
        Object o = new HashSet<Integer>();
        
        Object result = expr.reduce(o);
        
        assertNotNull(result);
        assertTrue(result instanceof Iterator);
    }
    
    // Test containsMatch method when match is found
    @Test
    public void testContainsMatch_Found() {
        CoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression();
        Iterator it = new HashSet<Integer>().iterator();
        Object value = 5;
        
        boolean result = expr.containsMatch(it, value);
        
        assertTrue(result);
    }
    
    // Test containsMatch method when match is not found
    @Test
    public void testContainsMatch_NotFound() {
        CoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression();
        Iterator it = new HashSet<Integer>().iterator();
        Object value = 10;
        
        boolean result = expr.containsMatch(it, value);
        
        assertFalse(result);
    }
    
    // Test findMatch method when match is found
    @Test
    public void testFindMatch_Found() {
        CoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression();
        Iterator lit = new HashSet<Integer>().iterator();
        Iterator rit = new HashSet<Integer>().iterator();
        
        boolean result = expr.findMatch(lit, rit);
        
        assertTrue(result);
    }
    
    // Test findMatch method when match is not found
    @Test
    public void testFindMatch_NotFound() {
        CoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression();
        Iterator lit = new HashSet<Integer>().iterator();
        Iterator rit = new HashSet<Integer>().iterator();
        
        boolean result = expr.findMatch(lit, rit);
        
        assertFalse(result);
    }
}
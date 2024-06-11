import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationRelationalExpression core = new MockCoreOperationRelationalExpression(arg1, arg2);
        
        // Test case 1: args[0].compute = true, args[1].compute = true
        MockEvalContext context1 = new MockEvalContext();
        assertEquals(Boolean.TRUE, core.computeValue(context1));
        
        // Test case 2: args[0].compute = false, args[1].compute = true
        MockEvalContext context2 = new MockEvalContext();
        context2.setResult(new Object[] {null});
        assertEquals(Boolean.FALSE, core.computeValue(context2));
        
        // Test case 3: args[0].compute = true, args[1].compute = false
        MockEvalContext context3 = new MockEvalContext();
        context3.setValue(Boolean.FALSE);
        assertEquals(Boolean.FALSE, core.computeValue(context3));
        
        // Test case 4: args[0].compute = false, args[1].compute = false
        MockEvalContext context4 = new MockEvalContext();
        context4.setValue(Boolean.FALSE);
        context4.setResult(new Object[] {null});
        assertEquals(Boolean.FALSE, core.computeValue(context4));
    }
    
    @Test
    public void testGetPrecedence() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationRelationalExpression core = new MockCoreOperationRelationalExpression(arg1, arg2);
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, core.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationRelationalExpression core = new MockCoreOperationRelationalExpression(arg1, arg2);
        assertFalse(core.isSymmetric());
    }
    
    @Test
    public void testEvaluateCompare() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationRelationalExpression core = new MockCoreOperationRelationalExpression(arg1, arg2);
        
        // Test case 1: compare = 0
        assertTrue(core.evaluateCompare(0));
        
        // Test case 2: compare < 0
        assertFalse(core.evaluateCompare(-1));
        
        // Test case 3: compare > 0
        assertFalse(core.evaluateCompare(1));
    }
    
    @Test
    public void testCompute() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationRelationalExpression core = new MockCoreOperationRelationalExpression(arg1, arg2);
        
        // Test case 1: left and right are objects with different values
        Object left1 = new Object();
        Object right1 = new Object();
        assertFalse(core.compute(left1, right1));
        
        // Test case 2: left and right are objects with the same value
        Object left2 = new Object();
        Object right2 = left2;
        assertTrue(core.compute(left2, right2));
        
        // Test case 3: left and right are iterators with different values
        Iterator left3 = new MockIterator();
        Iterator right3 = new MockIterator();
        assertFalse(core.compute(left3, right3));
        
        // Test case 4: left and right are iterators with the same value
        Iterator left4 = new MockIterator();
        Iterator right4 = left4;
        assertTrue(core.compute(left4, right4));
        
        // Test case 5: left is an iterator and right is an object with different values
        Iterator left5 = new MockIterator();
        Object right5 = new Object();
        assertFalse(core.compute(left5, right5));
        
        // Test case 6: left is an iterator and right is an object with the same value
        Iterator left6 = new MockIterator();
        Object right6 = left6;
        assertTrue(core.compute(left6, right6));
        
        // Test case 7: left is an iterator and right is an initial context
        Iterator left7 = new MockIterator();
        InitialContext right7 = new InitialContext();
        assertFalse(core.compute(left7, right7));
        
        // Test case 8: left is an initial context and right is an iterator
        InitialContext left8 = new InitialContext();
        Iterator right8 = new MockIterator();
        assertFalse(core.compute(left8, right8));
        
        // Test case 9: left is an initial context and right is an object with different values
        InitialContext left9 = new InitialContext();
        Object right9 = new Object();
        assertFalse(core.compute(left9, right9));
        
        // Test case 10: left is an initial context and right is an object with the same value
        InitialContext left10 = new InitialContext();
        Object right10 = left10;
        assertTrue(core.compute(left10, right10));
        
        // Test case 11: left is an initial context and right is a double with NaN value
        InitialContext left11 = new InitialContext();
        double right11 = Double.NaN;
        assertFalse(core.compute(left11, right11));
        
        // Test case 12: left is an initial context and right is a double with some value
        InitialContext left12 = new InitialContext();
        double right12 = 1.0;
        assertTrue(core.compute(left12, right12));
    }
    
    @Test
    public void testReduce() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationRelationalExpression core = new MockCoreOperationRelationalExpression(arg1, arg2);
        
        // Test case 1: o is an instance of SelfContext
        Object o1 = new SelfContext();
        Object result1 = core.reduce(o1);
        assertTrue(result1 instanceof SelfContext);
        
        // Test case 2: o is a Collection
        Object o2 = new HashSet<>();
        Object result2 = core.reduce(o2);
        assertTrue(result2 instanceof Iterator);
        
        // Test case 3: o is neither a SelfContext nor a Collection
        Object o3 = new Object();
        Object result3 = core.reduce(o3);
        assertEquals(o3, result3);
    }
    
    @Test
    public void testContainsMatch() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationRelationalExpression core = new MockCoreOperationRelationalExpression(arg1, arg2);
        
        // Test case 1: it has a match
        Iterator it1 = new MockIterator();
        Object value1 = new Object();
        assertTrue(core.containsMatch(it1, value1));
        
        // Test case 2: it does not have a match
        Iterator it2 = new MockIterator();
        Object value2 = new Object();
        assertFalse(core.containsMatch(it2, value2));
    }
    
    @Test
    public void testFindMatch() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationRelationalExpression core = new MockCoreOperationRelationalExpression(arg1, arg2);
        
        // Test case 1: left has a match with right
        Iterator lit1 = new MockIterator();
        Iterator rit1 = new MockIterator();
        assertTrue(core.findMatch(lit1, rit1));
        
        // Test case 2: left does not have a match with right
        Iterator lit2 = new MockIterator();
        Iterator rit2 = new MockIterator();
        assertFalse(core.findMatch(lit2, rit2));
    }
}
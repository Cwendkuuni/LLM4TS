import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        
        // Test case 1
        EvalContext context1 = new EvalContext();
        context1.setSomeProperty("value1");
        Object result1 = expr.computeValue(context1);
        assertEquals(Boolean.FALSE, result1);
        
        // Test case 2
        EvalContext context2 = new EvalContext();
        context2.setSomeProperty("value2");
        Object result2 = expr.computeValue(context2);
        assertEquals(Boolean.TRUE, result2);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        
        int result = expr.getPrecedence();
        assertEquals(RELATIONAL_EXPR_PRECEDENCE, result);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        
        boolean result = expr.isSymmetric();
        assertEquals(false, result);
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        
        // Test case 1
        int compare1 = 1;
        boolean result1 = expr.evaluateCompare(compare1);
        assertEquals(true, result1);
        
        // Test case 2
        int compare2 = 0;
        boolean result2 = expr.evaluateCompare(compare2);
        assertEquals(false, result2);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        
        // Test case 1
        Object left1 = new Object();
        Object right1 = new Object();
        boolean result1 = expr.compute(left1, right1);
        assertEquals(true, result1);
        
        // Test case 2
        Object left2 = new Object();
        Object right2 = null;
        boolean result2 = expr.compute(left2, right2);
        assertEquals(false, result2);
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        
        // Test case 1
        Object o1 = new Object();
        Object result1 = expr.reduce(o1);
        assertEquals(o1, result1);
        
        // Test case 2
        Object o2 = new SelfContext();
        Object result2 = expr.reduce(o2);
        assertEquals(((SelfContext) o2).getSingleNodePointer(), result2);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        
        // Test case 1
        Iterator it1 = new Iterator();
        Object value1 = new Object();
        boolean result1 = expr.containsMatch(it1, value1);
        assertEquals(false, result1);
        
        // Test case 2
        Iterator it2 = new Iterator();
        Object value2 = new Object();
        it2.add(value2);
        boolean result2 = expr.containsMatch(it2, value2);
        assertEquals(true, result2);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        
        // Test case 1
        Iterator lit1 = new Iterator();
        Iterator rit1 = new Iterator();
        boolean result1 = expr.findMatch(lit1, rit1);
        assertEquals(false, result1);
        
        // Test case 2
        Iterator lit2 = new Iterator();
        Object leftElement2 = new Object();
        lit2.add(leftElement2);
        Iterator rit2 = new Iterator();
        boolean result2 = expr.findMatch(lit2, rit2);
        assertEquals(false, result2);
        
        // Test case 3
        Iterator lit3 = new Iterator();
        lit3.add(leftElement2);
        Iterator rit3 = new Iterator();
        Object rightElement3 = new Object();
        rit3.add(rightElement3);
        boolean result3 = expr.findMatch(lit3, rit3);
        assertEquals(false, result3);
        
        // Test case 4
        Iterator lit4 = new Iterator();
        lit4.add(leftElement2);
        Iterator rit4 = new Iterator();
        Object rightElement4 = new Object();
        rit4.add(rightElement4);
        rit4.add(leftElement2);
        boolean result4 = expr.findMatch(lit4, rit4);
        assertEquals(true, result4);
    }

}
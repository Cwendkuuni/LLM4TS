import org.junit.Test;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression testObject;

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        Expression expression1 = new Expression();
        Expression expression2 = new Expression();
        args[0] = expression1;
        args[1] = expression2;
        testObject = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        Object result = testObject.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        Expression expression1 = new Expression();
        Expression expression2 = new Expression();
        args[0] = expression1;
        args[1] = expression2;
        testObject = new CoreOperationRelationalExpression(args);
        int result = testObject.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        Expression expression1 = new Expression();
        Expression expression2 = new Expression();
        args[0] = expression1;
        args[1] = expression2;
        testObject = new CoreOperationRelationalExpression(args);
        boolean result = testObject.isSymmetric();
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        Expression expression1 = new Expression();
        Expression expression2 = new Expression();
        args[0] = expression1;
        args[1] = expression2;
        testObject = new CoreOperationRelationalExpression(args);
        boolean result = testObject.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        Expression expression1 = new Expression();
        Expression expression2 = new Expression();
        args[0] = expression1;
        args[1] = expression2;
        testObject = new CoreOperationRelationalExpression(args);
        boolean result = testObject.compute(null, null);
        assertFalse(result);
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        Expression expression1 = new Expression();
        Expression expression2 = new Expression();
        args[0] = expression1;
        args[1] = expression2;
        testObject = new CoreOperationRelationalExpression(args);
        Object result = testObject.reduce(null);
        assertNull(result);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        Expression expression1 = new Expression();
        Expression expression2 = new Expression();
        args[0] = expression1;
        args[1] = expression2;
        testObject = new CoreOperationRelationalExpression(args);
        Iterator it = new HashSet().iterator();
        boolean result = testObject.containsMatch(it, null);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        Expression expression1 = new Expression();
        Expression expression2 = new Expression();
        args[0] = expression1;
        args[1] = expression2;
        testObject = new CoreOperationRelationalExpression(args);
        Iterator lit = new HashSet().iterator();
        Iterator rit = new HashSet().iterator();
        boolean result = testObject.findMatch(lit, rit);
        assertFalse(result);
    }
}
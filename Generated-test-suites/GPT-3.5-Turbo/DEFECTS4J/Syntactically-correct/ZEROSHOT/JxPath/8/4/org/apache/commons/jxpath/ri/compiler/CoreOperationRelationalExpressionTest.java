import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test case 1
        Expression[] args1 = new Expression[2];
        args1[0] = new CoreConstant("abc");
        args1[1] = new CoreConstant("def");
        CoreOperationRelationalExpression op1 = new CoreOperationRelationalExpression(args1);
        assertEquals(Boolean.FALSE, op1.computeValue(null));

        // Test case 2
        Expression[] args2 = new Expression[2];
        args2[0] = new CoreConstant(123);
        args2[1] = new CoreConstant(456);
        CoreOperationRelationalExpression op2 = new CoreOperationRelationalExpression(args2);
        assertEquals(Boolean.TRUE, op2.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new CoreConstant("abc");
        args[1] = new CoreConstant("def");
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(args);
        assertEquals(3, op.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new CoreConstant("abc");
        args[1] = new CoreConstant("def");
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(args);
        assertFalse(op.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new CoreConstant("abc");
        args[1] = new CoreConstant("def");
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(args);
        assertTrue(op.evaluateCompare(-1));
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new CoreConstant("abc");
        args[1] = new CoreConstant("def");
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(args);
        assertFalse(op.compute("abc", "def"));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new CoreConstant("abc");
        args[1] = new CoreConstant("def");
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(args);
        assertEquals("abc", op.reduce("abc"));
        assertEquals("def", op.reduce("def"));
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new CoreConstant("abc");
        args[1] = new CoreConstant("def");
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(args);
        assertFalse(op.containsMatch(null, null));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new CoreConstant("abc");
        args[1] = new CoreConstant("def");
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(args);
        assertFalse(op.findMatch(null, null));
    }
}
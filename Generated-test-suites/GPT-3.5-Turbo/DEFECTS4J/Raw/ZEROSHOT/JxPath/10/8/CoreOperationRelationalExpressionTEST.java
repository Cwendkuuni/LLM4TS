import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Expression[] args = new Expression[2];
        args[0] = new ConstantValueExpression(5);
        args[1] = new ConstantValueExpression(3);
        CoreOperationRelationalExpression expression = new LessThanExpression(args);
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new ConstantValueExpression(5);
        args[1] = new ConstantValueExpression(3);
        CoreOperationRelationalExpression expression = new LessThanExpression(args);
        int precedence = expression.getPrecedence();
        Assert.assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new ConstantValueExpression(5);
        args[1] = new ConstantValueExpression(3);
        CoreOperationRelationalExpression expression = new LessThanExpression(args);
        boolean symmetric = expression.isSymmetric();
        Assert.assertEquals(false, symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new ConstantValueExpression(5);
        args[1] = new ConstantValueExpression(3);
        CoreOperationRelationalExpression expression = new LessThanExpression(args);
        boolean evaluateCompare = expression.evaluateCompare(-1);
        Assert.assertEquals(true, evaluateCompare);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new ConstantValueExpression(5);
        args[1] = new ConstantValueExpression(3);
        CoreOperationRelationalExpression expression = new LessThanExpression(args);
        boolean compute = expression.compute(args[0].computeValue(context), args[1].computeValue(context));
        Assert.assertEquals(false, compute);
    }

    @Test
    public void testReduce() {
        Object o = new SelfContext();
        Expression[] args = new Expression[2];
        args[0] = new ConstantValueExpression(5);
        args[1] = new ConstantValueExpression(3);
        CoreOperationRelationalExpression expression = new LessThanExpression(args);
        Object reduce = expression.reduce(o);
        Assert.assertEquals(o, reduce);
    }

    @Test
    public void testContainsMatch() {
        Iterator it = new Iterator() {
            private int count = 0;
            public boolean hasNext() {
                return count < 1;
            }
            public Object next() {
                count++;
                return 5;
            }
        };
        Object value = 5;
        Expression[] args = new Expression[2];
        args[0] = new ConstantValueExpression(5);
        args[1] = new ConstantValueExpression(3);
        CoreOperationRelationalExpression expression = new LessThanExpression(args);
        boolean containsMatch = expression.containsMatch(it, value);
        Assert.assertEquals(true, containsMatch);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator() {
            private int count = 0;
            public boolean hasNext() {
                return count < 1;
            }
            public Object next() {
                count++;
                return 5;
            }
        };
        Iterator rit = new Iterator() {
            private int count = 0;
            public boolean hasNext() {
                return count < 1;
            }
            public Object next() {
                count++;
                return 3;
            }
        };
        Expression[] args = new Expression[2];
        args[0] = new ConstantValueExpression(5);
        args[1] = new ConstantValueExpression(3);
        CoreOperationRelationalExpression expression = new LessThanExpression(args);
        boolean findMatch = expression.findMatch(lit, rit);
        Assert.assertEquals(false, findMatch);
    }
}
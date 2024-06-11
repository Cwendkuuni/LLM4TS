import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Collection;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args){ 
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        EvalContext context = new EvalContext();
        Assert.assertEquals(Boolean.TRUE, coreOperationRelationalExpression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        Assert.assertEquals(3, coreOperationRelationalExpression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        Assert.assertEquals(false, coreOperationRelationalExpression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        Assert.assertEquals(false, coreOperationRelationalExpression.evaluateCompare(0));
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args){ 
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        Object left = new Object();
        Object right = new Object();
        Assert.assertEquals(true, coreOperationRelationalExpression.compute(left, right));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        Object o = new Object();
        Assert.assertEquals(o, coreOperationRelationalExpression.reduce(o));
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args); 
        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Object value = new Object();
        Assert.assertEquals(false, coreOperationRelationalExpression.containsMatch(it, value));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args); 
        Iterator lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Assert.assertEquals(false, coreOperationRelationalExpression.findMatch(lit, rit));
    }

    private class Expression extends org.apache.commons.jxpath.ri.compiler.Expression {

        @Override
        public Object computeValue(org.apache.commons.jxpath.ri.EvalContext context) {
            return null;
        }
    }

}
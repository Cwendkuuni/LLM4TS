import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = {new TestExpression(), new TestExpression()};
        CoreOperationRelationalExpression coreOperation = new TestCoreOperationRelationalExpression(args);

        EvalContext context = new TestEvalContext();
        Boolean result = (Boolean) coreOperation.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = {new TestExpression(), new TestExpression()};
        CoreOperationRelationalExpression coreOperation = new TestCoreOperationRelationalExpression(args);

        int precedence = coreOperation.getPrecedence();

        Assert.assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = {new TestExpression(), new TestExpression()};
        CoreOperationRelationalExpression coreOperation = new TestCoreOperationRelationalExpression(args);

        boolean symmetric = coreOperation.isSymmetric();

        Assert.assertFalse(symmetric);
    }

    private class TestExpression extends Expression {
        @Override
        public Object computeValue(EvalContext evalContext) {
            return null;
        }
    }

    private class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        public TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return true;
        }
    }

    private class TestEvalContext extends EvalContext {
        @Override
        public Object getSingleNodePointer() {
            return null;
        }
    }
}
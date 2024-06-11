import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_LessThan() {
        CoreOperationRelationalExpression expression = createExpression();
        EvalContext context = createEvalContext();
        context.setSingleNodePointer(1);

        Object result = expression.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_GreaterThan() {
        CoreOperationRelationalExpression expression = createExpression();
        EvalContext context = createEvalContext();
        context.setSingleNodePointer(10);

        Object result = expression.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_Equals() {
        CoreOperationRelationalExpression expression = createExpression();
        EvalContext context = createEvalContext();
        context.setSingleNodePointer(5);

        Object result = expression.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_InvalidComparisonOperands() {
        CoreOperationRelationalExpression expression = createExpression();
        EvalContext context = createEvalContext();
        context.setSingleNodePointer("not a number");

        Object result = expression.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftIteratorContainsMatch() {
        CoreOperationRelationalExpression expression = createExpression();
        EvalContext context = createEvalContext();
        context.setSingleNodePointer(Arrays.asList(1, 2, 3).iterator());

        Object result = expression.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_RightIteratorContainsMatch() {
        CoreOperationRelationalExpression expression = createExpression();
        EvalContext context = createEvalContext();
        context.setSingleNodePointer(4);
        InitialContext initialContext = new InitialContext(Collections.singletonList(1).iterator());
        context.setSingleNodePointer(initialContext);

        Object result = expression.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_NoIntersectionBetweenIterators() {
        CoreOperationRelationalExpression expression = createExpression();
        EvalContext context = createEvalContext();
        context.setSingleNodePointer(Arrays.asList(1, 2, 3).iterator());
        InitialContext initialContext = new InitialContext(Arrays.asList(4, 5, 6).iterator());
        context.setSingleNodePointer(initialContext);

        Object result = expression.computeValue(context);

        assertFalse((Boolean) result);
    }

    private CoreOperationRelationalExpression createExpression() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        return new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
    }

    private EvalContext createEvalContext() {
        return new EvalContext(null);
    }

    private static class DummyExpression extends Expression {
        protected DummyExpression() {
            super(null);
        }

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }

        @Override
        protected int getPrecedence() {
            return 0;
        }

        @Override
        protected boolean isSymmetric() {
            return false;
        }
    }
}
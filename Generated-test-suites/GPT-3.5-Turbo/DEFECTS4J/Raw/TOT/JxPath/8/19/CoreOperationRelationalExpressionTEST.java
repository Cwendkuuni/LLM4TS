
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_GreaterThan() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(5);
        args[1] = new MockExpression(3);
        CoreOperationRelationalExpression op = new MockCoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.TRUE, op.computeValue(context));
    }

    @Test
    public void testComputeValue_GreaterThanOrEqualTo() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(5);
        args[1] = new MockExpression(5);
        CoreOperationRelationalExpression op = new MockCoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.TRUE, op.computeValue(context));
    }

    @Test
    public void testComputeValue_LessThan() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(3);
        args[1] = new MockExpression(5);
        CoreOperationRelationalExpression op = new MockCoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.TRUE, op.computeValue(context));
    }

    @Test
    public void testComputeValue_LessThanOrEqualTo() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(5);
        args[1] = new MockExpression(5);
        CoreOperationRelationalExpression op = new MockCoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.TRUE, op.computeValue(context));
    }

    @Test
    public void testComputeValue_InvalidInput() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression("5");
        args[1] = new MockExpression(3);
        CoreOperationRelationalExpression op = new MockCoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext(null);
        assertEquals(Boolean.FALSE, op.computeValue(context));
    }

    private static class MockExpression extends Expression {
        private Object value;

        public MockExpression(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }

        protected int getPrecedence() {
            return 0;
        }

        protected boolean isSymmetric() {
            return false;
        }

        protected boolean evaluateCompare(int compare) {
            return false;
        }
    }

    private static class MockCoreOperationRelationalExpression extends CoreOperationRelationalExpression {

        public MockCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
    }

}

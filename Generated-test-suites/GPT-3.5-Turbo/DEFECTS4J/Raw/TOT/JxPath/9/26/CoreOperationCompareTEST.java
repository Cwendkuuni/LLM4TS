import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void equalTest() {
        // Typical use case: equal values
        EvalContext context = new EvalContext();
        Expression left = new ExpressionTest();
        Expression right = new ExpressionTest();
        CoreOperationCompare operation = new CoreOperationCompare(left, right);
        boolean result = operation.equal(context, left, right);
        assertTrue(result);

        // Typical use case: unequal values
        left = new ExpressionTest("value1");
        right = new ExpressionTest("value2");
        operation = new CoreOperationCompare(left, right);
        result = operation.equal(context, left, right);
        assertFalse(result);

        // Edge case: Null values
        left = new ExpressionTest(null);
        right = new ExpressionTest(null);
        operation = new CoreOperationCompare(left, right);
        result = operation.equal(context, left, right);
        assertTrue(result);

        // Error scenario: One value is null
        left = new ExpressionTest(null);
        right = new ExpressionTest("value");
        operation = new CoreOperationCompare(left, right);
        result = operation.equal(context, left, right);
        assertFalse(result);

        // Error scenario: One value is NaN
        left = new ExpressionTest(Double.NaN);
        right = new ExpressionTest(1.0);
        operation = new CoreOperationCompare(left, right);
        result = operation.equal(context, left, right);
        assertFalse(result);

        // Error scenario: Values are pointers
        left = new ExpressionTest(new Object());
        right = new ExpressionTest(new Object());
        operation = new CoreOperationCompare(left, right);
        result = operation.equal(context, left, right);
        assertFalse(result);
    }

    private class ExpressionTest extends Expression {
        private Object value;

        public ExpressionTest() {
        }

        public ExpressionTest(Object value) {
            this.value = value;
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            return new ArrayList<>().iterator();
        }

        @Override
        public Object compute(EvalContext context) {
            return value;
        }

        @Override
        protected boolean computeContextDependent() {
            return false;
        }
    }
}
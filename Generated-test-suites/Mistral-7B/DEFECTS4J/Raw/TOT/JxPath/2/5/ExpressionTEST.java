import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Expression.PointerIterator;
import org.apache.commons.jxpath.ri.compiler.Expression.ValueIterator;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

    @Test
    public void testContextDependentExpression() {
        ContextDependentExpression expression = new ContextDependentExpression();
        Expression context = new EvalContext(new Object());

        // context-dependent expression should be re-evaluated each time
        assertTrue(expression.isContextDependent());
        assertEquals("context-dependent value", expression.computeValue(context), expression.computeValue(context));

        // test iterate method
        Iterator<Object> iterator = expression.iterate(context);
        Object firstValue = iterator.next();
        assertSame(firstValue, expression.computeValue(context));
        assertNull(iterator.next());
    }

    @Test
    public void testContextIndependentExpression() {
        ContextIndependentExpression expression = new ContextIndependentExpression();
        Expression context = new EvalContext(new Object());

        // context-independent expression should not be re-evaluated each time
        assertFalse(expression.isContextDependent());
        Object value1 = expression.computeValue(context);
        Object value2 = expression.computeValue(context);
        assertNotSame(value1, value2);

        // test iterate method
        Iterator<Object> iterator = expression.iterate(context);
        assertSame(expression.computeValue(context), iterator.next());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratePointers() {
        ContextDependentExpressionWithPointers expression = new ContextDependentExpressionWithPointers();
        Expression context = new EvalContext(new Object());

        // context-dependent expression with pointers should be re-evaluated each time
        assertTrue(expression.isContextDependent());

        // test iteratePointers method
        Iterator<Pointer> iterator = expression.iteratePointers(context);
        Pointer firstPointer = iterator.next();
        assertSame(firstPointer.getValue(), expression.computeValue(context));
        assertNull(iterator.next());
    }

    static class ContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return "context-dependent value";
        }
    }

    static class ContextIndependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return new Integer(42);
        }
    }

    static class ContextDependentExpressionWithPointers extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return new QName(null, "test");
        }
    }
}
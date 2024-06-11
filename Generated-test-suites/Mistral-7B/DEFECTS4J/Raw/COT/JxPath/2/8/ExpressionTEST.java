import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.apache.commons.jxpath.ri.compiler.Expression.ZERO;
import static org.apache.commons.jxpath.ri.compiler.Expression.computeContextDependent;
import static org.apache.commons.jxpath.ri.compiler.Expression.computeValue;
import static org.apache.commons.jxpath.ri.compiler.Expression.compute;
import static org.apache.commons.jxpath.ri.compiler.Expression.NOT_A_NUMBER;

/**
 * Test class for Expression.java
 */
public class ExpressionTest {

    /**
     * Test method for {@link Expression#isContextDependent()}
     */
    @Test
    public void testIsContextDependent() {
        AbstractExpression expression = new MyExpression();
        Assert.assertFalse(expression.isContextDependent());
    }

    /**
     * Test method for {@link Expression#computeContextDependent()}
     */
    @Test
    public void testComputeContextDependent() {
        MyExpression expression = new MyExpression();
        expression.setContextDependent(true);
        Assert.assertTrue(computeContextDependent(expression));
    }

    /**
     * Test method for {@link Expression#computeValue(EvalContext)}
     */
    @Test
    public void testComputeValue() {
        MyExpression expression = new MyExpression();
        EvalContext context = new EvalContext(Collections.emptyList());
        Object value = computeValue(expression, context);
        Assert.assertEquals(ZERO, value);
    }

    /**
     * Test method for {@link Expression#compute(EvalContext)}
     */
    @Test
    public void testCompute() {
        MyExpression expression = new MyExpression();
        EvalContext context = new EvalContext(Collections.emptyList());
        Object result = compute(expression, context);
        Assert.assertEquals(ZERO, result);
    }

    /**
     * Test method for {@link Expression#iterate(EvalContext)}
     */
    @Test
    public void testIterate() {
        MyExpression expression = new MyExpression();
        EvalContext context = new EvalContext(Collections.emptyList());
        Iterator<Object> iterator = expression.iterate(context);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(ZERO, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * Test method for {@link Expression#iteratePointers(EvalContext)}
     */
    @Test
    public void testIteratePointers() {
        MyExpression expression = new MyExpression();
        EvalContext context = new EvalContext(Collections.emptyList());
        Iterator<Pointer> iterator = expression.iteratePointers(context);
        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * Test method for exception handling
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        MyExpression expression = new MyExpression();
        expression.iterate(new EvalContext(Collections.emptyList())).next();
        expression.remove();
    }

    /**
     * Test method for {@link Expression#computeValue(EvalContext)} with NaN
     */
    @Test
    public void testComputeValueNaN() {
        NaNExpression expression = new NaNExpression();
        EvalContext context = new EvalContext(Collections.emptyList());
        Object value = computeValue(expression, context);
        Assert.assertEquals(NOT_A_NUMBER, value);
    }

    /**
     * Test method for {@link Expression#computeValue(EvalContext)} with number
     */
    @Test
    public void testComputeValueNumber() {
        NumberExpression expression = new NumberExpression();
        EvalContext context = new EvalContext(Collections.emptyList());
        Object value = computeValue(expression, context);
        Assert.assertEquals(ONE, value);
    }

    /**
     * Test method for {@link Expression#compute(EvalContext)} with null context
     */
    @Test(expected = IllegalArgumentException.class)
    public void testComputeNullContext() {
        MyExpression expression = new MyExpression();
        expression.compute(null);
    }

    /**
     * Test method for {@link Expression#compute(EvalContext)} with empty context
     */
    @Test
    public void testComputeEmptyContext() {
        MyExpression expression = new MyExpression();
        EvalContext context = new EvalContext(Collections.emptyList());
        Object result = compute(expression, context);
        Assert.assertEquals(ZERO, result);
    }

    /**
     * Test method for {@link Expression#computeValue(EvalContext)} with context
     */
    @Test
    public void testComputeValueContext() {
        MyExpression expression = new MyExpression();
        EvalContext context = new EvalContext(Collections.singletonList(new NodePointer("root", null, null, null)));
        Object value = computeValue(expression, context);
        Assert.assertEquals(ONE, value);
    }

    private abstract static class AbstractExpression extends Expression {
    }

    private static class MyExpression extends AbstractExpression {
        private boolean contextDependent;

        public void setContextDependent(boolean contextDependent) {
            this.contextDependent = contextDependent;
        }

        @Override
        public boolean computeContextDependent() {
            return contextDependent;
        }
    }

    private static class NaNExpression extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return NOT_A_NUMBER;
        }

        @Override
        public Object compute(EvalContext context) {
            return NOT_A_NUMBER;
        }
    }

    private static class NumberExpression extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return ONE;
        }

        @Override
        public Object compute(EvalContext context) {
            return ONE;
        }
    }
}
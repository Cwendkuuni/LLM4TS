import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ExpressionTest {

    @Test
    public void testIsContextDependent_ConstantExpression_ReturnsFalse() {
        Expression expression = new ConstantExpression("test");
        boolean result = expression.isContextDependent();

        Assert.assertFalse(result);
    }

    @Test
    public void testIsContextDependent_VariableExpression_ReturnsTrue() {
        Expression expression = new VariableExpression("$variable");
        boolean result = expression.isContextDependent();

        Assert.assertTrue(result);
    }

    @Test
    public void testIsContextDependent_ComputeContextDependent_ReturnsTrue() {
        Expression expression = new CustomExpression();
        boolean result = expression.isContextDependent();

        Assert.assertTrue(result);
    }

    @Test
    public void testComputeValue_ConstantExpression_ReturnsConstantValue() {
        Expression expression = new ConstantExpression("test");
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        Assert.assertEquals("test", result);
    }

    @Test
    public void testComputeValue_VariableExpression_ReturnsVariableValue() {
        Expression expression = new VariableExpression("$variable");
        EvalContext context = new EvalContext();
        context.setVariable("variable", "test");

        Object result = expression.computeValue(context);

        Assert.assertEquals("test", result);
    }

    @Test
    public void testComputeValue_ComputeExpression_ReturnsComputedValue() {
        Expression expression = new CustomExpression();
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        Assert.assertEquals("computed", result);
    }

    @Test
    public void testCompute_ConstantExpression_ReturnsConstantValue() {
        Expression expression = new ConstantExpression("test");
        EvalContext context = new EvalContext();

        Object result = expression.compute(context);

        Assert.assertEquals("test", result);
    }

    @Test
    public void testCompute_VariableExpression_ReturnsVariableValue() {
        Expression expression = new VariableExpression("$variable");
        EvalContext context = new EvalContext();
        context.setVariable("variable", "test");

        Object result = expression.compute(context);

        Assert.assertEquals("test", result);
    }

    @Test
    public void testCompute_ComputeExpression_ReturnsComputedValue() {
        Expression expression = new CustomExpression();
        EvalContext context = new EvalContext();

        Object result = expression.compute(context);

        Assert.assertEquals("computed", result);
    }

    @Test
    public void testIterate_ConstantExpression_ReturnsIteratorWithConstantValue() {
        Expression expression = new ConstantExpression("test");
        EvalContext context = new EvalContext();

        Iterator result = expression.iterate(context);

        Assert.assertTrue(result.hasNext());
        Assert.assertEquals("test", result.next());
        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIterate_VariableExpression_ReturnsIteratorWithVariableValue() {
        Expression expression = new VariableExpression("$variable");
        EvalContext context = new EvalContext();
        context.setVariable("variable", "test");

        Iterator result = expression.iterate(context);

        Assert.assertTrue(result.hasNext());
        Assert.assertEquals("test", result.next());
        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIterate_ComputeExpression_ReturnsIteratorWithComputedValue() {
        Expression expression = new CustomExpression();
        EvalContext context = new EvalContext();

        Iterator result = expression.iterate(context);

        Assert.assertTrue(result.hasNext());
        Assert.assertEquals("computed", result.next());
        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIteratePointers_NullResult_ReturnsEmptyIterator() {
        Expression expression = new ConstantExpression(null);
        EvalContext context = new EvalContext();

        Iterator result = expression.iteratePointers(context);

        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIteratePointers_EvalContextResult_ReturnsEvalContextIterator() {
        Expression expression = new CustomExpression();
        EvalContext context = new EvalContext();

        Iterator result = expression.iteratePointers(context);

        Assert.assertEquals(context, result.next());
        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIteratePointers_OtherResult_ReturnsPointerIterator() {
        List<Object> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        Expression expression = new ConstantExpression(values);
        EvalContext context = new EvalContext();

        Iterator result = expression.iteratePointers(context);

        Assert.assertTrue(result.hasNext());
        Assert.assertTrue(result.next() instanceof NodePointer);
        Assert.assertFalse(result.hasNext());
    }

    private static class ConstantExpression extends Expression {
        private Object constantValue;

        public ConstantExpression(Object constantValue) {
            this.constantValue = constantValue;
        }

        public boolean computeContextDependent() {
            return false;
        }

        public Object computeValue(EvalContext context) {
            return constantValue;
        }

        public Object compute(EvalContext context) {
            return constantValue;
        }
    }

    private static class VariableExpression extends Expression {
        private String variableName;

        public VariableExpression(String variableName) {
            this.variableName = variableName;
        }

        public boolean computeContextDependent() {
            return true;
        }

        public Object computeValue(EvalContext context) {
            return context.getVariable(variableName);
        }

        public Object compute(EvalContext context) {
            return context.getVariable(variableName);
        }
    }

    private static class CustomExpression extends Expression {
        public boolean computeContextDependent() {
            return true;
        }

        public Object computeValue(EvalContext context) {
            return "computed";
        }

        public Object compute(EvalContext context) {
            return "computed";
        }
    }
}
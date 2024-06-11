import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testGreaterThanOrEqual() {
        // Test with double values
        double a = 10.5;
        double b = 5.0;
        CoreOperationGreaterThanOrEqual expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(a), new ConstantExpression(b));
        EvalContext context = new EvalContext();
        JXPathContext jxpathContext = new JXPathContext(new InfoSet());
        jxpathContext.setThis(context);
        Path path = new Path("/", expr);
        Object result = path.compile(jxpathContext).evaluate(jxpathContext, null);
        assertTrue("Expected true for: " + a + " >= " + b, result instanceof Boolean && ((Boolean) result).booleanValue());

        // Test with negative double values
        double c = -5.5;
        double d = -10.0;
        expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(c), new ConstantExpression(d));
        context = new EvalContext();
        jxpathContext = new JXPathContext(new InfoSet());
        jxpathContext.setThis(context);
        path = new Path("/", expr);
        result = path.compile(jxpathContext).evaluate(jxpathContext, null);
        assertTrue("Expected true for: " + c + " >= " + d, result instanceof Boolean && ((Boolean) result).booleanValue());

        // Test with equal double values
        double e = 5.5;
        double f = 5.5;
        expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(e), new ConstantExpression(f));
        context = new EvalContext();
        jxpathContext = new JXPathContext(new InfoSet());
        jxpathContext.setThis(context);
        path = new Path("/", expr);
        result = path.compile(jxpathContext).evaluate(jxpathContext, null);
        assertEquals("Expected equal for: " + e + " >= " + f, Boolean.TRUE, result);

        // Test with single digit integers
        int g = 10;
        int h = 5;
        expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(g), new ConstantExpression(h));
        context = new EvalContext();
        jxpathContext = new JXPathContext(new InfoSet());
        jxpathContext.setThis(context);
        path = new Path("/", expr);
        result = path.compile(jxpathContext).evaluate(jxpathContext, null);
        assertTrue("Expected true for: " + g + " >= " + h, result instanceof Boolean && ((Boolean) result).booleanValue());

        // Test with negative single digit integers
        int i = -5;
        int j = -10;
        expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(i), new ConstantExpression(j));
        context = new EvalContext();
        jxpathContext = new JXPathContext(new InfoSet());
        jxpathContext.setThis(context);
        path = new Path("/", expr);
        result = path.compile(jxpathContext).evaluate(jxpathContext, null);
        assertTrue("Expected true for: " + i + " >= " + j, result instanceof Boolean && ((Boolean) result).booleanValue());

        // Test with double and integer values
        double k = 5.5;
        int l = 10;
        expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(k), new ConstantExpression(l));
        context = new EvalContext();
        jxpathContext = new JXPathContext(new InfoSet());
        jxpathContext.setThis(context);
        path = new Path("/", expr);
        result = path.compile(jxpathContext).evaluate(jxpathContext, null);
        assertFalse("Expected false for: " + k + " >= " + l, result instanceof Boolean && ((Boolean) result).booleanValue());

        // Test with null values
        Double m = null;
        Double n = null;
        expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(m), new ConstantExpression(n));
        context = new EvalContext();
        jxpathContext = new JXPathContext(new InfoSet());
        jxpathContext.setThis(context);
        path = new Path("/", expr);
        result = path.compile(jxpathContext).evaluate(jxpathContext, null);
        assertEquals("Expected null for: " + m + " >= " + n, null, result);

        // Test with zero values
        double o = 0.0;
        double p = 5.0;
        expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(o), new ConstantExpression(p));
        context = new EvalContext();
        jxpathContext = new JXPathContext(new InfoSet());
        jxpathContext.setThis(context);
        path = new Path("/", expr);
        result = path.compile(jxpathContext).evaluate(jxpathContext, null);
        assertFalse("Expected false for: " + o + " >= " + p, result instanceof Boolean && ((Boolean) result).booleanValue());

        // Test with NaN values
        double q = Double.NaN;
        double r = Double.NaN;
        expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(q), new ConstantExpression(r));
        context = new EvalContext();
        jxpathContext = new JXPathContext(new InfoSet());
        jxpathContext.setThis(context);
        path = new Path("/", expr);
        result = path.compile(jxpathContext).evaluate(jxpathContext, null);
        assertEquals("Expected NaN for: " + q + " >= " + r, Double.NaN, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArguments() {
        // Test with null arguments
        CoreOperationGreaterThanOrEqual expr = new CoreOperationGreaterThanOrEqual(null, new ConstantExpression(5.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOneArgument() {
        // Test with one argument
        CoreOperationGreaterThanOrEqual expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0));
    }
}
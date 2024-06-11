import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.Variable;
import org.apache.commons.jxpath.ri.eval.ExpressionEvaluator;
import org.apache.commons.jxpath.ri.iter.ArrayIterator;
import org.apache.commons.jxpath.ri.iter.IteratorSupport;
import org.apache.commons.jxpath.ri.iter.SingleNodeIterator;
import org.apache.commons.jxpath.ri.util.SimpleNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CoreOperationCompare}
 */
public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare expression = new CoreOperationCompare(
            new Variable("a"),
            new Variable("a")
        );

        JXPathContext context = JXPathContext.newContext(new Object[]{"a", "a"});

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Object result = evaluator.eval(expression, context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = JXPathContext.newContext(new Object[]{"a", "b"});

        result = evaluator.eval(expression, context);

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testNotEqual() {
        CoreOperationCompare expression = new CoreOperationCompare(
            new Variable("a"),
            new Variable("b")
        );

        JXPathContext context = JXPathContext.newContext(new Object[]{"a", "a"});

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Object result = evaluator.eval(expression, context);

        assertFalse(result instanceof Boolean);

        context = JXPathContext.newContext(new Object[]{"a", "b"});

        result = evaluator.eval(expression, context);

        assertTrue(result instanceof Boolean);
        assertTrue(!((Boolean) result).booleanValue());

        context = JXPathContext.newContext(new Object[]{"a", null});

        result = evaluator.eval(expression, context);

        assertTrue(result instanceof Boolean);
        assertTrue(!((Boolean) result).booleanValue());
    }

    @Test
    public void testCompareCollections() {
        CoreOperationCompare expression = new CoreOperationCompare(
                new Variable("a"),
                new Variable("b")
        );

        List<Object> listA = new ArrayList<>();
        listA.add(1);
        listA.add(2);
        listA.add(3);

        List<Object> listB = new ArrayList<>();
        listB.add(1);
        listB.add(2);
        listB.add(3);

        JXPathContext context = JXPathContext.newContext(new Object[]{listA, listB});

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Object result = evaluator.eval(expression, context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        listB.add(4);

        context = JXPathContext.newContext(new Object[]{listA, listB});

        result = evaluator.eval(expression, context);

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCompareIterators() {
        CoreOperationCompare expression = new CoreOperationCompare(
                new Variable("a"),
                new Variable("b")
        );

        IteratorSupport iterA = new IteratorSupport(new ArrayIterator(new Object[]{1, 2, 3}));
        IteratorSupport iterB = new IteratorSupport(new ArrayIterator(new Object[]{1, 2, 3}));

        JXPathContext context = JXPathContext.newContext(new Object[]{iterA, iterB});

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Object result = evaluator.eval(expression, context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        iterB = new IteratorSupport(new ArrayIterator(new Object[]{1, 2, 3, 4}));

        context = JXPathContext.newContext(new Object[]{iterA, iterB});

        result = evaluator.eval(expression, context);

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCompareMismatchedTypes() {
        CoreOperationCompare expression = new CoreOperationCompare(
                new Constant("1"),
                new Variable("a")
        );

        JXPathContext context = JXPathContext.newContext(new Object[]{"a"});

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Object result = evaluator.eval(expression, context);

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCompareNulls() {
        CoreOperationCompare expression = new CoreOperationCompare(
                new Variable("a"),
                new Constant(null)
        );

        JXPathContext context = JXPathContext.newContext(new Object[]{null});

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Object result = evaluator.eval(expression, context);

        assertTrue(result instanceof Boolean);
        assertTrue(!((Boolean) result).booleanValue());

        context = JXPathContext.newContext(new Object[]{"a"});

        result = evaluator.eval(expression, context);

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCompareWithSingleNodeIterator() {
        CoreOperationCompare expression = new CoreOperationCompare(
                new Variable("a"),
                new Variable("b")
        );

        IteratorSupport iterA = new IteratorSupport(new SingleNodeIterator(new SimpleNode("root", new Object[]{"a"})));
        IteratorSupport iterB = new IteratorSupport(new SingleNodeIterator(new SimpleNode("root", new Object[]{"b"})));

        JXPathContext context = JXPathContext.newContext(new Object[]{iterA, iterB});

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Object result = evaluator.eval(expression, context);

        assertTrue(result instanceof Boolean);
        assertTrue(!((Boolean) result).booleanValue());
    }

    @Test
    public void testCompareWithDifferentAxis() {
        CoreOperationCompare expression = new CoreOperationCompare(
                new Variable("a"),
                new Variable("b")
        );

        Axis axisA = new Axis(Axis.CHILD, null);
        Axis axisB = new Axis(Axis.DESCENDANT, null);

        JXPathContext context = JXPathContext.newContext(new Object[]{axisA, axisB});

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Object result = evaluator.eval(expression, context);

        assertFalse(result instanceof Boolean);
    }
}
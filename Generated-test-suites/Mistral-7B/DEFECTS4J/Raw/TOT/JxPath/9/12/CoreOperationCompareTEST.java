import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.IntContext;
import org.apache.commons.jxpath.ri.EvaluatedNode;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.jxpath.JXPathContext.newContext;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(5),
            new LiteralExpression(5)
        );

        IntContext context = newIntContext();
        EvaluatedNode result = compare.compute(context);

        Assert.assertTrue(result.getValue() instanceof Boolean);
        Assert.assertTrue((Boolean) result.getValue());
    }

    @Test
    public void testNotEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(5),
            new LiteralExpression(6)
        );

        IntContext context = newIntContext();
        EvaluatedNode result = compare.compute(context);

        Assert.assertTrue(result.getValue() instanceof Boolean);
        Assert.assertFalse((Boolean) result.getValue());
    }

    @Test
    public void testEqualCollections() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(new HashSet(Arrays.asList(1, 2))),
            new LiteralExpression(new HashSet(Arrays.asList(1, 2)))
        );

        IntContext context = newIntContext();
        EvaluatedNode result = compare.compute(context);

        Assert.assertTrue(result.getValue() instanceof Boolean);
        Assert.assertTrue((Boolean) result.getValue());
    }

    @Test
    public void testNotEqualCollections() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(new HashSet(Arrays.asList(1, 2))),
            new LiteralExpression(new HashSet(Arrays.asList(1, 3)))
        );

        IntContext context = newIntContext();
        EvaluatedNode result = compare.compute(context);

        Assert.assertTrue(result.getValue() instanceof Boolean);
        Assert.assertFalse((Boolean) result.getValue());
    }

    @Test
    public void testIteratorsEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new IteratorExpression(Arrays.asList(1, 2).iterator()),
            new IteratorExpression(Arrays.asList(1, 2).iterator())
        );

        IntContext context = newIntContext();
        EvaluatedNode result = compare.compute(context);

        Assert.assertTrue(result.getValue() instanceof Boolean);
        Assert.assertTrue((Boolean) result.getValue());
    }

    @Test
    public void testIteratorsNotEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new IteratorExpression(Arrays.asList(1, 2).iterator()),
            new IteratorExpression(Arrays.asList(1, 3).iterator())
        );

        IntContext context = newIntContext();
        EvaluatedNode result = compare.compute(context);

        Assert.assertTrue(result.getValue() instanceof Boolean);
        Assert.assertFalse((Boolean) result.getValue());
    }

    @Test
    public void testNullLeft() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(null),
            new LiteralExpression(5)
        );

        IntContext context = newIntContext();
        EvaluatedNode result = compare.compute(context);

        Assert.assertFalse(result.getValue() instanceof Boolean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRight() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(5),
            new LiteralExpression(null)
        );

        IntContext context = newIntContext();
        compare.compute(context);
    }

    private IntContext newIntContext() {
        return new JXPathContext(
            new org.apache.commons.jxpath.DocumentLocator(),
            null
        );
    }

}
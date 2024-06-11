import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for {@link CoreOperationCompare}
 */
public class CoreOperationCompareTest {

    /**
     * Test for comparing two {@link String}s
     */
    @Test
    public void testStringComparison() {
        Expression left = new StringLiteralExpression("Hello");
        Expression right = new StringLiteralExpression("Hello");

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.compile().evaluate(new ExpressionContext(), new JXPathContext()));

        left = new StringLiteralExpression("Hello");
        right = new StringLiteralExpression("World");

        compare = new CoreOperationCompare(left, right);
        assertFalse(compare.compile().evaluate(new ExpressionContext(), new JXPathContext()));
    }

    /**
     * Test for comparing two {@link Number}s
     */
    @Test
    public void testNumberComparison() {
        Expression left = new NumberLiteralExpression(1.0);
        Expression right = new NumberLiteralExpression(1.0);

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.compile().evaluate(new ExpressionContext(), new JXPathContext()));

        left = new NumberLiteralExpression(1.0);
        right = new NumberLiteralExpression(2.0);

        compare = new CoreOperationCompare(left, right);
        assertFalse(compare.compile().evaluate(new ExpressionContext(), new JXPathContext()));
    }

    /**
     * Test for comparing a {@link String} and a {@link Number}
     */
    @Test
    public void testComparisonStringNumber() {
        Expression left = new StringLiteralExpression("10");
        Expression right = new NumberLiteralExpression(10);

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.compile().evaluate(new ExpressionContext(), new JXPathContext()));
    }

    /**
     * Test for comparing two {@link Iterator}s
     */
    @Test
    public void testIteratorComparison() {
        List list1 = Arrays.asList(1, 2, 3);
        List list2 = Arrays.asList(1, 2, 3);

        Expression left = new ListIteratorExpression(list1.iterator());
        Expression right = new ListIteratorExpression(list2.iterator());

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.compile().evaluate(new ExpressionContext(), new JXPathContext()));

        list1 = Arrays.asList(1, 2, 3);
        list2 = Arrays.asList(1, 2, 4);

        compare = new CoreOperationCompare(left, right);
        assertFalse(compare.compile().evaluate(new ExpressionContext(), new JXPathContext()));
    }

    /**
     * Test for checking if an {@link Iterator} contains a certain value
     */
    @Test
    public void testIteratorContains() {
        List list = Arrays.asList(1, 2, 3);

        Expression iterator = new ListIteratorExpression(list.iterator());
        Expression value = new NumberLiteralExpression(2);

        CoreOperationCompare compare = new CoreOperationCompare(iterator, value);
        assertFalse(compare.compile().evaluate(new ExpressionContext(), new JXPathContext()));

        list = Arrays.asList(1, 2, 3);
        compare = new CoreOperationCompare(iterator, new NumberLiteralExpression(2));
        assertTrue(compare.compile().evaluate(new ExpressionContext(), new JXPathContext()));
    }

    /**
     * Test for checking if an empty {@link Iterator} matches any value
     */
    @Test
    public void testIteratorEmpty() {
        Expression iterator = new ListIteratorExpression(Collections.emptyIterator());
        Expression value = new NumberLiteralExpression(1);

        CoreOperationCompare compare = new CoreOperationCompare(iterator, value);
        assertFalse(compare.compile().evaluate(new ExpressionContext(), new JXPathContext()));
    }
}
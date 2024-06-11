import org.apache.commons.jxpath.JxpathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.iter.ArrayIter;
import org.apache.commons.jxpath.ri.iter.SingletonIter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test greater than relation.
     */
    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(
                new CoreExpressionNumber(5L),
                new CoreExpressionNumber(4L)
        );

        JxpathContext context = new JxpathContext();
        Object left = new Integer(5);
        Object right = new Integer(4);

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right));

        left = new Double(5.0);
        right = new Double(4.0);

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right));

        left = new Integer(5);
        right = new Integer(5);

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right));

        left = new Integer(5);
        right = new Double(4.5);

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right));

        left = new Double(5.0);
        right = new Integer(5);

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right));

        left = new Double(5.5);
        right = new Double(5.0);

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right));

        left = new SingletonIter(new Integer(5));
        right = new SingletonIter(new Integer(4));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());
    }

    /**
     * Test greater than or equal relation.
     */
    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionNumber(5L),
                new CoreExpressionNumber(4L)
        );

        JxpathContext context = new JxpathContext();
        Object left = new Integer(5);
        Object right = new Integer(4);

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right));

        left = new Integer(5);
        right = new Integer(5);

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right));

        left = new Integer(5);
        right = new Double(4.5);

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right));

        left = new Double(5.0);
        right = new Integer(5);

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right));

        left = new Double(5.5);
        right = new Double(5.0);

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right));

        left = new SingletonIter(new Integer(5));
        right = new SingletonIter(new Integer(4));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Integer(5));
        right = new SingletonIter(new Integer(5));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(5.0));
        right = new SingletonIter(new Double(5.0));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(5.5));
        right = new SingletonIter(new Double(5.0));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Integer(5));
        right = new SingletonIter(new Double(5.5));

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(5.0));
        right = new SingletonIter(new Double(5.0));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(5.5));
        right = new SingletonIter(new Double(5.0));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());
    }

    /**
     * Test less than relation.
     */
    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expr = new CoreOperationLessThan(
                new CoreExpressionNumber(5L),
                new CoreExpressionNumber(4L)
        );

        JxpathContext context = new JxpathContext();
        Object left = new Integer(5);
        Object right = new Integer(4);

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right));

        left = new Integer(5);
        right = new Double(4.0);

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right));

        left = new Double(5.0);
        right = new Integer(4);

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right));

        left = new Double(5.0);
        right = new Double(4.0);

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right));

        left = new ArrayIter(new Object[]{1, 2, 3, 4, 5});
        right = new SingletonIter(new Integer(5));

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Integer(5));
        right = new ArrayIter(new Object[]{1, 2, 3, 4});

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(5.0));
        right = new SingletonIter(new Double(4.0));

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(4.5));
        right = new SingletonIter(new Double(5.0));

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Integer(5));
        right = new SingletonIter(new String("5"));

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new String("5"));
        right = new SingletonIter(new Integer(5));

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right).booleanValue());
    }

    /**
     * Test less than or equal relation.
     */
    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expr = new CoreOperationLessThanOrEqual(
                new CoreExpressionNumber(5L),
                new CoreExpressionNumber(4L)
        );

        JxpathContext context = new JxpathContext();
        Object left = new Integer(5);
        Object right = new Integer(4);

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right));

        left = new Integer(5);
        right = new Double(4.0);

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right));

        left = new Double(5.0);
        right = new Integer(4);

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right));

        left = new Double(5.0);
        right = new Double(4.0);

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right));

        left = new ArrayIter(new Object[]{1, 2, 3, 4, 5});
        right = new SingletonIter(new Integer(5));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Integer(5));
        right = new ArrayIter(new Object[]{1, 2, 3, 4});

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(5.0));
        right = new SingletonIter(new Double(4.0));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(5.5));
        right = new SingletonIter(new Double(5.0));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Integer(5));
        right = new SingletonIter(new SingletonIter(new Integer(5)));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(5.0));
        right = new SingletonIter(new Double(5.0));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(5.5));
        right = new SingletonIter(new Double(5.0));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Integer(5));
        right = new SingletonIter(new String("5"));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new String("5"));
        right = new SingletonIter(new Integer(5));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Integer(5));
        right = new SingletonIter(new Integer(5));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(5.0));
        right = new SingletonIter(new Double(5.0));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(5.5));
        right = new SingletonIter(new Double(5.0));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Integer(4));
        right = new SingletonIter(new Integer(5));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(4.5));
        right = new SingletonIter(new Integer(5));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Integer(5));
        right = new SingletonIter(new Integer(5));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(5.0));
        right = new SingletonIter(new Double(5.0));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new Double(5.5));
        right = new SingletonIter(new Double(5.0));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());
    }

    /**
     * Test contains.
     */
    @Test
    public void testContains() {
        CoreOperationRelationalExpression expr = new CoreOperationContains(
                new CoreExpressionVariable("elements"),
                new CoreExpressionNumber(5L)
        );

        JxpathContext context = new JxpathContext();
        Object left = new ArrayIter(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}).toArray());

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, new Integer(5)));

        left = new SingletonIter(new Integer(5));

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, new ArrayIter(new Object[]{1, 2, 3, 4})));

        left = new SingletonIter(new Integer(5));

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, new SingletonIter(new Integer(6))).booleanValue());

        left = new SingletonIter(new Integer(5));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, new SingletonIter(new Integer(5))).booleanValue());
    }

    /**
     * Test matches.
     */
    @Test
    public void testMatches() {
        CoreOperationRelationalExpression expr = new CoreOperationMatches(
                new CoreExpressionVariable("left"),
                new CoreExpressionVariable("right")
        );

        JxpathContext context = new JxpathContext();

        Object left = new SingletonIter(new String("apple"));
        Object right = new SingletonIter(new String("apple"));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new String("apple"));
        right = new SingletonIter(new String("orange"));

        assertEquals(Boolean.FALSE, expr.computeValue(context, left, right).booleanValue());

        left = new SingletonIter(new String("apple"));
        right = new SingletonIter(new SingletonIter(new String("apple")));

        assertEquals(Boolean.TRUE, expr.computeValue(context, left, right).booleanValue());
    }
}
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object left = "foo";
        Object right = "foo";
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}), null);
        assertTrue(new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right)).execute(context));

        left = new Integer(1);
        right = new Integer(1);
        assertTrue(new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right)).execute(context));

        left = new Boolean(true);
        right = new Boolean(true);
        assertTrue(new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right)).execute(context));

        left = new String("foo");
        right = new String("foo");
        assertTrue(new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right)).execute(context));

        left = new Pointer("foo");
        right = new Pointer("foo");
        assertTrue(new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right)).execute(context));

        left = new HashSet<Object>();
        left.add("foo");
        right = new HashSet<Object>();
        right.add("foo");
        assertTrue(new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right)).execute(context));

        left = new List<Object>() {{
            add("foo");
        }};
        right = new List<Object>() {{
            add("foo");
        }};
        assertTrue(new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right)).execute(context));

        left = new String("foo");
        right = new String("bar");
        assertFalse(new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right)).execute(context));

        left = new Integer(1);
        right = new Integer(2);
        assertFalse(new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right)).execute(context));

        left = new Boolean(true);
        right = new Boolean(false);
        assertFalse(new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right)).execute(context));

        left = new String("foo");
        right = null;
        assertFalse(new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right)).execute(context));

        left = null;
        right = new String("foo");
        assertFalse(new CoreOperationCompare(new ConstantExpression(left), new ConstantExpression(right)).execute(context));
    }

    @Test
    public void testContains() {
        Object value = "foo";
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}), null);

        Iterator iter = Collections.singletonList(value).iterator();
        assertTrue(new CoreOperationCompare(iter, new ConstantExpression(value)).execute(context));

        iter = Collections.singletonList("bar").iterator();
        assertFalse(new CoreOperationCompare(iter, new ConstantExpression(value)).execute(context));

        iter = new HashSet<Object>() {{
            add("foo");
            add("bar");
        }}.iterator();
        assertTrue(new CoreOperationCompare(iter, new ConstantExpression(value)).execute(context));

        iter = new HashSet<Object>() {{
            add("bar");
        }}.iterator();
        assertFalse(new CoreOperationCompare(iter, new ConstantExpression(value)).execute(context));
    }

    @Test
    public void testFindMatch() {
        Object value = "foo";
        EvalContext context = new EvalContext(new InitialContext(new Object[]{}), null);

        Iterator leftIter = Collections.singletonList(value).iterator();
        Iterator rightIter = Collections.singletonList("foo").iterator();
        assertTrue(new CoreOperationCompare(leftIter, rightIter).execute(context));

        leftIter = Collections.singletonList("bar").iterator();
        rightIter = Collections.singletonList("foo").iterator();
        assertFalse(new CoreOperationCompare(leftIter, rightIter).execute(context));

        leftIter = new HashSet<Object>() {{
            add("foo");
            add("bar");
        }}.iterator();
        rightIter = Collections.singletonList("foo").iterator();
        assertTrue(new CoreOperationCompare(leftIter, rightIter).execute(context));

        leftIter = new HashSet<Object>() {{
            add("bar");
        }}.iterator();
        rightIter = Collections.singletonList("foo").iterator();
        assertFalse(new CoreOperationCompare(leftIter, rightIter).execute(context));

        leftIter = new HashSet<Object>() {{
            add("foo");
            add("foo");
        }}.iterator();
        rightIter = Collections.singletonList("foo").iterator();
        assertTrue(new CoreOperationCompare(leftIter, rightIter).execute(context));

        leftIter = new HashSet<Object>() {{
            add("foo");
            add("foo");
        }}.iterator();
        rightIter = Collections.singletonList("bar").iterator();
        assertFalse(new CoreOperationCompare(leftIter, rightIter).execute(context));
    }
}
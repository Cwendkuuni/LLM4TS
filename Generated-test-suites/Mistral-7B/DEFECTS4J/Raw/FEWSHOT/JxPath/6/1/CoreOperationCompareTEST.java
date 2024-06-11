package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext(new Object[] { new Object() {}, new Object() {} });

        CoreOperationCompare cop = new CoreOperationCompare(
            new LiteralExpression(new Object() {}),
            new LiteralExpression(new Object() {})
        );

        assertTrue(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression("foo"),
            new LiteralExpression("foo")
        );

        assertTrue(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression(1),
            new LiteralExpression(1)
        );

        assertTrue(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression("true"),
            new LiteralExpression("true")
        );

        assertTrue(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression(1.0),
            new LiteralExpression(1.0)
        );

        assertTrue(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new VariableExpression("x"),
            new VariableExpression("x")
        );

        EvalContext context2 = new EvalContext(new Object[] { new Object() {} });
        context2.setVariable("x", new Object() {});

        assertTrue(cop.evaluate(context2));

        cop = new CoreOperationCompare(
            new LiteralExpression(new byte[0]),
            new LiteralExpression(new byte[0])
        );

        assertTrue(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression(null),
            new LiteralExpression(null)
        );

        assertTrue(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression(""),
            new LiteralExpression("")
        );

        assertTrue(cop.evaluate(context));

        // testing with different types, false
        cop = new CoreOperationCompare(
            new LiteralExpression(new Object() {}),
            new LiteralExpression("foo")
        );

        assertFalse(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression(1),
            new LiteralExpression("foo")
        );

        assertFalse(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression("foo"),
            new LiteralExpression(1)
        );

        assertFalse(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression("foo"),
            new LiteralExpression("bar")
        );

        assertFalse(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression(1.0),
            new LiteralExpression("foo")
        );

        assertFalse(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression("true"),
            new LiteralExpression(false)
        );

        assertFalse(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression(1),
            new LiteralExpression(2)
        );

        assertFalse(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression(1.1),
            new LiteralExpression(1.0)
        );

        assertFalse(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression("foo"),
            new LiteralExpression("bar")
        );

        assertFalse(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression(1),
            new LiteralExpression("1")
        );

        assertTrue(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression("1"),
            new LiteralExpression(1)
        );

        assertTrue(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression("1"),
            new LiteralExpression("1.0")
        );

        assertTrue(cop.evaluate(context));

        cop = new CoreOperationCompare(
            new LiteralExpression("1.0"),
            new LiteralExpression("1")
        );

        assertTrue(cop.evaluate(context));
    }

    @Test
    public void testContains() {
        CoreOperationCompare cop = new CoreOperationCompare(
            new LiteralExpression(Arrays.asList(1, 2, 3)),
            new LiteralExpression(1)
        );

        assertTrue(cop.evaluate(new EvalContext(new Object[] {})));

        cop = new CoreOperationCompare(
            new LiteralExpression(Arrays.asList(1, 2, 3)),
            new LiteralExpression(2)
        );

        assertFalse(cop.evaluate(new EvalContext(new Object[] {})));

        cop = new CoreOperationCompare(
            new LiteralExpression(Arrays.asList(1, 2, 3)),
            new LiteralExpression(4)
        );

        assertFalse(cop.evaluate(new EvalContext(new Object[] {})));

        cop = new CoreOperationCompare(
            new LiteralExpression(Arrays.asList(1, 2, 3)),
            new VariableExpression("x")
        );

        EvalContext context = new EvalContext(new Object[] { new Object() {}});
        context.setVariable("x", 1);

        assertTrue(cop.evaluate(context));

        cop.evaluate(context);
        context.setVariable("x", 2);

        assertFalse(cop.evaluate(context));

        cop.evaluate(context);
        context.setVariable("x", 3);

        assertTrue(cop.evaluate(context));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare cop = new CoreOperationCompare(
            new LiteralExpression(Arrays.asList(1, 2, 3)),
            new LiteralExpression(Arrays.asList(1, 2, 3, 4))
        );

        assertTrue(cop.evaluate(new EvalContext(new Object[] {})));

        cop = new CoreOperationCompare(
            new LiteralExpression(Arrays.asList(1, 2, 3)),
            new LiteralExpression(Arrays.asList(1, 4, 3, 2))
        );

        assertFalse(cop.evaluate(new EvalContext(new Object[] {})));

        cop = new CoreOperationCompare(
            new LiteralExpression(Arrays.asList(1, 2, 3)),
            new LiteralExpression(Arrays.asList(2, 3, 1, 4))
        );

        assertFalse(cop.evaluate(new EvalContext(new Object[] {})));

        cop = new CoreOperationCompare(
            new LiteralExpression(Arrays.asList(1, 2, 3)),
            new LiteralExpression(Arrays.asList(1, 2, 3, 1, 2, 3))
        );

        assertTrue(cop.evaluate(new EvalContext(new Object[] {})));
    }

    @Test
    public void testIterators() {
        Object[] values = { new Object() {}, new Object() {}, new Object() {}, new Object() {} };

        CoreOperationCompare cop = new CoreOperationCompare(
            new LiteralExpression(Arrays.asList(values)),
            new LiteralExpression(Arrays.asList(values))
        );

        assertTrue(cop.evaluate(new EvalContext(new Object[] {})));

        cop = new CoreOperationCompare(
            new LiteralExpression(Arrays.asList(values)),
            new LiteralExpression(Arrays.asList(Collections.singletonList(values[0])))
        );

        assertFalse(cop.evaluate(new EvalContext(new Object[] {})));

        cop = new CoreOperationCompare(
            new LiteralExpression(Arrays.asList(values)),
            new LiteralExpression(Arrays.asList(Collections.singletonList(values[1])))
        );

        assertFalse(cop.evaluate(new EvalContext(new Object[] {})));

        cop = new CoreOperationCompare(
            new LiteralExpression(Arrays.asList(values)),
            new LiteralExpression(Arrays.asList(values[0], values[1]))
        );

        assertTrue(cop.evaluate(new EvalContext(new Object[] {})));

        cop = new CoreOperationCompare(
            new LiteralExpression(Arrays.asList(values)),
            new LiteralExpression(Arrays.asList(values[1], values[0]))
        );

        assertFalse(cop.evaluate(new EvalContext(new Object[] {})));
    }

    @Test
    public void testPointers() {
        Object value = new Object();
        Pointer pointer = new Pointer(value);

        CoreOperationCompare cop = new CoreOperationCompare(
            new LiteralExpression(pointer),
            new LiteralExpression(pointer)
        );

        assertTrue(cop.evaluate(new EvalContext(new Object[] {})));

        cop = new CoreOperationCompare(
            new LiteralExpression(pointer),
            new LiteralExpression(new Pointer(new Object()))
        );

        assertFalse(cop.evaluate(new EvalContext(new Object[] {})));
    }

    @Test
    public void testDifferentTypes() {
        Object value1 = new Object();
        Object value2 = new Integer(1);

        CoreOperationCompare cop = new CoreOperationCompare(
            new LiteralExpression(value1),
            new LiteralExpression(value2)
        );

        assertFalse(cop.evaluate(new EvalContext(new Object[] {})));
    }
}
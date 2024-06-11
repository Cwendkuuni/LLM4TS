package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.EvaluationException;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.test.AbstractTest;
import org.apache.commons.jxpath.ri.test.ExpressionTestUtil;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link CoreOperationCompare}.
 */
public class CoreOperationCompareTest extends AbstractTest {

    private CoreOperationCompare op;

    @Before
    public void setUp() throws Exception {
        op = new CoreOperationCompare(
            new VariableExpression("$left"),
            new VariableExpression("$right")
        );
    }

    @Test
    public void testComparisonWithBooleanValues() {
        ExpressionContext ctx = new ExpressionContext(new JXPathContext(new Object[] { true, false }));
        Object result = op.compute(ctx);
        assertFalse(InfoSetUtil.booleanValue(result));

        ctx = new ExpressionContext(new JXPathContext(new Object[] { false, true }));
        result = op.compute(ctx);
        assertFalse(InfoSetUtil.booleanValue(result));

        ctx = new ExpressionContext(new JXPathContext(new Object[] { true, true }));
        result = op.compute(ctx);
        assertTrue(InfoSetUtil.booleanValue(result));
    }

    @Test
    public void testComparisonWithNumberValues() {
        ExpressionContext ctx = new ExpressionContext(new JXPathContext(new Object[] { 1.0d, 2.0d }));
        Object result = op.compute(ctx);
        assertFalse(InfoSetUtil.booleanValue(result));

        ctx = new ExpressionContext(new JXPathContext(new Object[] { 1.0d, 1.0d }));
        result = op.compute(ctx);
        assertTrue(InfoSetUtil.booleanValue(result));
    }

    @Test
    public void testComparisonWithStringValues() {
        ExpressionContext ctx = new ExpressionContext(new JXPathContext(new Object[] { "foo", "bar" }));
        Object result = op.compute(ctx);
        assertFalse(InfoSetUtil.booleanValue(result));

        ctx = new ExpressionContext(new JXPathContext(new Object[] { "foo", "foo" }));
        result = op.compute(ctx);
        assertTrue(InfoSetUtil.booleanValue(result));
    }

    @Test
    public void testComparisonWithNullValues() {
        ExpressionContext ctx = new ExpressionContext(new JXPathContext(new Object[] { null, null }));
        Object result = op.compute(ctx);
        assertTrue(InfoSetUtil.booleanValue(result));

        ctx = new ExpressionContext(new JXPathContext(new Object[] { null, new Integer(1) }));
        result = op.compute(ctx);
        assertFalse(InfoSetUtil.booleanValue(result));
    }

    @Test(expected = EvaluationException.class)
    public void testComparisonWithInvalidTypes() {
        ExpressionContext ctx = new ExpressionContext(new JXPathContext(new Object[] { new Object(), new Integer(1) }));
        op.compute(ctx);
    }

    @Test
    public void testComparisonWithCollections() {
        ExpressionContext ctx = new ExpressionContext(new JXPathContext(new Object[] {
            Arrays.asList(1, 2, 3),
            Arrays.asList(2, 3, 4)
        }));
        Object result = op.compute(ctx);
        assertFalse(InfoSetUtil.booleanValue(result));

        ctx = new ExpressionContext(new JXPathContext(new Object[] {
            Arrays.asList(1, 2, 3),
            Arrays.asList(1, 2, 3)
        }));
        result = op.compute(ctx);
        assertTrue(InfoSetUtil.booleanValue(result));
    }

    @Test
    public void testComparisonWithIterators() {
        ExpressionContext ctx = new ExpressionContext(new JXPathContext(new Object[] {
            TestUtils.createList(1, 2, 3).iterator(),
            TestUtils.createList(2, 3, 4).iterator()
        }));
        Object result = op.compute(ctx);
        assertFalse(InfoSetUtil.booleanValue(result));

        ctx = new ExpressionContext(new JXPathContext(new Object[] {
            TestUtils.createList(1, 2, 3).iterator(),
            TestUtils.createList(1, 2, 3).iterator()
        }));
        result = op.compute(ctx);
        assertTrue(InfoSetUtil.booleanValue(result));
    }

    @Test
    public void testComparisonWithPointers() {
        ExpressionContext ctx = new ExpressionContext(new JXPathContext(new Object[] {
            new Pointer(new Object(), new InitialContext(), "/a/b"),
            new Pointer(new Object(), new InitialContext(), "/a/b")
        }));
        Object result = op.compute(ctx);
        assertTrue(InfoSetUtil.booleanValue(result));

        ctx = new ExpressionContext(new JXPathContext(new Object[] {
            new Pointer(new Object(), new InitialContext(), "/a/b"),
            new Pointer(new Object(), new InitialContext(), "/a/c")
        }));
        result = op.compute(ctx);
        assertFalse(InfoSetUtil.booleanValue(result));
    }

    @Test
    public void testComparisonWithEmptyCollections() {
        ExpressionContext ctx = new ExpressionContext(new JXPathContext(new Object[] {
            Collections.emptyList().iterator(),
            Collections.emptyList().iterator()
        }));
        Object result = op.compute(ctx);
        assertTrue(InfoSetUtil.booleanValue(result));
    }
}
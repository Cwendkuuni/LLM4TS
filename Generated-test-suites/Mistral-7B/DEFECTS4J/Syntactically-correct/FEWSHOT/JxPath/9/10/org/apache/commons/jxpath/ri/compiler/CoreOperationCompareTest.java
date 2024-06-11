package org.apache.commons.jxpath.ri.compiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.RIEngine;
import org.apache.commons.jxpath.RIExpression;
import org.apache.commons.jxpath.RIEvalContext;
import org.apache.commons.jxpath.RIUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        RIEngine engine = new RIEngine();

        RIExpression leftExp = engine.compile("1");
        RIExpression rightExp = engine.compile("1");

        RIEvalContext context = new RIEvalContext();

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("true");
        rightExp = engine.compile("true");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("1.0");
        rightExp = engine.compile("1.0");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("'foo'");
        rightExp = engine.compile("'foo'");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("'foo'");
        rightExp = engine.compile("'bar'");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("1");
        rightExp = engine.compile("2");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("true");
        rightExp = engine.compile("false");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("1.0");
        rightExp = engine.compile("2.0");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("null");
        rightExp = engine.compile("null");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("null");
        rightExp = engine.compile("1");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("1");
        rightExp = engine.compile("null");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("1");
        rightExp = engine.compile("[]");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("[]");
        rightExp = engine.compile("1");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("[]");
        rightExp = engine.compile("[]");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));
    }

    @Test
    public void testContains() {
        RIEngine engine = new RIEngine();

        RIExpression leftExp = engine.compile("(1, 2, 3)");
        RIExpression rightExp = engine.compile("2");

        RIEvalContext context = new RIEvalContext();

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, 3)");
        rightExp = engine.compile("4");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, 3)");
        rightExp = engine.compile("(1, 2, 3, 4)");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, 3)");
        rightExp = engine.compile("(4, 2, 3)");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, 3)");
        rightExp = engine.compile("(1, 2, 3, 1, 2, 3)");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, 3)");
        rightExp = engine.compile("(1, 2)");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));
    }

    @Test
    public void testFindMatch() {
        RIEngine engine = new RIEngine();

        RIExpression leftExp = engine.compile("(1, 2, 3)");
        RIExpression rightExp = engine.compile("(1, 2, 3, 4)");

        RIEvalContext context = new RIEvalContext();

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, 3)");
        rightExp = engine.compile("(1, 2, 5, 3)");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, 3)");
        rightExp = engine.compile("(1, 1, 2, 2, 3)");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, 3)");
        rightExp = engine.compile("(1, 2, 3, 1, 2, 3, 1, 2, 3)");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, 3)");
        rightExp = engine.compile("(1, 2, 3, 1, 2, 4)");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, 3)");
        rightExp = engine.compile("(1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 4, 1, 2, 3)");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));
    }

    @Test
    public void testEqualWithIterators() {
        RIEngine engine = new RIEngine();

        RIExpression leftExp = engine.compile("(1, 2, [3, 4, 5])");
        RIExpression rightExp = engine.compile("(1, 2, [3, 4, 5])");

        RIEvalContext context = new RIEvalContext();

        Iterator leftIt = (Iterator) leftExp.evaluate(context);
        Iterator rightIt = (Iterator) rightExp.evaluate(context);

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("(1, 2, [3, 4, 6])");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("(1, 2, [3, 4, 5, 6])");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("(1, 2, [3, 4, 5, 6, 7])");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("(1, 2, [3, 4, 5, 6, 7, 8])");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));
    }

    @Test
    public void testContainsWithIterators() {
        RIEngine engine = new RIEngine();

        RIExpression leftExp = engine.compile("(1, 2, [3, 4, 5])");
        RIExpression rightExp = engine.compile("3");

        RIEvalContext context = new RIEvalContext();

        Iterator leftIt = (Iterator) leftExp.evaluate(context);
        Object right = rightExp.evaluate(context);

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("4");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("5");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("6");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("[3, 4, 5]");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("[3, 4, 6]");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("[3, 4, 5, 6]");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));
    }

    @Test
    public void testFindMatchWithIterators() {
        RIEngine engine = new RIEngine();

        Collection left = new ArrayList<>();
        left.add(1);
        left.add(2);
        left.add(3);

        RIExpression leftExp = engine.compile("(1, 2, [3, 4, 5])");
        RIExpression rightExp = engine.compile("(1, 2, [3, 4, 5])");

        RIEvalContext context = new RIEvalContext();

        Iterator leftIt = left.iterator();
        Iterator rightIt = (Iterator) rightExp.evaluate(context);

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("(1, 2, [3, 4, 6])");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("(1, 2, [3, 4, 5, 6])");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("(1, 2, [3, 4, 5, 6, 7])");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("(1, 2, [3, 4, 5, 6, 7, 8])");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("(1, 2, [3, 4, 5])");
        rightExp = engine.compile("(1, 2, [3, 4, 5, 6, 7, 8, 9])");

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));
    }

    @Test
    public void testEqualWithPointers() {
        RIEngine engine = new RIEngine();

        RIExpression leftExp = engine.compile("$[1]");
        RIExpression rightExp = engine.compile("$[1]");

        RIEvalContext context = new RIEvalContext();

        Pointer left = (Pointer) leftExp.evaluate(context);
        Pointer right = (Pointer) rightExp.evaluate(context);

        assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("$[1]");
        rightExp = engine.compile("$[2]");

        assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));

        leftExp = engine.compile("$[1]");
        rightExp = engine.compile("$[1]");

        if (left.getNode().getNodeType() == Node.ELEMENT_NODE &&
            right.getNode().getNodeType() == Node.ELEMENT_NODE) {
            assertTrue(new CoreOperationCompare(leftExp, rightExp).evaluate(context));
        } else {
            assertFalse(new CoreOperationCompare(leftExp, rightExp).evaluate(context));
        }
    }

}
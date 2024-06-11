package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object l = new Integer(1);
        Object r = new Integer(1);
        assertTrue(new CoreOperationCompare(new Const(l), new Const(r)).evaluate(new EvalContext(new Object()[0])));

        l = new Double(1.0);
        r = new Double(1.0);
        assertTrue(new CoreOperationCompare(new Const(l), new Const(r)).evaluate(new EvalContext(new Object()[0])));

        l = "hello";
        r = "hello";
        assertTrue(new CoreOperationCompare(new Const(l), new Const(r)).evaluate(new EvalContext(new Object()[0])));

        l = new Boolean(true);
        r = new Boolean(true);
        assertTrue(new CoreOperationCompare(new Const(l), new Const(r)).evaluate(new EvalContext(new Object()[0])));

        l = new InitialContext();
        r = new InitialContext();
        assertTrue(new CoreOperationCompare(new Const(l), new Const(r)).evaluate(new EvalContext(new Object()[0])));

        l = new SelfContext();
        r = new SelfContext();
        assertTrue(new CoreOperationCompare(new Const(l), new Const(r)).evaluate(new EvalContext(new Object()[0])));

        l = new HashSet<Object>() {{
            add(new Integer(1));
            add(new Integer(2));
        }};
        r = new HashSet<Object>() {{
            add(new Integer(1));
            add(new Integer(2));
        }};
        assertTrue(new CoreOperationCompare(new Const(l), new Const(r)).evaluate(new EvalContext(new Object()[0])));

        // Test for null cases
        l = null;
        r = null;
        assertTrue(new CoreOperationCompare(new Const(l), new Const(r)).evaluate(new EvalContext(new Object()[0])));

        l = new Integer(1);
        r = null;
        assertFalse(new CoreOperationCompare(new Const(l), new Const(r)).evaluate(new EvalContext(new Object()[0])));

        l = null;
        r = new Integer(1);
        assertFalse(new CoreOperationCompare(new Const(l), new Const(r)).evaluate(new EvalContext(new Object()[0])));
    }

    @Test
    public void testContains() {
        List<Object> list = new ArrayList<Object>() {{
            add(new Integer(1));
            add(new Integer(2));
            add(new Integer(3));
        }};

        Iterator<Object> iterator = list.iterator();

        Object value = new Integer(1);
        assertTrue(new CoreOperationCompare(iterator, new Const(value)).evaluate(new EvalContext(new Object()[0])));

        value = new Integer(3);
        assertFalse(new CoreOperationCompare(iterator, new Const(value)).evaluate(new EvalContext(new Object()[0])));
    }

    @Test
    public void testFindMatch() {
        List<Object> left = new ArrayList<Object>() {{
            add(new Integer(1));
            add(new Integer(2));
            add(new Integer(3));
        }};

        List<Object> right = new ArrayList<Object>() {{
            add(new Integer(1));
            add(new Integer(1));
            add(new Integer(3));
        }};

        Iterator<Object> lit = left.iterator();
        Iterator<Object> rit = right.iterator();

        assertTrue(new CoreOperationCompare(lit, rit).evaluate(new EvalContext(new Object[0])));

        left = new ArrayList<Object>() {{
            add(new Integer(1));
            add(new Integer(2));
            add(new Integer(3));
        }};

        right = new ArrayList<Object>() {{
            add(new Integer(3));
            add(new Integer(1));
            add(new Integer(1));
        }};

        lit = left.iterator();
        rit = right.iterator();

        assertFalse(new CoreOperationCompare(lit, rit).evaluate(new EvalContext(new Object[0])));
    }
}
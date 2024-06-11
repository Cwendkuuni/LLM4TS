package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Test class for CoreOperationCompare.
 */
public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object l = new Integer(1);
        Object r = new Integer(1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqual() {
        Object l = new Integer(1);
        Object r = new Integer(2);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualString() {
        Object l = "foo";
        Object r = "foo";
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualString() {
        Object l = "foo";
        Object r = "bar";
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualBoolean() {
        Object l = true;
        Object r = true;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualBoolean() {
        Object l = true;
        Object r = false;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualNull() {
        Object l = null;
        Object r = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualNullAndNumber() {
        Object l = null;
        Object r = new Integer(1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualNullAndString() {
        Object l = null;
        Object r = "foo";
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualNumberAndString() {
        Object l = new Integer(1);
        Object r = "foo";
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualCollectionIterator() {
        List list = new ArrayList();
        list.add(new Integer(1));
        list.add(new Integer(2));

        Iterator it = list.iterator();
        Object r = new Integer(1);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(it), new Constant(r));
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualCollectionIterator() {
        List list = new ArrayList();
        list.add(new Integer(1));
        list.add(new Integer(2));

        Iterator it = list.iterator();
        Object r = new Integer(3);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(it), new Constant(r));
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualHashSet() {
        HashSet set = new HashSet();
        set.add(new Integer(1));
        set.add(new Integer(2));

        Object l = set.iterator();
        Object r = new Integer(1);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualHashSet() {
        HashSet set = new HashSet();
        set.add(new Integer(1));
        set.add(new Integer(2));

        Object l = set.iterator();
        Object r = new Integer(3);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(l), new Constant(r));
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

}
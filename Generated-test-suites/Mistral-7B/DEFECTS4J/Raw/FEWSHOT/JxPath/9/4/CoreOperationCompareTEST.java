package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object l = "foo";
        Object r = "foo";
        EvalContext context = new EvalContext(new Object[0], new InitialContext());
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = 1.0;
        r = 1.0;
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = true;
        r = true;
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = new Integer(1);
        r = new Integer(1);
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = new Double(1.0);
        r = new Double(1.0);
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = new String("foo");
        r = new String("foo");
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = new Pointer(new Object[0], new SelfContext());
        r = new Pointer(new Object[0], new SelfContext());
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = new Pointer(new Object[0], new InitialContext());
        r = new Pointer(new Object[0], new InitialContext());
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = new Pointer(new Object[0], new SelfContext());
        r = new Pointer(new Object[0], new InitialContext());
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = new Pointer(new Object[0], new InitialContext());
        r = new Pointer(new Object[0], new SelfContext());
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = null;
        r = null;
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = new Object();
        r = new Object();
        assertTrue(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = "foo";
        r = 1;
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = 1;
        r = "foo";
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = "foo";
        r = null;
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = null;
        r = "foo";
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = new Integer(1);
        r = new Integer(2);
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));

        l = new Double(1.0);
        r = new Double(2.0);
        assertFalse(new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r)).evaluate(context));
    }

    @Test
    public void testContains() {
        Object[] array = new Object[0];
        Iterator it = new HashSet(array).iterator();

        assertFalse(new CoreOperationCompare(new ConstantExpression(array), new ConstantExpression(new Integer(1))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(array), new ConstantExpression("foo")).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(array), new ConstantExpression(new Double(1.0))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(array), new ConstantExpression(new Boolean(true))).evaluate(new EvalContext(new Object[0], new InitialContext())));

        Object[] arrayWithOne = {new Integer(1)};
        Iterator it1 = new HashSet(arrayWithOne).iterator();
        assertTrue(new CoreOperationCompare(new ConstantExpression(arrayWithOne), new ConstantExpression(new Integer(1))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(arrayWithOne), new ConstantExpression(new Integer(1))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(arrayWithOne), new ConstantExpression("foo")).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(arrayWithOne), new ConstantExpression(new Double(1.0))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(arrayWithOne), new ConstantExpression(new Boolean(true))).evaluate(new EvalContext(new Object[0], new InitialContext())));

        Object[] arrayWithTwo = {new Integer(1), new Integer(2)};
        Iterator it2 = new HashSet(arrayWithTwo).iterator();
        assertTrue(new CoreOperationCompare(new ConstantExpression(arrayWithTwo), new ConstantExpression(new Integer(1))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(arrayWithTwo), new ConstantExpression(new Integer(2))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(arrayWithTwo), new ConstantExpression("foo")).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(arrayWithTwo), new ConstantExpression(new Double(1.0))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(arrayWithTwo), new ConstantExpression(new Boolean(true))).evaluate(new EvalContext(new Object[0], new InitialContext())));

        Object[] arrayWithThree = {new Integer(1), new Integer(2), new Integer(3)};
        Iterator it3 = new HashSet(arrayWithThree).iterator();
        assertTrue(new CoreOperationCompare(new ConstantExpression(arrayWithThree), new ConstantExpression(new Integer(1))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(arrayWithThree), new ConstantExpression(new Integer(2))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(arrayWithThree), new ConstantExpression(new Integer(3))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(arrayWithThree), new ConstantExpression("foo")).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(arrayWithThree), new ConstantExpression(new Double(1.0))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(arrayWithThree), new ConstantExpression(new Boolean(true))).evaluate(new EvalContext(new Object[0], new InitialContext())));

        Object[] arrayWithFour = {new Integer(1), new Integer(2), new Integer(3), new Integer(4)};
        Iterator it4 = new HashSet(arrayWithFour).iterator();
        assertTrue(new CoreOperationCompare(new ConstantExpression(arrayWithFour), new ConstantExpression(new Integer(1))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(arrayWithFour), new ConstantExpression(new Integer(2))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(arrayWithFour), new ConstantExpression(new Integer(3))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(arrayWithFour), new ConstantExpression(new Integer(4))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(arrayWithFour), new ConstantExpression("foo")).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(arrayWithFour), new ConstantExpression(new Double(1.0))).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(arrayWithFour), new ConstantExpression(new Boolean(true))).evaluate(new EvalContext(new Object[0], new InitialContext())));
    }

    @Test
    public void testFindMatch() {
        Object[] array = new Object[0];
        Iterator it = new HashSet(array).iterator();
        Object[] array1 = {new Integer(1)};
        Iterator it1 = new HashSet(array1).iterator();
        Object[] array2 = {new Integer(1), new Integer(2)};
        Iterator it2 = new HashSet(array2).iterator();
        Object[] array3 = {new Integer(1), new Integer(2), new Integer(3)};
        Iterator it3 = new HashSet(array3).iterator();
        Object[] array4 = {new Integer(1), new Integer(2), new Integer(3), new Integer(4)};
        Iterator it4 = new HashSet(array4).iterator();

        assertFalse(new CoreOperationCompare(new ConstantExpression(array), new ConstantExpression(array1)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(array1), new ConstantExpression(array)).evaluate(new EvalContext(new Object[0], new InitialContext())));

        assertFalse(new CoreOperationCompare(new ConstantExpression(array), new ConstantExpression(array2)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(array2), new ConstantExpression(array)).evaluate(new EvalContext(new Object[0], new InitialContext())));

        assertFalse(new CoreOperationCompare(new ConstantExpression(array), new ConstantExpression(array3)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(array3), new ConstantExpression(array)).evaluate(new EvalContext(new Object[0], new InitialContext())));

        assertFalse(new CoreOperationCompare(new ConstantExpression(array), new ConstantExpression(array4)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertFalse(new CoreOperationCompare(new ConstantExpression(array4), new ConstantExpression(array)).evaluate(new EvalContext(new Object[0], new InitialContext())));

        assertTrue(new CoreOperationCompare(new ConstantExpression(array1), new ConstantExpression(array)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(array), new ConstantExpression(array1)).evaluate(new EvalContext(new Object[0], new InitialContext())));

        assertTrue(new CoreOperationCompare(new ConstantExpression(array2), new ConstantExpression(array)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(array), new ConstantExpression(array2)).evaluate(new EvalContext(new Object[0], new InitialContext())));

        assertTrue(new CoreOperationCompare(new ConstantExpression(array3), new ConstantExpression(array)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(array), new ConstantExpression(array3)).evaluate(new EvalContext(new Object[0], new InitialContext())));

        assertTrue(new CoreOperationCompare(new ConstantExpression(array4), new ConstantExpression(array)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(array), new ConstantExpression(array4)).evaluate(new EvalContext(new Object[0], new InitialContext())));

        assertTrue(new CoreOperationCompare(new ConstantExpression(array1), new ConstantExpression(array2)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(array2), new ConstantExpression(array1)).evaluate(new EvalContext(new Object[0], new InitialContext())));

        assertTrue(new CoreOperationCompare(new ConstantExpression(array1), new ConstantExpression(array3)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(array3), new ConstantExpression(array1)).evaluate(new EvalContext(new Object[0], new InitialContext())));

        assertTrue(new CoreOperationCompare(new ConstantExpression(array1), new ConstantExpression(array4)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(array4), new ConstantExpression(array1)).evaluate(new EvalContext(new Object[0], new InitialContext())));

        assertTrue(new CoreOperationCompare(new ConstantExpression(array2), new ConstantExpression(array3)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(array3), new ConstantExpression(array2)).evaluate(new EvalContext(new Object[0], new InitialContext())));

        assertTrue(new CoreOperationCompare(new ConstantExpression(array2), new ConstantExpression(array4)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(array4), new ConstantExpression(array2)).evaluate(new EvalContext(new Object[0], new InitialContext())));

        assertTrue(new CoreOperationCompare(new ConstantExpression(array3), new ConstantExpression(array4)).evaluate(new EvalContext(new Object[0], new InitialContext())));
        assertTrue(new CoreOperationCompare(new ConstantExpression(array4), new ConstantExpression(array3)).evaluate(new EvalContext(new Object[0], new InitialContext())));
    }

    @Test
    public void testNullPointer() {
        EvalContext context = new EvalContext(new Object[0], new InitialContext());
        assertNull(new CoreOperationCompare(new ConstantExpression(null), new ConstantExpression(new Integer(1))).evaluate(context));
        assertNull(new CoreOperationCompare(new ConstantExpression(new Integer(1)), new ConstantExpression(null)).evaluate(context));
    }
}
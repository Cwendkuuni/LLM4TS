package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompareTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CoreOperationCompare.
 */
public class CoreOperationCompareTest {

    // Test data
    private EvalContext context;
    private CoreOperationCompare compare;
    private Expression arg1, arg2;

    @Before
    public void setUp() {
        arg1 = new LiteralExpression(new Integer(1));
        arg2 = new LiteralExpression(new Integer(2));
        compare = new CoreOperationCompare(arg1, arg2);
        context = new EvalContext(new InitialContext(), new SelfContext());
    }

    @Test
    public void testEquals() {
        // Basic test
        assertTrue(compare.equals(compare));

        // Test with null
        assertNull(compare.equals(null));

        // Test with different class
        assertFalse(compare.equals(new CoreOperationAdd(arg1, arg2)));
    }

    @Test
    public void testEqualMethod() {
        // Basic test
        assertFalse(compare.equal(context, arg1, arg2));

        // Test with null argument
        assertFalse(compare.equal(context, arg1, null));
        assertFalse(compare.equal(context, null, arg2));

        // Test with different types
        assertFalse(compare.equal(context, arg1, new LiteralExpression(new String("test"))));
        assertFalse(compare.equal(context, arg1, new LiteralExpression(new Boolean(true))));

        // Test with equal values
        assertTrue(compare.equal(context, arg1, arg1));

        // Test with equal collections
        Collection col1 = new ArrayList();
        col1.add(new Integer(1));
        Collection col2 = new ArrayList();
        col2.add(new Integer(1));
        assertTrue(compare.equal(context, new VariableExpression("x"), col1.iterator()));
        assertTrue(compare.equal(context, col1.iterator(), col2.iterator()));

        // Test with equal pointers
        Pointer p1 = new Pointer(new InitialContext(), "test");
        Pointer p2 = new Pointer(new InitialContext(), "test");
        assertTrue(compare.equal(p1, p2));

        // Test with equal strings
        String str1 = "test";
        String str2 = "test";
        assertTrue(compare.equal(InfoSetUtil.stringValue(arg1), InfoSetUtil.stringValue(arg2)));
        assertTrue(compare.equal(InfoSetUtil.stringValue(arg1), str1));
        assertTrue(compare.equal(str1, InfoSetUtil.stringValue(arg2)));
        assertTrue(compare.equal(str1, str2));

        // Test with equal numbers
        Number num1 = new Integer(1);
        Number num2 = new Integer(1);
        assertTrue(compare.equal(num1, num2));

        // Test with equal booleans
        Boolean bool1 = Boolean.TRUE;
        Boolean bool2 = Boolean.TRUE;
        assertTrue(compare.equal(bool1, bool2));
    }

    @Test
    public void testContains() {
        // Test with empty collection and null value
        Collection empty = new ArrayList();
        assertFalse(compare.contains(empty.iterator(), null));

        // Test with empty collection and non-null value
        assertFalse(compare.contains(empty.iterator(), new Integer(1)));

        // Test with non-empty collection and null value
        Collection col = new ArrayList();
        col.add(new Integer(1));
        assertFalse(compare.contains(col.iterator(), null));

        // Test with non-empty collection and non-null value
        Collection col1 = new ArrayList();
        col1.add(new Integer(1));
        Collection col2 = new ArrayList();
        col2.add(new Integer(1));
        assertTrue(compare.contains(col1.iterator(), col2.iterator()));
    }

    @Test
    public void testFindMatch() {
        // Test with empty collections
        Collection empty1 = new ArrayList();
        Collection empty2 = new ArrayList();
        assertFalse(compare.findMatch(empty1.iterator(), empty2.iterator()));

        // Test with non-empty collections
        Collection col1 = new ArrayList();
        col1.add(new Integer(1));
        Collection col2 = new ArrayList();
        col2.add(new Integer(1));
        col2.add(new Integer(2));
        assertTrue(compare.findMatch(col1.iterator(), col2.iterator()));

        // Test with non-matching collections
        Collection col3 = new ArrayList();
        col3.add(new Integer(1));
        Collection col4 = new ArrayList();
        col4.add(new Integer(2));
        assertFalse(compare.findMatch(col3.iterator(), col4.iterator()));
    }

    @Test
    public void testEqualWithIterators() {
        // Test with null iterators
        Iterator it1 = null;
        Iterator it2 = null;
        assertFalse(compare.equal(it1, it2));

        // Test with iterators of empty collections
        Collection col1 = new ArrayList();
        Collection col2 = new ArrayList();
        Iterator it3 = col1.iterator();
        Iterator it4 = col2.iterator();
        assertFalse(compare.equal(it3, it4));

        // Test with iterators of the same collection
        Collection col = new ArrayList();
        col.add(new Integer(1));
        Iterator it5 = col.iterator();
        Iterator it6 = col.iterator();
        assertTrue(compare.equal(it5, it6));

        // Test with iterators of different collections
        Collection col1_1 = new ArrayList();
        col1_1.add(new Integer(1));
        Collection col2_1 = new ArrayList();
        col2_1.add(new Integer(2));
        Iterator it7 = col1_1.iterator();
        Iterator it8 = col2_1.iterator();
        assertFalse(compare.equal(it7, it8));
    }
}
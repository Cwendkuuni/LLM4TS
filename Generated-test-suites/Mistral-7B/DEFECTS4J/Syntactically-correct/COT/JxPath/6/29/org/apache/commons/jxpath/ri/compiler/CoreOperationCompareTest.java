package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.CoreOperationCompareTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTestUtils;
import org.apache.commons.jxpath.ri.compiler.ExpressionTestUtils;
import org.apache.commons.jxpath.ri.compiler.StaticContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Test class for CoreOperationCompare.
 */
public class CoreOperationCompareTest extends TestCase {

    public CoreOperationCompareTest(String name) {
        super(name);
    }

    /**
     * Test method for 'CoreOperationCompare.CoreOperationCompare(Expression arg1, Expression arg2)'.
     */
    public void testCoreOperationCompare() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreOperationTestUtils.SimpleExpression("1"),
            new CoreOperationTestUtils.SimpleExpression("1")
        );
        ExpressionContext context = new StaticContext();
        assertTrue(coreOperationCompare.evaluateToBoolean(context, null, null));
    }

    /**
     * Test method for 'CoreOperationCompare.equal(EvalContext context, Expression left, Expression right)'.
     */
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreOperationTestUtils.SimpleExpression("1"),
            new CoreOperationTestUtils.SimpleExpression("1")
        );
        ExpressionContext context = new StaticContext();

        Object left = new Integer(1);
        Object right = new Integer(1);
        assertTrue(coreOperationCompare.equal(context, new CoreOperationTestUtils.SimpleExpression(left), new CoreOperationTestUtils.SimpleExpression(right)));

        left = new String("Test");
        right = new String("Test");
        assertTrue(coreOperationCompare.equal(context, new CoreOperationTestUtils.SimpleExpression(left), new CoreOperationTestUtils.SimpleExpression(right)));

        left = new Boolean(true);
        right = new Boolean(true);
        assertTrue(coreOperationCompare.equal(context, new CoreOperationTestUtils.SimpleExpression(left), new CoreOperationTestUtils.SimpleExpression(right)));

        left = new Pointer(new Object(), null);
        right = new Pointer(new Object(), null);
        assertTrue(coreOperationCompare.equal(context, new CoreOperationTestUtils.SimpleExpression(left), new CoreOperationTestUtils.SimpleExpression(right)));

        left = new ArrayList<>();
        right = new ArrayList<>();
        Iterator leftIterator = left.iterator();
        leftIterator.next();
        Object leftValue = leftIterator.next();
        Iterator rightIterator = right.iterator();
        rightIterator.next();
        Object rightValue = rightIterator.next();
        assertTrue(coreOperationCompare.equal(context, new CoreOperationTestUtils.SimpleExpression(left), new CoreOperationTestUtils.SimpleExpression(right)));
        Assert.assertEquals(leftValue, rightValue);

        left = new HashSet<>();
        left.add(leftValue);
        right = new HashSet<>();
        right.add(leftValue);
        assertTrue(coreOperationCompare.equal(context, new CoreOperationTestUtils.SimpleExpression(left), new CoreOperationTestUtils.SimpleExpression(right)));

        left = new String[]{"Test", "Test2"};
        right = new String[]{"Test", "Test2"};
        assertTrue(coreOperationCompare.equal(context, new CoreOperationTestUtils.SimpleExpression(left), new CoreOperationTestUtils.SimpleExpression(right)));

        left = new Map<String, Object>();
        left.put("TestKey", leftValue);
        right = new Map<String, Object>();
        right.put("TestKey", rightValue);
        assertTrue(coreOperationCompare.equal(context, new CoreOperationTestUtils.SimpleExpression(left), new CoreOperationTestUtils.SimpleExpression(right)));

        left = new JXPathContext(Collections.singletonList(new Object()));
        right = new JXPathContext(Collections.singletonList(new Object()));
        Pointer leftPointer = new Pointer(new Object(), null);
        leftPointer.setValue(leftValue);
        leftPointer.setContext(left);
        Pointer rightPointer = new Pointer(new Object(), null);
        rightPointer.setValue(rightValue);
        rightPointer.setContext(right);
        assertTrue(coreOperationCompare.equal(context, new CoreOperationTestUtils.SimpleExpression(leftPointer), new CoreOperationTestUtils.SimpleExpression(rightPointer)));

        left = new CoreOperationTestUtils.SimpleExpression(leftValue);
        right = new CoreOperationTestUtils.SimpleExpression(rightValue);
        assertTrue(coreOperationCompare.equal(context, left, right));

        left = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        right = new CoreOperationTestUtils.SimpleExpression(new Integer(2));
        assertFalse(coreOperationCompare.equal(context, left, right));

        left = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        right = new CoreOperationTestUtils.SimpleExpression(null);
        assertFalse(coreOperationCompare.equal(context, left, right));

        left = new CoreOperationTestUtils.SimpleExpression(null);
        right = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        assertFalse(coreOperationCompare.equal(context, left, right));

        left = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        right = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        left.setContext(context);
        right.setContext(context);
        assertFalse(coreOperationCompare.equal(context, left, right));

        left = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        right = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        left.setContext(context);
        right = null;
        assertFalse(coreOperationCompare.equal(context, left, right));

        left = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        right = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        left = null;
        right = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        assertFalse(coreOperationCompare.equal(context, left, right));
    }

    /**
     * Test method for 'CoreOperationCompare.contains(Iterator it, Object value)'.
     */
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreOperationTestUtils.SimpleExpression(new ArrayList<>()),
            new CoreOperationTestUtils.SimpleExpression(new Integer(1))
        );

        CoreOperationTestUtils.SimpleExpression arrayList = new CoreOperationTestUtils.SimpleExpression(new ArrayList<>());
        Iterator it = arrayList.iterator();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        assertFalse(coreOperationCompare.contains(it, new Integer(2)));
        assertTrue(coreOperationCompare.contains(it, new Integer(1)));
    }

    /**
     * Test method for 'CoreOperationCompare.findMatch(Iterator lit, Iterator rit)'.
     */
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreOperationTestUtils.SimpleExpression(new ArrayList<>()),
            new CoreOperationTestUtils.SimpleExpression(new ArrayList<>())
        );

        CoreOperationTestUtils.SimpleExpression leftList = new CoreOperationTestUtils.SimpleExpression(new ArrayList<>());
        CoreOperationTestUtils.SimpleExpression rightList = new CoreOperationTestUtils.SimpleExpression(new ArrayList<>());
        Iterator leftIterator = leftList.iterator();
        Iterator rightIterator = rightList.iterator();
        leftIterator.next();
        leftIterator.next();
        leftIterator.next();
        leftIterator.next();
        leftIterator.next();
        rightIterator.next();
        rightIterator.next();
        rightIterator.next();
        Object leftValue = leftIterator.next();
        Object rightValue = rightIterator.next();
        assertTrue(coreOperationCompare.findMatch(leftIterator, rightIterator));
        Assert.assertEquals(leftValue, rightValue);
    }

    /**
     * Test method for 'CoreOperationCompare.equal(Object l, Object r)'.
     */
    public void testEqualObject() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreOperationTestUtils.SimpleExpression(new Integer(1)),
            new CoreOperationTestUtils.SimpleExpression(new Integer(1))
        );

        Object left = new Integer(1);
        Object right = new Integer(1);
        assertTrue(coreOperationCompare.equal(left, right));

        left = new String("Test");
        right = new String("Test");
        assertTrue(coreOperationCompare.equal(left, right));

        left = new Boolean(true);
        right = new Boolean(true);
        assertTrue(coreOperationCompare.equal(left, right));

        left = new Pointer(new Object(), null);
        right = new Pointer(new Object(), null);
        assertTrue(coreOperationCompare.equal(left, right));

        left = new ArrayList<>();
        left.add(new Integer(1));
        right = new ArrayList<>();
        right.add(new Integer(1));
        assertTrue(coreOperationCompare.equal(left, right));

        left = new HashSet<>();
        left.add(new Integer(1));
        right = new HashSet<>();
        right.add(new Integer(1));
        assertTrue(coreOperationCompare.equal(left, right));

        left = new String[]{"Test", "Test2"};
        right = new String[]{"Test", "Test2"};
        assertTrue(coreOperationCompare.equal(left, right));

        left = new Map<String, Object>();
        left.put("TestKey", new Integer(1));
        right = new Map<String, Object>();
        right.put("TestKey", new Integer(1));
        assertTrue(coreOperationCompare.equal(left, right));

        left = new JXPathContext(Collections.singletonList(new Object()));
        leftPointer = new Pointer(new Object(), null);
        leftPointer.setValue(new Integer(1));
        leftPointer.setContext(left);
        right = new JXPathContext(Collections.singletonList(new Object()));
        rightPointer = new Pointer(new Object(), null);
        rightPointer.setValue(new Integer(1));
        rightPointer.setContext(right);
        assertTrue(coreOperationCompare.equal(leftPointer, rightPointer));

        left = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        right = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        assertTrue(coreOperationCompare.equal(left, right));

        left = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        right = new CoreOperationTestUtils.SimpleExpression(new Integer(2));
        assertFalse(coreOperationCompare.equal(left, right));

        left = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        right = new CoreOperationTestUtils.SimpleExpression(null);
        assertFalse(coreOperationCompare.equal(left, right));

        left = new CoreOperationTestUtils.SimpleExpression(null);
        right = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        assertFalse(coreOperationCompare.equal(left, right));

        left = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        right = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        left = ExpressionTestUtils.setContext(left, new StaticContext());
        right = ExpressionTestUtils.setContext(right, new StaticContext());
        assertFalse(coreOperationCompare.equal(left, right));

        left = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        right = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        left = null;
        right = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        assertFalse(coreOperationCompare.equal(left, right));

        left = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        right = new CoreOperationTestUtils.SimpleExpression(new Integer(1));
        left = new CoreOperationTestUtils.SimpleExpression(left, null);
        right = new CoreOperationTestUtils.SimpleExpression(right, null);
        assertFalse(coreOperationCompare.equal(left, right));

        left = new ArrayList<>();
        left.add(new Integer(1));
        right = new ArrayList<>();
        right.add(new Integer(2));
        assertFalse(coreOperationCompare.equal(left, right));

        left = new HashSet<>();
        left.add(new Integer(1));
        right = new HashSet<>();
        right.add(new Integer(2));
        assertFalse(coreOperationCompare.equal(left, right));

        left = new String[]{"Test", "Test2"};
        right = new String[]{"Test", "Test3"};
        assertFalse(coreOperationCompare.equal(left, right));

        left = new Map<String, Object>();
        left.put("TestKey", new Integer(1));
        right = new Map<String, Object>();
        right.put("TestKey", new Integer(2));
        assertFalse(coreOperationCompare.equal(left, right));

        left = new JXPathContext(Collections.singletonList(new Object()));
        leftPointer = new Pointer(new Object(), null);
        leftPointer.setValue(new Integer(1));
        leftPointer.setContext(left);
        right = new JXPathContext(Collections.singletonList(new Object()));
        rightPointer = new Pointer(new Object(), null);
        rightPointer.setValue(new Integer(2));
        rightPointer.setContext(right);
        assertFalse(coreOperationCompare.equal(leftPointer, rightPointer));
    }

}
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathTestCase;
import org.apache.commons.jxpath.xml.Document;
import org.apache.commons.jxpath.xml.DocumentContainer;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        JXPathContext context = JXPathContext.newContext(new Object());
        Object result = coreOp.computeValue(context);
        assertFalse((boolean) result);

        Object left = new Object();
        Object right = new Object();
        context.getContextBean().setValue("left", left);
        context.getContextBean().setValue("right", right);
        args[0] = new CoreOperationRelationalExpression(context.compileExpression("left")) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        args[1] = new CoreOperationRelationalExpression(context.compileExpression("right")) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        result = coreOp.computeValue(context);
        assertFalse((boolean) result);

        args[0] = new CoreOperationRelationalExpression(context.compileExpression("left")) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        args[1] = new CoreOperationRelationalExpression(context.compileExpression("right")) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        result = coreOp.computeValue(context);
        assertTrue((boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        int precedence = coreOp.getPrecedence();
        assertEquals(9, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean isSymmetric = coreOp.isSymmetric();
        assertFalse(isSymmetric);
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = coreOp.evaluateCompare(1);
        assertTrue(result);

        result = coreOp.evaluateCompare(0);
        assertFalse(result);

        result = coreOp.evaluateCompare(-1);
        assertFalse(result);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpressionTest coreOpRelExprTest = new CoreOperationRelationalExpressionTest();

        Object left = new Object();
        Object right = new Object();
        boolean result = coreOpRelExprTest.compute(left, right);
        assertFalse(result);

        ArrayList<Object> leftList = new ArrayList<>();
        ArrayList<Object> rightList = new ArrayList<>();
        leftList.add(left);
        rightList.add(right);
        leftList.add(right);

        result = coreOpRelExprTest.compute(leftList.iterator(), rightList.iterator());
        assertTrue(result);

        result = coreOpRelExprTest.compute(leftList.iterator(), right);
        assertTrue(result);
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpressionTest coreOpRelExprTest = new CoreOperationRelationalExpressionTest();

        Object o = new Object();
        Object result = coreOpRelExprTest.reduce(o);
        assertEquals(o, result);

        InitialContext ic = new InitialContext(new SelfContext(null));
        result = coreOpRelExprTest.reduce(ic);
        assertEquals(ic, result);

        Collection<Object> collection = new ArrayList<>();
        collection.add(o);
        result = coreOpRelExprTest.reduce(collection);
        Iterator<Object> iterator = ((Collection) result).iterator();
        assertTrue(iterator.hasNext());
        assertEquals(o, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpressionTest coreOpRelExprTest = new CoreOperationRelationalExpressionTest();

        Object value = new Object();
        ArrayList<Object> arrayList = new ArrayList<>();
        int numElements = 5;
        for (int i = 0; i < numElements; i++) {
            arrayList.add(value);
        }
        Iterator<Object> it = arrayList.iterator();

        boolean result = coreOpRelExprTest.containsMatch(it, value);
        assertTrue(result);

        result = coreOpRelExprTest.containsMatch(it, new Object());
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpressionTest coreOpRelExprTest = new CoreOperationRelationalExpressionTest();

        Object left = new Object();
        Object right = new Object();
        ArrayList<Object> leftList = new ArrayList<>();
        ArrayList<Object> rightList = new ArrayList<>();
        leftList.add(left);
        rightList.add(right);
        Iterator<Object> lit = leftList.iterator();
        Iterator<Object> rit = rightList.iterator();

        boolean result = coreOpRelExprTest.findMatch(lit, rit);
        assertFalse(result);

        leftList.add(right);
        lit = leftList.iterator();
        result = coreOpRelExprTest.findMatch(lit, rit);
        assertTrue(result);
    }

}
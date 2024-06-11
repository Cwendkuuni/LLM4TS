package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.*;
import org.apache.commons.jxpath.ri.compiler.*;
import org.apache.commons.jxpath.ri.axes.*;
import org.apache.commons.jxpath.ri.util.*;
import org.apache.commons.jxpath.ri.util.TestHelper;
import org.junit.Test;

import java.util.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression leftExpr = TestHelper.literalExpression(0);
        Expression rightExpr = TestHelper.literalExpression(0);
        CoreOperationCompare op = new CoreOperationCompare(leftExpr, rightExpr);
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertTrue(result);
    }

    @Test
    public void testEqual_false() {
        Expression leftExpr = TestHelper.literalExpression(0);
        Expression rightExpr = TestHelper.literalExpression(1);
        CoreOperationCompare op = new CoreOperationCompare(leftExpr, rightExpr);
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertFalse(result);
    }

    @Test
    public void testEqual_null() {
        Expression leftExpr = TestHelper.literalExpression(null);
        Expression rightExpr = TestHelper.literalExpression(null);
        CoreOperationCompare op = new CoreOperationCompare(leftExpr, rightExpr);
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertTrue(result);
    }

    @Test
    public void testEqual_null_notNull() {
        Expression leftExpr = TestHelper.literalExpression(null);
        Expression rightExpr = TestHelper.literalExpression("test");
        CoreOperationCompare op = new CoreOperationCompare(leftExpr, rightExpr);
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertFalse(result);
    }

    @Test
    public void testEqual_boolean() {
        Expression leftExpr = TestHelper.literalExpression(true);
        Expression rightExpr = TestHelper.literalExpression(true);
        CoreOperationCompare op = new CoreOperationCompare(leftExpr, rightExpr);
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertTrue(result);
    }

    @Test
    public void testEqual_boolean_false() {
        Expression leftExpr = TestHelper.literalExpression(true);
        Expression rightExpr = TestHelper.literalExpression(false);
        CoreOperationCompare op = new CoreOperationCompare(leftExpr, rightExpr);
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertFalse(result);
    }

    @Test
    public void testEqual_number() {
        Expression leftExpr = TestHelper.literalExpression(1.0);
        Expression rightExpr = TestHelper.literalExpression(1.0);
        CoreOperationCompare op = new CoreOperationCompare(leftExpr, rightExpr);
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertTrue(result);
    }

    @Test
    public void testEqual_number_notEqual() {
        Expression leftExpr = TestHelper.literalExpression(1.0);
        Expression rightExpr = TestHelper.literalExpression(2.0);
        CoreOperationCompare op = new CoreOperationCompare(leftExpr, rightExpr);
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertFalse(result);
    }

    @Test
    public void testEqual_string() {
        Expression leftExpr = TestHelper.literalExpression("test");
        Expression rightExpr = TestHelper.literalExpression("test");
        CoreOperationCompare op = new CoreOperationCompare(leftExpr, rightExpr);
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertTrue(result);
    }

    @Test
    public void testEqual_string_notEqual() {
        Expression leftExpr = TestHelper.literalExpression("test");
        Expression rightExpr = TestHelper.literalExpression("anotherTest");
        CoreOperationCompare op = new CoreOperationCompare(leftExpr, rightExpr);
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertFalse(result);
    }

    @Test
    public void testEqual_iterators() {
        Object[] data = new Object[]{1, 2, 3, 4, 5};
        List list = new ArrayList();
        list.addAll(Arrays.asList(data));
        Iterator leftIter = list.iterator();
        Iterator rightIter = list.iterator();
        CoreOperationCompare op = new CoreOperationCompare(
            TestHelper.iteratorExpression(leftIter),
            TestHelper.iteratorExpression(rightIter)
        );
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertTrue(result);
    }

    @Test
    public void testEqual_iterators_notMatching() {
        Object[] dataLeft = new Object[]{1, 2, 3, 4, 5};
        Object[] dataRight = new Object[]{1, 2, 3};
        List leftList = new ArrayList();
        List rightList = new ArrayList();
        leftList.addAll(Arrays.asList(dataLeft));
        rightList.addAll(Arrays.asList(dataRight));
        Iterator leftIter = leftList.iterator();
        Iterator rightIter = rightList.iterator();
        CoreOperationCompare op = new CoreOperationCompare(
            TestHelper.iteratorExpression(leftIter),
            TestHelper.iteratorExpression(rightIter)
        );
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertFalse(result);
    }

    @Test
    public void testEqual_pointers() {
        Object node = TestHelper.createTestNode("root", "test");
        Pointer leftPtr = TestHelper.newPointerExpression("//test/node()", ctx -> new SingleNodePointer(node));
        Pointer rightPtr = TestHelper.newPointerExpression("//test/node()", ctx -> new SingleNodePointer(node));
        CoreOperationCompare op = new CoreOperationCompare(leftPtr, rightPtr);
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertTrue(result);
    }

    @Test
    public void testEqual_pointers_notEqual() {
        Object node1 = TestHelper.createTestNode("root1", "test1");
        Object node2 = TestHelper.createTestNode("root2", "test2");
        Pointer leftPtr = TestHelper.newPointerExpression("//test/node()", ctx -> new SingleNodePointer(node1));
        Pointer rightPtr = TestHelper.newPointerExpression("//test/node()", ctx -> new SingleNodePointer(node2));
        CoreOperationCompare op = new CoreOperationCompare(leftPtr, rightPtr);
        EvalContext ctx = TestHelper.newContext();
        Object result = op.compute(ctx);
        assertFalse(result);
    }
}
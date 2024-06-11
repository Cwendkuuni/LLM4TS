package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTestBase;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTestHelper;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.apache.commons.jxpath.ri.iter.Iterator;
import org.junit.Test;

public class CoreOperationCompareTest extends CoreOperationTestBase {

    @Test
    public void testCompareEqual() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createStringExpression("'abc'"),
            CoreOperationTestHelper.createStringExpression("'abc'")
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(compare.compute(context));
    }

    @Test
    public void testCompareNotEqual() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createStringExpression("'abc'"),
            CoreOperationTestHelper.createStringExpression("'def'")
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(compare.compute(context));
    }

    @Test
    public void testCompareNull() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            null,
            CoreOperationTestHelper.createStringExpression("'abc'")
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(compare.compute(context));
    }

    @Test
    public void testCompareNullWithNull() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            null,
            null
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(compare.compute(context));
    }

    @Test
    public void testCompareBooleanTrueWithBooleanTrue() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createBooleanExpression(true),
            CoreOperationTestHelper.createBooleanExpression(true)
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(compare.compute(context));
    }

    @Test
    public void testCompareBooleanFalseWithBooleanFalse() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createBooleanExpression(false),
            CoreOperationTestHelper.createBooleanExpression(false)
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(compare.compute(context));
    }

    @Test
    public void testCompareBooleanTrueWithBooleanFalse() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createBooleanExpression(true),
            CoreOperationTestHelper.createBooleanExpression(false)
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(compare.compute(context));
    }

    @Test
    public void testCompareBooleanFalseWithBooleanTrue() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createBooleanExpression(false),
            CoreOperationTestHelper.createBooleanExpression(true)
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(compare.compute(context));
    }

    @Test
    public void testCompareNumberEqual() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createNumberExpression(1.0),
            CoreOperationTestHelper.createNumberExpression(1.0)
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(compare.compute(context));
    }

    @Test
    public void testCompareNumberNotEqual() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createNumberExpression(1.0),
            CoreOperationTestHelper.createNumberExpression(2.0)
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(compare.compute(context));
    }

    @Test
    public void testCompareStringEqual() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createStringExpression("'abc'"),
            CoreOperationTestHelper.createStringExpression("'abc'")
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(compare.compute(context));
    }

    @Test
    public void testCompareStringNotEqual() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createStringExpression("'abc'"),
            CoreOperationTestHelper.createStringExpression("'def'")
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(compare.compute(context));
    }

    @Test
    public void testCompareCollectionEqual() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createListExpression("[1, 2, 3]"),
            CoreOperationTestHelper.createListExpression("[1, 2, 3]")
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(compare.compute(context));
    }

    @Test
    public void testCompareCollectionNotEqual() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createListExpression("[1, 2, 3]"),
            CoreOperationTestHelper.createListExpression("[1, 2, 4]")
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(compare.compute(context));
    }

    @Test
    public void testCompareIteratorEqual() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createIteratorExpression(
                CoreOperationTestHelper.createListExpression("[1, 2, 3]").iterator()),
            CoreOperationTestHelper.createIteratorExpression(
                CoreOperationTestHelper.createListExpression("[1, 2, 3]").iterator())
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(compare.compute(context));
    }

    @Test
    public void testCompareIteratorNotEqual() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createIteratorExpression(
                CoreOperationTestHelper.createListExpression("[1, 2, 3]").iterator()),
            CoreOperationTestHelper.createIteratorExpression(
                CoreOperationTestHelper.createListExpression("[1, 2, 4]").iterator())
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(compare.compute(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareInvalidArgument1() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext(new InitialContext());
        compare.compute(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareInvalidArgument2() throws Exception {
        CoreOperationCompare compare = new CoreOperationCompare(
            CoreOperationTestHelper.createStringExpression("'abc'"),
            null
        );
        EvalContext context = new EvalContext(new InitialContext());
        compare.compute(context);
    }
}
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.InfoSet;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompareEquals() {
        Expression left = new StringLiteralExpression("1");
        Expression right = new StringLiteralExpression("1");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertTrue(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareNotEquals() {
        Expression left = new StringLiteralExpression("1");
        Expression right = new StringLiteralExpression("2");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertFalse(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareBooleanTrueTrue() {
        Expression left = new BooleanLiteralExpression(true);
        Expression right = new BooleanLiteralExpression(true);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertTrue(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareBooleanTrueFalse() {
        Expression left = new BooleanLiteralExpression(true);
        Expression right = new BooleanLiteralExpression(false);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertFalse(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareBooleanFalseTrue() {
        Expression left = new BooleanLiteralExpression(false);
        Expression right = new BooleanLiteralExpression(true);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertFalse(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareBooleanFalseFalse() {
        Expression left = new BooleanLiteralExpression(false);
        Expression right = new BooleanLiteralExpression(false);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertTrue(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareNaNNaN() {
        Expression left = new DoubleLiteralExpression(Double.NaN);
        Expression right = new DoubleLiteralExpression(Double.NaN);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertTrue(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareNaNNotNaN() {
        Expression left = new DoubleLiteralExpression(Double.NaN);
        Expression right = new StringLiteralExpression("1");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertFalse(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareStringEmptyStringEmptyString() {
        Expression left = new StringLiteralExpression("");
        Expression right = new StringLiteralExpression("");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertTrue(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareStringEmptyStringNonEmptyString() {
        Expression left = new StringLiteralExpression("");
        Expression right = new StringLiteralExpression("foo");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertFalse(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareStringNonEmptyStringEmptyString() {
        Expression left = new StringLiteralExpression("foo");
        Expression right = new StringLiteralExpression("");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertFalse(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareStringNonEmptyStringNonEmptyString() {
        Expression left = new StringLiteralExpression("foo");
        Expression right = new StringLiteralExpression("bar");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertFalse(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareStringEqualString() {
        Expression left = new StringLiteralExpression("foo");
        Expression right = new StringLiteralExpression("foo");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertTrue(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareStringDifferentString() {
        Expression left = new StringLiteralExpression("foo");
        Expression right = new StringLiteralExpression("bar");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertFalse(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareCollectionNullNull() {
        Expression left = new VariableExpression("myCollection");
        Expression right = new VariableExpression("myCollection");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        context.setVariableValue("myCollection", Collections.emptyList());
        assertNull(compare.compute(context));
    }

    @Test
    public void testCoreOperationCompareCollectionNullList() {
        Expression left = new VariableExpression("myCollection");
        Expression right = new ListLiteralExpression(Arrays.asList(new StringLiteralExpression("foo")));
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        context.setVariableValue("myCollection", Collections.emptyList());
        assertFalse(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareCollectionListEmptyList() {
        Expression left = new VariableExpression("myCollection");
        Expression right = new ListLiteralExpression(Collections.emptyList());
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        context.setVariableValue("myCollection", Arrays.asList(new StringLiteralExpression("foo")));
        assertTrue(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareCollectionListSingleElement() {
        Expression left = new VariableExpression("myCollection");
        Expression right = new ListLiteralExpression(Collections.singletonList(new StringLiteralExpression("foo")));
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        context.setVariableValue("myCollection", Arrays.asList(new StringLiteralExpression("foo")));
        assertTrue(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareCollectionListThreeElements() {
        Expression left = new VariableExpression("myCollection");
        Expression right = new ListLiteralExpression(Arrays.asList(new StringLiteralExpression("foo"), new StringLiteralExpression("bar"), new StringLiteralExpression("baz")));
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        context.setVariableValue("myCollection", Arrays.asList(new StringLiteralExpression("foo"), new StringLiteralExpression("bar"), new StringLiteralExpression("baz")));
        assertTrue(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareCollectionListDifferentSize() {
        Expression left = new VariableExpression("myCollection");
        Expression right = new ListLiteralExpression(Arrays.asList(new StringLiteralExpression("foo"), new StringLiteralExpression("bar")));
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        context.setVariableValue("myCollection", Arrays.asList(new StringLiteralExpression("foo"), new StringLiteralExpression("bar"), new StringLiteralExpression("baz")));
        assertFalse(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareCollectionListContains() {
        Expression left = new VariableExpression("myCollection");
        Expression right = new StringLiteralExpression("foo");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        context.setVariableValue("myCollection", Arrays.asList(new StringLiteralExpression("foo"), new StringLiteralExpression("bar"), new StringLiteralExpression("baz")));
        assertTrue(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareCollectionListDoesNotContain() {
        Expression left = new VariableExpression("myCollection");
        Expression right = new StringLiteralExpression("qux");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        context.setVariableValue("myCollection", Arrays.asList(new StringLiteralExpression("foo"), new StringLiteralExpression("bar"), new StringLiteralExpression("baz")));
        assertFalse(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareCollectionListContainsDuplicates() {
        Expression left = new VariableExpression("myCollection");
        Expression right = new StringLiteralExpression("foo");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        context.setVariableValue("myCollection", Arrays.asList(new StringLiteralExpression("foo"), new StringLiteralExpression("foo"), new StringLiteralExpression("bar"), new StringLiteralExpression("baz")));
        assertTrue(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationCompareCollectionListDoesNotContainDuplicates() {
        Expression left = new VariableExpression("myCollection");
        Expression right = new ListLiteralExpression(Arrays.asList(new StringLiteralExpression("foo"), new StringLiteralExpression("bar"), new StringLiteralExpression("baz")));
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        context.setVariableValue("myCollection", Arrays.asList(new StringLiteralExpression("foo"), new StringLiteralExpression("bar"), new StringLiteralExpression("baz")));
        assertTrue(compare.equal(context, left, right));
    }

    @Test
    public void testCoreOperationComparePointerNullNull() {
        Expression left = new VariableExpression("myPointer");
        Expression right = new VariableExpression("myPointer");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertNull(compare.compute(context));
    }

    @Test
    public void testCoreOperationComparePointerNullPointer() {
        Expression left = new VariableExpression("myPointer");
        Expression right = new PointerExpression("myPointer", new StringLiteralExpression("/"));
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        assertNull(compare.compute(context));
    }

    @Test
    public void testCoreOperationComparePointerPointerEqual() {
        Expression left = new VariableExpression("myPointer");
        Expression right = new PointerExpression("myPointer", new StringLiteralExpression("/"));
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        context.setVariableValue("myPointer", new Pointer(new StringLiteralExpression("/"), new StringLiteralExpression(""), new Integer(0), null));
        Pointer leftValue = (Pointer) compare.compute(context);
        Pointer rightValue = (Pointer) context.getVariableValue("myPointer");
        assertTrue(leftValue.equals(rightValue));
    }

    @Test
    public void testCoreOperationComparePointerPointerDifferent() {
        Expression left = new VariableExpression("myPointer1");
        Expression right = new VariableExpression("myPointer2");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(new InitialContext(InfoSet.EMPTY_INFOSET));
        context.setVariableValue("myPointer1", new Pointer(new StringLiteralExpression("/"), new StringLiteralExpression(""), new Integer(0), null));
        context.setVariableValue("myPointer2", new Pointer(new StringLiteralExpression("/"), new StringLiteralExpression(""), new Integer(1), null));
        assertFalse(compare.equal(context, left, right));
    }
}
package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperations;
import org.apache.commons.jxpath.ri.compiler.operators.ComparisonOperator;
import org.apache.commons.jxpath.ri.compiler.operators.ComparisonOperator.Operator;
import org.apache.commons.jxpath.ri.util.InfoSetUtil;

public class CoreOperationCompareTest extends CoreOperationTest {

    @Override
    protected CoreOperation createOperation(Operator op, Expression expr1, Expression expr2) {
        return new CoreOperationCompare(expr1, expr2);
    }

    @Override
    protected CoreOperation getOperationUnderTest() {
        return CoreOperations.OP_EQUAL;
    }

    @org.junit.Test
    public void testEqual_BooleanValues() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(true), new CoreConstantExpression(true));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertTrue(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_BooleanValues_False() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(true), new CoreConstantExpression(false));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertFalse(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_BooleanValues_Null() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(null), new CoreConstantExpression(null));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertTrue(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_Null_Boolean() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(null), new CoreConstantExpression(true));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertFalse(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_Null_Null() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(null), new CoreConstantExpression(null));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertTrue(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_NumberValues() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(1), new CoreConstantExpression(1));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertTrue(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_NumberValues_NotEqual() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(1), new CoreConstantExpression(2));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertFalse(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_StringValues() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression("Hello"), new CoreConstantExpression("Hello"));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertTrue(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_StringValues_NotEqual() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression("Hello"), new CoreConstantExpression("World"));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertFalse(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_Iterators() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(iterator(1, 1, 1, 2, 2)),
                new CoreConstantExpression(iterator(1, 1, 1, 2, 2)));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertTrue(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_Iterators_NotEqual() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(iterator(1, 1, 1, 2, 2)),
                new CoreConstantExpression(iterator(1, 1, 1, 2, 3)));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertFalse(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_Iterators_Null() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(null), new CoreConstantExpression(iterator(1, 1, 1, 2, 2)));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertNull(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_Iterators_IteratorsNull() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(iterator(1, 1, 1, 2, 2)), new CoreConstantExpression(null));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertNull(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_PointerValues() {
        Pointer pointer1 = new Pointer(new Object[] { new Integer(1) }, Collections.singletonMap("name", "item1"));
        Pointer pointer2 = new Pointer(new Object[] { new Integer(1) }, Collections.singletonMap("name", "item1"));
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(pointer1), new CoreConstantExpression(pointer2));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertTrue(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_PointerValues_NotEqual() {
        Pointer pointer1 = new Pointer(new Object[] { new Integer(1) }, Collections.singletonMap("name", "item1"));
        Pointer pointer2 = new Pointer(new Object[] { new Integer(2) }, Collections.singletonMap("name", "item2"));
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(pointer1), new CoreConstantExpression(pointer2));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertFalse(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_PointerValues_Null() {
        Pointer pointer1 = new Pointer(new Object[] { new Integer(1) }, Collections.singletonMap("name", "item1"));
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(pointer1), new CoreConstantExpression(null));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertNull(op.compute(context));
    }

    @org.junit.Test
    public void testEqual_PointerValues_NullPointer() {
        Pointer pointer1 = new Pointer(new Object[] { new Integer(1) }, Collections.singletonMap("name", "item1"));
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_EQUAL,
                new CoreConstantExpression(null), new CoreConstantExpression(pointer1));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertNull(op.compute(context));
    }

    @org.junit.Test
    public void testContains_EmptyCollection() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_CONTAINS,
                new CoreConstantExpression(new ArrayList<>()), new CoreConstantExpression(1));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertFalse(op.compute(context));
    }

    @org.junit.Test
    public void testContains_CollectionWithSingleElement() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_CONTAINS,
                new CoreConstantExpression(Arrays.asList(1)), new CoreConstantExpression(1));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertTrue(op.compute(context));
    }

    @org.junit.Test
    public void testContains_CollectionsWithDifferentElements() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_CONTAINS,
                new CoreConstantExpression(Arrays.asList(1, 2, 3)), new CoreConstantExpression(4));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertFalse(op.compute(context));
    }

    @org.junit.Test
    public void testContains_CollectionWithMultipleElements() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_CONTAINS,
                new CoreConstantExpression(Arrays.asList(1, 2, 3)), new CoreConstantExpression(2));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertTrue(op.compute(context));
    }

    @org.junit.Test
    public void testContains_Null() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_CONTAINS,
                new CoreConstantExpression(null), new CoreConstantExpression(1));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertNull(op.compute(context));
    }

    @org.junit.Test
    public void testFindMatch_EmptySet() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_FIND_MATCH,
                new CoreConstantExpression(Collections.emptySet()), new CoreConstantExpression(1));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertFalse(op.compute(context));
    }

    @org.junit.Test
    public void testFindMatch_SetsWithSingleElement() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_FIND_MATCH,
                new CoreConstantExpression(Collections.singleton(1)), new CoreConstantExpression(1));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertTrue(op.compute(context));
    }

    @org.junit.Test
    public void testFindMatch_SetsWithDifferentElements() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_FIND_MATCH,
                new CoreConstantExpression(Arrays.asList(1, 2, 3)), new CoreConstantExpression(4));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertFalse(op.compute(context));
    }

    @org.junit.Test
    public void testFindMatch_SetsWithMultipleElements() {
        CoreOperationCompare op = (CoreOperationCompare) createOperation(ComparisonOperator.OP_FIND_MATCH,
                new CoreConstantExpression(Arrays.asList(1, 2, 3)), new CoreConstantExpression(2));
        EvalContext context = new EvalContext(new InitialContext(null), null, null);
        assertTrue(op.compute(context));
    }

    private Iterator<Object> iterator(int... values) {
        return Arrays.asList(values).iterator();
    }

}
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CoreOperationCompare tests")
public class CoreOperationCompareTest {

    @Test
    void testEqualWithBooleanValues() {
        Expression left = new LiteralExpression(true);
        Expression right = new LiteralExpression(true);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.evaluate(new EvaluationContext(), null), "Boolean values should be equal");
    }

    @Test
    void testEqualWithNumberValues() {
        Expression left = new LiteralExpression(1.0);
        Expression right = new LiteralExpression(1.0);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.evaluate(new EvaluationContext(), null), "Number values should be equal");
    }

    @Test
    void testEqualWithStringValues() {
        Expression left = new LiteralExpression("test");
        Expression right = new LiteralExpression("test");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.evaluate(new EvaluationContext(), null), "String values should be equal");
    }

    @Test
    void testEqualWithNullAndNull() {
        Expression left = new LiteralExpression(null);
        Expression right = new LiteralExpression(null);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.evaluate(new EvaluationContext(), null), "Null values should be equal");
    }

    @Test
    void testEqualWithNullAndNonNull() {
        Expression left = new LiteralExpression(null);
        Expression right = new LiteralExpression("test");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Null value should not be equal to non-null value");
    }

    @Test
    void testEqualWithDifferentDataTypes() {
        Expression left = new LiteralExpression(1.0);
        Expression right = new LiteralExpression("test");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Different data types should not be equal");
    }

    @Test
    void testEqualWithSelfContext() {
        Expression left = new SelfContext();
        Expression right = new SelfContext();
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.evaluate(new EvaluationContext(), null), "Self contexts should be equal");
    }

    @Test
    void testEqualWithInitialContext() {
        Expression left = new InitialContext();
        Expression right = new InitialContext();
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertTrue(compare.evaluate(new EvaluationContext(), null), "Initial contexts should be equal");
    }

    @Test
    void testEqualWithCollections() {
        Collection<Object> left = List.of(1, 2, 3);
        Collection<Object> right = List.of(1, 2, 3);
        Expression leftExpr = new VariableExpression("left", left);
        Expression rightExpr = new VariableExpression("right", right);
        CoreOperationCompare compare = new CoreOperationCompare(leftExpr, rightExpr);
        assertTrue(compare.evaluate(new EvaluationContext(), null), "Collections with same elements should be equal");
    }

    @Test
    void testEqualWithDifferentCollections() {
        Collection<Object> left = List.of(1, 2, 3);
        Collection<Object> right = List.of(3, 2, 1);
        Expression leftExpr = new VariableExpression("left", left);
        Expression rightExpr = new VariableExpression("right", right);
        CoreOperationCompare compare = new CoreOperationCompare(leftExpr, rightExpr);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Different collections should not be equal");
    }

    @Test
    void testEqualWithIterators() {
        List<Object> list = List.of(1, 2, 3);
        Iterator<Object> left = list.iterator();
        Iterator<Object> right = list.iterator();
        Expression leftExpr = new VariableExpression("left", left);
        Expression rightExpr = new VariableExpression("right", right);
        CoreOperationCompare compare = new CoreOperationCompare(leftExpr, rightExpr);
        assertTrue(compare.evaluate(new EvaluationContext(), null), "Iterators over the same collection should be equal");
    }

    @Test
    void testEqualWithDifferentIterators() {
        List<Object> list = List.of(1, 2, 3);
        Iterator<Object> left = list.iterator();
        Iterator<Object> right = List.of(3, 2, 1).iterator();
        Expression leftExpr = new VariableExpression("left", left);
        Expression rightExpr = new VariableExpression("right", right);
        CoreOperationCompare compare = new CoreOperationCompare(leftExpr, rightExpr);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Different iterators should not be equal");
    }

    @Test
    void testNotEqualWithBooleanValues() {
        Expression left = new LiteralExpression(false);
        Expression right = new LiteralExpression(true);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Boolean values should not be equal");
    }

    @ParameterizedTest
    @MethodSource("numberValuesForNotEqual")
    void testNotEqualWithNumberValues(Double l, Double r) {
        Expression left = new LiteralExpression(l);
        Expression right = new LiteralExpression(r);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Number values should not be equal");
    }

    private static Stream<Double[]> numberValuesForNotEqual() {
        return Stream.of(
            new Double[]{1.0, 2.0},
            new Double[]{2.0, 1.0},
            new Double[]{-1.0, 1.0},
            new Double[]{1.0, -1.0},
            new Double[]{Double.MAX_VALUE, Double.MIN_VALUE}
        );
    }

    @Test
    void testNotEqualWithStringValues() {
        Expression left = new LiteralExpression("test1");
        Expression right = new LiteralExpression("test2");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "String values should not be equal");
    }

    @Test
    void testNotEqualWithNullAndNonNull() {
        Expression left = new LiteralExpression(null);
        Expression right = new LiteralExpression("test");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Null value should not be equal to non-null value");
    }

    @Test
    void testNotEqualWithDifferentDataTypes() {
        Expression left = new LiteralExpression(1.0);
        Expression right = new LiteralExpression("test");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Different data types should not be equal");
    }

    @Test
    void testNotEqualWithSelfContext() {
        Expression left = new SelfContext();
        Expression right = new InitialContext();
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Self context and Initial context should not be equal");
    }

    @Test
    void testNotEqualWithInitialContext() {
        Expression left = new InitialContext();
        Expression right = new SelfContext();
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Initial context and Self context should not be equal");
    }

    @Test
    void testNotEqualWithCollections() {
        Collection<Object> left = List.of(1, 2, 3);
        Collection<Object> right = List.of(3, 2, 1);
        Expression leftExpr = new VariableExpression("left", left);
        Expression rightExpr = new VariableExpression("right", right);
        CoreOperationCompare compare = new CoreOperationCompare(leftExpr, rightExpr);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Different collections should not be equal");
    }

    @Test
    void testNotEqualWithIterators() {
        List<Object> list = List.of(1, 2, 3);
        Iterator<Object> left = list.iterator();
        Iterator<Object> right = List.of(3, 2, 1).iterator();
        Expression leftExpr = new VariableExpression("left", left);
        Expression rightExpr = new VariableExpression("right", right);
        CoreOperationCompare compare = new CoreOperationCompare(leftExpr, rightExpr);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Different iterators should not be equal");
    }

    @Test
    void testNotEqualWithPointers() {
        Pointer left = new Pointer("test1");
        Pointer right = new Pointer("test2");
        Expression leftExpr = new VariableExpression("left", left);
        Expression rightExpr = new VariableExpression("right", right);
        CoreOperationCompare compare = new CoreOperationCompare(leftExpr, rightExpr);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Pointers with different values should not be equal");
    }

    @Test
    void testNotEqualWithPointersToTheSameNode() {
        Pointer left = new Pointer("test");
        Expression leftExpr = new VariableExpression("left", left);
        CoreOperationCompare compare = new CoreOperationCompare(leftExpr, leftExpr);
        assertFalse(compare.evaluate(new EvaluationContext(), null), "Pointer to the same node should not be equal to itself");
    }
}
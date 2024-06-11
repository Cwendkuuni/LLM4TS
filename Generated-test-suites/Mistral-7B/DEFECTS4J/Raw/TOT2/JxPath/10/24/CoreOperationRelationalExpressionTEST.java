package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Iterator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertNull(expression.getLeftExpression());
        assertNull(expression.getRightExpression());
    }

    @Test
    public void testComputeValue() {
        Expression left = new ConstantExpression(1.0);
        Expression right = new ConstantExpression(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationAdd(left, new ConstantExpression(1.0)), right);
        Boolean result = (Boolean) expression.computeValue(new EvalContext());
        assertTrue(result);
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValueWithIncorrectDataType() {
        Expression left = new ConstantExpression("string");
        Expression right = new ConstantExpression(1.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(left, right);
        expression.computeValue(new EvalContext());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationAdd(new ConstantExpression(1.0), new ConstantExpression(1.0)), new ConstantExpression(2.0));
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationAdd(new ConstantExpression(1.0), new ConstantExpression(1.0)), new ConstantExpression(2.0));
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new ConstantExpression(1.0), new ConstantExpression(2.0));
        assertTrue(expression.evaluateCompare(-1));
        assertTrue(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testComputeValueWithNaN() {
        Expression left = new ConstantExpression(Double.NaN);
        Expression right = new ConstantExpression(Double.NaN);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(left, right);
        Boolean result = (Boolean) expression.computeValue(new EvalContext());
        assertFalse(result);
    }

    @Test
    public void testContainsMatch() {
        Iterator it = new ArrayList(Arrays.asList(new ConstantExpression(1.0), new ConstantExpression(2.0), new ConstantExpression(3.0)).iterator());
        Iterator rit = new ArrayList(Arrays.asList(new ConstantExpression(1.0), new ConstantExpression(1.0), new ConstantExpression(3.0)).iterator());
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(it, rit);
        assertFalse(expression.containsMatch(it, rit.next()));
        assertTrue(expression.containsMatch(it, rit.next()));
        assertFalse(expression.containsMatch(it, rit.next()));
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new ArrayList(Arrays.asList(new ConstantExpression(1.0), new ConstantExpression(2.0), new ConstantExpression(3.0)).iterator());
        Iterator rit = new ArrayList(Arrays.asList(new ConstantExpression(1.0), new ConstantExpression(1.0), new ConstantExpression(3.0)).iterator());
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(lit, rit);
        assertTrue(expression.findMatch(lit, rit));
    }

    @Test
    public void testComprehensive() {
        // Test with different data types
        Expression left1 = new ConstantExpression(1.0);
        Expression right1 = new ConstantExpression("string");
        CoreOperationRelationalExpression expression1 = new CoreOperationGreaterThan(left1, right1);
        Boolean result1 = (Boolean) expression1.computeValue(new EvalContext());
        assertFalse(result1);

        // Test with null values
        Expression left2 = new ConstantExpression(null);
        Expression right2 = new ConstantExpression(2.0);
        CoreOperationRelationalExpression expression2 = new CoreOperationGreaterThan(left2, right2);
        Boolean result2 = (Boolean) expression2.computeValue(new EvalContext());
        assertFalse(result2);

        // Test with Iterator data structures
        Iterator it1 = new ArrayList(Arrays.asList(new ConstantExpression(1.0), new ConstantExpression(2.0), new ConstantExpression(3.0)).iterator());
        Iterator it2 = new ArrayList(Arrays.asList(new ConstantExpression(1.0), new ConstantExpression(1.0), new ConstantExpression(3.0)).iterator());
        CoreOperationRelationalExpression expression3 = new CoreOperationGreaterThan(it1, it2);
        assertFalse(expression3.containsMatch(it1, it2.next()));
        assertTrue(expression3.containsMatch(it1, it2.next()));
        assertFalse(expression3.containsMatch(it1, it2.next()));

        // Test with HashSet data structures
        Iterator it3 = new HashSet(Arrays.asList(new ConstantExpression(1.0), new ConstantExpression(2.0), new ConstantExpression(3.0)).iterator());
        Iterator it4 = new HashSet(Arrays.asList(new ConstantExpression(1.0), new ConstantExpression(1.0), new ConstantExpression(3.0)).iterator());
        CoreOperationRelationalExpression expression4 = new CoreOperationGreaterThan(it3, it4);
        assertFalse(expression4.findMatch(it3, it4));
        assertTrue(expression4.findMatch(it3, it4.next()));
        assertFalse(expression4.findMatch(it3, it4.next()));
    }
}
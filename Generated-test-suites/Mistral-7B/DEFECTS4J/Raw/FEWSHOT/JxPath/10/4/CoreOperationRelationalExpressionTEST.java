package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan("1", "2");
        EvalContext context = new EvalContext(new SelfContext());
        Object left = new Integer(1);
        Object right = new Integer(2);

        assertEquals(Boolean.FALSE, expression.computeValue(context));

        context = new EvalContext(new SelfContext(left));
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        context = new EvalContext(new SelfContext(right));
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        context = new EvalContext(new SelfContext(left), new SelfContext(right));
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        context = new EvalContext(Arrays.asList(left, right).iterator());
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        context = new EvalContext(Collections.singletonList(left));
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        context = new EvalContext(Collections.singletonList(right));
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        context = new EvalContext(new InitialContext());
        context.setCurrentNode(left);
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        context.setCurrentNode(right);
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        context.setCurrentNode(left);
        context.setNextNode(right);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan("1", "2");
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan("1", "2");
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan("1", "2");
        assertTrue(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testReduce() {
        Object input = new SelfContext();

        assertEquals(input, CoreOperationRelationalExpression.reduce(input));

        InputMismatchException exception = assertThrows(InputMismatchException.class, () -> CoreOperationRelationalExpression.reduce(2));
        assertEquals("Error: invalid comparison type [java.lang.Integer]", exception.getMessage());

        InputMismatchException exception2 = assertThrows(InputMismatchException.class, () -> CoreOperationRelationalExpression.reduce(new HashSet<>()));
        assertEquals("Error: invalid comparison type [java.util.HashSet]", exception2.getMessage());
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan("1", "2");

        Iterator input1 = Arrays.asList(new Integer(1), new Integer(2), new Integer(3)).iterator();
        assertFalse(expression.containsMatch(input1, new Integer(2)));
        assertFalse(expression.containsMatch(input1, new Integer(1)));
        assertTrue(expression.containsMatch(input1, new Integer(3)));

        Iterator input2 = Arrays.asList(new Integer(2), new Integer(1), new Integer(3)).iterator();
        assertFalse(expression.containsMatch(input2, new Integer(2)));
        assertFalse(expression.containsMatch(input2, new Integer(1)));
        assertTrue(expression.containsMatch(input2, new Integer(3)));

        InputMismatchException exception = assertThrows(InputMismatchException.class, () -> expression.containsMatch(Arrays.asList(new Integer(1), new Integer(2), new Integer(3)).iterator(), new Integer(4)));
        assertEquals("Error: invalid comparison type [java.lang.Integer]", exception.getMessage());

        InputMismatchException exception2 = assertThrows(InputMismatchException.class, () -> expression.containsMatch(Arrays.asList(new Integer(1), new Integer(2), new Integer(3)).iterator(), new HashSet<>()));
        assertEquals("Error: invalid comparison type [java.util.HashSet]", exception2.getMessage());
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan("1", "2");

        Iterator input1 = Arrays.asList(new Integer(1), new Integer(2), new Integer(3)).iterator();
        HashSet expected = new HashSet();
        expected.add(new Integer(1));
        expected.add(new Integer(3));
        HashSet result = new HashSet();

        expression.findMatch(input1, Arrays.asList(new Integer(1), new Integer(2), new Integer(3)).iterator());

        assertEquals(expected, result);

        Iterator input2 = Arrays.asList(new Integer(1), new Integer(2), new Integer(3)).iterator();
        expected.clear();
        expected.add(new Integer(3));
        result.clear();

        expression.findMatch(Arrays.asList(new Integer(1), new Integer(2), new Integer(3)).iterator(), input1);

        assertEquals(expected, result);

        InputMismatchException exception = assertThrows(InputMismatchException.class, () -> expression.findMatch(Arrays.asList(new Integer(1), new Integer(2), new Integer(3)).iterator(), new Integer(4)));
        assertEquals("Error: invalid comparison type [java.lang.Integer]", exception.getMessage());

        InputMismatchException exception2 = assertThrows(InputMismatchException.class, () -> expression.findMatch(Arrays.asList(new Integer(1), new Integer(2), new Integer(3)).iterator(), new HashSet<>()));
        assertEquals("Error: invalid comparison type [java.util.HashSet]", exception2.getMessage());
    }
}
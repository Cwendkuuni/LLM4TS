package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for CoreOperationCompare class
 */
public class CoreOperationCompareTest {

    private EvalContext context;
    private InitialContext initialContext;
    private SelfContext selfContext;

    @Before
    public void setUp() {
        context = mock(EvalContext.class);
        initialContext = new InitialContext("root");
        selfContext = new SelfContext("root");
    }

    @Test
    public void testEqual_BooleanValues() {
        Expression left = new LiteralExpression(true);
        Expression right = new LiteralExpression(true);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_BooleanValues_False() {
        Expression left = new LiteralExpression(true);
        Expression right = new LiteralExpression(false);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_NumberValues() {
        Expression left = new LiteralExpression(1.0);
        Expression right = new LiteralExpression(1.0);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_NumberValues_NotEqual() {
        Expression left = new LiteralExpression(1.0);
        Expression right = new LiteralExpression(2.0);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_StringValues() {
        Expression left = new StringLiteralExpression("foo");
        Expression right = new StringLiteralExpression("foo");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_StringValues_NotEqual() {
        Expression left = new StringLiteralExpression("foo");
        Expression right = new StringLiteralExpression("bar");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_NullValues() {
        Expression left = new LiteralExpression(null);
        Expression right = new LiteralExpression(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_NullAndNonNullValues() {
        Expression left = new LiteralExpression(null);
        Expression right = new StringLiteralExpression("foo");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_PointerValues() {
        Expression left = new VariableExpression("$pointer1");
        Expression right = new VariableExpression("$pointer2");
        Pointer pointer1 = new Pointer("root", "foo");
        Pointer pointer2 = new Pointer("root", "foo");

        when(context.getVariables()).thenReturn(new Variables());
        when(context.getVariableValue("$pointer1")).thenReturn(pointer1);
        when(context.getVariableValue("$pointer2")).thenReturn(pointer2);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_PointerAndNonPointerValues() {
        Expression left = new VariableExpression("$pointer");
        Expression right = new StringLiteralExpression("foo");
        Pointer pointer = new Pointer("root", "foo");

        when(context.getVariables()).thenReturn(new Variables());
        when(context.getVariableValue("$pointer")).thenReturn(pointer);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testContains_IterableValues() {
        Expression left = new VariableExpression("$iterable");
        Expression value = new StringLiteralExpression("foo");

        List<String> iterable = new ArrayList<>();
        iterable.add("foo");
        iterable.add("bar");

        when(context.getVariables()).thenReturn(new Variables());
        when(context.getVariableValue("$iterable")).thenReturn(iterable.iterator());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, value);

        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testContains_IterableValues_NotContained() {
        Expression left = new VariableExpression("$iterable");
        Expression value = new StringLiteralExpression("bar");

        List<String> iterable = new ArrayList<>();
        iterable.add("foo");

        when(context.getVariables()).thenReturn(new Variables());
        when(context.getVariableValue("$iterable")).thenReturn(iterable.iterator());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, value);

        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatch_IteratorsWithMatchingElements() {
        Object leftValue = new Pointer("root", "foo");
        Object rightValue = new Pointer("root", "foo");

        Iterator<Object> leftIterator = () -> new Object[] { leftValue, null }[0];
        Iterator<Object> rightIterator = () -> new Object[] { rightValue, null }[0];

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$leftIterator"),
            new VariableExpression("$rightIterator")
        );

        when(context.getVariables()).thenReturn(new Variables());

        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatch_IteratorsWithoutMatchingElements() {
        Object leftValue = new Pointer("root", "foo");
        Object rightValue = new Pointer("root", "bar");

        Iterator<Object> leftIterator = () -> new Object[] { leftValue, null }[0];
        Iterator<Object> rightIterator = () -> new Object[] { rightValue, null }[0];

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$leftIterator"),
            new VariableExpression("$rightIterator")
        );

        when(context.getVariables()).thenReturn(new Variables());

        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatch_IteratorsWithMatchingSet() {
        Set<Object> leftSet = new HashSet<>();
        leftSet.add(new Pointer("root", "foo"));

        Set<Object> rightSet = new HashSet<>();
        rightSet.add(new Pointer("root", "foo"));

        Iterator<Object> leftIterator = leftSet.iterator();
        Iterator<Object> rightIterator = rightSet.iterator();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$leftIterator"),
            new VariableExpression("$rightIterator")
        );

        when(context.getVariables()).thenReturn(new Variables());

        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatch_IteratorsWithoutMatchingSet() {
        Set<Object> leftSet = new HashSet<>();
        leftSet.add(new Pointer("root", "foo"));

        Set<Object> rightSet = new HashSet<>();
        rightSet.add(new Pointer("root", "bar"));

        Iterator<Object> leftIterator = leftSet.iterator();
        Iterator<Object> rightIterator = rightSet.iterator();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$leftIterator"),
            new VariableExpression("$rightIterator")
        );

        when(context.getVariables()).thenReturn(new Variables());

        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatch_IteratorsWithMatchingCollection() {
        List<Object> leftCollection = new ArrayList<>();
        leftCollection.add(new Pointer("root", "foo"));

        List<Object> rightCollection = new ArrayList<>();
        rightCollection.add(new Pointer("root", "foo"));

        Iterator<Object> leftIterator = leftCollection.iterator();
        Iterator<Object> rightIterator = rightCollection.iterator();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$leftIterator"),
            new VariableExpression("$rightIterator")
        );

        when(context.getVariables()).thenReturn(new Variables());

        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatch_IteratorsWithoutMatchingCollection() {
        List<Object> leftCollection = new ArrayList<>();
        leftCollection.add(new Pointer("root", "foo"));

        List<Object> rightCollection = new ArrayList<>();
        rightCollection.add(new Pointer("root", "bar"));

        Iterator<Object> leftIterator = leftCollection.iterator();
        Iterator<Object> rightIterator = rightCollection.iterator();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$leftIterator"),
            new VariableExpression("$rightIterator")
        );

        when(context.getVariables()).thenReturn(new Variables());

        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatch_EmptyIterators() {
        Iterator<Object> leftIterator = Iterators.emptyIterator();
        Iterator<Object> rightIterator = Iterators.emptyIterator();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$leftIterator"),
            new VariableExpression("$rightIterator")
        );

        when(context.getVariables()).thenReturn(new Variables());

        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_VariableValues_SameVariable() {
        Expression left = new VariableExpression("$variable");
        Expression right = new VariableExpression("$variable");

        String variableName = "$variable";
        Object variableValue = new Pointer("root", "foo");

        when(context.getVariables()).thenReturn(new Variables());
        when(context.getVariableValue(variableName)).thenReturn(variableValue);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_VariableValues_DifferentVariables() {
        Expression left = new VariableExpression("$variable1");
        Expression right = new VariableExpression("$variable2");

        String variable1Name = "$variable1";
        Object variable1Value = new Pointer("root", "foo");
        String variable2Name = "$variable2";
        Object variable2Value = new Pointer("root", "bar");

        when(context.getVariables()).thenReturn(new Variables());
        when(context.getVariableValue(variable1Name)).thenReturn(variable1Value);
        when(context.getVariableValue(variable2Name)).thenReturn(variable2Value);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertFalse(coreOperationCompare.evaluate(context));
    }

}
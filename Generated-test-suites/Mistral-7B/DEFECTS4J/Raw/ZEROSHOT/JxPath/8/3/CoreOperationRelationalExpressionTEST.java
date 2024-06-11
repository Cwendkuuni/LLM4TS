import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionArg1;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionArg2;
import org.apache.commons.jxpath.ri.evaluator.EvalContext;
import org.apache.commons.jxpath.ri.iter.InitialContext;
import org.apache.commons.jxpath.ri.iter.SelfContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArg1(new InitialContext()), new CoreOperationRelationalExpressionArg2(new InitialContext()));
        EvalContext context = PowerMockito.mock(EvalContext.class);
        Object leftValue = new Integer(5);
        Object rightValue = new Integer(10);

        PowerMockito.whenNew(EvalContext.class).withNoArguments().thenReturn(context);
        PowerMockito.when(expression.args[0].computeValue(context)).thenReturn(leftValue);
        PowerMockito.when(expression.args[1].computeValue(context)).thenReturn(rightValue);

        Object result = expression.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArg1(new InitialContext()), new CoreOperationRelationalExpressionArg2(new InitialContext()));

        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArg1(new InitialContext()), new CoreOperationRelationalExpressionArg2(new InitialContext()));

        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArg1(new InitialContext()), new CoreOperationRelationalExpressionArg2(new InitialContext()));

        assertTrue(expression.evaluateCompare(1));
        assertFalse(expression.evaluateCompare(0));
    }

    @Test
    public void testComputeWithTwoInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArg1(new InitialContext()), new CoreOperationRelationalExpressionArg2(new InitialContext()));
        EvalContext context = PowerMockito.mock(EvalContext.class);

        PowerMockito.whenNew(EvalContext.class).withNoArguments().thenReturn(context);

        Object leftValue = Mockito.mock(Object.class);
        Object rightValue = Mockito.mock(Object.class);

        PowerMockito.when(expression.args[0].computeValue(context)).thenReturn(new SelfContext());
        PowerMockito.when(expression.args[1].computeValue(context)).thenReturn(new SelfContext());

        Object result = expression.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeWithTwoIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArg1(new InitialContext()), new CoreOperationRelationalExpressionArg2(new InitialContext()));
        EvalContext context = PowerMockito.mock(EvalContext.class);

        PowerMockito.whenNew(EvalContext.class).withNoArguments().thenReturn(context);

        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);

        PowerMockito.when(expression.args[0].computeValue(context)).thenReturn(leftIterator);
        PowerMockito.when(expression.args[1].computeValue(context)).thenReturn(rightIterator);

        PowerMockito.when(leftIterator.hasNext()).thenReturn(true).thenReturn(false);
        PowerMockito.when(rightIterator.hasNext()).thenReturn(true).thenReturn(false);
        PowerMockito.when(leftIterator.next()).thenReturn(new Integer(5));
        PowerMockito.when(rightIterator.next()).thenReturn(new Integer(10));

        Object result = expression.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeWithTwoIteratorsAndMatchFound() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArg1(new InitialContext()), new CoreOperationRelationalExpressionArg2(new InitialContext()));
        EvalContext context = PowerMockito.mock(EvalContext.class);

        PowerMockito.whenNew(EvalContext.class).withNoArguments().thenReturn(context);

        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);

        PowerMockito.when(expression.args[0].computeValue(context)).thenReturn(leftIterator);
        PowerMockito.when(expression.args[1].computeValue(context)).thenReturn(rightIterator);

        Set leftSet = new HashSet<>();
        leftSet.add(new Integer(5));
        leftSet.add(new Integer(6));

        PowerMockito.when(leftIterator.iterator()).thenReturn(leftSet.iterator());

        PowerMockito.when(rightIterator.hasNext()).thenReturn(false);

        Object result = expression.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeWithTwoIteratorsAndMatchFoundForLeft() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArg1(new InitialContext()), new CoreOperationRelationalExpressionArg2(new InitialContext()));
        EvalContext context = PowerMockito.mock(EvalContext.class);

        PowerMockito.whenNew(EvalContext.class).withNoArguments().thenReturn(context);

        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);

        PowerMockito.when(expression.args[0].computeValue(context)).thenReturn(leftIterator);
        PowerMockito.when(expression.args[1].computeValue(context)).thenReturn(rightIterator);

        Set leftSet = new HashSet<>();
        leftSet.add(new Integer(5));
        leftSet.add(new Integer(6));

        PowerMockito.when(leftIterator.iterator()).thenReturn(leftSet.iterator());

        PowerMockito.when(rightIterator.hasNext()).thenReturn(true);
        PowerMockito.when(rightIterator.next()).thenReturn(new Integer(6));

        Object result = expression.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeWithTwoIteratorsAndMatchFoundForRight() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArg1(new InitialContext()), new CoreOperationRelationalExpressionArg2(new InitialContext()));
        EvalContext context = PowerMockito.mock(EvalContext.class);

        PowerMockito.whenNew(EvalContext.class).withNoArguments().thenReturn(context);

        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);

        PowerMockito.when(expression.args[0].computeValue(context)).thenReturn(leftIterator);
        PowerMockito.when(expression.args[1].computeValue(context)).thenReturn(rightIterator);

        Set rightSet = new HashSet<>();
        rightSet.add(new Integer(6));
        rightSet.add(new Integer(7));

        PowerMockito.when(rightIterator.iterator()).thenReturn(rightSet.iterator());

        PowerMockito.when(leftIterator.hasNext()).thenReturn(true).thenReturn(false);

        Object result = expression.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeWithTwoIteratorsAndMatchFoundForBoth() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArg1(new InitialContext()), new CoreOperationRelationalExpressionArg2(new InitialContext()));
        EvalContext context = PowerMockito.mock(EvalContext.class);

        PowerMockito.whenNew(EvalContext.class).withNoArguments().thenReturn(context);

        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);

        PowerMockito.when(expression.args[0].computeValue(context)).thenReturn(leftIterator);
        PowerMockito.when(expression.args[1].computeValue(context)).thenReturn(rightIterator);

        Set leftSet = new HashSet<>();
        leftSet.add(new Integer(5));
        leftSet.add(new Integer(6));

        PowerMockito.when(leftIterator.iterator()).thenReturn(leftSet.iterator());

        Set rightSet = new HashSet<>();
        rightSet.add(new Integer(6));
        rightSet.add(new Integer(7));

        PowerMockito.when(rightIterator.iterator()).thenReturn(rightSet.iterator());

        Object result = expression.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeWithTwoDoubles() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArg1(new InitialContext()), new CoreOperationRelationalExpressionArg2(new InitialContext()));
        EvalContext context = PowerMockito.mock(EvalContext.class);

        PowerMockito.whenNew(EvalContext.class).withNoArguments().thenReturn(context);

        Object leftValue = new Double(5.5);
        Object rightValue = new Double(6.5);

        PowerMockito.when(expression.args[0].computeValue(context)).thenReturn(leftValue);
        PowerMockito.when(expression.args[1].computeValue(context)).thenReturn(rightValue);

        Object result = expression.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeWithTwoDoublesAndNotMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArg1(new InitialContext()), new CoreOperationRelationalExpressionArg2(new InitialContext()));
        EvalContext context = PowerMockito.mock(EvalContext.class);

        PowerMockito.whenNew(EvalContext.class).withNoArguments().thenReturn(context);

        Object leftValue = new Double(5.5);
        Object rightValue = new Double(4.5);

        PowerMockito.when(expression.args[0].computeValue(context)).thenReturn(leftValue);
        PowerMockito.when(expression.args[1].computeValue(context)).thenReturn(rightValue);

        Object result = expression.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeWithTwoDoublesAndEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArg1(new InitialContext()), new CoreOperationRelationalExpressionArg2(new InitialContext()));
        EvalContext context = PowerMockito.mock(EvalContext.class);

        PowerMockito.whenNew(EvalContext.class).withNoArguments().thenReturn(context);

        Object leftValue = new Double(5.5);
        Object rightValue = new Double(5.5);

        PowerMockito.when(expression.args[0].computeValue(context)).thenReturn(leftValue);
        PowerMockito.when(expression.args[1].computeValue(context)).thenReturn(rightValue);

        Object result = expression.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }
}
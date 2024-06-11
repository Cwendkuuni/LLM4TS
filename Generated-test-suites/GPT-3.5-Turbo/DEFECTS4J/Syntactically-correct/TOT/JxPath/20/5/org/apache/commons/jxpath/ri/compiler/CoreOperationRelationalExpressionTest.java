import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.Context;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.Compiler.XPath;

import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setup() {
        expression = new CoreOperationRelationalExpression(new Expression[2]) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }

        };
    }

    @Test
    public void computeValue_shouldReturnTrueWhenComparisonIsTrue() {
        EvaluationContext context = createContext(getMockedExpression(1), getMockedExpression(2));

        Object result = expression.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void computeValue_shouldReturnFalseWhenComparisonIsFalse() {
        EvaluationContext context = createContext(getMockedExpression(2), getMockedExpression(1));

        Object result = expression.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void computeValue_shouldReturnFalseWhenComparisonIsEqual() {
        EvaluationContext context = createContext(getMockedExpression(1), getMockedExpression(1));

        Object result = expression.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void computeValue_shouldReturnFalseWhenComparisonIsNaN() {
        EvaluationContext context = createContext(getMockedExpression(Double.NaN), getMockedExpression(1));

        Object result = expression.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void compute_shouldCallReduceMethodOnBothOperands() {
        Object leftOperand = getMockedExpression(1);
        Object rightOperand = getMockedExpression(2);
        EvaluationContext context = createContext(leftOperand, rightOperand);

        expression.compute(leftOperand, rightOperand);

        verify(leftOperand).reduce();
        verify(rightOperand).reduce();
    }

    @Test
    public void compute_shouldCallResetMethodOnInitialContextOperands() {
        Object leftOperand = getMockedInitialContext();
        Object rightOperand = getMockedInitialContext();
        EvaluationContext context = createContext(leftOperand, rightOperand);

        expression.compute(leftOperand, rightOperand);

        verify(leftOperand).reset();
        verify(rightOperand).reset();
    }

    @Test
    public void compute_shouldCallFindMatchIfBothOperandsAreIterator() {
        Iterator<Object> leftOperand = getMockedIterator();
        Iterator<Object> rightOperand = getMockedIterator();
        EvaluationContext context = createContext(leftOperand, rightOperand);

        expression.compute(leftOperand, rightOperand);

        verifyPrivate(expression).invoke("findMatch", leftOperand, rightOperand);
        verifyNoMoreInteractions(rightOperand);
    }

    @Test
    public void compute_shouldCallContainsMAatchIfLeftOperandIsIterator() {
        Iterator<Object> leftOperand = getMockedIterator();
        Object rightOperand = getMockedExpression(2);
        EvaluationContext context = createContext(leftOperand, rightOperand);

        expression.compute(leftOperand, rightOperand);

        verifyPrivate(expression).invoke("containsMatch", leftOperand, rightOperand);
    }

    @Test
    public void compute_shouldCallContainsMAatchIfRightOperandIsIterator() {
        Object leftOperand = getMockedExpression(1);
        Iterator<Object> rightOperand = getMockedIterator();
        EvaluationContext context = createContext(leftOperand, rightOperand);

        expression.compute(leftOperand, rightOperand);

        verifyPrivate(expression).invoke("containsMatch", rightOperand, leftOperand);
    }

    @Test
    public void compute_shouldCallEvaluateCompareIfBothOperandsAreNotIterator() {
        Object leftOperand = getMockedExpression(1);
        Object rightOperand = getMockedExpression(2);
        EvaluationContext context = createContext(leftOperand, rightOperand);

        expression.compute(leftOperand, rightOperand);

        verifyPrivate(expression).invoke("evaluateCompare", anyInt());
    }

    @Test
    public void reduce_shouldReturnSelfContextIfOperandIsSelfContext() {
        Expression operand = getMockedExpression(new SelfContext(null));
        EvaluationContext context = createContext(operand);

        Object result = expression.reduce(operand, context);

        assertEquals(operand, result);
    }

    @Test
    public void reduce_shouldReturnIteratorIfOperandIsCollection() {
        List<Object> list = new ArrayList<>();
        Expression operand = getMockedExpression(list);
        EvaluationContext context = createContext(operand);
        Iterator<?> iterator = list.iterator();

        Object result = expression.reduce(operand, context);

        assertEquals(iterator, result);
    }

    @Test
    public void reduce_shouldReturnOperandAsItselfIfNotSelfContextOrCollection() {
        Expression operand = getMockedExpression(1);
        EvaluationContext context = createContext(operand);

        Object result = expression.reduce(operand, context);

        assertEquals(operand, result);
    }

    @Test
    public void containsMatch_shouldReturnTrueIfAnyElementMatches() {
        Iterator<Object> iterator = getMockedIterator();
        Object value = getMockedExpression(2);

        boolean result = expression.containsMatch(iterator, value);

        assertTrue(result);
    }

    @Test
    public void containsMatch_shouldReturnFalseIfNoElementMatches() {
        Iterator<Object> iterator = getMockedIterator();
        Object value = getMockedExpression(3);

        boolean result = expression.containsMatch(iterator, value);

        assertFalse(result);
    }

    @Test
    public void findMatch_shouldReturnTrueIfAnyElementMatches() {
        Iterator<Object> lit = getMockedIterator(Arrays.asList(1, 2, 3));
        Iterator<Object> rit = getMockedIterator(Arrays.asList(3, 4, 5));

        boolean result = expression.findMatch(lit, rit);

        assertTrue(result);
    }

    @Test
    public void findMatch_shouldReturnFalseIfNoElementMatches() {
        Iterator<Object> lit = getMockedIterator(Arrays.asList(1, 2, 3));
        Iterator<Object> rit = getMockedIterator(Arrays.asList(4, 5, 6));

        boolean result = expression.findMatch(lit, rit);

        assertFalse(result);
    }

    private Expression getMockedExpression(Object result) {
        Expression expression = mock(Expression.class);
        when(expression.compute(any())).thenReturn(result);
        when(expression.reduce()).thenReturn(result);
        return expression;
    }

    private InitialContext getMockedInitialContext() {
        InitialContext initialContext = mock(InitialContext.class);
        when(initialContext.reset()).thenReturn(null);
        return initialContext;
    }

    private Iterator<Object> getMockedIterator() {
        return getMockedIterator(Arrays.asList(1, 2, 3));
    }

    private Iterator<Object> getMockedIterator(Collection<Object> elements) {
        Iterator<Object> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.next()).thenReturn(elements.toArray());
        return iterator;
    }

    private EvaluationContext createContext(Object... operands) {
        EvaluationContext context = mock(EvaluationContext.class);
        when(context.getFieldValue(any())).thenReturn(operands);
        return context;
    }

}
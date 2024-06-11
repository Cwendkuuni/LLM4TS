import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Iterator;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = createCoreOperationRelationalExpression(">", new Expression[]{"arg1", "arg2"});
        EvalContext context = PowerMockito.mock(EvalContext.class);
        Object leftValue = Mockito.mock(Object.class);
        Object rightValue = Mockito.mock(Object.class);

        PowerMockito.when(expression.compute(leftValue, rightValue)).thenReturn(true);
        PowerMockito.when(expression.computeValue(context)).thenReturn(Boolean.TRUE);

        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testComputeValueWithFalseResult() {
        CoreOperationRelationalExpression expression = createCoreOperationRelationalExpression(">", new Expression[]{"arg1", "arg2"});
        EvalContext context = PowerMockito.mock(EvalContext.class);
        Object leftValue = Mockito.mock(Object.class);
        Object rightValue = Mockito.mock(Object.class);

        PowerMockito.when(expression.compute(leftValue, rightValue)).thenReturn(false);
        PowerMockito.when(expression.computeValue(context)).thenReturn(Boolean.FALSE);

        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Expression[0]);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Expression[0]);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompareWithGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Expression[0]);
        assertTrue(expression.evaluateCompare(1));
    }

    @Test
    public void testEvaluateCompareWithNotGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Expression[0]);
        assertFalse(expression.evaluateCompare(0));
    }

    @Test
    public void testEvaluateCompareWithEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new Expression[0]);
        assertTrue(expression.evaluateCompare(0));
    }

    @Test
    public void testEvaluateCompareWithNotEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationNotEqualExpression(new Expression[0]);
        assertFalse(expression.evaluateCompare(0));
    }

    @Test
    public void testFindMatchWithIteratorsContainingSameElement() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Expression[0]);
        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);
        Object element = Mockito.mock(Object.class);

        PowerMockito.when(leftIterator.next()).thenReturn(element);
        PowerMockito.when(rightIterator.next()).thenReturn(element);

        assertTrue(expression.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testFindMatchWithIteratorsNotMatching() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Expression[0]);
        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);
        Object leftElement = Mockito.mock(Object.class);
        Object rightElement = Mockito.mock(Object.class);

        PowerMockito.when(leftIterator.next()).thenReturn(leftElement);
        PowerMockito.when(rightIterator.next()).thenReturn(rightElement);
        PowerMockito.when(expression.compute(leftElement, rightElement)).thenReturn(false);

        assertFalse(expression.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testContainsMatchWithIteratorContainingMatchingElement() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Expression[0]);
        Iterator iterator = Mockito.mock(Iterator.class);
        Object element = Mockito.mock(Object.class);

        PowerMockito.when(iterator.next()).thenReturn(element);
        PowerMockito.when(expression.compute(iterator.next(), element)).thenReturn(true);

        assertTrue(expression.containsMatch(iterator, element));
    }

    @Test
    public void testContainsMatchWithIteratorNotContainingMatchingElement() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Expression[0]);
        Iterator iterator = Mockito.mock(Iterator.class);
        Object element = Mockito.mock(Object.class);

        PowerMockito.when(iterator.next()).thenReturn(element);
        PowerMockito.when(expression.compute(iterator.next(), element)).thenReturn(false);

        assertFalse(expression.containsMatch(iterator, element));
    }

    private CoreOperationRelationalExpression createCoreOperationRelationalExpression(String operationName, Expression[] args) {
        try {
            Class<? extends CoreOperationRelationalExpression> cls = (Class<? extends CoreOperationRelationalExpression>) Class.forName("org.apache.commons.jxpath.ri.compiler." + operationName + "Expression");
            return cls.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.ExpandedName;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Literal;
import org.apache.commons.jxpath.ri.compiler.Predicate;
import org.apache.commons.jxpath.ri.compiler.Selector;
import org.apache.commons.jxpath.ri.compiler.VariableReference;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodePointerFactory;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointerIterator;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private Expression leftExpression;
    private Expression rightExpression;
    private EvalContext mockContext;

    @Before
    public void setUp() {
        leftExpression = mock(Expression.class);
        rightExpression = mock(Expression.class);
        expression = new CoreOperationRelationalExpression(new Expression[] {leftExpression, rightExpression}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        mockContext = mock(EvalContext.class);
    }

    @Test
    public void testComputeValue_LeftAndRightReturnsNull_ReturnsFalse() {
        when(leftExpression.computeValue(mockContext)).thenReturn(null);
        when(rightExpression.computeValue(mockContext)).thenReturn(null);

        assertFalse(expression.computeValue(mockContext));
    }

    @Test
    public void testComputeValue_LeftIsNull_ReturnsFalse() {
        when(leftExpression.computeValue(mockContext)).thenReturn(null);
        when(rightExpression.computeValue(mockContext)).thenReturn("value");

        assertFalse(expression.computeValue(mockContext));
    }

    @Test
    public void testComputeValue_RightIsNull_ReturnsFalse() {
        when(leftExpression.computeValue(mockContext)).thenReturn("value");
        when(rightExpression.computeValue(mockContext)).thenReturn(null);

        assertFalse(expression.computeValue(mockContext));
    }

    @Test
    public void testComputeValue_BothLeftAndRightArePointersAndEqual_ReturnsTrue() {
        NodePointer pointer1 = mock(NodePointer.class);
        NodePointer pointer2 = mock(NodePointer.class);

        when(leftExpression.computeValue(mockContext)).thenReturn(pointer1);
        when(rightExpression.computeValue(mockContext)).thenReturn(pointer2);
        when(pointer1.compareTo(pointer2)).thenReturn(0);

        assertTrue(expression.computeValue(mockContext));
    }

    @Test
    public void testComputeValue_BothLeftAndRightArePointersAndNotEqual_ReturnsFalse() {
        NodePointer pointer1 = mock(NodePointer.class);
        NodePointer pointer2 = mock(NodePointer.class);

        when(leftExpression.computeValue(mockContext)).thenReturn(pointer1);
        when(rightExpression.computeValue(mockContext)).thenReturn(pointer2);
        when(pointer1.compareTo(pointer2)).thenReturn(1);

        assertFalse(expression.computeValue(mockContext));
    }

    @Test
    public void testComputeValue_LeftPointerIsNull_ReturnsFalse() {
        NodePointer pointer = null;

        when(leftExpression.computeValue(mockContext)).thenReturn(pointer);
        when(rightExpression.computeValue(mockContext)).thenReturn("value");

        assertFalse(expression.computeValue(mockContext));
    }

    @Test
    public void testComputeValue_RightPointerIsNull_ReturnsFalse() {
        NodePointer pointer = null;

        when(leftExpression.computeValue(mockContext)).thenReturn("value");
        when(rightExpression.computeValue(mockContext)).thenReturn(pointer);

        assertFalse(expression.computeValue(mockContext));
    }

    @Test
    public void testComputeValue_BothLeftAndRightAreIterableAndContainsMatch_ReturnsTrue() {
        List<String> leftList = Arrays.asList("value1", "value2");
        List<String> rightList = Arrays.asList("value3", "value2");

        when(leftExpression.computeValue(mockContext)).thenReturn(leftList.iterator());
        when(rightExpression.computeValue(mockContext)).thenReturn(rightList.iterator());

        assertTrue(expression.computeValue(mockContext));
    }

    @Test
    public void testComputeValue_IteratorHasNextFalse_ReturnsFalse() {
        Iterator<Object> leftIterator = mock(Iterator.class);
        Iterator<Object> rightIterator = mock(Iterator.class);

        when(leftExpression.computeValue(mockContext)).thenReturn(leftIterator);
        when(rightExpression.computeValue(mockContext)).thenReturn(rightIterator);
        when(leftIterator.hasNext()).thenReturn(false);

        assertFalse(expression.computeValue(mockContext));
    }

    @Test
    public void testComputeValue_LeftIsIteratorAndRightIsValue_ContainsMatchFalse_ReturnsFalse() {
        Iterator<Object> iterator = mock(Iterator.class);
        Object value = "value2";

        when(leftExpression.computeValue(mockContext)).thenReturn(iterator);
        when(rightExpression.computeValue(mockContext)).thenReturn(value);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("value1", "value3");

        assertFalse(expression.computeValue(mockContext));
    }

    @Test
    public void testComputeValue_RightIsIteratorAndLeftIsValue_ContainsMatchTrue_ReturnsTrue() {
        Object value = "value2";
        Iterator<Object> iterator = mock(Iterator.class);

        when(leftExpression.computeValue(mockContext)).thenReturn(value);
        when(rightExpression.computeValue(mockContext)).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("value1", "value2");

        assertTrue(expression.computeValue(mockContext));
    }

    @Test
    public void testComputeValue_LeftAndRightAreNotIterators_ReturnsResultOfEvaluateCompare() {
        when(leftExpression.computeValue(mockContext)).thenReturn(2.0);
        when(rightExpression.computeValue(mockContext)).thenReturn(3.5);

        assertFalse(expression.computeValue(mockContext));
    }

    @Test
    public void testGetPrecedence_Returns3() {
        int precedence = expression.getPrecedence();

        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric_ReturnsFalse() {
        boolean isSymmetric = expression.isSymmetric();

        assertFalse(isSymmetric);
    }

    private void assertEquals(double expected, double actual){
        org.junit.Assert.assertEquals(expected, actual, 0.000001);
    }
}
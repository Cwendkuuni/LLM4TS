import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;

import java.util.*;

public class ExpressionTest {

    private Expression expression;

    @Test
    public void testIsContextDependent_ContextDependentExpression_ReturnsTrue() {
        // Arrange
        expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        // Act
        boolean result = expression.isContextDependent();

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsContextDependent_ContextIndependentExpression_ReturnsFalse() {
        // Arrange
        expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        // Act
        boolean result = expression.isContextDependent();

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIterate_ContextDependentExpression_ReturnsValueIterator() {
        // Arrange
        expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
            public Object computeValue(EvalContext context) { return Arrays.asList("Value1", "Value2").iterator(); }
            public Object compute(EvalContext context) { return null; }
        };

        // Act
        Iterator result = expression.iterate(null);

        // Assert
        Assert.assertTrue(result instanceof Expression.ValueIterator);
    }

    @Test
    public void testIterate_ContextIndependentExpression_ReturnsValueUtilsIterator() {
        // Arrange
        expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return Arrays.asList("Value1", "Value2").iterator(); }
        };

        // Act
        Iterator result = expression.iterate(null);

        // Assert
        Assert.assertTrue(result instanceof ValueUtils.Iterator);
    }

    @Test
    public void testIteratePointers_ResultIsNull_ReturnsEmptyListIterator() {
        // Arrange
        expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        // Arrange
        Iterator result = expression.iteratePointers(null);

        // Assert
        Assert.assertTrue(result.hasNext());
        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIteratePointers_ContextDependentExpression_ReturnsEvalContextIterator() {
        // Arrange
        expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
            public Object computeValue(EvalContext context) { return new EvalContext(); }
            public Object compute(EvalContext context) { return new EvalContext(); }
        };

        // Arranges
        Iterator result = expression.iteratePointers(null);

        // Assert
        Assert.assertTrue(result instanceof EvalContext);
    }

    @Test
    public void testIteratePointers_ContextIndependentExpression_ReturnsPointerIterator() {
        // Arrange
        expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) { return Arrays.asList("Value1", "Value2").iterator(); }
            public Object compute(EvalContext context) { return null; }
        };

        // Arranges
        Iterator result = expression.iteratePointers(null);

        // Assert
        Assert.assertTrue(result instanceof Expression.PointerIterator);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove_RemoveCalled_ThrowsUnsupportedOperationException() {
        // Arrange
        expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) { return null; }
            public Object compute(EvalContext context) { return null; }
        };

        // Act
        expression.remove();

        // Exception is thrown
    }
}
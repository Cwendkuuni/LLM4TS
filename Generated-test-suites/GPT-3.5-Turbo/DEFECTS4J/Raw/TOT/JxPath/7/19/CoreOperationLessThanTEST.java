import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.Expression;

import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    private Expression arg1;
    private Expression arg2;
    private CoreOperationLessThan lessThan;

    @Before
    public void setUp() {
        arg1 = new MockExpression();
        arg2 = new MockExpression();
        lessThan = new CoreOperationLessThan(arg1, arg2);
    }

    @Test
    public void testComputeValue_LessThan_ReturnsTrue() {
        // Arrange
        MockEvalContext context = new MockEvalContext();
        context.setValue(10);
        arg1.setValue(5);
        arg2.setValue(20);

        // Act
        Object result = lessThan.computeValue(context);

        // Assert
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue_NotLessThan_ReturnsFalse() {
        // Arrange
        MockEvalContext context = new MockEvalContext();
        context.setValue(10);
        arg1.setValue(20);
        arg2.setValue(5);

        // Act
        Object result = lessThan.computeValue(context);

        // Assert
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }

    @Test
    public void testComputeValue_Equal_ReturnsFalse() {
        // Arrange
        MockEvalContext context = new MockEvalContext();
        context.setValue(10);
        arg1.setValue(10);
        arg2.setValue(10);

        // Act
        Object result = lessThan.computeValue(context);

        // Assert
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }

    @Test
    public void testComputeValue_DifferentTypes_ReturnsFalse() {
        // Arrange
        MockEvalContext context = new MockEvalContext();
        context.setValue(10);
        arg1.setValue("5");
        arg2.setValue(10);

        // Act
        Object result = lessThan.computeValue(context);

        // Assert
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }

    @Test
    public void testComputeValue_MissingArgument_ReturnsFalse() {
        // Arrange
        MockEvalContext context = new MockEvalContext();
        context.setValue(10);
        arg1.setValue(10);
        arg2.setValue(null);

        // Act
        Object result = lessThan.computeValue(context);

        // Assert
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }

    @Test(expected = Exception.class)
    public void testComputeValue_ExceptionThrown_ThrowsException() {
        // Arrange
        MockEvalContext context = new MockEvalContext();
        context.setThrowException(true);
        arg1.setValue(10);
        arg2.setValue(20);

        // Act
        lessThan.computeValue(context);

        // Assert: Exception expected
    }

    private class MockEvalContext extends EvalContext {
        private Object value;
        private boolean throwException;

        void setValue(Object value) {
            this.value = value;
        }

        @Override
        public Object getVariableValue(String variable) {
            return value;
        }

        void setThrowException(boolean throwException) {
            this.throwException = throwException;
        }

        @Override
        public double toNumeric(Object object) {
            if (throwException) {
                throw new RuntimeException("Exception thrown");
            }
            return super.toNumeric(object);
        }
    }

    private class MockExpression extends Expression {
        private Object value;

        void setValue(Object value) {
            this.value = value;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}
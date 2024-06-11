
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();

        CoreOperationEqual operation = new CoreOperationEqual(new ExpressionMock(), new ExpressionMock());
        assertTrue((Boolean) operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual operation = new CoreOperationEqual(new ExpressionMock(), new ExpressionMock());
        assertEquals("=", operation.getSymbol());
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testComputeValue_NullArguments() {
        EvalContext context = new EvalContext();

        CoreOperationEqual operation = new CoreOperationEqual(null, null);
        assertFalse((Boolean) operation.computeValue(context));
    }

    @Test
    public void testComputeValue_DifferentTypes() {
        EvalContext context = new EvalContext();

        CoreOperationEqual operation = new CoreOperationEqual(new ExpressionMock(), new ExpressionMock());
        assertFalse((Boolean) operation.computeValue(context));
    }

    @Test
    public void testComputeValue_Exception() {
        EvalContext context = new EvalContext();

        CoreOperationEqual operation = new CoreOperationEqual(new ExpressionThrowingException(), new ExpressionMock());
        assertNull(operation.computeValue(context));
    }
}

class ExpressionMock extends Expression {
    // Mock implementation of Expression for testing purposes
}

class ExpressionThrowingException extends Expression {
    @Override
    public Object computeValue(EvalContext context) {
        throw new RuntimeException("Mock exception");
    }
}

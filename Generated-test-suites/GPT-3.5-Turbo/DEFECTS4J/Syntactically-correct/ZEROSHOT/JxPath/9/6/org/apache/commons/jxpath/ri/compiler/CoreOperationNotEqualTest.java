import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();

        // Test for equal values
        context.setReturnValue(true);
        assertEquals(Boolean.FALSE, coreOperationNotEqual.computeValue(context));

        // Test for not equal values
        context.setReturnValue(false);
        assertEquals(Boolean.TRUE, coreOperationNotEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }

    private class Expression extends org.apache.commons.jxpath.ri.compiler.Expression {
        public String toString() {
            return "Expression";
        }
    }
}
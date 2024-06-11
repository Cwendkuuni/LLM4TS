// CoreOperationNotEqualTest.java

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqualConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testComputeValueTrue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueFalse() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        arg1.setValue(Boolean.TRUE);
        arg2.setValue(Boolean.FALSE);
        Object result = coreOperationNotEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(), new Expression());
        String symbol = coreOperationNotEqual.getSymbol();
        assertEquals("!=", symbol);
    }
}
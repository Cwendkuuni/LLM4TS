import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;

import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    private CoreOperationEqual operationEqual;
    private EvalContext evalContext;

    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operationEqual = new CoreOperationEqual(arg1, arg2);
        evalContext = new EvalContext();
    }

    @Test
    public void testComputeValue_equal() {
        evalContext.setValue("testValue1", "abc");
        evalContext.setValue("testValue2", "abc");
        Object result = operationEqual.computeValue(evalContext);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_notEqual() {
        evalContext.setValue("testValue1", "abc");
        evalContext.setValue("testValue2", "def");
        Object result = operationEqual.computeValue(evalContext);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_nullValues() {
        evalContext.setValue("testValue1", null);
        evalContext.setValue("testValue2", null);
        Object result = operationEqual.computeValue(evalContext);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_oneNullValue() {
        evalContext.setValue("testValue1", "abc");
        evalContext.setValue("testValue2", null);
        Object result = operationEqual.computeValue(evalContext);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operationEqual.getSymbol();
        assertEquals("=", symbol);
    }

}
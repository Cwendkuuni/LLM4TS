import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanTest {

    private CoreOperationLessThan operation;

    @Before
    public void setUp() {
        operation = new CoreOperationLessThan(null, null);
    }

    @Test
    public void testComputeValue_LessThan_ReturnTrue() {
        EvalContext context = new EvalContext(null);
        context.getJXPathContext().setVariables(null);

        double arg1 = 5;
        double arg2 = 10;

        operation.args[0] = new ExpressionStub(arg1);
        operation.args[1] = new ExpressionStub(arg2);

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LessThan_ReturnFalse() {
        EvalContext context = new EvalContext(null);
        context.getJXPathContext().setVariables(null);

        double arg1 = 10;
        double arg2 = 5;

        operation.args[0] = new ExpressionStub(arg1);
        operation.args[1] = new ExpressionStub(arg2);

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }
}

class ExpressionStub extends Expression {
    private double value;

    public ExpressionStub(double value) {
        this.value = value;
    }

    public Object computeValue(EvalContext context) {
        return value;
    }
}
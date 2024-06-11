import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();

        // Typical use cases
        context.setNodeValue("5");
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        context.setNodeValue("10");
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        context.setNodeValue("5.6");
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        // Edge cases
        context.setNodeValue("0");
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        context.setNodeValue("-10");
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        context.setNodeValue("0.000001");
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Error scenarios
        context.setNodeValue("");
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        context.setNodeValue(null);
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        context.setNodeValue("abc");
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        assertEquals(">", operation.getSymbol());
    }

}
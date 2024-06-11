import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when arg1 equals arg2
        context.setValue(arg1, "value");
        context.setValue(arg2, "value");
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // Test when arg1 does not equal arg2
        context.setValue(arg1, "value1");
        context.setValue(arg2, "value2");
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        assertEquals("!=", operation.getSymbol());
        assertNotEquals("==", operation.getSymbol());
    }
}
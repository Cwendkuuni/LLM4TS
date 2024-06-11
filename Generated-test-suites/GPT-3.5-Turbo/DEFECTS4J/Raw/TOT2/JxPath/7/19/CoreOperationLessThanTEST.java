import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(), new Expression());
        
        // Test when left operand is less than right operand
        EvalContext context1 = new EvalContext();
        context1.getJXPathContext().getContextBeanInfo().getVariables().declareVariable("var1", 5);
        context1.setNodePointer(infoSetUtil.createNodePointer(context1.getJXPathContext(), 3)); // this will convert to double
        assertTrue((Boolean) operation.computeValue(context1));
        
        // Test when left operand is greater than right operand
        EvalContext context2 = new EvalContext();
        context2.getJXPathContext().getContextBeanInfo().getVariables().declareVariable("var1", 3);
        context2.setNodePointer(infoSetUtil.createNodePointer(context2.getJXPathContext(), 8));
        assertFalse((Boolean) operation.computeValue(context2));
        
        // Test when left operand is equal to right operand
        EvalContext context3 = new EvalContext();
        context3.getJXPathContext().getContextBeanInfo().getVariables().declareVariable("var1", 10);
        context3.setNodePointer(infoSetUtil.createNodePointer(context3.getJXPathContext(), 10));
        assertFalse((Boolean) operation.computeValue(context3));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(), new Expression());
        
        assertEquals("<", operation.getSymbol());
    }
}
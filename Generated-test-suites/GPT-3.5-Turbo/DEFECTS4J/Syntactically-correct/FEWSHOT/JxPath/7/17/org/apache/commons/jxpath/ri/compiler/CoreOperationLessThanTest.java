import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new ExpressionMock(5), new ExpressionMock(3));
        EvalContext context = new EvalContextMock();
        assertTrue((Boolean) operation.computeValue(context));
        
        operation = new CoreOperationLessThan(new ExpressionMock(3), new ExpressionMock(5));
        context = new EvalContextMock();
        assertFalse((Boolean) operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new ExpressionMock(5), new ExpressionMock(3));
        assertEquals("<", operation.getSymbol());
        
        operation = new CoreOperationLessThan(new ExpressionMock(3), new ExpressionMock(5));
        assertEquals("<", operation.getSymbol());
    }
    
    private class ExpressionMock extends Expression {
        private Object value;
        
        public ExpressionMock(Object value) {
            this.value = value;
        }
        
        public Object computeValue(EvalContext context) {
           return value;
        }
    }
    
    private class EvalContextMock extends EvalContext {
        public EvalContextMock() {
            // Mock any necessary dependencies for the EvalContext
        }
        // Override any necessary methods to return mock values as needed
    }

}
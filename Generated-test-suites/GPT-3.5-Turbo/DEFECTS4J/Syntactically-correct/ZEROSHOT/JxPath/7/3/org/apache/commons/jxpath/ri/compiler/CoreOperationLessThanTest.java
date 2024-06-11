import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanTest {
    
    private CoreOperationLessThan operation;
    
    @Before
    public void setUp() {
        operation = new CoreOperationLessThan(null, null);
    }
    
    @Test
    public void testComputeValueLessThan() {
        EvalContext context = new EvalContext(null);
        context.setNodeSet(new Object[] { 1, 2, 3, 4, 5 });
        operation.args[0] = new ExpressionStub(3);
        operation.args[1] = new ExpressionStub(5);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }
    
    @Test
    public void testComputeValueNotLessThan() {
        EvalContext context = new EvalContext(null);
        context.setNodeSet(new Object[] { 1, 2, 3, 4, 5 });
        operation.args[0] = new ExpressionStub(5);
        operation.args[1] = new ExpressionStub(3);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    @Test
    public void testComputeValueEqual() {
        EvalContext context = new EvalContext(null);
        context.setNodeSet(new Object[] { 1, 2, 3, 4, 5 });
        operation.args[0] = new ExpressionStub(3);
        operation.args[1] = new ExpressionStub(3);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        assertEquals("<", operation.getSymbol());
    }
    
    private class ExpressionStub extends Expression {
        
        private Object value;
        
        public ExpressionStub(Object value) {
            this.value = value;
        }
        
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}
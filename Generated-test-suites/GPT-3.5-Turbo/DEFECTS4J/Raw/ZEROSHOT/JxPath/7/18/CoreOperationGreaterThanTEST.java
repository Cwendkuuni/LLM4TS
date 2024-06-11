import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(new ExpressionMock(), new ExpressionMock());
        
        EvalContext context = new EvalContext();
        
        context.setNodeSet(new NodeSetMock());
        
        Object result = op.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(new ExpressionMock(), new ExpressionMock());
        
        String symbol = op.getSymbol();
        
        assertEquals(">", symbol);
    }
    
    private class ExpressionMock extends Expression {
        
        public Object computeValue(EvalContext context) {
            return null;
        }
        
    }
    
    private class NodeSetMock extends NodeSet {
        
        public NodeSetMock() {
            super(null, null, 0);
        }
        
        public boolean nextNode() {
            return false;
        }
        
        public Object getNode() {
            return null;
        }
        
    }
}
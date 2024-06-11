import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    
    @Test
    public void testComputeValueNotEqual() {
        CoreOperationNotEqual op = new CoreOperationNotEqual(new ExpressionMock(), new ExpressionMock());
        EvalContext context = new EvalContext();
        context.setContextNode(new NodeMock());
        
        Object result = op.computeValue(context);
        
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValueEqual() {
        CoreOperationNotEqual op = new CoreOperationNotEqual(new ExpressionMock(), new ExpressionMock());
        EvalContext context = new EvalContext();
        context.setContextNode(new NodeMock());
        
        Object result = op.computeValue(context);
        
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual op = new CoreOperationNotEqual(new ExpressionMock(), new ExpressionMock());
        
        String symbol = op.getSymbol();
        
        assertEquals("!=", symbol);
    }
}

class ExpressionMock extends Expression {
    public ExpressionMock() {
        super(null, null);
    }
    
    public Object computeValue(EvalContext context) {
        return null;
    }
}

class NodeMock extends Node {
    public NodeMock() {
        super(null, null);
    }
    
    public String getNodeName() {
        return null;
    }
}
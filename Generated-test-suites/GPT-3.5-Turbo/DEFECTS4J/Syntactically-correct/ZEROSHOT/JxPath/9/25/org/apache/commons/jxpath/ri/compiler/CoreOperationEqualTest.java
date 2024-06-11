import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationEqual equal = new CoreOperationEqual(null, null);
        
        EvalContext context = new EvalContext(null);
        Object result = equal.computeValue(context);
        assertNull(result);
        
        context = new EvalContext(null);
        context.getExpressionContext().setContextNode(new Object());
        result = equal.computeValue(context);
        assertNull(result);
        
        context = new EvalContext(null);
        context.getExpressionContext().setContextNode(new Object());
        context.setNodeSet(new Object());
        result = equal.computeValue(context);
        assertNull(result);
        
        context = new EvalContext(null);
        context.getExpressionContext().setContextNode(new Object());
        context.setNodeSet(new Object());
        context.setPosition(1);
        context.setSize(1);
        result = equal.computeValue(context);
        assertNull(result);
        
        context = new EvalContext(null);
        context.getExpressionContext().setContextNode(new Object());
        context.setNodeSet(new Object());
        context.setPosition(1);
        context.setSize(2);
        result = equal.computeValue(context);
        assertNull(result);
        
        context = new EvalContext(null);
        context.getExpressionContext().setContextNode(new Object());
        context.setNodeSet(new Object());
        context.setContextPosition(1);
        context.setContextSize(1);
        result = equal.computeValue(context);
        assertNull(result);
        
        context = new EvalContext(null);
        context.getExpressionContext().setContextNode(new Object());
        context.setNodeSet(new Object());
        context.setContextPosition(1);
        context.setContextSize(2);
        result = equal.computeValue(context);
        assertNull(result);
        
        context = new EvalContext(null);
        context.getExpressionContext().setContextNode(new Object());
        context.setNodeSet(new Object());
        context.setPosition(1);
        context.setSize(1);
        context.setContextPosition(1);
        context.setContextSize(1);
        result = equal.computeValue(context);
        assertNull(result);
        
        context = new EvalContext(null);
        context.getExpressionContext().setContextNode(new Object());
        context.setNodeSet(new Object());
        context.setPosition(1);
        context.setSize(1);
        context.setContextPosition(1);
        context.setContextSize(2);
        result = equal.computeValue(context);
        assertNull(result);
        
        context = new EvalContext(null);
        context.getExpressionContext().setContextNode(new Object());
        context.setNodeSet(new Object());
        context.setPosition(1);
        context.setSize(2);
        context.setContextPosition(1);
        context.setContextSize(1);
        result = equal.computeValue(context);
        assertNull(result);
        
        context = new EvalContext(null);
        context.getExpressionContext().setContextNode(new Object());
        context.setNodeSet(new Object());
        context.setPosition(1);
        context.setSize(2);
        context.setContextPosition(1);
        context.setContextSize(2);
        result = equal.computeValue(context);
        assertNull(result);
        
        context = new EvalContext(null);
        context.getExpressionContext().setContextNode(new Object());
        context.setNodeSet(new Object());
        context.setPosition(1);
        context.setSize(2);
        context.setContextPosition(2);
        context.setContextSize(2);
        result = equal.computeValue(context);
        assertNull(result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual equal = new CoreOperationEqual(null, null);
        String symbol = equal.getSymbol();
        assertEquals("=", symbol);
    }
}
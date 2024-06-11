
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext context3 = new EvalContext();
        EvalContext[] contexts = {context1, context2, context3};
        
        UnionContext unionContext = new UnionContext(null, contexts);
        
        int result = unionContext.getDocumentOrder();
        
        assertEquals(1, result);
    }
    
    @Test
    public void testSetPosition() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext context3 = new EvalContext();
        EvalContext[] contexts = {context1, context2, context3};
        
        UnionContext unionContext = new UnionContext(null, contexts);
        
        boolean result = unionContext.setPosition(1);
        
        assertTrue(result);
    }
    
    @Test
    public void testSetPosition_WhenNotPrepared() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext context3 = new EvalContext();
        EvalContext[] contexts = {context1, context2, context3};
        
        UnionContext unionContext = new UnionContext(null, contexts);
        
        boolean result = unionContext.setPosition(2);
        
        assertFalse(result);
    }
    
    @Test
    public void testSetPosition_WhenPrepared() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext context3 = new EvalContext();
        EvalContext[] contexts = {context1, context2, context3};
        
        UnionContext unionContext = new UnionContext(null, contexts);
        unionContext.setPosition(1);
        
        boolean result = unionContext.setPosition(2);
        
        assertFalse(result);
    }
    
    @Test
    public void testSetPosition_WhenPositionGreaterThanContextLength() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext context3 = new EvalContext();
        EvalContext[] contexts = {context1, context2, context3};
        
        UnionContext unionContext = new UnionContext(null, contexts);
        
        boolean result = unionContext.setPosition(4);
        
        assertFalse(result);
    }
}

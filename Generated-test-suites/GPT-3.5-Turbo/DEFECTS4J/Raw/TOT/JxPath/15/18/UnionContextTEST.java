import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        
        UnionContext unionContext = new UnionContext(null, contexts);
        
        int expected = 1;
        int actual = unionContext.getDocumentOrder();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPosition() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        
        UnionContext unionContext = new UnionContext(null, contexts);
        
        boolean expected = true;
        boolean actual = unionContext.setPosition(1);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPosition_NotPrepared() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        
        UnionContext unionContext = new UnionContext(null, contexts);
        
        boolean expected = false;
        boolean actual = unionContext.setPosition(1);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPosition_EmptyContexts() {
        EvalContext[] contexts = {};
        
        UnionContext unionContext = new UnionContext(null, contexts);
        
        boolean expected = false;
        boolean actual = unionContext.setPosition(1);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPosition_PositionEqualToZero() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        
        UnionContext unionContext = new UnionContext(null, contexts);
        
        boolean expected = false;
        boolean actual = unionContext.setPosition(0);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetPosition_PositionGreaterThanSize() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        EvalContext[] contexts = {context1, context2};
        
        UnionContext unionContext = new UnionContext(null, contexts);
        
        boolean expected = false;
        boolean actual = unionContext.setPosition(3);
        
        assertEquals(expected, actual);
    }
}
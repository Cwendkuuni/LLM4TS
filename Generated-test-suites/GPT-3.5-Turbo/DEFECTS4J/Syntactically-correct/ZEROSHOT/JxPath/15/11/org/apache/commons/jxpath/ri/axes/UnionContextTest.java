import org.junit.Test;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

import static org.junit.Assert.*;

public class UnionContextTest {
    
    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        int result = unionContext.getDocumentOrder();
        
        assertEquals(1, result);
    }
    
    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        boolean result = unionContext.setPosition(1);
        
        assertTrue(result);
    }
    
    @Test
    public void testSetPositionNotPrepared() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        boolean result = unionContext.setPosition(1);
        
        assertTrue(unionContext.isPrepared());
        assertTrue(result);
    }
    
    @Test
    public void testSetPositionPrepared() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.setPrepared(true);
        
        boolean result = unionContext.setPosition(1);
        
        assertFalse(unionContext.isPrepared());
        assertTrue(result);
    }
    
    @Test
    public void testSetPositionWithNodeSet() {
        EvalContext parentContext = new EvalContext();
        BasicNodeSet nodeSet = new BasicNodeSet();
        UnionContext unionContext = new UnionContext(parentContext, nodeSet);
        
        boolean result = unionContext.setPosition(1);
        
        assertTrue(result);
    }
    
    @Test
    public void testSetPositionWithNodeSetNotPrepared() {
        EvalContext parentContext = new EvalContext();
        BasicNodeSet nodeSet = new BasicNodeSet();
        UnionContext unionContext = new UnionContext(parentContext, nodeSet);
        
        boolean result = unionContext.setPosition(1);
        
        assertTrue(unionContext.isPrepared());
        assertTrue(result);
    }
    
    @Test
    public void testSetPositionWithNodeSetPrepared() {
        EvalContext parentContext = new EvalContext();
        BasicNodeSet nodeSet = new BasicNodeSet();
        UnionContext unionContext = new UnionContext(parentContext, nodeSet);
        unionContext.setPrepared(true);
        
        boolean result = unionContext.setPosition(1);
        
        assertFalse(unionContext.isPrepared());
        assertTrue(result);
    }
    
}
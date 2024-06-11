import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class UnionContextTest {
    @Test
    public void testGetDocumentOrder() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        
        assertEquals(1, unionContext.getDocumentOrder());
        
        EvalContext context3 = new EvalContext();
        UnionContext unionContext2 = new UnionContext(null, new EvalContext[]{context1, context2, context3});
        
        assertEquals(1, unionContext2.getDocumentOrder());
    }
    
    @Test
    public void testSetPosition() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        
        assertTrue(unionContext.setPosition(1));
        
        EvalContext context3 = new EvalContext();
        UnionContext unionContext2 = new UnionContext(null, new EvalContext[]{context1, context2, context3});
        
        assertTrue(unionContext2.setPosition(1));
    }
    
    @Test
    public void testSetPositionUnprepared() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        
        assertFalse(unionContext.setPosition(1));
        
        unionContext.setPosition(1);
        
        assertTrue(unionContext.setPosition(1));
    }
    
    @Test
    public void testSetPositionInvalidPosition() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        
        assertFalse(unionContext.setPosition(0));
        assertFalse(unionContext.setPosition(-1));
        assertFalse(unionContext.setPosition(2));
    }
    
    @Test
    public void testSetPositionSingleNodeSet() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        unionContext.getNodeSet().add(new NodePointer());
        
        assertTrue(unionContext.setPosition(1));
    }
    
    @Test
    public void testSetPositionSingleNodeSetInvalidPosition() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        unionContext.getNodeSet().add(new NodePointer());
        
        assertFalse(unionContext.setPosition(0));
        assertFalse(unionContext.setPosition(-1));
        assertFalse(unionContext.setPosition(2));
    }
    
    @Test
    public void testSetPositionWithDuplicatePointers() {
        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        
        context1.getNodeSet().add(pointer1);
        context1.getNodeSet().add(pointer2);
        
        assertTrue(unionContext.setPosition(1));
        
        context2.getNodeSet().add(pointer1);
        
        assertFalse(unionContext.setPosition(1));
    }
}
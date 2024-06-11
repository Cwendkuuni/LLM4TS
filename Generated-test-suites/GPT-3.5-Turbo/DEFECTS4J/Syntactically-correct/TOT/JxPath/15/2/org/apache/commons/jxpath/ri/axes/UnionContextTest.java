import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {
    
    // Test constructor
    @Test
    public void testConstructor() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        assertNotNull(unionContext);
        assertEquals(parentContext, unionContext.getParentContext());
        assertNotNull(unionContext.getNodeSet());
        assertEquals(BasicNodeSet.class, unionContext.getNodeSet().getClass());
        assertEquals(contexts, unionContext.getContexts());
        assertFalse(unionContext.isPrepared());
    }
    
    // Test getDocumentOrder when contexts length > 1
    @Test
    public void testGetDocumentOrderMultipleContexts() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[2];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        int documentOrder = unionContext.getDocumentOrder();
        
        assertEquals(1, documentOrder);
    }
    
    // Test getDocumentOrder when contexts length is not > 1
    @Test
    public void testGetDocumentOrderSingleContext() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[1];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        int documentOrder = unionContext.getDocumentOrder();
        
        assertEquals(0, documentOrder);
    }
    
    // Test setPosition when not prepared
    @Test
    public void testSetPositionNotPrepared() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[1];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        boolean positionSet = unionContext.setPosition(0);
        
        assertTrue(positionSet);
        assertTrue(unionContext.isPrepared());
    }
    
    // Test setPosition when prepared
    @Test
    public void testSetPositionPrepared() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[1];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.setPosition(0);
        
        boolean positionSet = unionContext.setPosition(1);
        
        assertTrue(positionSet);
        assertTrue(unionContext.isPrepared());
    }
    
    // Test setPosition when position is less than zero
    @Test
    public void testSetPositionInvalidPosition() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[1];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        boolean positionSet = unionContext.setPosition(-1);
        
        assertFalse(positionSet);
        assertFalse(unionContext.isPrepared());
    }
    
    // Test setPosition when position is greater than the size of the node set
    @Test
    public void testSetPositionOutOfRangePosition() {
        EvalContext parentContext = new EvalContext();
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add(new NodePointer());
        nodeSet.add(new NodePointer());
        parentContext.setNodeSet(nodeSet);
        EvalContext[] contexts = new EvalContext[1];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.setPosition(0);
        
        boolean positionSet = unionContext.setPosition(3);
        
        assertFalse(positionSet);
        assertFalse(unionContext.isPrepared());
    }
    
    // Test setPosition when position is valid but no node set is present
    @Test
    public void testSetPositionNoNodeSet() {
        EvalContext parentContext = new EvalContext();
        EvalContext[] contexts = new EvalContext[1];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        boolean positionSet = unionContext.setPosition(0);
        
        assertFalse(positionSet);
        assertFalse(unionContext.isPrepared());
    }

}
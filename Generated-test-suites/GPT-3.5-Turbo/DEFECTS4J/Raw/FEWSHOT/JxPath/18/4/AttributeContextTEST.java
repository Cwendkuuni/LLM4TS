import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class AttributeContextTest {
    
    @Test
    public void testGetCurrentNodePointer() {
        NodeTest nodeTest = new NodeNameTest();
        EvalContext parentContext = new EvalContext();
        AttributeContext context = new AttributeContext(parentContext, nodeTest);
        
        NodePointer currentNodePointer = context.getCurrentNodePointer();
        
        assertNull(currentNodePointer);
    }
    
    @Test
    public void testReset() {
        NodeTest nodeTest = new NodeNameTest();
        EvalContext parentContext = new EvalContext();
        AttributeContext context = new AttributeContext(parentContext, nodeTest);
        
        context.reset();
        
        assertFalse(context.setStarted);
        assertNull(context.iterator);
        assertEquals(0, context.getCurrentPosition());
    }
    
    @Test
    public void testSetPosition() {
        NodeTest nodeTest = new NodeNameTest();
        EvalContext parentContext = new EvalContext();
        AttributeContext context = new AttributeContext(parentContext, nodeTest);
        
        boolean result = context.setPosition(3);
        
        assertFalse(result);
    }
    
    @Test
    public void testNextNode() {
        NodeNameTest nodeTest = new NodeNameTest();
        EvalContext parentContext = new EvalContext();
        AttributeContext context = new AttributeContext(parentContext, nodeTest);
        
        boolean result = context.nextNode();
        
        assertFalse(result);
    }
}
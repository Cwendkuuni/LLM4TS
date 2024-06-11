import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeContextTest {

    private AttributeContext attributeContext;
    private EvalContext parentContext;
    
    @Before
    public void setUp() {
        NodeTest nodeTest = new NodeNameTest(new QName("attributeName"));
        parentContext = new EvalContext(null);
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }
    
    @Test
    public void testGetCurrentNodePointer() {
        NodePointer expectedResult = null;
        assertEquals(expectedResult, attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testReset() {
        attributeContext.reset();
        
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        super.reset();
    }
    
    @Test
    public void testSetPosition() {
        int position = 2;
        boolean expectedResult = false;
        boolean actualResult = attributeContext.setPosition(position);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testNextNode_NodeNameTest() {
        attributeContext.setStarted = false;
        attributeContext.iterator = null;
        attributeContext.currentNodePointer = null;
        
        boolean expectedResult = false;
        boolean actualResult = attributeContext.nextNode();
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testNextNode_IteratorNull() {
        attributeContext.setStarted = true;
        attributeContext.iterator = null;
        attributeContext.currentNodePointer = null;
        
        boolean expectedResult = false;
        boolean actualResult = attributeContext.nextNode();
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testNextNode_SetPositionFalse() {
        attributeContext.setStarted = true;
        attributeContext.iterator = new NodeIterator();
        attributeContext.currentNodePointer = null;
        
        boolean expectedResult = false;
        boolean actualResult = attributeContext.nextNode();
        
        assertEquals(expectedResult, actualResult);
    }
}
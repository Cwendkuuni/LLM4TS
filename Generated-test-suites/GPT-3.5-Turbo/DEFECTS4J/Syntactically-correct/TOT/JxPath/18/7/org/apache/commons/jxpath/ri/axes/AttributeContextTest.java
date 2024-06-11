import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class AttributeContextTest {
    
    private EvalContext parentContext;
    private AttributeContext attributeContext;
    private NodeTest nodeTest;
    
    @Before
    public void setUp() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest(new QName("attributeName"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }
    
    @Test
    public void testGetCurrentNodePointer() {
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);
    }
    
    @Test
    public void testReset() {
        attributeContext.reset();
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);
    }
    
    @Test
    public void testSetPosition() {
        assertFalse(attributeContext.setPosition(1));
    }
    
    @Test
    public void testNextNode() {
        assertFalse(attributeContext.nextNode());
    }
}
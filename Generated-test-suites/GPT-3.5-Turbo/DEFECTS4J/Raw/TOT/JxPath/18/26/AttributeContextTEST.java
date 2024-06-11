import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class AttributeContextTest {

    private EvalContext parentContext;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext();
        NodeNameTest nodeNameTest = new NodeNameTest("attributeName");
        attributeContext = new AttributeContext(parentContext, nodeNameTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.setPosition(1));
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeContext.setPosition(-1));
        assertTrue(attributeContext.setPosition(0));
        assertTrue(attributeContext.setPosition(1));
        assertFalse(attributeContext.setPosition(2));
    }

    @Test
    public void testNextNode() {
        assertFalse(attributeContext.nextNode()); 
        assertTrue(attributeContext.setPosition(1));
        assertFalse(attributeContext.nextNode()); 
    }

}
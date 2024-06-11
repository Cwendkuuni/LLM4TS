import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QualifiedName;
import org.apache.commons.jxpath.ri.axes.EvalContext;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeNameTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setup() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest(new QualifiedName("attribute")); 
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testConstructor() {
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.setPosition(1);
        attributeContext.reset();

        assertEquals(0, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
    }

    @Test
    public void testSetPosition() {
        assertTrue(attributeContext.setPosition(0));
        assertEquals(0, attributeContext.getCurrentPosition());

        assertFalse(attributeContext.setPosition(-1));
        assertEquals(0, attributeContext.getCurrentPosition());

        assertFalse(attributeContext.setPosition(1));
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode() {
        assertTrue(attributeContext.nextNode());
        assertEquals(1, attributeContext.getCurrentPosition());
        assertTrue(attributeContext.isSetStarted());
        assertNull(attributeContext.getCurrentNodePointer());

        attributeContext.setParentContext(null);
        assertFalse(attributeContext.nextNode());
        assertEquals(1, attributeContext.getCurrentPosition());

        attributeContext.setParentContext(new EvalContext());
        assertFalse(attributeContext.nextNode());
        assertEquals(1, attributeContext.getCurrentPosition());

        attributeContext.setStarted(false);
        nodeTest = new NodeNameTest(new QualifiedName("test"));
        attributeContext.setNodeTest(nodeTest);
        assertFalse(attributeContext.nextNode());
        assertEquals(1, attributeContext.getCurrentPosition());
    }
}
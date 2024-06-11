import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributeContextTest {

    private AttributeContext attributeContext;
    private EvalContext parentContext;
    private NodeTest nodeTest;

    @Before
    public void setUp() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest(new QName("attributeName"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
        assertTrue(attributeContext.getPosition() == 0);
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeContext.setPosition(1));
    }

    @Test
    public void testSetPosition_withReset() {
        attributeContext.setPosition(2);
        attributeContext.setPosition(1);
        assertFalse(attributeContext.isSetStarted());
        assertTrue(attributeContext.getPosition() == 0);
    }

    @Test
    public void testSetPosition_withNextNode() {
        attributeContext.setPosition(1);
        assertTrue(attributeContext.setPosition(1));
    }

    @Test
    public void testNextNode_withoutSetStarted() {
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_withNullIterator() {
        attributeContext.setStarted(true);
        assertFalse(attributeContext.nextNode());
    }
}
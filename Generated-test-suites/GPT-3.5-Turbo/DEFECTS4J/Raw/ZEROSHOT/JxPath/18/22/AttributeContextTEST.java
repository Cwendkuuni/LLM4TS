import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeContextTest {
    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null);
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
        assertFalse(attributeContext.setPosition(1));
        assertNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPosition() {
        attributeContext.reset();
        assertTrue(attributeContext.setPosition(1));
    }

    @Test
    public void testSetPositionInvalid() {
        attributeContext.reset();
        assertFalse(attributeContext.setPosition(2));
    }

    @Test
    public void testNextNode() {
        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
    }
}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {
    private AttributeContext attributeContext;
    private EvalContext parentContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null);
        attributeContext = new AttributeContext(parentContext, new NodeNameTest("attribute"));
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
        assertNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeContext.setPosition(1));
        assertFalse(attributeContext.setPosition(2));
        assertFalse(attributeContext.setPosition(3));
        assertFalse(attributeContext.setPosition(4));
    }

    @Test
    public void testNextNode() {
        assertFalse(attributeContext.nextNode());
    }
}
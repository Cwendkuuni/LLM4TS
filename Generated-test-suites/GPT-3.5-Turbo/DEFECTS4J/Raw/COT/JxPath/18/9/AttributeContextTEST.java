
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAttributeContext {

    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        attributeContext = new AttributeContext(null, null);
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer result = attributeContext.getCurrentNodePointer();
        assertNull(result);
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertEquals(false, attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        attributeContext.setPosition(0);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode() {
        boolean result = attributeContext.nextNode();
        assertEquals(false, result);
    }

}

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.QName;

public class AttributeContextTest {

    @Test
    public void testAttributeContext() {
        NodeNameTest nodeNameTest = new NodeNameTest(new QName("test"));
        AttributeContext attributeContext = new AttributeContext(null, nodeNameTest);
        
        assertNotNull(attributeContext);
        assertEquals(null, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        NodeNameTest nodeNameTest = new NodeNameTest(new QName("test"));
        AttributeContext attributeContext = new AttributeContext(null, nodeNameTest);
        
        assertNotNull(attributeContext);
        assertEquals(null, attributeContext.getCurrentNodePointer());
        
        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPosition() {
        NodeNameTest nodeNameTest = new NodeNameTest(new QName("test"));
        AttributeContext attributeContext = new AttributeContext(null, nodeNameTest);
       
        assertFalse(attributeContext.setPosition(1));
    }

    @Test
    public void testNextNode() {
        NodeNameTest nodeNameTest = new NodeNameTest(new QName("test"));
        AttributeContext attributeContext = new AttributeContext(null, nodeNameTest);
        
        assertFalse(attributeContext.nextNode());
    }

}
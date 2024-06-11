
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {
        // Initialize
        Element element = new Element("testElement");
        element.setAttribute("attribute1", "value1");
        element.setAttribute("attribute2", "value2");
        JXPathContext context = JXPathContext.newContext(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context, new QName("*"));

        // Test
        NodePointer nodePointer = iterator.getNodePointer();

        // Verify
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("testElement", nodePointer.getNode().getName());
    }

    @Test
    public void testGetPosition() {
        // Initialize
        Element element = new Element("testElement");
        element.setAttribute("attribute1", "value1");
        element.setAttribute("attribute2", "value2");
        JXPathContext context = JXPathContext.newContext(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context, new QName("*"));

        // Test
        int position = iterator.getPosition();

        // Verify
        Assert.assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        // Initialize
        Element element = new Element("testElement");
        element.setAttribute("attribute1", "value1");
        element.setAttribute("attribute2", "value2");
        JXPathContext context = JXPathContext.newContext(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context, new QName("*"));

        // Test
        boolean success = iterator.setPosition(1);

        // Verify
        Assert.assertTrue(success);
        Assert.assertEquals(1, iterator.getPosition());
    }
}


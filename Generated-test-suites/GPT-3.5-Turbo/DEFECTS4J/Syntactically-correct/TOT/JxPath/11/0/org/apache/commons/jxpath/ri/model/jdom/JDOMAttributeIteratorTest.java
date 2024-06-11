import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {
        // Test when parent node is an Element and attribute name is not wildcard
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        NodePointer parent = new NodePointer(element);
        QName attributeName = new QName(null, "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value1", nodePointer.getValue());

        // Test when parent node is an Element and attribute name is wildcard
        attributeName = new QName(null, "*");
        iterator = new JDOMAttributeIterator(parent, attributeName);

        nodePointer = iterator.getNodePointer();

        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value1", nodePointer.getValue());

        // Test when attribute name has a prefix and it maps to XML namespace
        element = new Element("test", Namespace.XML_NAMESPACE);
        element.setAttribute("xml:foo", "value");
        parent = new NodePointer(element);
        attributeName = new QName("xml", "foo");
        iterator = new JDOMAttributeIterator(parent, attributeName);

        nodePointer = iterator.getNodePointer();

        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value", nodePointer.getValue());

        // Test when attribute name has a prefix and it doesn't exist in the namespace context
        element = new Element("test");
        element.setAttribute("prefix:foo", "value");
        parent = new NodePointer(element);
        attributeName = new QName("prefix", "foo");
        iterator = new JDOMAttributeIterator(parent, attributeName);

        nodePointer = iterator.getNodePointer();

        Assert.assertNull(nodePointer);
    }

    @Test
    public void testGetPositionAndSetPosition() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        NodePointer parent = new NodePointer(element);
        QName attributeName = new QName(null, "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        // Test initial position
        Assert.assertEquals(0, iterator.getPosition());

        // Test setting valid position
        Assert.assertTrue(iterator.setPosition(2));
        Assert.assertEquals(2, iterator.getPosition());

        // Test setting invalid position
        Assert.assertFalse(iterator.setPosition(10));
        Assert.assertEquals(2, iterator.getPosition());
    }
}
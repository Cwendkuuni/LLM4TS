
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;

    @Before
    public void setup() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        parent = new NodePointer(element);
    }

    @Test
    public void testGetNodePointer_SingleAttribute_ReturnsAttributeNode() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(Attribute.class, nodePointer.getNodeType());
    }

    @Test
    public void testGetNodePointer_NonExistingAttribute_ReturnsNull() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("nonExistingAttr"));
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testGetNodePointer_WithNamespace_ReturnsAttributeNodeWithNamespace() {
        Namespace namespace = Namespace.getNamespace("prefix", "http://example.com");
        Element element = new Element("test", namespace);
        element.setAttribute("attr1", "value1", namespace);
        parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("prefix", "attr1"));
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(Attribute.class, nodePointer.getNodeType());
        Assert.assertEquals(namespace, ((Attribute) nodePointer.getNode()).getNamespace());
    }

    @Test
    public void testGetNodePointer_MultipleAttributes_ReturnsAttributeNodes() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
        NodePointer nodePointer1 = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer1);
        Assert.assertEquals(Attribute.class, nodePointer1.getNodeType());
        Assert.assertEquals("attr1", ((Attribute) nodePointer1.getNode()).getName());

        iterator.setPosition(2);
        NodePointer nodePointer2 = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer2);
        Assert.assertEquals(Attribute.class, nodePointer2.getNodeType());
        Assert.assertEquals("attr2", ((Attribute) nodePointer2.getNode()).getName());
    }

    @Test
    public void testGetNodePointer_PositionOutOfBounds_ReturnsNull() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
        Assert.assertFalse(iterator.setPosition(0));
        Assert.assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testGetPosition_PositionInRange_ReturnsPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testGetPosition_InvalidPosition_ReturnsZero() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
        iterator.setPosition(0);
        Assert.assertEquals(0, iterator.getPosition());
    }
}

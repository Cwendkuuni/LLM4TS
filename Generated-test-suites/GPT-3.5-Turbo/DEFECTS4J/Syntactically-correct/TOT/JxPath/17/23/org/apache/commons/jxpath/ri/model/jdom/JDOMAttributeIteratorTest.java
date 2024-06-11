import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private static class MockNodePointer implements NodePointer {
        private Object node;

        MockNodePointer(Object node) {
            this.node = node;
        }

        @Override
        public Object getNode() {
            return node;
        }
    }

    private JDOMAttributeIterator jdomAttributeIterator;

    @Before
    public void setup() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("xml", Namespace.XML_NAMESPACE, "attr");

        jdomAttributeIterator = new JDOMAttributeIterator(new MockNodePointer(element), new QName("attr"));
    }

    @Test
    public void testGetPosition_WithValidAttributes_ReturnsPosition() {
        Assert.assertEquals(0, jdomAttributeIterator.getPosition());

        jdomAttributeIterator.setPosition(1);
        Assert.assertEquals(1, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testGetPosition_WithInvalidAttributes_ReturnsZeroPosition() {
        jdomAttributeIterator.setPosition(5);
        Assert.assertEquals(0, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_WithValidPosition_ReturnsTrue() {
        Assert.assertTrue(jdomAttributeIterator.setPosition(3));
    }

    @Test
    public void testSetPosition_WithInvalidPosition_ReturnsFalse() {
        Assert.assertFalse(jdomAttributeIterator.setPosition(5));
    }

    @Test
    public void testGetNodePointer_WithValidPosition_ReturnsNodePointer() {
        jdomAttributeIterator.setPosition(1);
        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(Attribute.class, nodePointer.getNode().getClass());
        Assert.assertEquals("attr1", ((Attribute) nodePointer.getNode()).getName());
    }

    @Test
    public void testGetNodePointer_WithInvalidPosition_ReturnsNullNodePointer() {
        jdomAttributeIterator.setPosition(5);
        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testGetNodePointer_WithZeroPosition_ReturnsFirstNodePointer() {
        jdomAttributeIterator.setPosition(0);
        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(Attribute.class, nodePointer.getNode().getClass());
        Assert.assertEquals("attr1", ((Attribute) nodePointer.getNode()).getName());
    }

    @Test
    public void testGetNodePointer_AfterSettingPosition_ReturnsCorrectNodePointer() {
        jdomAttributeIterator.setPosition(2);
        jdomAttributeIterator.setPosition(1);
        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(Attribute.class, nodePointer.getNode().getClass());
        Assert.assertEquals("attr1", ((Attribute) nodePointer.getNode()).getName());
    }

    @Test
    public void testGetNodePointer_WithNamespaceAttribute_ReturnsCorrectNodePointer() {
        jdomAttributeIterator.setPosition(3);
        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(Attribute.class, nodePointer.getNode().getClass());
        Assert.assertEquals("attr", ((Attribute) nodePointer.getNode()).getName());
        Assert.assertEquals(Namespace.XML_NAMESPACE, ((Attribute) nodePointer.getNode()).getNamespace());
    }

    @Test
    public void testGetNodePointer_WithInvalidNamespaceAttribute_ReturnsNullNodePointer() {
        jdomAttributeIterator.setPosition(3);
        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(Attribute.class, nodePointer.getNode().getClass());
        Assert.assertEquals("attr", ((Attribute) nodePointer.getNode()).getName());
        Assert.assertNotEquals(Namespace.getNamespace("invalidNamespace"), ((Attribute) nodePointer.getNode()).getNamespace());
    }

    @Test
    public void testGetNodePointer_WithoutNamespace_ReturnsFirstNodePointer() {
        JDOMAttributeIterator jdomAttributeIteratorWithoutNamespace = new JDOMAttributeIterator(new MockNodePointer(new Element("testElement")), new QName("attr"));
        jdomAttributeIteratorWithoutNamespace.setPosition(1);
        NodePointer nodePointer = jdomAttributeIteratorWithoutNamespace.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(Attribute.class, nodePointer.getNode().getClass());
        Assert.assertEquals("attr1", ((Attribute) nodePointer.getNode()).getName());
    }

    @Test
    public void testGetNodePointer_WithoutNamespaceAfterSettingPosition_ReturnsCorrectNodePointer() {
        JDOMAttributeIterator jdomAttributeIteratorWithoutNamespace = new JDOMAttributeIterator(new MockNodePointer(new Element("testElement")), new QName("attr"));
        jdomAttributeIteratorWithoutNamespace.setPosition(2);
        jdomAttributeIteratorWithoutNamespace.setPosition(1);
        NodePointer nodePointer = jdomAttributeIteratorWithoutNamespace.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(Attribute.class, nodePointer.getNode().getClass());
        Assert.assertEquals("attr1", ((Attribute) nodePointer.getNode()).getName());
    }

    @Test
    public void testGetNodePointer_WithoutAttributes_ReturnsNullNodePointer() {
        Element elementWithoutAttributes = new Element("testElement");
        jdomAttributeIterator = new JDOMAttributeIterator(new MockNodePointer(elementWithoutAttributes), new QName("attr"));
        jdomAttributeIterator.setPosition(1);
        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }
}
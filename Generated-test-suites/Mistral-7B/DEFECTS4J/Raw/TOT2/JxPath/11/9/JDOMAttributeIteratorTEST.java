import org.apache.commons.jxpath.ri.model.JDOMDocument;
import org.apache.commons.jxpath.ri.model.JDOMNodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMNamespace;
import org.apache.commons.jxpath.ri.model.jdom.QName;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.NamespaceException;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        // Add assertions based on the expected behavior
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Attribute expectedAttribute = new Attribute("testAttribute", "testValue");
        NodePointer nodePointer = iterator.getNodePointer();
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Assert.assertSame(expectedAttribute, attributePointer.getAttribute());
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertFalse(iterator.setPosition(-1));
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testConstructorWithNullParent() {
        QName name = new QName("test:attribute");
        Assert.assertNull(new JDOMAttributeIterator(null, name));
    }

    @Test
    public void testConstructorWithNullName() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        Assert.assertNull(new JDOMAttributeIterator(parent, null));
    }

    @Test
    public void testConstructorWithEmptyList() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);
        Assert.assertTrue(iterator.attributes.isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNodePointerOutOfBounds() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator.setPosition(2);
        iterator.getNodePointer();
    }

    @Test
    public void testGetNodePointerWithNullIterator() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator = null;
        Attribute expectedAttribute = new Attribute("testAttribute", "testValue");
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testGetPositionWithNullIterator() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator = null;
        Assert.assertNull(iterator);
    }

    @Test
    public void testSetPositionWithNullIterator() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator = null;
        Assert.assertFalse(iterator.setPosition(-1));
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithNegativePosition() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator.setPosition(-1);
    }

    @Test
    public void testGetNodePointerWithInvalidParent() {
        QName name = new QName("test:attribute");
        NodePointer parent = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNull(iterator);
    }

    @Test
    public void testGetNodePointerWithEmptyList() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);
        Assert.assertTrue(iterator.attributes.isEmpty());
    }

    @Test
    public void testGetNodePointerWithInvalidName() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        QName name = new QName("", "test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);
        Assert.assertTrue(iterator.attributes.isEmpty());
    }

    @Test(expected = NamespaceException.class)
    public void testGetNodePointerWithInvalidNamespace() throws NamespaceException {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        String prefix = "xml";
        Namespace ns = null;
        QName name = new QName("test:" + prefix + ":attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNull(iterator);
    }

    @Test
    public void testGetNodePointerWithEmptyElement() {
        NodePointer parent = new JDOMNodePointer(new JDOMDocument(), new Element("root"));
        QName name = new QName("test:attribute");
        Element element = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator = null;
        parent.setNode(element);
        iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNull(iterator.getNodePointer());
    }
}
import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(null, null, null);
        QName name = new QName("test", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testTestAttr() {
        Attr attr1 = createAttribute("xmlns:test", "http://test.com/test");
        Attr attr2 = createAttribute("test", "value");

        Assert.assertTrue(new DOMAttributeIterator.testAttr(attr1));
        Assert.assertFalse(new DOMAttributeIterator.testAttr(attr2));

        Assert.assertTrue(new DOMAttributeIterator.testAttr(attr1, "xmlns", "test", "http://test.com/test"));
        Assert.assertTrue(new DOMAttributeIterator.testAttr(attr2, "test"));
    }

    @Test
    public void testGetAttribute() {
        Element element = createElement("test", "root", null);
        QName name = new QName("test", "attr");
        Attr attr = createAttribute("test:attr", "value");
        element.setAttributeNodeNS(attr);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, element, null), name);
        Attr result = iterator.getAttribute(element, name);

        Assert.assertEquals(attr, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeNullElement() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, null, null), new QName("test", "*"));
        iterator.getAttribute(null, new QName("test", "*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeNullName() {
        Element element = createElement("test", "root", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, element, null), null);
        iterator.getAttribute(element, null);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new NodePointer(null, null, null);
        QName name = new QName("test", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.setPosition(1);

        DOMAttributePointer result = (DOMAttributePointer) iterator.getNodePointer();
        Assert.assertEquals(1, result.getPosition());
        Assert.assertNotNull(result.getNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNodePointerOutOfBounds() {
        NodePointer parent = new NodePointer(null, null, null);
        QName name = new QName("test", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.setPosition(100);

        iterator.getNodePointer();
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new NodePointer(null, null, null);
        QName name = new QName("test", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertTrue(iterator.setPosition(5));
        Assert.assertFalse(iterator.setPosition(10));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionOutOfBounds() {
        NodePointer parent = new NodePointer(null, null, null);
        QName name = new QName("test", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.setPosition(0);

        iterator.setPosition(5);
    }

    private Node createElement(String tagName, String nodeName, String namespaceURI) {
        Element element = document.createElement(tagName);
        element.setNodeName(nodeName);
        if (namespaceURI != null) {
            element.setAttribute("xmlns", namespaceURI);
        }
        return element;
    }

    private Attr createAttribute(String nodeName, String nodeValue) {
        Attr attr = document.createAttribute(nodeName);
        attr.setValue(nodeValue);
        return attr;
    }
}
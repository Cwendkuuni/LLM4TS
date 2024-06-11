import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.jxpath.ri.model.DOMAttributePointer.getAttributeName;
import static org.apache.commons.jxpath.ri.model.DOMAttributePointer.getAttributeNamespaceURI;
import static org.apache.commons.jxpath.ri.model.DOMAttributePointer.getParentNode;

public class DOMAttributeIteratorTest {

    @Test
    public void testCreateIteratorForEmptyElement() {
        NodePointer parentNode = new DOMPointNodePointer(null, null, null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, new QName("", "*"));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testCreateIteratorForElementWithNoAttributes() {
        Element element = new Element("root");
        NodePointer parentNode = new DOMPointNodePointer(element, null, null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, new QName("", "*"));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testCreateIteratorForElementWithOneAttribute() {
        Element element = new Element("root");
        element.setAttributeNode(new Attr("data", "value"));
        NodePointer parentNode = new DOMPointNodePointer(element, null, null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, new QName("", "data"));
        NodePointer attributePointer = iterator.next();
        Assert.assertEquals("value", getAttributeValue(attributePointer));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testCreateIteratorForElementWithMultipleAttributes() {
        Element element = new Element("root");
        element.setAttributeNode(new Attr("data1", "value1"));
        element.setAttributeNode(new Attr("data2", "value2"));
        NodePointer parentNode = new DOMPointNodePointer(element, null, null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, new QName("", "*"));
        NodePointer firstAttributePointer = iterator.next();
        Assert.assertEquals("value1", getAttributeValue(firstAttributePointer));
        NodePointer secondAttributePointer = iterator.next();
        Assert.assertEquals("value2", getAttributeValue(secondAttributePointer));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testCreateIteratorForElementWithAttributeNS() {
        Element element = new Element("root");
        element.setAttributeNodeNS(new Attr("xmlns:x", "http://example.com/namespace"));
        NodePointer parentNode = new DOMPointNodePointer(element, null, null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, new QName("http://example.com/namespace", "x"));
        NodePointer attributePointer = iterator.next();
        Assert.assertEquals("http://example.com/namespace", getNamespaceURI(attributePointer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIteratorWithNullName() {
        NodePointer parentNode = new DOMPointNodePointer(null, null, null, 0);
        new DOMAttributeIterator(parentNode, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIteratorWithNonExistentParent() {
        NodePointer parentNode = new DOMPointNodePointer(null, null, null, 0);
        new DOMAttributeIterator(parentNode, new QName("", "*"));
    }

    @Test
    public void testNextAfterLast() {
        Element element = new Element("root");
        element.setAttributeNode(new Attr("data1", "value1"));
        element.setAttributeNode(new Attr("data2", "value2"));
        NodePointer parentNode = new DOMPointNodePointer(element, null, null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, new QName("", "*"));
        iterator.next();
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testNextAfterLastWithNS() {
        Element element = new Element("root");
        element.setAttributeNodeNS(new Attr("xmlns:x", "http://example.com/namespace"));
        NodePointer parentNode = new DOMPointNodePointer(element, null, null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, new QName("http://example.com/namespace", "x"));
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
        Assert.assertEquals(1, iterator.getPosition());
    }

    private String getAttributeValue(NodePointer nodePointer) {
        Attr attr = (Attr) getParentNode(nodePointer).getNode();
        return attr.getValue();
    }

    private String getNamespaceURI(NodePointer nodePointer) {
        Attr attr = (Attr) getParentNode(nodePointer).getNode();
        return attr.getNamespaceURI();
    }
}
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_SingleAttribute() {
        QName attributeName = new QName("http://www.w3.org/2000/xmlns/", "xmlns:xsd");
        NodePointer parent = new NodePointer(new Element("root"), null, null);
        DOMAttributeIterator iterator = new DOMAttribeIterator(parent, attributeName);
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer);
        Assert.assertEquals(attributeName, attributePointer.getNodeName());
        Assert.assertEquals("root", attributePointer.getNodeValue());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes() {
        QName attributeName1 = new QName("http://www.w3.org/2000/xmlns/", "xmlns:xsd");
        QName attributeName2 = new QName("", "class");
        NodePointer parent = new NodePointer(new Element("root"), null, null);
        Element rootElement = (Element) parent.getNode();
        rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd", "http://www.w3.org/2000/XMLSchema");
        rootElement.setAttributeNode(rootElement.createAttribute("class", "test"));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, attributeName1);
        NodePointer attributePointer1 = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer1);
        Assert.assertEquals(attributeName1, attributePointer1.getNodeName());
        Assert.assertEquals("http://www.w3.org/2000/XMLSchema", attributePointer1.getNodeValue());
        iterator.setPosition(2);
        NodePointer attributePointer2 = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer2);
        Assert.assertEquals(attributeName2, attributePointer2.getNodeName());
        Assert.assertEquals("test", attributePointer2.getNodeValue());
    }

    @Test
    public void testDOMAttributeIterator_FilterAttributes() {
        QName attributeName1 = new QName("", "class");
        QName attributeName2 = new QName("http://www.w3.org/2000/xmlns/", "xmlns:xsd");
        NodePointer parent = new NodePointer(new Element("root"), null, null);
        Element rootElement = (Element) parent.getNode();
        rootElement.setAttributeNode(rootElement.createAttribute("class", "test1"));
        rootElement.setAttributeNode(rootElement.createAttribute("xmlns:xsd", "http://www.w3.org/2000/XMLSchema"));
        rootElement.setAttributeNode(rootElement.createAttribute("class", "test2"));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, attributeName1);
        int position = 0;
        while (iterator.hasNext()) {
            NodePointer attributePointer = iterator.next();
            if (attributePointer.getNodeName().equals(attributeName1)) {
                position = iterator.getPosition();
                break;
            }
            iterator.next();
        }
        Assert.assertEquals(1, position);
        iterator = new DOMAttributeIterator(parent, attributeName2);
        position = 0;
        while (iterator.hasNext()) {
            NodePointer attributePointer = iterator.next();
            if (attributePointer.getNodeName().equals(attributeName2)) {
                position = iterator.getPosition();
                break;
            }
            iterator.next();
        }
        Assert.assertEquals(0, position);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDOMAttributeIterator_OutOfBoundsException() {
        QName attributeName = new QName("", "class");
        NodePointer parent = new NodePointer(new Element("root"), null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, attributeName);
        iterator.setPosition(10);
    }

    @Test
    public void testDOMAttributeIterator_EmptyIterator() {
        QName attributeName = new QName("", "class");
        NodePointer parent = new NodePointer(new Element("root"), null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, attributeName);
        Assert.assertFalse(iterator.hasNext());
    }

    private static class DOMAttributePointer implements NodePointer {
        private NodePointer parent;
        private Attr attr;

        public DOMAttributePointer(NodePointer parent, Attr attr) {
            this.parent = parent;
            this.attr = attr;
        }

        @Override
        public QName getNodeName() {
            return new QName(attr.getNamespaceURI(), attr.getLocalName());
        }

        @Override
        public String getNodeValue() {
            return attr.getValue();
        }

        @Override
        public Node getNode() {
            return attr;
        }

        @Override
        public NodePointer getParent() {
            return parent;
        }
    }
}
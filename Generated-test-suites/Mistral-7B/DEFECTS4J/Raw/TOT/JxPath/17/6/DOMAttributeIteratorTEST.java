package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator.DOMAttributePointer;

public class DOMAttributeIteratorTest {

    @Test
    public void testEmptyElement() {
        Element emptyElement = (Element) DOMHelper.createDocument("", "empty", null).getDocumentElement();
        NodeIterator iterator = new DOMAttributeIterator(DOMNodePointer.createRoot(emptyElement), new QName("*"));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testAttributeWithEmptyNS() {
        Element element = DOMHelper.createDocument("", "test", null)
                .getDocumentElement();
        element.setAttribute("test", "value");
        NodeIterator iterator = new DOMAttributeIterator(DOMNodePointer.createRoot(element), new QName("test"));
        NodePointer nodePointer = iterator.next();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("test", nodePointer.getNodeName().getLocalPart());
        Assert.assertEquals("value", nodePointer.getNodeValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testAttributeWithNS() {
        String xmlns = "http://test.com/namespace";
        Element element = DOMHelper.createDocument(xmlns, "test", null)
                .getDocumentElement();
        element.setAttributeNS("http://test.com/namespace", "test", "value");
        NodeIterator iterator = new DOMAttributeIterator(DOMNodePointer.createRoot(element), new QName("{http://test.com/namespace}", "test"));
        NodePointer nodePointer = iterator.next();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("test", nodePointer.getNodeName().getLocalPart());
        Assert.assertEquals("value", nodePointer.getNodeValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultipleAttributes() {
        String xmlns = "http://test.com/namespace";
        Element element = DOMHelper.createDocument(xmlns, "test", null)
                .getDocumentElement();
        element.setAttributeNS("http://test.com/namespace", "test1", "value1");
        element.setAttributeNS("http://test.com/namespace", "test2", "value2");
        NodeIterator iterator = new DOMAttributeIterator(DOMNodePointer.createRoot(element), new QName("{http://test.com/namespace}", "*"));
        List<NodePointer> nodeList = new ArrayList<>();
        while (iterator.hasNext()) {
            nodeList.add(iterator.next());
        }
        Assert.assertEquals(2, nodeList.size());
        for (NodePointer nodePointer : nodeList) {
            Assert.assertEquals("test1", nodePointer.getNodeName().getLocalPart());
            Assert.assertEquals("value1", nodePointer.getNodeValue());
            Assert.assertTrue(nodePointer instanceof DOMAttributePointer);
        }
    }

    @Test
    public void testEmptyAttributeList() {
        Element element = DOMHelper.createDocument("", "test", null)
                .getDocumentElement();
        NodeIterator iterator = new DOMAttributeIterator(DOMNodePointer.createRoot(element), new QName("*"));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullIterator() {
        new DOMAttributeIterator(null, new QName("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        new DOMAttributeIterator(DOMNodePointer.createRoot(DOMHelper.createDocument("", "test", null)
                .getDocumentElement()), null);
    }

    @Test
    public void testNextAfterLastPosition() {
        Element element = DOMHelper.createDocument("", "test", null)
                .getDocumentElement();
        NodeIterator iterator = new DOMAttributeIterator(DOMNodePointer.createRoot(element), new QName("test"));
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNextAfterLastPositionWithWildcard() {
        Element element = DOMHelper.createDocument("", "test", null)
                .getDocumentElement();
        NodeIterator iterator = new DOMAttributeIterator(DOMNodePointer.createRoot(element), new QName("*"));
        iterator.next();
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }
}
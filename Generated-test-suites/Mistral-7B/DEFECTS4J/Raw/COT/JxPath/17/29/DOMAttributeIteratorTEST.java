import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator.equalStrings;
import static org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator.testAttr;
import static org.apache.commons.jxpath.ri.model.dom.DOMAttributeIteratorTest.createNodeWithAttr;
import static org.apache.commons.jxpath.ri.model.dom.DOMAttributeIteratorTest.createNodeWithAttrs;
import static org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer.getNodeWithAttribute;
import static org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer.getNodeWithNamespaceAttribute;
import static org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer.getNodeWithNoAttribute;

/**
 * Test class for DOMAttributeIterator
 */
public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_empty() {
        NodePointer parent = new DOMPointer("null");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "*"));
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_singleAttribute() {
        Element element = createNodeWithAttr("root", "testAttr", "testValue");
        NodePointer parent = new DOMPointer("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "testAttr"));
        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertEquals("testValue", getNodeWithAttribute(iterator.getNodePointer()).getValue());
    }

    @Test
    public void testDOMAttributeIterator_multipleAttributes() {
        Element element = createNodeWithAttrs("root", "testAttr1", "testValue1", "testAttr2", "testValue2");
        NodePointer parent = new DOMPointer("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "*"));
        List<NodePointer> result = new ArrayList<>();
        for (int i = 0; i < iterator.getSize(); i++) {
            result.add(iterator.getNodePointer());
            iterator.next();
        }
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("testValue1", getNodeWithAttribute(result.get(0)).getValue());
        Assert.assertEquals("testValue2", getNodeWithAttribute(result.get(1)).getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testDOMAttributeIterator_notFound() {
        Element element = createNodeWithAttr("root", "testAttr", "testValue");
        NodePointer parent = new DOMPointer("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "notFoundAttr"));
        iterator.next();
    }

    @Test
    public void testDOMAttributeIterator_sameNodeWithDifferentPosition() {
        Element element = createNodeWithAttr("root", "testAttr", "testValue");
        NodePointer parent = new DOMPointer("root");
        DOMAttributeIterator iterator1 = new DOMAttributeIterator(parent, new QName("", "testAttr"));
        DOMAttributeIterator iterator2 = new DOMAttributeIterator(parent, new QName("", "testAttr"));
        Assert.assertNotNull(iterator1.getNodePointer());
        Assert.assertNotNull(iterator2.getNodePointer());
        Assert.assertNotSame(iterator1.getNodePointer(), iterator2.getNodePointer());
        iterator1.next();
        iterator2.next();
        Assert.assertEquals(iterator1.getNodePointer(), iterator2.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_namespaceAttribute() {
        Element element = createNodeWithNamespaceAttr("root", "testNS", "testAttr", "testValue");
        NodePointer parent = new DOMPointer("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("testNS", "testAttr"));
        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertEquals("testValue", getNodeWithNamespaceAttribute(iterator.getNodePointer()).getValue());
    }

    @Test
    public void testDOMAttributeIterator_namespaceNotFound() {
        Element element = createNodeWithNamespaceAttr("root", "testNS", "testAttr", "testValue");
        NodePointer parent = new DOMPointer("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("notFoundNS", "testAttr"));
        iterator.next();
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_prefixNotFound() {
        Element element = createNodeWithAttr("root", "prefix:testAttr", "testValue");
        NodePointer parent = new DOMPointer("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "testAttr"));
        iterator.next();
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_prefixAndLocalNameMatch() {
        Element element = createNodeWithAttr("root", "prefix:testAttr", "testValue");
        NodePointer parent = new DOMPointer("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("prefix", "testAttr"));
        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertEquals("testValue", getNodeWithAttribute(iterator.getNodePointer()).getValue());
    }

    @Test
    public void testDOMAttributeIterator_prefixAndLocalNameAndNamespaceMatch() {
        Element element = createNodeWithNamespaceAttr("root", "testNS", "prefix:testAttr", "testValue");
        NodePointer parent = new DOMPointer("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("prefix", "testAttr", "testNS"));
        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertEquals("testValue", getNodeWithNamespaceAttribute(iterator.getNodePointer()).getValue());
    }

    @Test
    public void testDOMAttributeIterator_noAttribute() {
        Element element = createNode("root");
        NodePointer parent = new DOMPointer("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "testAttr"));
        Assert.assertNull(iterator.getNodePointer());
    }

    private Node createNode(String tagName) {
        Document document = JaxpHelper.newDocumentBuilder().newDocument();
        return document.createElement(tagName);
    }

    private Node createNodeWithAttr(String tagName, String attrName, String attrValue) {
        Document document = JaxpHelper.newDocumentBuilder().newDocument();
        Element element = document.createElement(tagName);
        element.setAttribute(attrName, attrValue);
        document.appendChild(element);
        return document.getDocumentElement();
    }

    private Node createNodeWithAttrs(String tagName, String... attrNamesValues) {
        Document document = JaxpHelper.newDocumentBuilder().newDocument();
        Element element = document.createElement(tagName);
        for (String[] attrNameValue : attrNamesValues) {
            String attrName = attrNameValue[0];
            String attrValue = attrNameValue[1];
            element.setAttribute(attrName, attrValue);
        }
        document.appendChild(element);
        return document.getDocumentElement();
    }
}
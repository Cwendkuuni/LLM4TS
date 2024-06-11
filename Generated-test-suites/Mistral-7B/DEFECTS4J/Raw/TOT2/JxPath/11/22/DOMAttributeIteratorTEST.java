import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeTest;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeTestHelper;
import org.apache.commons.jxpath.ri.model.dom.DOMElement;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private NodePointer nodePointer;
    private Element element;

    @BeforeEach
    public void setUp() {
        element = new org.w3c.dom.DOMElement("root");
        nodePointer = new NodePointer(element);
    }

    @Test
    public void testDOMAttributeIteratorConstruction_Valid() {
        QName name = new QName("namespace", "localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
        // Verify that the iterator was initialized correctly
        Assertions.assertNotNull(iterator);
        Assertions.assertEquals(nodePointer, iterator.getParent());
        Assertions.assertEquals(name, iterator.getName());
        Assertions.assertNotNull(iterator.getAttributes());
        Assertions.assertTrue(iterator.hasNext());
    }

    @Test
    public void testTestAttr_MatchingAttrWithNoPrefix() {
        Attr attr = new Attr("xmlns:test", "test");
        QName testName = new QName("", "test");
        boolean result = DOMAttributeIterator.testAttr(attr, testName);
        Assertions.assertTrue(result);
    }

    @Test
    public void testTestAttr_MatchingAttrWithPrefix() {
        Attr attr = new Attr("xmlns:test:local", "local");
        QName testName = new QName("xmlns:test", "local");
        boolean result = DOMAttributeIterator.testAttr(attr, testName);
        Assertions.assertTrue(result);
    }

    @Test
    public void testTestAttr_NonMatchingAttr() {
        Attr attr = new Attr("xmlns:test2:local", "local");
        QName testName = new QName("xmlns:test", "local");
        boolean result = DOMAttributeIterator.testAttr(attr, testName);
        Assertions.assertFalse(result);
    }

    @Test
    public void testGetAttribute_NoNamespaceAttr() {
        Element element = new org.w3c.dom.DOMElement("root");
        element.setAttribute("localName", "value");

        QName name = new QName("", "localName");
        Attr attribute = DOMAttributeIterator.getAttribute(element, name);

        Assertions.assertEquals("value", attribute.getNodeValue());
    }

    @Test
    public void testGetAttribute_NamespaceAttr() {
        Element element = new org.w3c.dom.DOMElement("root");
        String namespaceURI = "http://example.com/";
        String localName = "localName";
        element.setAttributeNS(namespaceURI, localName, "value");

        QName name = new QName(namespaceURI, localName);
        Attr attribute = DOMAttributeIterator.getAttribute(element, name);

        Assertions.assertEquals("value", attribute.getNodeValue());
    }

    @Test
    public void testGetAttribute_AttrNotFound() {
        Element element = new org.w3c.dom.DOMElement("root");

        QName name = new QName("http://example.com/", "localName");
        Attr attribute = DOMAttributeIterator.getAttribute(element, name);

        Assertions.assertNull(attribute);
    }

    // ...
}
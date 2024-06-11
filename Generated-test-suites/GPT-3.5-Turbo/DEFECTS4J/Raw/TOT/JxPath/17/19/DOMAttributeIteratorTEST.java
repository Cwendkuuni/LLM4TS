import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private NodePointer nodePointer;
    private QName qName;

    @Before
    public void setUp() {
        Document document = XmlUtils.createDocument();
        Element rootElement = document.createElement("root");
        document.appendChild(rootElement);

        Element element = document.createElement("element");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
        element.setAttribute("ns:attr4", "value4");
        element.setAttribute("ns:attr5", "value5");
        element.setAttribute("xmlns:ns", "http://example.com/ns");

        rootElement.appendChild(element);

        nodePointer = new DOMNodePointer(null, element);
        qName = new QName("attr1");
    }

    @Test
    public void testConstructor_NoMatchingAttributes() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("nonexistent"));

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_MatchingAttributeSingle() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, qName);

        NodePointer attributePointer = iterator.getNodePointer();

        assertTrue(attributePointer instanceof DOMAttributePointer);
        assertEquals("value1", ((Attr) attributePointer.getNode()).getValue());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(1));
    }

    @Test
    public void testConstructor_MatchingAttributeMultiple() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));

        NodePointer attributePointer = iterator.getNodePointer();

        assertTrue(attributePointer instanceof DOMAttributePointer);
        assertEquals("value1", ((Attr) attributePointer.getNode()).getValue());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
    }

    @Test
    public void testConstructor_NamespaceMatchingAttribute() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("ns:attr4"));

        NodePointer attributePointer = iterator.getNodePointer();

        assertTrue(attributePointer instanceof DOMAttributePointer);
        assertEquals("value4", ((Attr) attributePointer.getNode()).getValue());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(1));
    }

    @Test
    public void testConstructor_NoNamespaceMatchingAttribute() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("attr4"));

        NodePointer attributePointer = iterator.getNodePointer();

        assertNull(attributePointer);
    }
}
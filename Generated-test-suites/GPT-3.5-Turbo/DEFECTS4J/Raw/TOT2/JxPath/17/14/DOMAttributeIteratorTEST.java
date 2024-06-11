import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        Document document = getSampleDocument();
        Node node = document.getDocumentElement();
        NodePointer pointer = new DOMNodePointer(node);
        DOMAttributeIterator iterator = new DOMAttributeIterator(pointer, new QName("test"));
        assertNotNull(iterator);
    }

    @Test
    public void testGetPositionAndSetPosition() {
        Document document = getSampleDocument();
        Node node = document.getDocumentElement();
        NodePointer pointer = new DOMNodePointer(node);
        DOMAttributeIterator iterator = new DOMAttributeIterator(pointer, new QName("*"));
        assertEquals(0, iterator.getPosition());
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        Document document = getSampleDocument();
        Node node = document.getDocumentElement();
        NodePointer pointer = new DOMNodePointer(node);
        DOMAttributeIterator iterator = new DOMAttributeIterator(pointer, new QName("*"));
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testSetPositionOutOfRange() {
        Document document = getSampleDocument();
        Node node = document.getDocumentElement();
        NodePointer pointer = new DOMNodePointer(node);
        DOMAttributeIterator iterator = new DOMAttributeIterator(pointer, new QName("*"));
        assertFalse(iterator.setPosition(5));
    }

    // Helper method to create a sample document
    private Document getSampleDocument() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element rootElement = document.createElement("root");
        rootElement.setAttribute("attr1", "value1");
        rootElement.setAttribute("attr2", "value2");
        Attr attr3 = document.createAttribute("attr3");
        attr3.setValue("value3");
        rootElement.setAttributeNode(attr3);
        document.appendChild(rootElement);
        return document;
    }
}
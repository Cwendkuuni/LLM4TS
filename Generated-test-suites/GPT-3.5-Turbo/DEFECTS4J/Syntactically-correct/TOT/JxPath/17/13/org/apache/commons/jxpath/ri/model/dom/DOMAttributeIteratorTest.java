import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;
    private Document document;
    private Element element;

    @Before
    public void setup() {
        document = new DocumentMock();
        element = document.createElement("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttributeNS("http://www.example.com", "ns:attr3", "value3");

        parent = new NodePointerMock(element);
        name = new QName("attr*", "*", "*");
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);

        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("attr1", attr.getName());
        assertEquals("value1", attr.getValue());
    }

    @Test
    public void testGetPosition() {
        Iterator<Attr> attributesIterator = getAttributesIterator();

        int expectedPosition = 1;
        while (attributesIterator.hasNext()) {
            assertEquals(expectedPosition, iterator.getPosition());
            iterator.setPosition(expectedPosition);
            expectedPosition++;
            attributesIterator.next();
        }
    }

    @Test
    public void testSetPosition() {
        Iterator<Attr> attributesIterator = getAttributesIterator();

        int expectedPosition = 2;
        while (attributesIterator.hasNext()) {
            iterator.setPosition(expectedPosition);
            expectedPosition++;
            attributesIterator.next();
            assertEquals(expectedPosition, iterator.getPosition());
        }
    }

    private Iterator<Attr> getAttributesIterator() {
        List<Attr> attributesList = new ArrayList<>();
        NamedNodeMap attributesMap = element.getAttributes();
        for (int i = 0; i < attributesMap.getLength(); i++) {
            attributesList.add((Attr) attributesMap.item(i));
        }
        return attributesList.iterator();
    }

    private class DocumentMock implements Document {
        // Mock implementation of Document interface
    }

    private class NodePointerMock implements NodePointer {
        // Mock implementation of NodePointer interface
    }

}
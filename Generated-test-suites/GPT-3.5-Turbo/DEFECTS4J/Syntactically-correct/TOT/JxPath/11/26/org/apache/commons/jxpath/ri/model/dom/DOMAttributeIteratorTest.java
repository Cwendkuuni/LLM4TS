import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

    private Element element;
    private NodePointer parent;
    private QName name;
    private NodeIterator iterator;

    @Before
    public void setUp() {
        // Create a sample DOM element
        Document document = XMLUtility.createDocument();
        element = document.createElement("sample");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        document.appendChild(element);
        
        parent = new DOMNodePointer((Node) document);
        name = new QName("attr1");
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();

        assertTrue(nodePointer instanceof DOMAttributePointer);

        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("attr1", attr.getName());
        assertEquals("value1", attr.getValue());
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();

        assertEquals(0, position);
    }

    @Test
    public void testSetPositionValid() {
        boolean result = iterator.setPosition(1);

        assertTrue(result);

        NodePointer nodePointer = iterator.getNodePointer();

        assertTrue(nodePointer instanceof DOMAttributePointer);
        assertEquals(1, iterator.getPosition());

        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("attr1", attr.getName());
        assertEquals("value1", attr.getValue());
    }

    @Test
    public void testSetPositionInvalid() {
        boolean result = iterator.setPosition(3);

        assertFalse(result);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionNegative() {
        boolean result = iterator.setPosition(-1);

        assertFalse(result);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testIteratorMultipleAttributes() {
        name = new QName("*");
        iterator = new DOMAttributeIterator(parent, name);

        int position = 1;
        while (iterator.setPosition(position++)) {
            NodePointer nodePointer = iterator.getNodePointer();
            
            assertTrue(nodePointer instanceof DOMAttributePointer);
            
            Attr attr = (Attr) nodePointer.getNode();
            assertTrue(attr.getName().startsWith("attr"));
            assertTrue(attr.getValue().startsWith("value"));
        }
        
        assertEquals(3, position);
    }

    @Test
    public void testIteratorNoAttributes() {
        element.removeAttribute("attr1");
        element.removeAttribute("attr2");
        
        name = new QName("attr1");
        iterator = new DOMAttributeIterator(parent, name);
        
        assertFalse(iterator.setPosition(1));
    }
}
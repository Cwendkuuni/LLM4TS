import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {
    private Element element;
    private NodePointer parent;
    private QName name;

    @Before
    public void setUp() {
        element = new Element("testElement");
        element.setAttribute("attribute1", "value1");
        element.setAttribute("attribute2", "value2");
        element.setAttribute("attribute3", "value3");
        Document document = new Document(element);
        parent = new JDOMNodePointer(document);
        name = new QName("attribute1");
    }

    @Test
    public void testConstructorWithExistingAttribute() {
        NodeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }

    @Test
    public void testConstructorWithNonExistingAttribute() {
        QName nonExistingAttributeName = new QName("nonexisting");
        NodeIterator iterator = new JDOMAttributeIterator(parent, nonExistingAttributeName);
        assertNull(iterator);
    }

    @Test
    public void testGetNodePointerWithValidPosition() {
        NodeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertTrue(iterator.setPosition(1));
        NodePointer pointer = iterator.getNodePointer();
        assertNotNull(pointer);
    }

    @Test
    public void testGetNodePointerWithInvalidPosition() {
        NodeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertFalse(iterator.setPosition(0));
        NodePointer pointer = iterator.getNodePointer();
        assertNull(pointer);
    }

    @Test
    public void testGetPosition() {
        NodeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithValidPosition() {
        NodeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        NodeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());
    }
}
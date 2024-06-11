import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        Element element = new DOMNodePointer().createElement("testElement");
        DOMNodePointer parent = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test"));

        assertNotNull(iterator);
    }

    @Test
    public void testGetPosition() {
        Element element = new DOMNodePointer().createElement("testElement");
        DOMNodePointer parent = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test"));

        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element element = new DOMNodePointer().createElement("testElement");
        DOMNodePointer parent = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test"));

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPositionOutOfBounds() {
        Element element = new DOMNodePointer().createElement("testElement");
        DOMNodePointer parent = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test"));

        assertFalse(iterator.setPosition(-1));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        Element element = new DOMNodePointer().createElement("testElement");
        DOMNodePointer parent = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test"));

        assertNotNull(iterator.getNodePointer());
    }
}
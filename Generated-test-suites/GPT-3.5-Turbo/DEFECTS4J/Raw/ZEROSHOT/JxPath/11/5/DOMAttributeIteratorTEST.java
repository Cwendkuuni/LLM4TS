import org.junit.Test;
import org.apache.commons.jxpath.ri.model.dom.*;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        assertNotNull(iterator);
        assertEquals(parent, iterator.parent);
        assertEquals(name, iterator.name);
        assertNotNull(iterator.attributes);
        assertEquals(0, iterator.position);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        NodePointer pointer = iterator.getNodePointer();

        assertNotNull(pointer);
        assertTrue(pointer instanceof DOMAttributePointer);
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        int position = iterator.getPosition();

        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        boolean result = iterator.setPosition(1);

        assertTrue(result);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition_ReturnsFalse() {
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        boolean result = iterator.setPosition(0);

        assertFalse(result);
        assertEquals(0, iterator.getPosition());
    }
}
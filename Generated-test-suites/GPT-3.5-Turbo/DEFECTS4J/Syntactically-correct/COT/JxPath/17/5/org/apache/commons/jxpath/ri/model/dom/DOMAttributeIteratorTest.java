
import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class DOMAttributeIteratorTest {

    private DOMNodePointer parentNode;
    private QName attributeName;

    @Before
    public void setUp() {
        Element parentElement = // create test DOM element here
        parentNode = new DOMNodePointer(parentElement);
        attributeName = new QName("testAttribute");
    }

    @Test
    public void testConstructor() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, attributeName);
        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, attributeName);
        DOMAttributePointer attributePointer = iterator.getNodePointer();
        assertNotNull(attributePointer);
    }

    @Test
    public void testGetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, attributeName);
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());

        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, attributeName);
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());

        assertFalse(iterator.setPosition(3));
        assertFalse(iterator.setPosition(4));
    }
}

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {
    private DOMAttributeIterator domAttributeIterator;
    private QName qname;
    private DOMNodePointer domNodePointer;

    @Before
    public void setUp() {
        Element element = new Element();
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
        DOMNodePointer parent = new DOMNodePointer(element);
        qname = new QName("attr1");

        domAttributeIterator = new DOMAttributeIterator(parent, qname);
        
        domNodePointer = new DOMNodePointer(element);
    }

    @After
    public void tearDown() {
        domAttributeIterator = null;
        qname = null;
        domNodePointer = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(domAttributeIterator);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = domAttributeIterator.getNodePointer();
        assertNotNull(nodePointer);

        assertTrue(nodePointer instanceof DOMAttributePointer);
        DOMAttributePointer attributePointer = (DOMAttributePointer) nodePointer;
        assertEquals("attr1", attributePointer.getAttribute().getName());
        assertEquals("value1", attributePointer.getAttribute().getValue());
    }

    @Test
    public void testGetPosition() {
        int position = domAttributeIterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = domAttributeIterator.setPosition(1);
        assertTrue(result);

        int position = domAttributeIterator.getPosition();
        assertEquals(1, position);

        NodePointer nodePointer = domAttributeIterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);
        DOMAttributePointer attributePointer = (DOMAttributePointer) nodePointer;
        assertEquals("attr1", attributePointer.getAttribute().getName());
        assertEquals("value1", attributePointer.getAttribute().getValue());
    }
}
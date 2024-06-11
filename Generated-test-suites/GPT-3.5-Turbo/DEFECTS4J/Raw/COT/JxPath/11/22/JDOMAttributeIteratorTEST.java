
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private JDOMAttributeIterator iterator;
    private NodePointer nodePointer;
    private Element element;
    private Attribute attr1;
    private Attribute attr2;
    

    @Before
    public void setUp() {
        element = new Element("testElement");
        attr1 = new Attribute("attr1", "value1");
        attr2 = new Attribute("attr2", "value2");
        element.setAttribute(attr1);
        element.setAttribute(attr2);
        
        nodePointer = new NodePointer(null, element);
        QName qName = new QName(null, "*");
        iterator = new JDOMAttributeIterator(nodePointer, qName);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(attr1, ((JDOMAttributePointer) nodePointer).getAttribute());
        
        iterator.setPosition(2);
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(attr2, ((JDOMAttributePointer) nodePointer).getAttribute());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
        
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
        
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(iterator.setPosition(0));
        
        assertTrue(iterator.setPosition(1));
        
        assertFalse(iterator.setPosition(3));
    }
}


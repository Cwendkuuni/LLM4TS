import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.w3c.dom.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DOMAttributeIteratorTest {
    
    private Element element;
    private NodePointer parent;
    private DOMAttributeIterator iterator;
    
    @Before
    public void setUp() {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        element = document.createElement("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "");
        element.setAttribute("attr3", null);
        
        parent = new DOMNodePointer(null, element);
        iterator = new DOMAttributeIterator(parent, new QName("*"));
    }
    
    @Test
    public void testGetNodePointer() {
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("attr1", attributePointer.getName());
        assertEquals("value1", attributePointer.getValue());
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }
    
    @Test
    public void testSetPositionInvalid() {
        assertFalse(iterator.setPosition(5));
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testSetPositionZero() {
        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testGetNodePointerNoAttributes() {
        Element emptyElement = element.getOwnerDocument().createElement("empty");
        NodePointer emptyParent = new DOMNodePointer(null, emptyElement);
        
        DOMAttributeIterator emptyIterator = new DOMAttributeIterator(emptyParent, new QName("*"));
        assertNull(emptyIterator.getNodePointer());
    }
    
    @Test
    public void testGetNodePointerNoMatchingAttributes() {
        parent = new DOMNodePointer(null, element);
        iterator = new DOMAttributeIterator(parent, new QName("nonExistentAttribute"));
        
        assertNull(iterator.getNodePointer());
    }

}
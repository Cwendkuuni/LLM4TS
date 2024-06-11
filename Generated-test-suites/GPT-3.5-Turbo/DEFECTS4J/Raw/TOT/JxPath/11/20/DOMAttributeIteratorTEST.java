
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.xml.DocumentContainer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    private DOMAttributeIterator iterator;
    private NodePointer parent;

    @Before
    public void setup() {
        parent = createTestNode();
        iterator = new DOMAttributeIterator(parent, new QName("*"));
    }

    @Test
    public void testGetNodePointer() {
        assertTrue(iterator.getNodePointer() instanceof DOMAttributePointer);
        assertEquals(parent, iterator.getNodePointer().getParent());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(3);
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(4));
        assertTrue(iterator.setPosition(1));
    }

    @Test
    public void testIterateOverAttributes_emptyNode() {
        parent = createEmptyTestNode();
        iterator = new DOMAttributeIterator(parent, new QName("test"));
        assertFalse(iterator.setPosition(1));
    }

    @Test
    public void testIterateOverAttributes_noMatchingAttributes() {
        iterator = new DOMAttributeIterator(parent, new QName("none"));
        assertFalse(iterator.setPosition(1));
    }

    @Test
    public void testIterateOverAttributes_withMatchingAttribute() {
        iterator = new DOMAttributeIterator(parent, new QName("name"));
        assertTrue(iterator.setPosition(1));
        assertEquals("attr1", ((DOMAttributePointer) iterator.getNodePointer()).getAttribute().getNodeName());
    }

    @Test
    public void testIterateOverAttributes_withNamespaceAndMatchingAttribute() {
        iterator = new DOMAttributeIterator(parent, new QName("ns:name"));
        assertTrue(iterator.setPosition(1));
        assertEquals("attr3", ((DOMAttributePointer) iterator.getNodePointer()).getAttribute().getNodeName());
    }

    private NodePointer createTestNode() {
        Document document = DocumentContainer.createDocument();
        Element root = document.createElement("root");
        
        Element element = document.createElement("element");
        element.setAttribute("name", "attr1");
        element.setAttribute("age", "attr2");
        element.setAttributeNS("http://example.com/ns", "ns:name", "attr3");
        
        root.appendChild(element);
        document.appendChild(root);
        
        return new DOMNodePointer(null, root);
    }
    
    private NodePointer createEmptyTestNode() {
        Document document = DocumentContainer.createDocument();
        Element root = document.createElement("root");
        document.appendChild(root);
        
        return new DOMNodePointer(null, root);
    }
}

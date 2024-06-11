import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.XPATHNamespaceResolver;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {
    
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() throws Exception {
        XPATHNamespaceResolver resolver = new XPATHNamespaceResolver();
        resolver.add("", "http://www.example.com"); // Add a default namespace

        Element element = createExampleElement();
        NodePointer parent = new DOMNodePointer(null, element, resolver);
        
        iterator = new DOMAttributeIterator(parent, new QName("", "*"));
    }

    // Test for typical use case
    @Test
    public void testTypicalUseCase() {
        assertTrue(iterator.setPosition(1));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);
        Attr attr = ((DOMAttributePointer) nodePointer).getAttribute();
        assertNotNull(attr);
        assertEquals("id", attr.getName());
        assertEquals("123", attr.getValue());
    }

    // Test for edge case - position = 0
    @Test
    public void testEdgeCasePositionZero() {
        assertFalse(iterator.setPosition(0));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    // Test for edge case - position = attributes.size() + 1
    @Test
    public void testEdgeCasePositionOutOfRange() {
        assertFalse(iterator.setPosition(iterator.attributes.size() + 1));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    // Test for error scenario - null parent NodePointer
    @Test(expected = NullPointerException.class)
    public void testErrorScenarioNullParent() {
        new DOMAttributeIterator(null, new QName("", "*"));
    }

    // Test for error scenario - null name QName
    @Test(expected = NullPointerException.class)
    public void testErrorScenarioNullName() {
        new DOMAttributeIterator(createExampleParent(), null);
    }

    // Helper method to create an example parent NodePointer
    private NodePointer createExampleParent() {
        XPATHNamespaceResolver resolver = new XPATHNamespaceResolver();
        resolver.add("", "http://www.example.com"); // Add a default namespace

        Element parentElement = createExampleElement();
        return new DOMNodePointer(null, parentElement, resolver);
    }

    // Helper method to create an example Element
    private Element createExampleElement() {
        Element element = new DOMElement();
        element.setTagName("testElement");
        element.setAttribute("xmlns", "http://www.example.com");
        element.setAttribute("id", "123");
        element.setAttribute("name", "test");
        return element;
    }

}
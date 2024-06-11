import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private JDOMAttributeIterator iterator;
    
    @Before
    public void setUp() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        
        parent = new JDOMNodePointer(element);
        iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(4));
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        Attribute attribute = ((JDOMAttributePointer) nodePointer).getAttribute();
        assertNotNull(attribute);
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());
    }
}
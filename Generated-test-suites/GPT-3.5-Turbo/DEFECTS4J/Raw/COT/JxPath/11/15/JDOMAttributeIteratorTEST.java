
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {
    
    private NodePointer parent;
    private List<Attribute> attributes;
    private JDOMAttributeIterator iterator;
    
    @Before
    public void setUp() {
        parent = new JDOMNodePointer();
        attributes = new ArrayList<>();
        iterator = new JDOMAttributeIterator(parent, new QName("name"));
    }
    
    @Test
    public void testNodePointer() {
        iterator.setPosition(1);
        NodePointer result = iterator.getNodePointer();
        assertEquals(result, new JDOMAttributePointer(parent, attributes.get(0)));
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(iterator.getPosition(), 0);
        iterator.setPosition(2);
        assertEquals(iterator.getPosition(), 2);
    }
    
    @Test
    public void testSetPosition() {
        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
    }
}

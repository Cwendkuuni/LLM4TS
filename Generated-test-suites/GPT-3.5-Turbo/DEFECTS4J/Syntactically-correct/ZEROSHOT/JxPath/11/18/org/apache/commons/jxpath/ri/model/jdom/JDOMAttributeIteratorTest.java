import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator attributeIterator;
    private NodePointer parent;
    private QName name;
    private Element element;

    @Before
    public void setUp() {
        parent = new NodePointer();
        name = new QName();
        element = new Element("testElement");
        parent.setNode(element);
    }

    @Test
    public void testConstructor() {
        attributeIterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(attributeIterator);
    }

    @Test
    public void testGetNodePointer() {
        attributeIterator = new JDOMAttributeIterator(parent, name);
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(JDOMAttributePointer.class, nodePointer.getClass());
    }

    @Test
    public void testGetPosition() {
        attributeIterator = new JDOMAttributeIterator(parent, name);
        assertEquals(0, attributeIterator.getPosition());
        attributeIterator.setPosition(1);
        assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        attributeIterator = new JDOMAttributeIterator(parent, name);
        assertFalse(attributeIterator.setPosition(0));
        assertTrue(attributeIterator.setPosition(1));
        assertTrue(attributeIterator.setPosition(2));
        assertFalse(attributeIterator.setPosition(3));
    }
}
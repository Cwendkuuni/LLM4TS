import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    private Element element;
    private NodePointer parentNode;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        element = new Element("testElement");
        element.setAttribute(new Attribute("attr1", "value1", Namespace.NO_NAMESPACE));
        element.setAttribute(new Attribute("attr2", "value2", Namespace.XML_NAMESPACE));

        parentNode = new JDOMNodePointer(element);
        attributeIterator = new JDOMAttributeIterator(parentNode, new QName("", ""));
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(attributeIterator.setPosition(1));
        assertFalse(attributeIterator.setPosition(0));
        assertFalse(attributeIterator.setPosition(3));
    }

    @Test
    public void testGetNodePointer() {
        attributeIterator.setPosition(1);
        NodePointer nodePointer = attributeIterator.getNodePointer();

        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
    }
}
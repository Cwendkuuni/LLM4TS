import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        Element element = new Element("person");
        element.setAttribute("name", "John");
        element.setAttribute("age", "30");
        element.setAttribute("gender", "male");
        parent = new NodePointer(element);
    }

    @Test
    public void testGetNodePointer() {
        QName name = new QName("name");
        attributeIterator = new JDOMAttributeIterator(parent, name);
        NodePointer nodePointer = attributeIterator.getNodePointer();
        
        Attribute attribute = (Attribute) nodePointer.getValue();
        assertEquals("name", attribute.getName());
        assertEquals("John", attribute.getValue());
        
        attributeIterator.setPosition(2);
        nodePointer = attributeIterator.getNodePointer();
        
        attribute = (Attribute) nodePointer.getValue();
        assertEquals("age", attribute.getName());
        assertEquals("30", attribute.getValue());
        
        attributeIterator.setPosition(3);
        nodePointer = attributeIterator.getNodePointer();
        
        attribute = (Attribute) nodePointer.getValue();
        assertEquals("gender", attribute.getName());
        assertEquals("male", attribute.getValue());
    }

    @Test
    public void testGetPosition() {
        QName name = new QName("name");
        attributeIterator = new JDOMAttributeIterator(parent, name);
        attributeIterator.setPosition(2);
        assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        QName name = new QName("name");
        attributeIterator = new JDOMAttributeIterator(parent, name);
        assertTrue(attributeIterator.setPosition(1));
        assertTrue(attributeIterator.setPosition(2));
        assertFalse(attributeIterator.setPosition(4));
    }
}
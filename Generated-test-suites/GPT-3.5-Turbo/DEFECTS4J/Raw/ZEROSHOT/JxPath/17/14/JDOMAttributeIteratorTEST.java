import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() throws Exception {
        Element element = new Element("testElement");
        element.setAttribute(new Attribute("attr1", "value1"));
        element.setAttribute(new Attribute("attr2", "value2"));
        element.setAttribute(new Attribute("attr3", "value3"));
        NodePointer nodePointer = new NodePointer(element);
        QName qName = new QName("attr1");
        attributeIterator = new JDOMAttributeIterator(nodePointer, qName);
    }

    @Test
    public void getNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
    }

    @Test
    public void getPosition() {
        int position = attributeIterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void setPosition() {
        assertFalse(attributeIterator.setPosition(0));
        assertFalse(attributeIterator.setPosition(-1));
        assertTrue(attributeIterator.setPosition(1));
        assertTrue(attributeIterator.setPosition(2));
        assertTrue(attributeIterator.setPosition(3));
        assertFalse(attributeIterator.setPosition(4));
    }

}
import org.junit.Test;
import static org.junit.Assert.*;
import org.jdom.Element;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class JDOMAttributeIteratorTest {

    Element element = new Element("test");
    NodePointer parent = new NodePointer(element);
    QName qName = new QName("test");

    @Test
    public void testConstructor() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);
        assertNotNull(iterator);
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(2));
    }

    @Test
    public void testGetNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);
        assertNotNull(iterator.getNodePointer());
    }
}
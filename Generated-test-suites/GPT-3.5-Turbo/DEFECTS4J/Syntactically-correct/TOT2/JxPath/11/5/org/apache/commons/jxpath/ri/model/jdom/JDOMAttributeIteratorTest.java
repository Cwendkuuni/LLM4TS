import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        Element parentElement = new Element("parent");
        parentElement.setAttribute("attr1", "value1");
        parentElement.setAttribute("attr2", "value2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(
                new JDOMNodePointer(parentElement), new QName("attr1"));

        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        Element parentElement = new Element("parent");
        parentElement.setAttribute("attr1", "value1");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(
                new JDOMNodePointer(parentElement), new QName("attr1"));

        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        Element parentElement = new Element("parent");
        parentElement.setAttribute("attr1", "value1");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(
                new JDOMNodePointer(parentElement), new QName("attr1"));

        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element parentElement = new Element("parent");
        parentElement.setAttribute("attr1", "value1");
        parentElement.setAttribute("attr2", "value2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(
                new JDOMNodePointer(parentElement), new QName("*"));

        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(3));
    }
}
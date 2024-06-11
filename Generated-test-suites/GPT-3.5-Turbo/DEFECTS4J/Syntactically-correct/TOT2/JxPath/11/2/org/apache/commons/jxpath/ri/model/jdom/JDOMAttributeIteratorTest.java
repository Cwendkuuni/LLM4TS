import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("attr1"));

        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testJDOMAttributeIteratorWithNamespace() {
        Element element = new Element("testElement", Namespace.getNamespace("test", "http://test.com"));
        element.setAttribute("attr1", "value1", Namespace.getNamespace("test", "http://test.com"));
        element.setAttribute("attr2", "value2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("attr1", "test"));

        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testJDOMAttributeIteratorWildcard() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("*"));

        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testJDOMAttributeIteratorNoAttributes() {
        Element element = new Element("testElement");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("attr1"));

        assertNotNull(iterator);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.setPosition(1));
    }

}
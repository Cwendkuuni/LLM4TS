import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Attribute;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        NodePointer parent = new NodePointer(element);
        QName qName = new QName("attr1", Namespace.NO_NAMESPACE);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals("value1", ((Attribute)nodePointer.getNode()).getValue());
    }

    @Test
    public void testGetPosition() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        NodePointer parent = new NodePointer(element);
        QName qName = new QName("attr1", Namespace.NO_NAMESPACE);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        NodePointer parent = new NodePointer(element);
        QName qName = new QName("attr1", Namespace.NO_NAMESPACE);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(4));
        assertTrue(iterator.setPosition(3));
    }
}
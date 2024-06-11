import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {
    private Element element;

    @Before
    public void setUp() {
        element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new NodePointer(element);
        QName name = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();

        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals("value1", ((Attribute) nodePointer.getNode()).getValue());
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer(element);
        QName name = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        int position = iterator.getPosition();

        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new NodePointer(element);
        QName name = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        boolean result = iterator.setPosition(1);

        assertTrue(result);
        assertEquals(1, iterator.getPosition());
    }
}
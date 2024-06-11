import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private JDOMAttributeIterator iterator;

    @Before
    public void setup() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        parent = new JDOMNodePointer(element);
        iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = iterator.setPosition(1);
        assertTrue(result);
        assertEquals(1, iterator.getPosition());
    }
}
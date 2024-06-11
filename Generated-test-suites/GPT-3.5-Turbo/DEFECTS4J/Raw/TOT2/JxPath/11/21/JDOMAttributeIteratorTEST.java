import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;

public class JDOMAttributeIteratorTest {
    private Element element;
    private NodePointer parent;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        parent = new JDOMNodePointer(element);
        iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
    }

    @Test
    public void testNodePointerNotNull() {
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertTrue(iterator.getPosition() > 0);
    }

    @Test
    public void testSetPositionValid() {
        assertTrue(iterator.setPosition(1));
    }

    @Test
    public void testSetPositionInvalid() {
        assertFalse(iterator.setPosition(0));
    }

    @Test
    public void testCompleteIteration() {
        int attributesSize = element.getAttributes().size();
        for (int i = 1; i <= attributesSize; i++) {
            assertTrue(iterator.setPosition(i));
            assertNotNull(iterator.getNodePointer());
        }
    }

    @Test
    public void testInvalidParentNode() {
        Element invalidElement = null;
        NodePointer invalidParent = new JDOMNodePointer(invalidElement);
        JDOMAttributeIterator invalidIterator = new JDOMAttributeIterator(invalidParent, new QName("attr"));
        assertNull(invalidIterator.getNodePointer());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedException() {
        iterator.getNodePointer().setValue("new value");
    }
}
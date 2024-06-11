import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    private Element testElement;
    private NodePointer testNodePointer;

    @Before
    public void setUp() {
        // Create a test Element for JDOMAttributeIterator
        testElement = new Element("testElement");
        testElement.setAttribute("attr1", "value1", Namespace.NO_NAMESPACE);
        testElement.setAttribute("attr2", "value2", Namespace.NO_NAMESPACE);

        testNodePointer = new JDOMAttributeIterator(new JDOMAttributePointer(null, testElement), new QName("attr1"));
    }

    @Test
    public void testGetNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, new QName("attr1"));
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, new QName("attr1"));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, new QName("attr1"));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testGetNodePointerNull() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, new QName("nonExistingAttribute"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSetPositionInvalid() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, new QName("attr1"));
        assertFalse(iterator.setPosition(0));
    }

}
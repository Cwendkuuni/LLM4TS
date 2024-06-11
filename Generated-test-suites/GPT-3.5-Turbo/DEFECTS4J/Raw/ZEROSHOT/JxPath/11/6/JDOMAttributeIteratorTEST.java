import junit.framework.TestCase;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.List;

public class JDOMAttributeIteratorTest extends TestCase {

    private Element testElement;
    private NodePointer testNodePointer;

    protected void setUp() throws Exception {
        super.setUp();
        testElement = new Element("test", Namespace.NO_NAMESPACE);

        Attribute attribute1 = new Attribute("attr1", "value1");
        Attribute attribute2 = new Attribute("attr2", "value2");
        Attribute attribute3 = new Attribute("attr3", "value3");

        testElement.setAttribute(attribute1);
        testElement.setAttribute(attribute2);
        testElement.setAttribute(attribute3);

        testNodePointer = new NodePointer();
        testNodePointer.setNode(testElement);
    }

    public void testConstructor() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, new QName("*"));
        assertNotNull(iterator);
    }

    public void testGetNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, new QName("*"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("attr1", ((Attribute) nodePointer.getNode()).getName());
        assertEquals("attr1", nodePointer.getName());
        assertEquals(testNodePointer, nodePointer.getParent());
    }

    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, new QName("*"));
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    public void testSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testNodePointer, new QName("*"));
        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(3));
        assertFalse(iterator.setPosition(4));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        testElement = null;
        testNodePointer = null;
    }
}
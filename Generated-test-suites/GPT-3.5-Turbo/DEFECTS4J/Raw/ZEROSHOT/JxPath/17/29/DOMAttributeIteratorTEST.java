import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    private static final String TEST_PREFIX = "testPrefix";
    private static final String TEST_LOCAL_NAME = "testName";
    private static final String TEST_NAMESPACE_URI = "testNamespaceURI";
    private static final String TEST_NODE_PREFIX = "testNodePrefix";
    private static final String TEST_NODE_LOCAL_NAME = "testNodeName";
    private static final String TEST_NODE_NAMESPACE_URI = "testNodeNamespaceURI";

    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        NodePointer parent = new NodePointer();
        QName name = new QName(TEST_PREFIX, TEST_LOCAL_NAME, TEST_NAMESPACE_URI);
        iterator = new DOMAttributeIterator(parent, name);
        iterator.attributes.add(createTestAttribute());
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);
        DOMAttributePointer attributePointer = (DOMAttributePointer) nodePointer;
        assertSame(iterator.parent, attributePointer.getParent());
        assertSame(iterator.attributes.get(0), attributePointer.getAttribute());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
    }

    private Attr createTestAttribute() {
        Element element = new Element();
        element.setTagName("test");
        NamedNodeMap attributes = new NamedNodeMap();
        Attr attribute = new Attr();
        attribute.setNodeName(TEST_NODE_PREFIX + ":" + TEST_NODE_LOCAL_NAME);
        attribute.setNamespaceURI(TEST_NODE_NAMESPACE_URI);
        attributes.setNamedItem(attribute);
        element.setAttributes(attributes);
        return attribute;
    }
}
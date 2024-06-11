import junit.framework.TestCase;

public class JDOMAttributeIteratorTest extends TestCase {
    private NodePointer parent;
    private JDOMAttributeIterator iterator;

    protected void setUp() throws Exception {
        super.setUp();
        parent = new JDOMNodePointer();
        iterator = new JDOMAttributeIterator(parent, new QName());
    }

    public void testGetNodePointer() {
        assertNull(iterator.getNodePointer());

        parent.setNode(new Element("test", Namespace.NO_NAMESPACE));
        assertEquals(JDOMAttributePointer.class, iterator.getNodePointer().getClass());
    }

    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());

        parent.setNode(new Element("test", Namespace.NO_NAMESPACE));

        iterator.getNodePointer();
        assertEquals(1, iterator.getPosition());
    }

    public void testSetPosition() {
        assertFalse(iterator.setPosition(1));
        assertFalse(iterator.setPosition(2));

        parent.setNode(new Element("test", Namespace.NO_NAMESPACE));
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(2));
    }
}
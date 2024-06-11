import junit.framework.TestCase;

public class JDOMAttributeIteratorTest extends TestCase {

    public void testGetNodePointer() {
        // Test when parent node is an instance of Element
        Element element = new Element("TestElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));

        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("attr1", nodePointer.getName());
        assertEquals("value1", ((Attribute)nodePointer.getNode()).getValue());
    }

    public void testGetPosition() {
        // Test when attributes list is not null
        Element element = new Element("TestElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }

    public void testSetPosition() {
        // Test when attributes list is not null
        Element element = new Element("TestElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(3));
    }

}
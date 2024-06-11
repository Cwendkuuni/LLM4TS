import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = new NodePointer();
        name = new QName("attribute");
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer_validPosition_returnsNodePointer() {
        iterator.setPosition(1);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
    }

    @Test
    public void testGetNodePointer_invalidPosition_returnsNull() {
        iterator.setPosition(0);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testGetPosition_returnsCurrentPosition() {
        iterator.setPosition(2);
        int position = iterator.getPosition();
        Assert.assertEquals(2, position);
    }

    @Test
    public void testSetPosition_validPosition_returnsTrue() {
        boolean result = iterator.setPosition(3);
        Assert.assertTrue(result);
    }

    @Test
    public void testSetPosition_invalidPosition_returnsFalse() {
        boolean result = iterator.setPosition(0);
        Assert.assertFalse(result);
    }

    @Test
    public void testSetPosition_positionLessThanOne_returnsFalse() {
        boolean result = iterator.setPosition(-1);
        Assert.assertFalse(result);
    }

    @Test
    public void testSetPosition_positionGreaterThanAttributesSize_returnsFalse() {
        boolean result = iterator.setPosition(10);
        Assert.assertFalse(result);
    }

    @Test
    public void testSetPosition_positionEqualsAttributesSize_returnsTrue() {
        boolean result = iterator.setPosition(1);
        Assert.assertTrue(result);
    }

    // Typical use case: Element node with matching attribute
    @Test
    public void testDOMAttributeIterator_typicalUseCase_validAttribute_returnsAttributeIterator() {
        Element element = new Element();
        parent.setNode(element);
        element.setAttribute("attribute", "value");
        
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertNotNull(iterator.getNodePointer());
        
        Assert.assertFalse(iterator.setPosition(2));
        Assert.assertNull(iterator.getNodePointer());
    }
    
    // Edge case: Element node without matching attribute
    @Test
    public void testDOMAttributeIterator_edgeCase_noMatchingAttribute_returnsEmptyIterator() {
        Element element = new Element();
        parent.setNode(element);
        
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        Assert.assertFalse(iterator.setPosition(1));
        Assert.assertNull(iterator.getNodePointer());
    }
    
    // Error scenario: Node is not an Element node
    @Test
    public void testDOMAttributeIterator_errorScenario_nodeNotElementNode_returnsEmptyIterator() {
        Node notElementNode = new Node();
        parent.setNode(notElementNode);
        
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        Assert.assertFalse(iterator.setPosition(1));
        Assert.assertNull(iterator.getNodePointer());
    }
}
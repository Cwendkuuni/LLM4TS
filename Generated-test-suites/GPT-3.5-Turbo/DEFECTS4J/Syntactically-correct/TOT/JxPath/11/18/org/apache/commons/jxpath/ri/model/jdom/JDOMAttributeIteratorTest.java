import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JDOMAttributeIteratorTest {

    private Element createDummyElement() {
        Element element = new Element("dummy");
        element.setAttribute("attribute1", "value1");
        element.setAttribute("attribute2", "value2");
        element.setAttribute("attribute3", "value3");
        return element;
    }

    @Test
    public void testConstructor_ValidElementAndName() {
        Element element = createDummyElement();
        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("attribute1");
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        Assertions.assertNotNull(iterator);
    }

    @Test
    public void testConstructor_InvalidElement() {
        NodePointer parent = new JDOMNodePointer(null);
        QName name = new QName("attribute1");
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        Assertions.assertNotNull(iterator);
        NodePointer nodePointer = iterator.getNodePointer();
        Assertions.assertNull(nodePointer);
    }

    @Test
    public void testConstructor_InvalidName() {
        Element element = createDummyElement();
        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("invalidAttribute");
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        Assertions.assertNotNull(iterator);
        NodePointer nodePointer = iterator.getNodePointer();
        Assertions.assertNull(nodePointer);
    }

    @Test
    public void testGetNodePointer_ValidPosition() {
        Element element = createDummyElement();
        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("attribute1");
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        int position = iterator.getPosition();
        NodePointer nodePointer = iterator.getNodePointer();
        
        Assertions.assertNotNull(nodePointer);
        Assertions.assertEquals(1, position);
    }

    @Test
    public void testGetNodePointer_InvalidPosition() {
        Element element = createDummyElement();
        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("attribute1");
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator.setPosition(0);
        
        int position = iterator.getPosition();
        NodePointer nodePointer = iterator.getNodePointer();
        
        Assertions.assertNull(nodePointer);
        Assertions.assertEquals(0, position);
    }

    @Test
    public void testGetPosition() {
        Element element = createDummyElement();
        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("attribute1");
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        int position = iterator.getPosition();
        
        Assertions.assertEquals(0, position);
    }

    @Test
    public void testSetPosition_ValidPosition() {
        Element element = createDummyElement();
        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("attribute1");
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator.setPosition(2);
        
        int position = iterator.getPosition();
        NodePointer nodePointer = iterator.getNodePointer();
        
        Assertions.assertNotNull(nodePointer);
        Assertions.assertEquals(2, position);
    }

    @Test
    public void testSetPosition_InvalidPosition() {
        Element element = createDummyElement();
        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("attribute1");
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        iterator.setPosition(4);
        
        int position = iterator.getPosition();
        NodePointer nodePointer = iterator.getNodePointer();
        
        Assertions.assertNull(nodePointer);
        Assertions.assertEquals(4, position);
    }
}
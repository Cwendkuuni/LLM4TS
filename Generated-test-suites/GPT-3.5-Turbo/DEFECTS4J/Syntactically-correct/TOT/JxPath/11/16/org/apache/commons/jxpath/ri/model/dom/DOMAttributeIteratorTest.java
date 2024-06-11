import org.junit.Test;
import org.junit.Assert;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        // Test with valid parent and name
        DOMNodePointer parent = new DOMNodePointer();
        QName name = new QName("attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);

        // Test with null parent
        parent = null;
        iterator = new DOMAttributeIterator(parent, name);
        Assert.assertNull(iterator);

        // Test with null name
        parent = new DOMNodePointer();
        name = null;
        iterator = new DOMAttributeIterator(parent, name);
        Assert.assertNull(iterator);
    }

    @Test
    public void testGetNamespaceURI() {
        // Test with valid prefix
        DOMNodePointer parent = new DOMNodePointer();
        parent.setNamespaceURI("http://example.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attribute"));
        String namespace = iterator.getNamespaceURI("prefix");
        Assert.assertEquals("http://example.com", namespace);

        // Test with null prefix
        namespace = iterator.getNamespaceURI(null);
        Assert.assertNull(namespace);

        // Test with invalid prefix
        namespace = iterator.getNamespaceURI("invalid_prefix");
        Assert.assertNull(namespace);
    }

    @Test
    public void testGetNodePointer() {
        // Test with valid position
        DOMNodePointer parent = new DOMNodePointer();
        QName name = new QName("attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.setPosition(0);
        DOMAttributePointer pointer = iterator.getNodePointer();
        Assert.assertNotNull(pointer);

        // Test with invalid position
        iterator.setPosition(-1);
        pointer = iterator.getNodePointer();
        Assert.assertNull(pointer);
    }

    @Test
    public void testGetPosition() {
        DOMNodePointer parent = new DOMNodePointer();
        QName name = new QName("attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.setPosition(3);
        int position = iterator.getPosition();
        Assert.assertEquals(3, position);
    }
}
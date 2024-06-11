import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {
        // Given
        JDOMAttributeIterator iterator;
        // Initialize parent and set up attributes
        NodePointer parent = new NodePointer();
        QName name = new QName("test", null, null);
        iterator = new JDOMAttributeIterator(parent, name);

        // When
        NodePointer nodePointer = iterator.getNodePointer();

        // Then
        Assert.assertNotNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        // Given
        JDOMAttributeIterator iterator;
        // Initialize parent and set up attributes
        NodePointer parent = new NodePointer();
        QName name = new QName("test", null, null);
        iterator = new JDOMAttributeIterator(parent, name);

        // When
        int position = iterator.getPosition();

        // Then
        Assert.assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        // Given
        JDOMAttributeIterator iterator;
        // Initialize parent and set up attributes
        NodePointer parent = new NodePointer();
        QName name = new QName("test", null, null);
        iterator = new JDOMAttributeIterator(parent, name);

        // When
        boolean result = iterator.setPosition(1);

        // Then
        Assert.assertEquals(true, result);
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPositionOutOfRange() {
        // Given
        JDOMAttributeIterator iterator;
        // Initialize parent and set up attributes
        NodePointer parent = new NodePointer();
        QName name = new QName("test", null, null);
        iterator = new JDOMAttributeIterator(parent, name);

        // When
        boolean result = iterator.setPosition(5);

        // Then
        Assert.assertEquals(false, result);
    }
}
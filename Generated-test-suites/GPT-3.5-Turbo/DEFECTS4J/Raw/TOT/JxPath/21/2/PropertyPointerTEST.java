import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test default property index
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());

        // Test setting a property index
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test getting the parent bean
        assertEquals(parentPointer.getNode(), propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test getting the property name
        assertEquals(new QName(null, propertyPointer.getPropertyName()), propertyPointer.getName());
    }

    @Test
    public void testIsActual() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test isActualProperty() is false
        assertFalse(propertyPointer.isActual());

        // Test isActualProperty() is true and super.isActual() is true
        propertyPointer.setPropertyIndex(1);
        assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test getting the immediate node
        assertNull(propertyPointer.getImmediateNode());

        // Test getting the immediate node after setting a value
        propertyPointer.setValue("test");
        assertEquals("test", propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test isCollection with null value
        assertFalse(propertyPointer.isCollection());

        // Test isCollection with non-null value
        propertyPointer.setValue(new ArrayList<>());
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test isLeaf with null value
        assertTrue(propertyPointer.isLeaf());

        // Test isLeaf with non-null value
        propertyPointer.setValue("test");
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test getLength with null value
        assertEquals(-1, propertyPointer.getLength());

        // Test getLength with non-null collection value
        propertyPointer.setValue(new ArrayList<>());
        assertEquals(0, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test getImmediateValuePointer with null value
        assertNull(propertyPointer.getImmediateValuePointer());

        // Test getImmediateValuePointer with non-null value
        propertyPointer.setValue("test");
        assertNotNull(propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test createPath with null value
        assertEquals(propertyPointer, propertyPointer.createPath(context));

        // Test createPath with non-null value
        propertyPointer.setValue("test");
        assertEquals(propertyPointer, propertyPointer.createPath(context));
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test createPath with null value
        assertEquals(propertyPointer, propertyPointer.createPath(context, "test"));

        // Test createPath with non-null value
        propertyPointer.setValue("test");
        assertEquals(propertyPointer, propertyPointer.createPath(context, "updated"));
        assertEquals("updated", propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreateChildWithIndex() {
        JXPathContext context = new JXPathContext();
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test createChild with null name and index
        PropertyPointer childPointer = (PropertyPointer) propertyPointer.createChild(context, null, 0);
        assertNotNull(childPointer);
        assertEquals(propertyPointer.getBean(), childPointer.getBean());

        // Test createChild with name and index
        QName name = new QName("test");
        childPointer = (PropertyPointer) propertyPointer.createChild(context, name, 1);
        assertNotNull(childPointer);
        assertEquals(propertyPointer.getBean(), childPointer.getBean());
        assertEquals(name.toString(), childPointer.getPropertyName());
        assertEquals(1, childPointer.getPropertyIndex());
    }

    @Test
    public void testHashCode() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test hashCode
        assertEquals(parentPointer.hashCode() + propertyPointer.getPropertyIndex() + propertyPointer.getIndex(), propertyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test equals with same object
        assertTrue(propertyPointer.equals(propertyPointer));

        // Test equals with null
        assertFalse(propertyPointer.equals(null));

        // Test equals with different class
        assertFalse(propertyPointer.equals(new Object()));

        // Test equals with different parent
        NodePointer otherParentPointer = new NodePointer();
        PropertyPointer otherPropertyPointer = new PropertyPointer(otherParentPointer);
        assertFalse(propertyPointer.equals(otherPropertyPointer));

        // Test equals with different property index
        otherPropertyPointer.setPropertyIndex(1);
        assertFalse(propertyPointer.equals(otherPropertyPointer));

        // Test equals with different property name
        otherPropertyPointer.setPropertyIndex(propertyPointer.getPropertyIndex());
        otherPropertyPointer.setPropertyName("test");
        assertFalse(propertyPointer.equals(otherPropertyPointer));

        // Test equals with different index
        otherPropertyPointer.setPropertyName(propertyPointer.getPropertyName());
        otherPropertyPointer.setIndex(1);
        assertFalse(propertyPointer.equals(otherPropertyPointer));

        // Test equals with same attributes
        otherPropertyPointer.setIndex(propertyPointer.getIndex());
        assertTrue(propertyPointer.equals(otherPropertyPointer));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        NodePointer pointer3 = new NodePointer();

        // Test compareChildNodePointers with null nodes
        PropertyPointer propertyPointer = new PropertyPointer();
        assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer1));

        // Test compareChildNodePointers with different nodes
        assertEquals(-1, propertyPointer.compareChildNodePointers(pointer1, pointer2));
        assertEquals(1, propertyPointer.compareChildNodePointers(pointer2, pointer1));

        // Test compareChildNodePointers with same nodes
        assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer3));
    }
}
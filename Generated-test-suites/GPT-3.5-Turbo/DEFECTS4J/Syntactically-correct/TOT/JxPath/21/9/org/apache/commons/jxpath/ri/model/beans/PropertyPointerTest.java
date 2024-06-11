import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test with default property index
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());

        // Test after setting property index
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test setting property index
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        NodePointer parentPointer = new NodePointer();
        Object bean = new Object();
        parentPointer.setNode(bean);

        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test getting parent bean
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        QName expectedName = new QName(null, "propertyName");

        // Test getting name with property name
        propertyPointer.setPropertyName("propertyName");
        assertEquals(expectedName, propertyPointer.getName());
    }

    @Test
    public void testGetPropertyName() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test getting property name
        propertyPointer.setPropertyName("propertyName");
        assertEquals("propertyName", propertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test setting property name
        propertyPointer.setPropertyName("propertyName");
        assertEquals("propertyName", propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test getting property count
        assertEquals(0, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test getting property names
        String[] expectedPropertyNames = new String[] { "property1", "property2", "property3" };
        assertArrayEquals(expectedPropertyNames, propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test checking if actual property
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test checking if actual
        assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parentPointer = new NodePointer();
        Object value = new Object();
        parentPointer.setNode(value);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test getting immediate node
        assertEquals(value, propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        NodePointer parentPointer = new NodePointer();
        Object value = new Object();
        parentPointer.setNode(value);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test checking if collection
        assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parentPointer = new NodePointer();
        Object value = new Object();
        parentPointer.setNode(value);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test checking if leaf
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        NodePointer parentPointer = new NodePointer();
        Object value = new Object();
        parentPointer.setNode(value);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test getting length
        assertEquals(1, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer parentPointer = new NodePointer();
        Object value = new Object();
        parentPointer.setNode(value);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        NodePointer expectedValuePointer = new NodePointer();
        expectedValuePointer.setName(new QName(null, "propertyName"));
        expectedValuePointer.setNode(value);

        // Test getting immediate value pointer
        assertEquals(expectedValuePointer, propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer parentPointer = new NodePointer();
        Object bean = new Object();
        parentPointer.setNode(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        // Test creating path
        propertyPointer.createPath(context);
        assertNotNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parentPointer = new NodePointer();
        Object bean = new Object();
        parentPointer.setNode(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        Object value = new Object();

        // Test creating path with value
        propertyPointer.createPath(context, value);
        assertEquals(value, propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parentPointer = new NodePointer();
        Object bean = new Object();
        parentPointer.setNode(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        QName name = new QName(null, "child");
        int index = 0;
        Object value = new Object();

        NodePointer expectedChildPointer = new NodePointer();
        expectedChildPointer.setName(name);
        expectedChildPointer.setIndex(index);
        expectedChildPointer.setNode(value);

        // Test creating child with value
        NodePointer actualChildPointer = propertyPointer.createChild(context, name, index, value);
        assertEquals(expectedChildPointer, actualChildPointer);
    }

    @Test
    public void testCreateChildWithoutValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parentPointer = new NodePointer();
        Object bean = new Object();
        parentPointer.setNode(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);

        QName name = new QName(null, "child");
        int index = 0;

        NodePointer expectedChildPointer = new NodePointer();
        expectedChildPointer.setName(name);
        expectedChildPointer.setIndex(index);
        expectedChildPointer.setNode(null);

        // Test creating child without value
        NodePointer actualChildPointer = propertyPointer.createChild(context, name, index);
        assertEquals(expectedChildPointer, actualChildPointer);
    }

    @Test
    public void testHashCode() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer);
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);

        propertyPointer1.setPropertyIndex(1);
        propertyPointer1.setPropertyName("propertyName");
        propertyPointer1.setIndex(0);

        propertyPointer2.setPropertyIndex(1);
        propertyPointer2.setPropertyName("propertyName");
        propertyPointer2.setIndex(0);

        assertEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer);
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);

        propertyPointer1.setPropertyIndex(1);
        propertyPointer1.setPropertyName("propertyName");
        propertyPointer1.setIndex(0);

        propertyPointer2.setPropertyIndex(1);
        propertyPointer2.setPropertyName("propertyName");
        propertyPointer2.setIndex(0);

        assertTrue(propertyPointer1.equals(propertyPointer2));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        
        PropertyPointer propertyPointer = new PropertyPointer();

        // Test comparing child node pointers
        assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }
}
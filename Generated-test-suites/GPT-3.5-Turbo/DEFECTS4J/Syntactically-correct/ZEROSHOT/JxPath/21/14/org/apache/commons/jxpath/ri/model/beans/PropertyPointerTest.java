import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, pointer.getPropertyIndex());

        pointer.setPropertyIndex(1);
        assertEquals(1, pointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, pointer.getPropertyIndex());

        pointer.setPropertyIndex(1);
        assertEquals(1, pointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);
        assertNull(pointer.getBean());

        Object bean = new Object();
        parent.setNode(bean);
        assertEquals(bean, pointer.getBean());
    }

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);
        QName expectedName = new QName(null, "");

        assertEquals(expectedName, pointer.getName());
    }

    @Test
    public void testGetPropertyName() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);
        assertNull(pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);
        assertNull(pointer.getPropertyName());

        String propertyName = "test";
        pointer.setPropertyName(propertyName);
        assertEquals(propertyName, pointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);
        assertEquals(0, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);
        assertEquals(0, pointer.getPropertyNames().length);
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);
        assertFalse(pointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);
        assertFalse(pointer.isActual());

        assertTrue(pointer.isActualProperty());
        assertTrue(pointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);

        assertNull(pointer.getImmediateNode());

        Object value = new Object();
        parent.setNode(value);
        assertEquals(value, pointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);
        assertFalse(pointer.isCollection());

        parent.setNode(new Object[]{});
        assertTrue(pointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);
        assertTrue(pointer.isLeaf());

        parent.setNode(new Object());
        assertTrue(pointer.isLeaf());

        parent.setNode(new Object[]{});
        assertFalse(pointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);
        assertEquals(-1, pointer.getLength());

        parent.setNode(new Object[]{});
        assertEquals(0, pointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);

        NodePointer valuePointer = pointer.getImmediateValuePointer();
        assertEquals(parent, valuePointer.getParent());
        assertNull(valuePointer.getName());
        assertNull(valuePointer.getNode());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);

        pointer.createPath(context);
        Object baseValue = context.getBaseValue();
        assertNotNull(baseValue);
        assertTrue(baseValue instanceof PropertyPointer);

        PropertyPointer createdPointer = (PropertyPointer) baseValue;
        assertEquals(pointer.getBean(), createdPointer.getBean());
        assertEquals(pointer.getPropertyName(), createdPointer.getPropertyName());
        assertEquals(pointer.getPropertyIndex(), createdPointer.getPropertyIndex());
        assertEquals(pointer.getIndex(), createdPointer.getIndex());

        try {
            pointer.createPath(context);
            fail("Expected exception not thrown.");
        } catch (JXPathAbstractFactoryException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);

        pointer.createPath(context);
        pointer.createPathWithValue(context, "testValue");

        Object baseValue = context.getBaseValue();
        assertNotNull(baseValue);
        assertTrue(baseValue instanceof PropertyPointer);

        PropertyPointer createdPointer = (PropertyPointer) baseValue;
        assertEquals(pointer.getBean(), createdPointer.getBean());
        assertEquals(pointer.getPropertyName(), createdPointer.getPropertyName());
        assertEquals(pointer.getPropertyIndex(), createdPointer.getPropertyIndex());
        assertEquals(pointer.getIndex(), createdPointer.getIndex());
        assertEquals("testValue", createdPointer.getImmediateNode());
    }

    @Test
    public void testCreateChildNodePointer() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        PropertyPointer pointer = new PropertyPointer(parent);

        QName name = new QName(null, "child");
        NodePointer childPointer = pointer.createChild(context, name, 0);
        assertEquals(parent, childPointer.getParent());
        assertEquals(name, childPointer.getName());
        assertNull(childPointer.getNode());

        try {
            pointer.createChild(context, name, 1);
            fail("Expected exception not thrown.");
        } catch (JXPathAbstractFactoryException e) {
            // Expected exception
        }
    }

    @Test
    public void testHashCode() {
        NodePointer parent1 = new NodePointer();
        NodePointer parent2 = new NodePointer();
        PropertyPointer pointer1 = new PropertyPointer(parent1);
        PropertyPointer pointer2 = new PropertyPointer(parent1);
        PropertyPointer pointer3 = new PropertyPointer(parent2);

        pointer1.setPropertyIndex(1);
        pointer2.setPropertyIndex(1);
        pointer3.setPropertyIndex(2);

        pointer1.setIndex(1);
        pointer2.setIndex(1);
        pointer3.setIndex(2);

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
    }

    @Test
    public void testEquals() {
        NodePointer parent1 = new NodePointer();
        NodePointer parent2 = new NodePointer();
        PropertyPointer pointer1 = new PropertyPointer(parent1);
        PropertyPointer pointer2 = new PropertyPointer(parent1);
        PropertyPointer pointer3 = new PropertyPointer(parent2);

        pointer1.setPropertyIndex(1);
        pointer2.setPropertyIndex(1);
        pointer3.setPropertyIndex(2);

        pointer1.setIndex(1);
        pointer2.setIndex(1);
        pointer3.setIndex(2);

        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer pointer = new PropertyPointer(new NodePointer());
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();

        assertEquals(0, pointer.compareChildNodePointers(pointer1, pointer2));
    }
}
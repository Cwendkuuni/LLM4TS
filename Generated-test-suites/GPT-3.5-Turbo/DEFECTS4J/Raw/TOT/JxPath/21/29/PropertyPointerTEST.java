import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyName() {
        PropertyPointer pointer = createPropertyPointer();
        
        // Typical use case
        assertEquals("propertyName", pointer.getPropertyName());
        
        // Edge case: empty property name
        pointer.setPropertyName("");
        assertEquals("", pointer.getPropertyName());
        
        // Error scenario: null property name
        pointer.setPropertyName(null);
        assertNull(pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer pointer = createPropertyPointer();
        
        // Typical use case
        pointer.setPropertyName("newPropertyName");
        assertEquals("newPropertyName", pointer.getPropertyName());
        
        // Edge case: empty property name
        pointer.setPropertyName("");
        assertEquals("", pointer.getPropertyName());
        
        // Error scenario: null property name
        pointer.setPropertyName(null);
        assertNull(pointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer pointer = createPropertyPointer();
        
        // Typical use case
        assertEquals(3, pointer.getPropertyCount());
        
        // Edge case: property count is 0
        pointer.setPropertyIndex(PropertyPointer.UNSPECIFIED_PROPERTY);
        assertEquals(0, pointer.getPropertyCount());
        
        // Error scenario: negative property count
        pointer.setPropertyIndex(-1);
        assertEquals(-1, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer pointer = createPropertyPointer();
        
        // Typical use case
        String[] propertyNames = pointer.getPropertyNames();
        assertEquals("property1", propertyNames[0]);
        assertEquals("property2", propertyNames[1]);
        assertEquals("property3", propertyNames[2]);
        
        // Edge case: empty property names
        pointer.setPropertyIndex(PropertyPointer.UNSPECIFIED_PROPERTY);
        propertyNames = pointer.getPropertyNames();
        assertNotNull(propertyNames);
        assertEquals(0, propertyNames.length);
        
        // Error scenario: null property names
        pointer.setPropertyIndex(-1);
        assertNull(pointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer pointer = createPropertyPointer();
        
        // Typical use case
        assertTrue(pointer.isActualProperty());
        
        // Edge case: not an actual property
        pointer.setPropertyIndex(PropertyPointer.UNSPECIFIED_PROPERTY);
        assertFalse(pointer.isActualProperty());
        
        // Error scenario: null property
        pointer.setPropertyName(null);
        assertFalse(pointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        PropertyPointer pointer = createPropertyPointer();
        
        // Typical use case
        assertTrue(pointer.isActual());
        
        // Edge case: not an actual property
        pointer.setPropertyIndex(PropertyPointer.UNSPECIFIED_PROPERTY);
        assertFalse(pointer.isActual());
        
        // Error scenario: null property
        pointer.setPropertyName(null);
        assertFalse(pointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer pointer = createPropertyPointer();
        
        // Typical use case
        assertEquals("value1", pointer.getImmediateNode());
        
        // Edge case: index is WHOLE_COLLECTION
        pointer.setIndex(PropertyPointer.WHOLE_COLLECTION);
        Object[] expectedValues = {"value1", "value2", "value3"};
        assertArrayEquals(expectedValues, (Object[]) pointer.getImmediateNode());
        
        // Error scenario: invalid index
        pointer.setIndex(3);
        assertNull(pointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer pointer = createPropertyPointer();
        
        // Typical use case
        assertFalse(pointer.isCollection());
        
        // Edge case: property is collection
        pointer.setIndex(PropertyPointer.WHOLE_COLLECTION);
        assertTrue(pointer.isCollection());
        
        // Error scenario: null property
        pointer.setPropertyName(null);
        assertFalse(pointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer pointer = createPropertyPointer();
        
        // Typical use case
        assertFalse(pointer.isLeaf());
        
        // Edge case: leaf property
        pointer.setPropertyIndex(0);
        assertTrue(pointer.isLeaf());
        
        // Error scenario: null property
        pointer.setPropertyName(null);
        assertTrue(pointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer pointer = createPropertyPointer();
        
        // Typical use case
        assertEquals(1, pointer.getLength());
        
        // Edge case: property is collection
        pointer.setIndex(PropertyPointer.WHOLE_COLLECTION);
        assertEquals(3, pointer.getLength());
        
        // Error scenario: null property
        pointer.setPropertyName(null);
        assertEquals(-1, pointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer pointer = createPropertyPointer();
        NodePointer valuePointer = pointer.getImmediateValuePointer();
        
        // Typical use case
        assertEquals("propertyName", valuePointer.getName().toString());
        assertEquals("value1", valuePointer.getNode());
        
        // Error scenario: null property
        pointer.setPropertyName(null);
        assertNull(pointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer pointer = createPropertyPointer();
        JXPathContext context = createJXPathContext();
        
        // Typical use case
        assertEquals(pointer, pointer.createPath(context));
        
        // Edge case: path already exists
        assertEquals(pointer, pointer.createPath(context));
        
        // Error scenario: factory unable to create object
        AbstractFactory factory = new MockAbstractFactory(false);
        assertNull(pointer.createPath(context, factory));
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer pointer = createPropertyPointer();
        JXPathContext context = createJXPathContext();
        
        // Typical use case
        assertEquals(pointer, pointer.createPath(context, "newValue"));
        assertEquals("newValue", pointer.getNode());
        
        // Edge case: index not within length
        pointer.setPropertyIndex(5);
        assertEquals(pointer, pointer.createPath(context, "anotherValue"));
        assertEquals("anotherValue", pointer.getNode());
        
        // Error scenario: factory unable to create object
        AbstractFactory factory = new MockAbstractFactory(false);
        assertNull(pointer.createPath(context, "value", factory));
    }

    @Test
    public void testCreateChildWithIndex() {
        PropertyPointer pointer = createPropertyPointer();
        JXPathContext context = createJXPathContext();
        QName name = new QName("childProperty");
        
        // Typical use case
        NodePointer childPointer = pointer.createChild(context, name, 2);
        assertEquals(name.toString(), childPointer.getName().toString());
        assertEquals("value3", childPointer.getNode());
        
        // Edge case: empty name
        name = new QName("");
        childPointer = pointer.createChild(context, name, 0);
        assertEquals("", childPointer.getName().toString());
        
        // Error scenario: factory unable to create object
        AbstractFactory factory = new MockAbstractFactory(false);
        assertNull(pointer.createChild(context, name, 1, factory));
    }

    @Test
    public void testCreateChildWithoutIndex() {
        PropertyPointer pointer = createPropertyPointer();
        JXPathContext context = createJXPathContext();
        QName name = new QName("childProperty");
        
        // Typical use case
        NodePointer childPointer = pointer.createChild(context, name);
        assertEquals(name.toString(), childPointer.getName().toString());
        assertNull(childPointer.getNode());
        
        // Edge case: empty name
        name = new QName("");
        childPointer = pointer.createChild(context, name);
        assertEquals("", childPointer.getName().toString());
        
        // Error scenario: factory unable to create object
        AbstractFactory factory = new MockAbstractFactory(false);
        assertNull(pointer.createChild(context, name, factory));
    }

    @Test
    public void testHashCode() {
        PropertyPointer pointer1 = createPropertyPointer();
        PropertyPointer pointer2 = createPropertyPointer();
        
        // Typical use case
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        
        // Edge case: different property index
        pointer2.setPropertyIndex(1);
        assertNotEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer pointer1 = createPropertyPointer();
        PropertyPointer pointer2 = createPropertyPointer();
        
        // Typical use case
        assertTrue(pointer1.equals(pointer1));
        assertTrue(pointer1.equals(pointer2));
        
        // Edge case: different parent
        pointer2.setParent(null);
        assertFalse(pointer1.equals(pointer2));
        
        // Error scenario: different property index
        pointer2.setParent(pointer1.getParent());
        pointer2.setPropertyIndex(1);
        assertFalse(pointer1.equals(pointer2));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer pointer = createPropertyPointer();
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        
        // Typical use case
        assertEquals(0, pointer.compareChildNodePointers(pointer1, pointer2));
        
        // Error scenario: value pointer comparison
        ValuePointer valuePointer1 = new ValuePointer();
        ValuePointer valuePointer2 = new ValuePointer();
        pointer.setValuePointer(valuePointer1);
        pointer1.setValuePointer(valuePointer1);
        pointer2.setValuePointer(valuePointer2);
        assertEquals(-1, pointer.compareChildNodePointers(pointer1, pointer2));
    }

    private PropertyPointer createPropertyPointer() {
        NodePointer parent = new NodePointer();
        return new MockPropertyPointer(parent, "propertyName");
    }
    
    private JXPathContext createJXPathContext() {
        return new JXPathContext();
    }
    
    private class MockPropertyPointer extends PropertyPointer {
        
        public MockPropertyPointer(NodePointer parent, String propertyName) {
            super(parent);
            setPropertyName(propertyName);
        }
        
        public String getPropertyName() {
            return propertyName;
        }
        
        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }
        
        public int getPropertyCount() {
            return 3;
        }
        
        public String[] getPropertyNames() {
            return new String[] {"property1", "property2", "property3"};
        }
        
        protected boolean isActualProperty() {
            return propertyName != null;
        }
        
        public boolean isActual() {
            return isActualProperty() && super.isActual();
        }
        
        public String toString() {
            return "MockPropertyPointer";
        }
        
    }
    
    private class MockAbstractFactory extends AbstractFactory {

        private boolean success;
        
        public MockAbstractFactory(boolean success) {
            super(null);
            this.success = success;
        }
        
        public boolean createObject(JXPathContext context, NodePointer pointer, Object parent, String property, int index) {
            return success;
        }
        
    }
    
    private class ValuePointer extends NodePointer {

        public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
            return 0;
        }
        
    }
    
}
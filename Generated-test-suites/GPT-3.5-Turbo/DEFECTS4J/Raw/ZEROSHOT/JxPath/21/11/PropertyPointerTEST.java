import static org.junit.Assert.*;
import org.junit.Test;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        // Test when property index is not set
        PropertyPointer pointer = new TestPropertyPointer(null);
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, pointer.getPropertyIndex());

        // Test when property index is set
        pointer.setPropertyIndex(5);
        assertEquals(5, pointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        // Test when property index is not set
        PropertyPointer pointer = new TestPropertyPointer(null);
        pointer.setPropertyIndex(5);
        assertEquals(5, pointer.getPropertyIndex());

        // Test when property index is already set
        pointer.setPropertyIndex(10);
        assertEquals(10, pointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        // Test when bean is not set
        PropertyPointer pointer = new TestPropertyPointer(null);
        assertNull(pointer.getBean());

        // Test when bean is set
        Object bean = new Object();
        pointer = new TestPropertyPointer(null, bean);
        assertEquals(bean, pointer.getBean());
    }

    @Test
    public void testGetPropertyName() {
        // Test when property name is not set
        PropertyPointer pointer = new TestPropertyPointer(null);
        assertNull(pointer.getPropertyName());

        // Test when property name is set
        pointer = new TestPropertyPointer(null);
        pointer.setPropertyName("testProperty");
        assertEquals("testProperty", pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        // Test when property name is not set
        PropertyPointer pointer = new TestPropertyPointer(null);
        assertNull(pointer.getPropertyName());

        // Test when property name is set
        pointer.setPropertyName("testProperty");
        assertEquals("testProperty", pointer.getPropertyName());

        // Test when property name is already set
        pointer.setPropertyName("newProperty");
        assertEquals("newProperty", pointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        // Test when property count is 0
        PropertyPointer pointer = new TestPropertyPointer(null);
        assertEquals(0, pointer.getPropertyCount());

        // Test when property count is greater than 0
        pointer = new TestPropertyPointer(null);
        assertEquals(10, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        // Test when property names are not set
        PropertyPointer pointer = new TestPropertyPointer(null);
        assertNull(pointer.getPropertyNames());

        // Test when property names are set
        pointer = new TestPropertyPointer(null);
        String[] propertyNames = {"name1", "name2", "name3"};
        pointer.setPropertyNames(propertyNames);
        assertArrayEquals(propertyNames, pointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        TestPropertyPointer pointer = new TestPropertyPointer(null);
        assertFalse(pointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        TestPropertyPointer pointer = new TestPropertyPointer(null);
        assertNull(pointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        TestPropertyPointer pointer = new TestPropertyPointer(null);
        assertFalse(pointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        TestPropertyPointer pointer = new TestPropertyPointer(null);
        assertTrue(pointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        TestPropertyPointer pointer = new TestPropertyPointer(null);
        assertEquals(-1, pointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        TestPropertyPointer pointer = new TestPropertyPointer(null);
        assertEquals(pointer, pointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        TestPropertyPointer pointer = new TestPropertyPointer(null);
        JXPathContext context = new JXPathContext(null);
        try {
            pointer.createPath(context);
            fail("Expected JXPathAbstractFactoryException");
        } catch (JXPathAbstractFactoryException e) {
            assertEquals("Factory null could not create an object for path: null", e.getMessage());
        }
    }

    @Test
    public void testCreatePathWithValue() {
        TestPropertyPointer pointer = new TestPropertyPointer(null);
        JXPathContext context = new JXPathContext(null);
        Object value = new Object();
        pointer.createPath(context, value);
        assertEquals(value, pointer.getBaseValue());
    }

    @Test
    public void testCreateChildPathWithValue() {
        TestPropertyPointer pointer = new TestPropertyPointer(null);
        JXPathContext context = new JXPathContext(null);
        Object value = new Object();
        pointer.createChild(context, null, 0, value);
        assertEquals(value, pointer.getImmediateNode());
    }

    @Test
    public void testCreateChildPath() {
        TestPropertyPointer pointer = new TestPropertyPointer(null);
        JXPathContext context = new JXPathContext(null);
        pointer.createChild(context, null, 0);
        assertNull(pointer.getImmediateNode());
    }

    @Test
    public void testHashCode() {
        TestPropertyPointer pointer1 = new TestPropertyPointer(null);
        TestPropertyPointer pointer2 = new TestPropertyPointer(null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        pointer1.setPropertyIndex(5);
        assertNotEquals(pointer1.hashCode(), pointer2.hashCode());

        pointer2.setPropertyIndex(5);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        TestPropertyPointer pointer1 = new TestPropertyPointer(null);
        TestPropertyPointer pointer2 = new TestPropertyPointer(null);
        assertTrue(pointer1.equals(pointer1));
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals(new Object()));
        assertTrue(pointer1.equals(pointer2));

        pointer1.setPropertyIndex(5);
        assertFalse(pointer1.equals(pointer2));
        pointer2.setPropertyIndex(5);
        assertTrue(pointer1.equals(pointer2));

        pointer1.setPropertyName("testProperty");
        assertFalse(pointer1.equals(pointer2));
        pointer2.setPropertyName("testProperty");
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testCompareChildNodePointers() {
        TestPropertyPointer pointer = new TestPropertyPointer(null);
        NodePointer pointer1 = new TestPropertyPointer(null);
        NodePointer pointer2 = new TestPropertyPointer(null);
        assertEquals(0, pointer.compareChildNodePointers(pointer1, pointer2));
    }

    private static class TestPropertyPointer extends PropertyPointer {

        public TestPropertyPointer(NodePointer parent) {
            super(parent);
        }

        public TestPropertyPointer(NodePointer parent, Object bean) {
            super(parent);
            this.bean = bean;
        }

        @Override
        public String getPropertyName() {
            return null;
        }

        @Override
        public void setPropertyName(String propertyName) {
        }

        @Override
        public int getPropertyCount() {
            return 0;
        }

        @Override
        public String[] getPropertyNames() {
            return null;
        }

        @Override
        protected boolean isActualProperty() {
            return false;
        }

    }
}
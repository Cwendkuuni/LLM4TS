import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        propertyPointer.setPropertyName("propertyName");
        assertEquals(new QName(null, "propertyName"), propertyPointer.getName());
    }

    @Test
    public void testGetPropertyName() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        propertyPointer.setPropertyName("propertyName");
        assertEquals("propertyName", propertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        propertyPointer.setPropertyName("propertyName");
        assertEquals("propertyName", propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        assertEquals(0, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        assertArrayEquals(new String[0], propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        assertFalse(propertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        assertNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        assertEquals(-1, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        assertNull(propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        JXPathContext context = new JXPathContext();
        propertyPointer.createPath(context);
        assertNotNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        JXPathContext context = new JXPathContext();
        propertyPointer.createPath(context, "value");
        assertEquals("value", propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreateChild() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        JXPathContext context = new JXPathContext();
        QName name = new QName(null, "child");
        NodePointer child = propertyPointer.createChild(context, name, 0);
        assertNotNull(child);
        assertEquals(propertyPointer, child.getParent());
        assertEquals(name, child.getName());
    }

    @Test
    public void testCreateChildWithValue() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        JXPathContext context = new JXPathContext();
        QName name = new QName(null, "child");
        NodePointer child = propertyPointer.createChild(context, name, 0, "value");
        assertNotNull(child);
        assertEquals(propertyPointer, child.getParent());
        assertEquals(name, child.getName());
        assertEquals("value", child.getNode());
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        int hashCode = propertyPointer.getImmediateParentPointer().hashCode()
            + propertyPointer.getPropertyIndex() + propertyPointer.getIndex();
        assertEquals(hashCode, propertyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new ConcretePropertyPointer(null);
        PropertyPointer propertyPointer2 = new ConcretePropertyPointer(null);
        assertTrue(propertyPointer1.equals(propertyPointer1));
        assertTrue(propertyPointer1.equals(propertyPointer2));
        assertFalse(propertyPointer1.equals(new Object()));
        assertFalse(propertyPointer1.equals(null));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer propertyPointer = new ConcretePropertyPointer(null);
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }

    private class ConcretePropertyPointer extends PropertyPointer {
        public ConcretePropertyPointer(NodePointer parent) {
            super(parent);
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
            return new String[0];
        }

        @Override
        protected boolean isActualProperty() {
            return false;
        }

        @Override
        protected boolean isActual() {
            return false;
        }

        @Override
        protected void setValue(Object value) {

        }

        @Override
        public NodePointer getImmediateValuePointer() {
            return null;
        }

        @Override
        protected Object getNode() {
            return null;
        }
    }
}
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;

    @Before
    public void setUp() {
        propertyPointer = new PropertyPointerTestImpl();
    }

    @Test
    public void testGetPropertyIndex() {
        propertyPointer.setPropertyIndex(5);
        assertEquals(5, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(3);
        assertEquals(3, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        propertyPointer.setBean(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        assertNotNull(propertyPointer.getName());
    }

    @Test
    public void testGetPropertyName() {
        assertNotNull(propertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        String propertyName = "test";
        propertyPointer.setPropertyName(propertyName);
        assertEquals(propertyName, propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        assertTrue(propertyPointer.getPropertyCount() >= 0);
    }

    @Test
    public void testGetPropertyNames() {
        assertNotNull(propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        assertNotNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        assertTrue(propertyPointer.getLength() >= -1);
    }

    @Test
    public void testGetImmediateValuePointer() {
        assertNotNull(propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        assertNotNull(propertyPointer.createPath(null));
    }

    @Test
    public void testCreatePathWithValue() {
        assertNotNull(propertyPointer.createPath(null, null));
    }

    @Test
    public void testCreateChildWithValue() {
        assertNotNull(propertyPointer.createChild(null, null, 0, null));
    }

    @Test
    public void testCreateChildWithoutValue() {
        assertNotNull(propertyPointer.createChild(null, null, 0));
    }

    @Test
    public void testHashCode() {
        assertNotEquals(0, propertyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(propertyPointer.equals(propertyPointer));
        assertFalse(propertyPointer.equals(null));
        assertFalse(propertyPointer.equals(new Object()));
        PropertyPointer other = new PropertyPointerTestImpl();
        assertTrue(propertyPointer.equals(other));
        other.setPropertyIndex(5);
        assertFalse(propertyPointer.equals(other));
        other.setPropertyIndex(propertyPointer.getPropertyIndex());
        other.setPropertyName("test");
        assertFalse(propertyPointer.equals(other));
        other.setPropertyName(propertyPointer.getPropertyName());
        other.setIndex(1);
        assertFalse(propertyPointer.equals(other));
    }

    @Test
    public void testCompareChildNodePointers() {
        assertEquals(0, propertyPointer.compareChildNodePointers(null, null));
    }
    
    private class PropertyPointerTestImpl extends PropertyPointer {

        public PropertyPointerTestImpl() {
            super(null);
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
    }

}
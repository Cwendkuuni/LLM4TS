import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyPointerTest {
    private PropertyPointer propertyPointer;

    @Before
    public void setUp() {
        propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "property";
            }

            @Override
            public void setPropertyName(String propertyName) {

            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property"};
            }

            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };
    }

    @Test
    public void testGetPropertyIndex() {
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(0);
        assertEquals(0, propertyPointer.getPropertyIndex());

        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        assertNull(propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        assertEquals(new QName(null, "property"), propertyPointer.getName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("property", propertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        propertyPointer.setPropertyName("newProperty");
        assertEquals("newProperty", propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        assertEquals(1, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        assertArrayEquals(new String[]{"property"}, propertyPointer.getPropertyNames());
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
        assertNull(propertyPointer.getImmediateNode());
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
        assertEquals(-1, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        assertNull(propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        propertyPointer.createPath(null);
        assertNotNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreatePathWithValue() {
        propertyPointer.createPath(null, "value");
        assertEquals("value", propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreateChildWithPathAndValue() {
        NodePointer nodePointer = propertyPointer.createChild(null, new QName("child"), 0, "value");
        assertNotNull(nodePointer);
        assertEquals("child", nodePointer.getName().getName());
        assertEquals("value", nodePointer.getImmediateNode());
    }

    @Test
    public void testCreateChildWithPathAndIndex() {
        NodePointer nodePointer = propertyPointer.createChild(null, new QName("child"), 0);
        assertNotNull(nodePointer);
        assertEquals("child", nodePointer.getName().getName());
    }

    @Test
    public void testHashCode() {
        PropertyPointer other = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "property";
            }

            @Override
            public void setPropertyName(String propertyName) {

            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property"};
            }

            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };

        assertEquals(propertyPointer.hashCode(), other.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer other = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "property";
            }

            @Override
            public void setPropertyName(String propertyName) {

            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property"};
            }

            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };

        assertTrue(propertyPointer.equals(other));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "property";
            }

            @Override
            public void setPropertyName(String propertyName) {

            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property"};
            }

            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };

        NodePointer pointer2 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "property";
            }

            @Override
            public void setPropertyName(String propertyName) {

            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property"};
            }

            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };

        assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }
}
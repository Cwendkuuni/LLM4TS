import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;

    @Before
    public void setUp() {
        propertyPointer = new PropertyPointer(null) {
            public String getPropertyName() {
                return "testProperty";
            }

            public void setPropertyName(String propertyName) {
                // Do nothing
            }

            public int getPropertyCount() {
                return 1;
            }

            public String[] getPropertyNames() {
                return new String[]{"testProperty"};
            }

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
    }

    @Test
    public void testSetPropertyName() {
        propertyPointer.setPropertyName("newTestProperty");
        assertEquals("newTestProperty", propertyPointer.getPropertyName());
    }

    @Test
    public void testGetBean() {
        assertNull(propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        assertEquals(new QName(null, "testProperty"), propertyPointer.getName());
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
        assertFalse(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        assertEquals(-1, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        assertNotNull(propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        try {
            propertyPointer.createPath(null);
            fail("Should have thrown an exception");
        } catch (JXPathAbstractFactoryException e) {
            assertEquals("Factory null could not create an object for path: null", e.getMessage());
        }
    }

    @Test
    public void testCreatePathWithValue() {
        propertyPointer.createPath(null, "testValue");
        assertEquals("testValue", propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreateChildWithPathAndValue() {
        PropertyPointer childPointer = (PropertyPointer) propertyPointer.createChild(null, new QName(null, "childProperty"), 0, "childValue");
        assertNotNull(childPointer);
        assertEquals("childProperty", childPointer.getPropertyName());
        assertEquals(0, childPointer.getPropertyIndex());
        assertEquals("childValue", childPointer.getImmediateNode());
    }

    @Test
    public void testCreateChildWithPath() {
        PropertyPointer childPointer = (PropertyPointer) propertyPointer.createChild(null, new QName(null, "childProperty"), 0);
        assertNotNull(childPointer);
        assertEquals("childProperty", childPointer.getPropertyName());
        assertEquals(0, childPointer.getPropertyIndex());
        assertNull(childPointer.getImmediateNode());
    }

    @Test
    public void testHashCode() {
        PropertyPointer other = new PropertyPointer(null) {
            public String getPropertyName() {
                return "testProperty";
            }

            public void setPropertyName(String propertyName) {
                // Do nothing
            }

            public int getPropertyCount() {
                return 1;
            }

            public String[] getPropertyNames() {
                return new String[]{"testProperty"};
            }

            protected boolean isActualProperty() {
                return true;
            }
        };
        other.setPropertyIndex(0);
        assertEquals(other.hashCode(), propertyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer other1 = new PropertyPointer(null) {
            public String getPropertyName() {
                return "testProperty";
            }

            public void setPropertyName(String propertyName) {
                // Do nothing
            }

            public int getPropertyCount() {
                return 1;
            }

            public String[] getPropertyNames() {
                return new String[]{"testProperty"};
            }

            protected boolean isActualProperty() {
                return true;
            }
        };
        other1.setPropertyIndex(0);

        PropertyPointer other2 = new PropertyPointer(null) {
            public String getPropertyName() {
                return "testProperty";
            }

            public void setPropertyName(String propertyName) {
                // Do nothing
            }

            public int getPropertyCount() {
                return 1;
            }

            public String[] getPropertyNames() {
                return new String[]{"testProperty"};
            }

            protected boolean isActualProperty() {
                return true;
            }
        };
        other2.setPropertyIndex(1);

        assertTrue(propertyPointer.equals(other1));
        assertFalse(propertyPointer.equals(other2));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new PropertyPointer(null) {
            public String getPropertyName() {
                return "testProperty";
            }

            public void setPropertyName(String propertyName) {
                // Do nothing
            }

            public int getPropertyCount() {
                return 1;
            }

            public String[] getPropertyNames() {
                return new String[]{"testProperty"};
            }

            protected boolean isActualProperty() {
                return true;
            }
        };
        pointer1.setPropertyIndex(0);

        NodePointer pointer2 = new PropertyPointer(null) {
            public String getPropertyName() {
                return "testProperty";
            }

            public void setPropertyName(String propertyName) {
                // Do nothing
            }

            public int getPropertyCount() {
                return 1;
            }

            public String[] getPropertyNames() {
                return new String[]{"testProperty"};
            }

            protected boolean isActualProperty() {
                return true;
            }
        };
        pointer2.setPropertyIndex(1);

        assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }
}
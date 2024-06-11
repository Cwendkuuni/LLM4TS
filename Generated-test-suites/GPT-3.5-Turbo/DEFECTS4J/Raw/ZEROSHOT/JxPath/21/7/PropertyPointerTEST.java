import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;

    @Before
    public void setUp() {
        propertyPointer = new PropertyPointer() {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {
            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };
    }

    @Test
    public void testGetPropertyIndex() {
        int expected = PropertyPointer.UNSPECIFIED_PROPERTY;
        int actual = propertyPointer.getPropertyIndex();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        int expected = 1;
        propertyPointer.setPropertyIndex(expected);
        int actual = propertyPointer.getPropertyIndex();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyIndexMultipleTimes() {
        int expected = 1;
        propertyPointer.setPropertyIndex(expected);
        int actual = propertyPointer.getPropertyIndex();
        assertEquals(expected, actual);

        expected = 2;
        propertyPointer.setPropertyIndex(expected);
        actual = propertyPointer.getPropertyIndex();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBean() {
        Object expected = null;
        Object actual = propertyPointer.getBean();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {
        assertNull(propertyPointer.getName());
    }

    @Test
    public void testGetPropertyName() {
        assertNull(propertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        String expected = "test";
        propertyPointer.setPropertyName(expected);
        String actual = propertyPointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyCount() {
        int expected = 0;
        int actual = propertyPointer.getPropertyCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyNames() {
        String[] expected = new String[0];
        String[] actual = propertyPointer.getPropertyNames();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testIsActualProperty() {
        assertFalse(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        Object expected = new Object();
        propertyPointer.setBaseValue(expected);
        Object actual = propertyPointer.getImmediateNode();
        assertEquals(expected, actual);
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
        int expected = 1;
        propertyPointer.setBaseValue(new Object());
        int actual = propertyPointer.getLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer expected = new NodePointer();
        NodePointer actual = propertyPointer.getImmediateValuePointer();
        assertNotNull(actual);
        assertNotSame(expected, actual);
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        assertFalse(propertyPointer.createPath(context) == null);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        assertFalse(propertyPointer.createPath(context, value) == null);
    }

    @Test
    public void testCreateChildNodePointerWithIndexAndValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName(null, null);
        int index = 0;
        Object value = new Object();
        assertFalse(propertyPointer.createChild(context, name, index, value) == null);
    }

    @Test
    public void testCreateChildNodePointerWithIndex() {
        JXPathContext context = new JXPathContext();
        QName name = new QName(null, null);
        int index = 0;
        assertFalse(propertyPointer.createChild(context, name, index) == null);
    }

    @Test
    public void testHashCode() {
        int expected = propertyPointer.getImmediateParentPointer().hashCode() + propertyPointer.getPropertyIndex() + propertyPointer.getIndex();
        int actual = propertyPointer.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(propertyPointer.equals(propertyPointer));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(propertyPointer.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentParentPointer() {
        PropertyPointer other = new PropertyPointer() {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {
            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };
        assertFalse(propertyPointer.equals(other));
    }

    @Test
    public void testEqualsWithDifferentPropertyIndex() {
        PropertyPointer other = new PropertyPointer() {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {
            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };
        other.setPropertyIndex(1);
        assertFalse(propertyPointer.equals(other));
    }

    @Test
    public void testEqualsWithDifferentPropertyName() {
        PropertyPointer other = new PropertyPointer() {
            public String getPropertyName() {
                return "test";
            }

            public void setPropertyName(String propertyName) {
            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };
        assertFalse(propertyPointer.equals(other));
    }

    @Test
    public void testEqualsWithDifferentIndex() {
        PropertyPointer other = new PropertyPointer() {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {
            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };
        other.setIndex(1);
        assertFalse(propertyPointer.equals(other));
    }

    @Test
    public void testEqualsWithDifferentIndexAndWholeCollection() {
        PropertyPointer other = new PropertyPointer() {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {
            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };
        propertyPointer.setIndex(PropertyPointer.WHOLE_COLLECTION);
        other.setIndex(1);
        assertFalse(propertyPointer.equals(other));
    }

    @Test
    public void testEqualsWithSameParentPointerPropertyIndexAndIndex() {
        PropertyPointer other = new PropertyPointer() {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {
            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };
        other.setParent(propertyPointer.getParent());
        other.setPropertyIndex(propertyPointer.getPropertyIndex());
        other.setIndex(propertyPointer.getIndex());
        assertTrue(propertyPointer.equals(other));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        int expected = propertyPointer.getValuePointer().compareChildNodePointers(pointer1, pointer2);
        int actual = propertyPointer.compareChildNodePointers(pointer1, pointer2);
        assertEquals(expected, actual);
    }
}
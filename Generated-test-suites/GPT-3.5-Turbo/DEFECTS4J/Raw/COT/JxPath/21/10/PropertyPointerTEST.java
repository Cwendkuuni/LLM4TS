import org.junit.Assert;
import org.junit.Test;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        // Set property index
        pointer.setPropertyIndex(1);

        // Assert property index
        Assert.assertEquals(1, pointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        // Assert bean is null
        Assert.assertNull(pointer.getBean());
    }

    @Test
    public void testGetName() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
            public String getPropertyName() {
                return "name";
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

        // Assert name
        Assert.assertEquals("name", pointer.getName().getName());
        Assert.assertNull(pointer.getName().getNamespaceURI());
    }

    @Test
    public void testIsActual() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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
                return true;
            }
        };

        // Assert isActual
        Assert.assertTrue(pointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        // Assert immediate node is null
        Assert.assertNull(pointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        // Assert isCollection is false
        Assert.assertFalse(pointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        // Assert isLeaf is true
        Assert.assertTrue(pointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        // Assert length is -1
        Assert.assertEquals(-1, pointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        // Assert immediate value pointer node is null
        Assert.assertNull(pointer.getImmediateValuePointer().getNode());
    }

    @Test
    public void testCreatePath() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        // Assert create path returns the same instance
        Assert.assertEquals(pointer, pointer.createPath(null));
    }

    @Test
    public void testCreatePathWithValue() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        // Assert create path with value returns the same instance
        Assert.assertEquals(pointer, pointer.createPath(null, "value"));
        Assert.assertEquals("value", pointer.getValue());
    }

    @Test
    public void testCreateChildNodePointerWithValue() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        // Assert create child node pointer with value returns a new PropertyPointer instance
        Assert.assertNotEquals(pointer, pointer.createChild(null, null, 0, "value"));
    }

    @Test
    public void testCreateChildNodePointerWithoutValue() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        // Assert create child node pointer without value returns a new PropertyPointer instance
        Assert.assertNotEquals(pointer, pointer.createChild(null, null, 0));
    }

    @Test
    public void testHashCode() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        int expectedHashCode = pointer.getImmediateParentPointer().hashCode()
                + pointer.getPropertyIndex()
                + pointer.getIndex();

        // Assert hashCode
        Assert.assertEquals(expectedHashCode, pointer.hashCode());
    }

    @Test
    public void testEquals() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        Assert.assertTrue(pointer.equals(pointer));

        // Create a new PropertyPointer instance
        PropertyPointer other = new PropertyPointer(null) {
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

        Assert.assertFalse(pointer.equals(other));
    }

    @Test
    public void testCompareChildNodePointers() {
        // Create a PropertyPointer instance
        PropertyPointer pointer = new PropertyPointer(null) {
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

        PropertyPointer pointer1 = new PropertyPointer(null) {
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

        PropertyPointer pointer2 = new PropertyPointer(null) {
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

        int result = pointer.compareChildNodePointers(pointer1, pointer2);

        // Assert compareChildNodePointers
        Assert.assertEquals(0, result);
    }
}
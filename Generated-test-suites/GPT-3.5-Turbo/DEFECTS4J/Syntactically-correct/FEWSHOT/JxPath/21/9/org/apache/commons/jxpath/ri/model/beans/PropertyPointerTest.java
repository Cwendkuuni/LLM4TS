import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {
    private PropertyPointer pointer;
    private NodePointer parent;

    @Before
    public void setUp() {
        parent = new NodePointer();
        pointer = new PropertyPointer(parent) {
            public String getPropertyName() {
                return "property";
            }

            public void setPropertyName(String propertyName) {
            }

            public int getPropertyCount() {
                return 1;
            }

            public String[] getPropertyNames() {
                return new String[] {"property"};
            }

            protected boolean isActualProperty() {
                return true;
            }
        };
    }

    @Test
    public void testGetPropertyIndex() {
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, pointer.getPropertyIndex());
        pointer.setPropertyIndex(0);
        assertEquals(0, pointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        pointer.setPropertyIndex(0);
        assertEquals(0, pointer.getPropertyIndex());
        pointer.setPropertyIndex(1);
        assertEquals(1, pointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        assertNotNull(pointer.getBean());
    }

    @Test
    public void testGetName() {
        assertEquals(new QName(null, "property"), pointer.getName());
    }

    @Test
    public void testGetImmediateNode() {
        assertNull(pointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        assertFalse(pointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        assertTrue(pointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        assertEquals(-1, pointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        assertNotNull(pointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        pointer.createPath(context);
        assertNotNull(pointer.getImmediateNode());
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        pointer.createPath(context, "value");
        assertEquals("value", pointer.getImmediateNode());
    }

    @Test
    public void testCreateChildWithPath() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("namespace", "name");
        NodePointer child = pointer.createChild(context, name, 0);
        assertNotNull(child);
        assertEquals(pointer.getPropertyName(), child.getPropertyName());
        assertEquals(0, child.getIndex());
    }

    @Test
    public void testCreateChildWithPathWithValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("namespace", "name");
        NodePointer child = pointer.createChild(context, name, 0, "value");
        assertNotNull(child);
        assertEquals(pointer.getPropertyName(), child.getPropertyName());
        assertEquals(0, child.getIndex());
        assertEquals("value", child.getImmediateNode());
    }

    @Test
    public void testHashCode() {
        assertEquals(parent.hashCode() + pointer.getPropertyIndex() + pointer.getIndex(), pointer.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(pointer.equals(new PropertyPointer(parent) {
            public String getPropertyName() {
                return "property";
            }

            public void setPropertyName(String propertyName) {
            }

            public int getPropertyCount() {
                return 1;
            }

            public String[] getPropertyNames() {
                return new String[] {"property"};
            }

            protected boolean isActualProperty() {
                return true;
            }
        }));
        assertFalse(pointer.equals(new PropertyPointer(parent) {
            public String getPropertyName() {
                return "different";
            }

            public void setPropertyName(String propertyName) {
            }

            public int getPropertyCount() {
                return 1;
            }

            public String[] getPropertyNames() {
                return new String[] {"property"};
            }

            protected boolean isActualProperty() {
                return true;
            }
        }));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        assertEquals(0, pointer.compareChildNodePointers(pointer1, pointer2));
    }
}
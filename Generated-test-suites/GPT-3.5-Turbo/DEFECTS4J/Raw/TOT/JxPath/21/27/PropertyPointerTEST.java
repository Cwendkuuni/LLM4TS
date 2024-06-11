import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;

    @Before
    public void setUp() {
        propertyPointer = new PropertyPointer() {
            // Override abstract methods for testing purposes
            public String getPropertyName() {
                return "";
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
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        propertyPointer.setImmediateParentPointer(new NodePointer() {
            public Object getNode() {
                return new Object();
            }
        });
        assertNotNull(propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        propertyPointer.setPropertyName("name");
        assertEquals("name", propertyPointer.getName().getLocalPart());
    }

    @Test
    public void testIsActual() {
        propertyPointer.setActual(false);
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        propertyPointer.setBaseValue("value");
        assertEquals("value", propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        propertyPointer.setBaseValue(new Object[] { "value1", "value2" });
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        propertyPointer.setNode("value");
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        propertyPointer.setBaseValue(new Object[] { "value1", "value2" });
        assertEquals(2, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        propertyPointer.setImmediateParentPointer(new NodePointer() {
            public Object getNode() {
                return new Object();
            }
        });
        propertyPointer.setName(new QName(null, "name"));
        propertyPointer.setImmediateNode("value");
        assertEquals("value", propertyPointer.getImmediateValuePointer().getValue());
    }

    @Test
    public void testCreatePathWithContext() {
        JXPathContext context = new JXPathContext();
        propertyPointer.setBaseValue(null);
        propertyPointer.setBean(new Object());
        propertyPointer.setPropertyName("property1");
        propertyPointer.setIndex(PropertyPointer.WHOLE_COLLECTION);
        try {
            propertyPointer.createPath(context);
        } catch (JXPathAbstractFactoryException e) {
            assertEquals("Factory " + context.getFactory()
                + " could not create an object for path: " + propertyPointer.asPath(), e.getMessage());
        }
    }

    @Test
    public void testCreatePathWithContextAndValue() {
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        propertyPointer.setBaseValue(null);
        propertyPointer.setBean(new Object());
        propertyPointer.setPropertyName("property1");
        propertyPointer.setIndex(0);
        propertyPointer.createPath(context, value);
        assertEquals(value, propertyPointer.getValue());
    }

    @Test
    public void testCreateChildWithContextNameIndexAndValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName(null, "name");
        int index = 1;
        Object value = new Object();
        PropertyPointer child = (PropertyPointer) propertyPointer.createChild(context, name, index, value);
        assertEquals(name.toString(), child.getPropertyName());
        assertEquals(index, child.getIndex());
        assertEquals(value, child.getValue());
    }

    @Test
    public void testCreateChildWithContextNameAndIndex() {
        JXPathContext context = new JXPathContext();
        QName name = new QName(null, "name");
        int index = 1;
        PropertyPointer child = (PropertyPointer) propertyPointer.createChild(context, name, index);
        assertEquals(name.toString(), child.getPropertyName());
        assertEquals(index, child.getIndex());
    }

    @Test
    public void testHashCode() {
        PropertyPointer other = new PropertyPointer() {
            // Override abstract methods for testing purposes
            public String getPropertyName() {
                return "";
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
        other.setImmediateParentPointer(new NodePointer());
        other.setPropertyIndex(1);
        other.setIndex(PropertyPointer.WHOLE_COLLECTION);

        propertyPointer.setImmediateParentPointer(new NodePointer());
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setIndex(PropertyPointer.WHOLE_COLLECTION);

        assertEquals(other.hashCode(), propertyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer other = new PropertyPointer() {
            // Override abstract methods for testing purposes
            public String getPropertyName() {
                return "";
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
        other.setImmediateParentPointer(new NodePointer());
        other.setPropertyIndex(1);
        other.setIndex(PropertyPointer.WHOLE_COLLECTION);

        propertyPointer.setImmediateParentPointer(new NodePointer());
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setIndex(PropertyPointer.WHOLE_COLLECTION);

        assertTrue(propertyPointer.equals(other));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }
}
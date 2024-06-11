import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PropertyPointerTest {
    private PropertyPointer propertyPointer;
    private NodePointer parentPointer;

    @Before
    public void setUp() {
        parentPointer = new NodePointer(null);
        propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public int getPropertyIndex() {
                return 0;
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"testProperty"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };
    }

    @Test
    public void testGetPropertyIndex() {
        assertEquals(0, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());

        propertyPointer.setPropertyIndex(0);
        assertEquals(0, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        propertyPointer.setBean(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        assertEquals("testProperty", propertyPointer.getName().getLocalPart());
    }

    @Test
    public void testGetPropertyCount() {
        assertEquals(1, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        String[] propertyNames = propertyPointer.getPropertyNames();
        assertEquals("testProperty", propertyNames[0]);
        assertNull(propertyNames[1]);
    }

    @Test
    public void testIsActual() {
        assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        assertNull(propertyPointer.getImmediateNode());

        Object testValue = new Object();
        propertyPointer.setValue(testValue);
        assertEquals(testValue, propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testGetLength() {
        assertNotNull(propertyPointer.getValue());
        assertEquals(-1, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        assertNull(propertyPointer.getImmediateValuePointer().getNode());

        Object testValue = new Object();
        propertyPointer.setValue(testValue);
        assertEquals(testValue, propertyPointer.getImmediateValuePointer().getNode());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        propertyPointer.createPath(context);
        assertNotNull(propertyPointer.getNode());
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePathWithInvalidFactory() {
        JXPathContext context = new JXPathContext() {
            @Override
            protected AbstractFactory getAbstractFactory() {
                throw new UnsupportedOperationException();
            }
        };
        propertyPointer.createPath(context);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        Object testValue = new Object();
        propertyPointer.createPath(context, testValue);
        assertEquals(testValue, propertyPointer.getNode());
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        PropertyPointer childProperty = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "testChildProperty";
            }

            @Override
            public int getPropertyIndex() {
                return 0;
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"testChildProperty"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        NodePointer childNodePointer = propertyPointer.createChild(context, null, 0, new Object());
        assertEquals(childProperty, childNodePointer);
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty1";
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty2";
            }
        };
        PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public int getPropertyIndex() {
                return 1;
            }
        };
        PropertyPointer propertyPointer4 = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };

        int hashCode = propertyPointer.hashCode();
        assertEquals(hashCode, propertyPointer1.hashCode());
        assertEquals(hashCode, propertyPointer2.hashCode());
        assertEquals(hashCode, propertyPointer4.hashCode());
        assertNotEquals(hashCode, propertyPointer3.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty1";
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty2";
            }
        };
        PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public int getPropertyIndex() {
                return 1;
            }
        };
        PropertyPointer propertyPointer4 = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };

        assertTrue(propertyPointer.equals(propertyPointer));
        assertFalse(propertyPointer.equals(null));
        assertFalse(propertyPointer.equals(new Object()));
        assertFalse(propertyPointer.equals(propertyPointer1));
        assertFalse(propertyPointer.equals(propertyPointer2));
        assertFalse(propertyPointer.equals(propertyPointer3));
        assertTrue(propertyPointer.equals(propertyPointer4));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer nodePointer1 = new NodePointer(null);
        NodePointer nodePointer2 = new NodePointer(null);
        NodePointer nodePointer3 = new NodePointer(null);
        NodePointer nodePointer4 = new NodePointer(null);

        int comparisonResult = propertyPointer.compareChildNodePointers(nodePointer1, nodePointer2);
        assertEquals(-1, comparisonResult);

        comparisonResult = propertyPointer.compareChildNodePointers(nodePointer3, nodePointer4);
        assertEquals(0, comparisonResult);

        comparisonResult = propertyPointer.compareChildNodePointers(nodePointer1, nodePointer3);
        assertEquals(-1, comparisonResult);

        comparisonResult = propertyPointer.compareChildNodePointers(nodePointer2, nodePointer3);
        assertEquals(0, comparisonResult);

        comparisonResult = propertyPointer.compareChildNodePointers(nodePointer1, nodePointer3);
        assertEquals(-1, comparisonResult);

        comparisonResult = propertyPointer.compareChildNodePointers(nodePointer1, nodePointer1);
        assertEquals(0, comparisonResult);

        comparisonResult = propertyPointer.compareChildNodePointers(nodePointer2, nodePointer1);
        assertEquals(1, comparisonResult);
    }
}
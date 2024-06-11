import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;
    private NodePointer parentPointer;

    @Before
    public void setUp() {
        parentPointer = new NodePointer();
        propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // Not needed for test
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
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        parentPointer.setNode(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        String expectedName = "testProperty";
        assertEquals(expectedName, propertyPointer.getName().toString());
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
        assertNotNull(propertyPointer.getImmediateValuePointer());
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePathWithNullNode() {
        JXPathContext context = new JXPathContext();
        propertyPointer.createPath(context);
    }

    @Test
    public void testCreatePathWithValidNode() {
        JXPathContext context = new JXPathContext();
        Object bean = new Object();
        parentPointer.setNode(bean);
        propertyPointer.createPath(context);
        assertTrue(context.isSetValueCalled()); // Assert that setValue is called
    }

    @Test
    public void testCreatePathWithExistingNode() {
        JXPathContext context = new JXPathContext();
        Object bean = new Object();
        parentPointer.setNode(bean);
        propertyPointer.createPath(context);
        assertFalse(context.isSetValueCalled()); // Assert that setValue is not called
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        propertyPointer.createPath(context, value);
        assertTrue(context.isSetValueCalled()); // Assert that setValue is called
    }

    @Test
    public void testCreateChildWithExistingParent() {
        JXPathContext context = new JXPathContext();
        Object bean = new Object();
        parentPointer.setNode(bean);
        NodePointer child = propertyPointer.createChild(context, null, -1);
        assertEquals(propertyPointer.getPropertyIndex(), child.getPropertyIndex());
    }

    @Test
    public void testCreateChildWithPath() {
        JXPathContext context = new JXPathContext();
        Object bean = new Object();
        parentPointer.setNode(bean);
        NodePointer child = propertyPointer.createChild(context, null, -1, new Object());
        assertEquals(propertyPointer.getPropertyIndex(), child.getPropertyIndex());
        assertTrue(context.isSetValueCalled()); // Assert that setValue is called
    }

    @Test
    public void testHashCode() {
        assertTrue(propertyPointer.hashCode() > 0);
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(propertyPointer.equals(propertyPointer));
    }

    @Test
    public void testEqualsWithDifferentObject() {
        PropertyPointer differentPropertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return "differentProperty";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // Not needed for test
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"differentProperty"};
            }

            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };
        assertFalse(propertyPointer.equals(differentPropertyPointer));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }
}
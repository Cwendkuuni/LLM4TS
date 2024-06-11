import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.JXPathContext;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {
    private PropertyPointer propertyPointer;
    private NodePointer parent;
    private JXPathContext context;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        propertyPointer = new PropertyPointerImpl(parent);
        context = mock(JXPathContext.class);
        when(parent.getImmediateParentContext()).thenReturn(context);
    }

    @Test
    public void testGetPropertyIndex() {
        propertyPointer.setPropertyIndex(5);
        assertEquals(5, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(5);
        assertEquals(5, propertyPointer.getPropertyIndex());

        propertyPointer.setPropertyIndex(8);
        assertEquals(8, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        when(parent.getNode()).thenReturn(bean);

        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        propertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", propertyPointer.getName().getName());
    }

    @Test
    public void testIsActualProperty() {
        assertEquals(true, propertyPointer.isActualProperty());
    }

    @Test
    public void testGetLength() {
        when(parent.getBaseValue()).thenReturn(new Object[] {1, 2, 3});
        assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testCreatePath() {
        AbstractFactory factory = mock(AbstractFactory.class);
        when(context.getFactory(propertyPointer)).thenReturn(factory);
        when(propertyPointer.getAbstractFactory(context)).thenReturn(factory);

        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setIndex(2);
        propertyPointer.createPath(context);

        // Verify that the factory method is called with the correct arguments
        verify(factory).createObject(context, propertyPointer, propertyPointer.getBean(), propertyPointer.getPropertyName(), 2);
    }

    @Test
    public void testCreatePathWithValue() {
        AbstractFactory factory = mock(AbstractFactory.class);
        when(context.getFactory(propertyPointer)).thenReturn(factory);
        when(propertyPointer.getAbstractFactory(context)).thenReturn(factory);

        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setIndex(2);
        propertyPointer.createPath(context, "value");

        // Verify that the factory method is called with the correct arguments
        verify(factory).createObject(context, propertyPointer, propertyPointer.getBean(), propertyPointer.getPropertyName(), 2);
        assertEquals("value", propertyPointer.getNode());
    }

    @Test
    public void testCreateChildWithValue() {
        QName name = new QName(null, "child");
        PropertyPointer childPropertyPointer = (PropertyPointer) propertyPointer.createChild(context, name, 0, "value");

        // Verify that the child property pointer has the correct parent and property name
        assertEquals(propertyPointer, childPropertyPointer.getParent());
        assertEquals("child", childPropertyPointer.getPropertyName());

        // Verify that the child property pointer has the correct index and node value
        assertEquals(0, childPropertyPointer.getIndex());
        assertEquals("value", childPropertyPointer.getNode());
    }

    private class PropertyPointerImpl extends PropertyPointer {
        private String propertyName;

        public PropertyPointerImpl(NodePointer parent) {
            super(parent);
        }

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public int getPropertyCount() {
            return 0;
        }

        public String[] getPropertyNames() {
            return null;
        }

        protected boolean isActualProperty() {
            return true;
        }

        public Object getBaseValue() {
            return null;
        }
    }
}
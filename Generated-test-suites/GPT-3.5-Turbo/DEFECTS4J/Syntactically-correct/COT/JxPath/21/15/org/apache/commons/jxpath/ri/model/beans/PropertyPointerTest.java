import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.JXPathContext;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyIndex(5);
        assertEquals(5, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyIndex(5);
        propertyPointer.setPropertyIndex(10);
        assertEquals(10, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public String getPropertyName() { return null; }
            public void setPropertyName(String propertyName) {}
            public int getPropertyCount() { return 0; }
            public String[] getPropertyNames() { return null; }
            protected boolean isActualProperty() { return true; }
        };
        Object bean = new Object();
        parent.setNode(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public String getPropertyName() { return "test"; }
            public void setPropertyName(String propertyName) {}
            public int getPropertyCount() { return 0; }
            public String[] getPropertyNames() { return null; }
            protected boolean isActualProperty() { return true; }
        };
        assertEquals(new QName(null, "test"), propertyPointer.getName());
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public String getPropertyName() { return null; }
            public void setPropertyName(String propertyName) {}
            public int getPropertyCount() { return 0; }
            public String[] getPropertyNames() { return null; }
            protected boolean isActualProperty() { return true; }
        };
        NodePointer valuePointer = propertyPointer.getImmediateValuePointer();
        assertEquals(propertyPointer, valuePointer.getParent());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public String getPropertyName() { return "test"; }
            public void setPropertyName(String propertyName) {}
            public int getPropertyCount() { return 0; }
            public String[] getPropertyNames() { return null; }
            protected boolean isActualProperty() { return true; }
        };
        propertyPointer.createPath(context);
        assertEquals(propertyPointer, context.getPointer(propertyPointer.asPath()));
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public String getPropertyName() { return "test"; }
            public void setPropertyName(String propertyName) {}
            public int getPropertyCount() { return 0; }
            public String[] getPropertyNames() { return null; }
            protected boolean isActualProperty() { return true; }
        };
        Object value = new Object();
        propertyPointer.createPath(context, value);
        assertEquals(value, propertyPointer.getNode());
    }

    @Test
    public void testCreateChildNodePointerWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public String getPropertyName() { return "test"; }
            public void setPropertyName(String propertyName) {}
            public int getPropertyCount() { return 0; }
            public String[] getPropertyNames() { return null; }
            protected boolean isActualProperty() { return true; }
        };
        propertyPointer.setIndex(0);
        Object value = new Object();
        NodePointer childNodePointer = propertyPointer.createChild(context, null, 0, value);
        assertEquals(value, childNodePointer.getNode());
    }

    @Test
    public void testCreateChildNodePointerWithoutValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public String getPropertyName() { return "test"; }
            public void setPropertyName(String propertyName) {}
            public int getPropertyCount() { return 0; }
            public String[] getPropertyNames() { return null; }
            protected boolean isActualProperty() { return true; }
        };
        propertyPointer.setIndex(0);
        NodePointer childNodePointer = propertyPointer.createChild(context, null, 0);
        assertEquals(null, childNodePointer.getNode());
    }

    @Test
    public void testHashCode() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer1 = new PropertyPointer(parent) {
            public String getPropertyName() { return "test"; }
            public void setPropertyName(String propertyName) {}
            public int getPropertyCount() { return 0; }
            public String[] getPropertyNames() { return null; }
            protected boolean isActualProperty() { return true; }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(parent) {
            public String getPropertyName() { return "test"; }
            public void setPropertyName(String propertyName) {}
            public int getPropertyCount() { return 0; }
            public String[] getPropertyNames() { return null; }
            protected boolean isActualProperty() { return true; }
        };
        propertyPointer1.setPropertyIndex(5);
        propertyPointer2.setPropertyIndex(10);
        assertNotEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        NodePointer parent1 = new NodePointer();
        NodePointer parent2 = new NodePointer();
        PropertyPointer propertyPointer1 = new PropertyPointer(parent1) {
            public String getPropertyName() { return "test"; }
            public void setPropertyName(String propertyName) {}
            public int getPropertyCount() { return 0; }
            public String[] getPropertyNames() { return null; }
            protected boolean isActualProperty() { return true; }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(parent2) {
            public String getPropertyName() { return "test"; }
            public void setPropertyName(String propertyName) {}
            public int getPropertyCount() { return 0; }
            public String[] getPropertyNames() { return null; }
            protected boolean isActualProperty() { return true; }
        };
        propertyPointer1.setPropertyIndex(5);
        propertyPointer2.setPropertyIndex(5);
        assertEquals(propertyPointer1, propertyPointer2);
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public String getPropertyName() { return "test"; }
            public void setPropertyName(String propertyName) {}
            public int getPropertyCount() { return 0; }
            public String[] getPropertyNames() { return null; }
            protected boolean isActualProperty() { return true; }
        };
        NodePointer pointer1 = propertyPointer.getImmediateValuePointer();
        NodePointer pointer2 = propertyPointer.getImmediateValuePointer();
        assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }
}
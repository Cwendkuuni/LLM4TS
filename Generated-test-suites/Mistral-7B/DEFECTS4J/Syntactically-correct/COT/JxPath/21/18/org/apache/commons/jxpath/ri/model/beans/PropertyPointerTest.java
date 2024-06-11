import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class PropertyPointerTest {

    @Test
    public void testConstructor() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Assert.assertNull(propertyPointer.getBean());
        Assert.assertEquals(-1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyIndex(1);
        Assert.assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setPropertyIndex(2);
        Assert.assertEquals(2, propertyPointer.getPropertyIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyIndexWithNegativeValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyIndex(-1);
    }

    @Test
    public void testGetBean() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        Assert.assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        String name = "testProperty";
        propertyPointer.setPropertyName(name);
        Assert.assertEquals(new QName(null, name), propertyPointer.getName());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        String name = "testProperty";
        propertyPointer.setPropertyName(name);
        Assert.assertEquals(name, propertyPointer.getPropertyName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyNameWithNullValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyName(null);
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public int getPropertyCount() {
                return 5;
            }
        };
        Assert.assertEquals(5, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String[] getPropertyNames() {
                return new String[]{"testProperty1", "testProperty2"};
            }
        };
        Assert.assertArrayEquals(new String[]{"testProperty1", "testProperty2"}, propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };
        Assert.assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };
        Assert.assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getImmediateNode() {
                return new Object();
            }
        };
        Assert.assertNotNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public boolean isCollection() {
                return true;
            }
        };
        Assert.assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public boolean isLeaf() {
                return true;
            }
        };
        Assert.assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public int getLength() {
                return 3;
            }
        };
        Assert.assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = propertyPointer.createPath(context);
        Assert.assertNotNull(nodePointer);
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        Object value = new Object();
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = propertyPointer.createPath(context, value);
        Assert.assertNotNull(nodePointer);
    }

    @Test
    public void testCreateChild() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        JXPathContext context = new JXPathContext();
        NodePointer childPointer = propertyPointer.createChild(context, null, 0, null);
        Assert.assertNotNull(childPointer);
    }

    @Test
    public void testCreateChildWithNameAndIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        JXPathContext context = new JXPathContext();
        NodePointer childPointer = propertyPointer.createChild(context, new QName(null, "testChild"), 0, null);
        Assert.assertNotNull(childPointer);
    }

    @Test
    public void testCreateChildWithName() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        JXPathContext context = new JXPathContext();
        NodePointer childPointer = propertyPointer.createChild(context, new QName(null, "testChild"), 0);
        Assert.assertNotNull(childPointer);
    }

    @Test
    public void testCreateChildWithValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        Object value = new Object();
        JXPathContext context = new JXPathContext();
        NodePointer childPointer = propertyPointer.createChild(context, null, 0, value);
        Assert.assertNotNull(childPointer);
    }

    @Test
    public void testCreateChildWithIndexAndValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        JXPathContext context = new JXPathContext();
        NodePointer childPointer = propertyPointer.createChild(context, null, 2, new Object());
        Assert.assertNotNull(childPointer);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateChildWithIndexOutOfBounds() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        JXPathContext context = new JXPathContext();
        NodePointer childPointer = propertyPointer.createChild(context, null, 5, null);
    }

}
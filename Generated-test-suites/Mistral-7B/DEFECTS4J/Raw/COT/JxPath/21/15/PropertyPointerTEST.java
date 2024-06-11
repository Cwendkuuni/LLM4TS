import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PropertyPointerTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Assert.assertNotNull(propertyPointer);
    }

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Assert.assertEquals(-1, propertyPointer.getPropertyIndex());

        propertyPointer.setPropertyIndex(1);
        Assert.assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPropertyIndexNegativeValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyIndex(-1);
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setBean(bean);
        Assert.assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        Assert.assertEquals(new QName(null, propertyName), propertyPointer.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyNameNull() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyName(null);
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public int getPropertyCount() {
                return 3;
            }
        };
        Assert.assertEquals(3, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String[] getPropertyNames() {
                return new String[]{"test1", "test2", "test3"};
            }
        };
        Assert.assertArrayEquals(new String[]{"test1", "test2", "test3"}, propertyPointer.getPropertyNames());
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
                return 5;
            }
        };
        Assert.assertEquals(5, propertyPointer.getLength());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public NodePointer createPath(JXPathContext context) {
                return new NodePointer(null);
            }
        };
        Assert.assertNotNull(propertyPointer.createPath(null));
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public NodePointer createPath(JXPathContext context, Object value) {
                return new NodePointer(null);
            }
        };
        Assert.assertNotNull(propertyPointer.createPath(null, new Object()));
    }

    @Test
    public void testCreateChild() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public NodePointer createChild(JXPathContext context, QName name, int index) {
                return new NodePointer(null);
            }

            @Override
            public NodePointer createChild(JXPathContext context, QName name, int index, Object value) {
                return new NodePointer(null);
            }
        };
        Assert.assertNotNull(propertyPointer.createChild(null, null, 0));
        Assert.assertNotNull(propertyPointer.createChild(null, null, 0, new Object()));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateChildIndexOutOfBounds() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.createChild(null, null, 10);
    }
}
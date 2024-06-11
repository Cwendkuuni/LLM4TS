import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.JXPathContext;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        int expected = PropertyPointer.UNSPECIFIED_PROPERTY;
        int actual = propertyPointer.getPropertyIndex();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        int index = 5;
        propertyPointer.setPropertyIndex(index);
        int expected = index;
        int actual = propertyPointer.getPropertyIndex();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetBean() {
        NodePointer parent = new NodePointer();
        Object bean = new Object();
        parent.setNode(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object expected = bean;
        Object actual = propertyPointer.getBean();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public String getPropertyName() {
                return "propertyName";
            }
        };
        propertyPointer.setPropertyName("propertyName");
        String expectedNamespaceURI = null;
        String expectedLocalName = "propertyName";
        QName expected = new QName(expectedNamespaceURI, expectedLocalName);
        QName actual = propertyPointer.getName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            protected boolean isActualProperty() {
                return true;
            }
        };
        propertyPointer.setActual(true);
        boolean expected = true;
        boolean actual = propertyPointer.isActual();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer();
        Object node = new Object();
        parent.setNode(node);
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public Object getBaseValue() {
                return "baseValue";
            }
        };
        Object expected = "baseValue";
        Object actual = propertyPointer.getImmediateNode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer();
        Object node = new Object();
        parent.setNode(node);
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public Object getBaseValue() {
                return new ArrayList<>();
            }
        };
        boolean expected = true;
        boolean actual = propertyPointer.isCollection();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNotCollection() {
        NodePointer parent = new NodePointer();
        Object node = new Object();
        parent.setNode(node);
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public Object getBaseValue() {
                return "baseValue";
            }
        };
        boolean expected = false;
        boolean actual = propertyPointer.isCollection();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer();
        Object node = new Object();
        parent.setNode(node);
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public Object getNode() {
                return null;
            }
        };
        boolean expected = true;
        boolean actual = propertyPointer.isLeaf();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNotLeaf() {
        NodePointer parent = new NodePointer();
        Object node = new Object();
        parent.setNode(node);
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public Object getNode() {
                return "nodeValue";
            }
        };
        boolean expected = false;
        boolean actual = propertyPointer.isLeaf();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer();
        Object node = new Object();
        parent.setNode(node);
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public int getBaseValue() {
                return 5;
            }
        };
        int expected = 5;
        int actual = propertyPointer.getLength();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer parent = new NodePointer();
        Object node = new Object();
        parent.setNode(node);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setActual(true);
        propertyPointer.setName(new QName(null, "propertyName"));
        propertyPointer.setImmediateNode("nodeValue");
        NodePointer expected = new NodePointer();
        expected.setNode("nodeValue");
        NodePointer actual = propertyPointer.getImmediateValuePointer();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        Object node = new Object();
        parent.setNode(node);
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public Object getBaseValue() {
                return null;
            }
        };
        boolean expected = true;
        boolean actual = propertyPointer.createPath(context) instanceof PropertyPointer;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreatePathError() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        Object node = new Object();
        parent.setNode(node);
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public Object getBaseValue() {
                return null;
            }
        };
        propertyPointer.setPropertyIndex(5);
        try {
            propertyPointer.createPath(context);
            Assert.fail("Expected JXPathAbstractFactoryException not thrown.");
        } catch (JXPathAbstractFactoryException ex) {
            Assert.assertTrue("Factory could not create an object for path.",
                    ex.getMessage().contains("could not create an object for path"));
        }
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        Object node = new Object();
        parent.setNode(node);
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public Object getBaseValue() {
                return new ArrayList<>();
            }
        };
        propertyPointer.setPropertyIndex(0);
        Object expected = "value";
        NodePointer actual = propertyPointer.createPath(context, expected);
        Assert.assertEquals(expected, actual.getValue());
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName(null, "propertyName");
        NodePointer parent = new NodePointer();
        Object node = new Object();
        parent.setNode(node);
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public Object getBaseValue() {
                return null;
            }
        };
        propertyPointer.setActual(true);
        propertyPointer.setIndex(0);
        NodePointer actual = propertyPointer.createChild(context, name, 0, "value");
        Assert.assertEquals("value", actual.getValue());
    }

    @Test
    public void testCreateChildNodePointer() {
        JXPathContext context = new JXPathContext();
        QName name = new QName(null, "propertyName");
        NodePointer parent = new NodePointer();
        Object node = new Object();
        parent.setNode(node);
        PropertyPointer propertyPointer = new PropertyPointer(parent) {
            public Object getBaseValue() {
                return null;
            }
        };
        propertyPointer.setActual(true);
        propertyPointer.setIndex(0);
        NodePointer actual = propertyPointer.createChild(context, name, 0);
        Assert.assertTrue(actual instanceof PropertyPointer);
    }

    @Test
    public void testHashCode() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        int expected = parent.hashCode() + propertyPointer.getPropertyIndex() + propertyPointer.getIndex();
        int actual = propertyPointer.hashCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer1 = new PropertyPointer(parent);
        PropertyPointer propertyPointer2 = new PropertyPointer(parent);
        propertyPointer1.setPropertyIndex(5);
        propertyPointer1.setPropertyName("propertyName");
        propertyPointer2.setPropertyIndex(5);
        propertyPointer2.setPropertyName("propertyName");
        boolean expected = true;
        boolean actual = propertyPointer1.equals(propertyPointer2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNotEquals() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer1 = new PropertyPointer(parent);
        PropertyPointer propertyPointer2 = new PropertyPointer(parent);
        propertyPointer1.setPropertyIndex(5);
        propertyPointer1.setPropertyName("propertyName");
        propertyPointer2.setPropertyIndex(10);
        propertyPointer2.setPropertyName("propertyName");
        boolean expected = false;
        boolean actual = propertyPointer1.equals(propertyPointer2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(null);
        int expected = propertyPointer.getValuePointer().compareChildNodePointers(pointer1, pointer2);
        int actual = propertyPointer.compareChildNodePointers(pointer1, pointer2);
        Assert.assertEquals(expected, actual);
    }

}
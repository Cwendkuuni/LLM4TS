import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testSetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setPropertyIndex(2);
        Assert.assertEquals(2, propertyPointer.getPropertyIndex());
    }

    @Test(expected = IllegalArgumentException.class)
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
    public void testIsActual() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public boolean isActualProperty() {
                return true;
            }
        };
        Assert.assertTrue(propertyPointer.isActual());

        propertyPointer = new PropertyPointer(null) {
            @Override
            public boolean isActualProperty() {
                return false;
            }
        };
        Assert.assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        Object value = new Object();
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return value;
            }
        };
        Assert.assertEquals(value, propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return new ArrayList<>();
            }
        };
        Assert.assertTrue(propertyPointer.isCollection());

        propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return new String("test");
            }
        };
        Assert.assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return new ArrayList<>();
            }
        };
        Assert.assertEquals(0, propertyPointer.getLength());

        propertyPointer.setIndex(1);
        Assert.assertEquals(1, propertyPointer.getLength());

        List list = new ArrayList<>();
        list.add(new Object());
        list.add(new Object());
        propertyPointer.setBaseValue(list);
        Assert.assertEquals(2, propertyPointer.getLength());
    }

    @Test
    public void testCreatePath() {
        Object bean = new Object();
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return bean;
            }

            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        PropertyPointer result = propertyPointer.createPath(null);
        Assert.assertEquals(propertyPointer, result);
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePathException() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                throw new RuntimeException();
            }
        };
        propertyPointer.createPath(null);
    }

    @Test
    public void testCreateChild() {
        Object bean = new Object();
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return bean;
            }

            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        PropertyPointer child = propertyPointer.createChild(null, null, 0, new Object());
        Assert.assertEquals(propertyPointer, child.getImmediateParentPointer());
        Assert.assertEquals("testProperty", child.getName().getLocalPart());
        Assert.assertEquals(new Object(), child.getNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildNegativeIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return new ArrayList<>();
            }
        };
        propertyPointer.createChild(null, null, -1, null);
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public int getPropertyCount() {
                return 1;
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public int getPropertyCount() {
                return 1;
            }
        };
        NodePointer parent1 = new NodePointer(null);
        NodePointer parent2 = new NodePointer(null);
        PropertyPointer propertyPointer3 = new PropertyPointer(parent1);
        PropertyPointer propertyPointer4 = new PropertyPointer(parent2);
        PropertyPointer propertyPointer5 = new PropertyPointer(parent1) {
            @Override
            public int getPropertyCount() {
                return 2;
            }
        };
        Assert.assertEquals(propertyPointer3.hashCode(), propertyPointer4.hashCode());
        Assert.assertEquals(propertyPointer3.hashCode(), propertyPointer5.hashCode());
        Assert.assertEquals(propertyPointer3.hashCode(), propertyPointer3.hashCode());
        Assert.assertEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
        Assert.assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());
        Assert.assertNotEquals(propertyPointer3.hashCode(), propertyPointer1.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public int getPropertyCount() {
                return 1;
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public int getPropertyCount() {
                return 1;
            }
        };
        NodePointer parent1 = new NodePointer(null);
        NodePointer parent2 = new NodePointer(null);
        PropertyPointer propertyPointer3 = new PropertyPointer(parent1);
        PropertyPointer propertyPointer4 = new PropertyPointer(parent2);
        PropertyPointer propertyPointer5 = new PropertyPointer(parent1) {
            @Override
            public int getPropertyCount() {
                return 2;
            }
        };
        Assert.assertFalse(propertyPointer3.equals(propertyPointer4));
        Assert.assertFalse(propertyPointer3.equals(propertyPointer5));
        Assert.assertFalse(propertyPointer3.equals(propertyPointer1));
        Assert.assertTrue(propertyPointer3.equals(propertyPointer3));
        Assert.assertTrue(propertyPointer1.equals(propertyPointer1));
        Assert.assertTrue(propertyPointer3.equals(propertyPointer3));
        Assert.assertTrue(propertyPointer3.equals(propertyPointer4));
        Assert.assertTrue(propertyPointer3.equals(propertyPointer5));
        Assert.assertTrue(propertyPointer1.equals(propertyPointer2));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public int getPropertyCount() {
                return 1;
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public int getPropertyCount() {
                return 1;
            }
        };
        NodePointer parent1 = new NodePointer(null);
        NodePointer parent2 = new NodePointer(null);
        PropertyPointer propertyPointer3 = new PropertyPointer(parent1);
        PropertyPointer propertyPointer4 = new PropertyPointer(parent2);
        PropertyPointer propertyPointer5 = new PropertyPointer(parent1) {
            @Override
            public int getPropertyCount() {
                return 2;
            }
        };
        NodePointer child1 = new NodePointer(null);
        NodePointer child2 = new NodePointer(null);
        NodePointer child3 = new NodePointer(null);
        NodePointer child4 = new NodePointer(null);
        propertyPointer1.setValue(child1);
        propertyPointer2.setValue(child2);
        propertyPointer3.setValue(child3);
        propertyPointer4.setValue(child4);
        propertyPointer5.setValue(child3);

        Assert.assertTrue(propertyPointer1.compareChildNodePointers(propertyPointer3, propertyPointer3) >= 0);
        Assert.assertTrue(propertyPointer1.compareChildNodePointers(propertyPointer3, propertyPointer5) < 0);
        Assert.assertTrue(propertyPointer1.compareChildNodePointers(propertyPointer5, propertyPointer3) > 0);
        Assert.assertTrue(propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer3) < 0);
        Assert.assertTrue(propertyPointer1.compareChildNodePointers(propertyPointer3, propertyPointer2) > 0);
        Assert.assertTrue(propertyPointer2.compareChildNodePointers(propertyPointer3, propertyPointer3) >= 0);
        Assert.assertTrue(propertyPointer2.compareChildNodePointers(propertyPointer3, propertyPointer5) < 0);
        Assert.assertTrue(propertyPointer2.compareChildNodePointers(propertyPointer5, propertyPointer3) > 0);
        Assert.assertTrue(propertyPointer2.compareChildNodePointers(propertyPointer1, propertyPointer3) < 0);
        Assert.assertTrue(propertyPointer2.compareChildNodePointers(propertyPointer3, propertyPointer1) > 0);
        Assert.assertTrue(propertyPointer3.compareChildNodePointers(propertyPointer1, propertyPointer3) >= 0);
        Assert.assertTrue(propertyPointer3.compareChildNodePointers(propertyPointer3, propertyPointer3) >= 0);
        Assert.assertTrue(propertyPointer3.compareChildNodePointers(propertyPointer4, propertyPointer3) < 0);
        Assert.assertTrue(propertyPointer3.compareChildNodePointers(propertyPointer5, propertyPointer3) > 0);
        Assert.assertTrue(propertyPointer3.compareChildNodePointers(propertyPointer1, propertyPointer5) < 0);
        Assert.assertTrue(propertyPointer3.compareChildNodePointers(propertyPointer5, propertyPointer1) > 0);
        Assert.assertTrue(propertyPointer4.compareChildNodePointers(propertyPointer3, propertyPointer3) >= 0);
        Assert.assertTrue(propertyPointer4.compareChildNodePointers(propertyPointer3, propertyPointer5) < 0);
        Assert.assertTrue(propertyPointer4.compareChildNodePointers(propertyPointer5, propertyPointer3) > 0);
        Assert.assertTrue(propertyPointer4.compareChildNodePointers(propertyPointer1, propertyPointer3) < 0);
        Assert.assertTrue(propertyPointer4.compareChildNodePointers(propertyPointer3, propertyPointer1) > 0);
        Assert.assertTrue(propertyPointer5.compareChildNodePointers(propertyPointer3, propertyPointer3) >= 0);
        Assert.assertTrue(propertyPointer5.compareChildNodePointers(propertyPointer3, propertyPointer5) >= 0);
        Assert.assertTrue(propertyPointer5.compareChildNodePointers(propertyPointer1, propertyPointer3) < 0);
        Assert.assertTrue(propertyPointer5.compareChildNodePointers(propertyPointer3, propertyPointer1) > 0);
        Assert.assertTrue(propertyPointer5.compareChildNodePointers(propertyPointer4, propertyPointer3) < 0);
        Assert.assertTrue(propertyPointer5.compareChildNodePointers(propertyPointer3, propertyPointer4) > 0);
    }
}
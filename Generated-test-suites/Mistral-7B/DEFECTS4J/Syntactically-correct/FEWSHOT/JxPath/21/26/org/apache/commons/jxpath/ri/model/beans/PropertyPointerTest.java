package org.apache.commons.cli;

import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointer() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        Assert.assertEquals(-1, propertyPointer.getPropertyIndex());
        Assert.assertNull(propertyPointer.getBean());

        propertyPointer.setPropertyIndex(123);
        Assert.assertEquals(123, propertyPointer.getPropertyIndex());

        Object bean = new Object();
        propertyPointer.setBean(bean);
        Assert.assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        propertyPointer.setPropertyName("propertyName");
        Assert.assertEquals("propertyName", propertyPointer.getName().getLocalPart());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer()) {
            @Override
            public int getPropertyCount() {
                return 42;
            }
        };
        Assert.assertEquals(42, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer()) {
            @Override
            public String[] getPropertyNames() {
                return new String[]{"name1", "name2"};
            }
        };
        Assert.assertArrayEquals(new String[]{"name1", "name2"}, propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        Assert.assertFalse(propertyPointer.isActualProperty());

        propertyPointer = new ActualPropertyPointer(new DummyNodePointer());
        Assert.assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        Object expectedValue = new Object();
        propertyPointer.setValue(expectedValue);
        Object actualValue = propertyPointer.getImmediateNode();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        Assert.assertFalse(propertyPointer.isCollection());

        propertyPointer = new DummyPropertyPointer(new DummyNodePointer()) {
            @Override
            public boolean isCollection() {
                return true;
            }
        };
        Assert.assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer()) {
            @Override
            public int getLength() {
                return 42;
            }
        };
        Assert.assertEquals(42, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        Object expectedValue = new Object();
        NodePointer expectedValuePointer = new DummyNodePointer();
        propertyPointer.setValue(expectedValue);
        propertyPointer.setValuePointer(expectedValuePointer);
        NodePointer actualValuePointer = propertyPointer.getImmediateValuePointer();
        Assert.assertEquals(expectedValuePointer, actualValuePointer);
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        Object expectedBean = new Object();
        String expectedPropertyName = "propertyName";
        propertyPointer.setBean(expectedBean);
        propertyPointer.setPropertyName(expectedPropertyName);
        JXPathContext context = new JXPathContext();
        NodePointer result = propertyPointer.createPath(context);
        Assert.assertEquals(propertyPointer, result);
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        Object expectedBean = new Object();
        String expectedPropertyName = "propertyName";
        propertyPointer.setBean(expectedBean);
        propertyPointer.setPropertyName(expectedPropertyName);
        JXPathContext context = new JXPathContext();
        Object expectedValue = new Object();
        NodePointer result = propertyPointer.createPath(context, expectedValue);
        Assert.assertEquals(expectedValue, result.getNode());
    }

    @Test
    public void testCreateChild() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        JXPathContext context = new JXPathContext();
        QName name = new QName("namespace:name");
        int index = 123;
        Object value = new Object();
        NodePointer child = propertyPointer.createChild(context, name, index, value);
        Assert.assertEquals(1, child.getChildCount());
        Assert.assertEquals(name.getLocalPart(), child.getName().getLocalPart());
        Assert.assertEquals(value, child.getNode());
    }

    @Test
    public void testCreateChildWithName() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        JXPathContext context = new JXPathContext();
        QName name = new QName("namespace:name");
        int index = 123;
        NodePointer child = propertyPointer.createChild(context, name, index);
        Assert.assertEquals(1, child.getChildCount());
        Assert.assertEquals(name.getLocalPart(), child.getName().getLocalPart());
        Assert.assertNull(child.getNode());
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer1 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer2 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer3 = new DummyPropertyPointer(new DummyNodePointer()) {
            @Override
            public int hashCode() {
                return 123;
            }
        };
        Assert.assertNotEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
        Assert.assertNotEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
        Assert.assertEquals(propertyPointer2.hashCode(), propertyPointer2.hashCode());
        PropertyPointer propertyPointer4 = propertyPointer1.clone();
        Assert.assertEquals(propertyPointer1.hashCode(), propertyPointer4.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer2 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer3 = new DummyPropertyPointer(new DummyNodePointer()) {
            @Override
            public boolean equals(Object obj) {
                return true;
            }
        };
        PropertyPointer propertyPointer4 = propertyPointer1.clone();
        Assert.assertNotEquals(propertyPointer1, propertyPointer2);
        Assert.assertNotEquals(propertyPointer1, propertyPointer3);
        Assert.assertEquals(propertyPointer1, propertyPointer4);
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer propertyPointer1 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer2 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer3 = new DummyPropertyPointer(new DummyNodePointer()) {
            @Override
            public NodePointer getValuePointer() {
                return new DummyNodePointer();
            }
        };
        Assert.assertLessThan(0, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer3));
    }
}

class DummyPropertyPointer extends PropertyPointer {
    public DummyPropertyPointer(NodePointer parent) {
        super(parent);
    }

    @Override
    public String getPropertyName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPropertyName(String propertyName) {
        throw new UnsupportedOperationException();
    }
}

class ActualPropertyPointer extends DummyPropertyPointer {
    public ActualPropertyPointer(NodePointer parent) {
        super(parent);
    }

    @Override
    public String getPropertyName() {
        return "propertyName";
    }

    @Override
    public void setPropertyName(String propertyName) {
        throw new UnsupportedOperationException();
    }
}

class DummyNodePointer implements NodePointer {
    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public NodePointer getChildNode(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NodePointer getParentNode() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Object getNode() {
        return null;
    }

    @Override
    public Object getBaseValue() {
        return null;
    }

    @Override
    public NodePointer clone() {
        return new DummyNodePointer();
    }

    @Override
    public boolean isActual() {
        return false;
    }

    @Override
    public NodePointer getValuePointer() {
        return this;
    }

    @Override
    public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
        return 0;
    }
}
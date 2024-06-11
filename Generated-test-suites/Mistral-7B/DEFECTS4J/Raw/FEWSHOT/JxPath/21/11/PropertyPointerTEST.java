package org.apache.commons.cli;

import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PropertyPointerTest {

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new TestPropertyPointer(null, 0);
        Assert.assertTrue(propertyPointer.isActualProperty());

        PropertyPointer collectionPropertyPointer = new CollectionPropertyPointer(null, 0);
        Assert.assertFalse(collectionPropertyPointer.isActualProperty());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = new TestPropertyPointer(new Object[]{"name1", "name2"}, 0);
        Assert.assertEquals(2, propertyPointer.getPropertyCount());

        PropertyPointer collectionPropertyPointer = new CollectionPropertyPointer(new Object[]{"name1", "name2"}, 1);
        Assert.assertEquals(1, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer propertyPointer = new TestPropertyPointer(new Object[]{"name1", "name2"}, 0);
        Assert.assertArrayEquals(new String[]{"name1", "name2"}, propertyPointer.getPropertyNames());

        PropertyPointer collectionPropertyPointer = new CollectionPropertyPointer(new Object[]{"name1", "name2"}, 1);
        Assert.assertArrayEquals(new String[]{""}, collectionPropertyPointer.getPropertyNames());
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new TestPropertyPointer(null, 0);
        Assert.assertEquals(new QName("", ""), propertyPointer.getName());

        PropertyPointer propertyPointerWithName = new TestPropertyPointer(null, 0, "propertyName");
        Assert.assertEquals(new QName("", "propertyName"), propertyPointerWithName.getName());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer propertyPointer = new TestPropertyPointer(null, 0);
        propertyPointer.setPropertyName("newPropertyName");
        Assert.assertEquals(new QName("", "newPropertyName"), propertyPointer.getName());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new TestPropertyPointer(new Object[0], 0);
        Assert.assertFalse(propertyPointer.isCollection());

        PropertyPointer collectionPropertyPointer = new CollectionPropertyPointer(new Object[]{}, 0);
        Assert.assertTrue(collectionPropertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = new TestPropertyPointer(new Object[]{1}, 0);
        Assert.assertFalse(propertyPointer.isLeaf());

        PropertyPointer leafPropertyPointer = new TestPropertyPointer(new Object[]{1}, 0, "propertyName");
        Assert.assertTrue(leafPropertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new CollectionPropertyPointer(new Object[]{}, 0);
        Assert.assertEquals(-1, propertyPointer.getLength());

        PropertyPointer arrayPropertyPointer = new CollectionPropertyPointer(new Object[]{1, 2, 3}, 0);
        Assert.assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = new TestPropertyPointer(new Object[]{1}, 0, "propertyName");
        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
        Assert.assertEquals(new TestNodePointer(1), immediateValuePointer);
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = new TestPropertyPointer(new Object[]{1}, 0, "propertyName");
        NodePointer path = propertyPointer.createPath(new JXPathContext() {});
        Assert.assertEquals(propertyPointer, path);
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = new TestPropertyPointer(new Object[]{1}, 0, "propertyName");
        NodePointer path = propertyPointer.createPath(new JXPathContext(), new Object[]{2});
        Assert.assertEquals(new TestPropertyPointer(new Object[]{2}, 0, "propertyName"), path);
    }

    @Test
    public void testCreateChild() {
        PropertyPointer propertyPointer = new TestPropertyPointer(new Object[]{1, 2}, 0, "propertyName");
        NodePointer child = propertyPointer.createChild(new JXPathContext(), new QName(""), 1, new Object[]{3});
        Assert.assertEquals(new TestPropertyPointer(new Object[]{1, 2, 3}, 0, "propertyName"), child);
    }

    @Test
    public void testCreateChildWithName() {
        PropertyPointer propertyPointer = new TestPropertyPointer(new Object[]{1, 2}, 0, "propertyName");
        NodePointer child = propertyPointer.createChild(new JXPathContext(), new QName("newName"), 1);
        Assert.assertEquals(new TestPropertyPointer(new Object[]{1, 2}, 0, "newName"), child);
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer1 = new TestPropertyPointer(new Object[]{1}, 0, "propertyName");
        PropertyPointer propertyPointer2 = new TestPropertyPointer(new Object[]{1}, 0, "propertyName");
        PropertyPointer propertyPointer3 = new TestPropertyPointer(new Object[]{1}, 1, "propertyName");

        Assert.assertEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
        Assert.assertNotEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new TestPropertyPointer(new Object[]{1}, 0, "propertyName");
        PropertyPointer propertyPointer2 = new TestPropertyPointer(new Object[]{1}, 0, "propertyName");
        PropertyPointer propertyPointer3 = new TestPropertyPointer(new Object[]{1}, 1, "propertyName");

        Assert.assertTrue(propertyPointer1.equals(propertyPointer2));
        Assert.assertFalse(propertyPointer1.equals(propertyPointer3));
    }

    @Test
    public void testCompareChildNodePointers() {
        TestNodePointer nodePointer1 = new TestNodePointer(1);
        TestNodePointer nodePointer2 = new TestNodePointer(1);
        TestNodePointer nodePointer3 = new TestNodePointer(2);

        PropertyPointer propertyPointer = new TestPropertyPointer(new Object[]{1}, 0, "propertyName");
        int comparisonResult = propertyPointer.compareChildNodePointers(nodePointer1, nodePointer2);
        Assert.assertEquals(0, comparisonResult);

        comparisonResult = propertyPointer.compareChildNodePointers(nodePointer1, nodePointer3);
        Assert.assertEquals(-1, comparisonResult);

        comparisonResult = propertyPointer.compareChildValuePointers(nodePointer1, nodePointer3);
        Assert.assertEquals(0, comparisonResult);

        comparisonResult = propertyPointer.compareChildValuePointers(nodePointer1, nodePointer1);
        Assert.assertEquals(0, comparisonResult);
    }

    private static class TestNodePointer implements NodePointer {
        private int value;

        public TestNodePointer(int value) {
            this.value = value;
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public Object getNode() {
            return value;
        }

        @Override
        public int getIndex() {
            return 0;
        }

        @Override
        public NodePointer getParentPointer() {
            return null;
        }

        @Override
        public NodePointer clone() {
            return new TestNodePointer(value);
        }

        @Override
        public NodePointer getValuePointer() {
            return this;
        }

        @Override
        public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
            return 0;
        }

        @Override
        public int compareChildValuePointers(NodePointer pointer1, NodePointer pointer2) {
            return 0;
        }
    }

    private static class TestPropertyPointer extends PropertyPointer {
        private Object[] values;
        private int valueIndex;
        private String propertyName;

        public TestPropertyPointer(Object[] values, int index, String propertyName) {
            super(new TestNodePointer(values), index);
            this.values = values;
            this.valueIndex = index;
            this.propertyName = propertyName;
        }

        @Override
        public String getPropertyName() {
            return propertyName;
        }

        @Override
        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public Object getBaseValue() {
            return values;
        }

        @Override
        public boolean isActualProperty() {
            return true;
        }
    }

    private static class CollectionPropertyPointer extends PropertyPointer {
        private Object[] values;

        public CollectionPropertyPointer(Object[] values, int index) {
            super(new CollectionNodePointer(values), index);
            this.values = values;
        }

        @Override
        public String getPropertyName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPropertyName(String propertyName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getBaseValue() {
            return values;
        }

        @Override
        public boolean isActualProperty() {
            return false;
        }
    }

    private static class CollectionNodePointer implements NodePointer {
        private Object[] values;

        public CollectionNodePointer(Object[] values) {
            this.values = values;
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public Object getNode() {
            return values;
        }

        @Override
        public int getIndex() {
            return -1;
        }

        @Override
        public NodePointer getParentPointer() {
            return null;
        }

        @Override
        public NodePointer clone() {
            return new CollectionNodePointer(values);
        }

        @Override
        public NodePointer getValuePointer() {
            throw new UnsupportedOperationException();
        }
    }
}
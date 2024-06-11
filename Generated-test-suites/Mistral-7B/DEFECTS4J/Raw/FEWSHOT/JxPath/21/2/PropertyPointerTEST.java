package org.apache.commons.cli;

import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointer() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "name";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name1", "name2", "name3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        Assert.assertEquals("name", propertyPointer.getPropertyName());
        Assert.assertArrayEquals(new String[]{"name1", "name2", "name3"}, propertyPointer.getPropertyNames());
        Assert.assertTrue(propertyPointer.isActual());
        Assert.assertEquals(3, propertyPointer.getPropertyCount());
    }

    @Test(expected = AssertionError.class)
    public void testPropertyPointerInvalidPropertyName() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name1", "name2", "name3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        propertyPointer.setPropertyName(null);
    }

    @Test
    public void testPropertyPointerHashCode() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "name1";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name1", "name2", "name3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "name1";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name1", "name2", "name3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        PropertyPointer propertyPointer3 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "name2";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name1", "name2", "name3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        Assert.assertEquals(System.identityHashCode(propertyPointer1), propertyPointer1.hashCode());
        Assert.assertEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
        Assert.assertNotEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
    }

    @Test
    public void testPropertyPointerEquals() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "name1";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name1", "name2", "name3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "name1";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name1", "name2", "name3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        PropertyPointer propertyPointer3 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "name2";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name1", "name2", "name3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        PropertyPointer propertyPointer4 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "name1";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name1", "name2", "name4"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        Assert.assertTrue(propertyPointer1.equals(propertyPointer2));
        Assert.assertFalse(propertyPointer1.equals(propertyPointer3));
        Assert.assertFalse(propertyPointer1.equals(propertyPointer4));
        Assert.assertFalse(propertyPointer1.equals(null));
        Assert.assertFalse(propertyPointer1.equals(new Object()));
    }

    @Test
    public void testPropertyPointerCompareChildNodePointers() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "name1";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name1", "name2", "name3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "name1";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name1", "name2", "name3"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        NodePointer nodePointer1 = new NodePointer(propertyPointer1);
        NodePointer nodePointer2 = new NodePointer(propertyPointer2);

        NodePointer childNodePointer1 = new NodePointer(propertyPointer1.createChild(null, new QName("child1"), 0, null));
        NodePointer childNodePointer2 = new NodePointer(propertyPointer2.createChild(null, new QName("child1"), 0, null));

        Assert.assertEquals(0, propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer2));
        Assert.assertEquals(0, propertyPointer1.compareChildNodePointers(childNodePointer1, childNodePointer2));

        NodePointer childNodePointer3 = new NodePointer(propertyPointer1.createChild(null, new QName("child2"), 0, null));
        Assert.assertNotEquals(0, propertyPointer1.compareChildNodePointers(childNodePointer1, childNodePointer3));
    }
}
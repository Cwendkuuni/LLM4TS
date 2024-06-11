package org.apache.commons.cli;

import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerBasic() {
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
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        Assert.assertEquals("name", propertyPointer.getName().getLocalPart());
        Assert.assertEquals(1, propertyPointer.getPropertyCount());
        Assert.assertArrayEquals(new String[]{"name"}, propertyPointer.getPropertyNames());
        Assert.assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerCollection() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "list";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 5;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"item1", "item2", "item3", "item4", "item5"};
            }

            @Override
            public boolean isActualProperty() {
                return false;
            }
        };

        Assert.assertEquals("list", propertyPointer.getName().getLocalPart());
        Assert.assertArrayEquals(new String[]{"item1", "item2", "item3", "item4", "item5"}, propertyPointer.getPropertyNames());
        Assert.assertFalse(propertyPointer.isActual());
        Assert.assertFalse(propertyPointer.isLeaf());
        Assert.assertTrue(propertyPointer.isCollection());
        Assert.assertEquals(5, propertyPointer.getLength());
    }

    @Test
    public void testPropertyPointerValues() {
        Object bean = new Object() {
            int value = 10;
            int[] list = {1, 2, 3, 4, 5};

            public int getValue() {
                return value;
            }

            public int[] getList() {
                return list;
            }
        };

        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "value";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"value"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "list";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 5;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"item1", "item2", "item3", "item4", "item5"};
            }

            @Override
            public boolean isActualProperty() {
                return false;
            }
        };

        propertyPointer1.setBean(bean);
        propertyPointer2.setBean(bean);

        Assert.assertEquals(10, propertyPointer1.getImmediateNode());
        Assert.assertNull(propertyPointer1.getValue());
        Assert.assertEquals(Arrays.asList(1, 2, 3, 4, 5), Arrays.asList(propertyPointer2.getImmediateNode()));
    }
}
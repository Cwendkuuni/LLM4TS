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
                return "propertyName";
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

        Assert.assertEquals(Integer.MIN_VALUE, propertyPointer.getPropertyIndex());
        Assert.assertNull(propertyPointer.getBean());
        Assert.assertEquals("propertyName", propertyPointer.getPropertyName());
        Assert.assertEquals(3, propertyPointer.getPropertyCount());
        Assert.assertArrayEquals(new String[]{"name1", "name2", "name3"}, propertyPointer.getPropertyNames());
        Assert.assertTrue(propertyPointer.isActual());

        propertyPointer.setPropertyIndex(1);
        Assert.assertEquals(1, propertyPointer.getPropertyIndex());

        Object bean = new Object();
        propertyPointer.setBean(bean);
        Assert.assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "propertyName";
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

            @Override
            public Object getBaseValue() {
                return new Object(){
                    Object[] values = new Object[]{1, 2, 3};

                    public Object get(int index) {
                        return values[index];
                    }
                };
            }
        };

        Assert.assertNull(propertyPointer.getImmediateNode());
        propertyPointer.setIndex(1);
        Assert.assertEquals(2, ((Number) propertyPointer.getImmediateNode()).intValue());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "propertyName";
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

            @Override
            public Object getBaseValue() {
                return new Object[]{1, 2, 3};
            }
        };

        Assert.assertTrue(propertyPointer.isCollection());
        PropertyPointer childPointer = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "childProperty";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 2;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"child1", "child2"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        Assert.assertFalse(childPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "propertyName";
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

            @Override
            public Object getBaseValue() {
                return new String("value");
            }
        };

        Assert.assertTrue(propertyPointer.isLeaf());
        PropertyPointer childPointer = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "childProperty";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // no-op
            }

            @Override
            public int getPropertyCount() {
                return 2;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"child1", "child2"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        Assert.assertFalse(childPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "propertyName";
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

            @Override
            public Object getBaseValue() {
                return new Object[]{1, 2, 3};
            }
        };

        Assert.assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "propertyName";
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

        Object value = new Object(){
            int value = 42;

            public int getValue() {
                return value;
            }
        };

        propertyPointer.setValue(value);

        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();

        Assert.assertEquals(PropertyPointer.class, immediateValuePointer.getClass());
        Assert.assertEquals("propertyName", immediateValuePointer.getName().getLocalPart());
        Assert.assertEquals(value, immediateValuePointer.getNode());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "propertyName";
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

            @Override
            public Object getBaseValue() {
                return new Object[]{1, 2, 3};
            }
        };

        JXPathContext context = new JXPathContext();
        context.setPropertyValueMap(Arrays.asList(new AbstractMap.SimpleEntry<>("bean", new Object(){
            String name = "beanName";

            public String getName() {
                return name;
            }
        })));

        NodePointer nodePointer = propertyPointer.createPath(context);
        Assert.assertEquals(propertyPointer.getClass(), nodePointer.getClass());
        Assert.assertEquals("propertyName", nodePointer.getName().getLocalPart());
        Assert.assertEquals(propertyPointer.getBean(), nodePointer.getNode());
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "propertyName";
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

            @Override
            public Object getBaseValue() {
                return new Object[]{1, 2, 3};
            }
        };

        JXPathContext context = new JXPathContext();
        context.setPropertyValueMap(Arrays.asList(new AbstractMap.SimpleEntry<>("bean", new Object(){
            String name = "beanName";

            public String getName() {
                return name;
            }

            public int getIndex() {
                return 1;
            }
        })));

        NodePointer nodePointer = propertyPointer.createPath(context, 2);
        Assert.assertEquals(propertyPointer.getClass(), nodePointer.getClass());
        Assert.assertEquals("propertyName", nodePointer.getName().getLocalPart());
        Assert.assertEquals(propertyPointer.getBean(), nodePointer.getNode());
        Assert.assertEquals(2, nodePointer.getIndex());
    }

    @Test
    public void testCreateChild() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "propertyName";
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

            @Override
            public Object getBaseValue() {
                return new Object[]{1, 2, 3};
            }
        };

        PropertyPointer childPointer1 = propertyPointer.createChild(null, "childProperty1", 1);
        PropertyPointer childPointer2 = propertyPointer.createChild(null, "childProperty2", 2);

        Assert.assertEquals(PropertyPointer.class, childPointer1.getClass());
        Assert.assertEquals("childProperty1", childPointer1.getPropertyName());
        Assert.assertEquals(propertyPointer, childPointer1.getParentPointer());
        Assert.assertEquals(1, childPointer1.getIndex());
        Assert.assertNull(childPointer1.getBean());

        Assert.assertEquals(PropertyPointer.class, childPointer2.getClass());
        Assert.assertEquals("childProperty2", childPointer2.getPropertyName());
        Assert.assertEquals(propertyPointer, childPointer2.getParentPointer());
        Assert.assertEquals(2, childPointer2.getIndex());
        Assert.assertNull(childPointer2.getBean());
    }

    @Test
    public void testCreateChildWithValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "propertyName";
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

            @Override
            public Object getBaseValue() {
                return new Object[]{1, 2, 3};
            }
        };

        PropertyPointer childPointer1 = propertyPointer.createChild(null, "childProperty1", 1, new Object(){
            int value = 42;

            public int getValue() {
                return value;
            }
        });

        Assert.assertEquals(PropertyPointer.class, childPointer1.getClass());
        Assert.assertEquals("childProperty1", childPointer1.getPropertyName());
        Assert.assertEquals(propertyPointer, childPointer1.getParentPointer());
        Assert.assertEquals(1, childPointer1.getIndex());
        Assert.assertEquals(new Object(){
            int value = 42;

            public int getValue() {
                return value;
            }
        }, childPointer1.getNode());
    }
}
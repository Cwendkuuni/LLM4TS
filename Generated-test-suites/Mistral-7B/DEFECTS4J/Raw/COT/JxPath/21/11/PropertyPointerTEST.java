package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@RunWith(PowerMockRunner.class)
@PowerMockRunner(classLoader = AnnotatedClassLoader.class)
public class PropertyPointerTest {

    @Test
    public void testConstructor() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Assert.assertNotNull(propertyPointer);
    }

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        propertyPointer.setPropertyIndex(1);
        int index = propertyPointer.getPropertyIndex();
        Assert.assertEquals(1, index);
    }

    @Test
    public void testSetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setPropertyIndex(2);
        Assert.assertEquals(2, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object() {
        };
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        propertyPointer.setBean(bean);
        Object result = propertyPointer.getBean();
        Assert.assertEquals(bean, result);
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        QName result = propertyPointer.getName();
        String expected = "testProperty";
        Assert.assertEquals(expected, result.getLocalPart());
    }

    @Test
    public void testGetPropertyCount() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public int getPropertyCount() {
                return 2;
            }
        };
        int result = propertyPointer.getPropertyCount();
        Assert.assertEquals(2, result);
    }

    @Test
    public void testGetPropertyNames() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"name1", "name2"};
            }
        };
        String[] result = propertyPointer.getPropertyNames();
        Assert.assertArrayEquals(new String[]{"name1", "name2"}, result);
    }

    @Test
    public void testIsActualProperty() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };
        boolean result = propertyPointer.isActualProperty();
        Assert.assertTrue(result);
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public Object getBaseValue() {
                return new Object() {
                    int value = 10;
                };
            }
        };
        Object result = propertyPointer.getImmediateNode();
        Assert.assertEquals(10, result);
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public Object getBaseValue() {
                return new Object() {
                    Object[] values = new Object[]{1, 2, 3};
                };
            }
        };
        boolean result = propertyPointer.isCollection();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public Object getBaseValue() {
                return new Object() {
                    int value = 10;
                };
            }
        };
        boolean result = propertyPointer.isLeaf();
        Assert.assertFalse(result);

        propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public Object getBaseValue() {
                return 10;
            }
        };
        result = propertyPointer.isLeaf();
        Assert.assertTrue(result);
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public Object getBaseValue() {
                return new Object() {
                    int[] values = new int[]{1, 2, 3, 4};
                };
            }
        };
        int result = propertyPointer.getLength();
        Assert.assertEquals(4, result);
    }

    @Test
    public void testGetImmediateValuePointer() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public Object getBaseValue() {
                return new Object() {
                    int value = 10;
                };
            }
        };
        NodePointer result = propertyPointer.getImmediateValuePointer();
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreatePath() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        JXPathContext context = PowerMockito.mock(JXPathContext.class);
        Object bean = new Object() {
        };
        PowerMockito.when(context.getBean(propertyPointer)).thenReturn(bean);
        NodePointer result = propertyPointer.createPath(context);
        Assert.assertEquals(propertyPointer, result);
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePathFailure() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        JXPathContext context = PowerMockito.mock(JXPathContext.class);
        AbstractFactory factory = PowerMockito.mock(AbstractFactory.class);
        PowerMockito.when(context.getFactory()).thenReturn(factory);
        PowerMockito.when(factory.createObject(context, propertyPointer, null, "testProperty", 0)).thenThrow(new JXPathAbstractFactoryException(""));
        propertyPointer.createPath(context);
    }

    @Test
    public void testCreateChild() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        JXPathContext context = PowerMockito.mock(JXPathContext.class);
        Object value = new Object() {
        };
        NodePointer result = propertyPointer.createChild(context, null, 0, value);
        Assert.assertEquals(propertyPointer, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildFailure() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        JXPathContext context = PowerMockito.mock(JXPathContext.class);
        NodePointer result = propertyPointer.createChild(context, null, -1, null);
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty1";
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty2";
            }
        };
        PropertyPointer propertyPointer3 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty1";
            }
        };
        PropertyPointer propertyPointer4 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty1";
            }

            @Override
            public int getPropertyIndex() {
                return 1;
            }
        };

        int result1 = propertyPointer1.hashCode();
        int result2 = propertyPointer2.hashCode();
        int result3 = propertyPointer3.hashCode();
        int result4 = propertyPointer4.hashCode();

        Assert.assertNotEquals(result1, result2);
        Assert.assertEquals(result1, result3);
        Assert.assertNotEquals(result1, result4);
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty1";
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty1";
            }
        };
        PropertyPointer propertyPointer3 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty2";
            }
        };

        boolean result1 = propertyPointer1.equals(propertyPointer1);
        boolean result2 = propertyPointer1.equals(propertyPointer2);
        boolean result3 = propertyPointer1.equals(propertyPointer3);

        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
        Assert.assertFalse(result3);
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty1";
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty2";
            }
        };
        NodePointer nodePointer1 = Mockito.mock(NodePointer.class);
        NodePointer nodePointer2 = Mockito.mock(NodePointer.class);
        NodePointer nodePointer3 = Mockito.mock(NodePointer.class);

        int result1 = propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer2);
        int result2 = propertyPointer1.compareChildNodePointers(nodePointer2, nodePointer3);

        Assert.assertEquals(-1, result1);
        Assert.assertEquals(0, result2);
    }
}
package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointerImpl;
import org.junit.Test;

import java.util.Arrays;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointer() {
        Object bean = new Object() {
            String property1 = "value1";
            String property2 = "value2";
        };

        PropertyPointer propertyPointer1 = new PropertyPointerImpl(null) {
            @Override
            public String getPropertyName() {
                return "property1";
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointerImpl(null) {
            @Override
            public String getPropertyName() {
                return "property2";
            }
        };

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        PropertyPointer propertyPointer1Parent = new PropertyPointerImpl(parentPointer) {
            @Override
            public String getPropertyName() {
                return "parent";
            }
        };

        PropertyPointer propertyPointer1Instance = new PropertyPointerImpl(propertyPointer1Parent) {
            @Override
            public String getPropertyName() {
                return "property1";
            }

            @Override
            public Object getBean() {
                return bean;
            }
        };

        PropertyPointer propertyPointer2Instance = new PropertyPointerImpl(propertyPointer2Parent) {
            @Override
            public String getPropertyName() {
                return "property2";
            }

            @Override
            public Object getBean() {
                return bean;
            }
        };

        assertEquals("property1", propertyPointer1Instance.getName().getLocalPart());
        assertEquals("property2", propertyPointer2Instance.getName().getLocalPart());
        assertEquals(bean, propertyPointer1Instance.getBean());
        assertEquals(bean, propertyPointer2Instance.getBean());

        assertEquals(propertyPointer1Instance, propertyPointer1Instance);
        assertEquals(propertyPointer1Instance, propertyPointer1Instance);
        assertNotEquals(propertyPointer1Instance, propertyPointer2Instance);
        assertNotEquals(propertyPointer2Instance, propertyPointer1Instance);
        assertNotEquals(propertyPointer1Instance, new Object());

        PropertyPointer propertyPointer3 = propertyPointer1Instance.clone();
        assertEquals(propertyPointer1Instance, propertyPointer3);

        propertyPointer1Instance.setPropertyIndex(0);
        propertyPointer2Instance.setPropertyIndex(1);
        assertEquals(-1, propertyPointer1Instance.compareChildNodePointers(propertyPointer2Instance, null));
        assertEquals(0, propertyPointer1Instance.compareChildNodePointers(propertyPointer1Instance, null));
        assertEquals(1, propertyPointer2Instance.compareChildNodePointers(propertyPointer1Instance, null));

        PropertyPointer propertyPointer4 = new PropertyPointerImpl(propertyPointer1Instance) {
            @Override
            public String getPropertyName() {
                return "grandParent";
            }
        };

        assertEquals(Arrays.asList(propertyPointer1Instance, propertyPointer4), propertyPointer4.getAncestorOrSelfAsList());
    }

    @Test
    public void testGetPropertyCount() {
        Object bean = new Object() {
            String property1 = "value1";
            String property2 = "value2";
            String[] properties = new String[]{"property1", "property2", "property3"};
        };

        PropertyPointer propertyPointer1 = new PropertyPointerImpl(null) {
            @Override
            public String getPropertyName() {
                return "property1";
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }
        };

        PropertyPointer propertyPointer2 = new PropertyPointerImpl(null) {
            @Override
            public String getPropertyName() {
                return "property2";
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }
        };

        PropertyPointer propertyPointer3 = new PropertyPointerImpl(null) {
            @Override
            public String getPropertyName() {
                return "properties";
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }
        };

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        PropertyPointer propertyPointer1Parent = new PropertyPointerImpl(parentPointer) {
            @Override
            public String getPropertyName() {
                return "parent";
            }
        };

        PropertyPointer propertyPointer1Instance = new PropertyPointerImpl(propertyPointer1Parent) {
            @Override
            public String getPropertyName() {
                return "property1";
            }

            @Override
            public Object getBean() {
                return bean;
            }
        };

        PropertyPointer propertyPointer2Instance = new PropertyPointerImpl(propertyPointer2Parent) {
            @Override
            public String getPropertyName() {
                return "property2";
            }

            @Override
            public Object getBean() {
                return bean;
            }
        };

        PropertyPointer propertyPointer3Instance = new PropertyPointerImpl(propertyPointer3Parent) {
            @Override
            public String getPropertyName() {
                return "properties";
            }

            @Override
            public Object getBean() {
                return bean;
            }
        };

        assertEquals(1, propertyPointer1Instance.getPropertyCount());
        assertEquals(1, propertyPointer2Instance.getPropertyCount());
        assertEquals(3, propertyPointer3Instance.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        Object bean = new Object() {
            String property1 = "value1";
            String property2 = "value2";
        };

        PropertyPointer propertyPointer1 = new PropertyPointerImpl(null) {
            @Override
            public String getPropertyName() {
                return "property1";
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property1"};
            }
        };

        PropertyPointer propertyPointer2 = new PropertyPointerImpl(null) {
            @Override
            public String getPropertyName() {
                return "property2";
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property2"};
            }
        };

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        PropertyPointer propertyPointer1Parent = new PropertyPointerImpl(parentPointer) {
            @Override
            public String getPropertyName() {
                return "parent";
            }
        };

        PropertyPointer propertyPointer1Instance = new PropertyPointerImpl(propertyPointer1Parent) {
            @Override
            public String getPropertyName() {
                return "property1";
            }

            @Override
            public Object getBean() {
                return bean;
            }
        };

        PropertyPointer propertyPointer2Instance = new PropertyPointerImpl(propertyPointer2Parent) {
            @Override
            public String getPropertyName() {
                return "property2";
            }

            @Override
            public Object getBean() {
                return bean;
            }
        };

        assertEquals(new String[]{"property1"}, propertyPointer1Instance.getPropertyNames());
        assertEquals(new String[]{"property2"}, propertyPointer2Instance.getPropertyNames());
    }

    @Test
    public void testIsActual() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        assertFalse(propertyPointer.isActual());

        PropertyPointer propertyPointerActual = new PropertyPointerImpl(null) {
            @Override
            public boolean isActualProperty() {
                return true;
            }
        };
        assertTrue(propertyPointerActual.isActual());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        assertFalse(propertyPointer.isCollection());

        PropertyPointer propertyPointerCollection = new PropertyPointerImpl(null) {
            @Override
            public boolean isCollection() {
                return true;
            }
        };
        assertTrue(propertyPointerCollection.isCollection());
    }

    @Test
    public void testGetImmediateNode() {
        Object bean = new Object() {
            String property1 = "value1";
        };

        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public Object getBean() {
                return bean;
            }
        };

        assertEquals(propertyPointer.getBean(), propertyPointer.getImmediateNode());

        PropertyPointer propertyPointerWithCollection = new PropertyPointerImpl(null) {
            @Override
            public Object getBean() {
                return new Object()[0];
            }
        };
        assertNotNull(propertyPointerWithCollection.getImmediateNode());
    }

    @Test
    public void testGetLength() {
        Object bean = new Object[]{1, 2, 3};

        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public Object getBean() {
                return bean;
            }
        };

        assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testCreatePath() {
        Object bean = new Object() {
            String property1 = "value1";
        };

        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public Object getBean() {
                return bean;
            }
        };

        JXPathContext context = new JXPathContext();
        PropertyPointer propertyPointerPath = propertyPointer.createPath(context);
        assertEquals(propertyPointer, propertyPointerPath);

        Object value = new Object();
        PropertyPointer propertyPointerValue = propertyPointer.createPath(context, value);
        assertEquals(propertyPointer, propertyPointerValue.getParentPointer());
        assertEquals(value, propertyPointerValue.getNode());
    }

    @Test
    public void testCreateChild() {
        Object bean = new Object() {
            String property1 = "value1";
            String[] properties = new String[]{"property1", "property2", "property3"};
        };

        PropertyPointer propertyPointerParent = new PropertyPointerImpl(null) {
            @Override
            public Object getBean() {
                return bean;
            }
        };

        PropertyPointer propertyPointerChild1 = propertyPointerParent.createChild(null, null, 0, "child1");
        assertEquals("child1", propertyPointerChild1.getName().getLocalPart());
        assertEquals(bean, propertyPointerChild1.getBean());

        PropertyPointer propertyPointerChild2 = propertyPointerParent.createChild(null, null, 1, "child2");
        assertEquals("child2", propertyPointerChild2.getName().getLocalPart());
        assertEquals(bean, propertyPointerChild2.getBean());

        PropertyPointer propertyPointerChild3 = propertyPointerParent.createChild(null, null, 2, "child3");
        assertEquals("child3", propertyPointerChild3.getName().getLocalPart());
        assertEquals(bean, propertyPointerChild3.getBean());

        PropertyPointer propertyPointerChildCollection = propertyPointerParent.createChild(null, null, 0, new Object[0]);
        assertEquals(0, propertyPointerChildCollection.getLength());
    }
}
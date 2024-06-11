package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    private Object testBean;
    private PropertyPointer propertyPointer;

    @Before
    public void setup() {
        testBean = new TestBean();
        propertyPointer = new PropertyPointer(new NodePointer(new NodePointer(null))) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        propertyPointer.setBean(testBean);
    }

    @Test
    public void testGetPropertyIndex() {
        assertNotNull(propertyPointer);
        assertEquals(-1, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(0);
        assertEquals(0, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        assertNotNull(propertyPointer.getBean());
        assertEquals(testBean.getClass(), propertyPointer.getBean().getClass());
    }

    @Test
    public void testGetName() {
        assertEquals("testProperty", propertyPointer.getName().getLocalPart());
    }

    @Test
    public void testGetPropertyCount() {
        TestBean testBean = new TestBean();
        testBean.setTestProperty(new TestClass[3]);
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(new NodePointer(null))) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        propertyPointer.setBean(testBean);
        assertEquals(3, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        TestBean testBean = new TestBean();
        testBean.setTestProperty(new TestClass[3]);
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(new NodePointer(null))) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        propertyPointer.setBean(testBean);
        String[] propertyNames = propertyPointer.getPropertyNames();
        assertEquals(1, propertyNames.length);
        assertEquals("testProperty", propertyNames[0]);
    }

    @Test
    public void testIsActualProperty() {
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        TestBean testBean = new TestBean();
        testBean.setTestProperty("testValue");
        propertyPointer.setBean(testBean);
        assertEquals("testValue", propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        TestBean testBean = new TestBean();
        testBean.setTestProperty(new TestClass[3]);
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        TestBean testBean = new TestBean();
        testBean.setTestProperty("testValue");
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        TestBean testBean = new TestBean();
        testBean.setTestProperty(new TestClass[3]);
        assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        TestBean testBean = new TestBean();
        testBean.setTestProperty("testValue");
        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
        assertEquals(NodePointer.class, immediateValuePointer.getClass());
        assertEquals("testValue", immediateValuePointer.getNode().toString());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCreatePathWithInvalidIndex() {
        TestBean testBean = new TestBean();
        testBean.setTestProperty(new TestClass[3]);
        propertyPointer.setBean(testBean);
        propertyPointer.createPath(new JXPathContext());
        propertyPointer.createPath(new JXPathContext(), 3);
    }

    @Test
    public void testCreatePath() {
        TestBean testBean = new TestBean();
        testBean.setTestProperty("testValue");
        NodePointer nodePointer = propertyPointer.createPath(new JXPathContext());
        assertEquals("testValue", nodePointer.getNode().toString());
    }

    @Test
    public void testCreatePathWithValue() {
        TestBean testBean = new TestBean();
        TestClass testClass = new TestClass();
        testBean.setTestProperty(testClass);
        propertyPointer.setBean(testBean);
        NodePointer nodePointer = propertyPointer.createPath(new JXPathContext(), testClass);
        assertEquals(testClass.getClass(), nodePointer.getNode().getClass());
    }

    @Test
    public void testCreateChild() {
        TestBean testBean = new TestBean();
        testBean.setTestProperty(new TestClass[3]);
        PropertyPointer childPropertyPointer = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "testChildProperty";
            }
        };
        NodePointer nodePointer = childPropertyPointer.createPath(new JXPathContext(), 1);
        assertEquals(TestClass.class, nodePointer.getNode().getClass());
    }

    @Test
    public void testCreateChildWithName() {
        TestBean testBean = new TestBean();
        testBean.setTestProperty(new TestClass[3]);
        PropertyPointer childPropertyPointer = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "testChildProperty";
            }
        };
        NodePointer nodePointer = childPropertyPointer.createChild(new JXPathContext(), null, 1);
        assertEquals(TestClass.class, nodePointer.getNode().getClass());
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer1 = new PropertyPointer(new NodePointer(new NodePointer(null))) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(new NodePointer(new NodePointer(null))) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        PropertyPointer propertyPointer3 = new PropertyPointer(new NodePointer(new NodePointer(null))) {
            @Override
            public String getPropertyName() {
                return "testChildProperty";
            }
        };
        assertEquals(propertyPointer.hashCode(), propertyPointer1.hashCode());
        assertNotEquals(propertyPointer.hashCode(), propertyPointer2.hashCode());
        assertNotEquals(propertyPointer.hashCode(), propertyPointer3.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new PropertyPointer(new NodePointer(new NodePointer(null))) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(new NodePointer(new NodePointer(null))) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        PropertyPointer propertyPointer3 = new PropertyPointer(new NodePointer(new NodePointer(null))) {
            @Override
            public String getPropertyName() {
                return "testChildProperty";
            }
        };
        PropertyPointer propertyPointer4 = new PropertyPointer(new NodePointer(new NodePointer(null))) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        PropertyPointer propertyPointer5 = new PropertyPointer(new NodePointer(propertyPointer)) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        assertTrue(propertyPointer.equals(propertyPointer1));
        assertTrue(propertyPointer.equals(propertyPointer4));
        assertTrue(propertyPointer.equals(propertyPointer5));
        assertFalse(propertyPointer.equals(propertyPointer2));
        assertFalse(propertyPointer.equals(propertyPointer3));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer nodePointer1 = new NodePointer(new NodePointer(null), "testProperty");
        NodePointer nodePointer2 = new NodePointer(new NodePointer(null), "testChildProperty");
        NodePointer nodePointer3 = new NodePointer(nodePointer1, "testChildProperty");
        assertEquals(0, propertyPointer.compareChildNodePointers(nodePointer1, nodePointer1));
        assertEquals(1, propertyPointer.compareChildNodePointers(nodePointer1, nodePointer2));
        assertEquals(-1, propertyPointer.compareChildNodePointers(nodePointer2, nodePointer1));
        assertEquals(0, propertyPointer.compareChildNodePointers(nodePointer1, nodePointer3));
        assertEquals(1, propertyPointer.compareChildNodePointers(nodePointer3, nodePointer1));
    }
}
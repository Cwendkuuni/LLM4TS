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
                return "testProperty";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // not implemented
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"testProperty"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        Assert.assertEquals("testProperty", propertyPointer.getName().getLocalPart());
        Assert.assertEquals(1, propertyPointer.getPropertyCount());
        Assert.assertArrayEquals(new String[]{"testProperty"}, propertyPointer.getPropertyNames());
        Assert.assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return new Object() {
                    Object value = "testValue";

                    public Object getValue() {
                        return value;
                    }
                };
            }
        };

        Assert.assertEquals("testValue", propertyPointer.getImmediateNode().getNode().toString());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return Arrays.asList("test1", "test2");
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return "testString";
            }
        };

        Assert.assertTrue(propertyPointer1.isCollection());
        Assert.assertFalse(propertyPointer2.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return 1;
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return new String[]{"test1", "test2"};
            }
        };

        Assert.assertTrue(propertyPointer1.isLeaf());
        Assert.assertFalse(propertyPointer2.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return Arrays.asList("test1", "test2", "test3");
            }
        };

        Assert.assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return "testValue";
            }
        };

        NodePointer nodePointer = propertyPointer.getImmediateValuePointer();
        Assert.assertEquals("testValue", nodePointer.getNode().toString());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return new Object() {
                    Object value = "testValue";

                    public Object getValue() {
                        return value;
                    }
                };
            }
        };

        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = propertyPointer.createPath(context);
        Assert.assertEquals(propertyPointer, nodePointer);
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return new Object() {
                    Object value = "testValue";

                    public Object getValue() {
                        return value;
                    }
                };
            }
        };

        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = propertyPointer.createPath(context, "newValue");
        Assert.assertEquals("newValue", nodePointer.getNode().toString());
    }

    @Test
    public void testCreateChild() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return new Object() {
                    Object value = Arrays.asList("test1", "test2");

                    public Object getValue() {
                        return value;
                    }
                };
            }
        };

        JXPathContext context = new JXPathContext();
        PropertyPointer childPropertyPointer = (PropertyPointer) propertyPointer.createChild(
            context, new QName("childProperty"), 1, Arrays.asList("testChild1", "testChild2")).createPath(context);

        Assert.assertEquals("testChild1", childPropertyPointer.getNode().toString());
    }

    @Test
    public void testCreateChildWithNullName() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBaseValue() {
                return null;
            }
        };

        JXPathContext context = new JXPathContext();
        PropertyPointer childPropertyPointer = (PropertyPointer) propertyPointer.createChild(
            context, null, 1).createPath(context);

        Assert.assertNull(childPropertyPointer.getNode());
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        PropertyPointer propertyPointer3 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty2";
            }
        };

        NodePointer nodePointer1 = new NodePointer(null, 0) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        NodePointer nodePointer2 = new NodePointer(null, 0) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        NodePointer nodePointer3 = new NodePointer(null, 0) {
            @Override
            public String getPropertyName() {
                return "testProperty2";
            }
        };

        Assert.assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());
        Assert.assertEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
        Assert.assertEquals(propertyPointer1.hashCode(), nodePointer1.hashCode());
        Assert.assertNotEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
        Assert.assertNotEquals(propertyPointer1.hashCode(), nodePointer2.hashCode());
        Assert.assertNotEquals(propertyPointer1.hashCode(), nodePointer3.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        PropertyPointer propertyPointer3 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty2";
            }
        };
        NodePointer nodePointer1 = new NodePointer(null, 0) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        NodePointer nodePointer2 = new NodePointer(null, 0) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        NodePointer nodePointer3 = new NodePointer(null, 0) {
            @Override
            public String getPropertyName() {
                return "testProperty2";
            }
        };

        Assert.assertTrue(propertyPointer1.equals(propertyPointer1));
        Assert.assertTrue(propertyPointer1.equals(propertyPointer2));
        Assert.assertTrue(propertyPointer1.equals(nodePointer1));
        Assert.assertFalse(propertyPointer1.equals(propertyPointer3));
        Assert.assertFalse(propertyPointer1.equals(nodePointer2));
        Assert.assertFalse(propertyPointer1.equals(nodePointer3));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer propertyPointer1 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        PropertyPointer propertyPointer3 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty2";
            }
        };
        NodePointer nodePointer1 = new NodePointer(null, 0) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        NodePointer nodePointer2 = new NodePointer(null, 0) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        NodePointer nodePointer3 = new NodePointer(null, 0) {
            @Override
            public String getPropertyName() {
                return "testProperty2";
            }
        };
        NodePointer nodePointer4 = new NodePointer(null, 1) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };

        Assert.assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer1));
        Assert.assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer2));
        Assert.assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer1));
        Assert.assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, nodePointer1));
        Assert.assertEquals(0, propertyPointer1.compareChildNodePointers(nodePointer1, propertyPointer1));
        Assert.assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer3));
        Assert.assertEquals(-1, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer3));
        Assert.assertEquals(-1, propertyPointer1.compareChildNodePointers(propertyPointer3, propertyPointer1));
        Assert.assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, nodePointer3));
        Assert.assertEquals(-1, propertyPointer1.compareChildNodePointers(nodePointer3, propertyPointer1));
        Assert.assertEquals(1, propertyPointer1.compareChildNodePointers(propertyPointer1, nodePointer4));
        Assert.assertEquals(-1, propertyPointer1.compareChildNodePointers(nodePointer4, propertyPointer1));
    }
}
package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.jxpath.ri.model.beans.AbstractFactory;
import org.apache.commons.jxpath.ri.model.beans.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;
    private AbstractFactory factory;

    @Before
    public void setUp() {
        factory = new AbstractFactory() {
            @Override
            public Object createObject(JXPathContext context, NodePointer node, Object bean, String propertyName, int index) {
                return propertyName;
            }
        };
        propertyPointer = new PropertyPointer(NodePointer.NOP) {
            @Override
            public AbstractFactory getAbstractFactory(JXPathContext context) {
                return factory;
            }
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
            @Override
            public void setPropertyName(String propertyName) {
                // empty
            }
            @Override
            public int getPropertyCount() {
                return 0;
            }
            @Override
            public String[] getPropertyNames() {
                return new String[0];
            }
            @Override
            public boolean isActualProperty() {
                return true;
            }
        };
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        propertyPointer.setBean(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetBeanWithNull() {
        propertyPointer.setBean(null);
        assertNull(propertyPointer.getBean());
    }

    @Test
    public void testGetImmediateNode() {
        Object value = "testValue";
        propertyPointer.setValue(value);
        assertEquals(value, propertyPointer.getImmediateNode());
    }

    @Test
    public void testGetImmediateNodeWithUninitialized() {
        assertNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        Object collection = new Object[] { "test1", "test2" };
        propertyPointer.setValue(collection);
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        String leaf = "testValue";
        propertyPointer.setValue(leaf);
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        Object collection = new Object[] { "test1", "test2" };
        propertyPointer.setValue(collection);
        assertEquals(2, propertyPointer.getLength());
    }

    @Test
    public void testCreatePath() {
        Object bean = new Object();
        propertyPointer.setBean(bean);
        JXPathContext context = new JXPathContext();
        NodePointer createdPath = propertyPointer.createPath(context);
        assertSame(propertyPointer, createdPath);
    }

    @Test
    public void testCreatePathWithValue() {
        Object bean = new Object();
        propertyPointer.setBean(bean);
        Object value = new Object();
        JXPathContext context = new JXPathContext();
        NodePointer createdPath = propertyPointer.createPath(context, value);
        assertSame(propertyPointer, createdPath);
        assertEquals(value, createdPath.getNode());
    }

    @Test
    public void testCreateChildWithNull() {
        JXPathContext context = new JXPathContext();
        NodePointer createdChild = propertyPointer.createChild(context, null, 0);
        assertNull(createdChild);
    }

    @Test
    public void testCreateChildWithNameAndIndex() {
        JXPathContext context = new JXPathContext();
        NodePointer createdChild = propertyPointer.createChild(context, new QName("testName"), 0, null);
        assertNull(createdChild);
    }

    @Test
    public void testCreateChildWithName() {
        JXPathContext context = new JXPathContext();
        NodePointer createdChild = propertyPointer.createChild(context, new QName("testName"));
        assertNull(createdChild);
    }

    @Test
    public void testCreateChildWithIndex() {
        JXPathContext context = new JXPathContext();
        NodePointer createdChild = propertyPointer.createChild(context, null, 0);
        assertNull(createdChild);
    }

    @Test
    public void testHashCode() {
        PropertyPointer other = new PropertyPointer(NodePointer.NOP) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };

        NodePointer parent1 = NodePointer.NOP;
        NodePointer parent2 = NodePointer.NOP;
        int index1 = 1;
        int index2 = 2;

        int hashCode1 = propertyPointer.hashCode();
        int hashCode2 = other.hashCode();
        int hashCodeSameParent = new PropertyPointer(propertyPointer) {}.hashCode();
        int hashCodeSameName = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        }.hashCode();
        int hashCodeDifferentName = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "differentProperty";
            }
        }.hashCode();
        int hashCodeDifferentParent = new PropertyPointer(NodePointer.NOP) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        }.hashCode();
        int hashCodeSameIndex = new PropertyPointer(propertyPointer) {
            @Override
            public int getPropertyIndex() {
                return index1;
            }
        }.hashCode();
        int hashCodeDifferentIndex = new PropertyPointer(propertyPointer) {
            @Override
            public int getPropertyIndex() {
                return index2;
            }
        }.hashCode();

        assertEquals(hashCode1, propertyPointer.hashCode());
        assertEquals(hashCode2, other.hashCode());
        assertEquals(hashCodeSameParent, new PropertyPointer(propertyPointer, parent1).hashCode());
        assertEquals(hashCodeSameName, new PropertyPointer(propertyPointer, parent1) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        }.hashCode());
        assertEquals(hashCodeDifferentName, new PropertyPointer(propertyPointer, parent1) {
            @Override
            public String getPropertyName() {
                return "differentProperty";
            }
        }.hashCode());
        assertEquals(hashCodeDifferentParent, new PropertyPointer(NodePointer.NOP, parent1) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        }.hashCode());
        assertEquals(hashCodeSameIndex, new PropertyPointer(propertyPointer) {
            @Override
            public int getPropertyIndex() {
                return index1;
            }
        }.hashCode());
        assertEquals(hashCodeDifferentIndex, new PropertyPointer(propertyPointer) {
            @Override
            public int getPropertyIndex() {
                return index2;
            }
        }.hashCode());
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(propertyPointer.equals(propertyPointer));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(propertyPointer.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentPropertyPointer() {
        PropertyPointer other = new PropertyPointer(NodePointer.NOP) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        assertFalse(propertyPointer.equals(other));
    }

    @Test
    public void testEqualsWithSameNameDifferentParent() {
        PropertyPointer other = new PropertyPointer(NodePointer.NOP) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
        assertFalse(propertyPointer.equals(other));
    }

    @Test
    public void testEqualsWithSameNameSameParent() {
        PropertyPointer other = new PropertyPointer(propertyPointer) {}.clone();
        assertTrue(propertyPointer.equals(other));
    }

    @Test
    public void testEqualsWithDifferentNameSameParent() {
        PropertyPointer other = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "differentProperty";
            }
        }.clone();
        assertFalse(propertyPointer.equals(other));
    }

    @Test
    public void testEqualsWithSameNameDifferentParentDifferentIndex() {
        PropertyPointer other = new PropertyPointer(NodePointer.NOP) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        }.clone();
        PropertyPointer other2 = new PropertyPointer(NodePointer.NOP) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        }.clone();
        other.setIndex(1);
        other2.setIndex(2);
        assertFalse(propertyPointer.equals(other));
        assertFalse(propertyPointer.equals(other2));
    }

    @Test
    public void testEqualsWithSameNameDifferentParentSameIndex() {
        PropertyPointer other = new PropertyPointer(NodePointer.NOP) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        }.clone();
        PropertyPointer other2 = new PropertyPointer(NodePointer.NOP) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        }.clone();
        other.setIndex(1);
        other2.setIndex(1);
        assertTrue(propertyPointer.equals(other2));
    }

    @Test
    public void testCompareChildNodePointersWithSamePointer() {
        int result = propertyPointer.compareChildNodePointers(propertyPointer, propertyPointer);
        assertEquals(0, result);
    }

    @Test
    public void testCompareChildNodePointersWithDifferentPointerSameValue() {
        NodePointer other = NodePointer.NOP;
        int result = propertyPointer.compareChildNodePointers(propertyPointer, other);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareChildNodePointersWithDifferentPointerDifferentValue() {
        NodePointer other = new PropertyPointer(NodePointer.NOP) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        }.clone();
        int result = propertyPointer.compareChildNodePointers(propertyPointer, other);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareChildNodePointersWithDifferentPointerSameValueDifferentIndex() {
        NodePointer other = NodePointer.NOP;
        int result = propertyPointer.compareChildNodePointers(propertyPointer, other);
        assertEquals(-1, result);

        PropertyPointer otherProperty = new PropertyPointer(NodePointer.NOP) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        }.clone();
        int result2 = propertyPointer.compareChildNodePointers(propertyPointer, otherProperty);
        assertEquals(-1, result2);

        otherProperty.setIndex(1);
        int result3 = propertyPointer.compareChildNodePointers(propertyPointer, otherProperty);
        assertEquals(0, result3);
    }

    @Test
    public void testCompareChildNodePointersWithDifferentPointerSameValueSameIndex() {
        NodePointer other = NodePointer.NOP;
        int result = propertyPointer.compareChildNodePointers(propertyPointer, other);
        assertEquals(-1, result);

        PropertyPointer otherProperty = new PropertyPointer(NodePointer.NOP) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        }.clone();
        int result2 = propertyPointer.compareChildNodePointers(propertyPointer, otherProperty);
        assertEquals(-1, result2);

        otherProperty.setIndex(0);
        int result3 = propertyPointer.compareChildNodePointers(propertyPointer, otherProperty);
        assertEquals(0, result3);
    }

    @Test
    public void testCompareChildNodePointersWithSameParentDifferentProperty() {
        PropertyPointer other = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "differentProperty";
            }
        }.clone();
        int result = propertyPointer.compareChildNodePointers(propertyPointer, other);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareChildNodePointersWithSameParentSamePropertyDifferentIndex() {
        PropertyPointer other = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        }.clone();
        other.setIndex(1);
        int result = propertyPointer.compareChildNodePointers(propertyPointer, other);
        assertEquals(1, result);
    }

    @Test
    public void testCompareChildNodePointersWithSameParentSamePropertySameIndex() {
        PropertyPointer other = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        }.clone();
        int result = propertyPointer.compareChildNodePointers(propertyPointer, other);
        assertEquals(0, result);
    }

    @Test
    public void testCompareChildNodePointersWithSameParentSamePropertyNull() {
        NodePointer other = null;
        int result = propertyPointer.compareChildNodePointers(propertyPointer, other);
        assertEquals(-1, result);
    }
}
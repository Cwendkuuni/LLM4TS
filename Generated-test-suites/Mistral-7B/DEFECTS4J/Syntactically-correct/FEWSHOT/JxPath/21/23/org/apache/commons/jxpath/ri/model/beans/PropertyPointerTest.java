package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointerImpl;
import org.apache.commons.jxpath.util.ValueUtils;

public class PropertyPointerTest {

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        assertFalse(propertyPointer.isActualProperty());

        PropertyPointerImpl propertyPointerWithActualProperty = new PropertyPointerImpl(null) {
            @Override
            public String getPropertyName() {
                return "propertyName";
            }
        };
        assertTrue(propertyPointerWithActualProperty.isActualProperty());
    }

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        int actualPropertyIndex = propertyPointer.getPropertyIndex();
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, actualPropertyIndex);

        PropertyPointerImpl propertyPointerWithActualPropertyIndex = new PropertyPointerImpl(null) {
            @Override
            public int getPropertyIndex() {
                return 0;
            }
        };
        int expectedPropertyIndex = 0;
        assertEquals(expectedPropertyIndex, propertyPointerWithActualPropertyIndex.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public Object getBean() {
                return bean;
            }
        };
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        String propertyName = "propertyName";
        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public String getPropertyName() {
                return propertyName;
            }
        };
        QName expectedName = new QName(null, propertyName);
        assertEquals(expectedName, propertyPointer.getName());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        int actualPropertyCount = propertyPointer.getPropertyCount();
        assertEquals(-1, actualPropertyCount);

        PropertyPointerImpl propertyPointerWithPropertyCount = new PropertyPointerImpl(null) {
            @Override
            public int getPropertyCount() {
                return 5;
            }
        };
        int expectedPropertyCount = 5;
        assertEquals(expectedPropertyCount, propertyPointerWithPropertyCount.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        String[] propertyNames = new String[]{"propertyName1", "propertyName2"};
        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public String[] getPropertyNames() {
                return propertyNames;
            }
        };
        String[] expectedPropertyNames = propertyNames;
        assertArrayEquals(expectedPropertyNames, propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActual() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public boolean isActualProperty() {
                return false;
            }
        };
        assertFalse(propertyPointer.isActual());

        PropertyPointerImpl propertyPointerWithActualProperty = new PropertyPointerImpl(null) {
            @Override
            public boolean isActualProperty() {
                return true;
            }

            @Override
            public String getPropertyName() {
                return "propertyName";
            }
        };
        assertTrue(propertyPointerWithActualProperty.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        Object baseValue = new Object();
        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public Object getBaseValue() {
                return baseValue;
            }
        };

        Object immediateNode = new Object();
        propertyPointer.value = immediateNode;

        Object expectedImmediateNode = immediateNode;
        Object actualImmediateNode = propertyPointer.getImmediateNode();
        assertEquals(expectedImmediateNode, actualImmediateNode);
    }

    @Test
    public void testIsCollection() {
        Object baseValue = new Object();
        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public Object getBaseValue() {
                return baseValue;
            }
        };

        Object collection = Arrays.asList("item1", "item2").toArray();
        propertyPointer.value = collection;

        boolean expectedCollection = true;
        boolean actualCollection = propertyPointer.isCollection();
        assertEquals(expectedCollection, actualCollection);
    }

    @Test
    public void testIsLeaf() {
        Object node = new String("leafNode");
        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public Object getNode() {
                return node;
            }
        };

        boolean expectedLeaf = true;
        boolean actualLeaf = propertyPointer.isLeaf();
        assertEquals(expectedLeaf, actualLeaf);
    }

    @Test
    public void testGetLength() {
        Object baseValue = Arrays.asList("item1", "item2").toArray();
        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public Object getBaseValue() {
                return baseValue;
            }
        };

        int expectedLength = 2;
        int actualLength = propertyPointer.getLength();
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetImmediateValuePointer() {
        Object baseValue = new Object();
        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public Object getBaseValue() {
                return baseValue;
            }
        };
        PropertyPointer childPropertyPointer = new PropertyPointerImpl(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "childPropertyName";
            }
        };

        Object immediateNode = new Object();
        propertyPointer.value = immediateNode;

        NodePointer expectedImmediateValuePointer = NodePointer.newChildNodePointer(
                propertyPointer,
                "childPropertyName",
                immediateNode);
        NodePointer actualImmediateValuePointer = propertyPointer.getImmediateValuePointer();
        assertSame(expectedImmediateValuePointer, actualImmediateValuePointer);
    }

    @Test
    public void testCreatePath() {
        Object baseValue = new Object();
        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public Object getBaseValue() {
                return baseValue;
            }
        };

        Object childValue = new Object();
        propertyPointer.setIndex(0);
        propertyPointer.setValue(childValue);

        JXPathContext context = null;
        PropertyPointer expectedPath = propertyPointer;
        PropertyPointer actualPath = propertyPointer.createPath(context);
        assertEquals(expectedPath, actualPath);

        Object bean = new Object();
        propertyPointer.setBean(bean);
        PropertyPointer beanPropertyPointer = new PropertyPointerImpl(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "beanPropertyName";
            }
        };
        expectedPath = beanPropertyPointer;
        actualPath = propertyPointer.createPath(context);
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testCreatePathWithValue() {
        Object baseValue = new Object();
        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public Object getBaseValue() {
                return baseValue;
            }
        };

        Object valueToSet = new Object();
        propertyPointer.setIndex(0);

        JXPathContext context = null;
        PropertyPointer expectedPath = propertyPointer;
        PropertyPointer actualPath = propertyPointer.createPath(context, valueToSet);
        assertEquals(expectedPath, actualPath);

        Object childValue = new Object();
        propertyPointer.setValue(childValue);
        PropertyPointer childPropertyPointer = new PropertyPointerImpl(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "childPropertyName";
            }
        };
        expectedPath = childPropertyPointer;
        actualPath = propertyPointer.createPath(context, valueToSet);
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testCreateChild() {
        Object baseValue = new Object();
        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public Object getBaseValue() {
                return baseValue;
            }
        };

        String childPropertyName = "childPropertyName";
        int index = 0;

        PropertyPointer expectedChild = new PropertyPointerImpl(propertyPointer) {
            @Override
            public String getPropertyName() {
                return childPropertyName;
            }
        };
        expectedChild.setIndex(index);

        NodePointer actualChild = propertyPointer.createChild(null, null, index, null);
        assertEquals(expectedChild, actualChild);

        String name = "childPropertyName";
        expectedChild = new PropertyPointerImpl(propertyPointer) {
            @Override
            public String getPropertyName() {
                return name;
            }
        };
        actualChild = propertyPointer.createChild(null, new QName(null, name), 0, null);
        assertEquals(expectedChild, actualChild);
    }

    @Test
    public void testCreateChildWithValue() {
        Object baseValue = new Object();
        PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
            @Override
            public Object getBaseValue() {
                return baseValue;
            }
        };

        String childPropertyName = "childPropertyName";
        int index = 0;
        Object childValue = new Object();

        PropertyPointer expectedChild = new PropertyPointerImpl(propertyPointer) {
            @Override
            public String getPropertyName() {
                return childPropertyName;
            }
        };
        expectedChild.setIndex(index);
        expectedChild.setValue(childValue);

        NodePointer actualChild = propertyPointer.createChild(null, null, index, childValue);
        assertEquals(expectedChild, actualChild);

        String name = "childPropertyName";
        expectedChild = new PropertyPointerImpl(propertyPointer) {
            @Override
            public String getPropertyName() {
                return name;
            }
        };
        expectedChild.setValue(childValue);
        actualChild = propertyPointer.createChild(null, new QName(null, name), 0, childValue);
        assertEquals(expectedChild, actualChild);
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer1 = new PropertyPointerImpl(null);
        PropertyPointer propertyPointer2 = new PropertyPointerImpl(null);
        int propertyIndex1 = 0;
        int propertyIndex2 = 1;

        int expectedHashCode1 = propertyPointer1.hashCode();
        int actualHashCode1 = propertyPointer1.hashCode();
        assertEquals(expectedHashCode1, actualHashCode1);

        propertyPointer1.setIndex(propertyIndex1);
        int expectedHashCode2 = propertyPointer1.hashCode();
        int actualHashCode2 = propertyPointer1.hashCode();
        assertEquals(expectedHashCode2, actualHashCode2);

        propertyPointer2.setIndex(propertyIndex2);

        int expectedHashCode3 = propertyPointer1.hashCode();
        int actualHashCode3 = propertyPointer2.hashCode();
        assertNotEquals(expectedHashCode3, actualHashCode3);
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new PropertyPointerImpl(null);
        PropertyPointer propertyPointer2 = new PropertyPointerImpl(null);
        int propertyIndex1 = 0;
        int propertyIndex2 = 1;

        PropertyPointer propertyPointer3 = propertyPointer1;
        boolean expectedEquals1 = true;
        boolean actualEquals1 = propertyPointer1.equals(propertyPointer3);
        assertEquals(expectedEquals1, actualEquals1);

        PropertyPointer propertyPointer4 = new PropertyPointerImpl(null);
        boolean expectedEquals2 = false;
        boolean actualEquals2 = propertyPointer1.equals(propertyPointer4);
        assertEquals(expectedEquals2, actualEquals2);

        PropertyPointer propertyPointer5 = new PropertyPointerImpl(null) {
            @Override
            public int getPropertyIndex() {
                return propertyIndex1;
            }
        };
        boolean expectedEquals3 = true;
        boolean actualEquals3 = propertyPointer1.equals(propertyPointer5);
        assertEquals(expectedEquals3, actualEquals3);

        PropertyPointer propertyPointer6 = new PropertyPointerImpl(null) {
            @Override
            public int getPropertyIndex() {
                return propertyIndex2;
            }
        };
        boolean expectedEquals4 = false;
        boolean actualEquals4 = propertyPointer1.equals(propertyPointer6);
        assertEquals(expectedEquals4, actualEquals4);

        PropertyPointer propertyPointer7 = new PropertyPointerImpl(null) {
            @Override
            public int getPropertyIndex() {
                return propertyIndex1;
            }

            @Override
            public String getPropertyName() {
                return "propertyName";
            }
        };
        boolean expectedEquals5 = false;
        boolean actualEquals5 = propertyPointer1.equals(propertyPointer7);
        assertEquals(expectedEquals5, actualEquals5);

        PropertyPointer propertyPointer8 = new PropertyPointerImpl(propertyPointer1) {
            @Override
            public String getPropertyName() {
                return "propertyName";
            }
        };
        boolean expectedEquals6 = true;
        boolean actualEquals6 = propertyPointer1.equals(propertyPointer8);
        assertEquals(expectedEquals6, actualEquals6);

        PropertyPointer propertyPointer9 = new PropertyPointerImpl(propertyPointer1) {
            @Override
            public String getPropertyName() {
                return "propertyName";
            }

            @Override
            public Object getBean() {
                return new Object();
            }
        };
        boolean expectedEquals7 = false;
        boolean actualEquals7 = propertyPointer1.equals(propertyPointer9);
        assertEquals(expectedEquals7, actualEquals7);
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer propertyPointer1 = new PropertyPointerImpl(null);
        PropertyPointer propertyPointer2 = new PropertyPointerImpl(null);
        NodePointer nodePointer1 = new PropertyPointerImpl(null);
        NodePointer nodePointer2 = new PropertyPointerImpl(null);

        int expectedComparisonResult1 = 0;
        int actualComparisonResult1 = propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer1);
        assertEquals(expectedComparisonResult1, actualComparisonResult1);

        int expectedComparisonResult2 = -1;
        int actualComparisonResult2 = propertyPointer1.compareChildNodePointers(nodePointer2, nodePointer1);
        assertEquals(expectedComparisonResult2, actualComparisonResult2);

        int expectedComparisonResult3 = 1;
        int actualComparisonResult3 = propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer2);
        assertEquals(expectedComparisonResult3, actualComparisonResult3);
    }
}
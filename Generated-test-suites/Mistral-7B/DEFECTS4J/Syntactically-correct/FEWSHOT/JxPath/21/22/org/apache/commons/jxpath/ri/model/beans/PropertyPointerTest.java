package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.AbstractFactory;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointerTest;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new TestPropertyPointer();
        assertEquals(-1, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(0);
        assertEquals(0, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        PropertyPointer propertyPointer = new TestPropertyPointer();
        assertNull(propertyPointer.getBean());
        propertyPointer.setBean(new Object());
        assertSame(propertyPointer.getBean(), propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new TestPropertyPointer();
        assertNull(propertyPointer.getName());
        propertyPointer.setPropertyName("test");
        assertEquals("test", propertyPointer.getName().getLocalPart());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = new TestPropertyPointer();
        assertEquals(-1, propertyPointer.getPropertyCount());
        propertyPointer.setPropertyCount(5);
        assertEquals(5, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer propertyPointer = new TestPropertyPointer();
        assertNull(propertyPointer.getPropertyNames());
        String[] propertyNames = new String[]{"test1", "test2"};
        propertyPointer.setPropertyNames(propertyNames);
        assertArrayEquals(propertyNames, propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new TestPropertyPointer();
        assertFalse(propertyPointer.isActualProperty());
        propertyPointer = new ActualPropertyPointer();
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new TestPropertyPointer();
        assertNull(propertyPointer.getImmediateNode());
        propertyPointer.setValue(new Object());
        assertSame(propertyPointer.getValue(), propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new TestPropertyPointer();
        assertFalse(propertyPointer.isCollection());
        propertyPointer.setValue(new int[5]);
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = new TestPropertyPointer();
        assertTrue(propertyPointer.isLeaf());
        propertyPointer.setValue(new Object[]{1, 2, 3});
        assertFalse(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new TestPropertyPointer();
        assertEquals(-1, propertyPointer.getLength());
        propertyPointer.setValue(new int[5]);
        assertEquals(5, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = new TestPropertyPointer();
        assertNull(propertyPointer.getImmediateValuePointer());
        propertyPointer.setValue(new Object());
        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
        assertNotNull(immediateValuePointer);
        assertEquals(propertyPointer, immediateValuePointer.getParent());
    }

    @Test
    public void testCreatePath() {
        TestPropertyFactory factory = new TestPropertyFactory();
        PropertyPointer propertyPointer = new TestPropertyPointer();
        propertyPointer.setBean(new Object());
        propertyPointer.setPropertyName("test");

        NodePointer nodePointer = propertyPointer.createPath(new JXPathContext() {
            @Override
            public AbstractFactory getAbstractFactory() {
                return factory;
            }
        });

        assertNotNull(nodePointer);
        assertEquals(propertyPointer, nodePointer.getParent());
    }

    @Test
    public void testCreatePathWithValue() {
        TestPropertyFactory factory = new TestPropertyFactory();
        PropertyPointer propertyPointer = new TestPropertyPointer();
        propertyPointer.setBean(new Object());
        propertyPointer.setPropertyName("test");

        NodePointer nodePointer = propertyPointer.createPath(new JXPathContext() {
            @Override
            public AbstractFactory getAbstractFactory() {
                return factory;
            }

            @Override
            public Object getBaseValue() {
                return new Object();
            }
        }, new Object());

        assertNotNull(nodePointer);
        assertEquals(propertyPointer, nodePointer.getParent());
    }

    @Test
    public void testCreateChild() {
        TestPropertyFactory factory = new TestPropertyFactory();
        PropertyPointer propertyPointer = new TestPropertyPointer();
        propertyPointer.setBean(new Object());
        propertyPointer.setPropertyName("test");

        NodePointer childNodePointer = propertyPointer.createChild(new JXPathContext() {
            @Override
            public AbstractFactory getAbstractFactory() {
                return factory;
            }
        }, new QName("child"), 0, new Object());

        assertNotNull(childNodePointer);
        assertEquals(propertyPointer, childNodePointer.getParent());
    }

    @Test
    public void testCreateChildWithName() {
        TestPropertyFactory factory = new TestPropertyFactory();
        PropertyPointer propertyPointer = new TestPropertyPointer();
        propertyPointer.setBean(new Object());
        propertyPointer.setPropertyName("test");

        NodePointer childNodePointer = propertyPointer.createChild(new JXPathContext() {
            @Override
            public AbstractFactory getAbstractFactory() {
                return factory;
            }
        }, new QName("child"), 0);

        assertNotNull(childNodePointer);
        assertEquals(propertyPointer, childNodePointer.getParent());
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer1 = new TestPropertyPointer();
        PropertyPointer propertyPointer2 = new TestPropertyPointer();
        PropertyPointer propertyPointer3 = new TestPropertyPointer();

        assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());
        assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());

        propertyPointer1.setPropertyName("test");
        propertyPointer2.setPropertyName("test");
        propertyPointer3.setPropertyName("test2");

        assertEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
        assertNotEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());

        propertyPointer1.setIndex(1);
        propertyPointer2.setIndex(1);
        propertyPointer3.setIndex(2);

        assertEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
        assertNotEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());

        propertyPointer1.setParent(new TestPropertyPointer());
        propertyPointer2.setParent(new TestPropertyPointer());
        propertyPointer3.setParent(new TestPropertyPointer());

        NodePointer parentPointer1 = propertyPointer1.getParent();
        NodePointer parentPointer2 = propertyPointer2.getParent();
        NodePointer parentPointer3 = propertyPointer3.getParent();

        assertSame(parentPointer1, parentPointer1);
        assertNotSame(parentPointer1, parentPointer2);
        assertNotSame(parentPointer1, parentPointer3);

        assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());
        assertEquals(propertyPointer1.hashCode(), parentPointer1.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new TestPropertyPointer();
        PropertyPointer propertyPointer2 = new TestPropertyPointer();
        PropertyPointer propertyPointer3 = new TestPropertyPointer();

        PropertyPointer propertyPointer1Clone = new TestPropertyPointer();
        PropertyPointer propertyPointer2Clone = new TestPropertyPointer();
        PropertyPointer propertyPointer3Clone = new TestPropertyPointer();

        PropertyPointer propertyPointer1SameIndex = new TestPropertyPointer();
        PropertyPointer propertyPointer1SameIndexClone = new TestPropertyPointer();

        assertEquals(propertyPointer1, propertyPointer1);
        assertNotEquals(propertyPointer1, propertyPointer2);
        assertNotEquals(propertyPointer1, propertyPointer3);

        propertyPointer1.setPropertyName("test");
        propertyPointer2.setPropertyName("test");
        propertyPointer3.setPropertyName("test2");

        assertEquals(propertyPointer1, propertyPointer2);
        assertNotEquals(propertyPointer1, propertyPointer3);

        propertyPointer1.setIndex(1);
        propertyPointer2.setIndex(1);
        propertyPointer3.setIndex(2);

        assertEquals(propertyPointer1, propertyPointer2);
        assertNotEquals(propertyPointer1, propertyPointer3);

        propertyPointer1.setParent(new TestPropertyPointer());
        propertyPointer2.setParent(new TestPropertyPointer());
        propertyPointer3.setParent(new TestPropertyPointer());

        NodePointer parentPointer1 = propertyPointer1.getParent();
        NodePointer parentPointer2 = propertyPointer2.getParent();
        NodePointer parentPointer3 = propertyPointer3.getParent();

        assertEquals(propertyPointer1, propertyPointer1Clone);
        assertEquals(propertyPointer1, parentPointer1);
        assertEquals(propertyPointer1, propertyPointer1Clone.getParent());

        assertEquals(propertyPointer1, propertyPointer1SameIndex);
        assertEquals(propertyPointer1, propertyPointer1SameIndexClone);
        assertEquals(propertyPointer1SameIndex, propertyPointer1SameIndexClone);
        assertEquals(propertyPointer1SameIndex, propertyPointer1SameIndex.getParent());
        assertEquals(propertyPointer1SameIndex, propertyPointer1SameIndexClone.getParent());

        assertFalse(propertyPointer1.equals(null));
        assertFalse(propertyPointer1.equals("test"));
        assertFalse(propertyPointer1.equals(new Object()));
        assertFalse(propertyPointer1.equals(new TestPropertyPointer()));
        assertFalse(propertyPointer1.equals(new TestPropertyPointer().clone()));

        assertFalse(propertyPointer1.equals(propertyPointer2));
        assertFalse(propertyPointer1.equals(propertyPointer3));
        assertFalse(propertyPointer1.equals(propertyPointer1Clone));
        assertFalse(propertyPointer1.equals(propertyPointer1SameIndex));
        assertFalse(propertyPointer1.equals(propertyPointer1Clone.getParent()));
        assertFalse(propertyPointer1.equals(propertyPointer1SameIndexClone));
        assertFalse(propertyPointer1.equals(propertyPointer1SameIndex.getParent()));
        assertFalse(propertyPointer1.equals(propertyPointer1SameIndexClone.getParent()));

        assertFalse(propertyPointer1Clone.equals(null));
        assertFalse(propertyPointer1Clone.equals("test"));
        assertFalse(propertyPointer1Clone.equals(new Object()));
        assertFalse(propertyPointer1Clone.equals(new TestPropertyPointer()));
        assertFalse(propertyPointer1Clone.equals(propertyPointer1));
        assertFalse(propertyPointer1Clone.equals(propertyPointer1.getParent()));
        assertFalse(propertyPointer1Clone.equals(propertyPointer1Clone.getParent()));

        assertFalse(propertyPointer1SameIndex.equals(null));
        assertFalse(propertyPointer1SameIndex.equals("test"));
        assertFalse(propertyPointer1SameIndex.equals(new Object()));
        assertFalse(propertyPointer1SameIndex.equals(new TestPropertyPointer()));
        assertFalse(propertyPointer1SameIndex.equals(propertyPointer1));
        assertFalse(propertyPointer1SameIndex.equals(propertyPointer1.getParent()));
        assertFalse(propertyPointer1SameIndex.equals(propertyPointer1Clone));
        assertFalse(propertyPointer1SameIndex.equals(propertyPointer1Clone.getParent()));
        assertEquals(propertyPointer1SameIndex, propertyPointer1SameIndex);
        assertEquals(propertyPointer1SameIndex, propertyPointer1SameIndex.getParent());

        assertFalse(propertyPointer2.equals(null));
        assertFalse(propertyPointer2.equals("test"));
        assertFalse(propertyPointer2.equals(new Object()));
        assertFalse(propertyPointer2.equals(new TestPropertyPointer()));
        assertFalse(propertyPointer2.equals(propertyPointer1));
        assertFalse(propertyPointer2.equals(propertyPointer1.getParent()));
        assertFalse(propertyPointer2.equals(propertyPointer1Clone));
        assertFalse(propertyPointer2.equals(propertyPointer1Clone.getParent()));
        assertFalse(propertyPointer2.equals(propertyPointer1SameIndex));
        assertFalse(propertyPointer2.equals(propertyPointer1SameIndex.getParent()));
        assertFalse(propertyPointer2.equals(propertyPointer1SameIndexClone));
        assertFalse(propertyPointer2.equals(propertyPointer1SameIndexClone.getParent()));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer propertyPointer1 = new TestPropertyPointer();
        PropertyPointer propertyPointer2 = new TestPropertyPointer();
        PropertyPointer propertyPointer3 = new TestPropertyPointer();

        NodePointer nodePointer1 = new NodePointer();
        NodePointer nodePointer2 = new NodePointer();
        NodePointer nodePointer3 = new NodePointer();

        assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer1));
        assertEquals(-1, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer2));
        assertEquals(1, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer1));

        assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, nodePointer1));
        assertEquals(-1, propertyPointer1.compareChildNodePointers(nodePointer1, propertyPointer1));

        assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer1));
        assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer1.clone()));

        assertEquals(-1, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer1));
        assertEquals(1, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer2));

        assertEquals(0, propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer1));
        assertEquals(-1, nodePointer1.compareChildNodePointers(propertyPointer1, nodePointer1));
        assertEquals(-1, nodePointer1.compareChildNodePointers(nodePointer1, propertyPointer2));
        assertEquals(1, propertyPointer1.compareChildNodePointers(propertyPointer2, nodePointer1));

        assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer3));
        assertEquals(-1, propertyPointer1.compareChildNodePointers(propertyPointer3, propertyPointer1));

        assertEquals(propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer1),
                     propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer3) + propertyPointer3.compareChildNodePointers(propertyPointer1, propertyPointer3));
    }
}

class TestPropertyPointer extends PropertyPointer {
    @Override
    public String getPropertyName() {
        return "test";
    }

    @Override
    public void setPropertyName(String propertyName) {
        // Intentionally empty
    }
}

class ActualPropertyPointer extends PropertyPointer {
    @Override
    public boolean isActualProperty() {
        return true;
    }
}

class TestPropertyFactory extends AbstractFactory {
    @Override
    public Object createObject(JXPathContext context, NodePointer parent, String propertyName, int index) {
        return new Object();
    }
}
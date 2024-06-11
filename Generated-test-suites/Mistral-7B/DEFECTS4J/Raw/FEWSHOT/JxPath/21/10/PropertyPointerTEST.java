package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointer() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        assertNull(propertyPointer.getValue());
        propertyPointer.setValue(new Object());
        assertSame(propertyPointer.getValue(), propertyPointer.getImmediateNode());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertEquals(-1, propertyPointer.getLength());
        assertNull(propertyPointer.getImmediateValuePointer());
        
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = propertyPointer.createPath(context);
        assertSame(nodePointer, propertyPointer);
        assertSame(nodePointer.getNode(), propertyPointer.getValue());
    }

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(0);
        assertEquals(0, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        assertNull(propertyPointer.getBean());
        propertyPointer.setBean(new Object());
        assertSame(propertyPointer.getBean(), propertyPointer.getImmediateParentPointer().getNode());
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        assertNull(propertyPointer.getName());
        propertyPointer.setPropertyName("propertyName");
        assertEquals("propertyName", propertyPointer.getName().getLocalPart());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        assertEquals(-1, propertyPointer.getPropertyCount());
        propertyPointer = new ArrayPropertyPointer(new DummyNodePointer(), new DummyNodePointer());
        assertEquals(5, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        assertNull(propertyPointer.getPropertyNames());
        propertyPointer = new ArrayPropertyPointer(new DummyNodePointer(), new DummyNodePointer());
        String[] propertyNames = propertyPointer.getPropertyNames();
        assertEquals(5, propertyNames.length);
        assertEquals("name1", propertyNames[0]);
        assertEquals("name2", propertyNames[1]);
        assertEquals("name3", propertyNames[2]);
        assertEquals("name4", propertyNames[3]);
        assertEquals("name5", propertyNames[4]);
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        assertFalse(propertyPointer.isActualProperty());
        propertyPointer = new ActualPropertyPointer(new DummyNodePointer(), "propertyName");
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        assertNull(propertyPointer.getImmediateNode());
        propertyPointer.setValue(new Object());
        assertSame(propertyPointer.getValue(), propertyPointer.getImmediateNode());
        propertyPointer = new ArrayPropertyPointer(new DummyNodePointer(), new DummyNodePointer());
        assertSame(propertyPointer.getValueAt(0), propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        assertFalse(propertyPointer.isCollection());
        propertyPointer = new ArrayPropertyPointer(new DummyNodePointer(), new DummyNodePointer());
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        assertFalse(propertyPointer.isLeaf());
        propertyPointer = new ScalarPropertyPointer(new DummyNodePointer(), "propertyName");
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        assertEquals(-1, propertyPointer.getLength());
        propertyPointer = new ArrayPropertyPointer(new DummyNodePointer(), new DummyNodePointer());
        assertEquals(5, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        assertNull(propertyPointer.getImmediateValuePointer());
        propertyPointer.setValue(new Object());
        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
        assertNotNull(immediateValuePointer);
        assertSame(propertyPointer, immediateValuePointer.getParent());
        assertNull(immediateValuePointer.getNode());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = propertyPointer.createPath(context);
        assertSame(nodePointer, propertyPointer);
        assertSame(nodePointer.getNode(), propertyPointer.getValue());

        propertyPointer = new ArrayPropertyPointer(new DummyNodePointer(), new DummyNodePointer());
        nodePointer = propertyPointer.createPath(context);
        assertSame(nodePointer, propertyPointer);
        assertSame(nodePointer.getNode(), propertyPointer.getValueAt(0));
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        JXPathContext context = new JXPathContext();
        propertyPointer.createPath(context, new Object());
        NodePointer nodePointer = propertyPointer.clone().getImmediateValuePointer();
        assertNotNull(nodePointer);
        assertSame(nodePointer.getNode(), propertyPointer.getValue());
    }

    @Test
    public void testCreateChild() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        JXPathContext context = new JXPathContext();
        NodePointer childNodePointer = propertyPointer.createChild(context, new QName("name"), 0, new Object());
        assertNotNull(childNodePointer);
        assertSame(childNodePointer.getParent(), propertyPointer);
        assertSame(childNodePointer.getNode(), propertyPointer.getValueAt(0));
    }

    @Test
    public void testCreateChildWithName() {
        PropertyPointer propertyPointer = new DummyPropertyPointer(new DummyNodePointer());
        JXPathContext context = new JXPathContext();
        NodePointer childNodePointer = propertyPointer.createChild(context, new QName("name"));
        assertNotNull(childNodePointer);
        assertSame(childNodePointer.getParent(), propertyPointer);
        assertNull(childNodePointer.getNode());
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer1 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer2 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer3 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer4 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer5 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer6 = new DummyPropertyPointer(new DummyNodePointer());

        propertyPointer1.setPropertyName("name1");
        propertyPointer2.setPropertyName("name1");
        propertyPointer3.setPropertyName("name2");
        propertyPointer4.setPropertyName("name2");
        propertyPointer5.setPropertyName("name3");
        propertyPointer6.setPropertyName("name3");

        propertyPointer1.setPropertyIndex(0);
        propertyPointer2.setPropertyIndex(1);
        propertyPointer3.setPropertyIndex(0);
        propertyPointer4.setPropertyIndex(1);
        propertyPointer5.setPropertyIndex(2);
        propertyPointer6.setPropertyIndex(2);

        int hashCode1 = propertyPointer1.hashCode();
        int hashCode2 = propertyPointer2.hashCode();
        int hashCode3 = propertyPointer3.hashCode();
        int hashCode4 = propertyPointer4.hashCode();
        int hashCode5 = propertyPointer5.hashCode();
        int hashCode6 = propertyPointer6.hashCode();

        assertEquals(hashCode1, propertyPointer1.hashCode());
        assertEquals(hashCode2, propertyPointer2.hashCode());
        assertEquals(hashCode3, propertyPointer3.hashCode());
        assertEquals(hashCode4, propertyPointer4.hashCode());
        assertEquals(hashCode5, propertyPointer5.hashCode());
        assertEquals(hashCode6, propertyPointer6.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer2 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer3 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer4 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer5 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer6 = new DummyPropertyPointer(new DummyNodePointer());

        propertyPointer1.setPropertyName("name1");
        propertyPointer2.setPropertyName("name1");
        propertyPointer3.setPropertyName("name2");
        propertyPointer4.setPropertyName("name2");
        propertyPointer5.setPropertyName("name3");
        propertyPointer6.setPropertyName("name3");

        propertyPointer1.setPropertyIndex(0);
        propertyPointer2.setPropertyIndex(1);
        propertyPointer3.setPropertyIndex(0);
        propertyPointer4.setPropertyIndex(1);
        propertyPointer5.setPropertyIndex(2);
        propertyPointer6.setPropertyIndex(2);

        assertFalse(propertyPointer1.equals(propertyPointer2));
        assertFalse(propertyPointer1.equals(propertyPointer3));
        assertFalse(propertyPointer1.equals(propertyPointer4));
        assertFalse(propertyPointer1.equals(propertyPointer5));
        assertFalse(propertyPointer1.equals(propertyPointer6));

        PropertyPointer propertyPointer7 = new DummyPropertyPointer(new DummyNodePointer());
        propertyPointer7.setPropertyName("name1");
        propertyPointer7.setPropertyIndex(0);
        assertTrue(propertyPointer1.equals(propertyPointer7));

        PropertyPointer propertyPointer8 = new DummyPropertyPointer(new DummyNodePointer());
        propertyPointer8.setPropertyName("name1");
        propertyPointer8.setPropertyIndex(0);
        propertyPointer8.setParent(propertyPointer1);
        assertTrue(propertyPointer1.equals(propertyPointer8));

        PropertyPointer propertyPointer9 = new DummyPropertyPointer(new DummyNodePointer());
        propertyPointer9.setPropertyName("name1");
        propertyPointer9.setPropertyIndex(0);
        propertyPointer9.setParent(propertyPointer3);
        assertFalse(propertyPointer1.equals(propertyPointer9));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer propertyPointer1 = new DummyPropertyPointer(new DummyNodePointer());
        PropertyPointer propertyPointer2 = new DummyPropertyPointer(new DummyNodePointer());
        NodePointer nodePointer1 = new DummyNodePointer();
        NodePointer nodePointer2 = new DummyNodePointer();

        int result = propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer2);
        assertEquals(0, result);

        propertyPointer1.setValue(nodePointer1);
        propertyPointer2.setValue(nodePointer2);

        result = propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer2);
        assertEquals(-1, result);

        result = propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer1);
        assertEquals(1, result);
    }
}

class DummyNodePointer extends NodePointer {
    public DummyNodePointer() {
        super(null, 0);
    }

    @Override
    public NodePointer clone() {
        return new DummyNodePointer();
    }

    @Override
    public String toString() {
        return "DummyNodePointer";
    }
}

abstract class DummyPropertyPointer extends PropertyPointer {
    public DummyPropertyPointer(NodePointer parent) {
        super(parent);
    }

    @Override
    public String getPropertyName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPropertyName(String propertyName) {
        throw new UnsupportedOperationException();
    }
}

class ScalarPropertyPointer extends DummyPropertyPointer {
    public ScalarPropertyPointer(NodePointer parent, String propertyName) {
        super(parent);
        setPropertyName(propertyName);
    }

    @Override
    public int getPropertyCount() {
        return 1;
    }

    @Override
    public String[] getPropertyNames() {
        return new String[]{"propertyName"};
    }

    @Override
    public boolean isActualProperty() {
        return true;
    }
}

class ArrayPropertyPointer extends DummyPropertyPointer {
    private final NodePointer arrayNodePointer;
    private final int length;

    public ArrayPropertyPointer(NodePointer parent, NodePointer arrayNodePointer) {
        super(parent);
        this.arrayNodePointer = arrayNodePointer;
        setPropertyName("array");
        setPropertyIndex(UNSPECIFIED_PROPERTY);
        setIndex(UNSPECIFIED_PROPERTY);

        JXPathIntrospector introspector = new JXPathIntrospector();
        int length = introspector.getBeanPropertyCount(arrayNodePointer.getNode());
        this.length = length;
    }

    @Override
    public int getPropertyCount() {
        return length;
    }

    @Override
    public String[] getPropertyNames() {
        String[] propertyNames = new String[length];
        for (int i = 0; i < length; i++) {
            JXPathIntrospector introspector = new JXPathIntrospector();
            propertyNames[i] = introspector.getPropertyName(arrayNodePointer.getNode(), i);
        }
        return propertyNames;
    }

    @Override
    public Object getBaseValue() {
        return arrayNodePointer.getNode();
    }

    @Override
    public Object getValueAt(int index) {
        return ValueUtils.getValue(arrayNodePointer.getNode(), index);
    }

    @Override
    public boolean isActualProperty() {
        return false;
    }
}
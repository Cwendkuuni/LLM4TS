package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathIntrospector;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;
    private NodePointer parentPointer;

    @Before
    public void setUp() {
        parentPointer = new NodePointer();
        propertyPointer = new TestPropertyPointer(parentPointer);
    }

    @Test
    public void testGetPropertyIndex() {
        Assert.assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(1);
        Assert.assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Assert.assertNull(propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        QName expected = new QName(null, "propertyName");
        propertyPointer.setPropertyName("propertyName");
        QName actual = propertyPointer.getName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActual() {
        Assert.assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        Assert.assertNotNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        Assert.assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        Assert.assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        Assert.assertEquals(-1, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer actual = propertyPointer.getImmediateValuePointer();
        Assert.assertNotNull(actual);
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        AbstractFactory factory = new AbstractFactory();
        context.setFactory(factory);
        try {
            propertyPointer.createPath(context);
            Assert.fail("Expected JXPathAbstractFactoryException to be thrown");
        } catch (JXPathAbstractFactoryException e) {
            // Exception expected
        }
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        AbstractFactory factory = new AbstractFactory();
        context.setFactory(factory);
        propertyPointer.setValue("test");
        propertyPointer.createPath(context, "test");
        Assert.assertEquals("test", propertyPointer.getValue());
    }

    @Test
    public void testCreateChildNodePointerWithValue() {
        JXPathContext context = new JXPathContext();
        AbstractFactory factory = new AbstractFactory();
        context.setFactory(factory);
        QName name = new QName(null, "child");
        NodePointer actual = propertyPointer.createChild(context, name, 0, "test");
        Assert.assertNotNull(actual);
    }

    @Test
    public void testCreateChildNodePointerWithoutValue() {
        JXPathContext context = new JXPathContext();
        AbstractFactory factory = new AbstractFactory();
        context.setFactory(factory);
        QName name = new QName(null, "child");
        NodePointer actual = propertyPointer.createChild(context, name, 0);
        Assert.assertNotNull(actual);
    }

    @Test
    public void testHashCode() {
        Assert.assertNotEquals(0, propertyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        Assert.assertFalse(propertyPointer.equals(null));
        Assert.assertFalse(propertyPointer.equals(new Object()));
        Assert.assertTrue(propertyPointer.equals(propertyPointer));
        Assert.assertTrue(propertyPointer.equals(new TestPropertyPointer(parentPointer)));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        Assert.assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }
}

class TestPropertyPointer extends PropertyPointer {

    public TestPropertyPointer(NodePointer parent) {
        super(parent);
    }

    @Override
    public String getPropertyName() {
        return null;
    }

    @Override
    public void setPropertyName(String propertyName) {

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
    protected boolean isActualProperty() {
        return false;
    }
}
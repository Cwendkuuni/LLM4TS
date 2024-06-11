package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;
    private NodePointer parentNodePointer;

    @Before
    public void setUp() {
        parentNodePointer = new NodePointer(new Object());
        propertyPointer = new TestPropertyPointer(parentNodePointer);
    }

    @Test
    public void testGetPropertyIndex() {
        Assert.assertEquals(-1, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(0);
        Assert.assertEquals(0, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(0);
        propertyPointer.setPropertyIndex(1);
        Assert.assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyIndexIllegalArgument() {
        propertyPointer.setPropertyIndex(-1);
    }

    @Test
    public void testGetBean() {
        Assert.assertNotNull(propertyPointer.getBean());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetBeanBeforeSetParent() {
        propertyPointer.getBean();
    }

    @Test
    public void testGetName() {
        Assert.assertNull(propertyPointer.getName());
        propertyPointer.setPropertyName("testPropertyName");
        Assert.assertEquals("testPropertyName", propertyPointer.getName().getLocalPart());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyNameIllegalArgument() {
        propertyPointer.setPropertyName(null);
    }

    @Test
    public void testGetPropertyCount() {
        Assert.assertEquals(-1, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        String[] expected = new String[]{"testProperty1", "testProperty2"};
        propertyPointer.setPropertyNames(expected);
        Assert.assertArrayEquals(expected, propertyPointer.getPropertyNames());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyNamesIllegalArgument() {
        String[] names = new String[0];
        propertyPointer.setPropertyNames(names);
    }

    @Test
    public void testIsActualProperty() {
        Assert.assertFalse(propertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        Assert.assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        Object expected = new Object();
        propertyPointer.setValue(expected);
        Assert.assertEquals(expected, propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        List list = new ArrayList();
        list.add(new Object());
        propertyPointer.setValue(list);
        Assert.assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        Assert.assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        List list = new ArrayList();
        list.add(new Object());
        list.add(new Object());
        propertyPointer.setValue(list);
        Assert.assertEquals(2, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        Object expected = new Object();
        propertyPointer.setValue(expected);
        NodePointer expectedNodePointer = NodePointer.newChildNodePointer(
                propertyPointer,
                propertyPointer.getName(),
                propertyPointer.getImmediateNode());
        Assert.assertEquals(expectedNodePointer, propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        Object expected = new Object();
        propertyPointer.createPath(new JXPathContext(new Object()));
        Assert.assertEquals(expected, propertyPointer.getNode());
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePathFailure() {
        propertyPointer.createPath(new JXPathContext(new Object()));
        // This line should throw an exception
        propertyPointer.createPath(new JXPathContext(new Object()));
    }

    @Test
    public void testCreateChild() {
        String name = "testChildProperty";
        Object value = new Object();
        NodePointer child = propertyPointer.createChild(new JXPathContext(new Object()), name, 0, value);
        Assert.assertEquals(name, child.getName().getLocalPart());
        Assert.assertEquals(value, child.getNode());
    }

    @Test
    public void testCreateChildFailure() {
        String name = "testChildProperty";
        propertyPointer.createChild(new JXPathContext(new Object()), name, 0);
        // This line should throw an exception
        propertyPointer.createChild(new JXPathContext(new Object()), name, 0);
    }

    @Test
    public void testHashCode() {
        NodePointer anotherPropertyPointer = new TestPropertyPointer(parentNodePointer);
        Assert.assertEquals(propertyPointer.hashCode(), anotherPropertyPointer.hashCode());

        propertyPointer.setPropertyIndex(0);
        Assert.assertEquals(propertyPointer.hashCode(), anotherPropertyPointer.hashCode());

        anotherPropertyPointer.setPropertyIndex(1);
        Assert.neq(propertyPointer.hashCode(), anotherPropertyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        NodePointer anotherPropertyPointer = new TestPropertyPointer(parentNodePointer);
        Assert.assertTrue(propertyPointer.equals(anotherPropertyPointer));

        propertyPointer.setPropertyIndex(0);
        Assert.assertTrue(propertyPointer.equals(anotherPropertyPointer));

        anotherPropertyPointer.setPropertyIndex(1);
        Assert.assertFalse(propertyPointer.equals(anotherPropertyPointer));

        anotherPropertyPointer = new TestPropertyPointer(parentNodePointer);
        anotherPropertyPointer.setPropertyIndex(0);
        anotherPropertyPointer.setPropertyName("testPropertyName");
        Assert.assertFalse(propertyPointer.equals(anotherPropertyPointer));

        anotherPropertyPointer = new NodePointer(new Object());
        Assert.assertFalse(propertyPointer.equals(anotherPropertyPointer));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer anotherPropertyPointer = new TestPropertyPointer(parentNodePointer);
        int result = propertyPointer.compareChildNodePointers(new NodePointer(new Object()), anotherPropertyPointer);
        Assert.assertEquals(-1, result);

        result = propertyPointer.compareChildNodePointers(new NodePointer(new Object()), propertyPointer);
        Assert.assertEquals(0, result);

        result = propertyPointer.compareChildNodePointers(propertyPointer, anotherPropertyPointer);
        Assert.assertEquals(1, result);
    }
}

class TestPropertyPointer extends PropertyPointer {

    public TestPropertyPointer(NodePointer parent) {
        super(parent);
    }

    @Override
    public String getPropertyName() {
        return "testPropertyName";
    }

    @Override
    public void setPropertyName(String propertyName) {
        // Do nothing
    }

    @Override
    public int getPropertyCount() {
        return 2;
    }

    @Override
    public String[] getPropertyNames() {
        return new String[]{"testProperty1", "testProperty2"};
    }

    @Override
    public boolean isActualProperty() {
        return true;
    }
}
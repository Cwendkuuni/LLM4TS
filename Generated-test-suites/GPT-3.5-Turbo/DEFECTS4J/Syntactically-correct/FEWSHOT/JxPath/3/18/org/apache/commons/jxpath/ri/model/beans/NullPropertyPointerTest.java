package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {
    
    private NodePointer parent;
    private NullPropertyPointer nullPropertyPointer;
    
    @Before
    public void setup() {
        parent = new NodePointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
    }

    @Test
    public void testGetName() {
        Assert.assertNotNull(nullPropertyPointer.getName());
        Assert.assertEquals("*", nullPropertyPointer.getName().getName());
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(0);
    }

    @Test
    public void testGetLength() {
        Assert.assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        Assert.assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        Assert.assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        Assert.assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NodePointer valuePointer = nullPropertyPointer.getValuePointer();
        Assert.assertNotNull(valuePointer);
        Assert.assertTrue(valuePointer instanceof NullPointer);
        Assert.assertEquals("*", valuePointer.getQName().getName());
    }

    @Test
    public void testIsActualProperty() {
        Assert.assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        Assert.assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        Assert.assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_nullParentContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_parentNotContainer() {
        parent.setContainer(false);
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_dynamicPropertyCreationNotSupported() {
        parent.setContainer(true);
        nullPropertyPointer.setValue("value");
    }

    @Test
    public void testSetValue_dynamicPropertyCreationSupported() {
        parent.setContainer(true);
        parent.setPropertyOwnerPointer(new PropertyOwnerPointer());
        nullPropertyPointer.setValue("value");
    }

    @Test
    public void testCreatePath_attribute() {
        JXPathContext context = new JXPathContext();
        NodePointer path = nullPropertyPointer.createPath(context);
        Assert.assertNotNull(path);
        Assert.assertTrue(path instanceof NullPointer);
        Assert.assertEquals("*", path.getQName().getName());
    }

    @Test
    public void testCreatePath_notAttribute() {
        JXPathContext context = new JXPathContext();
        parent.setPropertyOwnerPointer(new PropertyOwnerPointer());
        NodePointer path = nullPropertyPointer.createPath(context);
        Assert.assertNotNull(path);
        Assert.assertTrue(path instanceof PropertyPointer);
        Assert.assertEquals("*", path.getQName().getName());
    }

    @Test
    public void testCreatePath_value() {
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        NodePointer path = nullPropertyPointer.createPath(context, value);
        Assert.assertNotNull(path);
        Assert.assertTrue(path instanceof NullPointer);
        Assert.assertEquals("*", path.getQName().getName());
        Assert.assertEquals(value, path.getValue());
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("child");
        int index = 1;
        NodePointer childPath = nullPropertyPointer.createChild(context, name, index);
        Assert.assertNotNull(childPath);
        Assert.assertTrue(childPath instanceof NullPointer);
        Assert.assertEquals("child", childPath.getQName().getName());
        Assert.assertEquals(index, childPath.getIndex());
    }

    @Test
    public void testCreateChild_value() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("child");
        int index = 1;
        Object value = new Object();
        NodePointer childPath = nullPropertyPointer.createChild(context, name, index, value);
        Assert.assertNotNull(childPath);
        Assert.assertTrue(childPath instanceof NullPointer);
        Assert.assertEquals("child", childPath.getQName().getName());
        Assert.assertEquals(index, childPath.getIndex());
        Assert.assertEquals(value, childPath.getValue());
    }

    @Test
    public void testGetPropertyName() {
        Assert.assertEquals("*", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("name");
        Assert.assertEquals("name", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("name");
        Assert.assertEquals("name", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        Assert.assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        Assert.assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        Assert.assertNotNull(nullPropertyPointer.getPropertyNames());
        Assert.assertEquals(0, nullPropertyPointer.getPropertyNames().length);
    }

    @Test
    public void testAsPath() {
        Assert.assertEquals("*", nullPropertyPointer.asPath());
    }

    @Test
    public void testEscape() {
        String string = "test'escape'";
        String escapedString = nullPropertyPointer.escape(string);
        Assert.assertNotNull(escapedString);
        Assert.assertEquals("test&apos;escape&apos;", escapedString);
    }
}
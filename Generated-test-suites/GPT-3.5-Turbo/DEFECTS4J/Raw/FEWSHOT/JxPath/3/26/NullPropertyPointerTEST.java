package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {

    private NullPropertyPointer nullPropertyPointer;

    @Before
    public void setup() {
        nullPropertyPointer = new NullPropertyPointer(null);
    }

    @Test
    public void testGetName() {
        QName expectedName = new QName("*");
        QName actualName = nullPropertyPointer.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(0);
    }

    @Test
    public void testGetLength() {
        int expectedLength = 0;
        int actualLength = nullPropertyPointer.getLength();

        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetBaseValue() {
        Object expectedValue = null;
        Object actualValue = nullPropertyPointer.getBaseValue();

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetImmediateNode() {
        Object expectedNode = null;
        Object actualNode = nullPropertyPointer.getImmediateNode();

        assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testIsLeaf() {
        boolean isLeaf = nullPropertyPointer.isLeaf();

        assertTrue(isLeaf);
    }

    @Test
    public void testGetValuePointer() {
        NullPointer expectedPointer = new NullPointer(null, new QName(nullPropertyPointer.getPropertyName()));
        NodePointer actualPointer = nullPropertyPointer.getValuePointer();

        assertEquals(expectedPointer.getClass(), actualPointer.getClass());
        assertEquals(expectedPointer.getName(), actualPointer.getName());
    }

    @Test
    public void testIsActualProperty() {
        boolean isActualProperty = nullPropertyPointer.isActualProperty();

        assertFalse(isActualProperty);
    }

    @Test
    public void testIsActual() {
        boolean isActual = nullPropertyPointer.isActual();

        assertFalse(isActual);
    }

    @Test
    public void testIsContainer() {
        boolean isContainer = nullPropertyPointer.isContainer();

        assertTrue(isContainer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NodePointer parent = new NullPointer(null, new QName("parent"));
        nullPropertyPointer.setParent(parent);

        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithDynamicPropertyDeclarationNotSupported() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer(null);
        nullPropertyPointer.setParent(parent);

        nullPropertyPointer.setValue("value");
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = JXPathContext.newContext(null);

        NodePointer expectedPointer = new PropertyPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        NodePointer actualPointer = nullPropertyPointer.createPath(context);

        assertEquals(expectedPointer.getClass(), actualPointer.getClass());
        assertEquals(expectedPointer.getName(), actualPointer.getName());
        assertEquals(expectedPointer.getIndex(), actualPointer.getIndex());
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = JXPathContext.newContext(null);
        Object value = "value";

        NodePointer expectedPointer = new PropertyPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        expectedPointer.setValue(value);
        NodePointer actualPointer = nullPropertyPointer.createPath(context, value);

        assertEquals(expectedPointer.getClass(), actualPointer.getClass());
        assertEquals(expectedPointer.getName(), actualPointer.getName());
        assertEquals(expectedPointer.getIndex(), actualPointer.getIndex());
        assertEquals(expectedPointer.getImmediateParentPointer(), actualPointer.getImmediateParentPointer());
    }

    @Test
    public void testCreateChild() {
        String propertyName = "childProperty";
        QName name = new QName(propertyName);
        int index = 0;

        NodePointer parent = new NullPointer(null, new QName("parent"));
        nullPropertyPointer.setParent(parent);
        JXPathContext context = JXPathContext.newContext(null);

        NodePointer expectedPointer = parent.createChild(context, name, index);
        NodePointer actualPointer = nullPropertyPointer.createChild(context, name, index);

        assertEquals(expectedPointer, actualPointer);
    }

    @Test
    public void testCreateChildWithValue() {
        String propertyName = "childProperty";
        QName name = new QName(propertyName);
        int index = 0;
        Object value = "value";

        NodePointer parent = new NullPointer(null, new QName("parent"));
        nullPropertyPointer.setParent(parent);
        JXPathContext context = JXPathContext.newContext(null);

        NodePointer expectedPointer = parent.createChild(context, name, index, value);
        NodePointer actualPointer = nullPropertyPointer.createChild(context, name, index, value);

        assertEquals(expectedPointer, actualPointer);
    }

    @Test
    public void testGetPropertyName() {
        String expectedName = "*";
        String actualName = nullPropertyPointer.getPropertyName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void testSetPropertyName() {
        String propertyName = "property";

        nullPropertyPointer.setPropertyName(propertyName);
    }

    @Test
    public void testSetNameAttributeValue() {
        String attributeValue = "attrValue";

        nullPropertyPointer.setNameAttributeValue(attributeValue);
        String actualName = nullPropertyPointer.getPropertyName();

        assertEquals(attributeValue, actualName);
    }

    @Test
    public void testIsCollection() {
        boolean isCollection = nullPropertyPointer.isCollection();

        assertFalse(isCollection);
    }

    @Test
    public void testGetPropertyCount() {
        int propertyCount = nullPropertyPointer.getPropertyCount();

        assertEquals(0, propertyCount);
    }

    @Test
    public void testGetPropertyNames() {
        String[] propertyNames = nullPropertyPointer.getPropertyNames();

        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPath() {
        String expectedPath = "@name='*'";
        String actualPath = nullPropertyPointer.asPath();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testEscape() {
        String string = "foo'bar\"baz";
        String expectedString = "foo&apos;bar&quot;baz";
        String actualString = nullPropertyPointer.escape(string);

        assertEquals(expectedString, actualString);
    }
}
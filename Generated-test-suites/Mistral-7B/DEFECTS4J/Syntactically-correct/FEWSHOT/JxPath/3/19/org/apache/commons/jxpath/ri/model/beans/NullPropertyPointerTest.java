package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NullPropertyPointerTest {

    @Test
    public void testNewNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertTrue(nullPropertyPointer.isContainer());
        assertFalse(nullPropertyPointer.isActual());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertEquals("", nullPropertyPointer.getPropertyName());
        assertNull(nullPropertyPointer.getValuePointer());
        assertSame(parent, nullPropertyPointer.getParent());
        assertNull(nullPropertyPointer.getImmediateNode());
        assertNull(nullPropertyPointer.getBaseValue());
        assertFalse(nullPropertyPointer.isLeaf());
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("testProperty");
        QName expected = new QName("testProperty");
        QName actual = nullPropertyPointer.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", nullPropertyPointer.getPropertyName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer expected = new NodePointer(new Object());
        NodePointer actual = nullPropertyPointer.createPath(context);
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        nullPropertyPointer.createPath(context, new Object());
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testName");
        NodePointer expected = new NodePointer(new Object());
        NodePointer actual = nullPropertyPointer.createChild(context, name, 0);
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testName");
        Object value = new Object();
        nullPropertyPointer.createChild(context, name, 0, value);
    }

    @Test
    public void testIsContainer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyIndex(1);
    }

    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsDynamicPropertyDeclarationSupported() {
        NodePointer parent = new NodePointer(new Object());
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isDynamicPropertyDeclarationSupported());
        assertTrue(propertyOwnerPointer.isDynamicPropertyDeclarationSupported());
    }

    @Test
    public void testValuePointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer valuePointer = nullPropertyPointer.getValuePointer();
        assertInstanceOf(NullPointer.class, valuePointer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testInvalidSetValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("testValue");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testInvalidSetPropertyIndex() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyIndex(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidSetIndex() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.createChild(new JXPathContext(), new QName("testName"), -1);
    }

    @Test
    public void testPropertyName() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("testProperty");
        assertEquals("testProperty", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testPropertyCount() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testPropertyNames() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertEquals(new String[0], propertyNames);
    }

    @Test
    public void testAsPath() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals("", nullPropertyPointer.asPath());

        NullPropertyPointer nullPropertyPointerByNameAttribute = new NullPropertyPointer(parent);
        nullPropertyPointerByNameAttribute.setNameAttributeValue("testProperty");
        assertEquals("[@name='testProperty']", nullPropertyPointerByNameAttribute.asPath());
    }

    @Test
    public void testEscape() {
        String testString = "testString with 'single quote' and \"double quote\"";
        String escapedString = NullPropertyPointer.escape(testString);
        String expectedEscapedString = "testString with &apos;single quote&apos; and &quot;double quote&quot;";
        assertEquals(expectedEscapedString, escapedString);
    }
}
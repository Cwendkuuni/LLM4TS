package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.cli.beans.NullPropertyPointer;
import org.apache.commons.cli.model.QName;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName name = nullPropertyPointer.getName();
        assertEquals("*", name.getLocalPart());
        assertEquals("", name.getNamespaceURI());

        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(new PropertyOwnerPointer(null));
        QName name1 = nullPropertyPointer1.getName();
        assertEquals("", name1.getLocalPart());
        assertEquals("", name1.getNamespaceURI());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsContainer() {
        assertTrue(new NullPropertyPointer(null).isContainer());
    }

    @Test
    public void testIsLeaf() {
        assertTrue(new NullPropertyPointer(null).isLeaf());
    }

    @Test
    public void testIsActualProperty() {
        assertFalse(new NullPropertyPointer(null).isActualProperty());
    }

    @Test
    public void testIsActual() {
        assertFalse(new NullPropertyPointer(null).isActual());
    }

    @Test
    public void testGetBaseValue() {
        assertNull(new NullPropertyPointer(null).getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        assertNull(new NullPropertyPointer(null).getImmediateNode());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueThrowsExceptionWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("test");
    }

    @Test
    public void testCreatePathWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.createPath(null));
    }

    @Test
    public void testCreatePathWithPropertyOwnerParent() {
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
        NodePointer nodePointer = nullPropertyPointer.createPath(null);
        assertTrue(nodePointer instanceof PropertyPointer);
    }

    @Test
    public void testCreatePath() {
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
        NodePointer nodePointer = nullPropertyPointer.createPath(null);
        assertEquals(propertyOwnerPointer, nodePointer.getParent());
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
        NodePointer nodePointer = nullPropertyPointer.createPath(null, "test");
        assertTrue(nodePointer instanceof PropertyPointer);
        assertEquals("test", nodePointer.getValue().toString());
    }

    @Test
    public void testCreateChildWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.createChild(null, new QName("test"), 0));
    }

    @Test
    public void testCreateChildWithPropertyOwnerParent() {
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
        NodePointer nodePointer = nullPropertyPointer.createChild(null, new QName("test"), 0);
        assertTrue(nodePointer instanceof PropertyPointer);
        assertEquals("test", nodePointer.getName().getLocalPart());
    }

    @Test
    public void testCreateChild() {
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
        NodePointer nodePointer = nullPropertyPointer.createChild(null, new QName("test"), 0, "test");
        assertTrue(nodePointer instanceof PropertyPointer);
        assertEquals("test", nodePointer.getValue().toString());
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("", nullPropertyPointer.asPath());

        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(propertyOwnerPointer);
        assertEquals("", nullPropertyPointer1.asPath());

        PropertyOwnerPointer propertyOwnerPointer1 = new PropertyOwnerPointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(propertyOwnerPointer1);
        NullPropertyPointer nullPropertyPointer2 = new NullPropertyPointer(propertyOwnerPointer1);
        nullPropertyPointer2.setPropertyName("test");
        assertEquals("[@name='test']", nullPropertyPointer2.asPath());

        PropertyOwnerPointer propertyOwnerPointer2 = new PropertyOwnerPointer(null);
        PropertyPointer propertyPointer1 = new PropertyPointer(propertyOwnerPointer2);
        PropertyPointer propertyPointer2 = new PropertyPointer(propertyOwnerPointer2);
        NullPropertyPointer nullPropertyPointer3 = new NullPropertyPointer(propertyOwnerPointer2);
        nullPropertyPointer3.setPropertyName("test");
        nullPropertyPointer3.setIndex(1);
        assertEquals("[@name='test'][1]", nullPropertyPointer3.asPath());
    }

    @Test
    public void testEscape() {
        String string = "test'test\"test";
        String escapedString = NullPropertyPointer.escape(string);
        String expected = "test&apos;test&quot;test";
        assertEquals(expected, escapedString);
    }
}
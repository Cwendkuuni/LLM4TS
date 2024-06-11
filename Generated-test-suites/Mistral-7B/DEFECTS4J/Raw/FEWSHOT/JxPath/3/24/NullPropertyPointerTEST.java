package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);

        assertNull(nullPropertyPointer.createPath(context));
    }

    @Test
    public void testCreatePathWithPropertyOwner() {
        JXPathContext context = new JXPathContext(new Object());
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);

        NodePointer nodePointer = propertyOwnerPointer.createPath(context);
        assertNotNull(nodePointer);

        NodePointer result = nullPropertyPointer.createPath(context);
        assertEquals(nodePointer, result);
    }

    @Test
    public void testCreatePathWithNullPointer() {
        JXPathContext context = new JXPathContext(new Object());
        NullPointer nullPointer = new NullPointer(null, new QName("null"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);

        NodePointer result = nullPropertyPointer.createPath(context);
        assertNotNull(result);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parent = context.getDocument().getDocumentElement();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("value");
    }

    @Test
    public void testSetValueWithPropertyOwnerParent() {
        JXPathContext context = new JXPathContext(new Object());
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
        PropertyPointer propertyPointer = propertyOwnerPointer.getPropertyPointer();

        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setValue("value");

        nullPropertyPointer.setValue("newValue");

        assertEquals("newValue", propertyPointer.getValue());
    }

    @Test
    public void testCreatePathWithCreateChild() {
        JXPathContext context = new JXPathContext(new Object());
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);

        NodePointer nodePointer = propertyOwnerPointer.createPath(context);
        assertNotNull(nodePointer);

        NodePointer result = nullPropertyPointer.createChild(context, new QName("test"), 0);
        assertNotNull(result);
        assertEquals(nodePointer, result.getParent());
    }

    @Test
    public void testCreatePathWithCreateChildAndValue() {
        JXPathContext context = new JXPathContext(new Object());
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);

        NodePointer nodePointer = propertyOwnerPointer.createPath(context);
        assertNotNull(nodePointer);

        NodePointer result = nullPropertyPointer.createChild(context, new QName("test"), 0, "value");
        assertNotNull(result);
        assertEquals("value", result.getValue());
        assertEquals(nodePointer, result.getParent());
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("test");
        assertTrue(nullPropertyPointer.isByNameAttribute());
        assertEquals("test", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("/", nullPropertyPointer.asPath());

        NullPropertyPointer nullPropertyPointerWithAttribute = new NullPropertyPointer(null);
        nullPropertyPointerWithAttribute.setNameAttributeValue("test");
        assertEquals("/@test", nullPropertyPointerWithAttribute.asPath());

        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new Object());
        NullPropertyPointer nullPropertyPointerWithParent = new NullPropertyPointer(propertyOwnerPointer);
        assertEquals("/", nullPropertyPointerWithParent.asPath());

        PropertyPointer propertyPointer = propertyOwnerPointer.getPropertyPointer();
        propertyPointer.setPropertyName("test");
        propertyPointer.setValue("value");

        NullPropertyPointer nullPropertyPointerWithChild = new NullPropertyPointer(propertyPointer);
        assertEquals("/test", nullPropertyPointerWithChild.asPath());
    }
}
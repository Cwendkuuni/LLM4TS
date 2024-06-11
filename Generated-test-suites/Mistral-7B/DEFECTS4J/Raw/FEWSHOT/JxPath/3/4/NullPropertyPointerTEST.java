package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NullPropertyPointerTest {

    @Test
    public void testCreatePathNullContext() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertThrows(NullPointerException.class, () -> nullPropertyPointer.createPath(null));
    }

    @Test
    public void testCreatePathNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertThrows(NullPointerException.class, () -> nullPropertyPointer.createPath(new JXPathContext()));
    }

    @Test
    public void testSetValueNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertThrows(NullPointerException.class, () -> nullPropertyPointer.setValue(new Object()));
    }

    @Test
    public void testCreatePathParentIsPropertyOwner() {
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
        NodePointer newParent = propertyOwnerPointer.createPath(new JXPathContext());
        assertEquals(propertyOwnerPointer, newParent);

        NodePointer propertyPointer = newParent.getPropertyPointer();
        NodePointer child = nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 0);
        assertEquals(child, newParent.createChild(new JXPathContext(), new QName("test"), 0));
    }

    @Test
    public void testCreatePathParentIsNullPointer() {
        NullPointer nullPointer = new NullPointer(null, new QName("test"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
        NodePointer newParent = nullPointer.createPath(new JXPathContext());

        NodePointer child = nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 0);
        assertEquals(child, newParent.createChild(new JXPathContext(), new QName("test"), 0));
    }

    @Test
    public void testCreatePathParentIsNotPropertyOwner() {
        NodePointer nodePointer = new DOMNodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);

        NodePointer newParent = nodePointer.createPath(new JXPathContext());
        assertNull(newParent.createChild(new JXPathContext(), new QName("test"), 0));
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
        assertEquals("test", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
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
        assertEquals("", nullPropertyPointer.asPath());

        NullPropertyPointer nullPropertyPointerWithNameAttribute =
                new NullPropertyPointer(null);
        nullPropertyPointerWithNameAttribute.setNameAttributeValue("test");
        assertEquals("[@name='test']", nullPropertyPointerWithNameAttribute.asPath());
    }

    @Test
    public void testEscape() {
        assertEquals("", NullPropertyPointer.escape(""));
        assertEquals("&apos;foo&apos;bar&quot;baz&quot;",
                NullPropertyPointer.escape("foo'bar\"baz"));
    }
}
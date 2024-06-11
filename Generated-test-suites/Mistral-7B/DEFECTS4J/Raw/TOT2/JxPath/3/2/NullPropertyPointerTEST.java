
package org.junit.test; // Replace with your test package

import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName result = nullPropertyPointer.getName();
        assertEquals("*", result.getName());
        assertEquals("*", result.getLocalPart());
        assertEquals("", result.getNamespaceURI());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("test");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonChangeableLocation() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("test");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathWithNullContext() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.createPath(null);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathWithNonChangeableLocation() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.createPath(null);
    }

    @Test
    public void testGetPropertyNameWithEmptyString() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("");
        assertEquals("", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyNameWithNullString() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName(null);
        assertNull(nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsContainerWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testIsLeafWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetBaseValueWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(null, nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNodeWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(null, nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsActualPropertyWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActualWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsCollectionWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCountWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testgetPropertyNamesWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] result = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testAsPathWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("", nullPropertyPointer.asPath());
    }

    @Test
    public void testEscapeWithNullString() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("", nullPropertyPointer.escape(null));
    }

    @Test
    public void testEscapeWithEmptyString() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("", nullPropertyPointer.escape(""));
    }

    @Test
    public void testEscapeWithSingleQuote() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("&apos;", nullPropertyPointer.escape("'"));
    }

    @Test
    public void testEscapeWithDoubleQuote() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("&quot;", nullPropertyPointer.escape("\""));
    }
}

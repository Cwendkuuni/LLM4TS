
package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    private NullPropertyPointer nullPropertyPointer;

    @Before
    public void setUp() {
        NodePointer parent = new NodePointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
    }

    @Test
    public void testGetName() {
        QName name = nullPropertyPointer.getName();
        assertEquals("*", name.toXPath());
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(5);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetLength() {
        int length = nullPropertyPointer.getLength();
        assertEquals(0, length);
    }

    @Test
    public void testGetBaseValue() {
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NodePointer valuePointer = nullPropertyPointer.getValuePointer();
        assertEquals("null:*", valuePointer.asPath());
    }

    @Test
    public void testIsActualProperty() {
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        nullPropertyPointer.setValue("test");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithDynamicPropertyDeclarationSupported() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        parent.setDynamicPropertyDeclarationSupported(true);
        nullPropertyPointer.setParent(parent);
        nullPropertyPointer.setValue("test");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonChangeableLocation() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        parent.setDynamicPropertyDeclarationSupported(false);
        nullPropertyPointer.setParent(parent);
        nullPropertyPointer.setValue("test");
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer newPath = nullPropertyPointer.createPath(context);
        assertNotNull(newPath);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        Object value = "test";
        NodePointer newPath = nullPropertyPointer.createPath(context, value);
        assertNotNull(newPath);
        assertEquals("test", newPath.getNode());
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("child");
        int index = 0;
        NodePointer newChild = nullPropertyPointer.createChild(context, name, index);
        assertNotNull(newChild);
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("child");
        int index = 0;
        Object value = "test";
        NodePointer newChild = nullPropertyPointer.createChild(context, name, index, value);
        assertNotNull(newChild);
        assertEquals("test", newChild.getNode());
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = nullPropertyPointer.getPropertyName();
        assertEquals("*", propertyName);
    }

    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("child");
        String propertyName = nullPropertyPointer.getPropertyName();
        assertEquals("child", propertyName);
    }

    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("child");
        String propertyName = nullPropertyPointer.getPropertyName();
        assertEquals("child", propertyName);
    }

    @Test
    public void testIsCollection() {
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        int propertyCount = nullPropertyPointer.getPropertyCount();
        assertEquals(0, propertyCount);
    }

    @Test
    public void testGetPropertyNames() {
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertNotNull(propertyNames);
        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPathWithoutNameAttribute() {
        String path = nullPropertyPointer.asPath();
        assertEquals("null:*", path);
    }

    @Test
    public void testAsPathWithNameAttribute() {
        nullPropertyPointer.setNameAttributeValue("child");
        nullPropertyPointer.setPropertyIndex(5);
        String path = nullPropertyPointer.asPath();
        assertEquals("null:*[@name='child'][6]", path);
    }

    @Test
    public void testAsPathWithEscapedQuotes() {
        nullPropertyPointer.setNameAttributeValue("child'\"name");
        nullPropertyPointer.setPropertyIndex(1);
        String path = nullPropertyPointer.asPath();
        assertEquals("null:*[@name='child&quot;&apos;name'][2]", path);
    }
}

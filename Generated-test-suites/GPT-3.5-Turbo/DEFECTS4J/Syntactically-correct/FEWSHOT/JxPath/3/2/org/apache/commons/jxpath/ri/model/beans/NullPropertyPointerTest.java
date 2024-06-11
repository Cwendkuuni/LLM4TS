
package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    private NullPropertyPointer nullPropertyPointer;
    private PropertyOwnerPointer parent;

    @Before
    public void setUp() {
        parent = new PropertyOwnerPointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
    }

    @Test
    public void testGetName() {
        assertEquals(new QName("*"), nullPropertyPointer.getName());
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(0);
        assertEquals(0, nullPropertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetLength() {
        assertEquals(0, nullPropertyPointer.getLength());
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
        assertNotNull(valuePointer);
        assertTrue(valuePointer instanceof NullPointer);
        assertEquals(new QName(nullPropertyPointer.getPropertyName()), valuePointer.getPropertyName());
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

    @Test
    public void testSetValue_NullParentContainer_ReturnJXPathInvalidAccessException() {
        try {
            nullPropertyPointer.setValue("test");
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            assertEquals("Cannot set property /, the target object is null", e.getMessage());
        }
    }

    @Test
    public void testSetValue_DynamicPropertyDeclarationSupported_ReturnSetValue() {
        parent.setDynamicPropertyDeclarationSupported(true);
        PropertyPointer propertyPointer = parent.getPropertyPointer();
        propertyPointer.setPropertyName("test");
        nullPropertyPointer.setValue("value");
        assertEquals("value", propertyPointer.getValue());
    }

    @Test
    public void testSetValue_PathDoesNotMatchChangeableLocation_ReturnJXPathInvalidAccessException() {
        try {
            parent.setDynamicPropertyDeclarationSupported(false);
            nullPropertyPointer.setValue("value");
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            assertEquals("Cannot set property /, path does not match a changeable location", e.getMessage());
        }
    }

    @Test
    public void testCreatePath_IsAttribute_ReturnAttributeNodePointer() {
        JXPathContext context = JXPathContext.newContext(null);
        NodePointer attributePointer = nullPropertyPointer.createPath(context);
        assertNotNull(attributePointer);
        assertTrue(attributePointer instanceof AttributeNodePointer);
        assertEquals(new QName(nullPropertyPointer.getPropertyName()), attributePointer.getName());
    }

    @Test
    public void testCreatePath_IsElement_ReturnElementNodePointer() {
        JXPathContext context = JXPathContext.newContext(null);
        parent.setPropertyPointer(nullPropertyPointer);
        NodePointer elementPointer = nullPropertyPointer.createPath(context);
        assertNotNull(elementPointer);
        assertTrue(elementPointer instanceof ElementNodePointer);
        assertEquals(new QName(nullPropertyPointer.getPropertyName()), elementPointer.getName());
    }

    @Test
    public void testCreatePathWithObject_IsAttribute_ReturnAttributeNodePointerWithValue() {
        JXPathContext context = JXPathContext.newContext(null);
        Object value = "test";
        NodePointer attributePointer = nullPropertyPointer.createPath(context, value);
        assertNotNull(attributePointer);
        assertTrue(attributePointer instanceof AttributeNodePointer);
        assertEquals(new QName(nullPropertyPointer.getPropertyName()), attributePointer.getName());
        assertEquals(value, attributePointer.getValue());
    }

    @Test
    public void testCreatePathWithObject_IsElement_ReturnElementNodePointerWithValue() {
        JXPathContext context = JXPathContext.newContext(null);
        Object value = "test";
        parent.setPropertyPointer(nullPropertyPointer);
        NodePointer elementPointer = nullPropertyPointer.createPath(context, value);
        assertNotNull(elementPointer);
        assertTrue(elementPointer instanceof ElementNodePointer);
        assertEquals(new QName(nullPropertyPointer.getPropertyName()), elementPointer.getName());
        assertEquals(value, elementPointer.getValue());
    }

    @Test
    public void testCreateChild_IsAttribute_ReturnAttributeNodePointer() {
        JXPathContext context = JXPathContext.newContext(null);
        QName name = new QName("test");
        int index = 0;
        NodePointer attributePointer = nullPropertyPointer.createChild(context, name, index);
        assertNotNull(attributePointer);
        assertTrue(attributePointer instanceof AttributeNodePointer);
        assertEquals(name, attributePointer.getName());
    }

    @Test
    public void testCreateChild_IsElement_ReturnElementNodePointer() {
        JXPathContext context = JXPathContext.newContext(null);
        QName name = new QName("test");
        int index = 0;
        parent.setPropertyPointer(nullPropertyPointer);
        NodePointer elementPointer = nullPropertyPointer.createChild(context, name, index);
        assertNotNull(elementPointer);
        assertTrue(elementPointer instanceof ElementNodePointer);
        assertEquals(name, elementPointer.getName());
    }

    @Test
    public void testCreateChildWithValue_IsAttribute_ReturnAttributeNodePointerWithValue() {
        JXPathContext context = JXPathContext.newContext(null);
        QName name = new QName("test");
        int index = 0;
        Object value = "value";
        NodePointer attributePointer = nullPropertyPointer.createChild(context, name, index, value);
        assertNotNull(attributePointer);
        assertTrue(attributePointer instanceof AttributeNodePointer);
        assertEquals(name, attributePointer.getName());
        assertEquals(value, attributePointer.getValue());
    }

    @Test
    public void testCreateChildWithValue_IsElement_ReturnElementNodePointerWithValue() {
        JXPathContext context = JXPathContext.newContext(null);
        QName name = new QName("test");
        int index = 0;
        Object value = "value";
        parent.setPropertyPointer(nullPropertyPointer);
        NodePointer elementPointer = nullPropertyPointer.createChild(context, name, index, value);
        assertNotNull(elementPointer);
        assertTrue(elementPointer instanceof ElementNodePointer);
        assertEquals(name, elementPointer.getName());
        assertEquals(value, elementPointer.getValue());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection_WholeCollection_ReturnFalse() {
        nullPropertyPointer.setPropertyIndex(-1);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testIsCollection_NotWholeCollection_ReturnTrue() {
        nullPropertyPointer.setPropertyIndex(0);
        assertTrue(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertNotNull(propertyNames);
        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPath_NotByNameAttribute_ReturnPath() {
        assertEquals("/", nullPropertyPointer.asPath());
    }

    @Test
    public void testAsPath_ByNameAttribute_ReturnPath() {
        nullPropertyPointer.setNameAttributeValue("test");
        assertEquals("/[@name='test']", nullPropertyPointer.asPath());
    }

    @Test
    public void testAsPath_ByNameAttributeWithIndex_ReturnPath() {
        nullPropertyPointer.setNameAttributeValue("test");
        nullPropertyPointer.setPropertyIndex(0);
        assertEquals("/[@name='test'][1]", nullPropertyPointer.asPath());
    }
}

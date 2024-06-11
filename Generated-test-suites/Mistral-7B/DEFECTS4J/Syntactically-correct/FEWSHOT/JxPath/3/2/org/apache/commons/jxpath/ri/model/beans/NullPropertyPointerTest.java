package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getParent());
        assertNull(nullPropertyPointer.getValuePointer());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertFalse(nullPropertyPointer.isActual());
        assertFalse(nullPropertyPointer.isContainer());
        assertEquals("", nullPropertyPointer.asPath());

        NullPropertyPointer nullPropertyPointerWithParent = new NullPropertyPointer(new PropertyOwnerPointer(new NodePointer(null)));
        assertNotNull(nullPropertyPointerWithParent.getParent());
        assertNull(nullPropertyPointerWithParent.getValuePointer());
        assertFalse(nullPropertyPointerWithParent.isActualProperty());
        assertFalse(nullPropertyPointerWithParent.isActual());
        assertTrue(nullPropertyPointerWithParent.isContainer());
        assertEquals("", nullPropertyPointerWithParent.asPath());

        NullPropertyPointer nullPropertyPointerWithParentAndName = new NullPropertyPointer(new PropertyOwnerPointer(new NodePointer(null, new QName("test"))));
        assertNotNull(nullPropertyPointerWithParentAndName.getParent());
        assertNull(nullPropertyPointerWithParentAndName.getValuePointer());
        assertFalse(nullPropertyPointerWithParentAndName.isActualProperty());
        assertFalse(nullPropertyPointerWithParentAndName.isActual());
        assertTrue(nullPropertyPointerWithParentAndName.isContainer());
        assertEquals("test", nullPropertyPointerWithParentAndName.asPath());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer(new NodePointer(null)));
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
        assertEquals("test", nullPropertyPointer.asPath());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer(new NodePointer(null)));
        nullPropertyPointer.setNameAttributeValue("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
        assertEquals("[@name='test']", nullPropertyPointer.asPath());
    }

    @Test
    public void testCreatePath() {
        NodePointer nodePointer = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
        JXPathContext context = new StaticContext();

        NodePointer createdPath = nullPropertyPointer.createPath(context);
        assertNull(createdPath);

        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(nodePointer);
        NullPropertyPointer nullPropertyPointerWithParent = new NullPropertyPointer(propertyOwnerPointer);
        createdPath = nullPropertyPointerWithParent.createPath(context);
        assertNotNull(createdPath);
        assertEquals(propertyOwnerPointer.getClass(), createdPath.getClass());

        NodePointer parent = new DomNodePointer(context, null);
        PropertyOwnerPointer propertyOwnerPointerWithParent = new PropertyOwnerPointer(parent);
        NullPropertyPointer nullPropertyPointerWithParentAndProperty = new NullPropertyPointer(propertyOwnerPointerWithParent);
        createdPath = nullPropertyPointerWithParentAndProperty.createPath(context);
        assertNotNull(createdPath);
        assertFalse(createdPath instanceof PropertyOwnerPointer);
        assertFalse(createdPath instanceof DomNodePointer);
        assertEquals(NodePointer.class, createdPath.getClass());
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer nodePointer = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
        JXPathContext context = new StaticContext();

        NodePointer createdPathWithValue = nullPropertyPointer.createPath(context, new Object());
        assertNull(createdPathWithValue);

        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(nodePointer);
        NullPropertyPointer nullPropertyPointerWithParent = new NullPropertyPointer(propertyOwnerPointer);
        createdPathWithValue = nullPropertyPointerWithParent.createPath(context, new Object());
        assertNotNull(createdPathWithValue);
        assertFalse(createdPathWithValue instanceof PropertyOwnerPointer);
        assertFalse(createdPathWithValue instanceof DomNodePointer);
        assertEquals(NodePointer.class, createdPathWithValue.getClass());
    }

    @Test
    public void testCreateChild() {
        NodePointer nodePointer = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
        JXPathContext context = new StaticContext();
        QName name = new QName("test");

        NodePointer createdChild = nullPropertyPointer.createChild(context, name, 0);
        assertNull(createdChild);

        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(nodePointer);
        NullPropertyPointer nullPropertyPointerWithParent = new NullPropertyPointer(propertyOwnerPointer);
        createdChild = nullPropertyPointerWithParent.createChild(context, name, 0);
        assertNotNull(createdChild);
        assertFalse(createdChild instanceof PropertyOwnerPointer);
        assertFalse(createdChild instanceof DomNodePointer);
        assertEquals(NodePointer.class, createdChild.getClass());

        DomNodePointer domNodePointer = new DomNodePointer(context, null);
        PropertyOwnerPointer propertyOwnerPointerWithDomNode = new PropertyOwnerPointer(domNodePointer);
        NullPropertyPointer nullPropertyPointerWithParentAndDomNode = new NullPropertyPointer(propertyOwnerPointerWithDomNode);
        createdChild = nullPropertyPointerWithParentAndDomNode.createChild(context, name, 0);
        assertNotNull(createdChild);
        assertInstanceOf(DomNodePointer.class, createdChild);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer nodePointer = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
        JXPathContext context = new StaticContext();
        QName name = new QName("test");
        Object value = new Object();

        NodePointer createdChildWithValue = nullPropertyPointer.createChild(context, name, 0, value);
        assertNull(createdChildWithValue);

        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(nodePointer);
        NullPropertyPointer nullPropertyPointerWithParent = new NullPropertyPointer(propertyOwnerPointer);
        createdChildWithValue = nullPropertyPointerWithParent.createChild(context, name, 0, value);
        assertNotNull(createdChildWithValue);
        assertFalse(createdChildWithValue instanceof PropertyOwnerPointer);
        assertFalse(createdChildWithValue instanceof DomNodePointer);
        assertEquals(NodePointer.class, createdChildWithValue.getClass());

        DomNodePointer domNodePointer = new DomNodePointer(context, null);
        PropertyOwnerPointer propertyOwnerPointerWithDomNode = new PropertyOwnerPointer(domNodePointer);
        NullPropertyPointer nullPropertyPointerWithParentAndDomNode = new NullPropertyPointer(propertyOwnerPointerWithDomNode);
        createdChildWithValue = nullPropertyPointerWithParentAndDomNode.createChild(context, name, 0, value);
        assertNotNull(createdChildWithValue);
        assertInstanceOf(DomNodePointer.class, createdChildWithValue);
        assertEquals(value, createdChildWithValue.getValue());
    }

}
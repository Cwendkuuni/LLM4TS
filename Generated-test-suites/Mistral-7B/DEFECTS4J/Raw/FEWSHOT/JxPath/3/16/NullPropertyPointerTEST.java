package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NullPropertyPointerTest {

    @Test
    public void testCreateNullPropertyPointer() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNull(pointer.getValuePointer());
        assertEquals("", pointer.getPropertyName());
        assertFalse(pointer.isActual());
        assertFalse(pointer.isActualProperty());
        assertFalse(pointer.isContainer());
        assertFalse(pointer.isLeaf());
        assertEquals(0, pointer.getLength());
        assertNull(pointer.getImmediateNode());
        assertNull(pointer.getBaseValue());
    }

    @Test
    public void testCreateNullPropertyPointerWithNameAttribute() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setNameAttributeValue("name");
        assertEquals("name", pointer.getPropertyName());
        assertTrue(pointer.isAttribute());
        assertFalse(pointer.isActual());
        assertFalse(pointer.isActualProperty());
        assertFalse(pointer.isContainer());
        assertFalse(pointer.isLeaf());
        assertEquals(0, pointer.getLength());
        assertNull(pointer.getImmediateNode());
        assertNull(pointer.getBaseValue());
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = "propertyName";
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyName(propertyName);
        assertEquals(propertyName, pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        String propertyName = "propertyName";
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyName(propertyName);
        assertEquals(propertyName, pointer.getPropertyName());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNullParent() {
        NodePointer parent = null;
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        String value = "value";
        pointer.setValue(value);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueParentIsContainer() {
        NodePointer parent = new NodePointer(null, new QName("parent"), null, new Object[0]);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        String value = "value";
        pointer.setValue(value);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueParentIsDynamicPropertyOwner() {
        NodePointer parent = new DynamicPropertyOwnerPointer(new NodePointer(null), new QName("parent"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        String value = "value";
        pointer.setValue(value);
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new StaticContext();
        NodePointer result = pointer.createPath(context);
        assertNull(result);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new StaticContext();
        Object value = new Object();
        NodePointer result = pointer.createPath(context, value);
        assertNull(result);
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new StaticContext();
        QName name = new QName("name");
        NodePointer result = pointer.createChild(context, name);
        assertNull(result);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new StaticContext();
        QName name = new QName("name");
        Object value = new Object();
        NodePointer result = pointer.createChild(context, name, 1, value);
        assertNull(result);
    }

    @Test
    public void testAsPath() {
        String propertyName = "propertyName";
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyName(propertyName);
        JXPathContext context = new StaticContext();
        String result = pointer.asPath();
        assertEquals("", result);
        pointer.setNameAttributeValue("name");
        result = pointer.asPath();
        assertEquals("", result);
    }

    @Test
    public void testEscape() {
        String input = "foo'bar\"qux";
        String expected = "foo&apos;bar&quot;qux";
        String result = new NullPropertyPointer().escape(input);
        assertEquals(expected, result);
    }
}

class StaticContext implements JXPathContext {

    @Override
    public Object get(NodePointer pointer) {
        return null;
    }

    @Override
    public NodePointer createChild(NodePointer pointer, QName name, int index) {
        return new NodePointer(null);
    }

    @Override
    public NodePointer createChild(NodePointer pointer, QName name, int index, Object value) {
        return new NodePointer(null);
    }

    @Override
    public NodePointer createPath(NodePointer pointer) {
        return new NodePointer(null);
    }

    @Override
    public NodePointer createPath(NodePointer pointer, Object value) {
        return new NodePointer(null);
    }

    @Override
    public Object callMethod(NodePointer pointer, String name, Object[] arguments) {
        return null;
    }

    @Override
    public Object callMethod(NodePointer pointer, String name, Object[] arguments, QName methodSignature) {
        return null;
    }

    @Override
    public void setProperty(NodePointer pointer, String propertyName, Object propertyValue) {
    }

    @Override
    public void removeProperty(NodePointer pointer, String propertyName) {
    }

    @Override
    public void addProperty(NodePointer pointer, String propertyName, Object propertyValue) {
    }

    @Override
    public void removeNode(NodePointer pointer) {
    }

    @Override
    public void addNode(NodePointer pointer, NodePointer newNode) {
    }

    @Override
    public void removeNodes(NodePointer pointer, NodePointer endPointer) {
    }

    @Override
    public void addNodes(NodePointer pointer, NodePointer newNode) {
    }

    @Override
    public int getPropertyCount(NodePointer pointer) {
        return 0;
    }

    @Override
    public String[] getPropertyNames(NodePointer pointer) {
        return new String[0];
    }

    @Override
    public Object getPropertyValue(NodePointer pointer, String propertyName) {
        return null;
    }

    @Override
    public NodePointer getPropertyPointer(NodePointer pointer, String propertyName) {
        return new NodePointer(null);
    }

    @Override
    public NodePointer getIndexedPropertyPointer(NodePointer pointer, int index) {
        return new NodePointer(null);
    }

    @Override
    public NodePointer getAttributePointer(NodePointer pointer, String attributeName) {
        return new NodePointer(null);
    }

    @Override
    public NodePointer createAttribute(NodePointer pointer, QName attributeName) {
        return new NodePointer(null);
    }

    @Override
    public NodePointer createAttribute(NodePointer pointer, QName attributeName, Object attributeValue) {
        return new NodePointer(null);
    }

    @Override
    public NodePointer createAttribute(NodePointer pointer, String attributeName, Object attributeValue) {
        return new NodePointer(null);
    }

    @Override
    public Object getAttributeValue(NodePointer pointer, String attributeName) {
        return null;
    }

    @Override
    public void setAttributeValue(NodePointer pointer, String attributeName, Object attributeValue) {
    }

    @Override
    public void removeAttribute(NodePointer pointer, String attributeName) {
    }
}
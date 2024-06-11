package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;
    private NodePointer parentPointer;
    private Object mockBean;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parentPointer = NodePointer.newChildNodePointer(null, null, new Object());
        mockBean = new Object() {
            public String getProperty1() { return "property1"; }
            public String[] getPropertyArray() { return new String[]{"property1", "property2"}; }
            public Object getProperty2() { return new Object(); }
        };
        propertyPointer = new TestPropertyPointer(parentPointer);
        propertyPointer.setBean(mockBean);
    }

    @Test
    public void testGetPropertyIndex() {
        assertEquals(-1, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(0);
        assertEquals(0, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        assertEquals(mockBean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        assertEquals("", propertyPointer.getName().getLocalPart());
        propertyPointer.setPropertyName("property1");
        assertEquals("property1", propertyPointer.getName().getLocalPart());
    }

    @Test
    public void testGetPropertyCount() {
        propertyPointer = new TestPropertyPointer(parentPointer);
        assertEquals(-1, propertyPointer.getPropertyCount());

        propertyPointer = new TestStringPropertyPointer(parentPointer);
        assertEquals(2, propertyPointer.getPropertyCount());

        propertyPointer = new TestObjectPropertyPointer(parentPointer);
        assertEquals(1, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        propertyPointer = new TestStringPropertyPointer(parentPointer);
        assertArrayEquals(new String[]{"property1", "property2"}, propertyPointer.getPropertyNames());

        propertyPointer = new TestObjectPropertyPointer(parentPointer);
        assertArrayEquals(new String[]{"property"}, propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        propertyPointer = new TestStringPropertyPointer(parentPointer);
        assertTrue(propertyPointer.isActualProperty());

        propertyPointer = new TestObjectPropertyPointer(parentPointer);
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        propertyPointer = new TestStringPropertyPointer(parentPointer);
        assertEquals("property1", propertyPointer.getImmediateNode());

        propertyPointer.setPropertyIndex(1);
        assertEquals("property2", propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        propertyPointer = new TestStringPropertyPointer(parentPointer);
        assertFalse(propertyPointer.isCollection());

        propertyPointer = new TestObjectPropertyPointer(parentPointer);
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testGetLength() {
        propertyPointer = new TestStringPropertyPointer(parentPointer);
        assertEquals(0, propertyPointer.getLength());

        propertyPointer.setBean(new String[]{"property1", "property2", "property3"});
        assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testCreatePath() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        propertyPointer.setPropertyName("property1");
        propertyPointer.setPropertyIndex(0);

        Object result = propertyPointer.createPath(new JXPathContext(new Object())).getNode();
        assertEquals("property1", result);

        propertyPointer.setPropertyName("property2");
        propertyPointer.setPropertyIndex(1);

        result = propertyPointer.createPath(new JXPathContext(new Object())).getNode();
        assertEquals("property2", result);
    }

    @Test
    public void testCreatePathWithValue() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        propertyPointer.setPropertyName("property1");
        propertyPointer.setPropertyIndex(0);

        propertyPointer.createPath(new JXPathContext(new Object()), "value");
        assertEquals("value", propertyPointer.getNode().toString());
    }

    @Test
    public void testGetImmediateValuePointer() {
        Object result = propertyPointer.getImmediateValuePointer().getNode();
        assertNull(result);

        propertyPointer.setPropertyName("property1");
        assertEquals("property1", propertyPointer.getImmediateValuePointer().getNode().toString());
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer1 = new TestStringPropertyPointer(parentPointer);
        PropertyPointer propertyPointer2 = new TestStringPropertyPointer(parentPointer);
        PropertyPointer propertyPointer3 = new TestObjectPropertyPointer(parentPointer);

        assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());
        assertEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
        assertNotSame(propertyPointer1.hashCode(), propertyPointer3.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new TestStringPropertyPointer(parentPointer);
        PropertyPointer propertyPointer2 = new TestStringPropertyPointer(parentPointer);
        PropertyPointer propertyPointer3 = new TestObjectPropertyPointer(parentPointer);

        assertTrue(propertyPointer1.equals(propertyPointer1));
        assertTrue(propertyPointer1.equals(propertyPointer2));
        assertFalse(propertyPointer1.equals(propertyPointer3));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer nodePointer1 = NodePointer.newChildNodePointer(null, null, new Object());
        NodePointer nodePointer2 = NodePointer.newChildNodePointer(null, null, new Object());
        NodePointer nodePointer3 = NodePointer.newChildNodePointer(null, null, new Object());

        NodePointer propertyPointer1 = new TestStringPropertyPointer(nodePointer1);
        NodePointer propertyPointer2 = new TestStringPropertyPointer(nodePointer2);
        NodePointer propertyPointer3 = new TestStringPropertyPointer(nodePointer3);

        assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer3));

        NodePointer objectPointer1 = new TestObjectPropertyPointer(nodePointer1);
        NodePointer objectPointer2 = new TestObjectPropertyPointer(nodePointer2);
        NodePointer objectPointer3 = new TestStringPropertyPointer(nodePointer3);

        assertEquals(-1, propertyPointer1.compareChildNodePointers(objectPointer2, propertyPointer3));
        assertEquals(1, propertyPointer1.compareChildNodePointers(propertyPointer2, objectPointer2));
    }

    private static class TestPropertyPointer extends PropertyPointer {
        public TestPropertyPointer(NodePointer parent) { super(parent); }
        public String getPropertyName() { return "property"; }
    }

    private static class TestStringPropertyPointer extends PropertyPointer {
        public TestStringPropertyPointer(NodePointer parent) { super(parent); }
        public String getPropertyName() { return "property"; }
    }

    private static class TestObjectPropertyPointer extends PropertyPointer {
        public TestObjectPropertyPointer(NodePointer parent) { super(parent); }
        public String getPropertyName() { return "property"; }
        public Object getBaseValue() { return mockBean; }
    }
}
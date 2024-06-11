package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {
    private PropertyPointer propertyPointer;
    private NodePointer parentPointer;

    @Before
    public void setUp() {
        parentPointer = mock(NodePointer.class);
        propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.getImmediateParentPointer()).thenReturn(parentPointer);
        when(propertyPointer.getPropertyName()).thenReturn("property");
        when(propertyPointer.getPropertyIndex()).thenReturn(1);
        when(propertyPointer.getIndex()).thenReturn(0);
        when(propertyPointer.getBaseValue()).thenReturn("value");
        when(propertyPointer.getImmediateNode()).thenReturn("value");
        when(propertyPointer.isCollection()).thenReturn(false);
        when(propertyPointer.getLength()).thenReturn(1);
    }

    @Test
    public void testGetPropertyIndex() {
        int propertyIndex = propertyPointer.getPropertyIndex();
        assertEquals(1, propertyIndex);
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(2);
        verify(propertyPointer, times(1)).setIndex(PropertyPointer.WHOLE_COLLECTION);
    }

    @Test
    public void testGetBean() {
        Object bean = propertyPointer.getBean();
        assertEquals("value", bean);
    }

    @Test
    public void testGetName() {
        QName name = propertyPointer.getName();
        assertEquals("property", name.getLocalPart());
        assertNull(name.getNamespaceURI());
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = propertyPointer.getPropertyName();
        assertEquals("property", propertyName);
    }

    @Test
    public void testSetPropertyName() {
        propertyPointer.setPropertyName("newProperty");
        verify(propertyPointer, times(1)).setPropertyName("newProperty");
    }

    @Test
    public void testGetPropertyCount() {
        int propertyCount = propertyPointer.getPropertyCount();
        assertEquals(1, propertyCount);
    }

    @Test
    public void testGetPropertyNames() {
        String[] propertyNames = propertyPointer.getPropertyNames();
        assertEquals(1, propertyNames.length);
        assertEquals("property", propertyNames[0]);
    }

    @Test
    public void testIsActualProperty() {
        boolean isActualProperty = propertyPointer.isActualProperty();
        assertTrue(isActualProperty);
    }

    @Test
    public void testGetValuePointer() {
        // Create a new JXPathContext
        JXPathContext context = JXPathContext.newContext(propertyPointer);

        // Get the value pointer
        PropertyPointer valuePointer = propertyPointer.getValuePointer(context);

        // Verify that the value pointer is not null
        assertNotNull(valuePointer);

        // Verify that the value pointer is of type NodePointer
        assertTrue(valuePointer instanceof NodePointer);
    }

    @Test
    public void testCreatePath() {
        // Create a new JXPathContext
        JXPathContext context = JXPathContext.newContext(propertyPointer);

        // Create the path
        NodePointer createdPath = propertyPointer.createPath(context);

        // Verify that the created path is not null
        assertNotNull(createdPath);
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePath_FactoryException() {
        // Create a new JXPathContext
        JXPathContext context = JXPathContext.newContext(propertyPointer);

        // Mock the AbstractFactory
        AbstractFactory factory = mock(AbstractFactory.class);
        when(factory.createObject(context, propertyPointer, propertyPointer.getBean(), propertyPointer.getPropertyName(), 0)).thenReturn(false);

        // Create the path
        propertyPointer.createPath(context);
    }

    @Test
    public void testCreatePathWithValue() {
        // Create a new JXPathContext
        JXPathContext context = JXPathContext.newContext(propertyPointer);

        // Create the path with a value
        NodePointer createdPath = propertyPointer.createPath(context, "newValue");

        // Verify that the created path is not null
        assertNotNull(createdPath);

        // Verify that the created path has the new value
        assertEquals("newValue", createdPath.getImmediateNode());
    }

    @Test
    public void testHashcode() {
        int hashCode = propertyPointer.hashCode();
        assertEquals(1, hashCode);
    }

    @Test
    public void testEquals() {
        PropertyPointer other = mock(PropertyPointer.class);
        when(other.getImmediateParentPointer()).thenReturn(parentPointer);
        when(other.getPropertyName()).thenReturn("property");
        when(other.getPropertyIndex()).thenReturn(1);
        when(other.getIndex()).thenReturn(0);

        boolean isEqual = propertyPointer.equals(other);
        assertTrue(isEqual);
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = mock(NodePointer.class);
        NodePointer pointer2 = mock(NodePointer.class);

        // Mock the getValuePointer method of PropertyPointer
        PropertyPointer valuePointer = mock(PropertyPointer.class);
        when(propertyPointer.getValuePointer(any(JXPathContext.class))).thenReturn(valuePointer);
        when(valuePointer.compareChildNodePointers(pointer1, pointer2)).thenReturn(0);

        // Compare the child node pointers
        int result = propertyPointer.compareChildNodePointers(pointer1, pointer2);
        assertEquals(0, result);
    }
}
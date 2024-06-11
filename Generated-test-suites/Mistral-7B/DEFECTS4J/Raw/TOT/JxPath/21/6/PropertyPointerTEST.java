package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointerFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointer() {
        PropertyPointer parentPointer = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        Object parent = parentPointer.getNode();

        propertyPointer.setPropertyIndex(0);
        assertFalse(propertyPointer.isActual());
        assertNull(propertyPointer.getPropertyName());
        assertEquals(-1, propertyPointer.getLength());
        assertSame(parent, propertyPointer.getBean());
        assertNull(propertyPointer.getNode());
        assertNull(propertyPointer.getValue());
        assertNull(propertyPointer.getImmediateNode());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());

        propertyPointer.setPropertyName("testProperty");
        assertNull(propertyPointer.getPropertyIndex());
        assertEquals("testProperty", propertyPointer.getPropertyName());
        assertNull(propertyPointer.getPropertyCount());
        assertNull(propertyPointer.getPropertyNames());
        assertEquals(1, propertyPointer.getLength());
        assertSame(parent, propertyPointer.getBean());
        assertNull(propertyPointer.getNode());
        assertNull(propertyPointer.getValue());
        assertNull(propertyPointer.getImmediateNode());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());

        Object testObject = new Object() {
            int testProperty = 42;
        };
        propertyPointer.setBean(testObject);
        assertEquals(testObject, propertyPointer.getBean());
        assertNull(propertyPointer.getNode());
        assertNull(propertyPointer.getValue());
        assertNull(propertyPointer.getImmediateNode());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());

        JXPathContext context = new JXPathContext(testObject);
        propertyPointer.createPath(context);
        assertSame(testObject, propertyPointer.getNode());
        assertSame(testObject.getClass().getDeclaredField("testProperty"), propertyPointer.getValueField());
        assertEquals(42, ((Number) propertyPointer.getValue()).intValue());
        assertSame(propertyPointer, propertyPointer.getImmediateValuePointer().getParent());

        JXPathContext contextWithIndex = new JXPathContext(new Object[2][], null);
        propertyPointer.createPath(contextWithIndex, 0);
        assertSame(testObject, propertyPointer.getNode());
        assertSame(testObject.getClass().getDeclaredField("testProperty"), propertyPointer.getValueField());
        assertEquals(42, ((Number) propertyPointer.getValue()).intValue());
        assertEquals(0, propertyPointer.getIndex());
        assertSame(propertyPointer, propertyPointer.getImmediateValuePointer().getParent());

        propertyPointer.setIndex(1);
        assertSame(propertyPointer, propertyPointer.getImmediateValuePointer().getParent());
        assertNull(propertyPointer.getNode());
        assertNull(propertyPointer.getValue());
        assertNull(propertyPointer.getImmediateNode());
        assertTrue(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertEquals(2, propertyPointer.getLength());

        Object[] testArray = new Object[2];
        testArray[0] = new Object() {
            int testProperty1 = 1;
        };
        testArray[1] = new Object() {
            int testProperty2 = 2;
        };
        propertyPointer.setBaseValue(testArray);
        assertSame(testArray, propertyPointer.getBaseValue());
        assertSame(testArray[0], propertyPointer.getNode());
        assertSame(testArray[0].getClass().getDeclaredField("testProperty1"), propertyPointer.getValueField());
        assertEquals(1, ((Number) propertyPointer.getValue()).intValue());
        assertSame(propertyPointer, propertyPointer.getImmediateValuePointer().getParent());

        propertyPointer.next();
        assertSame(testArray[1], propertyPointer.getNode());
        assertSame(testArray[1].getClass().getDeclaredField("testProperty2"), propertyPointer.getValueField());
        assertEquals(2, ((Number) propertyPointer.getValue()).intValue());
        assertSame(propertyPointer, propertyPointer.getImmediateValuePointer().getParent());

        propertyPointer.next();
        assertNull(propertyPointer.getNode());
        assertNull(propertyPointer.getValue());
        assertNull(propertyPointer.getImmediateNode());
        assertTrue(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());

        PropertyPointerFactory factory = new PropertyPointerFactory();
        PropertyPointer propertyPointer2 = factory.createPropertyPointer(contextWithIndex, 0, "testProperty", testObject.getClass());
        assertSame(testObject, propertyPointer2.getBean());
        assertSame(testObject.getClass().getDeclaredField("testProperty"), propertyPointer2.getValueField());
        assertEquals(42, ((Number) propertyPointer2.getValue()).intValue());
        assertSame(propertyPointer2, propertyPointer2.getImmediateValuePointer().getParent());

        propertyPointer2.next();
        assertNull(propertyPointer2.getNode());
        assertNull(propertyPointer2.getValue());
        assertNull(propertyPointer2.getImmediateNode());
        assertTrue(propertyPointer2.isCollection());
        assertFalse(propertyPointer2.isLeaf());
    }
}
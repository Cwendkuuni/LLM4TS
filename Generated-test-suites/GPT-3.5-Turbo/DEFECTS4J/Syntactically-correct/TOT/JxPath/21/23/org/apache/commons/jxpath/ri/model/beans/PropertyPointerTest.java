import static org.junit.Assert.*;

import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathIntrospector;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {

    private JXPathContext context;
    private PropertyPointerTestObject testObject;

    @Before
    public void setUp() {
        context = JXPathContext.newContext(project);
        testObject = new PropertyPointerTestObject();
        context.getVariables().declareVariable("testObject", testObject);
    }

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, pointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        pointer.setPropertyIndex(3);
        assertEquals(3, pointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        assertEquals(testObject, pointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        assertEquals("propertyName", pointer.getName().getName());
    }

    @Test
    public void testGetPropertyName() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        assertEquals("propertyName", pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        pointer.setPropertyName("newPropertyName");
        assertEquals("newPropertyName", pointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        assertEquals(1, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        String[] expected = {"propertyName"};
        assertArrayEquals(expected, pointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        assertTrue(pointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        assertTrue(pointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        assertEquals("propertyValue", pointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        assertFalse(pointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        assertTrue(pointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        assertEquals(-1, pointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        assertEquals("propertyValue", pointer.getImmediateValuePointer().getValue());
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePath() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        pointer.createPath(context);
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        pointer.createPath(context, "newValue");
        assertEquals("newValue", pointer.getImmediateValuePointer().getValue());
    }

    @Test
    public void testCreateChildWithValue() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        pointer.createChild(context, JXPathIntrospector.createQName("childName"), 0, "childValue");
        assertEquals("childValue", pointer.getImmediateValuePointer().getValue());
    }

    @Test
    public void testCreateChildWithoutValue() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        pointer.createChild(context, JXPathIntrospector.createQName("childName"), 0);
        assertNull(pointer.getImmediateValuePointer().getValue());
    }

    @Test
    public void testHashCode() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        int expectedHashCode = pointer.getImmediateParentPointer().hashCode() + pointer.getPropertyIndex() + pointer.getIndex();
        assertEquals(expectedHashCode, pointer.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer pointer1 = (PropertyPointer)context.getValue("testObject");
        PropertyPointer pointer2 = (PropertyPointer)context.getValue("testObject");
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer pointer = (PropertyPointer)context.getValue("testObject");
        PropertyPointer pointer1 = (PropertyPointer)context.getValue("testObject");
        PropertyPointer pointer2 = (PropertyPointer)context.getValue("testObject");
        int result = pointer.compareChildNodePointers(pointer1, pointer2);
        assertEquals(0, result);
    }
}
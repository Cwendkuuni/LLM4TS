import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getValuePointer());
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testGetNameMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        nullPropertyPointer.setPropertyName("testProperty");
        QName expectedName = new QName("testProperty");
        QName actualName = nullPropertyPointer.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testSetPropertyNameMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        nullPropertyPointer.setPropertyName("testProperty");
        String expectedPropertyName = "testProperty";
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        assertEquals(expectedPropertyName, actualPropertyName);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueMethodWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Object value = new Object();
        nullPropertyPointer.setValue(value);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueMethodWithNonContainerParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        Object value = new Object();
        nullPropertyPointer.setValue(value);
    }

    @Test
    public void testSetValueMethodWithSupportedParent() {
        NodePointer propertyOwnerParent = new NodePointer(new PropertyOwnerClass());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerParent);
        Object value = new Object();
        nullPropertyPointer.setValue(value);
        // Verify that the value was set on the property owner
        Object propertyValue = propertyOwnerParent.getValue();
        assertEquals(value, propertyValue);
    }

    @Test
    public void testCreatePathMethodWithNoValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        JXPathContext context = new JXPathContext(new Object());
        NodePointer actualPath = nullPropertyPointer.createPath(context);
        NodePointer expectedPath = new NodePointer(context.getCurrentNode());
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testCreatePathMethodWithValue() {
        NodePointer propertyOwnerParent = new NodePointer(new PropertyOwnerClass());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerParent);
        Object value = new Object();
        nullPropertyPointer.setValue(value);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer actualPath = nullPropertyPointer.createPath(context, value);
        NodePointer expectedPath = new NodePointer(context.getCurrentNode(), value);
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testCreateChildMethodWithNoValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        JXPathContext context = new JXPathContext(new Object());
        NodePointer actualChild = nullPropertyPointer.createChild(context, new QName("testChild"));
        NodePointer expectedChild = new NullPointer(nullPropertyPointer, new QName("testChild"));
        assertEquals(expectedChild, actualChild);
    }

    @Test
    public void testCreateChildMethodWithValue() {
        NodePointer propertyOwnerParent = new NodePointer(new PropertyOwnerClass());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerParent);
        NodePointer expectedChild = new NodePointer(propertyOwnerParent, new QName("testChild"));
        NodePointer actualChild = nullPropertyPointer.createChild(
            new JXPathContext(new Object()), new QName("testChild"), 0, null);
        assertEquals(expectedChild, actualChild);
    }

    @Test
    public void testIsActualPropertyMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActualMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainerMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testGetLengthMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        int actualLength = nullPropertyPointer.getLength();
        int expectedLength = 0;
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetBaseValueMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        Object actualBaseValue = nullPropertyPointer.getBaseValue();
        assertNull(actualBaseValue);
    }

    @Test
    public void testGetImmediateNodeMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        NodePointer actualImmediateNode = nullPropertyPointer.getImmediateNode();
        assertNull(actualImmediateNode);
    }

    @Test
    public void testIsLeafMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointerMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        NodePointer expectedValuePointer = new NullPointer(nullPropertyPointer, new QName("*"));
        NodePointer actualValuePointer = nullPropertyPointer.getValuePointer();
        assertEquals(expectedValuePointer, actualValuePointer);
    }

    @Test
    public void testAsPathMethodWithoutByNameAttribute() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        String expectedPath = "/";
        String actualPath = nullPropertyPointer.asPath();
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testAsPathMethodWithByNameAttribute() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        nullPropertyPointer.setNameAttributeValue("testProperty");
        int index = 1;
        String expectedPath = "/@name='testProperty']";
        String actualPath = nullPropertyPointer.asPath();
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testCreatePathWithAttributeValueMethod() {
        NodePointer propertyOwnerParent = new NodePointer(new PropertyOwnerClass());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerParent);
        nullPropertyPointer.setNameAttributeValue("testProperty");
        int index = 1;
        Object value = new Object();
        JXPathContext context = new JXPathContext(new Object());
        NodePointer actualPath = nullPropertyPointer.createPath(context, value);
        NodePointer expectedPath = new NodePointer(propertyOwnerParent, new QName("testProperty"), index);
        assertEquals(expectedPath, actualPath);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonSupportedParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        Object value = new Object();
        nullPropertyPointer.setValue(value);
    }
}

class PropertyOwnerClass {
    public Object getProperty(String propertyName) {
        return null;
    }

    public void setProperty(String propertyName, Object value) {
        // implementation
    }
}
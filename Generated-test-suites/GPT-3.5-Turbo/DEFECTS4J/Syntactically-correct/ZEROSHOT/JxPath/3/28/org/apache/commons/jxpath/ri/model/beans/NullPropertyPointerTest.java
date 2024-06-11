import org.junit.Test;

import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName name = nullPropertyPointer.getName();
        assertNull(name);
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(0);
        // No assertion as the method does not have any return value
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        int length = nullPropertyPointer.getLength();
        assertEquals(0, length);
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Object baseValue = nullPropertyPointer.getBaseValue();
        assertNull(baseValue);
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Object immediateNode = nullPropertyPointer.getImmediateNode();
        assertNull(immediateNode);
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        boolean isLeaf = nullPropertyPointer.isLeaf();
        assertTrue(isLeaf);
    }

    @Test
    public void testGetValuePointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer valuePointer = nullPropertyPointer.getValuePointer();
        assertNotNull(valuePointer);
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        boolean isActualProperty = nullPropertyPointer.isActualProperty();
        assertFalse(isActualProperty);
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        boolean isActual = nullPropertyPointer.isActual();
        assertFalse(isActual);
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        boolean isContainer = nullPropertyPointer.isContainer();
        assertTrue(isContainer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_whenParentIsNull_shouldThrowException() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(null);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_whenParentIsContainer_shouldThrowException() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer());
        nullPropertyPointer.setValue(null);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_whenDynamicPropertyDeclarationSupported_shouldThrowException() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer() {
            @Override
            public boolean isDynamicPropertyDeclarationSupported() {
                return true;
            }

            @Override
            public PropertyPointer getPropertyPointer() {
                return new PropertyPointer(null) {
                    @Override
                    public void setPropertyName(String propertyName) {

                    }

                    @Override
                    public void setValue(Object value) {
                        throw new JXPathInvalidAccessException("");
                    }
                };
            }
        });
        nullPropertyPointer.setValue(null);
    }

    @Test
    public void testCreatePath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = null; // provide appropriate context
        NodePointer path = nullPropertyPointer.createPath(context);
        assertNotNull(path);
    }

    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = null; // provide appropriate context
        Object value = null; // provide appropriate value
        NodePointer pathWithValue = nullPropertyPointer.createPath(context, value);
        assertNotNull(pathWithValue);
    }

    @Test
    public void testCreateChild() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = null; // provide appropriate context
        QName name = null; // provide appropriate name
        int index = 0; // provide appropriate index
        NodePointer child = nullPropertyPointer.createChild(context, name, index);
        assertNotNull(child);
    }

    @Test
    public void testCreateChildWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = null; // provide appropriate context
        QName name = null; // provide appropriate name
        int index = 0; // provide appropriate index
        Object value = null; // provide appropriate value
        NodePointer childWithValue = nullPropertyPointer.createChild(context, name, index, value);
        assertNotNull(childWithValue);
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String propertyName = nullPropertyPointer.getPropertyName();
        assertEquals("*", propertyName);
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("propertyName");
        String propertyName = nullPropertyPointer.getPropertyName();
        assertEquals("propertyName", propertyName);
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("attributeValue");
        String propertyName = nullPropertyPointer.getPropertyName();
        assertEquals("attributeValue", propertyName);
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        boolean isCollection = nullPropertyPointer.isCollection();
        assertFalse(isCollection);
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        int propertyCount = nullPropertyPointer.getPropertyCount();
        assertEquals(0, propertyCount);
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertNotNull(propertyNames);
        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String path = nullPropertyPointer.asPath();
        assertNotNull(path);
        assertEquals("", path);
    }

}
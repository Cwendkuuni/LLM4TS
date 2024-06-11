import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {
    @Test
    public void testGetName() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        QName result = pointer.getName();

        // Assert
        assertNotNull(result);
        assertEquals("*", result.getName());
    }

    @Test
    public void testSetPropertyIndex() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        pointer.setPropertyIndex(1);

        // Assert
        // No assertions possible
    }

    @Test
    public void testGetLength() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        int result = pointer.getLength();

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testGetBaseValue() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        Object result = pointer.getBaseValue();

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetImmediateNode() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        Object result = pointer.getImmediateNode();

        // Assert
        assertNull(result);
    }

    @Test
    public void testIsLeaf() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        boolean result = pointer.isLeaf();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testGetValuePointer() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        NodePointer result = pointer.getValuePointer();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof NullPointer);
        assertEquals("*", ((NullPointer) result).getPropertyName());
    }

    @Test
    public void testIsActualProperty() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        boolean result = pointer.isActualProperty();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsActual() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        boolean result = pointer.isActual();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsContainer() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        boolean result = pointer.isContainer();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testSetValue() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act/Assert
        try {
            pointer.setValue(null);
            fail("JXPathInvalidAccessException expected");
        } catch (JXPathInvalidAccessException e) {
            assertEquals("Cannot set property null, the target object is null", e.getMessage());
        }
    }

    @Test
    public void testCreatePath() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext();

        // Act/Assert
        try {
            pointer.createPath(context);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testCreatePathWithValue() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext();

        // Act/Assert
        try {
            pointer.createPath(context, null);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testCreateChild() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext();

        // Act/Assert
        try {
            pointer.createChild(context, null, 0);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testCreateChildWithValue() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext();

        // Act/Assert
        try {
            pointer.createChild(context, null, 0, null);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetPropertyName() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        String result = pointer.getPropertyName();

        // Assert
        assertEquals("*", result);
    }

    @Test
    public void testSetPropertyName() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        pointer.setPropertyName("test");

        // Assert
        assertEquals("test", pointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        pointer.setNameAttributeValue("test");

        // Assert
        assertEquals("test", pointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        boolean result = pointer.isCollection();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetPropertyCount() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        int result = pointer.getPropertyCount();

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testGetPropertyNames() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        String[] result = pointer.getPropertyNames();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testAsPath() {
        // Arrange
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        // Act
        String result = pointer.asPath();

        // Assert
        assertEquals("[@name='*']", result);
    }

    private class JXPathContext {
        public NodePointer createPath(NodePointer parent) {
            return null;
        }

        public NodePointer createAttribute(NodePointer parent, QName name) {
            return null;
        }

        public NodePointer createChild(NodePointer parent, QName name, int index) {
            return null;
        }

        public NodePointer createChild(NodePointer parent, QName name, int index, Object value) {
            return null;
        }
    }
}
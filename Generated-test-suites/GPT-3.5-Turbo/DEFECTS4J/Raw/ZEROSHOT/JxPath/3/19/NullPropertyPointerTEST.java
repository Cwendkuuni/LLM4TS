import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    private NullPropertyPointer nullPropertyPointer;

    @Before
    public void setUp() {
        nullPropertyPointer = new NullPropertyPointer(null);
    }

    @Test
    public void testGetName() {
        assertNull(nullPropertyPointer.getName());
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(1);
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
        assertNotNull(nullPropertyPointer.getValuePointer());
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
    public void testSetValue() {
        try {
            nullPropertyPointer.setValue(null);
            fail("Expected JXPathInvalidAccessException to be thrown");
        } catch (JXPathInvalidAccessException e) {
            assertEquals("Cannot set property null, the target object is null", e.getMessage());
        }
    }

    @Test
    public void testCreatePath() {
        assertNotNull(nullPropertyPointer.createPath(null));
    }

    @Test
    public void testCreatePathWithValue() {
        assertNotNull(nullPropertyPointer.createPath(null, null));
    }

    @Test
    public void testCreateChild() {
        assertNotNull(nullPropertyPointer.createChild(null, null, 0));
    }

    @Test
    public void testCreateChildWithValue() {
        assertNotNull(nullPropertyPointer.createChild(null, null, 0, null));
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("property");
        assertEquals("property", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("attributeValue");
        assertEquals("attributeValue", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isAttributeName());
    }

    @Test
    public void testIsCollection() {
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        assertNotNull(nullPropertyPointer.getPropertyNames());
        assertEquals(0, nullPropertyPointer.getPropertyNames().length);
    }

    @Test
    public void testAsPath() {
        assertEquals("[*]", nullPropertyPointer.asPath());
    }

    @Test
    public void testEscape() {
        String string = "test'escape'";
        assertEquals("test&apos;escape&apos;", nullPropertyPointer.escape(string));
    }
}
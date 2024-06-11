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
        assertEquals("*", nullPropertyPointer.getName().toString());
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(5);
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

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        nullPropertyPointer.setValue("Test value");
    }

    @Test
    public void testCreatePath_WithAttribute() {
        assertNotNull(nullPropertyPointer.createPath(null));
    }

    @Test
    public void testCreatePath_WithIndex() {
        nullPropertyPointer.setPropertyIndex(5);
        assertNotNull(nullPropertyPointer.createPath(null));
    }

    @Test
    public void testCreatePath_WithValue() {
        assertNotNull(nullPropertyPointer.createPath(null, "Test value"));
    }

    @Test
    public void testCreateChild() {
        assertNotNull(nullPropertyPointer.createChild(null, new org.apache.commons.jxpath.JXPathContext(), 5));
    }

    @Test
    public void testCreateChild_WithValue() {
        assertNotNull(nullPropertyPointer.createChild(null, new org.apache.commons.jxpath.JXPathContext(), 5, "Test value"));
    }
    
    @Test
    public void testGetPropertyName() {
        assertNull(nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("Test property");
        assertEquals("Test property", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("Test name attribute");
        assertEquals("Test name attribute", nullPropertyPointer.getPropertyName());
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
        assertArrayEquals(new String[0], nullPropertyPointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        assertEquals("@name='Test name attribute'", nullPropertyPointer.asPath());
    }
}
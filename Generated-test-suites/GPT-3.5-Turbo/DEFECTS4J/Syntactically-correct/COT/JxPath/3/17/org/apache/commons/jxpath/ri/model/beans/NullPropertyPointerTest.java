import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertNull(pointer.getName());
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyIndex(0);
        // No assertion, method does not return a value
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals(0, pointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertNull(pointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertNull(pointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertTrue(pointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertNotNull(pointer.getValuePointer());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertTrue(pointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setValue(null);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NodePointer parent = new NullPropertyPointer(null);
        parent.isLeaf = true;
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue(null);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithDynamicPropertyDeclarationSupported() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue(null);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathWithNullParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.createPath(null);
    }

    @Test
    public void testCreatePathWhenAttribute() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.attribute = true;
        assertNotNull(pointer.createPath(null));
    }

    @Test
    public void testCreatePathWithParentPropertyOwnerPointer() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNotNull(pointer.createPath(null));
    }

    @Test
    public void testCreatePathWithNonPropertyParent() {
        NodePointer parent = new NullPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNotNull(pointer.createPath(null));
    }

    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertNotNull(pointer.createPath(null, null));
    }

    @Test
    public void testCreateChildWithAttribute() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.attribute = true;
        assertNotNull(pointer.createChild(null, null, 0));
    }

    @Test
    public void testCreateChildWithNonAttribute() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.attribute = false;
        assertNotNull(pointer.createChild(null, null, 0));
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals("*", pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyName("name");
        assertEquals("name", pointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setNameAttributeValue("name");
        assertEquals("name", pointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals(0, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertArrayEquals(new String[0], pointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals("", pointer.asPath());
    }

    @Test
    public void testEscape() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals("abc", pointer.escape("abc"));
        assertEquals("a&apos;b", pointer.escape("a'b"));
        assertEquals("a&quot;b", pointer.escape("a\"b"));
    }

}
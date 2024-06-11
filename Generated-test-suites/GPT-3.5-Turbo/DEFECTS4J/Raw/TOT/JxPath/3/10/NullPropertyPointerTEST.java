import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals("*", pointer.getName().getName());
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyIndex(1); // No effect
        assertEquals(0, pointer.getLength());
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
    public void testSetValueNullParentContainer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNullParentNonContainer() {
        NullPropertyPointer parent = new NullPropertyPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueDynamicPropertyDeclarationSupported() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer(null);
        parent.setDynamicPropertyDeclarationSupported(true);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueDynamicPropertyDeclarationNotSupported() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer(null);
        parent.setDynamicPropertyDeclarationSupported(false);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue("value");
    }

    @Test
    public void testCreatePath() {
        NullPropertyPointer parent = new NullPropertyPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNull(pointer.createPath(null));
    }

    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer parent = new NullPropertyPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNull(pointer.createPath(null, "value"));
    }

    @Test
    public void testCreateChild() {
        NullPropertyPointer parent = new NullPropertyPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNull(pointer.createChild(null, new QName("child"), 0));
    }

    @Test
    public void testCreateChildWithValue() {
        NullPropertyPointer parent = new NullPropertyPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNull(pointer.createChild(null, new QName("child"), 0, "value"));
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
        String[] propertyNames = pointer.getPropertyNames();
        assertNotNull(propertyNames);
        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals("*", pointer.asPath());
    }

    @Test
    public void testAsPathWithNameAttributeNotSet() {
        NullPropertyPointer parent = new NullPropertyPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertEquals("*", pointer.asPath());
    }

    @Test
    public void testAsPathWithNameAttributeSet() {
        NullPropertyPointer parent = new NullPropertyPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setNameAttributeValue("name");
        assertEquals("*/[@name='name']", pointer.asPath());
    }

    @Test
    public void testAsPathWithNameAttributeSetAndIndex() {
        NullPropertyPointer parent = new NullPropertyPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setNameAttributeValue("name");
        pointer.setIndex(1);
        assertEquals("*/[@name='name'][2]", pointer.asPath());
    }

    @Test
    public void testEscape() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals("aapos;bquot;c", pointer.escape("a'b\"c"));
    }
}
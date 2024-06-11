import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals(new QName("*"), pointer.getName());
    }

    @Test
    public void testGetPropertyIndex() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyIndex(4);
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
        NodePointer valuePointer = pointer.getValuePointer();
        assertEquals(new NullPointer(pointer, new QName("*")), valuePointer);
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
        pointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NullPointer parent = new NullPointer(null, new QName("*"));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonChangeableLocation() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue("value");
    }

    @Test
    public void testCreatePathWithNullParent() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer newPath = pointer.createPath(context);
        assertNotNull(newPath);
    }

    @Test
    public void testCreatePathWithAttribute() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setNameAttributeValue("attribute");
        NodePointer newPath = pointer.createPath(context);
        assertNotNull(newPath);
        assertTrue(newPath instanceof AttributePointer);
        assertEquals(new QName("attribute"), newPath.getName());
    }

    @Test
    public void testCreatePathWithNonNullParent() {
        JXPathContext context = new JXPathContext(null);
        PropertyOwnerPointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NodePointer newPath = pointer.createPath(context);
        assertNotNull(newPath);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer newPath = pointer.createPath(context, "value");
        assertNotNull(newPath);
        assertTrue(newPath instanceof NullPointer);
        assertEquals(new QName("*"), newPath.getName());
        assertEquals("value", newPath.getValue());
    }

    @Test
    public void testCreateChildWithPath() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer childPath = pointer.createChild(context, new QName("child"), 1);
        assertNotNull(childPath);
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer childPath = pointer.createChild(context, new QName("child"), 1, "value");
        assertNotNull(childPath);
        assertTrue(childPath instanceof NullPointer);
        assertEquals(new QName("*"), childPath.getName());
        assertEquals("value", childPath.getValue());
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals("*", pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyName("newProperty");
        assertEquals("newProperty", pointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setNameAttributeValue("attribute");
        assertEquals("attribute", pointer.getPropertyName());
        assertTrue(pointer.isAttributeName());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isCollection());
        pointer.setPropertyIndex(1);
        assertTrue(pointer.isCollection());
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
        pointer.setNameAttributeValue("attribute");
        assertEquals("[@name='attribute']", pointer.asPath());
        pointer.setPropertyIndex(1);
        assertEquals("[@name='attribute'][2]", pointer.asPath());
    }

    @Test
    public void testEscape() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String string = "test'string\"";
        assertEquals("test&apos;string&quot;", pointer.escape(string));
    }
}
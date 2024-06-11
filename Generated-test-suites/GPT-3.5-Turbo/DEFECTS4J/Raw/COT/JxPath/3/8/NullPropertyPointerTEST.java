import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertNull(pointer.getName());
    }

    @Test
    public void testGetPropertyIndex() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyIndex(0);
        assertEquals(0, pointer.getPropertyIndex());
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
        assertTrue(pointer.getValuePointer() instanceof NullPointer);
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
    public void testSetValueWithNullParentAndContainer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParentAndNonContainer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(new PropertyOwnerPointer());
        pointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathWithNullParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.createPath(new JXPathContext());
    }

    @Test
    public void testCreatePathWithParentPropertyOwnerPointer() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NodePointer result = pointer.createPath(new JXPathContext());
        assertTrue(result instanceof PropertyPointer);
    }

    @Test
    public void testCreatePathWithParentNullPointer() {
        NullPointer parent = new NullPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NodePointer result = pointer.createPath(new JXPathContext());
        assertEquals(parent, result);
    }

    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(new NullPointer());
        NodePointer result = pointer.createPath(new JXPathContext(), "value");
        assertEquals(pointer, result);
        assertEquals("value", result.getValue());
    }

    @Test
    public void testCreateChild() {
        NullPropertyPointer pointer = new NullPropertyPointer(new NullPointer());
        NodePointer result = pointer.createChild(new JXPathContext(), new QName("name"), 0);
        assertEquals(pointer, result.getParent());
        assertEquals(new QName("name"), result.getName());
        assertEquals(0, result.getIndex());
    }

    @Test
    public void testCreateChildWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(new NullPointer());
        NodePointer result = pointer.createChild(new JXPathContext(), new QName("name"), 0, "value");
        assertEquals(pointer, result.getParent());
        assertEquals(new QName("name"), result.getName());
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(new NullPointer());
        assertEquals("*", pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(new NullPointer());
        pointer.setPropertyName("name");
        assertEquals("name", pointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(new NullPointer());
        pointer.setNameAttributeValue("attributeValue");
        assertEquals("attributeValue", pointer.getPropertyName());
    }

    @Test
    public void testIsCollectionWithWholeCollection() {
        NullPropertyPointer pointer = new NullPropertyPointer(new NullPointer());
        assertFalse(pointer.isCollection());
    }

    @Test
    public void testIsCollectionWithIndex() {
        NullPropertyPointer pointer = new NullPropertyPointer(new NullPointer());
        pointer.setPropertyIndex(1);
        assertTrue(pointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer pointer = new NullPropertyPointer(new NullPointer());
        assertEquals(0, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer pointer = new NullPropertyPointer(new NullPointer());
        assertArrayEquals(new String[0], pointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        NullPointer parent = new NullPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertEquals("", pointer.asPath());

        NullPropertyPointer namedPointer = new NullPropertyPointer(parent);
        namedPointer.setNameAttributeValue("name");
        assertEquals("[@name='name']", namedPointer.asPath());

        parent.addChild(new NullPropertyPointer(parent));
        namedPointer.setIndex(0);
        assertEquals("[@name='name'][1]", namedPointer.asPath());
    }

}
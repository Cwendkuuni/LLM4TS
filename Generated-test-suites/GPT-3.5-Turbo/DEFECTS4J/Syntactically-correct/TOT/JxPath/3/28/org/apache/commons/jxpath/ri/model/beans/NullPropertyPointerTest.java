import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {

    NullPropertyPointer nullPropertyPointer;

    @Before
    public void setUp() {
        nullPropertyPointer = new NullPropertyPointer(null);
    }

    @Test
    public void testGetName() {
        assertEquals("*", nullPropertyPointer.getName().getName());
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(1);
        assertEquals(0, nullPropertyPointer.getIndex());
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
        NodePointer valuePointer = nullPropertyPointer.getValuePointer();
        assertNotNull(valuePointer);
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
    public void testSetValueWithNullParent() {
        nullPropertyPointer.setValue(123);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NodePointer parent = new NodePointer();
        nullPropertyPointer.setParent(parent);
        nullPropertyPointer.setValue(123);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithDynamicPropertyDeclarationSupported() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        parent.setDynamicPropertyDeclarationSupported(true);

        PropertyPointer propertyPointer = new PropertyPointer();
        parent.setPropertyPointer(propertyPointer);

        nullPropertyPointer.setParent(parent);
        nullPropertyPointer.setValue(123);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithDynamicPropertyDeclarationNotSupported() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        parent.setDynamicPropertyDeclarationSupported(false);

        PropertyPointer propertyPointer = new PropertyPointer();
        parent.setPropertyPointer(propertyPointer);

        nullPropertyPointer.setParent(parent);
        nullPropertyPointer.setValue(123);
    }

    @Test
    public void testCreatePathWithAttribute() {
        NodePointer path = nullPropertyPointer.createPath(null);
        assertNotNull(path);
        assertTrue(path instanceof AttributePointer);
    }

    @Test
    public void testCreatePathWithNullParentPointer() {
        nullPropertyPointer.setParent(null);

        NodePointer path = nullPropertyPointer.createPath(null);
        assertNotNull(path);
        assertTrue(path instanceof ChildPointer);
    }

    @Test
    public void testCreatePathWithAttributeAndValue() {
        NodePointer path = nullPropertyPointer.createPath(null, 123);
        assertNotNull(path);
        assertTrue(path instanceof AttributePointer);
        assertEquals(123, path.getValue());
    }

    @Test
    public void testCreatePathWithNullParentPointerAndValue() {
        nullPropertyPointer.setParent(null);

        NodePointer path = nullPropertyPointer.createPath(null, 123);
        assertNotNull(path);
        assertTrue(path instanceof ChildPointer);
        assertEquals(123, path.getValue());
    }

    @Test
    public void testCreateChild() {
        QName name = new QName("test");
        int index = 0;

        NodePointer child = nullPropertyPointer.createChild(null, name, index);
        assertNotNull(child);
        assertTrue(child instanceof ChildPointer);
        assertEquals(name, child.getName());
        assertEquals(index, child.getIndex());
    }

    @Test
    public void testCreateChildWithValue() {
        QName name = new QName("test");
        int index = 0;
        Object value = 123;

        NodePointer child = nullPropertyPointer.createChild(null, name, index, value);
        assertNotNull(child);
        assertTrue(child instanceof ChildPointer);
        assertEquals(name, child.getName());
        assertEquals(index, child.getIndex());
        assertEquals(value, child.getValue());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
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
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertNotNull(propertyNames);
        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPath() {
        nullPropertyPointer.setPropertyName("test");
        String path = nullPropertyPointer.asPath();
        assertNotNull(path);
        assertEquals("*[@name='test']", path);
    }

    @Test
    public void testAsPathWithIndex() {
        nullPropertyPointer.setPropertyName("test");
        nullPropertyPointer.setIndex(0);
        String path = nullPropertyPointer.asPath();
        assertNotNull(path);
        assertEquals("*[@name='test'][1]", path);
    }

    @Test
    public void testEscape() {
        String escapedString = nullPropertyPointer.escape("'");
        assertEquals("&apos;", escapedString);
    }

    @Test
    public void testEscapeWithDoubleQuotes() {
        String escapedString = nullPropertyPointer.escape("\"");
        assertEquals("&quot;", escapedString);
    }

    @Test
    public void testEscapeWithNoSpecialCharacters() {
        String originalString = "abcdefg";
        String escapedString = nullPropertyPointer.escape(originalString);
        assertEquals(originalString, escapedString);
    }

}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    private NullPropertyPointer nullPropertyPointer;

    @Before
    public void setup() {
        nullPropertyPointer = new NullPropertyPointer(null);
    }

    @Test
    public void testGetName() {
        assertEquals("*", nullPropertyPointer.getName().toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(0);
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
        assertEquals("*", nullPropertyPointer.getValuePointer().asPath());
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
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        nullPropertyPointer.setParent(new NonContainerNodePointer());
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreatePathWithNullAttribute() {
        nullPropertyPointer.createPath(null);
    }

    @Test
    public void testCreatePathWithAValue() {
        NodePointer result = nullPropertyPointer.createPath(new JXPathContext());
        assertNotNull(result);
        assertEquals("*", result.asPath());
    }

    @Test
    public void testCreateChildWithIndex() {
        NodePointer result = nullPropertyPointer.createChild(new JXPathContext(), new QName("name"), 1);
        assertNotNull(result);
        assertEquals("*[1]/name[1]", result.asPath());
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer result = nullPropertyPointer.createChild(new JXPathContext(), new QName("name"), 0, "value");
        assertNotNull(result);
        assertEquals("*[1]/name[0]", result.asPath());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("name");
        assertEquals("name", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("attributeValue");
        assertEquals("attributeValue", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        assertFalse(nullPropertyPointer.isCollection());
        nullPropertyPointer.setIndex(1);
        assertTrue(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        assertArrayEquals(new String[]{}, nullPropertyPointer.getPropertyNames());
    }

    @Test
    public void testAsPathWithAttributeName() {
        nullPropertyPointer.setNameAttributeValue("attributeName");
        assertEquals("[@name='attributeName']", nullPropertyPointer.asPath());
    }

    @Test
    public void testAsPathWithIndex() {
        nullPropertyPointer.setIndex(1);
        assertEquals("[1]", nullPropertyPointer.asPath());
    }

    @Test
    public void testEscape() {
        assertEquals("", nullPropertyPointer.escape(""));
        assertEquals("abc", nullPropertyPointer.escape("abc"));
        assertEquals("a&amp;b", nullPropertyPointer.escape("a&b"));
        assertEquals("a&apos;b", nullPropertyPointer.escape("a'b"));
        assertEquals("a&quot;b", nullPropertyPointer.escape("a\"b"));
    }
}
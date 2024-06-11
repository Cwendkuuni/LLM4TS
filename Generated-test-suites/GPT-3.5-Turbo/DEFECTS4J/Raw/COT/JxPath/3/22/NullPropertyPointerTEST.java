import static org.junit.Assert.*;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getName().getPrefix());
        assertEquals("*", nullPropertyPointer.getName().getName());
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(3);
        // No functionality to test
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.getValuePointer() instanceof NullPointer);
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_nullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_nonContainerParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer());
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_dynamicPropertyDeclarationSupported() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer());
        ((PropertyOwnerPointer) nullPropertyPointer.getParent()).setDynamicPropertyDeclarationSupported(true);
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_nonChangeableLocation() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer());
        nullPropertyPointer.setValue("value");
    }

    @Test
    public void testCreatePath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer());
        assertNotNull(nullPropertyPointer.createPath(new JXPathContext()));
        // No functionality to test
    }

    @Test
    public void testCreatePath_withValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer());
        assertNotNull(nullPropertyPointer.createPath(new JXPathContext(), "value"));
        // No functionality to test
    }

    @Test
    public void testCreateChild() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer());
        assertNotNull(nullPropertyPointer.createChild(new JXPathContext(), new QName("name"), 1));
        // No functionality to test
    }

    @Test
    public void testCreateChild_withValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer());
        assertNotNull(nullPropertyPointer.createChild(new JXPathContext(), new QName("name"), 1, "value"));
        // No functionality to test
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("newPropertyName");
        assertEquals("newPropertyName", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("attributeValue");
        assertEquals("attributeValue", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertArrayEquals(new String[0], nullPropertyPointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("*", nullPropertyPointer.asPath());
    }

    @Test
    public void testEscape() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("test&quot;test", nullPropertyPointer.escape("test\"test"));
        assertEquals("test&apos;test", nullPropertyPointer.escape("test'test"));
    }
}
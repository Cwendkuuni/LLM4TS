import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
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
        assertEquals("*", nullPropertyPointer.getName().getName());
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(5);
        // Since NullPropertyPointer always returns 0 for getLength(),
        // setting the property index should not change anything.
        assertEquals(0, nullPropertyPointer.getLength());
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
        assertTrue(valuePointer instanceof NullPointer);
        assertEquals("*", valuePointer.getName().getName());
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
    public void testSetValueNullParent() {
        nullPropertyPointer.setValue(5);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNonContainerParent() {
        NodePointer parent = new NodePointer(null) {
            public boolean isContainer() { return false; }
        };
        nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(5);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueDynamicPropertyDeclarationNotSupported() {
        NodePointer parent = new PropertyOwnerPointer(null) {
            public boolean isDynamicPropertyDeclarationSupported() { return false; }
        };
        nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(5);
    }

    @Test
    public void testSetValueDynamicPropertyDeclarationSupported() {
        NodePointer parent = new PropertyOwnerPointer(null) {
            public boolean isDynamicPropertyDeclarationSupported() { return true; }
            public PropertyPointer getPropertyPointer() { return new PropertyPointer(null) {}; }
        };
        nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(5);
        // If setValue does not throw an exception, the test passes.
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = JXPathContext.newContext(null);
        NodePointer newPath = nullPropertyPointer.createPath(context);
        assertTrue(newPath instanceof NullPropertyPointer);
        assertSame(context, newPath.getContext());
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = JXPathContext.newContext(null);
        NodePointer newPath = nullPropertyPointer.createPath(context, 5);
        assertTrue(newPath instanceof NullPropertyPointer);
        assertSame(context, newPath.getContext());
        // The actual value does not matter, as the NullPropertyPointer does not store it.
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = JXPathContext.newContext(null);
        NodePointer newChild = nullPropertyPointer.createChild(context, new QName("test"), 0);
        assertTrue(newChild instanceof NullPropertyPointer);
        assertSame(context, newChild.getContext());
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = JXPathContext.newContext(null);
        NodePointer newChild = nullPropertyPointer.createChild(context, new QName("test"), 0, 5);
        assertTrue(newChild instanceof NullPropertyPointer);
        assertSame(context, newChild.getContext());
        // The actual value does not matter, as the NullPropertyPointer does not store it.
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
        assertEquals("/*", nullPropertyPointer.asPath());
    }

    @Test
    public void testAsPathWithNameAttribute() {
        nullPropertyPointer.setNameAttributeValue("test");
        assertEquals("/*[@name='test']", nullPropertyPointer.asPath());
    }

    @Test
    public void testAsPathWithNameAttributeAndIndex() {
        nullPropertyPointer.setNameAttributeValue("test");
        nullPropertyPointer.setPropertyIndex(2);
        assertEquals("/*[@name='test'][3]", nullPropertyPointer.asPath());
    }

    @Test
    public void testEscape() {
        assertEquals("abc", nullPropertyPointer.escape("abc"));
        assertEquals("abc&apos;def", nullPropertyPointer.escape("abc'def"));
        assertEquals("abc&quot;def&quot;ghi",
                nullPropertyPointer.escape("abc\"def\"ghi"));
    }
}
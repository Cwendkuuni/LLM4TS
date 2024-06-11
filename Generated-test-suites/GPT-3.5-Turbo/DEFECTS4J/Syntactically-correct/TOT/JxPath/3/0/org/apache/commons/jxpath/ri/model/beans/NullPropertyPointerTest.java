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
        assertNull(nullPropertyPointer.getName());
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(0);
        // Assert that the property index is not set for NullPropertyPointer
        assertEquals(0, nullPropertyPointer.getPropertyIndex());
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
    public void testSetValue_NullParentObject() {
        nullPropertyPointer.setValue("testValue");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_ReadOnlyParent() {
        // Simulate a readOnly parent by setting parent as a non-container NodePointer
        NodePointer parent = new NodePointer(null) {
            @Override
            public boolean isContainer() {
                return false;
            }
        };
        NullPropertyPointer readOnlyPropertyPointer = new NullPropertyPointer(parent);
        readOnlyPropertyPointer.setValue("testValue");
    }

    @Test
    public void testCreatePath() {
        // Test when parent is null
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer newPath = nullPropertyPointer.createPath(null);
        // Assert that the newPath is a NullPropertyPointer with the same parent
        assertEquals(nullPropertyPointer.getParent(), newPath.getParent());
    }

    @Test
    public void testCreatePath_ObjectValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newPath = nullPropertyPointer.createPath(null, "testValue");
        // Assert that the newPath is a NullPropertyPointer with the same parent
        assertEquals(nullPropertyPointer.getParent(), newPath.getParent());
        // Assert that the newPath's base value is set to "testValue"
        assertEquals("testValue", newPath.getBaseValue());
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName testName = new QName("testName");
        int testIndex = 0;
        NodePointer newPath = nullPropertyPointer.createChild(null, testName, testIndex);
        // Assert that the newPath's parent is the same as the createPath result
        assertEquals(nullPropertyPointer.createPath(null), newPath.getParent());
    }

    @Test
    public void testCreateChild_ObjectValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName testName = new QName("testName");
        int testIndex = 0;
        Object testValue = "testValue";
        NodePointer newPath = nullPropertyPointer.createChild(null, testName, testIndex, testValue);
        // Assert that the newPath's parent is the same as the createPath result
        assertEquals(nullPropertyPointer.createPath(null), newPath.getParent());
        // Assert that the newPath's base value is set to "testValue"
        assertEquals(testValue, newPath.getBaseValue());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        String testPropertyName = "testName";
        nullPropertyPointer.setPropertyName(testPropertyName);
        assertEquals(testPropertyName, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        String testAttributeValue = "testAttributeValue";
        nullPropertyPointer.setNameAttributeValue(testAttributeValue);
        assertEquals(testAttributeValue, nullPropertyPointer.getPropertyName());
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
        String asPath = nullPropertyPointer.asPath();
        assertNotNull(asPath);
        assertTrue(asPath.isEmpty());
    }

    @Test
    public void testEscape() {
        String testString = "test'string\"with'quotes";
        String escapedString = nullPropertyPointer.escape(testString);
        assertNotNull(escapedString);
        assertEquals("test&apos;string&quot;with&apos;quotes", escapedString);
    }
}
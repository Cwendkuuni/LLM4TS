import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getValuePointer());
        assertEquals("", nullPropertyPointer.getPropertyName());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertTrue(nullPropertyPointer.isContainer());
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", nullPropertyPointer.getName().getLocalPart());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyIndexThrowsException() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(1);
    }

    @Test
    public void testGetLengthReturnsZero() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValueReturnsNull() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testIsLeafReturnsTrue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetImmediateNodeReturnsNull() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsActualPropertyReturnsFalse() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActualReturnsFalse() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueThrowsException() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("value");
    }

    @Test
    public void testCreatePathReturnsNullPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.createPath(null);
        assertNull(nodePointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePathThrowsExceptionWithNullContext() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.createPath(null);
    }

    @Test
    public void testCreatePathWithAttributeReturnsAttributePointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer attributeNullPropertyPointer =
                new NullPropertyPointer(new NodePointer(null));
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = nullPropertyPointer.createPath(context, "testAttribute");
        assertTrue(nodePointer instanceof NodePointer);
        assertTrue(nodePointer instanceof AttributePointer);
        assertEquals(attributeNullPropertyPointer, nodePointer.getParentPointer());
        assertEquals("testAttribute", nodePointer.getName().getLocalPart());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePathWithInvalidIndexThrowsException() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer parent = new NodePointer(null);
        NodePointer nodePointer = nullPropertyPointer.createPath(null, 0, -1);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathWithInvalidPathThrowsException() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer parent = new NodePointer(null);
        NodePointer nodePointer = nullPropertyPointer.createPath(null, "testPath");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathWithNullValueThrowsException
            () {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer parent = new NodePointer(null);
        NodePointer nodePointer = nullPropertyPointer.createPath(null, null, 0, null);
    }

    @Test
    public void testCreateChildReturnsNullPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.createChild(null, new QName("testChild"), 0);
        assertNull(nodePointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildThrowsExceptionWithNullContext() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.createChild(null, new QName("testChild"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildThrowsExceptionWithNullName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.createChild(null, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildThrowsExceptionWithInvalidIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.createChild(null, new QName("testChild"), -1);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreateChildThrowsExceptionWithInvalidPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.createChild(null, new QName("testChild"), 0, null);
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("testName");
        assertEquals("testName", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollectionReturnsFalse() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCountReturnsZero() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNamesReturnsEmptyArray() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(new String[0], propertyNames);
    }

    @Test
    public void testAsPathReturnsEmptyPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("", nullPropertyPointer.asPath());
    }

    @Test
    public void testEscapeMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String string = "APlaceForSingleQuotes&\'APlaceForDoubleQuotes\"";
        String escapedString = nullPropertyPointer.escape(string);
        assertEquals("APlaceForSingleQuotes&apos;APlaceForDoubleQuotes&quot;", escapedString);
    }
}
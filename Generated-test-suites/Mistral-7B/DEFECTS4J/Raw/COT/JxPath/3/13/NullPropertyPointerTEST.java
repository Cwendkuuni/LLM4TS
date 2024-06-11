import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNull(pointer.getValuePointer());
        assertEquals(parent, pointer.getParentPointer());
        assertEquals("", pointer.getPropertyName());
        assertFalse(pointer.isCollection());
        assertTrue(pointer.isLeaf());
        assertFalse(pointer.isActual());
        assertFalse(pointer.isContainer());
    }

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyName("testProperty");
        QName name = pointer.getName();
        assertEquals("testProperty", name.getLocalPart());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyIndex(1);
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertEquals(0, pointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNull(pointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNull(pointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertTrue(pointer.isLeaf());
    }

    @Test
    public void testHasValuePointer() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNotNull(pointer.getValuePointer());
        assertFalse(pointer.getValuePointer().getValue() instanceof NodePointer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NodePointer parent = null;
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue("testValue");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonPropertyParent() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue("testValue");
    }

    @Test
    public void testSetValueWithPropertyOwnerParent() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertEquals(propertyName, pointer.getPropertyName());
        assertFalse(pointer.getValuePointer().getValue() instanceof NodePointer);
        assertEquals("testValue", pointer.getValuePointer().getValue().toString());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathWithNullParent() {
        NodePointer parent = null;
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.createPath(new JXPathContext());
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(null);
        NodePointer child = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NodePointer result = pointer.createPath(new JXPathContext());
        assertEquals(child, result);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(null);
        NodePointer child = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NodePointer result = pointer.createPath(new JXPathContext(), "testValue");
        assertEquals(child, result);
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(null);
        NodePointer child = new NodePointer(null);
        NodePointer result = new NullPropertyPointer(parent).createChild(new JXPathContext(), new QName("testName"), 0, child);
        assertEquals(child, result);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer(null);
        NodePointer child = new NodePointer(null);
        NodePointer result = new NullPropertyPointer(parent).createChild(new JXPathContext(), new QName("testName"), 0, child, "testValue");
        assertEquals(child, result);
        assertEquals("testValue", result.getValuePointer().getValue().toString());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetPropertyNameWithNullParent() {
        NodePointer parent = null;
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyName("testProperty");
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyName("testProperty");
        assertEquals("testProperty", pointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setNameAttributeValue("testProperty");
        assertEquals("testProperty", pointer.getPropertyName());
        assertTrue(pointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertFalse(pointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertEquals(0, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        String[] propertyNames = pointer.getPropertyNames();
        assertArrayEquals(new String[0], propertyNames);
    }

    @Test
    public void testAsPath() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertEquals("", pointer.asPath());
        pointer.setPropertyName("testProperty");
        assertEquals("/testProperty", pointer.asPath());
        pointer.setNameAttributeValue("testProperty");
        assertEquals("/testProperty[@name='testProperty']", pointer.asPath());
    }

    @Test
    public void testEscape() {
        String string = "test'string\"with 'single and double quotes";
        String escapedString = new NullPropertyPointer().escape(string);
        assertEquals("test&apos;string&quot;with &apos;single and &quot;double quotes", escapedString);
    }
}
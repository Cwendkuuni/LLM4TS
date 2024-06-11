import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getValuePointer());
        assertFalse(nullPropertyPointer.isActual());
        assertTrue(nullPropertyPointer.isContainer());
        assertEquals("", nullPropertyPointer.getPropertyName());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertFalse(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        QName name = nullPropertyPointer.getName();
        assertEquals("testProperty", name.getLocalPart());
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(1);
        assertEquals(1, nullPropertyPointer.getLength());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.getLength();
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(null, nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(null, nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer(context, null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newPath = nullPropertyPointer.createPath(context);
        assertNull(newPath);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonPropertyOwnerParent() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer(context, null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testSetValueWithPropertyOwnerParent() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new PropertyOwnerPointer(context, null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(new Object());
        PropertyPointer propertyPointer = parent.getPropertyPointer();
        assertEquals("", propertyPointer.getPropertyName());
        assertEquals(new Object(), propertyPointer.getValue());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathWithNonPropertyOwnerParent() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer(context, null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newPath = nullPropertyPointer.createPath(context);
        assertNull(newPath);
    }

    @Test
    public void testCreatePathWithPropertyOwnerParent() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new PropertyOwnerPointer(context, null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newPath = nullPropertyPointer.createPath(context);
        assertNotNull(newPath);
        assertEquals(parent, newPath.getParent());
        assertEquals(nullPropertyPointer, newPath.getLast());
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer(context, null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newPath = nullPropertyPointer.createPath(context, new Object());
        assertNotNull(newPath);
        assertEquals(parent, newPath.getParent());
        assertEquals(nullPropertyPointer, newPath.getLast());
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer(context, null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newChild = nullPropertyPointer.createChild(context, new QName("testName"));
        assertNull(newChild);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateChildWithIndex() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer(context, null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.createChild(context, new QName("testName"), 1);
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer(context, null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newChild = nullPropertyPointer.createChild(context, new QName("testName"), null, new Object());
        assertNull(newChild);
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
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        assertEquals("testAttribute", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
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
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(new String[0], propertyNames);
    }

    @Test
    public void testAsPath() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer(context, null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("testProperty");
        String path = nullPropertyPointer.asPath();
        assertEquals("/testProperty", path);
    }

    @Test
    public void testAsPathWithNameAttribute() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer(context, null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        nullPropertyPointer.setIndex(1);
        String path = nullPropertyPointer.asPath();
        assertEquals("/testProperty[@name='testAttribute']", path);
    }

    @Test
    public void testEscape() {
        String string = "test'string\"";
        String escapedString = new NullPropertyPointer().escape(string);
        assertEquals("test&apos;string&quot;", escapedString);
    }
}
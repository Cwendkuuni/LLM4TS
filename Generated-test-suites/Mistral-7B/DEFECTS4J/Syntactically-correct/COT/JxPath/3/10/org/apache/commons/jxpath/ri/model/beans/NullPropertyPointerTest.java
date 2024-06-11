import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(null, new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getValuePointer());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testGetNameMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName name = nullPropertyPointer.getName();
        assertEquals("*", name.getLocalPart());
    }

    @Test
    public void testSetPropertyIndexMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(-1);
        assertEquals(0, nullPropertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetLengthMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValueMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testIsLeafMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testGetValuePointerMethodWithNullParent() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.getValuePointer();
    }

    @Test
    public void testIsActualPropertyMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActualMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainerMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueMethodWithNullParent() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathMethodWithNullContext() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.createPath(null);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testGetValuePointerMethodWithNullParent() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.getValuePointer();
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetPropertyNameMethodWithNullParent() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("propertyName");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathMethodWithInvalidContext() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.createPath(context);
    }

    @Test
    public void testCreatePathMethod() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parent = new NodePointer(context.getDocument(), new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(parent, nullPropertyPointer.getParent());
        NodePointer result = nullPropertyPointer.createPath(context);
        assertNotNull(result);
        assertEquals(parent, result.getParent());
    }

    @Test
    public void testCreatePathMethodWithValue() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parent = new NodePointer(context.getDocument(), new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object value = new Object();
        NodePointer result = nullPropertyPointer.createPath(context, value);
        assertNotNull(result);
        assertEquals(parent, result.getParent());
    }

    @Test
    public void testCreateChildMethodWithNullContext() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.createChild(null, new QName("child"), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildMethodWithNullContextAndName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.createChild(null, null, 1);
    }

    @Test
    public void testCreateChildMethod() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parent = new NodePointer(context.getDocument(), new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName("child");
        NodePointer result = nullPropertyPointer.createChild(context, name, 1);
        assertNotNull(result);
        assertEquals(parent, result.getParent());
    }

    @Test
    public void testCreateChildMethodWithValue() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parent = new NodePointer(context.getDocument(), new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName("child");
        Object value = new Object();
        NodePointer result = nullPropertyPointer.createChild(context, name, 1, value);
        assertNotNull(result);
        assertEquals(parent, result.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyNameMethodWithInvalidParent() {
        NodePointer parent = new NodePointer(null, new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("propertyName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameAttributeValueMethodWithInvalidParent() {
        NodePointer parent = new NodePointer(null, new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("attributeValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePathMethodWithInvalidIndex() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parent = new NodePointer(context.getDocument(), new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.createPath(context, "index", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePathMethodWithInvalidValue() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parent = new NodePointer(context.getDocument(), new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.createPath(context, "value", 1, null);
    }
}
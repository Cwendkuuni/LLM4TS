import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getPropertyName());
        assertFalse(nullPropertyPointer.isByNameAttribute());
        assertNotNull(nullPropertyPointer.getParent());
        assertTrue(nullPropertyPointer.isContainer());
        assertTrue(nullPropertyPointer.isLeaf());
        assertEquals(0, nullPropertyPointer.getLength());
        assertNull(nullPropertyPointer.getValuePointer());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertFalse(nullPropertyPointer.isActual());
        assertFalse(nullPropertyPointer.isContainer());
        assertThrows(JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue(new Object()));
    }

    @Test
    public void testNullPropertyPointerGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", nullPropertyPointer.getName().getLocalPart());
    }

    @Test
    public void testNullPropertyPointerSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(5);
        assertEquals(5, nullPropertyPointer.getIndex());
    }

    @Test
    public void testNullPropertyPointerGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testNullPropertyPointerGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testNullPropertyPointerGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testNullPropertyPointerIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testNullPropertyPointergetValuePointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NullPointer nullPointer = nullPropertyPointer.getValuePointer();
        assertNotNull(nullPointer);
        assertSame(nullPropertyPointer, nullPointer.getParent());
    }

    @Test
    public void testNullPropertyPointerisActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testNullPropertyPointerisActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testNullPropertyPointerIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testNullPropertyPointerSetValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testNullPropertyPointerCreatePath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer nodePointer = nullPropertyPointer.createPath(context);
        assertNull(nodePointer);
    }

    @Test
    public void testNullPropertyPointerCreatePathWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer nodePointer = nullPropertyPointer.createPath(context, new Object());
        assertNull(nodePointer);
    }

    @Test
    public void testNullPropertyPointerCreateChild() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("test"), 0);
        assertNull(nodePointer);
    }

    @Test
    public void testNullPropertyPointerCreateChildWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("test"), 0, new Object());
        assertNull(nodePointer);
    }

    @Test
    public void testNullPropertyPointerSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testNullPropertyPointerSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        assertEquals("testAttribute", nullPropertyPointer.getPropertyName());
        // Test that byNameAttribute is set to true
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testNullPropertyPointerIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testNullPropertyPointerGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testNullPropertyPointerGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(new String[0], propertyNames);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testNullPropertyPointerSetValueWithNonContainerParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Object parent = new Object();
        nullPropertyPointer.setValue(new Object());
        NodePointer parentPointer = new NodePointer(parent);
        NullPropertyPointer nullPropertyPointerWithParent = new NullPropertyPointer(parentPointer);
        nullPropertyPointerWithParent.setValue("test");
        assertEquals("test", nullPropertyPointerWithParent.getValuePointer().getValue());
        // Test that setting value on nullPropertyPointer throws an exception
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testNullPropertyPointerCreatePathWithNonContainerParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Object parent = new Object();
        nullPropertyPointer.setParent(new NodePointer(parent));
        JXPathContext context = new JXPathContext(new Object());
        NodePointer nodePointer = nullPropertyPointer.createPath(context);
        assertNull(nodePointer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testNullPropertyPointerCreatePathWithValueWithNonContainerParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Object parent = new Object();
        nullPropertyPointer.setParent(new NodePointer(parent));
        JXPathContext context = new JXPathContext(new Object());
        NodePointer nodePointer = nullPropertyPointer.createPath(context, new Object());
        assertNull(nodePointer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testNullPropertyPointerCreateChildWithNonContainerParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Object parent = new Object();
        nullPropertyPointer.setParent(new NodePointer(parent));
        JXPathContext context = new JXPathContext(new Object());
        NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("test"), 0);
        assertNull(nodePointer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testNullPropertyPointerCreateChildWithValueWithNonContainerParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Object parent = new Object();
        nullPropertyPointer.setParent(new NodePointer(parent));
        JXPathContext context = new JXPathContext(new Object());
        NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("test"), 0, new Object());
        assertNull(nodePointer);
    }
}
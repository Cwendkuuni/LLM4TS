import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        QName expected = new QName("*");
        QName result = nullPointer.getName();
        assertEquals(expected, result);
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        boolean result = nullPointer.isLeaf();
        assertTrue(result);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        nullPointer.setValue("test");
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPointer.createPath(context);
        assertNotNull(result);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPointer.createPath(context, "test");
        assertNotNull(result);
        assertEquals("test", result.getValue());
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        QName name = new QName("child");
        NodePointer result = nullPointer.createChild(context, name, 0);
        assertNotNull(result);
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        QName name = new QName("child");
        NodePointer result = nullPointer.createChild(context, name, 0, "test");
        assertNotNull(result);
        assertEquals("test", result.getValue());
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        nullPointer.setPropertyName("test");
        assertEquals("test", nullPointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        boolean result = nullPointer.isCollection();
        assertFalse(result);
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        int result = nullPointer.getPropertyCount();
        assertEquals(0, result);
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        String[] result = nullPointer.getPropertyNames();
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testAsPath() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        String result = nullPointer.asPath();
        assertNotNull(result);
        assertEquals("/*", result);
    }

    @Test
    public void testAsPathWithNameAttribute() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        nullPointer.setPropertyName("test");
        String result = nullPointer.asPath();
        assertNotNull(result);
        assertEquals("/*[@name='test']", result);
    }

    @Test
    public void testAsPathWithNameAttributeAndIndex() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPointer = new NullPropertyPointer(parent);
        nullPointer.setPropertyName("test");
        nullPointer.setIndex(1);
        String result = nullPointer.asPath();
        assertNotNull(result);
        assertEquals("/*[@name='test'][2]", result);
    }

}
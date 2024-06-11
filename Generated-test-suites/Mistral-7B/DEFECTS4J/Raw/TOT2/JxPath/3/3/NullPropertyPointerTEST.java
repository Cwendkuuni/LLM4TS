
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getValuePointer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetPropertyNameNull() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName(null);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetPropertyNameEmpty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetPropertyNameNonExistentProperty() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("nonExistentProperty");
    }

    @Test
    public void testSetValueNull() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(null);
        assertNull(nullPropertyPointer.getValuePointer());
    }

    @Test
    public void testSetValueInstanceOfNullPropertyPointer() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NullPropertyPointer value = new NullPropertyPointer(new NodePointer(null));
        nullPropertyPointer.setValue(value);
        assertEquals(value, nullPropertyPointer.getValuePointer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathNullContext() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = null;
        assertNull(nullPropertyPointer.createPath(context));
    }

    @Test
    public void testCreatePathEmptyContext() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        assertNull(nullPropertyPointer.createPath(context));
    }

    @Test
    public void testCreatePathExistentProperty() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer expected = new NodePointer(new Object());
        NodePointer actual = nullPropertyPointer.createPath(context);
        assertEquals(expected, actual);
    }

    // Add test cases for the remaining methods here...

}
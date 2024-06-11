import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        assertEquals("*", nullProperty.getName().getName());
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        assertEquals(0, nullProperty.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        assertNull(nullProperty.getBaseValue());
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testSetValueNullParent() {
        NullPropertyPointer nullProperty = new NullPropertyPointer(null);
        assertThrows(JXPathInvalidAccessException.class, () -> {
            nullProperty.setValue("testValue");
        });
    }

    @Test
    public void testCreatePathWithNullValue() {
        JXPathContext context = JXPathContext.newContext(null);
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        NodePointer nodePointer = nullProperty.createPath(context, null);
        assertNull(nodePointer);
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        nullProperty.setPropertyName("testPropertyName");
        assertEquals("testPropertyName", nullProperty.getPropertyName());
    }

}
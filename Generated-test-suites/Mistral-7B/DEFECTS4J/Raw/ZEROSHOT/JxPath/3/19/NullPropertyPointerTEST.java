import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerInstance() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
        assertNull(nullPropertyPointer.getImmediateNode());
        assertTrue(nullPropertyPointer.isLeaf());
        assertEquals(0, nullPropertyPointer.getLength());
        assertFalse(nullPropertyPointer.isActual());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertFalse(nullPropertyPointer.isContainer());
    }

    @Test
    public void testNullPropertyPointerCreatePath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer nodePointer = nullPropertyPointer.createPath(context);
        assertNull(nodePointer);
    }

    @Test
    public void testNullPropertyPointerSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testNullPropertyPointerSetValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("test");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testNullPropertyPointerCreateChild() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(new Object());
        nullPropertyPointer.createChild(context, new QName("test"), 0);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testNullPropertyPointerCreateChildWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(new Object());
        nullPropertyPointer.createChild(context, new QName("test"), 0, "test");
    }

    @Test
    public void testNullPropertyPointerAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("", nullPropertyPointer.asPath());
    }

    @Test
    public void testNullPropertyPointerSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testNullPropertyPointerIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testNullPropertyPointergetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testNullPropertyPointergetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(new String[0], propertyNames);
    }
}
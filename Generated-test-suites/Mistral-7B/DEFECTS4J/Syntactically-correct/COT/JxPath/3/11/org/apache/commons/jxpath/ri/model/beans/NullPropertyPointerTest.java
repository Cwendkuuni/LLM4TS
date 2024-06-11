import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getValuePointer());
    }

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        QName expectedName = new QName("testProperty");
        QName actualName = nullPropertyPointer.getName();
        assertEquals(expectedName, actualName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(0);
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testgetValuePointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NullPointer nullPointer = nullPropertyPointer.getValuePointer();
        assertNotNull(nullPointer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NonPropertyOwner() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Object parent = new Object();
        nullPropertyPointer.setValue(parent);
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer expectedResult = new NodePointer(null);
        NodePointer actualResult = nullPropertyPointer.createPath(context);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePath_NonPropertyOwner() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer nonPropertyOwner = new NodePointer(null);
        nullPropertyPointer.createPath(context, nonPropertyOwner);
    }

    @Test
    public void testCreatePath_Attribute() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer expectedAttribute = new NodePointer(null).createAttribute(context, new QName("test"));
        NodePointer actualAttribute = nullPropertyPointer.createPath(context).createAttribute(context, new QName("test"));
        assertEquals(expectedAttribute, actualAttribute);
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer expectedChild = new NodePointer(null).createChild(context, new QName("test"), 0);
        NodePointer actualChild = nullPropertyPointer.createChild(context, new QName("test"), 0);
        assertEquals(expectedChild, actualChild);
    }

    @Test
    public void testCreateChild_WithValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer expectedChild = new NodePointer(null).createChild(context, new QName("test"), 0, new Object());
        NodePointer actualChild = nullPropertyPointer.createChild(context, new QName("test"), 0, new Object());
        assertEquals(expectedChild, actualChild);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateChild_InvalidIndex() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        nullPropertyPointer.createChild(context, new QName("test"), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName(null);
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());
    }
}
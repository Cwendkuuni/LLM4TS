import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(null, nullPropertyPointer.getParent());
        assertEquals("*", nullPropertyPointer.getPropertyName());
        assertFalse(nullPropertyPointer.isByNameAttribute());
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

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NodePointer nonContainerParent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonContainerParent);
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testCreatePathWithContainerParent() {
        NodePointer containerParent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(containerParent);
        NodePointer result = nullPropertyPointer.createPath(null);
        assertEquals(containerParent, result);
    }

    @Test
    public void testCreatePathWithNonContainerParent() {
        NodePointer nonContainerParent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonContainerParent);
        NodePointer result = nullPropertyPointer.createPath(null);
        assertNull(result);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonPropertyOwnerParent() {
        NodePointer nonPropertyOwnerParent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonPropertyOwnerParent);
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testCreatePathWithPropertyOwnerParent() {
        NodePointer propertyOwnerParent = new PropertyOwnerPointer(new NodePointer(null, null));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerParent);
        NodePointer result = nullPropertyPointer.createPath(null);
        assertEquals(propertyOwnerParent.getPropertyPointer(), result);
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
        NodePointer result = nullPropertyPointer.getValuePointer();
        assertEquals(new NullPointer(nullPropertyPointer, new QName("*")), result);
    }

    @Test
    public void testCreatePathWithContext() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer result = nullPropertyPointer.createPath(context);
        assertNull(result);
    }

    @Test
    public void testCreatePathWithContextAndValue() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        NodePointer result = nullPropertyPointer.createPath(context, value);
        assertNull(result);
    }

    @Test
    public void testCreateChildWithContext() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testName");
        NodePointer result = nullPropertyPointer.createChild(context, name);
        assertNull(result);
    }

    @Test
    public void testCreateChildWithContextAndIndex() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testName");
        NodePointer result = nullPropertyPointer.createChild(context, name, 0);
        assertNull(result);
    }

    @Test
    public void testCreateChildWithContextAndValueAndIndex() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testName");
        Object value = new Object();
        NodePointer result = nullPropertyPointer.createChild(context, name, 0, value);
        assertNull(result);
    }

    @Test
    public void testCreateChildWithContextAndNameAttribute() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testName");
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        NodePointer result = nullPropertyPointer.createChild(context, name);
        assertNull(result);
    }

    @Test
    public void testCreateChildWithContextAndNameAttributeAndValue() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testName");
        Object value = new Object();
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        NodePointer result = nullPropertyPointer.createChild(context, name, value);
        assertNull(result);
    }
}
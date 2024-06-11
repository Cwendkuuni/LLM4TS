import org.junit.Test;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName expected = new QName("*");
        assertEquals(expected, nullPropertyPointer.getName());
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyIndex(0);
        // Perform assertion
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        int expected = 0;
        assertEquals(expected, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer expected = new NodePointer();
        expected.setValue(null);
        assertEquals(expected, nullPropertyPointer.getValuePointer());
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithContainerParent() {
        NodePointer containerParent = new NodePointer();
        containerParent.setContainer(true);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(containerParent);
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonChangeableLocationParent() {
        NodePointer nonChangeableLocationParent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonChangeableLocationParent);
        nullPropertyPointer.setValue("value");
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer expectedParent = new NodePointer();
        assertEquals(expectedParent, nullPropertyPointer.createPath(context));
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer expectedParent = new NodePointer();
        Object value = new Object();
        assertEquals(expectedParent, nullPropertyPointer.createPath(context, value));
    }

    @Test
    public void testCreateChildWithIndex() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName();
        int index = 0;
        NodePointer expected = new NodePointer();
        assertEquals(expected, nullPropertyPointer.createChild(context, name, index));
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName();
        int index = 0;
        Object value = new Object();
        NodePointer expected = new NodePointer();
        assertEquals(expected, nullPropertyPointer.createChild(context, name, index, value));
    }

    @Test
    public void testGetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String expected = "*";
        assertEquals(expected, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String propertyName = "testProperty";
        nullPropertyPointer.setPropertyName(propertyName);
        assertEquals(propertyName, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String attributeValue = "testName";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        assertEquals(attributeValue, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        int expected = 0;
        assertEquals(expected, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String[] expected = new String[0];
        assertArrayEquals(expected, nullPropertyPointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String expected = "";
        assertEquals(expected, nullPropertyPointer.asPath());
    }

    private class NodePointer extends NodePointer {
        // Implement abstract methods
    }
}
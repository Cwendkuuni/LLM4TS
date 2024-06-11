import org.junit.Test;

import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName expected = new QName("*");
        QName actual = nullPropertyPointer.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyIndex(0);
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        int expected = 0;
        int actual = nullPropertyPointer.getLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object expected = null;
        Object actual = nullPropertyPointer.getBaseValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object expected = null;
        Object actual = nullPropertyPointer.getImmediateNode();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NullPointer expected = new NullPointer(null, new QName(nullPropertyPointer.getPropertyName()));
        NodePointer actual = nullPropertyPointer.getValuePointer();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        NodePointer expected = parent.createPath(context).createChild(context, nullPropertyPointer.getName(), nullPropertyPointer.getIndex());
        NodePointer actual = nullPropertyPointer.createPath(context);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        Object value = new Object();
        NodePointer expected = parent.createPath(context).createChild(context, nullPropertyPointer.getName(), nullPropertyPointer.getIndex(), value);
        NodePointer actual = nullPropertyPointer.createPath(context, value);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateChildWithNameAndIndex() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        QName name = new QName("*");
        int index = 0;
        NodePointer expected = nullPropertyPointer.createPath(context).createChild(context, name, index);
        NodePointer actual = nullPropertyPointer.createChild(context, name, index);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateChildWithNameIndexAndValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        QName name = new QName("*");
        int index = 0;
        Object value = new Object();
        NodePointer expected = nullPropertyPointer.createPath(context).createChild(context, name, index, value);
        NodePointer actual = nullPropertyPointer.createChild(context, name, index, value);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String expected = "*";
        String actual = nullPropertyPointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String propertyName = "test";
        nullPropertyPointer.setPropertyName(propertyName);
        String expected = propertyName;
        String actual = nullPropertyPointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String attributeValue = "test";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        String expected = attributeValue;
        String actual = nullPropertyPointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        int expected = 0;
        int actual = nullPropertyPointer.getPropertyCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String[] expected = new String[0];
        String[] actual = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPathWithoutNameAttribute() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String expected = nullPropertyPointer.getImmediateParentPointer().asPath() + "[@name='" + nullPropertyPointer.escape(nullPropertyPointer.getPropertyName()) + "']";
        String actual = nullPropertyPointer.asPath();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPathWithNameAttribute() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue(nullPropertyPointer.getPropertyName());
        String expected = nullPropertyPointer.getImmediateParentPointer().asPath() + "[@name='" + nullPropertyPointer.escape(nullPropertyPointer.getPropertyName()) + "']";
        String actual = nullPropertyPointer.asPath();
        assertEquals(expected, actual);
    }

    @Test
    public void testEscape() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String string = "test";
        String expected = string;
        String actual = nullPropertyPointer.escape(string);
        assertEquals(expected, actual);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName expected = new QName("*");
        QName actual = nullPropertyPointer.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyIndex(0);
        // No assertion needed, check if errors occur
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        int expected = 0;
        int actual = nullPropertyPointer.getLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object expected = null;
        Object actual = nullPropertyPointer.getBaseValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object expected = null;
        Object actual = nullPropertyPointer.getImmediateNode();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        boolean expected = true;
        boolean actual = nullPropertyPointer.isLeaf();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetValuePointer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NullPointer expected = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        NodePointer actual = nullPropertyPointer.getValuePointer();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        boolean expected = false;
        boolean actual = nullPropertyPointer.isActualProperty();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        boolean expected = false;
        boolean actual = nullPropertyPointer.isActual();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsContainer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        boolean expected = true;
        boolean actual = nullPropertyPointer.isContainer();
        assertEquals(expected, actual);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("Value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithContainerParent() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("Value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithDynamicPropertyDeclarationSupported() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        parent.setDynamicPropertyDeclarationSupported(true);
        nullPropertyPointer.setValue("Value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithPathDoesNotMatchChangeableLocation() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        parent.setDynamicPropertyDeclarationSupported(false);
        nullPropertyPointer.setValue("Value");
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer expected = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        NodePointer actual = nullPropertyPointer.createPath(context);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer expected = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        NodePointer actual = nullPropertyPointer.createPath(context, "Value");
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName("Name");
        int index = 0;
        NodePointer expected = nullPropertyPointer.createPath(context).createChild(context, name, index);
        NodePointer actual = nullPropertyPointer.createChild(context, name, index);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName("Name");
        int index = 0;
        Object value = "Value";
        NodePointer expected = nullPropertyPointer.createPath(context).createChild(context, name, index, value);
        NodePointer actual = nullPropertyPointer.createChild(context, name, index, value);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String expected = "*";
        String actual = nullPropertyPointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String propertyName = "PropertyName";
        nullPropertyPointer.setPropertyName(propertyName);
        // No assertion needed, check if errors occur
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String attributeValue = "AttributeValue";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        // No assertion needed, check if errors occur
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        boolean expected = false;
        boolean actual = nullPropertyPointer.isCollection();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        int expected = 0;
        int actual = nullPropertyPointer.getPropertyCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String[] expected = new String[0];
        String[] actual = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPath() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String expected = nullPropertyPointer.getImmediateParentPointer().asPath() + "[@name='" + nullPropertyPointer.escape(nullPropertyPointer.getPropertyName()) + "']";
        if (nullPropertyPointer.getIndex() != PropertyPointer.WHOLE_COLLECTION) {
            expected += "[" + (nullPropertyPointer.getIndex() + 1) + "]";
        }
        String actual = nullPropertyPointer.asPath();
        assertEquals(expected, actual);
    }

    @Test
    public void testEscape() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String string = "test";
        String expected = "test";
        String actual = nullPropertyPointer.escape(string);
        assertEquals(expected, actual);
    }
}
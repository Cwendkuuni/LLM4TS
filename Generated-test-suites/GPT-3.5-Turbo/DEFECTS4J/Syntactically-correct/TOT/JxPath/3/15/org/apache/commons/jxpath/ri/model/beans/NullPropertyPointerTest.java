import org.junit.Test;
import org.junit.Assert;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        QName expected = new QName("*");
        QName actual = nullProperty.getName();
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        nullProperty.setPropertyIndex(0);

        // No assert since this method has no return value
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        int expected = 0;
        int actual = nullProperty.getLength();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        Object expected = null;
        Object actual = nullProperty.getBaseValue();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        Object expected = null;
        Object actual = nullProperty.getImmediateNode();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        boolean expected = true;
        boolean actual = nullProperty.isLeaf();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetValuePointer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        NullPointer expected = new NullPointer(nullProperty, new QName(nullProperty.getPropertyName()));
        NullPointer actual = nullProperty.getValuePointer();

        Assert.assertEquals(expected, actual);
    }

    // Additional test methods

    @Test
    public void testIsActualProperty() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        boolean expected = false;
        boolean actual = nullProperty.isActualProperty();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        boolean expected = false;
        boolean actual = nullProperty.isActual();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsContainer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        boolean expected = true;
        boolean actual = nullProperty.isContainer();

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_nullParent() {
        NullPropertyPointer nullProperty = new NullPropertyPointer(null);

        nullProperty.setValue("test value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_nonContainerParent() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        parent.setContainer(false);

        nullProperty.setValue("test value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_dynamicPropertyDeclarationNotSupported() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        parent.setContainer(true);
        parent.setPropertyOwnerPointer(false);

        nullProperty.setValue("test value");
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        JXPathContext context = new JXPathContext();
        NodePointer expected = new NodePointer();
        NodePointer actual = nullProperty.createPath(context);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        JXPathContext context = new JXPathContext();
        Object value = "test value";

        NodePointer expected = new NodePointer();
        NodePointer actual = nullProperty.createPath(context, value);

        Assert.assertEquals(expected, actual);
    }

    // Additional test methods

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        JXPathContext context = new JXPathContext();
        QName name = new QName("test");
        int index = 0;

        NodePointer expected = new NodePointer();
        NodePointer actual = nullProperty.createChild(context, name, index);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        JXPathContext context = new JXPathContext();
        QName name = new QName("test");
        int index = 0;
        Object value = "test value";

        NodePointer expected = new NodePointer();
        NodePointer actual = nullProperty.createChild(context, name, index, value);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        String expected = "*";
        String actual = nullProperty.getPropertyName();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        String propertyName = "test";
        nullProperty.setPropertyName(propertyName);

        String expected = propertyName;
        String actual = nullProperty.getPropertyName();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        String attributeValue = "test";
        nullProperty.setNameAttributeValue(attributeValue);

        String expected = attributeValue;
        String actual = nullProperty.getPropertyName();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        boolean expected = false;
        boolean actual = nullProperty.isCollection();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        int expected = 0;
        int actual = nullProperty.getPropertyCount();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        String[] expected = new String[0];
        String[] actual = nullProperty.getPropertyNames();

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPath() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);

        String expected = nullProperty.getPropertyName();
        String actual = nullProperty.asPath();

        Assert.assertEquals(expected, actual);
    }
}
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertEquals("*", nullPropertyPointer.getName().getName());
    }

    @Test
    public void testSetPropertyIndex() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(5);
        Assert.assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetLength() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NullPointer nullPointer = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        Assert.assertEquals(nullPointer, nullPropertyPointer.getValuePointer());
    }

    @Test
    public void testIsActualProperty() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NullParent() {
        // Error scenario
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NonContainerParent() {
        // Error scenario
        PropertyOwnerPointer parentPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPointer);
        nullPropertyPointer.setValue("value");
    }

    @Test
    public void testCreatePath() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = null;
        NullPointer nullPointer = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        Assert.assertEquals(nullPointer, nullPropertyPointer.createPath(context));
    }

    @Test
    public void testCreatePathWithValue() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = null;
        NullPointer nullPointer = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        Assert.assertEquals(nullPointer, nullPropertyPointer.createPath(context, "value"));
    }

    @Test
    public void testCreateChild() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = null;
        QName name = new QName("child");
        int index = 0;
        NullPointer nullPointer = new NullPointer(nullPropertyPointer, name);
        Assert.assertEquals(nullPointer, nullPropertyPointer.createChild(context, name, index));
    }

    @Test
    public void testCreateChildWithValue() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = null;
        QName name = new QName("child");
        int index = 0;
        Object value = "value";
        NullPointer nullPointer = new NullPointer(nullPropertyPointer, name);
        Assert.assertEquals(nullPointer, nullPropertyPointer.createChild(context, name, index, value));
    }

    @Test
    public void testGetPropertyName() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("property");
        Assert.assertEquals("property", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("attribute");
        Assert.assertTrue(nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(5);
        Assert.assertTrue(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] expectedPropertyNames = new String[0];
        Assert.assertArrayEquals(expectedPropertyNames, nullPropertyPointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        // Typical use case
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NullPointer nullPointer = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        Assert.assertEquals(nullPointer.asPath(), nullPropertyPointer.asPath());
    }

}
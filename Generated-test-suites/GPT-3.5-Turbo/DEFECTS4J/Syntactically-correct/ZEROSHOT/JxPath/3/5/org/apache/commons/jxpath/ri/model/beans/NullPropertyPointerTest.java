import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {
    @Test
    public void testGetName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertEquals("*", pointer.getName().toString());
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyIndex(1);
        // test setPropertyIndex does nothing
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertEquals(0, pointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertNull(pointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertNull(pointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertTrue(pointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertTrue(pointer.getValuePointer() instanceof NullPointer);
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertFalse(pointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertFalse(pointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertTrue(pointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setValue(null);
    }

    @Test
    public void testCreatePath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer newPointer = pointer.createPath(null);
        Assert.assertTrue(newPointer instanceof NullPointer);
    }

    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer newPointer = pointer.createPath(null, null);
        Assert.assertTrue(newPointer instanceof NullPointer);
    }

    @Test
    public void testCreateChild() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer newPointer = pointer.createChild(null, null, 0);
        Assert.assertTrue(newPointer instanceof NullPropertyPointer);
    }

    @Test
    public void testCreateChildWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer newPointer = pointer.createChild(null, null, 0, null);
        Assert.assertTrue(newPointer instanceof NullPropertyPointer);
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertEquals("*", pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyName("foo");
        Assert.assertEquals("foo", pointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setNameAttributeValue("bar");
        Assert.assertEquals("bar", pointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertFalse(pointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertEquals(0, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertArrayEquals(new String[0], pointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertEquals("*", pointer.asPath());
    }

    @Test
    public void testEscape() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertEquals("abc", pointer.escape("abc"));
    }
}
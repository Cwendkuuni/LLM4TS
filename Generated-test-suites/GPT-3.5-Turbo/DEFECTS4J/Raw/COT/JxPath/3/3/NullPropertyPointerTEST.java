
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertNotNull(pointer.getName());
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyIndex(1);
        Assert.assertEquals(0, pointer.getPropertyIndex());
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
        Assert.assertNotNull(pointer.getValuePointer());
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
    public void testSetValueWithNullParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setValue("test");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue("test");
    }

    @Test
    public void testCreatePath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertNotNull(pointer.createPath(null));
    }

    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertNotNull(pointer.createPath(null, "test"));
    }

    @Test
    public void testCreateChild() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertNotNull(pointer.createChild(null, null, 0));
    }

    @Test
    public void testCreateChildWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertNotNull(pointer.createChild(null, null, 0, "test"));
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertEquals("*", pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyName("test");
        Assert.assertEquals("test", pointer.getPropertyName());
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
        Assert.assertNotNull(pointer.asPath());
    }

}

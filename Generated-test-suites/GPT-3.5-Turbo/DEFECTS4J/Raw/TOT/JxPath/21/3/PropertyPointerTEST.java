import org.junit.Assert;
import org.junit.Test;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer pointer = new MockPropertyPointer();
        pointer.setPropertyIndex(1);
        Assert.assertEquals(1, pointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        PropertyPointer pointer = new MockPropertyPointer();
        pointer.setPropertyIndex(1);
        pointer.setPropertyIndex(2);
        Assert.assertEquals(2, pointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        PropertyPointer pointer = new MockPropertyPointer();
        pointer.setNode(new Object());
        Assert.assertNotNull(pointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer pointer = new MockPropertyPointer();
        pointer.setPropertyName("test");
        Assert.assertEquals("test", pointer.getName().getLocalPart());
    }

    @Test
    public void testGetPropertyName() {
        PropertyPointer pointer = new MockPropertyPointer();
        pointer.setPropertyName("test");
        Assert.assertEquals("test", pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer pointer = new MockPropertyPointer();
        pointer.setPropertyName("test1");
        pointer.setPropertyName("test2");
        Assert.assertEquals("test2", pointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer pointer = new MockPropertyPointer();
        Assert.assertEquals(0, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer pointer = new MockPropertyPointer();
        Assert.assertArrayEquals(new String[]{}, pointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer pointer = new MockPropertyPointer();
        Assert.assertFalse(pointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        PropertyPointer pointer = new MockPropertyPointer();
        pointer.setNode(new Object());
        Assert.assertFalse(pointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer pointer = new MockPropertyPointer();
        pointer.setNode("test");
        Assert.assertEquals("test", pointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer pointer = new MockPropertyPointer();
        pointer.setNode(new Object[]{});
        Assert.assertTrue(pointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer pointer = new MockPropertyPointer();
        pointer.setNode("test");
        Assert.assertTrue(pointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer pointer = new MockPropertyPointer();
        pointer.setNode(new Object[]{1, 2, 3});
        Assert.assertEquals(3, pointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer pointer = new MockPropertyPointer();
        pointer.setNode("test");
        Assert.assertEquals("test", pointer.getImmediateValuePointer().getValue());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer pointer = new MockPropertyPointer();
        Assert.assertNull(pointer.createPath(null));
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer pointer = new MockPropertyPointer();
        pointer.createPath(null, "test");
        Assert.assertEquals("test", pointer.getValue());
    }

    @Test
    public void testCreateChildWithIndexAndValue() {
        PropertyPointer parent = new MockPropertyPointer();
        PropertyPointer child = (PropertyPointer) parent.createChild(null, null, 0, "test");
        Assert.assertEquals(0, child.getPropertyIndex());
        Assert.assertEquals("test", child.getValue());
    }

    @Test
    public void testCreateChildWithIndex() {
        PropertyPointer parent = new MockPropertyPointer();
        PropertyPointer child = (PropertyPointer) parent.createChild(null, null, 0);
        Assert.assertEquals(0, child.getPropertyIndex());
    }

    @Test
    public void testHashCode() {
        PropertyPointer pointer1 = new MockPropertyPointer();
        PropertyPointer pointer2 = new MockPropertyPointer();
        Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer pointer1 = new MockPropertyPointer();
        PropertyPointer pointer2 = new MockPropertyPointer();
        Assert.assertEquals(pointer1, pointer2);
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer pointer = new MockPropertyPointer();
        Assert.assertEquals(0, pointer.compareChildNodePointers(null, null));
    }

    private static class MockPropertyPointer extends PropertyPointer {
        public MockPropertyPointer() {
            super(null);
        }

        public String getPropertyName() {
            return null;
        }

        public void setPropertyName(String propertyName) {

        }

        public int getPropertyCount() {
            return 0;
        }

        public String[] getPropertyNames() {
            return new String[0];
        }

        protected boolean isActualProperty() {
            return false;
        }
    }
}
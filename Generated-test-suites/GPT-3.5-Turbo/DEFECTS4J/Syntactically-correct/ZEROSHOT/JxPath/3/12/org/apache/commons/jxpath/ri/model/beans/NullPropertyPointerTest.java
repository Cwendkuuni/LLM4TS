import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {
    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertNotEquals(null, nullPropertyPointer.getName());
    }
    
    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyIndex(1);
        Assert.assertEquals(0, nullPropertyPointer.getLength());
    }
    
    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals(0, nullPropertyPointer.getLength());
    }
    
    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals(null, nullPropertyPointer.getBaseValue());
    }
    
    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals(null, nullPropertyPointer.getImmediateNode());
    }
    
    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertTrue(nullPropertyPointer.isLeaf());
    }
    
    @Test
    public void testGetValuePointer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertNotEquals(null, nullPropertyPointer.getValuePointer());
    }
    
    @Test
    public void testIsActualProperty() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertFalse(nullPropertyPointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertFalse(nullPropertyPointer.isActual());
    }
    
    @Test
    public void testIsContainer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertTrue(nullPropertyPointer.isContainer());
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("test");
    }
    
    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        nullPropertyPointer.createPath(context);
    }
    
    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        nullPropertyPointer.createPath(context, "test");
    }
    
    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("child");
        int index = 1;
        nullPropertyPointer.createChild(context, name, index);
    }
    
    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("child");
        int index = 1;
        nullPropertyPointer.createChild(context, name, index, "test");
    }
    
    @Test
    public void testGetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("*", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("test");
        Assert.assertEquals("test", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("test");
        Assert.assertEquals("test", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertFalse(nullPropertyPointer.isCollection());
    }
    
    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals(0, nullPropertyPointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String[] expected = new String[0];
        Assert.assertArrayEquals(expected, nullPropertyPointer.getPropertyNames());
    }
    
    @Test
    public void testAsPath() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("**", nullPropertyPointer.asPath());
    }
}